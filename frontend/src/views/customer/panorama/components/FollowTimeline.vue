<template>
  <el-card class="follow-timeline-card" v-loading="loading">
    <template #header>
      <div class="card-header">
        <span class="card-title">跟进记录</span>
        <el-button type="primary" link @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新建跟进
        </el-button>
      </div>
    </template>

    <div class="timeline-content">
      <el-timeline v-if="follows.length > 0">
        <el-timeline-item
          v-for="follow in displayFollows"
          :key="follow.id"
          :timestamp="follow.followTime"
          placement="top"
          :color="getFollowTypeColor(follow.followType)"
        >
          <div class="follow-item">
            <div class="follow-header">
              <div class="follow-type">
                <el-icon :size="16">
                  <component :is="getFollowTypeIcon(follow.followType)" />
                </el-icon>
                <span class="type-text">{{ getFollowTypeText(follow.followType) }}</span>
              </div>
              <div class="follow-user">
                <el-avatar :size="24" :src="getUserAvatar(follow)">
                  {{ follow.userName?.charAt(0) || 'U' }}
                </el-avatar>
                <span class="user-name">{{ follow.userName || '未知' }}</span>
              </div>
            </div>
            
            <div class="follow-content">
              <div class="content-text">{{ follow.followContent }}</div>
              <div v-if="follow.contactName" class="contact-info">
                <el-icon><User /></el-icon>
                <span>{{ follow.contactName }}</span>
              </div>
            </div>

            <div v-if="follow.nextFollowTime" class="next-follow">
              <el-icon><Clock /></el-icon>
              <span>下次跟进: {{ follow.nextFollowTime }}</span>
            </div>
          </div>
        </el-timeline-item>
      </el-timeline>

      <el-empty v-else description="暂无跟进记录" :image-size="120" />

      <div v-if="follows.length > limit" class="view-more">
        <el-button type="primary" link @click="handleViewAll">
          查看全部 ({{ follows.length }})
        </el-button>
      </div>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Plus, Phone, ChatDotRound, Message, Location, More, User, Clock } from '@element-plus/icons-vue'
import type { CustomerFollow } from '@/types/customer'

interface Props {
  follows: CustomerFollow[]
  loading?: boolean
  limit?: number
}

const props = withDefaults(defineProps<Props>(), {
  follows: () => [],
  loading: false,
  limit: 5
})

const emit = defineEmits<{
  add: []
  viewAll: []
}>()

// 显示的跟进记录
const displayFollows = computed(() => {
  return props.follows.slice(0, props.limit)
})

// 获取跟进方式图标
const getFollowTypeIcon = (type: string) => {
  const iconMap: Record<string, any> = {
    phone: Phone,
    wechat: ChatDotRound,
    email: Message,
    visit: Location,
    other: More
  }
  return iconMap[type] || More
}

// 获取跟进方式文本
const getFollowTypeText = (type: string): string => {
  const textMap: Record<string, string> = {
    phone: '电话',
    wechat: '微信',
    email: '邮件',
    visit: '上门',
    other: '其他'
  }
  return textMap[type] || type
}

// 获取跟进方式颜色
const getFollowTypeColor = (type: string): string => {
  const colorMap: Record<string, string> = {
    phone: '#3B82F6',
    wechat: '#10B981',
    email: '#F59E0B',
    visit: '#8B5CF6',
    other: '#6B7280'
  }
  return colorMap[type] || '#6B7280'
}

// 获取用户头像
const getUserAvatar = (follow: CustomerFollow) => {
  // 这里可以根据实际情况返回头像URL
  return ''
}

// 新建跟进
const handleAdd = () => {
  emit('add')
}

// 查看全部
const handleViewAll = () => {
  emit('viewAll')
}
</script>

<style lang="scss" scoped>
.follow-timeline-card {
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
    :deep(.el-timeline) {
      padding-left: 0;
    }

    :deep(.el-timeline-item__timestamp) {
      font-size: 12px;
      color: var(--color-text-secondary);
    }

    .follow-item {
      padding: 12px 16px;
      background: var(--color-bg-primary);
      border-radius: 8px;
      border-left: 3px solid var(--color-primary);

      .follow-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 8px;

        .follow-type {
          display: flex;
          align-items: center;
          gap: 4px;

          .type-text {
            font-size: 14px;
            font-weight: 500;
            color: var(--color-text-primary);
          }
        }

        .follow-user {
          display: flex;
          align-items: center;
          gap: 8px;

          .user-name {
            font-size: 12px;
            color: var(--color-text-secondary);
          }
        }
      }

      .follow-content {
        margin-bottom: 8px;

        .content-text {
          font-size: 14px;
          color: var(--color-text-primary);
          line-height: 1.6;
          margin-bottom: 8px;
        }

        .contact-info {
          display: flex;
          align-items: center;
          gap: 4px;
          font-size: 12px;
          color: var(--color-text-secondary);
        }
      }

      .next-follow {
        display: flex;
        align-items: center;
        gap: 4px;
        font-size: 12px;
        color: var(--color-warning);
        padding: 4px 8px;
        background: rgba(245, 158, 11, 0.1);
        border-radius: 4px;
        width: fit-content;
      }
    }

    .view-more {
      display: flex;
      justify-content: center;
      margin-top: 16px;
    }
  }
}
</style>
