<template>
  <div class="admin-apps">
    <!-- 操作栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="openDialog()">
        <el-icon><Plus /></el-icon> 新增应用
      </el-button>
      <el-input v-model="keyword" placeholder="搜索..." clearable style="width:200px" @input="loadData" />
    </div>

    <!-- 表格 -->
    <el-table :data="list" stripe style="width:100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="名称" width="150" />
      <el-table-column prop="description" label="简介" show-overflow-tooltip />
      <el-table-column label="分类" width="100">
        <template #default="{ row }">{{ getCategoryName(row.categoryId) }}</template>
      </el-table-column>
      <el-table-column prop="version" label="版本" width="80" />
      <el-table-column prop="url" label="链接" show-overflow-tooltip width="200" />
      <el-table-column prop="clickCount" label="点击" width="70" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" text size="small" @click="openDialog(row)">编辑</el-button>
          <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)">
            <template #reference>
              <el-button type="danger" text size="small">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="page"
      :page-size="10"
      :total="total"
      layout="total, prev, pager, next"
      style="margin-top:16px;justify-content:flex-end"
      @current-change="loadData"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑应用' : '新增应用'" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称" required>
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="form.description" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" placeholder="选择分类" clearable>
            <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="链接" required>
          <el-input v-model="form.url" placeholder="https://..." />
        </el-form-item>
        <el-form-item label="版本号">
          <el-input v-model="form.version" placeholder="1.0.0" />
        </el-form-item>
        <el-form-item label="封面图">
          <el-input v-model="form.coverImage" placeholder="图片URL" />
        </el-form-item>
        <el-form-item label="详细介绍">
          <el-input v-model="form.detail" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getApps, addApp, updateApp, deleteApp, getCategories } from '../../api'

const list = ref([])
const categories = ref([])
const loading = ref(false)
const page = ref(1)
const total = ref(0)
const keyword = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const saving = ref(false)

const form = reactive({
  id: null, name: '', description: '', categoryId: null,
  url: '', version: '', coverImage: '', detail: '', sortOrder: 0, status: 1
})

let searchTimer = null

onMounted(() => {
  loadData()
  loadCategories()
})

const loadData = async () => {
  clearTimeout(searchTimer)
  searchTimer = setTimeout(async () => {
    loading.value = true
    try {
      const res = await getApps({ page: page.value, size: 10, keyword: keyword.value })
      list.value = res.data.records
      total.value = Number(res.data.total)
    } finally { loading.value = false }
  }, 300)
}

const loadCategories = async () => {
  const res = await getCategories()
  categories.value = res.data
}

const getCategoryName = (id) => {
  const cat = categories.value.find(c => c.id === id)
  return cat ? cat.name : '-'
}

const openDialog = (row) => {
  if (row) {
    isEdit.value = true
    Object.assign(form, row)
  } else {
    isEdit.value = false
    Object.assign(form, { id: null, name: '', description: '', categoryId: null, url: '', version: '', coverImage: '', detail: '', sortOrder: 0, status: 1 })
  }
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!form.name || !form.url) {
    ElMessage.warning('名称和链接不能为空')
    return
  }
  saving.value = true
  try {
    if (isEdit.value) {
      await updateApp(form.id, form)
    } else {
      await addApp(form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (e) {
    ElMessage.error(e.message)
  } finally { saving.value = false }
}

const handleDelete = async (id) => {
  try {
    await deleteApp(id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    ElMessage.error(e.message)
  }
}
</script>

<style scoped>
.toolbar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}
</style>
