import { createRouter, createWebHistory } from 'vue-router'

/**
 * 路由配置
 */
const routes = [
  {
    path: '/',
    name: 'PortalHome',
    component: () => import('../views/PortalHome.vue'),
    meta: { title: '首页' }
  },
  {
    path: '/apps',
    name: 'AppNav',
    component: () => import('../views/AppNav.vue'),
    meta: { title: '应用导航' }
  },
  {
    path: '/showcase',
    name: 'Showcase',
    component: () => import('../views/Showcase.vue'),
    meta: { title: '产品宣贯' }
  },
  {
    path: '/showcase/:id',
    name: 'ShowcaseDetail',
    component: () => import('../views/ShowcaseDetail.vue'),
    meta: { title: '宣贯详情' }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/admin',
    component: () => import('../layouts/AdminLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: 'apps',
        name: 'AdminApps',
        component: () => import('../views/admin/AdminApps.vue'),
        meta: { title: '应用管理' }
      },
      {
        path: 'categories',
        name: 'AdminCategories',
        component: () => import('../views/admin/AdminCategories.vue'),
        meta: { title: '分类管理' }
      },
      {
        path: 'showcase',
        name: 'AdminShowcase',
        component: () => import('../views/admin/AdminShowcase.vue'),
        meta: { title: '宣贯管理' }
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('../views/admin/AdminUsers.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'settings',
        name: 'AdminSettings',
        component: () => import('../views/admin/AdminSettings.vue'),
        meta: { title: '平台设置' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - XX平台` : 'XX平台'

  // 需要登录的路由
  if (to.meta.requiresAuth) {
    const token = localStorage.getItem('token')
    const role = localStorage.getItem('role')
    if (!token || role !== 'ADMIN') {
      next({ name: 'Login', query: { redirect: to.fullPath } })
      return
    }
  }
  next()
})

export default router
