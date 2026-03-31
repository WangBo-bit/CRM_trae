<template>
  <div class="layout-container">
    <!-- 侧边栏 -->
    <Sidebar class="sidebar-container" />
    
    <!-- 主内容区 -->
    <div class="main-container" :class="{ 'sidebar-collapsed': !sidebarOpened }">
      <!-- 顶部导航 -->
      <Navbar />
      
      <!-- 主内容 -->
      <AppMain />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useAppStore } from '@/stores/modules/app'
import Sidebar from './components/Sidebar.vue'
import Navbar from './components/Navbar.vue'
import AppMain from './components/AppMain.vue'

const appStore = useAppStore()
const sidebarOpened = computed(() => appStore.sidebarOpened)
</script>

<style lang="scss" scoped>
.layout-container {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

.sidebar-container {
  width: 220px;
  height: 100%;
  transition: width 0.3s;
  background-color: #1F2937;
  overflow: hidden;
  
  &.collapsed {
    width: 64px;
  }
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 100%;
  margin-left: -220px;
  padding-left: 220px;
  transition: all 0.3s;
  background-color: var(--color-bg-primary);
  
  &.sidebar-collapsed {
    margin-left: -64px;
    padding-left: 64px;
  }
}
</style>
