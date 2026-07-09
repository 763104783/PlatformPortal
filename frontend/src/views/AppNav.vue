<template>
  <div class="app-nav tech-bg">
    <!-- 顶部 -->
    <header class="nav-header">
      <div class="header-inner">
        <div class="header-left">
          <el-button text @click="$router.push('/')" style="color:#fff">
            <el-icon><ArrowLeft /></el-icon> 返回首页
          </el-button>
          <h2 class="page-title gradient-text">应用导航</h2>
        </div>
        <!-- 搜索和筛选 -->
        <div class="header-right">
          <el-input
            v-model="keyword"
            placeholder="搜索应用名称或简介..."
            :prefix-icon="Search"
            clearable
            style="width: 260px"
            @input="handleSearch"
          />
          <el-select v-model="categoryId" placeholder="全部分类" clearable @change="loadApps" style="width: 140px">
            <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select>
          <el-select v-model="sortField" placeholder="排序" @change="loadApps" style="width: 120px">
            <el-option label="默认排序" value="" />
            <el-option label="点击最多" value="clickCount" />
            <el-option label="名称A-Z" value="name" />
          </el-select>
          <el-button-group>
            <el-button :type="viewMode === 'card' ? 'primary' : ''" @click="viewMode = 'card'">
              <el-icon><Grid /></el-icon>
            </el-button>
            <el-button :type="viewMode === 'list' ? 'primary' : ''" @click="viewMode = 'list'">
              <el-icon><List /></el-icon>
            </el-button>
          </el-button-group>
        </div>
      </div>
    </header>

    <!-- 应用列表 -->
    <main class="nav-main">
      <!-- 卡片布局 -->
      <div v-if="viewMode === 'card'" class="app-grid">
        <div v-for="app in apps" :key="app.id" class="app-card glass-card fade-in-up" @click="openApp(app)">
          <div class="card-cover">
            <img v-if="app.coverImage" :src="app.coverImage" :alt="app.name" />
            <div v-else class="cover-placeholder">
              <el-icon :size="40"><Monitor /></el-icon>
            </div>
          </div>
          <div class="card-body">
            <div class="card-header">
              <h4>{{ app.name }}</h4>
              <el-tag size="small" v-if="getCategoryName(app.categoryId)">
                {{ getCategoryName(app.categoryId) }}
              </el-tag>
            </div>
            <p class="card-desc">{{ app.description || '暂无简介' }}</p>
            <div class="card-footer">
              <span class="version">v{{ app.version || '1.0' }}</span>
              <span class="clicks">
                <el-icon><View /></el-icon> {{ app.clickCount || 0 }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- 列表布局 -->
      <div v-else class="app-list">
        <div v-for="app in apps" :key="app.id" class="app-list-item glass-card" @click="openApp(app)">
          <div class="list-cover">
            <img v-if="app.coverImage" :src="app.coverImage" :alt="app.name" />
            <div v-else class="cover-placeholder small">
              <el-icon :size="24"><Monitor /></el-icon>
            </div>
          </div>
          <div class="list-info">
            <h4>{{ app.name }}</h4>
            <p>{{ app.description || '暂无简介' }}</p>
          </div>
          <div class="list-meta">
            <el-tag size="small" v-if="getCategoryName(app.categoryId)">
              {{ getCategoryName(app.categoryId) }}
            </el-tag>
            <span>v{{ app.version || '1.0' }}</span>
            <span><el-icon><View /></el-icon> {{ app.clickCount || 0 }}</span>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <el-empty v-if="apps.length === 0" description="暂无应用数据" />

      <!-- 分页 -->
      <div class="pagination-wrap" v-if="total > pageSize">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="total"
          layout="prev, pager, next"
          @current-change="loadApps"
        />
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { getApps, getCategories, clickApp } from '../api'

const apps = ref([])
const categories = ref([])
const keyword = ref('')
const categoryId = ref(null)
const sortField = ref('')
const viewMode = ref('card')
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

let searchTimer = null

onMounted(() => {
  loadCategories()
  loadApps()
})

const loadCategories = async () => {
  try {
    const res = await getCategories()
    categories.value = res.data
  } catch (e) {
    console.error(e)
  }
}

const loadApps = async () => {
  try {
    const res = await getApps({
      page: currentPage.value,
      size: pageSize.value,
      categoryId: categoryId.value,
      keyword: keyword.value,
      sortField: sortField.value,
      sortOrder: sortField.value ? 'desc' : ''
    })
    apps.value = res.data.records
    total.value = Number(res.data.total)
  } catch (e) {
    console.error(e)
  }
}

const handleSearch = () => {
  clearTimeout(searchTimer)
  searchTimer = setTimeout(() => {
    currentPage.value = 1
    loadApps()
  }, 300)
}

const getCategoryName = (id) => {
  const cat = categories.value.find(c => c.id === id)
  return cat ? cat.name : ''
}

const openApp = (app) => {
  // 记录点击
  clickApp(app.id).catch(() => {})
  // 新窗口打开
  if (app.url) {
    window.open(app.url, '_blank')
  }
}
</script>

<style scoped>
.app-nav {
  min-height: 100vh;
}

.nav-header {
  padding: 16px 40px;
  background: rgba(13, 11, 26, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid var(--border-color);
}

.header-inner {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 16px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.page-title {
  font-size: 24px;
  font-weight: 700;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.nav-main {
  max-width: 1400px;
  margin: 0 auto;
  padding: 32px 40px;
}

/* 卡片布局 */
.app-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
}

.app-card {
  cursor: pointer;
  overflow: hidden;
}

.card-cover {
  height: 160px;
  overflow: hidden;
  background: rgba(123, 45, 142, 0.1);
}

.card-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cover-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #9B4DCA;
  background: linear-gradient(135deg, rgba(90, 29, 110, 0.2), rgba(155, 77, 202, 0.1));
}

.cover-placeholder.small {
  width: 60px;
  height: 60px;
  border-radius: 8px;
}

.card-body {
  padding: 16px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}

.card-header h4 {
  font-size: 16px;
  color: #fff;
}

.card-desc {
  font-size: 13px;
  color: #B8A9C9;
  line-height: 1.5;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #8B7B9B;
}

.clicks {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 列表布局 */
.app-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.app-list-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  cursor: pointer;
}

.list-cover {
  flex-shrink: 0;
}

.list-info {
  flex: 1;
}

.list-info h4 {
  font-size: 16px;
  color: #fff;
  margin-bottom: 4px;
}

.list-info p {
  font-size: 13px;
  color: #B8A9C9;
}

.list-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 13px;
  color: #8B7B9B;
}

.pagination-wrap {
  margin-top: 32px;
  display: flex;
  justify-content: center;
}
</style>
