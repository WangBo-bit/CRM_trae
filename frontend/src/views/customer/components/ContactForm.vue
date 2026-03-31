<template>
  <el-dialog
    :model-value="visible"
    :title="dialogTitle"
    width="600px"
    :close-on-click-modal="false"
    @update:model-value="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      size="default"
    >
      <el-form-item label="姓名" prop="name">
        <el-input
          v-model="formData.name"
          placeholder="请输入姓名"
          maxlength="50"
        />
      </el-form-item>

      <el-form-item label="性别" prop="gender">
        <el-radio-group v-model="formData.gender">
          <el-radio :value="1">男</el-radio>
          <el-radio :value="2">女</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="职位" prop="position">
        <el-input
          v-model="formData.position"
          placeholder="请输入职位"
          maxlength="50"
        />
      </el-form-item>

      <el-form-item label="部门" prop="department">
        <el-input
          v-model="formData.department"
          placeholder="请输入部门"
          maxlength="100"
        />
      </el-form-item>

      <el-form-item label="电话" prop="phone">
        <el-input
          v-model="formData.phone"
          placeholder="请输入电话"
          maxlength="20"
        />
      </el-form-item>

      <el-form-item label="手机" prop="mobile">
        <el-input
          v-model="formData.mobile"
          placeholder="请输入手机"
          maxlength="20"
        />
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input
          v-model="formData.email"
          placeholder="请输入邮箱"
          maxlength="100"
        />
      </el-form-item>

      <el-form-item label="微信号" prop="wechat">
        <el-input
          v-model="formData.wechat"
          placeholder="请输入微信号"
          maxlength="50"
        />
      </el-form-item>

      <el-form-item label="QQ号" prop="qq">
        <el-input
          v-model="formData.qq"
          placeholder="请输入QQ号"
          maxlength="20"
        />
      </el-form-item>

      <el-form-item label="决策层级" prop="decisionLevel">
        <el-select
          v-model="formData.decisionLevel"
          placeholder="请选择决策层级"
          style="width: 100%"
        >
          <el-option label="决策者" value="decision_maker" />
          <el-option label="影响者" value="influencer" />
          <el-option label="执行者" value="executor" />
        </el-select>
      </el-form-item>

      <el-form-item label="主要联系人" prop="isPrimary">
        <el-switch v-model="isPrimarySwitch" />
      </el-form-item>

      <el-form-item label="生日" prop="birthday">
        <el-date-picker
          v-model="formData.birthday"
          type="date"
          placeholder="请选择生日"
          style="width: 100%"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>

      <el-form-item label="爱好" prop="hobby">
        <el-input
          v-model="formData.hobby"
          placeholder="请输入爱好"
          maxlength="500"
        />
      </el-form-item>

      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { customerApi } from '@/api/customer'
import type { ContactCreateRequest, ContactUpdateRequest, Contact } from '@/types/customer'

// Props
interface Props {
  visible: boolean
  customerId: string
  contactId?: string
  mode: 'create' | 'edit'
}

const props = withDefaults(defineProps<Props>(), {
  visible: false,
  contactId: '',
  mode: 'create'
})

// Emits
const emit = defineEmits<{
  'update:visible': [value: boolean]
  success: []
}>()

// 表单引用
const formRef = ref<FormInstance>()
const submitLoading = ref(false)

// 表单数据
const formData = reactive<ContactCreateRequest>({
  customerId: '',
  name: '',
  gender: 0,
  position: '',
  department: '',
  phone: '',
  mobile: '',
  email: '',
  wechat: '',
  qq: '',
  decisionLevel: 'executor',
  isPrimary: 0,
  birthday: '',
  hobby: '',
  remark: ''
})

// 主要联系人开关
const isPrimarySwitch = computed({
  get: () => formData.isPrimary === 1,
  set: (val: boolean) => {
    formData.isPrimary = val ? 1 : 0
  }
})

// 表单验证规则
const formRules: FormRules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { max: 50, message: '姓名不能超过50个字符', trigger: 'blur' }
  ],
  mobile: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

// 弹窗标题
const dialogTitle = computed(() => {
  return props.mode === 'create' ? '新建联系人' : '编辑联系人'
})

// 监听visible变化
watch(() => props.visible, (val) => {
  if (val) {
    formData.customerId = props.customerId
    if (props.mode === 'edit' && props.contactId) {
      loadContactDetail()
    } else {
      resetForm()
    }
  }
})

// 加载联系人详情
const loadContactDetail = async () => {
  try {
    const response = await customerApi.getCustomerContacts(props.customerId)
    if (response.code === 200) {
      const contact = response.data.find((item: Contact) => item.id === props.contactId)
      if (contact) {
        Object.assign(formData, {
          customerId: contact.customerId,
          name: contact.name,
          gender: contact.gender,
          position: contact.position,
          department: contact.department,
          phone: contact.phone,
          mobile: contact.mobile,
          email: contact.email,
          wechat: contact.wechat,
          qq: contact.qq,
          decisionLevel: contact.decisionLevel,
          isPrimary: contact.isPrimary,
          birthday: contact.birthday,
          hobby: contact.hobby,
          remark: contact.remark
        })
      }
    }
  } catch (error) {
    console.error('加载联系人详情失败:', error)
  }
}

// 重置表单
const resetForm = () => {
  formRef.value?.resetFields()
  Object.assign(formData, {
    customerId: props.customerId,
    name: '',
    gender: 0,
    position: '',
    department: '',
    phone: '',
    mobile: '',
    email: '',
    wechat: '',
    qq: '',
    decisionLevel: 'executor',
    isPrimary: 0,
    birthday: '',
    hobby: '',
    remark: ''
  })
}

// 关闭弹窗
const handleClose = () => {
  emit('update:visible', false)
  resetForm()
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (props.mode === 'create') {
          await customerApi.createContact(props.customerId, formData)
          ElMessage.success('创建成功')
        } else {
          const updateData: ContactUpdateRequest = {
            id: props.contactId!,
            ...formData
          }
          await customerApi.updateContact(props.customerId, props.contactId!, updateData)
          ElMessage.success('更新成功')
        }
        emit('success')
        handleClose()
      } catch (error) {
        console.error('提交失败:', error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}
</script>

<style lang="scss" scoped>
:deep(.el-dialog__body) {
  padding: 20px 24px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}
</style>
