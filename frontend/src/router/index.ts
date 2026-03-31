import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import NProgress from 'nprogress'
import { getToken } from '@/utils/auth'

// 公共路由
export const constantRoutes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', hidden: true }
  },
  {
    path: '/404',
    name: '404',
    component: () => import('@/views/error/404.vue'),
    meta: { title: '404', hidden: true }
  }
]

// 动态路由(需要权限)
export const asyncRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layouts/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'House' }
      }
    ]
  },
  {
    path: '/customer',
    name: 'Customer',
    component: () => import('@/layouts/index.vue'),
    redirect: '/customer/list',
    meta: { title: '客户管理', icon: 'User' },
    children: [
      {
        path: 'list',
        name: 'CustomerList',
        component: () => import('@/views/customer/index.vue'),
        meta: { title: '客户列表', icon: 'List' }
      },
      {
        path: 'detail/:id',
        name: 'CustomerDetail',
        component: () => import('@/views/customer/detail.vue'),
        meta: { title: '客户详情', hidden: true }
      },
      {
        path: 'panorama/:id',
        name: 'CustomerPanorama',
        component: () => import('@/views/customer/panorama/index.vue'),
        meta: { title: '客户全景视图', hidden: true }
      },
      {
        path: 'pool',
        name: 'CustomerPool',
        component: () => import('@/views/customer/pool/index.vue'),
        meta: { title: '公海池', icon: 'Grid' }
      },
      {
        path: 'pool/rules',
        name: 'CustomerPoolRules',
        component: () => import('@/views/customer/pool/rules.vue'),
        meta: { title: '公海池规则管理', hidden: true }
      },
      {
        path: 'grade',
        name: 'CustomerGrade',
        component: () => import('@/views/customer/grade/index.vue'),
        meta: { title: '分级管理', icon: 'Medal' }
      }
    ]
  },
  {
    path: '/opportunity',
    name: 'Opportunity',
    component: () => import('@/layouts/index.vue'),
    redirect: '/opportunity/list',
    meta: { title: '商机管理', icon: 'TrendCharts' },
    children: [
      {
        path: 'list',
        name: 'OpportunityList',
        component: () => import('@/views/opportunity/index.vue'),
        meta: { title: '商机列表', icon: 'List' }
      },
      {
        path: 'kanban',
        name: 'OpportunityKanban',
        component: () => import('@/views/opportunity/kanban.vue'),
        meta: { title: '商机看板', icon: 'Grid' }
      },
      {
        path: 'detail/:id',
        name: 'OpportunityDetail',
        component: () => import('@/views/opportunity/detail.vue'),
        meta: { title: '商机详情', hidden: true }
      }
    ]
  },
  {
    path: '/poc',
    name: 'POC',
    component: () => import('@/layouts/index.vue'),
    redirect: '/poc/list',
    meta: { title: 'POC管理', icon: 'DataAnalysis' },
    children: [
      {
        path: 'list',
        name: 'POCList',
        component: () => import('@/views/poc/index.vue'),
        meta: { title: 'POC列表', icon: 'List' }
      },
      {
        path: 'apply',
        name: 'POCApply',
        component: () => import('@/views/poc/apply.vue'),
        meta: { title: 'POC申请', icon: 'DocumentAdd' }
      },
      {
        path: 'detail/:id',
        name: 'POCDetail',
        component: () => import('@/views/poc/detail.vue'),
        meta: { title: 'POC详情', hidden: true }
      }
    ]
  },
  {
    path: '/assessment',
    name: 'Assessment',
    component: () => import('@/layouts/index.vue'),
    redirect: '/assessment/list',
    meta: { title: '技术评估', icon: 'Cpu' },
    children: [
      {
        path: 'list',
        name: 'AssessmentList',
        component: () => import('@/views/assessment/index.vue'),
        meta: { title: '评估列表', icon: 'List' }
      },
      {
        path: 'detail/:id',
        name: 'AssessmentDetail',
        component: () => import('@/views/assessment/detail.vue'),
        meta: { title: '评估详情', hidden: true }
      }
    ]
  },
  {
    path: '/channel',
    name: 'Channel',
    component: () => import('@/layouts/index.vue'),
    redirect: '/channel/partner',
    meta: { title: '渠道管理', icon: 'Share' },
    children: [
      {
        path: 'partner',
        name: 'ChannelPartner',
        component: () => import('@/views/channel/partner/index.vue'),
        meta: { title: '渠道伙伴', icon: 'OfficeBuilding' }
      },
      {
        path: 'project',
        name: 'ChannelProject',
        component: () => import('@/views/channel/project/index.vue'),
        meta: { title: '项目报备', icon: 'Document' }
      },
      {
        path: 'rebate',
        name: 'ChannelRebate',
        component: () => import('@/views/channel/rebate/index.vue'),
        meta: { title: '返利管理', icon: 'Money' }
      }
    ]
  },
  {
    path: '/system',
    name: 'System',
    component: () => import('@/layouts/index.vue'),
    redirect: '/system/user',
    meta: { title: '系统管理', icon: 'Setting' },
    children: [
      {
        path: 'user',
        name: 'SystemUser',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理', icon: 'User' }
      },
      {
        path: 'role',
        name: 'SystemRole',
        component: () => import('@/views/system/role/index.vue'),
        meta: { title: '角色管理', icon: 'UserFilled' }
      },
      {
        path: 'permission',
        name: 'SystemPermission',
        component: () => import('@/views/system/permission/index.vue'),
        meta: { title: '权限管理', icon: 'Lock' }
      },
      {
        path: 'dept',
        name: 'SystemDept',
        component: () => import('@/views/system/dept/index.vue'),
        meta: { title: '部门管理', icon: 'OfficeBuilding' }
      },
      {
        path: 'dict',
        name: 'SystemDict',
        component: () => import('@/views/system/dict/index.vue'),
        meta: { title: '字典管理', icon: 'Collection' }
      },
      {
        path: 'log',
        name: 'SystemLog',
        component: () => import('@/views/system/log/index.vue'),
        meta: { title: '操作日志', icon: 'Document' }
      }
    ]
  },
  // 404页面必须放在最后
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404',
    meta: { hidden: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes: [...constantRoutes, ...asyncRoutes],
  scrollBehavior: () => ({ left: 0, top: 0 })
})

// 路由守卫
router.beforeEach((to, from, next) => {
  NProgress.start()
  
  // 设置页面标题
  document.title = `${to.meta.title || '首页'} - 芯动力科技CRM系统`
  
  // 判断是否需要登录
  if (to.path === '/login') {
    // 如果已登录，重定向到首页或原目标页面
    if (getToken()) {
      const redirect = to.query.redirect as string
      next(redirect || '/dashboard')
    } else {
      next()
    }
    NProgress.done()
  } else {
    // 判断是否有token
    if (getToken()) {
      next()
    } else {
      // 保存原目标路径，登录后重定向
      next(`/login?redirect=${encodeURIComponent(to.fullPath)}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})

export default router
