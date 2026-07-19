<!--
  产品展示 - 模型管理页
  功能：
    1. Tab切换：数据实体模型 / 算法能力模型
    2. 列表展示模型数据（序号、名称、简介、制作人、版本、时间）
    3. 支持搜索、分页
    4. 点击行查看模型详情（弹窗）
  接口：
    - GET /api/data-model/page（数据实体模型）
    - GET /api/algo-model/page（算法能力模型）
  URL参数：tab=data|algo 控制默认Tab（从侧边栏跳转）
-->
<template>
  <div class="model-manage">
    <!-- Tab切换 -->
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="数据实体模型" name="data" />
      <el-tab-pane label="算法能力模型" name="algo" />
    </el-tabs>

    <!-- 工具栏 -->
    <div class="toolbar">
      <el-input
        v-model="keyword"
        placeholder="搜索模型名称..."
        :prefix-icon="Search"
        clearable
        style="width: 260px"
        @clear="loadData"
        @keyup.enter="loadData"
      />
    </div>

    <!-- 数据表格 -->
    <div v-loading="loading" class="content-area">
      <el-table :data="modelList" stripe @row-click="showDetail" style="cursor: pointer;">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="name" label="模型名称" min-width="150" />
        <el-table-column prop="description" label="简介" min-width="200" show-overflow-tooltip />
        <el-table-column prop="author" label="制作人" width="100" />
        <el-table-column prop="version" label="版本" width="80" />
        <el-table-column prop="createTime" label="创建时间" width="160" />
      </el-table>

      <el-empty v-if="!loading && modelList.length === 0" description="暂无模型数据" />
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
    <el-dialog v-model="detailVisible" :title="currentModel?.name" width="600px">
      <el-descriptions v-if="currentModel" :column="1" border>
        <el-descriptions-item label="模型名称">{{ currentModel.name }}</el-descriptions-item>
        <el-descriptions-item label="简介">{{ currentModel.description || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="制作人">{{ currentModel.author || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="版本">{{ currentModel.version || 'V1.0' }}</el-descriptions-item>
        <el-descriptions-item label="技术栈">{{ currentModel.techStack || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentModel.createTime || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * 模型管理页逻辑
 * - 根据activeTab决定调用数据模型还是算法模型接口
 * - 支持URL参数tab初始化（从侧边栏跳转）
 */
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getDataModels, getAlgoModels } from '../../api'

const route = useRoute()

// ==================== 状态变量 ====================
const activeTab = ref('data')   // 当前Tab：data数据实体 / algo算法能力
const modelList = ref([])       // 模型列表
const loading = ref(false)      // 加载状态
const keyword = ref('')         // 搜索关键词
const page = ref(1)             // 当前页
const pageSize = 10             // 每页条数
const total = ref(0)            // 总数
const detailVisible = ref(false) // 详情弹窗
const currentModel = ref(null)   // 当前查看的模型

/**
 * 加载模型数据
 * 根据activeTab调用不同接口
 */
const loadData = async () => {
  loading.value = true
  try {
    const params = {
      page: page.value,
      size: pageSize,
      keyword: keyword.value || undefined
    }
    // 根据Tab选择接口
    const apiFn = activeTab.value === 'data' ? getDataModels : getAlgoModels
    const res = await apiFn(params)
    modelList.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) {
    ElMessage.error('加载模型数据失败')
  } finally {
    loading.value = false
  }
}

/** Tab切换处理 */
const handleTabChange = () => {
  page.value = 1
  keyword.value = ''
  loadData()
}

/** 显示模型详情 */
const showDetail = (row) => {
  currentModel.value = row
  detailVisible.value = true
}

onMounted(() => {
  // 从URL参数初始化Tab
  if (route.query.tab) {
    activeTab.value = route.query.tab
  }
  loadData()
})
</script>

<style scoped>
.toolbar {
  margin-bottom: 16px;
}

.content-area {
  min-height: 300px;
}

.pagination-area {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}
</style>
