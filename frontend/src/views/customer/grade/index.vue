<template>
  <div class="grade-rule-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">客户分级规则管理</h2>
    </div>

    <!-- 搜索表单 -->
    <el-card class="search-card">
      <el-form :model="searchForm" inline size="default">
        <el-form-item label="规则名称">
          <el-input
            v-model="searchForm.ruleName"
            placeholder="请输入规则名称"
            clearable
            style="width: 200px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="规则类型">
          <el-select
            v-model="searchForm.ruleType"
            placeholder="请选择类型"
            clearable
            style="width: 150px"
          >
            <el-option label="采购金额" value="purchase_amount" />
            <el-option label="采购频次" value="purchase_frequency" />
            <el-option label="商机金额" value="opportunity_amount" />
            <el-option label="POC数量" value="poc_count" />
            <el-option label="跟进次数" value="follow_count" />
            <el-option label="合作时长" value="cooperation_duration" />
            <el-option label="自定义规则" value="custom" />
          </el-select>
        </el-form-item>

        <el-form-item label="目标等级">
          <el-select
            v-model="searchForm.targetLevel"
            placeholder="请选择等级"
            clearable
            style="width: 120px"
          >
            <el-option label="A类" value="A" />
            <el-option label="B类" value="B" />
            <el-option label="C类" value="C" />
            <el-option label="D类" value="D" />
          </el-select>
        </el-form-item>

        <el-form-item label="状态">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="toolbar-card">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button type="primary" @click="handleCreate">
            <el-icon><Plus /></el-icon>
            新建规则
          </el-button>
          <el-button
            type="danger"
            :disabled="selectedRows.length === 0"
            @click="handleBatchDelete"
          >
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
        </div>
        <div class="toolbar-right">
          <el-button @click="handleBatchCalculate">
            <el-icon><RefreshRight /></el-icon>
            批量计算等级
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 规则列表表格 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="ruleList"
        border
        stripe
        row-key="id"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />

        <el-table-column prop="ruleName" label="规则名称" min-width="180">
          <template #default="{ row }">
            <div class="rule-name">
              <span>{{ row.ruleName }}</span>
              <el-tag v-if="row.priority <= 10" type="danger" size="small" effect="plain">
                高优先级
              </el-tag>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="ruleCode" label="规则编码" width="150" />

        <el-table-column prop="ruleType" label="规则类型" width="120" align="center">
          <template #default="{ row }">
            <el-tag effect="plain">
              {{ getRuleTypeText(row.ruleType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="targetLevel" label="目标等级" width="100" align="center">
          <template #default="{ row }">
            <el-tag
              :type="getLevelTagType(row.targetLevel)"
              effect="plain"
            >
              {{ row.targetLevel }}类
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="priority" label="优先级" width="100" align="center">
          <template #default="{ row }">
            <el-input-number
              v-model="row.priority"
              :min="1"
              :max="999"
              size="small"
              controls-position="right"
              @change="handlePriorityChange(row)"
            />
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

        <el-table-column prop="createdTime" label="创建时间" width="160" />

        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="primary" link @click="handleViewConditions(row)">
              查看条件
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

    <!-- 新建/编辑规则弹窗 -->
    <el-dialog
      v-model="formVisible"
      :title="formMode === 'create' ? '新建分级规则' : '编辑分级规则'"
      width="800px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="ruleForm"
        :rules="formRules"
        label-width="100px"
        size="default"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="规则名称" prop="ruleName">
              <el-input
                v-model="ruleForm.ruleName"
                placeholder="请输入规则名称"
                maxlength="50"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规则编码" prop="ruleCode">
              <el-input
                v-model="ruleForm.ruleCode"
                placeholder="请输入规则编码"
                maxlength="50"
                :disabled="formMode === 'edit'"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="规则类型" prop="ruleType">
              <el-select
                v-model="ruleForm.ruleType"
                placeholder="请选择规则类型"
                style="width: 100%"
              >
                <el-option label="采购金额" value="purchase_amount" />
                <el-option label="采购频次" value="purchase_frequency" />
                <el-option label="商机金额" value="opportunity_amount" />
                <el-option label="POC数量" value="poc_count" />
                <el-option label="跟进次数" value="follow_count" />
                <el-option label="合作时长" value="cooperation_duration" />
                <el-option label="自定义规则" value="custom" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标等级" prop="targetLevel">
              <el-select
                v-model="ruleForm.targetLevel"
                placeholder="请选择目标等级"
                style="width: 100%"
              >
                <el-option label="A类 - 战略客户" value="A" />
                <el-option label="B类 - 重点客户" value="B" />
                <el-option label="C类 - 潜力客户" value="C" />
                <el-option label="D类 - 一般客户" value="D" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-input-number
                v-model="ruleForm.priority"
                :min="1"
                :max="999"
                style="width: 100%"
              />
              <div class="form-tip">数字越小优先级越高</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="ruleForm.status">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="规则条件" prop="conditions">
          <div class="conditions-container">
            <div
              v-for="(condition, index) in ruleForm.conditions"
              :key="index"
              class="condition-item"
            >
              <el-row :gutter="10" align="middle">
                <el-col :span="6">
                  <el-select
                    v-model="condition.field"
                    placeholder="选择字段"
                    size="small"
                  >
                    <el-option label="采购金额" value="purchase_amount" />
                    <el-option label="采购频次" value="purchase_frequency" />
                    <el-option label="商机金额" value="opportunity_amount" />
                    <el-option label="POC数量" value="poc_count" />
                    <el-option label="跟进次数" value="follow_count" />
                    <el-option label="合作时长(月)" value="cooperation_duration" />
                  </el-select>
                </el-col>
                <el-col :span="5">
                  <el-select
                    v-model="condition.operator"
                    placeholder="操作符"
                    size="small"
                  >
                    <el-option label="大于" value="gt" />
                    <el-option label="大于等于" value="gte" />
                    <el-option label="小于" value="lt" />
                    <el-option label="小于等于" value="lte" />
                    <el-option label="等于" value="eq" />
                    <el-option label="不等于" value="neq" />
                    <el-option label="区间" value="between" />
                  </el-select>
                </el-col>
                <el-col :span="10">
                  <div v-if="condition.operator === 'between'" style="display: flex; gap: 8px;">
                    <el-input-number
                      v-model="condition.value[0]"
                      placeholder="最小值"
                      size="small"
                      style="width: 50%"
                    />
                    <el-input-number
                      v-model="condition.value[1]"
                      placeholder="最大值"
                      size="small"
                      style="width: 50%"
                    />
                  </div>
                  <el-input-number
                    v-else
                    v-model="condition.value"
                    placeholder="请输入值"
                    size="small"
                    style="width: 100%"
                  />
                </el-col>
                <el-col :span="3">
                  <el-button
                    type="danger"
                    :icon="Delete"
                    circle
                    size="small"
                    @click="removeCondition(index)"
                  />
                </el-col>
              </el-row>
            </div>
            <el-button
              type="primary"
              link
              :icon="Plus"
              @click="addCondition"
            >
              添加条件
            </el-button>
          </div>
        </el-form-item>

        <el-form-item label="规则描述" prop="description">
          <el-input
            v-model="ruleForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入规则描述"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="ruleForm.remark"
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

    <!-- 查看条件弹窗 -->
    <el-dialog
      v-model="conditionVisible"
      title="规则条件详情"
      width="600px"
    >
      <el-table :data="currentConditions" border stripe>
        <el-table-column prop="field" label="字段" width="150">
          <template #default="{ row }">
            {{ getFieldText(row.field) }}
          </template>
        </el-table-column>
        <el-table-column prop="operator" label="操作符" width="120">
          <template #default="{ row }">
            {{ getOperatorText(row.operator) }}
          </template>
        </el-table-column>
        <el-table-column prop="value" label="值">
          <template #default="{ row }">
            <span v-if="row.operator === 'between'">
              {{ row.value[0] }} ~ {{ row.value[1] }}
            </span>
            <span v-else>{{ row.value }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 批量计算弹窗 -->
    <el-dialog
      v-model="calculateVisible"
      title="批量计算客户等级"
      width="600px"
    >
      <el-alert
        title="提示"
        type="info"
        description="将根据已启用的分级规则，重新计算所有客户的等级"
        :closable="false"
        show-icon
        style="margin-bottom: 20px"
      />
      <el-form :model="calculateForm" label-width="100px">
        <el-form-item label="计算范围">
          <el-radio-group v-model="calculateForm.recalculateAll">
            <el-radio :label="true">计算所有客户</el-radio>
            <el-radio :label="false">计算指定客户</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="!calculateForm.recalculateAll" label="客户ID">
          <el-input
            v-model="calculateForm.customerIdsText"
            type="textarea"
            :rows="4"
            placeholder="请输入客户ID，多个ID用逗号分隔"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="calculateVisible = false">取消</el-button>
        <el-button type="primary" :loading="calculateLoading" @click="handleCalculateSubmit">
          开始计算
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox, FormInstance, FormRules } from 'element-plus'
import { Search, Refresh, Plus, Delete, RefreshRight } from '@element-plus/icons-vue'
import { customerApi } from '@/api/customer'
import type {
  CustomerGradeRule,
  CustomerGradeRuleCreateRequest,
  CustomerGradeRuleUpdateRequest,
  CustomerGradeRuleQueryParams,
  GradeRuleCondition
} from '@/types/customer'

// 搜索表单
const searchForm = reactive<CustomerGradeRuleQueryParams>({
  ruleName: '',
  ruleType: undefined,
  targetLevel: undefined,
  status: undefined
})

// 规则列表
const ruleList = ref<CustomerGradeRule[]>([])
const loading = ref(false)

// 分页
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 选中的行
const selectedRows = ref<CustomerGradeRule[]>([])

// 表单相关
const formVisible = ref(false)
const formRef = ref<FormInstance>()
const formMode = ref<'create' | 'edit'>('create')
const submitLoading = ref(false)
const ruleForm = reactive<CustomerGradeRuleCreateRequest>({
  ruleName: '',
  ruleCode: '',
  ruleType: 'purchase_amount',
  targetLevel: 'D',
  conditions: [],
  priority: 100,
  status: 1,
  description: '',
  remark: ''
})

// 表单验证规则
const formRules: FormRules = {
  ruleName: [
    { required: true, message: '请输入规则名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  ruleCode: [
    { required: true, message: '请输入规则编码', trigger: 'blur' },
    { pattern: /^[A-Z_]+$/, message: '只能包含大写字母和下划线', trigger: 'blur' }
  ],
  ruleType: [
    { required: true, message: '请选择规则类型', trigger: 'change' }
  ],
  targetLevel: [
    { required: true, message: '请选择目标等级', trigger: 'change' }
  ],
  conditions: [
    {
      validator: (rule, value, callback) => {
        if (!value || value.length === 0) {
          callback(new Error('请至少添加一个条件'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ]
}

// 查看条件相关
const conditionVisible = ref(false)
const currentConditions = ref<GradeRuleCondition[]>([])

// 批量计算相关
const calculateVisible = ref(false)
const calculateLoading = ref(false)
const calculateForm = reactive({
  recalculateAll: true,
  customerIdsText: ''
})

// 获取规则列表
const getRuleList = async () => {
  loading.value = true
  try {
    const params: CustomerGradeRuleQueryParams = {
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    const response = await customerApi.getGradeRuleList(params)
    if (response.code === 200) {
      ruleList.value = response.data.list
      pagination.total = response.data.total
    }
  } catch (error) {
    console.error('获取规则列表失败:', error)
    ElMessage.error('获取规则列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.pageNum = 1
  getRuleList()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    ruleName: '',
    ruleType: undefined,
    targetLevel: undefined,
    status: undefined
  })
  handleSearch()
}

// 新建规则
const handleCreate = () => {
  formMode.value = 'create'
  Object.assign(ruleForm, {
    ruleName: '',
    ruleCode: '',
    ruleType: 'purchase_amount',
    targetLevel: 'D',
    conditions: [],
    priority: 100,
    status: 1,
    description: '',
    remark: ''
  })
  formVisible.value = true
}

// 编辑规则
const handleEdit = (row: CustomerGradeRule) => {
  formMode.value = 'edit'
  Object.assign(ruleForm, {
    ruleName: row.ruleName,
    ruleCode: row.ruleCode,
    ruleType: row.ruleType,
    targetLevel: row.targetLevel,
    conditions: JSON.parse(JSON.stringify(row.conditions)),
    priority: row.priority,
    status: row.status,
    description: row.description,
    remark: row.remark,
    id: row.id
  })
  formVisible.value = true
}

// 添加条件
const addCondition = () => {
  ruleForm.conditions.push({
    field: 'purchase_amount',
    operator: 'gte',
    value: 0 as any // 初始化为数字，当operator为between时会动态切换为数组
  })
}

// 删除条件
const removeCondition = (index: number) => {
  ruleForm.conditions.splice(index, 1)
}

// 监听条件操作符变化，自动转换value类型
watch(
  () => ruleForm.conditions,
  (conditions) => {
    conditions.forEach((condition) => {
      if (condition.operator === 'between' && !Array.isArray(condition.value)) {
        // 从单值切换到区间，初始化数组
        condition.value = [0, 0] as any
      } else if (condition.operator !== 'between' && Array.isArray(condition.value)) {
        // 从区间切换到单值，取第一个值
        condition.value = condition.value[0] || 0
      }
    })
  },
  { deep: true }
)

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitLoading.value = true
    try {
      if (formMode.value === 'create') {
        await customerApi.createGradeRule(ruleForm)
        ElMessage.success('创建成功')
      } else {
        await customerApi.updateGradeRule((ruleForm as any).id, ruleForm as CustomerGradeRuleUpdateRequest)
        ElMessage.success('更新成功')
      }
      formVisible.value = false
      getRuleList()
    } catch (error) {
      console.error('保存规则失败:', error)
      ElMessage.error('保存规则失败')
    } finally {
      submitLoading.value = false
    }
  })
}

// 选择变化
const handleSelectionChange = (rows: CustomerGradeRule[]) => {
  selectedRows.value = rows
}

// 批量删除
const handleBatchDelete = () => {
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedRows.value.length} 个规则吗?`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const ids = selectedRows.value.map(row => row.id)
      await customerApi.batchDeleteGradeRules(ids)
      ElMessage.success('删除成功')
      getRuleList()
    } catch (error) {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  })
}

// 删除规则
const handleDelete = (row: CustomerGradeRule) => {
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
      await customerApi.deleteGradeRule(row.id)
      ElMessage.success('删除成功')
      getRuleList()
    } catch (error) {
      console.error('删除规则失败:', error)
      ElMessage.error('删除规则失败')
    }
  })
}

// 状态变化
const handleStatusChange = async (row: CustomerGradeRule) => {
  const oldStatus = row.status === 1 ? 0 : 1 // 保存旧状态用于回滚
  try {
    if (row.status === 1) {
      await customerApi.enableGradeRule(row.id)
      ElMessage.success('启用成功')
    } else {
      await customerApi.disableGradeRule(row.id)
      ElMessage.success('禁用成功')
    }
    getRuleList()
  } catch (error) {
    console.error('状态更新失败:', error)
    ElMessage.error('状态更新失败')
    // 恢复原状态
    row.status = oldStatus
  }
}

// 优先级变化
const handlePriorityChange = async (row: CustomerGradeRule) => {
  try {
    await customerApi.updateGradeRulePriority(row.id, row.priority)
    ElMessage.success('优先级更新成功')
  } catch (error) {
    console.error('优先级更新失败:', error)
    ElMessage.error('优先级更新失败')
  }
}

// 查看条件
const handleViewConditions = (row: CustomerGradeRule) => {
  currentConditions.value = row.conditions
  conditionVisible.value = true
}

// 批量计算
const handleBatchCalculate = () => {
  calculateForm.recalculateAll = true
  calculateForm.customerIdsText = ''
  calculateVisible.value = true
}

// 提交批量计算
const handleCalculateSubmit = async () => {
  calculateLoading.value = true
  try {
    const params: any = {
      recalculateAll: calculateForm.recalculateAll
    }
    if (!calculateForm.recalculateAll && calculateForm.customerIdsText) {
      // 解析并验证客户ID
      const ids = calculateForm.customerIdsText
        .split(',')
        .map(id => id.trim())
        .filter(id => id && id.length > 0)
      
      if (ids.length === 0) {
        ElMessage.warning('请输入至少一个有效的客户ID')
        calculateLoading.value = false
        return
      }
      
      params.customerIds = ids
    }
    const response = await customerApi.batchCalculateCustomerGrade(params)
    if (response.code === 200) {
      ElMessage.success(`计算完成: 成功 ${response.data.successCount} 个, 失败 ${response.data.failCount} 个`)
      calculateVisible.value = false
    }
  } catch (error) {
    console.error('批量计算失败:', error)
    ElMessage.error('批量计算失败')
  } finally {
    calculateLoading.value = false
  }
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

// 获取规则类型文本
const getRuleTypeText = (type: string): string => {
  const textMap: Record<string, string> = {
    purchase_amount: '采购金额',
    purchase_frequency: '采购频次',
    opportunity_amount: '商机金额',
    poc_count: 'POC数量',
    follow_count: '跟进次数',
    cooperation_duration: '合作时长',
    custom: '自定义'
  }
  return textMap[type] || type
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

// 获取字段文本
const getFieldText = (field: string): string => {
  const textMap: Record<string, string> = {
    purchase_amount: '采购金额',
    purchase_frequency: '采购频次',
    opportunity_amount: '商机金额',
    poc_count: 'POC数量',
    follow_count: '跟进次数',
    cooperation_duration: '合作时长'
  }
  return textMap[field] || field
}

// 获取操作符文本
const getOperatorText = (operator: string): string => {
  const textMap: Record<string, string> = {
    gt: '大于',
    gte: '大于等于',
    lt: '小于',
    lte: '小于等于',
    eq: '等于',
    neq: '不等于',
    between: '区间'
  }
  return textMap[operator] || operator
}

// 初始化
onMounted(() => {
  getRuleList()
})
</script>

<style lang="scss" scoped>
.grade-rule-container {
  padding: var(--page-padding);
}

.page-header {
  margin-bottom: 16px;

  .page-title {
    font-size: 24px;
    font-weight: 700;
    color: var(--color-text-primary);
    margin: 0;
  }
}

.search-card {
  margin-bottom: 16px;

  :deep(.el-card__body) {
    padding: 16px 20px 0;
  }

  .el-form-item {
    margin-bottom: 16px;
  }
}

.toolbar-card {
  margin-bottom: 16px;

  :deep(.el-card__body) {
    padding: 12px 20px;
  }

  .toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .toolbar-left,
    .toolbar-right {
      display: flex;
      gap: 12px;
    }
  }
}

.table-card {
  :deep(.el-card__body) {
    padding: 0;
  }

  .rule-name {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .pagination {
    display: flex;
    justify-content: flex-end;
    padding: 16px 20px;
  }
}

.conditions-container {
  width: 100%;

  .condition-item {
    margin-bottom: 12px;
    padding: 12px;
    background: #f9fafb;
    border-radius: 4px;
  }
}

.form-tip {
  font-size: 12px;
  color: #6b7280;
  margin-top: 4px;
}
</style>
