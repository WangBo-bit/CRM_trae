import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAppStore = defineStore('app', () => {
  // 侧边栏状态
  const sidebar = ref({
    opened: localStorage.getItem('sidebarStatus') ? !!+localStorage.getItem('sidebarStatus') : true,
    withoutAnimation: false
  })
  
  // 设备类型
  const device = ref<string>('desktop')
  
  // 主题
  const theme = ref<string>(localStorage.getItem('theme') || 'light')

  // 计算属性
  const sidebarOpened = computed(() => sidebar.value.opened)

  // 切换侧边栏
  const toggleSidebar = (withoutAnimation: boolean = false) => {
    sidebar.value.opened = !sidebar.value.opened
    sidebar.value.withoutAnimation = withoutAnimation
    
    if (sidebar.value.opened) {
      localStorage.setItem('sidebarStatus', '1')
    } else {
      localStorage.setItem('sidebarStatus', '0')
    }
  }

  // 关闭侧边栏
  const closeSidebar = (withoutAnimation: boolean = false) => {
    sidebar.value.opened = false
    sidebar.value.withoutAnimation = withoutAnimation
    localStorage.setItem('sidebarStatus', '0')
  }

  // 切换设备
  const toggleDevice = (value: string) => {
    device.value = value
  }

  // 切换主题
  const toggleTheme = () => {
    theme.value = theme.value === 'light' ? 'dark' : 'light'
    localStorage.setItem('theme', theme.value)
    document.documentElement.setAttribute('data-theme', theme.value)
  }

  return {
    sidebar,
    device,
    theme,
    sidebarOpened,
    toggleSidebar,
    closeSidebar,
    toggleDevice,
    toggleTheme
  }
})
