<template>
  <el-card class="contact-card" v-loading="loading">
    <template #header>
      <div class="card-header">
        <span class="card-title">联系人信息</span>
        <el-button type="primary" link @click="handleAdd">
          <el-icon><Plus /></el-icon>
          添加
        </el-button>
      </div>
    </template>

    <div class="contact-content">
      <!-- 主联系人 -->
      <div v-if="primaryContact" class="primary-contact">
        <div class="contact-header">
          <el-icon class="star-icon"><Star /></el-icon>
          <span class="label">主联系人</span>
        </div>
        <div class="contact-info">
          <div class="contact-name">
            <span class="name">{{ primaryContact.name }}</span>
            <el-tag v-if="primaryContact.position" size="small" style="margin-left: 8px">
              {{ primaryContact.position }}
            </el-tag>
          </div>
          <div class="contact-details">
            <div v-if="primaryContact.mobile" class="detail-item">
              <el-icon><Phone /></el-icon>
              <span>{{ primaryContact.mobile }}</span>
            </div>
            <div v-if="primaryContact.email" class="detail-item">
              <el-icon><Message /></el-icon>
              <span>{{ primaryContact.email }}</span>
            </div>
            <div v-if="primaryContact.department" class="detail-item">
              <el-icon><OfficeBuilding /></el-icon>
              <span>{{ primaryContact.department }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 联系人统计 -->
      <div class="contact-statistics">
        <div class="stat-item">
          <span class="stat-value">{{ contacts.length }}</span>
          <span class="stat-label">联系人总数</span>
        </div>
        <div class="stat-item">
          <span class="stat-value">{{ decisionMakerCount }}</span>
          <span class="stat-label">决策者</span>
        </div>
        <div class="stat-item">
          <span class="stat-value">{{ influencerCount }}</span>
          <span class="stat-label">影响者</span>
        </div>
      </div>

      <!-- 联系人列表 -->
      <div class="contact-list">
        <div class="list-header">
          <span>联系人列表</span>
          <el-button type="primary" link size="small" @click="handleViewAll">
            查看全部
          </el-button>
        </div>
        <div class="list-content">
          <div
            v-for="contact in displayContacts"
            :key="contact.id"
            class="contact-item"
            @click="handleViewContact(contact)"
          >
            <div class="contact-avatar">
              <el-avatar :size="36" :src="getAvatarUrl(contact)">
                {{ contact.name.charAt(0) }}
              </el-avatar>
            </div>
            <div class="contact-info">
              <div class="contact-name">
                <span>{{ contact.name }}</span>
                <el-tag v-if="contact.isPrimary" type="warning" size="small">主</el-tag>
                <el-tag v-if="contact.position" size="small" style="margin-left: 4px">
                  {{ contact.position }}
                </el-tag>
              </div>
              <div class="contact-meta">
                <span v-if="contact.mobile">{{ contact.mobile }}</span>
                <span v-if="contact.department" style="margin-left: 8px">
                  {{ contact.department }}
                </span>
              </div>
            </div>
            <div class="contact-level">
              <el-tag :type="getDecisionLevelType(contact.decisionLevel)" size="small">
                {{ getDecisionLevelText(contact.decisionLevel) }}
              </el-tag>
            </div>
          </div>
          <el-empty v-if="contacts.length === 0" description="暂无联系人" :image-size="80" />
        </div>
      </div>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Plus, Star, Phone, Message, OfficeBuilding } from '@element-plus/icons-vue'
import type { Contact } from '@/types/customer'

interface Props {
  contacts: Contact[]
  loading?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  contacts: () => [],
  loading: false
})

const emit = defineEmits<{
  add: []
  viewAll: []
  viewContact: [contact: Contact]
}>()

// 主联系人
const primaryContact = computed(() => {
  return props.contacts.find(c => c.isPrimary === 1)
})

// 决策者数量
const decisionMakerCount = computed(() => {
  return props.contacts.filter(c => c.decisionLevel === 'decision_maker').length
})

// 影响者数量
const influencerCount = computed(() => {
  return props.contacts.filter(c => c.decisionLevel === 'influencer').length
})

// 显示的联系人列表(最多显示5个)
const displayContacts = computed(() => {
  return props.contacts.slice(0, 5)
})

// 获取头像URL
const getAvatarUrl = (contact: Contact) => {
  // 这里可以根据实际情况返回头像URL
  return ''
}

// 获取决策层级类型
const getDecisionLevelType = (level: string): '' | 'success' | 'warning' | 'info' | 'danger' => {
  const typeMap: Record<string, '' | 'success' | 'warning' | 'info' | 'danger'> = {
    decision_maker: 'danger',
    influencer: 'warning',
    executor: 'info'
  }
  return typeMap[level] || ''
}

// 获取决策层级文本
const getDecisionLevelText = (level: string): string => {
  const textMap: Record<string, string> = {
    decision_maker: '决策者',
    influencer: '影响者',
    executor: '执行者'
  }
  return textMap[level] || level
}

// 添加联系人
const handleAdd = () => {
  emit('add')
}

// 查看全部
const handleViewAll = () => {
  emit('viewAll')
}

// 查看联系人
const handleViewContact = (contact: Contact) => {
  emit('viewContact', contact)
}
</script>

<style lang="scss" scoped>
.contact-card {
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

  .contact-content {
    .primary-contact {
      padding: 16px;
      margin-bottom: 16px;
      background: var(--color-bg-primary);
      border-radius: 8px;
      border-left: 3px solid var(--color-warning);

      .contact-header {
        display: flex;
        align-items: center;
        margin-bottom: 12px;

        .star-icon {
          color: var(--color-warning);
          margin-right: 8px;
        }

        .label {
          font-size: 14px;
          font-weight: 600;
          color: var(--color-text-primary);
        }
      }

      .contact-info {
        .contact-name {
          display: flex;
          align-items: center;
          margin-bottom: 8px;

          .name {
            font-size: 16px;
            font-weight: 600;
            color: var(--color-text-primary);
          }
        }

        .contact-details {
          display: flex;
          flex-wrap: wrap;
          gap: 16px;

          .detail-item {
            display: flex;
            align-items: center;
            gap: 4px;
            font-size: 14px;
            color: var(--color-text-secondary);

            .el-icon {
              font-size: 16px;
            }
          }
        }
      }
    }

    .contact-statistics {
      display: flex;
      justify-content: space-around;
      padding: 16px;
      margin-bottom: 16px;
      background: var(--color-bg-primary);
      border-radius: 8px;

      .stat-item {
        display: flex;
        flex-direction: column;
        align-items: center;

        .stat-value {
          font-size: 24px;
          font-weight: 700;
          color: var(--color-primary);
          line-height: 1.2;
        }

        .stat-label {
          font-size: 12px;
          color: var(--color-text-secondary);
          margin-top: 4px;
        }
      }
    }

    .contact-list {
      .list-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12px;
        font-size: 14px;
        font-weight: 600;
        color: var(--color-text-primary);
      }

      .list-content {
        .contact-item {
          display: flex;
          align-items: center;
          padding: 12px;
          margin-bottom: 8px;
          background: var(--color-bg-white);
          border: 1px solid var(--color-border-secondary);
          border-radius: 8px;
          cursor: pointer;
          transition: all 0.2s;

          &:hover {
            border-color: var(--color-primary);
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
          }

          .contact-avatar {
            margin-right: 12px;
          }

          .contact-info {
            flex: 1;

            .contact-name {
              display: flex;
              align-items: center;
              margin-bottom: 4px;
              font-size: 14px;
              font-weight: 500;
              color: var(--color-text-primary);
            }

            .contact-meta {
              font-size: 12px;
              color: var(--color-text-secondary);
            }
          }

          .contact-level {
            margin-left: 12px;
          }
        }
      }
    }
  }
}
</style>
