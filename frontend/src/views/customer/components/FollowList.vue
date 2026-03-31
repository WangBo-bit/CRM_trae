<template>
  <div class="follow-list-container">
    <!-- 操作栏 -->
    <div class="toolbar">
      <el-button type="primary" size="small" @click="handleCreate">
        <el-icon><Plus /></el-icon>
        新建跟进记录
      </el-button>
    </div>

    <!-- 跟进记录列表 -->
    <el-timeline v-loading="loading">
      <el-timeline-item
        v-for="item in followList"
        :key="item.id"
        :timestamp="item.followTime"
        placement="top"
      >
        <el-card>
          <div class="follow-item">
            <div class="follow-header">
              <div class="follow-info">
                <el-tag :type="getFollowTypeTagType(item.followType)" size="small">
                  {{ getFollowTypeText(item.followType) }}
                </el-tag>
                <span class="follow-user">{{ item.userName }}</span>
                <span v-if="item.contactName" class="follow-contact">
                  联系人: {{ item.contactName }}
                </span>
              </div>
              <el-button type="primary" link size="small" @click="handleDelete(item)">
                删除
              </el-button>
            </div>
            <div class="follow-content">{{ item.followContent }}</div>
            <div v-if="item.nextFollowTime" class="follow-next">
              下次跟进时间: {{ item.nextFollowTime }}
            </div>
          </div>
        </el-card>
      </el-timeline-item>
    </el-timeline>

    <!-- 空状态 -->
    <el-empty v-if="followList.length === 0" description="暂无跟进记录" />

    <!-- 跟进记录表单弹窗 -->
    <el-dialog
      v-model="formVisible"
      title="新建跟进记录"
      width="600px"
    >
      <el-form :model="followForm" label-width="100px">
        <el-form-item label="跟进方式">
          <el-select v-model="followForm.followType" placeholder="请选择跟进方式" style="width: 100%">
            <el-option label="电话" value="phone" />
            <el-option label="上门" value="visit" />
            <el-option label="微信" value="wechat" />
            <el-option label="邮件" value="email" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系人">
          <el-select v-model="followForm.contactId" placeholder="请选择联系人" style="width: 100%">
            <el-option
              v-for="contact in contactList"
              :key="contact.id"
              :label="contact.name"
              :value="contact.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="跟进内容">
          <el-input
            v-model="followForm.followContent"
            type="textarea"
            :rows="4"
            placeholder="请输入跟进内容"
          />
        </el-form-item>
        <el-form-item label="下次跟进时间">
          <el-date-picker
            v-model="followForm.nextFollowTime"
            type="datetime"
            placeholder="请选择下次跟进时间"
            style="width: 100%"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { customerApi } from '@/api/customer'
import type { CustomerFollow, CustomerFollowCreateRequest, Contact } from '@/types/customer'

// Props
interface Props {
  customerId: string
}

const props = defineProps<Props>()

// 跟进记录列表
const followList = ref<CustomerFollow[]>([])
const loading = ref(false)

// 联系人列表(用于下拉选择)
const contactList = ref<Contact[]>([])

// 表单弹窗
const formVisible = ref(false)
const followForm = reactive<CustomerFollowCreateRequest>({
  customerId: '',
  followType: 'phone',
  followContent: '',
  contactId: '',
  nextFollowTime: ''
})

// 获取跟进记录列表
const getFollowList = async () => {
  loading.value = true
  try {
    const response = await customerApi.getCustomerFollows(props.customerId)
    if (response.code === 200) {
      followList.value = response.data
    }
  } catch (error) {
    console.error('获取跟进记录列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 获取联系人列表
const getContactList = async () => {
  try {
    const response = await customerApi.getCustomerContacts(props.customerId)
    if (response.code === 200) {
      contactList.value = response.data
    }
  } catch (error) {
    console.error('获取联系人列表失败:', error)
  }
}

// 新建跟进记录
const handleCreate = () => {
  followForm.customerId = props.customerId
  followForm.followType = 'phone'
  followForm.followContent = ''
  followForm.contactId = ''
  followForm.nextFollowTime = ''
  formVisible.value = true
}

// 删除跟进记录
const handleDelete = (row: CustomerFollow) => {
  ElMessageBox.confirm(
    '确定要删除该跟进记录吗?',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await customerApi.deleteFollow(props.customerId, row.id)
      ElMessage.success('删除成功')
      getFollowList()
    } catch (error) {
      console.error('删除跟进记录失败:', error)
    }
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!followForm.followContent) {
    ElMessage.warning('请输入跟进内容')
    return
  }

  try {
    await customerApi.createFollow(props.customerId, followForm)
    ElMessage.success('创建成功')
    formVisible.value = false
    getFollowList()
  } catch (error) {
    console.error('创建跟进记录失败:', error)
  }
}

// 获取跟进方式标签类型
const getFollowTypeTagType = (type: string): '' | 'success' | 'warning' | 'info' | 'danger' | 'primary' => {
  const typeMap: Record<string, '' | 'success' | 'warning' | 'info' | 'danger' | 'primary'> = {
    phone: 'primary',
    visit: 'success',
    wechat: 'warning',
    email: 'info',
    other: ''
  }
  return typeMap[type] || ''
}

// 获取跟进方式文本
const getFollowTypeText = (type: string) => {
  const textMap: Record<string, string> = {
    phone: '电话',
    visit: '上门',
    wechat: '微信',
    email: '邮件',
    other: '其他'
  }
  return textMap[type] || type
}

// 监听customerId变化
watch(() => props.customerId, () => {
  if (props.customerId) {
    getFollowList()
    getContactList()
  }
}, { immediate: true })
</script>

<style lang="scss" scoped>
.follow-list-container {
  .toolbar {
    margin-bottom: 16px;
  }

  .follow-item {
    .follow-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12px;

      .follow-info {
        display: flex;
        align-items: center;
        gap: 12px;

        .follow-user {
          font-weight: 500;
          color: var(--color-text-primary);
        }

        .follow-contact {
          font-size: 13px;
          color: var(--color-text-secondary);
        }
      }
    }

    .follow-content {
      font-size: 14px;
      line-height: 1.6;
      color: var(--color-text-primary);
      margin-bottom: 12px;
    }

    .follow-next {
      font-size: 13px;
      color: var(--color-text-secondary);
    }
  }
}
</style>
