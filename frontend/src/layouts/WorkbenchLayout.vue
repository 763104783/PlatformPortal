<!--
  工作台布局组件
  布局：左侧导航（200px） + 右侧操作区
  说明：
    - 工作台面向业务APP搭建人员和模型算法工程师
    - 提供平台信息查看、应用交付配置、业务编排、模型管理等功能
    - 当前版本以数据管理为主，可视化编排为预留扩展
  权限：需登录（路由守卫已校验）
-->
<template>
  <div class="workbench-layout tech-bg">
    <!-- 顶部导航栏 -->
    <header class="workbench-header">
      <div class="header-left">
        <el-button text @click="$router.push('/')">
          <el-icon><Back /></el-icon> 返回首页
        </el-button>
        <span class="header-title">工作台</span>
      </div>
      <div class="header-right">
        <span class="user-name">{{ userStore.username }}</span>
        <el-button type="danger" text @click="handleLogout">退出</el-button>
      </div>
    </header>

    <div class="workbench-body">
      <!-- 左侧导航 -->
      <aside class="workbench-sidebar">
        <el-menu
          :default-active="activeMenu"
          router
          background-color="transparent"
          text-color="#B8A9C9"
          active-text-color="#9B4DCA"
        >
          <el-menu-item index="/workbench/platform">
            <el-icon><InfoFilled /></el-icon>
            <span>平台信息</span>
          </el-menu-item>
          <el-menu-item index="/workbench/app">
            <el-icon><Grid /></el-icon>
            <span>应用交付</span>
          </el-menu-item>
          <el-menu-item index="/workbench/process">
            <el-icon><Connection /></el-icon>
            <span>业务编排</span>
          </el-menu-item>
          <el-menu-item index="/workbench/model">
            <el-icon><Cpu /></el-icon>
            <span>模型管理</span>
          </el-menu-item>
        </el-menu>
      </aside>

      <!-- 右侧操作区 -->
      <main class="workbench-main">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
/**
 * 工作台布局逻辑
 */
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
.workbench-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.workbench-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 24px;
  background: rgba(13, 11, 26, 0.95);
  border-bottom: 1px solid var(--border-color);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-title {
  font-size: 18px;
  font-weight: 600;
  color: #fff;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-name {
  color: #B8A9C9;
}

.workbench-body {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.workbench-sidebar {
  width: 200px;
  background: rgba(26, 16, 53, 0.8);
  border-right: 1px solid var(--border-color);
  padding: 12px 0;
}

.workbench-sidebar .el-menu {
  border-right: none;
}

.workbench-main {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
}

/* ========================================
 * Element Plus 组件紫色科技风主题覆盖
 * 使搜索框、表格、卡片、按钮、分页等组件与深色背景协调
 * ======================================== */

/* 卡片组件：深色背景替代白色 */
:deep(.el-card) {
  --el-card-bg-color: rgba(255, 255, 255, 0.04);
  background: rgba(255, 255, 255, 0.04) !important;
  border: 1px solid var(--border-color) !important;
  color: #e0d6eb;
  border-radius: 12px;
}

:deep(.el-card__header) {
  border-bottom: 1px solid rgba(123, 45, 142, 0.15);
  color: #fff;
  font-weight: 600;
}

/* 搜索输入框：深色背景 + 浅色文字 */
:deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.06) !important;
  border: 1px solid var(--border-color);
  box-shadow: none !important;
  border-radius: 8px;
}

:deep(.el-input__wrapper:hover) {
  border-color: var(--primary-light);
}

:deep(.el-input__wrapper.is-focus) {
  border-color: var(--primary-light) !important;
  box-shadow: 0 0 0 2px rgba(155, 77, 202, 0.15) !important;
}

:deep(.el-input__inner) {
  color: #e0d6eb !important;
}

:deep(.el-input__inner::placeholder) {
  color: #6B5B7B !important;
}

:deep(.el-input__prefix .el-icon) {
  color: #6B5B7B;
}

:deep(.el-input__clear) {
  color: #6B5B7B;
  background: transparent;
}

/* 表格：深色背景 + 紫色边框线 */
:deep(.el-table) {
  --el-table-bg-color: rgba(255, 255, 255, 0.03);
  --el-table-tr-bg-color: rgba(255, 255, 255, 0.03);
  --el-table-header-bg-color: rgba(123, 45, 142, 0.12);
  --el-table-header-text-color: #B8A9C9;
  --el-table-text-color: #e0d6eb;
  --el-table-border-color: rgba(123, 45, 142, 0.15);
  --el-table-row-hover-bg-color: rgba(123, 45, 142, 0.15);
  --el-table-current-row-bg-color: rgba(123, 45, 142, 0.2);
  --el-table-fill-color: rgba(255, 255, 255, 0.03);
  border-radius: 12px;
  overflow: hidden;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td.el-table__cell) {
  background: rgba(123, 45, 142, 0.06) !important;
}

:deep(.el-table__fixed-right-patch) {
  background: transparent;
}

/* 分页器 */
:deep(.el-pagination) {
  --el-pagination-bg-color: transparent;
  --el-pagination-text-color: #B8A9C9;
  --el-pagination-button-bg-color: rgba(255, 255, 255, 0.06);
  --el-pagination-hover-color: var(--primary-light);
}

:deep(.el-pagination .btn-prev),
:deep(.el-pagination .btn-next),
:deep(.el-pagination .el-pager li) {
  background: rgba(255, 255, 255, 0.06) !important;
  color: #B8A9C9 !important;
  border: 1px solid rgba(123, 45, 142, 0.2);
}

:deep(.el-pagination .el-pager li.is-active) {
  background: var(--primary-color) !important;
  color: #fff !important;
  border-color: var(--primary-color);
}

:deep(.el-pagination .el-pager li:hover) {
  color: var(--primary-light) !important;
}

:deep(.el-pagination__total) {
  color: #6B5B7B;
}

/* Tabs标签页 */
:deep(.el-tabs__item) {
  color: #6B5B7B;
}

:deep(.el-tabs__item.is-active) {
  color: var(--primary-light);
}

:deep(.el-tabs__active-bar) {
  background-color: var(--primary-light);
}

:deep(.el-tabs__nav-wrap::after) {
  background-color: rgba(123, 45, 142, 0.15);
}

:deep(.el-tabs__item:hover) {
  color: var(--primary-light);
}

/* 弹窗：深色背景 */
:deep(.el-dialog) {
  background: #1A1035 !important;
  border: 1px solid var(--border-color);
  border-radius: 16px;
}

:deep(.el-dialog__header) {
  border-bottom: 1px solid rgba(123, 45, 142, 0.15);
  padding-bottom: 16px;
}

:deep(.el-dialog__title) {
  color: #fff;
  font-weight: 600;
}

:deep(.el-dialog__headerbtn .el-dialog__close) {
  color: #6B5B7B;
}

:deep(.el-dialog__body) {
  color: #B8A9C9;
}

/* 描述列表 */
:deep(.el-descriptions) {
  --el-descriptions-item-bordered-label-background: rgba(123, 45, 142, 0.12) !important;
  --el-descriptions-table-border: rgba(123, 45, 142, 0.15) !important;
}

:deep(.el-descriptions__label) {
  color: #B8A9C9 !important;
  background: rgba(123, 45, 142, 0.12) !important;
}

:deep(.el-descriptions__content) {
  color: #e0d6eb !important;
  background: rgba(255, 255, 255, 0.03) !important;
}

/* 弹窗内描述列表单元格边框 */
:deep(.el-descriptions--bordered .el-descriptions__cell) {
  border-color: rgba(123, 45, 142, 0.15) !important;
}

/* Tag标签 */
:deep(.el-tag) {
  --el-tag-bg-color: rgba(123, 45, 142, 0.12);
  --el-tag-border-color: rgba(123, 45, 142, 0.2);
  --el-tag-text-color: #B8A9C9;
}

:deep(.el-tag--info) {
  --el-tag-bg-color: rgba(123, 45, 142, 0.12);
  --el-tag-border-color: rgba(123, 45, 142, 0.2);
  --el-tag-text-color: #B8A9C9;
}

:deep(.el-tag--success) {
  --el-tag-bg-color: rgba(46, 160, 67, 0.12);
  --el-tag-border-color: rgba(46, 160, 67, 0.25);
  --el-tag-text-color: #56d364;
}

/* 空状态文字 */
:deep(.el-empty__description p) {
  color: #6B5B7B;
}

/* Loading遮罩 */
:deep(.el-loading-mask) {
  background: rgba(13, 11, 26, 0.7);
}

/* 表格内滚动条 */
:deep(.el-table__body-wrapper)::-webkit-scrollbar {
  width: 4px;
  height: 4px;
}

:deep(.el-table__body-wrapper)::-webkit-scrollbar-thumb {
  background: var(--primary-color);
  border-radius: 2px;
}
</style>
