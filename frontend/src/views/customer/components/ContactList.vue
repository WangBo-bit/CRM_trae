<template>
  <div class="contact-list-container">
    <!-- 操作栏 -->
    <div class="toolbar">
      <el-button type="primary" size="small" @click="handleCreate">
        <el-icon><Plus /></el-icon>
        新建联系人
      </el-button>
    </div>

    <!-- 联系人列表 -->
    <el-table :data="contactList" border stripe v-loading="loading">
      <el-table-column prop="name" label="姓名" width="120" />

      <el-table-column prop="position" label="职位" width="120" />

      <el-table-column prop="department" label="部门" width="120" />

      <el-table-column prop="mobile" label="手机" width="130" />

      <el-table-column prop="email" label="邮箱" min-width="180" />

      <el-table-column prop="decisionLevel" label="决策层级" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="getDecisionLevelTagType(row.decisionLevel)" size="small">
            {{ getDecisionLevelText(row.decisionLevel) }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="isPrimary" label="主要联系人" width="100" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.isPrimary === 1" type="success" size="small">是</el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="handleEdit(row)">
            编辑
          </el-button>
          <el-button type="danger" link size="small" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 空状态 -->
    <el-empty v-if="contactList.length === 0" description="暂无联系人" />

    <!-- 联系人表单弹窗 -->
    <ContactForm
      v-model:visible="formVisible"
      :customer-id="customerId"
      :contact-id="currentContactId"
      :mode="formMode"
      @success="handleFormSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { customerApi } from '@/api/customer'
import type { Contact } from '@/types/customer'
import ContactForm from './ContactForm.vue'

// Props
interface Props {
  customerId: string
}

const props = defineProps<Props>()

// 联系人列表
const contactList = ref<Contact[]>([])
const loading = ref(false)

// 表单弹窗
const formVisible = ref(false)
const currentContactId = ref('')
const formMode = ref<'create' | 'edit'>('create')

// 获取联系人列表
const getContactList = async () => {
  loading.value = true
  try {
    const response = await customerApi.getCustomerContacts(props.customerId)
    if (response.code === 200) {
      contactList.value = response.data
    }
  } catch (error) {
    console.error('获取联系人列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 新建联系人
const handleCreate = () => {
  currentContactId.value = ''
  formMode.value = 'create'
  formVisible.value = true
}

// 编辑联系人
const handleEdit = (row: Contact) => {
  currentContactId.value = row.id
  formMode.value = 'edit'
  formVisible.value = true
}

// 删除联系人
const handleDelete = (row: Contact) => {
  ElMessageBox.confirm(
    `确定要删除联系人"${row.name}"吗?`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await customerApi.deleteContact(props.customerId, row.id)
      ElMessage.success('删除成功')
      getContactList()
    } catch (error) {
      console.error('删除联系人失败:', error)
    }
  })
}

// 表单提交成功
const handleFormSuccess = () => {
  formVisible.value = false
  getContactList()
}

// 获取决策层级标签类型
const getDecisionLevelTagType = (level: string): '' | 'success' | 'warning' | 'info' | 'danger' => {
  const typeMap: Record<string, '' | 'success' | 'warning' | 'info' | 'danger'> = {
    decision_maker: 'danger',
    influencer: 'warning',
    executor: 'info'
  }
  return typeMap[level] || ''
}

// 获取决策层级文本
const getDecisionLevelText = (level: string) => {
  const textMap: Record<string, string> = {
    decision_maker: '决策者',
    influencer: '影响者',
    executor: '执行者'
  }
  return textMap[level] || level
}

// 监听customerId变化
watch(() => props.customerId, () => {
  if (props.customerId) {
    getContactList()
  }
}, { immediate: true })
</script>

<style lang="scss" scoped>
.contact-list-container {
  .toolbar {
    margin-bottom: 16px;
  }
}
</style>
