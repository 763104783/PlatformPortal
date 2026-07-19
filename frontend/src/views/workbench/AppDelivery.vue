<!--
  工作台 - 应用交付页
  功能：查看交付应用列表（只读浏览，编辑需到后台管理）
  说明：工作台面向搭建人员，提供应用配置查看能力
  接口：GET /api/app/page
-->
<template>
  <div class="workbench-app">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>交付应用列表</span>
          <el-input v-model="keyword" placeholder="搜索..." clearable style="width: 200px;" @clear="loadData" @keyup.enter="loadData" />
        </div>
      </template>

      <el-table :data="appList" v-loading="loading" stripe>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="name" label="应用名称" min-width="140" />
        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ row.appType?.name || '未分类' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="url" label="访问地址" min-width="180" show-overflow-tooltip />
        <el-table-column prop="version" label="版本" width="70" />
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="openApp(row)">打开</el-button>
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getApps, clickApp } from '../../api'

const appList = ref([])
const loading = ref(false)
const keyword = ref('')
const page = ref(1)
const pageSize = 10
const total = ref(0)

const loadData = async () => {
  loading.value = true
  try {
    const res = await getApps({ page: page.value, size: pageSize, keyword: keyword.value || undefined })
    appList.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const openApp = async (app) => {
  if (app.url) {
    window.open(app.url, '_blank')
    try { await clickApp(app.id) } catch (e) {}
  } else {
    ElMessage.warning('该应用暂未配置访问地址')
  }
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
