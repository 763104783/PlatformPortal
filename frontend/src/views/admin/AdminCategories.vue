<template>
  <div class="admin-categories">
    <div class="toolbar">
      <el-button type="primary" @click="openDialog()">
        <el-icon><Plus /></el-icon> 新增分类
      </el-button>
    </div>

    <el-table :data="list" stripe style="width:100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="分类名称" />
      <el-table-column prop="sortOrder" label="排序" width="80" />
      <el-table-column label="操作" width="150">
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

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑分类' : '新增分类'" width="400px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称" required>
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getCategories, addCategory, updateCategory, deleteCategory } from '../../api'

const list = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)

const form = reactive({ id: null, name: '', sortOrder: 0 })

onMounted(() => loadData())

const loadData = async () => {
  loading.value = true
  try {
    const res = await getCategories()
    list.value = res.data
  } finally { loading.value = false }
}

const openDialog = (row) => {
  if (row) {
    isEdit.value = true
    Object.assign(form, row)
  } else {
    isEdit.value = false
    Object.assign(form, { id: null, name: '', sortOrder: 0 })
  }
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!form.name) { ElMessage.warning('名称不能为空'); return }
  try {
    if (isEdit.value) { await updateCategory(form.id, form) }
    else { await addCategory(form) }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (e) { ElMessage.error(e.message) }
}

const handleDelete = async (id) => {
  try { await deleteCategory(id); ElMessage.success('删除成功'); loadData() }
  catch (e) { ElMessage.error(e.message) }
}
</script>

<style scoped>
.toolbar { display: flex; gap: 12px; margin-bottom: 16px; }
</style>
