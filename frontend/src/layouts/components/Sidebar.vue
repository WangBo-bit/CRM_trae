<template>
  <div class="sidebar" :class="{ 'sidebar--collapsed': !sidebarOpened }">
    <!-- Logo -->
    <div class="sidebar-logo">
      <img src="@/assets/images/logo.png" alt="芯动力科技" class="logo-img" />
      <span v-show="sidebarOpened" class="logo-text">芯动力CRM</span>
    </div>
    
    <!-- 菜单 -->
    <el-scrollbar class="sidebar-menu-container">
      <el-menu
        :default-active="activeMenu"
        :collapse="!sidebarOpened"
        :collapse-transition="false"
        :unique-opened="true"
        background-color="#1F2937"
        text-color="#D1D5DB"
        active-text-color="#FFFFFF"
        class="sidebar-menu"
        router
      >
        <template v-for="route in routes" :key="route.path">
          <!-- 没有子菜单 -->
          <template v-if="!route.children && !route.meta?.hidden">
            <el-menu-item :index="route.path">
              <el-icon v-if="route.meta?.icon">
                <component :is="route.meta.icon" />
              </el-icon>
              <template #title>{{ route.meta?.title }}</template>
            </el-menu-item>
          </template>
          
          <!-- 有子菜单 -->
          <template v-else-if="route.children && !route.meta?.hidden">
            <el-sub-menu :index="route.path">
              <template #title>
                <el-icon v-if="route.meta?.icon">
                  <component :is="route.meta.icon" />
                </el-icon>
                <span>{{ route.meta?.title }}</span>
              </template>
              
              <template v-for="child in route.children" :key="child.path">
                <el-menu-item
                  v-if="!child.meta?.hidden"
                  :index="`${route.path}/${child.path}`"
                >
                  <el-icon v-if="child.meta?.icon">
                    <component :is="child.meta.icon" />
                  </el-icon>
                  <template #title>{{ child.meta?.title }}</template>
                </el-menu-item>
              </template>
            </el-sub-menu>
          </template>
        </template>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/stores/modules/app'
import { asyncRoutes } from '@/router'

const route = useRoute()
const appStore = useAppStore()

const sidebarOpened = computed(() => appStore.sidebarOpened)
const activeMenu = computed(() => route.path)
const routes = computed(() => asyncRoutes.filter(r => !r.meta?.hidden))
</script>

<style lang="scss" scoped>
.sidebar {
  width: 220px;
  height: 100%;
  background-color: #1F2937;
  transition: width 0.3s;
  overflow: hidden;
  
  &--collapsed {
    width: 64px;
    
    .logo-text {
      display: none;
    }
  }
}

.sidebar-logo {
  display: flex;
  align-items: center;
  height: 56px;
  padding: 0 20px;
  background-color: rgba(0, 0, 0, 0.2);
  
  .logo-img {
    width: 32px;
    height: 32px;
    margin-right: 12px;
  }
  
  .logo-text {
    font-size: 16px;
    font-weight: 600;
    color: #FFFFFF;
    white-space: nowrap;
  }
}

.sidebar-menu-container {
  height: calc(100% - 56px);
}

.sidebar-menu {
  border-right: none;
  
  &:not(.el-menu--collapse) {
    width: 220px;
  }
  
  .el-menu-item {
    height: 48px;
    line-height: 48px;
    
    &:hover {
      background-color: rgba(255, 255, 255, 0.1) !important;
    }
    
    &.is-active {
      background-color: rgba(59, 130, 246, 0.2) !important;
      border-right: 3px solid #3B82F6;
    }
  }
  
  .el-sub-menu {
    .el-menu-item {
      padding-left: 50px !important;
    }
  }
}
</style>
