<template>
  <el-card class="statistics-card" v-loading="loading">
    <template #header>
      <div class="card-header">
        <span class="card-title">统计数据</span>
      </div>
    </template>

    <div class="statistics-content">
      <!-- 统计数据网格 -->
      <div class="stats-grid">
        <div class="stat-item">
          <div class="stat-icon follow">
            <el-icon :size="24"><ChatDotRound /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics?.followCount || 0 }}</div>
            <div class="stat-label">跟进次数</div>
          </div>
        </div>

        <div class="stat-item">
          <div class="stat-icon contact">
            <el-icon :size="24"><User /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics?.contactCount || 0 }}</div>
            <div class="stat-label">联系人数量</div>
          </div>
        </div>

        <div class="stat-item">
          <div class="stat-icon opportunity">
            <el-icon :size="24"><TrendCharts /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics?.opportunityCount || 0 }}</div>
            <div class="stat-label">商机数量</div>
          </div>
        </div>

        <div class="stat-item">
          <div class="stat-icon poc">
            <el-icon :size="24"><DataAnalysis /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics?.pocCount || 0 }}</div>
            <div class="stat-label">POC数量</div>
          </div>
        </div>
      </div>

      <!-- 详细数据 -->
      <div class="detail-stats">
        <div class="detail-item">
          <span class="detail-label">商机总金额</span>
          <span class="detail-value amount">
            ¥{{ formatAmount(statistics?.opportunityAmount || 0) }}
          </span>
        </div>
        <div class="detail-item">
          <span class="detail-label">成交金额</span>
          <span class="detail-value amount success">
            ¥{{ formatAmount(statistics?.wonAmount || 0) }}
          </span>
        </div>
        <div class="detail-item">
          <span class="detail-label">POC成功率</span>
          <span class="detail-value" :class="getSuccessRateClass(statistics?.pocSuccessRate)">
            {{ statistics?.pocSuccessRate || 0 }}%
          </span>
        </div>
        <div class="detail-item">
          <span class="detail-label">最后跟进</span>
          <span class="detail-value time">
            {{ statistics?.lastFollowTime || '-' }}
          </span>
        </div>
        <div class="detail-item">
          <span class="detail-label">下次跟进</span>
          <span class="detail-value time" :class="getNextFollowClass(statistics?.nextFollowTime)">
            {{ statistics?.nextFollowTime || '-' }}
          </span>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { ChatDotRound, User, TrendCharts, DataAnalysis } from '@element-plus/icons-vue'
import type { CustomerStatistics } from '@/types/customer'

interface Props {
  statistics?: CustomerStatistics
  loading?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  loading: false
})

// 格式化金额
const formatAmount = (amount: number): string => {
  if (amount >= 10000) {
    return (amount / 10000).toFixed(2) + '万'
  }
  return amount.toFixed(2)
}

// 获取成功率样式类
const getSuccessRateClass = (rate?: number): string => {
  if (!rate) return ''
  if (rate >= 80) return 'high'
  if (rate >= 50) return 'medium'
  return 'low'
}

// 获取下次跟进样式类
const getNextFollowClass = (time?: string): string => {
  if (!time) return ''
  const nextTime = new Date(time).getTime()
  const now = Date.now()
  const diffDays = Math.ceil((nextTime - now) / (1000 * 60 * 60 * 24))
  
  if (diffDays < 0) return 'overdue'
  if (diffDays <= 3) return 'urgent'
  return ''
}
</script>

<style lang="scss" scoped>
.statistics-card {
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

  .statistics-content {
    .stats-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 16px;
      margin-bottom: 20px;

      .stat-item {
        display: flex;
        align-items: center;
        padding: 16px;
        background: var(--color-bg-primary);
        border-radius: 8px;
        transition: all 0.2s;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
        }

        .stat-icon {
          display: flex;
          align-items: center;
          justify-content: center;
          width: 48px;
          height: 48px;
          border-radius: 12px;
          margin-right: 12px;

          &.follow {
            background: rgba(59, 130, 246, 0.1);
            color: var(--color-primary);
          }

          &.contact {
            background: rgba(16, 185, 129, 0.1);
            color: var(--color-success);
          }

          &.opportunity {
            background: rgba(245, 158, 11, 0.1);
            color: var(--color-warning);
          }

          &.poc {
            background: rgba(139, 92, 246, 0.1);
            color: #8B5CF6;
          }
        }

        .stat-info {
          flex: 1;

          .stat-value {
            font-size: 24px;
            font-weight: 700;
            color: var(--color-text-primary);
            line-height: 1.2;
          }

          .stat-label {
            font-size: 12px;
            color: var(--color-text-secondary);
            margin-top: 4px;
          }
        }
      }
    }

    .detail-stats {
      display: flex;
      flex-direction: column;
      gap: 12px;

      .detail-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 12px 16px;
        background: var(--color-bg-primary);
        border-radius: 8px;

        .detail-label {
          font-size: 14px;
          color: var(--color-text-secondary);
        }

        .detail-value {
          font-size: 14px;
          font-weight: 600;
          color: var(--color-text-primary);

          &.amount {
            color: var(--color-primary);

            &.success {
              color: var(--color-success);
            }
          }

          &.high {
            color: var(--color-success);
          }

          &.medium {
            color: var(--color-warning);
          }

          &.low {
            color: var(--color-error);
          }

          &.time {
            &.overdue {
              color: var(--color-error);
            }

            &.urgent {
              color: var(--color-warning);
            }
          }
        }
      }
    }
  }
}
</style>
