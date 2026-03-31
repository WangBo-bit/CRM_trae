<template>
  <div class="customer-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">客户管理</h2>
    </div>

    <!-- 搜索表单 -->
    <el-card class="search-card">
      <el-form :model="searchForm" inline size="default">
        <el-form-item label="企业名称">
          <el-input
            v-model="searchForm.keyword"
            placeholder="请输入企业名称"
            clearable
            style="width: 200px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="行业">
          <el-select
            v-model="searchForm.industry"
            placeholder="请选择行业"
            clearable
            style="width: 150px"
          >
            <el-option label="人工智能" value="人工智能" />
            <el-option label="智能制造" value="智能制造" />
            <el-option label="智慧城市" value="智慧城市" />
            <el-option label="自动驾驶" value="自动驾驶" />
            <el-option label="物联网" value="物联网" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>

        <el-form-item label="客户等级">
          <el-select
            v-model="searchForm.customerLevel"
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

        <el-form-item label="客户状态">
          <el-select
            v-model="searchForm.customerStatus"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="潜在客户" value="potential" />
            <el-option label="意向客户" value="intentional" />
            <el-option label="成交客户" value="won" />
            <el-option label="流失客户" value="lost" />
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
            新建客户
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
          <el-button 
            type="warning"
            :disabled="selectedRows.length === 0"
            @click="handleBatchCalculateGrade"
          >
            <el-icon><RefreshRight /></el-icon>
            批量计算等级
          </el-button>
          <el-button @click="handleExport">
            <el-icon><Download /></el-icon>
            导出
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 客户列表表格 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="customerList"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />

        <el-table-column prop="customerCode" label="客户编码" width="150" />

        <el-table-column prop="companyName" label="企业名称" min-width="200">
          <template #default="{ row }">
            <el-link type="primary" @click="handleViewDetail(row.id)">
              {{ row.companyName }}
            </el-link>
          </template>
        </el-table-column>

        <el-table-column prop="industry" label="行业" width="120" />

        <el-table-column prop="customerLevel" label="客户等级" width="100" align="center">
          <template #default="{ row }">
            <el-tag
              :type="getLevelTagType(row.customerLevel)"
              effect="plain"
            >
              {{ row.customerLevel }}类
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="customerStatus" label="客户状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.customerStatus)">
              {{ getStatusText(row.customerStatus) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="ownerName" label="负责人" width="100" />

        <el-table-column prop="province" label="省份" width="100" />

        <el-table-column prop="city" label="城市" width="100" />

        <el-table-column prop="lastFollowTime" label="最后跟进时间" width="160" />

        <el-table-column prop="createdTime" label="创建时间" width="160" />

        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleViewDetail(row.id)">
              查看
            </el-button>
            <el-button type="primary" link @click="handleEdit(row.id)">
              编辑
            </el-button>
            <el-dropdown trigger="click" @command="(command: string) => handleCommand(command, row)">
              <el-button type="primary" link>
                更多<el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="adjustGrade">调整等级</el-dropdown-item>
                  <el-dropdown-item command="transfer">转移客户</el-dropdown-item>
                  <el-dropdown-item command="pool">放入公海池</el-dropdown-item>
                  <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
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

    <!-- 客户表单弹窗 -->
    <CustomerForm
      v-model:visible="formVisible"
      :customer-id="currentCustomerId"
      :mode="formMode"
      @success="handleFormSuccess"
    />

    <!-- 转移客户弹窗 -->
    <el-dialog
      v-model="transferVisible"
      title="转移客户"
      width="500px"
    >
      <el-form :model="transferForm" label-width="100px">
        <el-form-item label="新负责人">
          <el-select
            v-model="transferForm.newOwnerId"
            placeholder="请选择新负责人"
            style="width: 100%"
          >
            <el-option label="张明" value="user001" />
            <el-option label="李华" value="user002" />
            <el-option label="王芳" value="user003" />
          </el-select>
        </el-form-item>
        <el-form-item label="转移原因">
          <el-input
            v-model="transferForm.reason"
            type="textarea"
            :rows="3"
            placeholder="请输入转移原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="transferVisible = false">取消</el-button>
        <el-button type="primary" @click="handleTransferSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 等级调整弹窗 -->
    <GradeAdjustDialog
      v-model:visible="gradeAdjustVisible"
      :mode="gradeAdjustMode"
      :customer-id="currentCustomerId"
      :customer-name="currentCustomerName"
      :current-level="currentCustomerLevel"
      :selected-count="selectedRows.length"
      :selected-ids="selectedCustomerIds"
      @success="handleGradeAdjustSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Delete, Download, ArrowDown, RefreshRight } from '@element-plus/icons-vue'
import { customerApi } from '@/api/customer'
import type { Customer, CustomerQueryParams, CustomerTransferRequest } from '@/types/customer'
import CustomerForm from './components/CustomerForm.vue'
import GradeAdjustDialog from './components/GradeAdjustDialog.vue'

const router = useRouter()

// 搜索表单
const searchForm = reactive<CustomerQueryParams>({
  keyword: '',
  industry: '',
  customerLevel: undefined,
  customerStatus: undefined
})

// 客户列表
const customerList = ref<Customer[]>([])
const loading = ref(false)

// 分页
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 选中的行
const selectedRows = ref<Customer[]>([])

// 表单弹窗
const formVisible = ref(false)
const currentCustomerId = ref('')
const formMode = ref<'create' | 'edit'>('create')

// 转移客户弹窗
const transferVisible = ref(false)
const transferForm = reactive<CustomerTransferRequest>({
  customerId: '',
  newOwnerId: '',
  reason: ''
})

// 等级调整弹窗
const gradeAdjustVisible = ref(false)
const gradeAdjustMode = ref<'single' | 'batch'>('single')
const currentCustomerName = ref('')
const currentCustomerLevel = ref<'A' | 'B' | 'C' | 'D'>('D')
const selectedCustomerIds = computed(() => selectedRows.value.map(row => row.id))

// 获取客户列表
const getCustomerList = async () => {
  loading.value = true
  try {
    const params: CustomerQueryParams = {
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    const response = await customerApi.getCustomerList(params)
    if (response.code === 200) {
      customerList.value = response.data.list
      pagination.total = response.data.total
    }
  } catch (error) {
    console.error('获取客户列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.pageNum = 1
  getCustomerList()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    keyword: '',
    industry: '',
    customerLevel: undefined,
    customerStatus: undefined
  })
  handleSearch()
}

// 新建客户
const handleCreate = () => {
  currentCustomerId.value = ''
  formMode.value = 'create'
  formVisible.value = true
}

// 编辑客户
const handleEdit = (id: string) => {
  currentCustomerId.value = id
  formMode.value = 'edit'
  formVisible.value = true
}

// 查看详情
const handleViewDetail = (id: string) => {
  router.push(`/customer/detail/${id}`)
}

// 选择变化
const handleSelectionChange = (rows: Customer[]) => {
  selectedRows.value = rows
}

// 批量删除
const handleBatchDelete = () => {
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedRows.value.length} 个客户吗?`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const ids = selectedRows.value.map(row => row.id)
      await customerApi.batchDeleteCustomers(ids)
      ElMessage.success('删除成功')
      getCustomerList()
    } catch (error) {
      console.error('批量删除失败:', error)
    }
  })
}

// 导出
const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  getCustomerList()
}

// 当前页变化
const handleCurrentChange = (page: number) => {
  pagination.pageNum = page
  getCustomerList()
}

// 下拉菜单命令
const handleCommand = (command: string, row: Customer) => {
  switch (command) {
    case 'adjustGrade':
      handleAdjustGrade(row)
      break
    case 'transfer':
      handleTransfer(row)
      break
    case 'pool':
      handlePutToPool(row)
      break
    case 'delete':
      handleDelete(row)
      break
  }
}

// 调整客户等级
const handleAdjustGrade = (row: Customer) => {
  gradeAdjustMode.value = 'single'
  currentCustomerId.value = row.id
  currentCustomerName.value = row.companyName
  currentCustomerLevel.value = row.customerLevel
  gradeAdjustVisible.value = true
}

// 批量计算等级
const handleBatchCalculateGrade = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请先选择要计算的客户')
    return
  }
  gradeAdjustMode.value = 'batch'
  gradeAdjustVisible.value = true
}

// 等级调整成功
const handleGradeAdjustSuccess = () => {
  getCustomerList()
}

// 转移客户
const handleTransfer = (row: Customer) => {
  transferForm.customerId = row.id
  transferForm.newOwnerId = ''
  transferForm.reason = ''
  transferVisible.value = true
}

// 转移客户提交
const handleTransferSubmit = async () => {
  if (!transferForm.newOwnerId) {
    ElMessage.warning('请选择新负责人')
    return
  }
  try {
    await customerApi.transferCustomer(transferForm.customerId, transferForm)
    ElMessage.success('转移成功')
    transferVisible.value = false
    getCustomerList()
  } catch (error) {
    console.error('转移客户失败:', error)
  }
}

// 放入公海池
const handlePutToPool = (row: Customer) => {
  ElMessageBox.confirm(
    `确定要将客户"${row.companyName}"放入公海池吗?`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await customerApi.putCustomerToPool(row.id, { customerId: row.id })
      ElMessage.success('已放入公海池')
      getCustomerList()
    } catch (error) {
      console.error('放入公海池失败:', error)
    }
  })
}

// 删除客户
const handleDelete = (row: Customer) => {
  ElMessageBox.confirm(
    `确定要删除客户"${row.companyName}"吗?`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await customerApi.deleteCustomer(row.id)
      ElMessage.success('删除成功')
      getCustomerList()
    } catch (error) {
      console.error('删除客户失败:', error)
    }
  })
}

// 表单提交成功
const handleFormSuccess = () => {
  formVisible.value = false
  getCustomerList()
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

// 获取状态标签类型
const getStatusTagType = (status: string): '' | 'success' | 'warning' | 'info' | 'danger' => {
  const typeMap: Record<string, '' | 'success' | 'warning' | 'info' | 'danger'> = {
    potential: 'info',
    intentional: 'warning',
    won: 'success',
    lost: 'danger'
  }
  return typeMap[status] || ''
}

// 获取状态文本
const getStatusText = (status: string): string => {
  const textMap: Record<string, string> = {
    potential: '潜在客户',
    intentional: '意向客户',
    won: '成交客户',
    lost: '流失客户'
  }
  return textMap[status] || status
}

// 初始化
onMounted(() => {
  getCustomerList()
})
</script>

<style lang="scss" scoped>
.customer-container {
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

  .pagination {
    display: flex;
    justify-content: flex-end;
    padding: 16px 20px;
  }
}
</style>
