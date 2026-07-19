<!--
  产品展示页布局组件
  布局：左侧树形导航（240px） + 右侧业务内容区
  侧边栏树结构：
    - 应用交付（按应用类型展开子节点）
    - 业务编排（按业务类型展开子节点）
    - 模型管理（数据实体 / 算法能力）
  功能：
    1. 动态加载应用类型列表作为"应用交付"子节点
    2. 点击树节点切换右侧内容或筛选数据
    3. 顶部导航栏提供返回首页入口
-->
<template>
  <div class="product-layout tech-bg">
    <!-- 顶部导航栏 -->
    <header class="product-header">
      <div class="header-left">
        <el-button text @click="$router.push('/')">
          <el-icon><Back /></el-icon> 返回首页
        </el-button>
        <span class="header-title">产品展示</span>
      </div>
      <div class="header-right">
        <el-button text type="info" @click="$router.push('/showcase')">业绩宣贯</el-button>
        <el-button v-if="userStore.isLoggedIn" text type="primary" @click="$router.push('/workbench')">
          工作台
        </el-button>
      </div>
    </header>

    <div class="product-body">
      <!-- 左侧树形导航 -->
      <aside class="product-sidebar">
        <el-menu
          :default-active="activeMenu"
          :default-openeds="['app', 'process', 'model']"
          router
          background-color="transparent"
          text-color="#B8A9C9"
          active-text-color="#9B4DCA"
        >
          <!-- 应用交付分组 -->
          <el-sub-menu index="app">
            <template #title>
              <el-icon><Grid /></el-icon>
              <span>应用交付</span>
            </template>
            <!-- 全部应用 -->
            <el-menu-item index="/product/app">全部应用</el-menu-item>
            <!-- 按应用类型动态生成子节点 -->
            <el-menu-item
              v-for="item in appTypes"
              :key="item.id"
              :index="`/product/app?type=${item.id}`"
            >
              {{ item.name }}
            </el-menu-item>
          </el-sub-menu>

          <!-- 业务编排分组 -->
          <el-sub-menu index="process">
            <template #title>
              <el-icon><Connection /></el-icon>
              <span>业务编排</span>
            </template>
            <el-menu-item index="/product/process">全部流程</el-menu-item>
          </el-sub-menu>

          <!-- 模型管理分组 -->
          <el-sub-menu index="model">
            <template #title>
              <el-icon><Cpu /></el-icon>
              <span>模型管理</span>
            </template>
            <el-menu-item index="/product/model?tab=data">数据实体</el-menu-item>
            <el-menu-item index="/product/model?tab=algo">算法能力</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </aside>

      <!-- 右侧业务内容区 -->
      <main class="product-main">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
/**
 * 产品展示布局逻辑
 * - 加载应用类型列表用于侧边栏动态渲染
 * - 根据当前路由路径高亮对应菜单项
 */
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import { getAppTypes } from '../api'

const route = useRoute()
const userStore = useUserStore()

/** 应用类型列表（用于侧边栏动态子节点） */
const appTypes = ref([])

/** 当前激活的菜单项（根据路由路径+查询参数） */
const activeMenu = computed(() => {
  const path = route.path
  const query = route.query
  if (path === '/product/app' && query.type) return `/product/app?type=${query.type}`
  if (path === '/product/model' && query.tab) return `/product/model?tab=${query.tab}`
  return path
})

onMounted(async () => {
  // 加载应用类型（公开接口）
  try {
    const res = await getAppTypes()
    appTypes.value = res.data || []
  } catch (e) {
    console.error('加载应用类型失败', e)
  }
})
</script>

<style scoped>
/* 整体布局 */
.product-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 顶部导航栏 */
.product-header {
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
  gap: 8px;
}

/* 主体区域：左右分栏 */
.product-body {
  flex: 1;
  display: flex;
  overflow: hidden;
}

/* 左侧导航栏 */
.product-sidebar {
  width: 240px;
  background: rgba(26, 16, 53, 0.8);
  border-right: 1px solid var(--border-color);
  overflow-y: auto;
  padding: 12px 0;
}

.product-sidebar .el-menu {
  border-right: none;
}

/* 右侧内容区 */
.product-main {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
}

/* ========================================
 * Element Plus 组件紫色科技风主题覆盖
 * 使搜索框、表格、按钮、分页等组件与深色背景协调
 * ======================================== */

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

/* 清空按钮 */
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

/* 表格条纹效果 */
:deep(.el-table--striped .el-table__body tr.el-table__row--striped td.el-table__cell) {
  background: rgba(123, 45, 142, 0.06) !important;
}

/* 表格固定列右侧阴影 */
:deep(.el-table__fixed-right-patch) {
  background: transparent;
}

/* 单选按钮组：布局切换 */
:deep(.el-radio-group) {
  --el-border-color-base: rgba(123, 45, 142, 0.3);
}

:deep(.el-radio-button__inner) {
  background: rgba(255, 255, 255, 0.06) !important;
  border-color: rgba(123, 45, 142, 0.3) !important;
  color: #B8A9C9 !important;
}

:deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
  background: var(--primary-color) !important;
  border-color: var(--primary-color) !important;
  color: #fff !important;
  box-shadow: -1px 0 0 0 var(--primary-color) !important;
}

:deep(.el-radio-button__inner:hover) {
  color: #fff !important;
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

/* 描述列表（弹窗内） */
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

:deep(.el-descriptions--bordered .el-descriptions__cell) {
  border-color: rgba(123, 45, 142, 0.15) !important;
}

/* Tag标签 */
:deep(.el-tag--info) {
  --el-tag-bg-color: rgba(123, 45, 142, 0.12);
  --el-tag-border-color: rgba(123, 45, 142, 0.2);
  --el-tag-text-color: #B8A9C9;
}

/* 空状态文字 */
:deep(.el-empty__description p) {
  color: #6B5B7B;
}

/* Loading遮罩 */
:deep(.el-loading-mask) {
  background: rgba(13, 11, 26, 0.7);
}

/* 滚动条在表格内 */
:deep(.el-table__body-wrapper)::-webkit-scrollbar {
  width: 4px;
  height: 4px;
}

:deep(.el-table__body-wrapper)::-webkit-scrollbar-thumb {
  background: var(--primary-color);
  border-radius: 2px;
}
</style>
