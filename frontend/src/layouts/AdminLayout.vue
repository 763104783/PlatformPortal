<template>
  <el-container class="admin-layout">
    <!-- 侧边栏 -->
    <el-aside width="220px" class="admin-aside">
      <div class="admin-logo">
        <h3>XX平台管理</h3>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#1A1035"
        text-color="#B8A9C9"
        active-text-color="#9B4DCA"
      >
        <el-menu-item index="/admin/apps">
          <el-icon><Grid /></el-icon>
          <span>应用管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/categories">
          <el-icon><FolderOpened /></el-icon>
          <span>分类管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/showcase">
          <el-icon><Document /></el-icon>
          <span>宣贯管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/users">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/settings">
          <el-icon><Setting /></el-icon>
          <span>平台设置</span>
        </el-menu-item>
      </el-menu>
      <div class="admin-back">
        <el-button type="primary" plain @click="$router.push('/')">
          返回前台
        </el-button>
      </div>
    </el-aside>

    <!-- 主内容区 -->
    <el-container>
      <el-header class="admin-header">
        <span class="header-title">{{ $route.meta.title }}</span>
        <div class="header-right">
          <span class="admin-name">{{ userStore.username }}</span>
          <el-button type="danger" text @click="handleLogout">退出</el-button>
        </div>
      </el-header>
      <el-main class="admin-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

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
