/**
 * XX平台 路由配置
 * 
 * 页面层级结构：
 * 第一层：门户欢迎页（/）
 * 第二层：
 *   - 公司产品业绩宣贯页（/showcase）
 *   - 产品展示页（/product）- 含侧边栏树形导航
 *   - 工作台（/workbench）- 需登录
 *   - 后台管理（/admin）- 需管理员权限
 * 
 * 权限说明：
 * - 公开页面：门户、宣贯、产品展示、登录
 * - 需登录：工作台
 * - 需管理员：后台管理
 */

import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  // ==================== 第一层：门户欢迎页 ====================
  {
    path: '/',
    name: 'PortalHome',
    component: () => import('../views/PortalHome.vue'),
    meta: { title: '首页' }
  },

  // ==================== 第二层-1：公司产品业绩宣贯 ====================
  {
    path: '/showcase',
    name: 'Showcase',
    component: () => import('../views/Showcase.vue'),
    meta: { title: '产品业绩宣贯' }
  },
  {
    path: '/showcase/:id',
    name: 'ShowcaseDetail',
    component: () => import('../views/ShowcaseDetail.vue'),
    meta: { title: '宣贯详情' }
  },

  // ==================== 第二层-2：产品展示（侧边栏布局） ====================
  {
    path: '/product',
    component: () => import('../layouts/ProductLayout.vue'),
    meta: { title: '产品展示' },
    children: [
      {
        path: '',
        redirect: '/product/app'
      },
      {
        path: 'app',
        name: 'ProductApp',
        component: () => import('../views/product/AppDelivery.vue'),
        meta: { title: '应用交付' }
      },
      {
        path: 'process',
        name: 'ProductProcess',
        component: () => import('../views/product/BizProcess.vue'),
        meta: { title: '业务编排' }
      },
      {
        path: 'model',
        name: 'ProductModel',
        component: () => import('../views/product/ModelManage.vue'),
        meta: { title: '模型管理' }
      }
    ]
  },

  // ==================== 第二层-3：工作台（需登录） ====================
  {
    path: '/workbench',
    component: () => import('../layouts/WorkbenchLayout.vue'),
    meta: { title: '工作台', requiresAuth: true },
    children: [
      {
        path: '',
        redirect: '/workbench/platform'
      },
      {
        path: 'platform',
        name: 'WorkbenchPlatform',
        component: () => import('../views/workbench/PlatformInfo.vue'),
        meta: { title: '平台信息' }
      },
      {
        path: 'app',
        name: 'WorkbenchApp',
        component: () => import('../views/workbench/AppDelivery.vue'),
        meta: { title: '应用交付' }
      },
      {
        path: 'process',
        name: 'WorkbenchProcess',
        component: () => import('../views/workbench/BizProcess.vue'),
        meta: { title: '业务编排' }
      },
      {
        path: 'model',
        name: 'WorkbenchModel',
        component: () => import('../views/workbench/ModelManage.vue'),
        meta: { title: '模型管理' }
      }
    ]
  },

  // ==================== 第二层-4：后台管理（需管理员） ====================
  {
    path: '/admin',
    component: () => import('../layouts/AdminLayout.vue'),
    meta: { title: '后台管理', requiresAuth: true, requiresAdmin: true },
    children: [
      {
        path: '',
        redirect: '/admin/platform'
      },
      {
        path: 'platform',
        name: 'AdminPlatform',
        component: () => import('../views/admin/AdminPlatform.vue'),
        meta: { title: '平台信息管理' }
      },
      {
        path: 'app-type',
        name: 'AdminAppType',
        component: () => import('../views/admin/AdminAppType.vue'),
        meta: { title: '应用类型管理' }
      },
      {
        path: 'app',
        name: 'AdminApp',
        component: () => import('../views/admin/AdminApp.vue'),
        meta: { title: '交付应用管理' }
      },
      {
        path: 'process-tpl',
        name: 'AdminProcessTpl',
        component: () => import('../views/admin/AdminProcessTpl.vue'),
        meta: { title: '流程模板管理' }
      },
      {
        path: 'process',
        name: 'AdminProcess',
        component: () => import('../views/admin/AdminProcess.vue'),
        meta: { title: '业务流程管理' }
      },
      {
        path: 'data-model',
        name: 'AdminDataModel',
        component: () => import('../views/admin/AdminDataModel.vue'),
        meta: { title: '数据实体模型管理' }
      },
      {
        path: 'algo-model',
        name: 'AdminAlgoModel',
        component: () => import('../views/admin/AdminAlgoModel.vue'),
        meta: { title: '算法能力模型管理' }
      },
      {
        path: 'showcase',
        name: 'AdminShowcase',
        component: () => import('../views/admin/AdminShowcase.vue'),
        meta: { title: '宣贯数据管理' }
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('../views/admin/AdminUsers.vue'),
        meta: { title: '用户管理' }
      }
    ]
  },

  // ==================== 登录页 ====================
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

/**
 * 全局路由守卫
 * 1. 设置页面标题
 * 2. 检查登录状态（requiresAuth）
 * 3. 检查管理员权限（requiresAdmin）
 */
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - XX平台` : 'XX平台'

  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role')

  // 需要登录的路由
  if (to.meta.requiresAuth && !token) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }

  // 需要管理员权限的路由
  if (to.meta.requiresAdmin && role !== 'ADMIN') {
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }

  next()
})

export default router
