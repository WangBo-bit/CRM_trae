<template>
  <div class="customer-detail-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <el-button @click="handleBack">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>
        <h2 class="page-title">{{ customerInfo?.companyName || '客户详情' }}</h2>
      </div>
      <div class="header-right">
        <el-button type="primary" @click="handleEdit">
          <el-icon><Edit /></el-icon>
          编辑
        </el-button>
        <el-button type="danger" @click="handleDelete">
          <el-icon><Delete /></el-icon>
          删除
        </el-button>
      </div>
    </div>

    <!-- 基本信息卡片 -->
    <el-card class="info-card" v-loading="loading">
      <template #header>
        <div class="card-header">
          <span class="card-title">基本信息</span>
        </div>
      </template>

      <el-descriptions :column="3" border>
        <el-descriptions-item label="客户编码">
          {{ customerInfo?.customerCode }}
        </el-descriptions-item>
        <el-descriptions-item label="企业名称">
          {{ customerInfo?.companyName }}
        </el-descriptions-item>
        <el-descriptions-item label="企业简称">
          {{ customerInfo?.shortName || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="行业">
          {{ customerInfo?.industry }}
        </el-descriptions-item>
        <el-descriptions-item label="子行业">
          {{ customerInfo?.subIndustry || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="企业规模">
          {{ customerInfo?.companyScale || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="客户等级">
          <el-tag :type="getLevelTagType(customerInfo?.customerLevel)">
            {{ customerInfo?.customerLevel }}类
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="客户状态">
          <el-tag :type="getStatusTagType(customerInfo?.customerStatus)">
            {{ getStatusText(customerInfo?.customerStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="客户来源">
          {{ customerInfo?.customerSource || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="省份">
          {{ customerInfo?.province || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="城市">
          {{ customerInfo?.city || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="企业官网">
          <el-link
            v-if="customerInfo?.website"
            :href="customerInfo.website"
            target="_blank"
            type="primary"
          >
            {{ customerInfo.website }}
          </el-link>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="企业地址" :span="3">
          {{ customerInfo?.address || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="负责人">
          {{ customerInfo?.ownerName || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="所属部门">
          {{ customerInfo?.deptName || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="标签">
          <el-tag
            v-for="tag in customerInfo?.tags"
            :key="tag"
            style="margin-right: 8px"
          >
            {{ tag }}
          </el-tag>
          <span v-if="!customerInfo?.tags?.length">-</span>
        </el-descriptions-item>
        <el-descriptions-item label="下次跟进时间">
          {{ customerInfo?.nextFollowTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="最后跟进时间">
          {{ customerInfo?.lastFollowTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ customerInfo?.createdTime }}
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="3">
          {{ customerInfo?.remark || '-' }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 标签页 -->
    <el-card class="tabs-card">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="联系人" name="contacts">
          <ContactList :customer-id="customerId" />
        </el-tab-pane>
        <el-tab-pane label="跟进记录" name="follows">
          <FollowList :customer-id="customerId" />
        </el-tab-pane>
        <el-tab-pane label="商机" name="opportunities">
          <div class="empty-content">
            <el-empty description="暂无商机数据" />
          </div>
        </el-tab-pane>
        <el-tab-pane label="POC" name="pocs">
          <div class="empty-content">
            <el-empty description="暂无POC数据" />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 客户表单弹窗 -->
    <CustomerForm
      v-model:visible="formVisible"
      :customer-id="customerId"
      mode="edit"
      @success="handleFormSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft, Edit, Delete } from '@element-plus/icons-vue'
import { customerApi } from '@/api/customer'
import type { CustomerDetail } from '@/types/customer'
import CustomerForm from './components/CustomerForm.vue'
import ContactList from './components/ContactList.vue'
import FollowList from './components/FollowList.vue'

const route = useRoute()
const router = useRouter()

const customerId = route.params.id as string
const loading = ref(false)
const customerInfo = ref<CustomerDetail>()
const activeTab = ref('contacts')
const formVisible = ref(false)

// 获取客户详情
const getCustomerDetail = async () => {
  loading.value = true
  try {
    const response = await customerApi.getCustomerDetail(customerId)
    if (response.code === 200) {
      customerInfo.value = response.data
    }
  } catch (error) {
    console.error('获取客户详情失败:', error)
  } finally {
    loading.value = false
  }
}

// 返回
const handleBack = () => {
  router.back()
}

// 编辑
const handleEdit = () => {
  formVisible.value = true
}

// 删除
const handleDelete = () => {
  ElMessageBox.confirm(
    `确定要删除客户"${customerInfo.value?.companyName}"吗?`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await customerApi.deleteCustomer(customerId)
      ElMessage.success('删除成功')
      router.back()
    } catch (error) {
      console.error('删除客户失败:', error)
    }
  })
}

// 表单提交成功
const handleFormSuccess = () => {
  formVisible.value = false
  getCustomerDetail()
}

// 获取等级标签类型
const getLevelTagType = (level?: string): '' | 'success' | 'warning' | 'info' | 'danger' => {
  if (!level) return ''
  const typeMap: Record<string, '' | 'success' | 'warning' | 'info' | 'danger'> = {
    A: 'danger',
    B: 'warning',
    C: '',
    D: 'info'
  }
  return typeMap[level] || ''
}

// 获取状态标签类型
const getStatusTagType = (status?: string): '' | 'success' | 'warning' | 'info' | 'danger' => {
  if (!status) return ''
  const typeMap: Record<string, '' | 'success' | 'warning' | 'info' | 'danger'> = {
    potential: 'info',
    intentional: 'warning',
    won: 'success',
    lost: 'danger'
  }
  return typeMap[status] || ''
}

// 获取状态文本
const getStatusText = (status?: string): string => {
  if (!status) return ''
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
  getCustomerDetail()
})
</script>

<style lang="scss" scoped>
.customer-detail-container {
  padding: var(--page-padding);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;

  .header-left {
    display: flex;
    align-items: center;
    gap: 16px;

    .page-title {
      font-size: 24px;
      font-weight: 700;
      color: var(--color-text-primary);
      margin: 0;
    }
  }

  .header-right {
    display: flex;
    gap: 12px;
  }
}

.info-card {
  margin-bottom: 16px;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .card-title {
      font-size: 16px;
      font-weight: 600;
      color: var(--color-text-primary);
    }
  }
}

.tabs-card {
  :deep(.el-card__body) {
    padding: 0;
  }

  :deep(.el-tabs__header) {
    margin: 0;
    padding: 0 20px;
    background: var(--color-bg-primary);
  }

  :deep(.el-tabs__content) {
    padding: 20px;
  }
}

.empty-content {
  padding: 40px 0;
}
</style>
