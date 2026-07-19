<!--
  产品展示 - 应用交付页
  功能：
    1. 卡片/列表两种布局切换展示交付应用
    2. 支持按名称搜索、按应用类型筛选（通过URL参数type）
    3. 点击应用卡片 → 新窗口打开应用链接并记录点击次数
    4. 分页加载数据
  接口：GET /api/app/page（前台接口，只返回启用状态的应用）
-->
<template>
  <div class="app-delivery">
    <!-- 顶部工具栏：搜索 + 布局切换 -->
    <div class="toolbar">
      <el-input
        v-model="keyword"
        placeholder="搜索应用名称..."
        :prefix-icon="Search"
        clearable
        style="width: 260px"
        @clear="loadData"
        @keyup.enter="loadData"
      />
      <div class="toolbar-right">
        <!-- 布局切换按钮组 -->
        <el-radio-group v-model="viewMode" size="small">
          <el-radio-button value="card">
            <el-icon><Grid /></el-icon> 卡片
          </el-radio-button>
          <el-radio-button value="list">
            <el-icon><List /></el-icon> 列表
          </el-radio-button>
        </el-radio-group>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-loading="loading" class="content-area">
      <!-- 卡片视图 -->
      <div v-if="viewMode === 'card'" class="card-grid">
        <div
          v-for="app in appList"
          :key="app.id"
          class="app-card glass-card"
          @click="openApp(app)"
        >
          <!-- 应用封面图 -->
          <div class="app-cover">
            <img v-if="app.coverImage" :src="app.coverImage" :alt="app.name" />
            <div v-else class="cover-placeholder">
              <el-icon :size="36"><Monitor /></el-icon>
            </div>
          </div>
          <!-- 应用信息 -->
          <div class="app-info">
            <h4 class="app-name">{{ app.name }}</h4>
            <p class="app-desc">{{ app.description || '暂无简介' }}</p>
            <div class="app-meta">
              <el-tag size="small" type="info">{{ app.appType?.name || '未分类' }}</el-tag>
              <span class="app-version">{{ app.version || 'V1.0' }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 列表视图 -->
      <el-table v-else :data="appList" stripe style="width: 100%">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="name" label="应用名称" min-width="150" />
        <el-table-column prop="description" label="简介" min-width="200" show-overflow-tooltip />
        <el-table-column label="类型" width="120">
          <template #default="{ row }">
            <el-tag size="small">{{ row.appType?.name || '未分类' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="version" label="版本" width="80" />
        <el-table-column prop="clickCount" label="点击量" width="80" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="openApp(row)">打开</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 空状态 -->
      <el-empty v-if="!loading && appList.length === 0" description="暂无应用数据" />
    </div>

    <!-- 分页 -->
    <div class="pagination-area" v-if="total > 0">
      <el-pagination
        v-model:current-page="page"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="loadData"
      />
    </div>
  </div>
</template>

<script setup>
/**
 * 应用交付页逻辑
 * - 支持URL参数type筛选（从侧边栏点击应用类型跳转）
 * - 卡片/列表双视图切换
 * - 点击应用打开新窗口并记录点击次数
 */
import { ref, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getApps, clickApp } from '../../api'

const route = useRoute()

// ==================== 状态变量 ====================
const appList = ref([])       // 应用列表数据
const loading = ref(false)    // 加载状态
const keyword = ref('')       // 搜索关键词
const viewMode = ref('card')  // 视图模式：card卡片 / list列表
const page = ref(1)           // 当前页码
const pageSize = 12           // 每页条数
const total = ref(0)          // 总记录数

/**
 * 加载应用列表数据
 * 调用前台接口 GET /api/app/page，只返回status=1（启用）的应用
 */
const loadData = async () => {
  loading.value = true
  try {
    const params = {
      page: page.value,
      size: pageSize,
      keyword: keyword.value || undefined,
      typeId: route.query.type || undefined  // 从URL参数获取类型筛选
    }
    const res = await getApps(params)
    appList.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) {
    ElMessage.error('加载应用列表失败')
  } finally {
    loading.value = false
  }
}

/**
 * 打开应用（新窗口）并记录点击次数
 * @param {Object} app - 应用对象
 */
const openApp = async (app) => {
  if (app.url) {
    window.open(app.url, '_blank')
    // 异步记录点击次数（不阻塞用户操作）
    try {
      await clickApp(app.id)
    } catch (e) {
      // 点击计数失败不影响用户体验
    }
  } else {
    ElMessage.warning('该应用暂未配置访问地址')
  }
}

// 监听URL参数变化（侧边栏切换应用类型时触发）
watch(() => route.query.type, () => {
  page.value = 1
  loadData()
})

onMounted(() => {
  loadData()
})
</script>

<style scoped>
/* 工具栏 */
.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

/* 卡片网格布局 */
.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

/* 应用卡片 */
.app-card {
  cursor: pointer;
  overflow: hidden;
}

.app-cover {
  height: 140px;
  background: rgba(123, 45, 142, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.app-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cover-placeholder {
  color: #9B4DCA;
}

.app-info {
  padding: 16px;
}

.app-name {
  font-size: 16px;
  color: #fff;
  margin-bottom: 8px;
}

.app-desc {
  font-size: 13px;
  color: #B8A9C9;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.app-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.app-version {
  font-size: 12px;
  color: #6B5B7B;
}

/* 分页区域 */
.pagination-area {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}

/* 内容区域最小高度 */
.content-area {
  min-height: 300px;
}
</style>
