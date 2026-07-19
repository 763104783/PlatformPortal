<!--
  产品展示 - 业务编排页
  功能：
    1. 卡片/列表两种布局展示业务流程
    2. 支持按名称搜索
    3. 点击卡片查看流程详情（弹窗展示关联模板）
    4. 分页加载
  接口：GET /api/process/page（含关联模板信息）
-->
<template>
  <div class="biz-process">
    <!-- 顶部工具栏 -->
    <div class="toolbar">
      <el-input
        v-model="keyword"
        placeholder="搜索业务流程..."
        :prefix-icon="Search"
        clearable
        style="width: 260px"
        @clear="loadData"
        @keyup.enter="loadData"
      />
      <el-radio-group v-model="viewMode" size="small">
        <el-radio-button value="card"><el-icon><Grid /></el-icon> 卡片</el-radio-button>
        <el-radio-button value="list"><el-icon><List /></el-icon> 列表</el-radio-button>
      </el-radio-group>
    </div>

    <!-- 内容区 -->
    <div v-loading="loading" class="content-area">
      <!-- 卡片视图 -->
      <div v-if="viewMode === 'card'" class="card-grid">
        <div
          v-for="item in processList"
          :key="item.id"
          class="process-card glass-card"
          @click="showDetail(item)"
        >
          <div class="process-icon">
            <el-icon :size="32"><Connection /></el-icon>
          </div>
          <h4 class="process-name">{{ item.name }}</h4>
          <p class="process-desc">{{ item.description || '暂无简介' }}</p>
          <div class="process-meta">
            <span class="author">{{ item.author || '未知' }}</span>
            <el-tag size="small" type="info">{{ item.templates?.length || 0 }}个模板</el-tag>
          </div>
        </div>
      </div>

      <!-- 列表视图 -->
      <el-table v-else :data="processList" stripe>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="name" label="流程名称" min-width="150" />
        <el-table-column prop="description" label="简介" min-width="200" show-overflow-tooltip />
        <el-table-column prop="author" label="制作人" width="100" />
        <el-table-column label="关联模板" width="100">
          <template #default="{ row }">{{ row.templates?.length || 0 }}个</template>
        </el-table-column>
        <el-table-column label="操作" width="80" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="showDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="!loading && processList.length === 0" description="暂无业务流程数据" />
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

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" :title="currentProcess?.name" width="600px">
      <div v-if="currentProcess" class="detail-content">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="流程名称">{{ currentProcess.name }}</el-descriptions-item>
          <el-descriptions-item label="简介">{{ currentProcess.description || '暂无' }}</el-descriptions-item>
          <el-descriptions-item label="制作人">{{ currentProcess.author || '未知' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentProcess.createTime || '-' }}</el-descriptions-item>
        </el-descriptions>
        <!-- 关联模板列表 -->
        <h4 style="margin: 16px 0 8px; color: #fff;">关联流程模板</h4>
        <el-tag
          v-for="tpl in currentProcess.templates"
          :key="tpl.id"
          style="margin: 4px;"
        >
          {{ tpl.name }}
        </el-tag>
        <p v-if="!currentProcess.templates?.length" style="color: #6B5B7B;">暂无关联模板</p>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * 业务编排页逻辑
 * - 加载业务流程列表（含关联模板）
 * - 支持搜索、分页、卡片/列表切换
 * - 点击查看详情弹窗
 */
import { ref, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getProcesses } from '../../api'

// ==================== 状态变量 ====================
const processList = ref([])     // 流程列表
const loading = ref(false)      // 加载状态
const keyword = ref('')         // 搜索关键词
const viewMode = ref('card')    // 视图模式
const page = ref(1)             // 当前页
const pageSize = 12             // 每页条数
const total = ref(0)            // 总数
const detailVisible = ref(false) // 详情弹窗
const currentProcess = ref(null) // 当前查看的流程

/** 加载业务流程列表 */
const loadData = async () => {
  loading.value = true
  try {
    const res = await getProcesses({
      page: page.value,
      size: pageSize,
      keyword: keyword.value || undefined
    })
    processList.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) {
    ElMessage.error('加载业务流程失败')
  } finally {
    loading.value = false
  }
}

/** 显示流程详情弹窗 */
const showDetail = (item) => {
  currentProcess.value = item
  detailVisible.value = true
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.process-card {
  padding: 24px;
  cursor: pointer;
  text-align: center;
}

.process-icon {
  color: #9B4DCA;
  margin-bottom: 12px;
}

.process-name {
  font-size: 16px;
  color: #fff;
  margin-bottom: 8px;
}

.process-desc {
  font-size: 13px;
  color: #B8A9C9;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.process-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.author {
  font-size: 12px;
  color: #6B5B7B;
}

.pagination-area {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}

.content-area {
  min-height: 300px;
}
</style>
