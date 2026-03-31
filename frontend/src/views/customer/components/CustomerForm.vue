<template>
  <el-dialog
    :model-value="visible"
    :title="dialogTitle"
    width="800px"
    :close-on-click-modal="false"
    @update:model-value="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      size="default"
    >
      <el-row :gutter="24">
        <el-col :span="12">
          <el-form-item label="企业名称" prop="companyName">
            <el-input
              v-model="formData.companyName"
              placeholder="请输入企业名称"
              maxlength="200"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="企业简称" prop="shortName">
            <el-input
              v-model="formData.shortName"
              placeholder="请输入企业简称"
              maxlength="100"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="行业" prop="industry">
            <el-select
              v-model="formData.industry"
              placeholder="请选择行业"
              style="width: 100%"
            >
              <el-option label="人工智能" value="人工智能" />
              <el-option label="智能制造" value="智能制造" />
              <el-option label="智慧城市" value="智慧城市" />
              <el-option label="自动驾驶" value="自动驾驶" />
              <el-option label="物联网" value="物联网" />
              <el-option label="其他" value="其他" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="子行业" prop="subIndustry">
            <el-input
              v-model="formData.subIndustry"
              placeholder="请输入子行业"
              maxlength="50"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="企业规模" prop="companyScale">
            <el-select
              v-model="formData.companyScale"
              placeholder="请选择企业规模"
              style="width: 100%"
            >
              <el-option label="1-50人" value="1-50人" />
              <el-option label="50-150人" value="50-150人" />
              <el-option label="150-500人" value="150-500人" />
              <el-option label="500-1000人" value="500-1000人" />
              <el-option label="1000人以上" value="1000人以上" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="企业官网" prop="website">
            <el-input
              v-model="formData.website"
              placeholder="请输入企业官网"
              maxlength="255"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="省份" prop="province">
            <el-select
              v-model="formData.province"
              placeholder="请选择省份"
              style="width: 100%"
              @change="handleProvinceChange"
            >
              <el-option label="广东省" value="广东省" />
              <el-option label="北京市" value="北京市" />
              <el-option label="上海市" value="上海市" />
              <el-option label="浙江省" value="浙江省" />
              <el-option label="江苏省" value="江苏省" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="城市" prop="city">
            <el-select
              v-model="formData.city"
              placeholder="请选择城市"
              style="width: 100%"
            >
              <el-option label="深圳市" value="深圳市" />
              <el-option label="广州市" value="广州市" />
              <el-option label="北京市" value="北京市" />
              <el-option label="上海市" value="上海市" />
              <el-option label="杭州市" value="杭州市" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="企业地址" prop="address">
            <el-input
              v-model="formData.address"
              placeholder="请输入企业地址"
              maxlength="500"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="客户等级" prop="customerLevel">
            <el-select
              v-model="formData.customerLevel"
              placeholder="请选择客户等级"
              style="width: 100%"
            >
              <el-option label="A类(战略客户)" value="A" />
              <el-option label="B类(重点客户)" value="B" />
              <el-option label="C类(潜力客户)" value="C" />
              <el-option label="D类(一般客户)" value="D" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="客户状态" prop="customerStatus">
            <el-select
              v-model="formData.customerStatus"
              placeholder="请选择客户状态"
              style="width: 100%"
            >
              <el-option label="潜在客户" value="potential" />
              <el-option label="意向客户" value="intentional" />
              <el-option label="成交客户" value="won" />
              <el-option label="流失客户" value="lost" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="客户来源" prop="customerSource">
            <el-select
              v-model="formData.customerSource"
              placeholder="请选择客户来源"
              style="width: 100%"
            >
              <el-option label="展会" value="展会" />
              <el-option label="网站" value="网站" />
              <el-option label="电话营销" value="电话营销" />
              <el-option label="客户介绍" value="客户介绍" />
              <el-option label="合作伙伴" value="合作伙伴" />
              <el-option label="其他" value="其他" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="下次跟进时间" prop="nextFollowTime">
            <el-date-picker
              v-model="formData.nextFollowTime"
              type="datetime"
              placeholder="请选择下次跟进时间"
              style="width: 100%"
              value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="标签" prop="tags">
            <el-select
              v-model="formData.tags"
              multiple
              filterable
              allow-create
              default-first-option
              placeholder="请选择或输入标签"
              style="width: 100%"
            >
              <el-option label="AI芯片" value="AI芯片" />
              <el-option label="边缘计算" value="边缘计算" />
              <el-option label="重点跟进" value="重点跟进" />
              <el-option label="高意向" value="高意向" />
              <el-option label="待跟进" value="待跟进" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="24">
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
        </el-col>
      </el-row>
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
import type { CustomerCreateRequest, CustomerUpdateRequest, Customer } from '@/types/customer'

// Props
interface Props {
  visible: boolean
  customerId?: string
  mode: 'create' | 'edit'
}

const props = withDefaults(defineProps<Props>(), {
  visible: false,
  customerId: '',
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
const formData = reactive<CustomerCreateRequest>({
  companyName: '',
  shortName: '',
  industry: '',
  subIndustry: '',
  companyScale: '',
  province: '',
  city: '',
  district: '',
  address: '',
  website: '',
  customerLevel: 'D',
  customerStatus: 'potential',
  customerSource: '',
  tags: [],
  remark: '',
  nextFollowTime: ''
})

// 表单验证规则
const formRules: FormRules = {
  companyName: [
    { required: true, message: '请输入企业名称', trigger: 'blur' },
    { max: 200, message: '企业名称不能超过200个字符', trigger: 'blur' }
  ],
  industry: [
    { required: true, message: '请选择行业', trigger: 'change' }
  ],
  customerLevel: [
    { required: true, message: '请选择客户等级', trigger: 'change' }
  ],
  customerStatus: [
    { required: true, message: '请选择客户状态', trigger: 'change' }
  ],
  website: [
    { type: 'url', message: '请输入正确的网址格式', trigger: 'blur' }
  ]
}

// 弹窗标题
const dialogTitle = computed(() => {
  return props.mode === 'create' ? '新建客户' : '编辑客户'
})

// 监听visible变化
watch(() => props.visible, (val) => {
  if (val) {
    if (props.mode === 'edit' && props.customerId) {
      loadCustomerDetail()
    } else {
      resetForm()
    }
  }
})

// 加载客户详情
const loadCustomerDetail = async () => {
  try {
    const response = await customerApi.getCustomerDetail(props.customerId!)
    if (response.code === 200) {
      const customer = response.data
      Object.assign(formData, {
        companyName: customer.companyName,
        shortName: customer.shortName,
        industry: customer.industry,
        subIndustry: customer.subIndustry,
        companyScale: customer.companyScale,
        province: customer.province,
        city: customer.city,
        district: customer.district,
        address: customer.address,
        website: customer.website,
        customerLevel: customer.customerLevel,
        customerStatus: customer.customerStatus,
        customerSource: customer.customerSource,
        tags: customer.tags || [],
        remark: customer.remark,
        nextFollowTime: customer.nextFollowTime
      })
    }
  } catch (error) {
    console.error('加载客户详情失败:', error)
  }
}

// 省份变化
const handleProvinceChange = () => {
  formData.city = ''
}

// 重置表单
const resetForm = () => {
  // 重置为初始值
  Object.assign(formData, {
    companyName: '',
    shortName: '',
    industry: '',
    subIndustry: '',
    companyScale: '',
    province: '',
    city: '',
    district: '',
    address: '',
    website: '',
    customerLevel: 'D',
    customerStatus: 'potential',
    customerSource: '',
    tags: [],
    remark: '',
    nextFollowTime: ''
  })
  // 清除表单验证状态
  formRef.value?.clearValidate()
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
          await customerApi.createCustomer(formData)
          ElMessage.success('创建成功')
        } else {
          const updateData: CustomerUpdateRequest = {
            id: props.customerId!,
            ...formData
          }
          await customerApi.updateCustomer(props.customerId!, updateData)
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
