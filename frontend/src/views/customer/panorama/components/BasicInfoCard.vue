<template>
  <el-card class="basic-info-card" v-loading="loading">
    <template #header>
      <div class="card-header">
        <span class="card-title">基本信息</span>
        <el-button type="primary" link @click="handleEdit">
          <el-icon><Edit /></el-icon>
          编辑
        </el-button>
      </div>
    </template>

    <div class="info-content">
      <!-- 客户名称和等级 -->
      <div class="info-header">
        <div class="company-name">
          <h3>{{ customerInfo?.companyName }}</h3>
          <el-tag v-if="customerInfo?.shortName" size="small" style="margin-left: 8px">
            {{ customerInfo.shortName }}
          </el-tag>
        </div>
        <div class="customer-level">
          <el-tag :type="getLevelTagType(customerInfo?.customerLevel)" size="large">
            {{ customerInfo?.customerLevel }}类客户
          </el-tag>
          <el-tag :type="getStatusTagType(customerInfo?.customerStatus)" size="large" style="margin-left: 8px">
            {{ getStatusText(customerInfo?.customerStatus) }}
          </el-tag>
        </div>
      </div>

      <!-- 详细信息 -->
      <el-descriptions :column="2" border class="info-descriptions">
        <el-descriptions-item label="客户编码">
          {{ customerInfo?.customerCode }}
        </el-descriptions-item>
        <el-descriptions-item label="行业">
          {{ customerInfo?.industry }}
          <span v-if="customerInfo?.subIndustry"> / {{ customerInfo.subIndustry }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="企业规模">
          {{ customerInfo?.companyScale || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="客户来源">
          {{ customerInfo?.customerSource || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="负责人">
          {{ customerInfo?.ownerName || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="所属部门">
          {{ customerInfo?.deptName || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="企业官网" :span="2">
          <el-link
            v-if="customerInfo?.website && isValidUrl(customerInfo.website)"
            :href="customerInfo.website"
            target="_blank"
            rel="noopener noreferrer"
            type="primary"
          >
            {{ customerInfo.website }}
          </el-link>
          <span v-else-if="customerInfo?.website" class="invalid-url">
            {{ customerInfo.website }} (无效链接)
          </span>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="企业地址" :span="2">
          {{ getFullAddress() }}
        </el-descriptions-item>
        <el-descriptions-item label="客户标签" :span="2">
          <div class="tags-container">
            <el-tag
              v-for="tag in customerInfo?.tags"
              :key="tag"
              size="small"
              style="margin-right: 8px; margin-bottom: 8px"
            >
              {{ tag }}
            </el-tag>
            <span v-if="!customerInfo?.tags?.length">-</span>
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="下次跟进">
          {{ customerInfo?.nextFollowTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="最后跟进">
          {{ customerInfo?.lastFollowTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">
          {{ customerInfo?.remark || '-' }}
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Edit } from '@element-plus/icons-vue'
import type { CustomerDetail } from '@/types/customer'

interface Props {
  customerInfo?: CustomerDetail
  loading?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  loading: false
})

const emit = defineEmits<{
  edit: []
}>()

// 编辑
const handleEdit = () => {
  emit('edit')
}

// 获取完整地址
const getFullAddress = () => {
  if (!props.customerInfo) return '-'
  const { province, city, district, address } = props.customerInfo
  const parts = [province, city, district, address].filter(Boolean)
  return parts.length > 0 ? parts.join('') : '-'
}

// 验证URL安全性(防止XSS攻击)
const isValidUrl = (url: string): boolean => {
  if (!url) return false
  try {
    const parsedUrl = new URL(url)
    // 只允许 http 和 https 协议
    return parsedUrl.protocol === 'http:' || parsedUrl.protocol === 'https:'
  } catch {
    return false
  }
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
</script>

<style lang="scss" scoped>
.basic-info-card {
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

  .info-content {
    .info-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      padding-bottom: 16px;
      border-bottom: 1px solid var(--color-border-secondary);

      .company-name {
        display: flex;
        align-items: center;

        h3 {
          font-size: 20px;
          font-weight: 700;
          color: var(--color-text-primary);
          margin: 0;
        }
      }

      .customer-level {
        display: flex;
        align-items: center;
      }
    }

    .info-descriptions {
      :deep(.el-descriptions__label) {
        font-weight: 500;
        color: var(--color-text-secondary);
      }

      :deep(.el-descriptions__content) {
        color: var(--color-text-primary);
      }
    }

    .tags-container {
      display: flex;
      flex-wrap: wrap;
    }

    .invalid-url {
      color: var(--color-text-secondary);
      font-style: italic;
    }
  }
}
</style>
