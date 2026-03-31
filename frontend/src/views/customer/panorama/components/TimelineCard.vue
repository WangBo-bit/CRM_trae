<template>
  <el-card class="timeline-card" v-loading="loading">
    <template #header>
      <div class="card-header">
        <span class="card-title">动态时间线</span>
        <el-button type="primary" link size="small" @click="handleRefresh">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </template>

    <div class="timeline-content">
      <el-timeline v-if="timeline.length > 0">
        <el-timeline-item
          v-for="item in timeline"
          :key="item.id"
          :timestamp="item.operationTime"
          placement="top"
          :color="getOperationTypeColor(item.operationType)"
        >
          <div class="timeline-item">
            <div class="timeline-header">
              <div class="operation-type">
                <el-icon :size="16">
                  <component :is="getOperationTypeIcon(item.operationType)" />
                </el-icon>
                <span class="type-text">{{ getOperationTypeText(item.operationType) }}</span>
              </div>
              <div class="operator">
                <span>{{ item.operatorName || '系统' }}</span>
              </div>
            </div>

            <div class="timeline-content-text">
              {{ item.operationDesc }}
            </div>

            <div v-if="item.relatedName" class="related-info">
              <el-icon><Link /></el-icon>
              <el-button type="primary" link size="small" @click="handleViewRelated(item)">
                {{ item.relatedName }}
              </el-button>
            </div>
          </div>
        </el-timeline-item>
      </el-timeline>

      <el-empty v-else description="暂无动态记录" :image-size="120" />

      <div v-if="hasMore" class="load-more">
        <el-button @click="handleLoadMore" :loading="loading">
          加载更多
        </el-button>
      </div>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { 
  Refresh, 
  Link,
  Document,
  Edit,
  ChatDotRound,
  TrendCharts,
  DataAnalysis,
  User,
  More
} from '@element-plus/icons-vue'
import type { CustomerTimeline } from '@/types/customer'

interface Props {
  timeline: CustomerTimeline[]
  loading?: boolean
  hasMore?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  timeline: () => [],
  loading: false,
  hasMore: false
})

const emit = defineEmits<{
  refresh: []
  loadMore: []
  viewRelated: [item: CustomerTimeline]
}>()

// 获取操作类型图标
const getOperationTypeIcon = (type: string) => {
  const iconMap: Record<string, any> = {
    create: Document,
    update: Edit,
    follow: ChatDotRound,
    opportunity: TrendCharts,
    poc: DataAnalysis,
    contact: User,
    other: More
  }
  return iconMap[type] || More
}

// 获取操作类型文本
const getOperationTypeText = (type: string): string => {
  const textMap: Record<string, string> = {
    create: '创建客户',
    update: '更新信息',
    follow: '跟进记录',
    opportunity: '商机相关',
    poc: 'POC相关',
    contact: '联系人相关',
    other: '其他操作'
  }
  return textMap[type] || type
}

// 获取操作类型颜色
const getOperationTypeColor = (type: string): string => {
  const colorMap: Record<string, string> = {
    create: '#10B981',
    update: '#3B82F6',
    follow: '#F59E0B',
    opportunity: '#8B5CF6',
    poc: '#06B6D4',
    contact: '#EC4899',
    other: '#6B7280'
  }
  return colorMap[type] || '#6B7280'
}

// 刷新
const handleRefresh = () => {
  emit('refresh')
}

// 加载更多
const handleLoadMore = () => {
  emit('loadMore')
}

// 查看关联信息
const handleViewRelated = (item: CustomerTimeline) => {
  emit('viewRelated', item)
}
</script>

<style lang="scss" scoped>
.timeline-card {
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

  .timeline-content {
    max-height: 600px;
    overflow-y: auto;

    :deep(.el-timeline) {
      padding-left: 0;
    }

    :deep(.el-timeline-item__timestamp) {
      font-size: 12px;
      color: var(--color-text-secondary);
    }

    .timeline-item {
      padding: 12px 16px;
      background: var(--color-bg-primary);
      border-radius: 8px;
      border-left: 3px solid var(--color-primary);

      .timeline-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 8px;

        .operation-type {
          display: flex;
          align-items: center;
          gap: 4px;

          .type-text {
            font-size: 14px;
            font-weight: 500;
            color: var(--color-text-primary);
          }
        }

        .operator {
          font-size: 12px;
          color: var(--color-text-secondary);
        }
      }

      .timeline-content-text {
        font-size: 14px;
        color: var(--color-text-primary);
        line-height: 1.6;
        margin-bottom: 8px;
      }

      .related-info {
        display: flex;
        align-items: center;
        gap: 4px;
        font-size: 12px;
        color: var(--color-primary);
        padding: 4px 8px;
        background: rgba(59, 130, 246, 0.1);
        border-radius: 4px;
        width: fit-content;
      }
    }

    .load-more {
      display: flex;
      justify-content: center;
      margin-top: 16px;
    }
  }
}
</style>
