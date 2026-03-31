<template>
  <div class="customer-panorama-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <el-button @click="handleBack">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>
        <h2 class="page-title">{{ customerInfo?.companyName || '客户全景视图' }}</h2>
      </div>
      <div class="header-right">
        <el-button @click="handleRefresh">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
        <el-button type="primary" @click="handleEdit">
          <el-icon><Edit /></el-icon>
          编辑
        </el-button>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="panorama-content" v-loading="pageLoading">
      <el-row :gutter="16">
        <!-- 左侧列 -->
        <el-col :xs="24" :lg="12">
          <!-- 基本信息卡片 -->
          <BasicInfoCard
            :customer-info="customerInfo"
            :loading="loading.basicInfo"
            @edit="handleEdit"
          />

          <!-- 联系人卡片 -->
          <ContactCard
            :contacts="contacts"
            :loading="loading.contacts"
            @add="handleAddContact"
            @view-all="handleViewAllContacts"
            @view-contact="handleViewContact"
          />

          <!-- 统计数据卡片 -->
          <StatisticsCard
            :statistics="statistics"
            :loading="loading.statistics"
          />
        </el-col>

        <!-- 右侧列 -->
        <el-col :xs="24" :lg="12">
          <!-- 跟进时间线 -->
          <FollowTimeline
            :follows="follows"
            :loading="loading.follows"
            :limit="5"
            @add="handleAddFollow"
            @view-all="handleViewAllFollows"
          />

          <!-- 动态时间线 -->
          <TimelineCard
            :timeline="timeline"
            :loading="loading.timeline"
            :has-more="hasMoreTimeline"
            @refresh="handleRefreshTimeline"
            @load-more="handleLoadMoreTimeline"
            @view-related="handleViewRelated"
          />
        </el-col>
      </el-row>
    </div>

    <!-- 客户表单弹窗 -->
    <CustomerForm
      v-model:visible="formVisible"
      :customer-id="customerId"
      mode="edit"
      @success="handleFormSuccess"
    />

    <!-- 联系人表单弹窗 -->
    <ContactForm
      v-model:visible="contactFormVisible"
      :customer-id="customerId"
      :contact-id="currentContactId"
      @success="handleContactFormSuccess"
    />

    <!-- 跟进记录表单弹窗 -->
    <FollowForm
      v-model:visible="followFormVisible"
      :customer-id="customerId"
      @success="handleFollowFormSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Edit, Refresh } from '@element-plus/icons-vue'
import { customerApi } from '@/api/customer'
import type { 
  CustomerDetail, 
  Contact, 
  CustomerFollow, 
  CustomerStatistics, 
  CustomerTimeline 
} from '@/types/customer'
import BasicInfoCard from './components/BasicInfoCard.vue'
import ContactCard from './components/ContactCard.vue'
import StatisticsCard from './components/StatisticsCard.vue'
import FollowTimeline from './components/FollowTimeline.vue'
import TimelineCard from './components/TimelineCard.vue'
import CustomerForm from '../components/CustomerForm.vue'
import ContactForm from '../components/ContactForm.vue'
import FollowForm from '../components/FollowForm.vue'

const route = useRoute()
const router = useRouter()

// 获取并验证客户ID
const customerId = computed(() => {
  const id = route.params.id as string
  if (!id || typeof id !== 'string' || id.trim() === '') {
    ElMessage.error('客户ID无效')
    router.push('/customer/list')
    return ''
  }
  return id.trim()
})

// 数据状态
const customerInfo = ref<CustomerDetail>()
const contacts = ref<Contact[]>([])
const follows = ref<CustomerFollow[]>([])
const statistics = ref<CustomerStatistics>()
const timeline = ref<CustomerTimeline[]>([])

// 加载状态
const loading = ref({
  basicInfo: false,
  contacts: false,
  follows: false,
  statistics: false,
  timeline: false
})

const pageLoading = computed(() => {
  return Object.values(loading.value).some(v => v)
})

// 弹窗状态
const formVisible = ref(false)
const contactFormVisible = ref(false)
const followFormVisible = ref(false)
const currentContactId = ref('')

// 时间线分页
const hasMoreTimeline = ref(false)
const timelineLimit = 10
const timelineOffset = ref(0)

// 获取客户全景视图数据
const getCustomerPanorama = async () => {
  if (!customerId.value) return
  
  loading.value.basicInfo = true
  try {
    const response = await customerApi.getCustomerPanorama(customerId.value)
    if (response.code === 200) {
      customerInfo.value = response.data
      contacts.value = response.data.contacts || []
      follows.value = response.data.follows || []
    } else {
      ElMessage.error(response.message || '获取客户全景视图失败')
    }
  } catch (error: any) {
    console.error('获取客户全景视图失败:', error)
    const errorMsg = error?.response?.data?.message || error?.message || '获取客户全景视图失败'
    ElMessage.error(errorMsg)
  } finally {
    loading.value.basicInfo = false
  }
}

// 获取客户统计数据
const getCustomerStatistics = async () => {
  if (!customerId.value) return
  
  loading.value.statistics = true
  try {
    const response = await customerApi.getCustomerStatistics(customerId.value)
    if (response.code === 200) {
      statistics.value = response.data
    }
  } catch (error) {
    console.error('获取客户统计数据失败:', error)
  } finally {
    loading.value.statistics = false
  }
}

// 获取客户动态时间线
const getCustomerTimeline = async (isLoadMore = false) => {
  if (!customerId.value) return
  
  loading.value.timeline = true
  try {
    const response = await customerApi.getCustomerTimeline(customerId.value, { 
      limit: timelineLimit,
      offset: isLoadMore ? timelineOffset.value : 0
    })
    if (response.code === 200) {
      if (isLoadMore) {
        // 加载更多时追加数据
        timeline.value = [...timeline.value, ...response.data]
      } else {
        // 首次加载时重置数据
        timeline.value = response.data
        timelineOffset.value = 0
      }
      timelineOffset.value += response.data.length
      hasMoreTimeline.value = response.data.length >= timelineLimit
    }
  } catch (error) {
    console.error('获取客户动态时间线失败:', error)
  } finally {
    loading.value.timeline = false
  }
}

// 加载所有数据
const loadAllData = async () => {
  await Promise.all([
    getCustomerPanorama(),
    getCustomerStatistics(),
    getCustomerTimeline()
  ])
}

// 返回
const handleBack = () => {
  router.back()
}

// 刷新
const handleRefresh = () => {
  loadAllData()
}

// 编辑客户
const handleEdit = () => {
  formVisible.value = true
}

// 表单提交成功
const handleFormSuccess = () => {
  formVisible.value = false
  getCustomerPanorama()
}

// 添加联系人
const handleAddContact = () => {
  currentContactId.value = ''
  contactFormVisible.value = true
}

// 查看所有联系人
const handleViewAllContacts = () => {
  router.push({
    path: `/customer/detail/${customerId.value}`,
    query: { tab: 'contacts' }
  })
}

// 查看联系人
const handleViewContact = (contact: Contact) => {
  currentContactId.value = contact.id
  contactFormVisible.value = true
}

// 联系人表单提交成功
const handleContactFormSuccess = () => {
  contactFormVisible.value = false
  getCustomerPanorama()
}

// 添加跟进记录
const handleAddFollow = () => {
  followFormVisible.value = true
}

// 查看所有跟进记录
const handleViewAllFollows = () => {
  router.push({
    path: `/customer/detail/${customerId.value}`,
    query: { tab: 'follows' }
  })
}

// 跟进记录表单提交成功
const handleFollowFormSuccess = () => {
  followFormVisible.value = false
  getCustomerPanorama()
  getCustomerTimeline()
}

// 刷新时间线
const handleRefreshTimeline = () => {
  getCustomerTimeline()
}

// 加载更多时间线
const handleLoadMoreTimeline = async () => {
  await getCustomerTimeline(true)
}

// 查看关联信息
const handleViewRelated = (item: CustomerTimeline) => {
  if (!item.relatedId || !item.relatedType) return

  // 根据关联类型跳转到不同的详情页
  const routeMap: Record<string, string> = {
    opportunity: `/opportunity/detail/${item.relatedId}`,
    poc: `/poc/detail/${item.relatedId}`,
    assessment: `/assessment/detail/${item.relatedId}`
  }

  const path = routeMap[item.relatedType]
  if (path) {
    router.push(path)
  }
}

// 初始化
onMounted(() => {
  loadAllData()
})
</script>

<style lang="scss" scoped>
.customer-panorama-container {
  padding: var(--page-padding);
  min-height: calc(100vh - 56px);
  background: var(--color-bg-primary);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding: 16px 20px;
  background: var(--color-bg-white);
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);

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

.panorama-content {
  :deep(.el-card) {
    margin-bottom: 16px;
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    transition: all 0.3s;

    &:hover {
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }
  }

  :deep(.el-card__header) {
    padding: 16px 20px;
    border-bottom: 1px solid var(--color-border-secondary);
  }

  :deep(.el-card__body) {
    padding: 20px;
  }
}

// 响应式布局
@media (max-width: 992px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;

    .header-right {
      width: 100%;
      justify-content: flex-end;
    }
  }
}
</style>
