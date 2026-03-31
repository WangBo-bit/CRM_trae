<template>
  <el-dialog
    v-model="dialogVisible"
    title="新建跟进记录"
    width="640px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="100px"
      label-position="right"
    >
      <el-form-item label="跟进方式" prop="followType">
        <el-select v-model="formData.followType" placeholder="请选择跟进方式" style="width: 100%">
          <el-option label="电话" value="phone" />
          <el-option label="微信" value="wechat" />
          <el-option label="邮件" value="email" />
          <el-option label="上门" value="visit" />
          <el-option label="其他" value="other" />
        </el-select>
      </el-form-item>

      <el-form-item label="联系人" prop="contactId">
        <el-select 
          v-model="formData.contactId" 
          placeholder="请选择联系人" 
          style="width: 100%" 
          clearable
          :disabled="contacts.length === 0"
        >
          <el-option
            v-for="contact in contacts"
            :key="contact.id"
            :label="contact.name"
            :value="contact.id"
          >
            <span>{{ contact.name }}</span>
            <span style="float: right; color: var(--color-text-secondary); font-size: 12px">
              {{ contact.position }}
            </span>
          </el-option>
        </el-select>
        <div v-if="contacts.length === 0" class="contact-tip">
          <el-icon><Warning /></el-icon>
          <span>暂无联系人，请先添加联系人</span>
        </div>
      </el-form-item>

      <el-form-item label="跟进时间" prop="followTime">
        <el-date-picker
          v-model="formData.followTime"
          type="datetime"
          placeholder="选择跟进时间"
          style="width: 100%"
          format="YYYY-MM-DD HH:mm:ss"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>

      <el-form-item label="跟进内容" prop="followContent">
        <el-input
          v-model="formData.followContent"
          type="textarea"
          :rows="4"
          placeholder="请输入跟进内容"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>

      <el-form-item label="下次跟进时间">
        <el-date-picker
          v-model="formData.nextFollowTime"
          type="datetime"
          placeholder="选择下次跟进时间"
          style="width: 100%"
          format="YYYY-MM-DD HH:mm:ss"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>

      <el-form-item label="附件">
        <el-upload
          v-model:file-list="fileList"
          action="/api/v1/upload"
          :limit="5"
          :file-list="fileList"
          :before-upload="handleBeforeUpload"
          :on-success="handleUploadSuccess"
          :on-error="handleUploadError"
          :on-remove="handleUploadRemove"
          accept=".pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.txt,.jpg,.jpeg,.png,.gif,.zip,.rar"
        >
          <el-button type="primary">上传附件</el-button>
          <template #tip>
            <div class="el-upload__tip">
              支持PDF、Word、Excel、PPT、TXT、图片、压缩文件格式，最多上传5个文件，单个文件不超过10MB
            </div>
          </template>
        </el-upload>
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Warning } from '@element-plus/icons-vue'
import type { FormInstance, FormRules, UploadUserFile } from 'element-plus'
import { customerApi } from '@/api/customer'
import type { Contact, CustomerFollowCreateRequest } from '@/types/customer'

interface Props {
  visible: boolean
  customerId: string
  followId?: string
}

const props = defineProps<Props>()

const emit = defineEmits<{
  'update:visible': [value: boolean]
  success: []
}>()

const formRef = ref<FormInstance>()
const submitting = ref(false)
const contacts = ref<Contact[]>([])
const fileList = ref<UploadUserFile[]>([])

// 表单数据
const formData = ref<CustomerFollowCreateRequest>({
  customerId: props.customerId,
  followType: '',
  contactId: '',
  followTime: '',
  followContent: '',
  nextFollowTime: '',
  attachments: ''
})

// 表单验证规则
const rules: FormRules = {
  followType: [
    { required: true, message: '请选择跟进方式', trigger: 'change' }
  ],
  followTime: [
    { required: true, message: '请选择跟进时间', trigger: 'change' }
  ],
  followContent: [
    { required: true, message: '请输入跟进内容', trigger: 'blur' },
    { min: 10, max: 500, message: '跟进内容长度在10到500个字符', trigger: 'blur' }
  ],
  nextFollowTime: [
    {
      validator: (rule, value, callback) => {
        if (value && formData.value.followTime) {
          const nextTime = new Date(value).getTime()
          const followTime = new Date(formData.value.followTime).getTime()
          if (nextTime <= followTime) {
            callback(new Error('下次跟进时间必须大于当前跟进时间'))
          } else {
            callback()
          }
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ]
}

// 对话框显示状态
const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

// 监听visible变化
watch(() => props.visible, (val) => {
  if (val) {
    loadContacts()
    resetForm()
  }
})

// 加载联系人列表
const loadContacts = async () => {
  try {
    const response = await customerApi.getCustomerContacts(props.customerId)
    if (response.code === 200) {
      contacts.value = response.data
    }
  } catch (error) {
    console.error('加载联系人列表失败:', error)
  }
}

// 重置表单
const resetForm = () => {
  // 获取当前时间并格式化
  const now = new Date()
  const formatDateTime = (date: Date): string => {
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const hours = String(date.getHours()).padStart(2, '0')
    const minutes = String(date.getMinutes()).padStart(2, '0')
    const seconds = String(date.getSeconds()).padStart(2, '0')
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
  }
  
  formData.value = {
    customerId: props.customerId,
    followType: '',
    contactId: '',
    followTime: formatDateTime(now),
    followContent: '',
    nextFollowTime: '',
    attachments: ''
  }
  fileList.value = []
  formRef.value?.clearValidate()
}

// 允许上传的文件类型
const allowedFileTypes = [
  'application/pdf',
  'application/msword',
  'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
  'application/vnd.ms-excel',
  'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
  'application/vnd.ms-powerpoint',
  'application/vnd.openxmlformats-officedocument.presentationml.presentation',
  'text/plain',
  'image/jpeg',
  'image/jpg',
  'image/png',
  'image/gif',
  'application/zip',
  'application/x-rar-compressed',
  'application/x-zip-compressed'
]

// 最大文件大小 10MB
const maxFileSize = 10 * 1024 * 1024

// 上传前校验
const handleBeforeUpload = (file: File) => {
  // 校验文件类型
  if (!allowedFileTypes.includes(file.type)) {
    ElMessage.error('不支持的文件类型，请上传PDF、Word、Excel、PPT、TXT、图片或压缩文件')
    return false
  }
  
  // 校验文件大小
  if (file.size > maxFileSize) {
    ElMessage.error('文件大小不能超过10MB')
    return false
  }
  
  return true
}

// 上传成功
const handleUploadSuccess = (response: any, file: any) => {
  if (response.code === 200) {
    ElMessage.success('上传成功')
  } else {
    ElMessage.error(response.message || '上传失败')
    // 移除上传失败的文件
    const index = fileList.value.findIndex(f => f.uid === file.uid)
    if (index > -1) {
      fileList.value.splice(index, 1)
    }
  }
}

// 上传失败
const handleUploadError = (error: Error, file: any) => {
  console.error('文件上传失败:', error)
  ElMessage.error('文件上传失败，请重试')
  // 移除上传失败的文件
  const index = fileList.value.findIndex(f => f.uid === file.uid)
  if (index > -1) {
    fileList.value.splice(index, 1)
  }
}

// 删除附件
const handleUploadRemove = (file: any) => {
  // 处理删除附件逻辑
}

// 关闭对话框
const handleClose = () => {
  emit('update:visible', false)
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    submitting.value = true
    try {
      // 处理附件
      if (fileList.value.length > 0) {
        const attachments = fileList.value
          .filter(file => file.status === 'success')
          .map(file => {
            const url = file.response?.data?.url || file.url
            if (!url) {
              console.warn('文件URL获取失败:', file.name)
              return null
            }
            return {
              fileName: file.name,
              fileUrl: url
            }
          })
          .filter(Boolean)
        
        if (attachments.length > 0) {
          formData.value.attachments = JSON.stringify(attachments)
        }
      }

      const response = await customerApi.createFollow(props.customerId, formData.value)
      if (response.code === 200) {
        ElMessage.success('创建成功')
        emit('success')
        handleClose()
      }
    } catch (error) {
      console.error('创建跟进记录失败:', error)
      ElMessage.error('创建失败')
    } finally {
      submitting.value = false
    }
  })
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

:deep(.el-dialog__body) {
  padding: 20px 24px;
}

:deep(.el-upload__tip) {
  color: var(--color-text-secondary);
  font-size: 12px;
  margin-top: 8px;
}

.contact-tip {
  display: flex;
  align-items: center;
  gap: 4px;
  margin-top: 8px;
  padding: 8px 12px;
  background: rgba(245, 158, 11, 0.1);
  border-radius: 4px;
  font-size: 12px;
  color: var(--color-warning);
  
  .el-icon {
    font-size: 14px;
  }
}
</style>
