<!--
  工作台 - 模型管理页
  功能：查看数据实体模型和算法能力模型（Tab切换）
  接口：
    - GET /api/data-model/page
    - GET /api/algo-model/page
-->
<template>
  <div class="workbench-model">
    <el-card>
      <template #header>
        <div class="card-header">
          <el-tabs v-model="activeTab" @tab-change="handleTabChange" style="margin-bottom: -16px;">
            <el-tab-pane label="数据实体模型" name="data" />
            <el-tab-pane label="算法能力模型" name="algo" />
          </el-tabs>
        </div>
      </template>

      <el-table :data="modelList" v-loading="loading" stripe>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="name" label="模型名称" min-width="150" />
        <el-table-column prop="description" label="简介" min-width="200" show-overflow-tooltip />
        <el-table-column prop="author" label="制作人" width="100" />
        <el-table-column prop="version" label="版本" width="80" />
        <el-table-column prop="techStack" label="技术栈" width="120" show-overflow-tooltip />
      </el-table>

      <el-pagination
        v-model:current-page="page"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        style="margin-top: 16px; justify-content: flex-end;"
        @current-change="loadData"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getDataModels, getAlgoModels } from '../../api'

const activeTab = ref('data')
const modelList = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = 10
const total = ref(0)

const loadData = async () => {
  loading.value = true
  try {
    const apiFn = activeTab.value === 'data' ? getDataModels : getAlgoModels
    const res = await apiFn({ page: page.value, size: pageSize })
    modelList.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => {
  page.value = 1
  loadData()
}

onMounted(() => loadData())
</script>

<style scoped>
.card-header {
  display: flex;
  align-items: center;
}
</style>
