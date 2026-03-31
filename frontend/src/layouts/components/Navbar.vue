<template>
  <div class="navbar">
    <!-- 左侧 -->
    <div class="navbar-left">
      <!-- 折叠按钮 -->
      <div class="hamburger" @click="toggleSidebar">
        <el-icon :size="20">
          <component :is="sidebarOpened ? 'Fold' : 'Expand'" />
        </el-icon>
      </div>
      
      <!-- 面包屑 -->
      <el-breadcrumb separator="/">
        <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path">
          {{ item.meta?.title }}
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    
    <!-- 右侧 -->
    <div class="navbar-right">
      <!-- 主题切换 -->
      <div class="navbar-item" @click="toggleTheme">
        <el-icon :size="20">
          <component :is="theme === 'light' ? 'Moon' : 'Sunny'" />
        </el-icon>
      </div>
      
      <!-- 全屏 -->
      <div class="navbar-item" @click="toggleFullscreen">
        <el-icon :size="20">
          <FullScreen />
        </el-icon>
      </div>
      
      <!-- 用户信息 -->
      <el-dropdown class="user-dropdown" trigger="click" @command="handleCommand">
        <div class="user-info">
          <el-avatar :size="32" :src="userInfo?.avatar">
            {{ userInfo?.realName?.charAt(0) }}
          </el-avatar>
          <span class="user-name">{{ userInfo?.realName }}</span>
          <el-icon><ArrowDown /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              <el-icon><User /></el-icon>
              个人中心
            </el-dropdown-item>
            <el-dropdown-item command="password">
              <el-icon><Lock /></el-icon>
              修改密码
            </el-dropdown-item>
            <el-dropdown-item divided command="logout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useAppStore } from '@/stores/modules/app'
import { useUserStore } from '@/stores'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()
const userStore = useUserStore()

const sidebarOpened = computed(() => appStore.sidebarOpened)
const theme = computed(() => appStore.theme)
const userInfo = computed(() => userStore.userInfo)

// 面包屑
const breadcrumbs = computed(() => {
  return route.matched.filter(item => item.meta?.title)
})

// 切换侧边栏
const toggleSidebar = () => {
  appStore.toggleSidebar()
}

// 切换主题
const toggleTheme = () => {
  appStore.toggleTheme()
}

// 切换全屏
const toggleFullscreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
  } else {
    document.exitFullscreen()
  }
}

// 处理下拉菜单命令
const handleCommand = (command: string) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'password':
      router.push('/password')
      break
    case 'logout':
      handleLogout()
      break
  }
}

// 退出登录
const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await userStore.logoutAction()
    router.push('/login')
  })
}
</script>

<style lang="scss" scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 56px;
  padding: 0 24px;
  background-color: var(--color-bg-white);
  border-bottom: 1px solid var(--color-border-secondary);
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.navbar-left {
  display: flex;
  align-items: center;
  
  .hamburger {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    margin-right: 12px;
    cursor: pointer;
    border-radius: 4px;
    transition: background-color 0.3s;
    
    &:hover {
      background-color: var(--color-bg-secondary);
    }
  }
  
  .el-breadcrumb {
    font-size: 14px;
  }
}

.navbar-right {
  display: flex;
  align-items: center;
  gap: 8px;
  
  .navbar-item {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 36px;
    height: 36px;
    cursor: pointer;
    border-radius: 4px;
    transition: background-color 0.3s;
    
    &:hover {
      background-color: var(--color-bg-secondary);
    }
  }
  
  .user-dropdown {
    margin-left: 8px;
    cursor: pointer;
    
    .user-info {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 4px 8px;
      border-radius: 4px;
      transition: background-color 0.3s;
      
      &:hover {
        background-color: var(--color-bg-secondary);
      }
      
      .user-name {
        font-size: 14px;
        color: var(--color-text-primary);
      }
    }
  }
}
</style>
