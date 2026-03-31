<template>
  <div class="pool-rules-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="page-header__left">
        <el-button link @click="handleBack">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>
        <h2 class="page-title">公海池规则管理</h2>
      </div>
      <div class="page-header__right">
        <el-button type="primary" @click="handleCreate">
          <el-icon><Plus /></el-icon>
          新建规则
        </el-button>
      </div>
    </div>

    <!-- 规则说明卡片 -->
    <el-card class="info-card">
      <div class="info-card__content">
        <el-icon class="info-icon" :size="24" color="#3B82F6"><InfoFilled /></el-icon>
        <div class="info-text">
          <h4>公海池规则说明</h4>
          <p>公海池规则用于自动将符合条件的客户放入公海池。系统会根据规则的优先级和条件自动执行，优先级数字越小优先级越高。</p>
        </div>
      </div>
    </el-card>

    <!-- 规则列表 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="ruleList"
        border
        stripe
      >
        <el-table-column prop="ruleName" label="规则名称" min-width="150" />

        <el-table-column prop="ruleCode" label="规则编码" width="150" />

        <el-table-column prop="ruleType" label="规则类型" width="150">
          <template #default="{ row }">
            <el-tag :type="getRuleTypeTagType(row.ruleType)">
              {{ getRuleTypeText(row.ruleType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="conditionDays" label="条件天数" width="120" align="center">
          <template #default="{ row }">
            {{ row.conditionDays ? `${row.conditionDays}天` : '-' }}
          </template>
        </el-table-column>

        <el-table-column prop="priority" label="优先级" width="100" align="center">
          <template #default="{ row }">
            <el-tag effect="plain">{{ row.priority }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>

        <el-table-column prop="description" label="规则描述" min-width="200" show-overflow-tooltip />

        <el-table-column prop="createdTime" label="创建时间" width="160" />

        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="danger" link @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 规则表单弹窗 -->
    <el-dialog
      v-model="formVisible"
      :title="formMode === 'create' ? '新建规则' : '编辑规则'"
      width="600px"
      @close="handleFormClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
      >
        <el-form-item label="规则名称" prop="ruleName">
          <el-input
            v-model="formData.ruleName"
            placeholder="请输入规则名称"
            maxlength="50"
          />
        </el-form-item>

        <el-form-item label="规则编码" prop="ruleCode">
          <el-input
            v-model="formData.ruleCode"
            placeholder="请输入规则编码"
            maxlength="50"
            :disabled="formMode === 'edit'"
          />
        </el-form-item>

        <el-form-item label="规则类型" prop="ruleType">
          <el-select
            v-model="formData.ruleType"
            placeholder="请选择规则类型"
            style="width: 100%"
            :disabled="formMode === 'edit'"
          >
            <el-option label="长期未跟进" value="no_follow" />
            <el-option label="长期无商机" value="no_opportunity" />
            <el-option label="长期未成交" value="no_deal" />
            <el-option label="客户流失" value="customer_lost" />
            <el-option label="手动放入" value="manual" />
          </el-select>
        </el-form-item>

        <el-form-item
          v-if="formData.ruleType === 'no_follow'"
          label="未跟进天数"
          prop="conditionDays"
        >
          <el-input-number
            v-model="formData.conditionDays"
            :min="1"
            :max="365"
            placeholder="请输入天数"
            style="width: 100%"
          />
          <div class="form-hint">超过该天数未跟进的客户将自动进入公海池</div>
        </el-form-item>

        <el-form-item
          v-if="formData.ruleType === 'no_opportunity'"
          label="无商机天数"
          prop="conditionDays"
        >
          <el-input-number
            v-model="formData.conditionDays"
            :min="1"
            :max="365"
            placeholder="请输入天数"
            style="width: 100%"
          />
          <div class="form-hint">超过该天数无商机的客户将自动进入公海池</div>
        </el-form-item>

        <el-form-item
          v-if="formData.ruleType === 'no_deal'"
          label="未成交天数"
          prop="conditionDays"
        >
          <el-input-number
            v-model="formData.conditionDays"
            :min="1"
            :max="365"
            placeholder="请输入天数"
            style="width: 100%"
          />
          <div class="form-hint">超过该天数未成交的客户将自动进入公海池</div>
        </el-form-item>

        <el-form-item label="优先级" prop="priority">
          <el-input-number
            v-model="formData.priority"
            :min="1"
            :max="1000"
            placeholder="请输入优先级"
            style="width: 100%"
          />
          <div class="form-hint">数字越小优先级越高</div>
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="规则描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入规则描述"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注"
            maxlength="500"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  ArrowLeft,
  Plus,
  InfoFilled
} from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import { poolApi } from '@/api/pool'
import type {
  PoolRule,
  PoolRuleCreateRequest,
  PoolRuleUpdateRequest,
  PoolRuleType
} from '@/types/pool'

const router = useRouter()

// 加载状态
const loading = ref(false)

// 规则列表
const ruleList = ref<PoolRule[]>([])

// 分页
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 表单弹窗
const formVisible = ref(false)
const formMode = ref<'create' | 'edit'>('create')
const formRef = ref<FormInstance>()
const submitLoading = ref(false)
const currentRuleId = ref('')

// 表单数据
const formData = reactive<PoolRuleCreateRequest>({
  ruleName: '',
  ruleCode: '',
  ruleType: 'no_follow' as PoolRuleType,
  conditionDays: 30,
  priority: 100,
  status: 1,
  description: '',
  remark: ''
})

// 自定义验证规则 - 条件天数
const validateConditionDays = (rule: any, value: number, callback: any) => {
  // 手动放入类型不需要条件天数
  if (formData.ruleType === 'manual') {
    callback()
    return
  }
  
  if (!value && value !== 0) {
    callback(new Error('请输入条件天数'))
  } else if (value < 1 || value > 365) {
    callback(new Error('条件天数必须在1-365之间'))
  } else {
    callback()
  }
}

// 表单验证规则
const formRules: FormRules = {
  ruleName: [
    { required: true, message: '请输入规则名称', trigger: 'blur' },
    { max: 50, message: '规则名称不能超过50个字符', trigger: 'blur' },
    { min: 2, message: '规则名称至少2个字符', trigger: 'blur' }
  ],
  ruleCode: [
    { required: true, message: '请输入规则编码', trigger: 'blur' },
    { max: 50, message: '规则编码不能超过50个字符', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9_]+$/, message: '规则编码只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  ruleType: [
    { required: true, message: '请选择规则类型', trigger: 'change' }
  ],
  conditionDays: [
    { validator: validateConditionDays, trigger: 'blur' }
  ],
  priority: [
    { required: true, message: '请输入优先级', trigger: 'blur' },
    { type: 'number', min: 1, max: 1000, message: '优先级必须在1-1000之间', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 获取规则列表
const getRuleList = async () => {
  loading.value = true
  try {
    const response = await poolApi.getPoolRuleList({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    if (response.code === 200) {
      ruleList.value = response.data.list
      pagination.total = response.data.total
    }
  } catch (error) {
    console.error('获取规则列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 返回
const handleBack = () => {
  router.push('/customer/pool')
}

// 新建
const handleCreate = () => {
  formMode.value = 'create'
  currentRuleId.value = ''
  resetForm()
  formVisible.value = true
}

// 编辑
const handleEdit = (row: PoolRule) => {
  formMode.value = 'edit'
  currentRuleId.value = row.id
  Object.assign(formData, {
    ruleName: row.ruleName,
    ruleCode: row.ruleCode,
    ruleType: row.ruleType,
    conditionDays: row.conditionDays,
    conditionCount: row.conditionCount,
    conditionAmount: row.conditionAmount,
    priority: row.priority,
    status: row.status,
    description: row.description,
    remark: row.remark
  })
  formVisible.value = true
}

// 删除
const handleDelete = (row: PoolRule) => {
  ElMessageBox.confirm(
    `确定要删除规则"${row.ruleName}"吗?`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await poolApi.deletePoolRule(row.id)
      ElMessage.success('删除成功')
      getRuleList()
    } catch (error) {
      console.error('删除规则失败:', error)
      ElMessage.error('删除规则失败,请稍后重试')
    }
  }).catch(() => {
    // 用户取消删除
  })
}

// 状态变更
const handleStatusChange = async (row: PoolRule) => {
  const oldStatus = row.status === 1 ? 0 : 1
  try {
    if (row.status === 1) {
      await poolApi.enablePoolRule(row.id)
      ElMessage.success('启用成功')
    } else {
      await poolApi.disablePoolRule(row.id)
      ElMessage.success('禁用成功')
    }
  } catch (error) {
    console.error('状态变更失败:', error)
    ElMessage.error('状态变更失败,请稍后重试')
    // 恢复原状态
    row.status = oldStatus
  }
}

// 表单关闭
const handleFormClose = () => {
  formRef.value?.resetFields()
  resetForm()
}

// 重置表单
const resetForm = () => {
  Object.assign(formData, {
    ruleName: '',
    ruleCode: '',
    ruleType: 'no_follow',
    conditionDays: 30,
    priority: 100,
    status: 1,
    description: '',
    remark: ''
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (formMode.value === 'create') {
          await poolApi.createPoolRule(formData)
          ElMessage.success('创建成功')
        } else {
          const updateData: PoolRuleUpdateRequest = {
            id: currentRuleId.value,
            ...formData
          }
          await poolApi.updatePoolRule(currentRuleId.value, updateData)
          ElMessage.success('更新成功')
        }
        formVisible.value = false
        getRuleList()
      } catch (error) {
        console.error('提交失败:', error)
        ElMessage.error(formMode.value === 'create' ? '创建规则失败,请稍后重试' : '更新规则失败,请稍后重试')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  getRuleList()
}

// 当前页变化
const handleCurrentChange = (page: number) => {
  pagination.pageNum = page
  getRuleList()
}

// 获取规则类型标签类型
const getRuleTypeTagType = (type: PoolRuleType): '' | 'success' | 'warning' | 'info' | 'danger' => {
  const typeMap: Record<string, '' | 'success' | 'warning' | 'info' | 'danger'> = {
    no_follow: 'warning',
    no_opportunity: 'info',
    no_deal: 'danger',
    customer_lost: 'danger',
    manual: ''
  }
  return typeMap[type] || ''
}

// 获取规则类型文本
const getRuleTypeText = (type: PoolRuleType): string => {
  const textMap: Record<string, string> = {
    no_follow: '长期未跟进',
    no_opportunity: '长期无商机',
    no_deal: '长期未成交',
    customer_lost: '客户流失',
    manual: '手动放入'
  }
  return textMap[type] || type
}

// 初始化
onMounted(() => {
  getRuleList()
})
</script>

<style lang="scss" scoped>
.pool-rules-container {
  padding: var(--page-padding);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;

  .page-header__left {
    display: flex;
    align-items: center;
    gap: 12px;
  }

  .page-title {
    font-size: 24px;
    font-weight: 700;
    color: var(--color-text-primary);
    margin: 0;
  }
}

.info-card {
  margin-bottom: 16px;

  :deep(.el-card__body) {
    padding: 16px 20px;
  }

  .info-card__content {
    display: flex;
    align-items: flex-start;
    gap: 12px;
  }

  .info-icon {
    flex-shrink: 0;
    margin-top: 2px;
  }

  .info-text {
    h4 {
      font-size: 14px;
      font-weight: 600;
      color: var(--color-text-primary);
      margin: 0 0 8px 0;
    }

    p {
      font-size: 13px;
      color: var(--color-text-secondary);
      margin: 0;
      line-height: 1.6;
    }
  }
}

.table-card {
  :deep(.el-card__body) {
    padding: 0;
  }

  .pagination {
    display: flex;
    justify-content: flex-end;
    padding: 16px 20px;
  }
}

.form-hint {
  font-size: 12px;
  color: var(--color-text-tertiary);
  margin-top: 4px;
  line-height: 1.5;
}
</style>
