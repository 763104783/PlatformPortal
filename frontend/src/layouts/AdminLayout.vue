<!--
  后台管理布局组件
  布局：左侧树形导航（220px） + 右侧管理区
  侧边栏结构：
    - 平台信息
    - 应用交付（应用类型管理 / 交付应用管理）
    - 业务编排（流程模板管理 / 业务流程管理）
    - 模型管理（数据实体模型 / 算法能力模型）
    - 宣贯管理
    - 用户管理
  权限：需管理员登录（路由守卫已校验）
-->
<template>
  <el-container class="admin-layout">
    <!-- 左侧导航栏 -->
    <el-aside width="220px" class="admin-aside">
      <!-- Logo区域 -->
      <div class="admin-logo">
        <h3>XX平台管理</h3>
      </div>

      <!-- 导航菜单（树形结构） -->
      <el-menu
        :default-active="activeMenu"
        :default-openeds="['app-group', 'process-group', 'model-group']"
        router
        background-color="#1A1035"
        text-color="#B8A9C9"
        active-text-color="#9B4DCA"
      >
        <!-- 平台信息 -->
        <el-menu-item index="/admin/platform">
          <el-icon><InfoFilled /></el-icon>
          <span>平台信息</span>
        </el-menu-item>

        <!-- 应用交付分组 -->
        <el-sub-menu index="app-group">
          <template #title>
            <el-icon><Grid /></el-icon>
            <span>应用交付</span>
          </template>
          <el-menu-item index="/admin/app-type">应用类型管理</el-menu-item>
          <el-menu-item index="/admin/app">交付应用管理</el-menu-item>
        </el-sub-menu>

        <!-- 业务编排分组 -->
        <el-sub-menu index="process-group">
          <template #title>
            <el-icon><Connection /></el-icon>
            <span>业务编排</span>
          </template>
          <el-menu-item index="/admin/process-tpl">流程模板管理</el-menu-item>
          <el-menu-item index="/admin/process">业务流程管理</el-menu-item>
        </el-sub-menu>

        <!-- 模型管理分组 -->
        <el-sub-menu index="model-group">
          <template #title>
            <el-icon><Cpu /></el-icon>
            <span>模型管理</span>
          </template>
          <el-menu-item index="/admin/data-model">数据实体模型</el-menu-item>
          <el-menu-item index="/admin/algo-model">算法能力模型</el-menu-item>
        </el-sub-menu>

        <!-- 宣贯管理 -->
        <el-menu-item index="/admin/showcase">
          <el-icon><Document /></el-icon>
          <span>宣贯管理</span>
        </el-menu-item>

        <!-- 用户管理 -->
        <el-menu-item index="/admin/users">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
      </el-menu>

      <!-- 返回前台按钮 -->
      <div class="admin-back">
        <el-button type="primary" plain @click="$router.push('/')">返回前台</el-button>
      </div>
    </el-aside>

    <!-- 右侧主内容区 -->
    <el-container>
      <!-- 顶部栏：页面标题 + 用户信息 -->
      <el-header class="admin-header">
        <span class="header-title">{{ $route.meta.title }}</span>
        <div class="header-right">
          <span class="admin-name">{{ userStore.username }}</span>
          <el-button type="danger" text @click="handleLogout">退出</el-button>
        </div>
      </el-header>

      <!-- 子路由内容区 -->
      <el-main class="admin-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
/**
 * 后台管理布局逻辑
 * - 根据当前路由高亮菜单项
 * - 提供退出登录功能
 */
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

/** 当前激活菜单项（绑定路由路径） */
const activeMenu = computed(() => route.path)

/** 退出登录 */
const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
}

.admin-aside {
  background: #1A1035;
  display: flex;
  flex-direction: column;
}

.admin-logo {
  padding: 20px;
  text-align: center;
  border-bottom: 1px solid rgba(155, 77, 202, 0.2);
}

.admin-logo h3 {
  color: #9B4DCA;
  font-size: 18px;
}

.admin-aside .el-menu {
  border-right: none;
  flex: 1;
}

.admin-back {
  padding: 16px;
  text-align: center;
}

.admin-header {
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.header-title {
  font-size: 18px;
  font-weight: 600;
  color: #2C1654;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.admin-name {
  color: #6B5B7B;
}

.admin-main {
  background: #F5F3F7;
}
</style>
