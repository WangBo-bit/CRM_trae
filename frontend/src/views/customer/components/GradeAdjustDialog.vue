<template>
  <el-dialog
    :model-value="visible"
    :title="dialogTitle"
    width="500px"
    :close-on-click-modal="false"
    destroy-on-close
    @update:model-value="handleClose"
  >
    <el-form
      ref="formRef"
      :model="adjustForm"
      :rules="formRules"
      label-width="100px"
      size="default"
    >
      <!-- 单个客户调整 -->
      <template v-if="mode === 'single'">
        <div class="customer-info">
          <div class="info-item">
            <span class="label">客户名称:</span>
            <span class="value">{{ customerName }}</span>
          </div>
          <div class="info-item">
            <span class="label">当前等级:</span>
            <span class="value">
              <el-tag :type="getLevelTagType(currentLevel)" effect="plain">
                {{ currentLevel }}类
              </el-tag>
            </span>
          </div>
        </div>

        <el-form-item label="新等级" prop="newLevel">
          <el-select
            v-model="adjustForm.newLevel"
            placeholder="请选择新等级"
            style="width: 100%"
          >
            <el-option label="A类 - 战略客户" value="A" />
            <el-option label="B类 - 重点客户" value="B" />
            <el-option label="C类 - 潜力客户" value="C" />
            <el-option label="D类 - 一般客户" value="D" />
          </el-select>
        </el-form-item>

        <el-form-item label="调整原因" prop="reason">
          <el-input
            v-model="adjustForm.reason"
            type="textarea"
            :rows="4"
            placeholder="请输入调整原因"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </template>

      <!-- 批量计算 -->
      <template v-else>
        <el-alert
          title="提示"
          type="info"
          :description="`将根据已启用的分级规则，重新计算选中的 ${selectedCount} 个客户的等级`"
          :closable="false"
          show-icon
          style="margin-bottom: 20px"
        />

        <el-form-item label="计算范围">
          <el-radio-group v-model="adjustForm.recalculateAll">
            <el-radio :label="false">计算选中客户</el-radio>
            <el-radio :label="true">计算所有客户</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item v-if="!adjustForm.recalculateAll" label="选中客户">
          <el-tag type="info">已选中 {{ selectedCount }} 个客户</el-tag>
        </el-form-item>
      </template>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" :loading="loading" @click="handleSubmit">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, watch, computed } from 'vue'
import { ElMessage, FormInstance, FormRules } from 'element-plus'
import { customerApi } from '@/api/customer'
import type { CustomerLevel } from '@/types/customer'

interface Props {
  visible: boolean
  mode: 'single' | 'batch'
  customerId?: string
  customerName?: string
  currentLevel?: CustomerLevel
  selectedCount?: number
  selectedIds?: string[]
}

interface Emits {
  (e: 'update:visible', value: boolean): void
  (e: 'success'): void
}

const props = withDefaults(defineProps<Props>(), {
  visible: false,
  mode: 'single',
  customerId: '',
  customerName: '',
  currentLevel: 'D',
  selectedCount: 0,
  selectedIds: () => []
})

const emit = defineEmits<Emits>()

const formRef = ref<FormInstance>()
const loading = ref(false)

const adjustForm = reactive({
  newLevel: '' as CustomerLevel,
  reason: '',
  recalculateAll: false
})

// 弹窗标题
const dialogTitle = computed(() => {
  return props.mode === 'single' ? '调整客户等级' : '批量计算客户等级'
})

// 表单验证规则
const formRules = computed<FormRules>(() => {
  if (props.mode === 'single') {
    return {
      newLevel: [
        { required: true, message: '请选择新等级', trigger: 'change' }
      ],
      reason: [
        { required: true, message: '请输入调整原因', trigger: 'blur' },
        { min: 5, max: 500, message: '长度在 5 到 500 个字符', trigger: 'blur' }
      ]
    }
  }
  return {}
})

// 监听visible变化，重置表单
watch(() => props.visible, (val) => {
  if (val) {
    if (props.mode === 'single') {
      adjustForm.newLevel = props.currentLevel || 'D'
      adjustForm.reason = ''
    } else {
      adjustForm.recalculateAll = false
    }
  }
})

// 关闭弹窗
const handleClose = () => {
  emit('update:visible', false)
}

// 提交表单
const handleSubmit = async () => {
  if (props.mode === 'single') {
    // 单个客户调整
    if (!formRef.value) return
    
    // 验证customerId是否存在
    if (!props.customerId) {
      ElMessage.error('客户ID不能为空')
      return
    }
    
    // 验证新等级是否与当前等级相同
    if (adjustForm.newLevel === props.currentLevel) {
      ElMessage.warning('新等级与当前等级相同，无需调整')
      return
    }
    
    await formRef.value.validate(async (valid) => {
      if (!valid) return
      
      loading.value = true
      try {
        await customerApi.adjustCustomerGrade({
          customerId: props.customerId!,
          newLevel: adjustForm.newLevel,
          reason: adjustForm.reason
        })
        ElMessage.success('等级调整成功')
        emit('success')
        handleClose()
      } catch (error) {
        console.error('等级调整失败:', error)
        ElMessage.error('等级调整失败')
      } finally {
        loading.value = false
      }
    })
  } else {
    // 批量计算
    loading.value = true
    try {
      const params: any = {
        recalculateAll: adjustForm.recalculateAll
      }
      if (!adjustForm.recalculateAll && props.selectedIds && props.selectedIds.length > 0) {
        params.customerIds = props.selectedIds
      }
      const response = await customerApi.batchCalculateCustomerGrade(params)
      if (response.code === 200) {
        ElMessage.success(`计算完成: 成功 ${response.data.successCount} 个, 失败 ${response.data.failCount} 个`)
        emit('success')
        handleClose()
      }
    } catch (error) {
      console.error('批量计算失败:', error)
      ElMessage.error('批量计算失败')
    } finally {
      loading.value = false
    }
  }
}

// 获取等级标签类型
const getLevelTagType = (level: string): '' | 'success' | 'warning' | 'info' | 'danger' => {
  const typeMap: Record<string, '' | 'success' | 'warning' | 'info' | 'danger'> = {
    A: 'danger',
    B: 'warning',
    C: '',
    D: 'info'
  }
  return typeMap[level] || ''
}
</script>

<style lang="scss" scoped>
.customer-info {
  padding: 12px 16px;
  background: #f9fafb;
  border-radius: 4px;
  margin-bottom: 16px;

  .info-item {
    display: flex;
    align-items: center;
    margin-bottom: 8px;

    &:last-child {
      margin-bottom: 0;
    }

    .label {
      width: 80px;
      color: #6b7280;
      font-size: 14px;
    }

    .value {
      flex: 1;
      font-size: 14px;
      color: #111827;
    }
  }
}
</style>
