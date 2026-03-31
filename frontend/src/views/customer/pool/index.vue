<template>
  <div class="pool-customer-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">客户公海池</h2>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="statistics-row">
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card">
          <div class="stat-card__content">
            <div class="stat-card__icon">
              <el-icon :size="32" color="#3B82F6"><User /></el-icon>
            </div>
            <div class="stat-card__info">
              <div class="stat-card__value">{{ statistics.totalCustomers }}</div>
              <div class="stat-card__label">公海池客户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card">
          <div class="stat-card__content">
            <div class="stat-card__icon">
              <el-icon :size="32" color="#10B981"><Download /></el-icon>
            </div>
            <div class="stat-card__info">
              <div class="stat-card__value">{{ statistics.todayInCount }}</div>
              <div class="stat-card__label">今日进入</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card">
          <div class="stat-card__content">
            <div class="stat-card__icon">
              <el-icon :size="32" color="#F59E0B"><Upload /></el-icon>
            </div>
            <div class="stat-card__info">
              <div class="stat-card__value">{{ statistics.todayOutCount }}</div>
              <div class="stat-card__label">今日认领</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card">
          <div class="stat-card__content">
            <div class="stat-card__icon">
              <el-icon :size="32" color="#8B5CF6"><TrendCharts /></el-icon>
            </div>
            <div class="stat-card__info">
              <div class="stat-card__value">{{ statistics.monthInCount }}</div>
              <div class="stat-card__label">本月进入</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

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

        <el-form-item label="省份">
          <el-select
            v-model="searchForm.province"
            placeholder="请选择省份"
            clearable
            style="width: 120px"
          >
            <el-option label="广东省" value="广东省" />
            <el-option label="北京市" value="北京市" />
            <el-option label="上海市" value="上海市" />
            <el-option label="浙江省" value="浙江省" />
            <el-option label="江苏省" value="江苏省" />
          </el-select>
        </el-form-item>

        <el-form-item label="城市">
          <el-select
            v-model="searchForm.city"
            placeholder="请选择城市"
            clearable
            style="width: 120px"
          >
            <el-option label="深圳市" value="深圳市" />
            <el-option label="广州市" value="广州市" />
            <el-option label="北京市" value="北京市" />
            <el-option label="上海市" value="上海市" />
            <el-option label="杭州市" value="杭州市" />
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
          <el-button
            type="primary"
            :disabled="selectedRows.length === 0"
            @click="handleBatchClaim"
          >
            <el-icon><Plus /></el-icon>
            批量认领
          </el-button>
        </div>
        <div class="toolbar-right">
          <el-button @click="handleExport">
            <el-icon><Download /></el-icon>
            导出
          </el-button>
          <el-button @click="handleViewRules">
            <el-icon><Setting /></el-icon>
            规则管理
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

        <el-table-column prop="province" label="省份" width="100" />

        <el-table-column prop="city" label="城市" width="100" />

        <el-table-column prop="poolTime" label="进入公海池时间" width="160" />

        <el-table-column prop="lastFollowTime" label="最后跟进时间" width="160" />

        <el-table-column prop="createdTime" label="创建时间" width="160" />

        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleViewDetail(row.id)">
              查看
            </el-button>
            <el-button type="success" link @click="handleClaim(row)">
              认领
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

    <!-- 批量认领确认弹窗 -->
    <el-dialog
      v-model="claimDialogVisible"
      title="批量认领确认"
      width="500px"
    >
      <div class="claim-dialog-content">
        <el-icon class="claim-icon" :size="48" color="#F59E0B">
          <WarningFilled />
        </el-icon>
        <p class="claim-text">
          确定要认领选中的 <strong>{{ selectedRows.length }}</strong> 个客户吗?
        </p>
        <p class="claim-hint">认领后，这些客户将成为您的私有客户</p>
      </div>
      <template #footer>
        <el-button @click="claimDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="claimLoading" @click="handleClaimSubmit">
          确定认领
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Search,
  Refresh,
  Plus,
  Download,
  User,
  TrendCharts,
  Setting,
  WarningFilled
} from '@element-plus/icons-vue'
import { poolApi } from '@/api/pool'
import type {
  PoolCustomer,
  PoolCustomerQueryParams,
  PoolStatistics
} from '@/types/pool'

const router = useRouter()

// 搜索表单
const searchForm = reactive<PoolCustomerQueryParams>({
  keyword: '',
  industry: '',
  customerLevel: undefined,
  province: '',
  city: ''
})

// 统计数据
const statistics = ref<PoolStatistics>({
  totalCustomers: 0,
  todayInCount: 0,
  todayOutCount: 0,
  weekInCount: 0,
  weekOutCount: 0,
  monthInCount: 0,
  monthOutCount: 0,
  levelDistribution: [],
  industryDistribution: [],
  regionDistribution: []
})

// 客户列表
const customerList = ref<PoolCustomer[]>([])
const loading = ref(false)

// 分页
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 选中的行
const selectedRows = ref<PoolCustomer[]>([])

// 认领弹窗
const claimDialogVisible = ref(false)
const claimLoading = ref(false)

// 获取统计数据
const getStatistics = async () => {
  try {
    const response = await poolApi.getPoolStatistics()
    if (response.code === 200) {
      statistics.value = response.data
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

// 获取客户列表
const getCustomerList = async () => {
  loading.value = true
  try {
    const params: PoolCustomerQueryParams = {
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    const response = await poolApi.getPoolCustomerList(params)
    if (response.code === 200) {
      customerList.value = response.data.list
      pagination.total = response.data.total
    }
  } catch (error) {
    console.error('获取客户列表失败:', error)
    ElMessage.error('获取客户列表失败')
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
    province: '',
    city: ''
  })
  handleSearch()
}

// 选择变化
const handleSelectionChange = (rows: PoolCustomer[]) => {
  selectedRows.value = rows
}

// 批量认领
const handleBatchClaim = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请先选择要认领的客户')
    return
  }
  claimDialogVisible.value = true
}

// 认领单个客户
const handleClaim = (row: PoolCustomer) => {
  selectedRows.value = [row]
  claimDialogVisible.value = true
}

// 提交认领
const handleClaimSubmit = async () => {
  claimLoading.value = true
  try {
    const customerIds = selectedRows.value.map(row => row.id)
    const response = await poolApi.batchClaimCustomers({ customerIds })
    if (response.code === 200) {
      const result = response.data
      if (result.failCount > 0) {
        ElMessage.warning(`成功认领 ${result.successCount} 个客户，失败 ${result.failCount} 个`)
      } else {
        ElMessage.success(`成功认领 ${result.successCount} 个客户`)
      }
      claimDialogVisible.value = false
      selectedRows.value = []
      getCustomerList()
      getStatistics()
    }
  } catch (error) {
    console.error('认领客户失败:', error)
    ElMessage.error('认领客户失败')
  } finally {
    claimLoading.value = false
  }
}

// 查看详情
const handleViewDetail = (id: string) => {
  router.push(`/customer/detail/${id}`)
}

// 导出
const handleExport = async () => {
  try {
    await poolApi.exportPoolCustomers(searchForm)
    ElMessage.success('导出成功')
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败,请稍后重试')
  }
}

// 查看规则管理
const handleViewRules = () => {
  router.push('/customer/pool/rules')
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

// 初始化
onMounted(() => {
  getStatistics()
  getCustomerList()
})
</script>

<style lang="scss" scoped>
.pool-customer-container {
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

.statistics-row {
  margin-bottom: 16px;
}

.stat-card {
  :deep(.el-card__body) {
    padding: 20px;
  }

  .stat-card__content {
    display: flex;
    align-items: center;
    gap: 16px;
  }

  .stat-card__icon {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 56px;
    height: 56px;
    border-radius: 8px;
    background: var(--color-primary-lightest);
  }

  .stat-card__info {
    flex: 1;
  }

  .stat-card__value {
    font-size: 28px;
    font-weight: 700;
    color: var(--color-text-primary);
    line-height: 1.2;
  }

  .stat-card__label {
    font-size: 14px;
    color: var(--color-text-tertiary);
    margin-top: 4px;
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

.claim-dialog-content {
  text-align: center;
  padding: 20px 0;

  .claim-icon {
    margin-bottom: 16px;
  }

  .claim-text {
    font-size: 16px;
    color: var(--color-text-primary);
    margin-bottom: 8px;

    strong {
      color: var(--color-primary);
      font-size: 18px;
    }
  }

  .claim-hint {
    font-size: 14px;
    color: var(--color-text-tertiary);
  }
}
</style>
