<!--
  工作台 - 业务编排页
  功能：查看业务流程列表及其关联模板
  接口：GET /api/process/page
-->
<template>
  <div class="workbench-process">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>业务流程列表</span>
          <el-input v-model="keyword" placeholder="搜索..." clearable style="width: 200px;" @clear="loadData" @keyup.enter="loadData" />
        </div>
      </template>

      <el-table :data="processList" v-loading="loading" stripe>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="name" label="流程名称" min-width="150" />
        <el-table-column prop="description" label="简介" min-width="200" show-overflow-tooltip />
        <el-table-column prop="author" label="制作人" width="100" />
        <el-table-column label="关联模板" width="100">
          <template #default="{ row }">{{ row.templates?.length || 0 }}个</template>
        </el-table-column>
        <el-table-column label="操作" width="80">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="showDetail(row)">详情</el-button>
          </template>
        </el-table-column>
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

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" :title="current?.name" width="500px">
      <el-descriptions v-if="current" :column="1" border>
        <el-descriptions-item label="流程名称">{{ current.name }}</el-descriptions-item>
        <el-descriptions-item label="简介">{{ current.description || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="制作人">{{ current.author || '未知' }}</el-descriptions-item>
      </el-descriptions>
      <h4 style="margin: 16px 0 8px;">关联模板</h4>
      <el-tag v-for="t in current?.templates" :key="t.id" style="margin: 4px;">{{ t.name }}</el-tag>
      <p v-if="!current?.templates?.length" style="color: #999;">暂无关联模板</p>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getProcesses } from '../../api'

const processList = ref([])
const loading = ref(false)
const keyword = ref('')
const page = ref(1)
const pageSize = 10
const total = ref(0)
const detailVisible = ref(false)
const current = ref(null)

const loadData = async () => {
  loading.value = true
  try {
    const res = await getProcesses({ page: page.value, size: pageSize, keyword: keyword.value || undefined })
    processList.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const showDetail = (row) => {
  current.value = row
  detailVisible.value = true
}

onMounted(() => loadData())
</script>

<style scoped>
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
