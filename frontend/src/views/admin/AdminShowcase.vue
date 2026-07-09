<template>
  <div class="admin-showcase">
    <div class="toolbar">
      <el-button type="primary" @click="openDialog()">
        <el-icon><Plus /></el-icon> 新增宣贯项
      </el-button>
      <el-select v-model="filterCategory" placeholder="全部类别" clearable @change="loadData" style="width:150px">
        <el-option v-for="dim in dimensions" :key="dim.key" :label="dim.label" :value="dim.key" />
      </el-select>
    </div>

    <el-table :data="list" stripe style="width:100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column label="类别" width="120">
        <template #default="{ row }">{{ dimensionMap[row.category] || row.category }}</template>
      </el-table-column>
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="summary" label="摘要" show-overflow-tooltip />
      <el-table-column prop="sortOrder" label="排序" width="70" />
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

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑宣贯项' : '新增宣贯项'" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="类别" required>
          <el-select v-model="form.category">
            <el-option v-for="dim in dimensions" :key="dim.key" :label="dim.label" :value="dim.key" />
          </el-select>
        </el-form-item>
        <el-form-item label="标题" required>
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="摘要">
          <el-input v-model="form.summary" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="详细内容">
          <el-input v-model="form.content" type="textarea" :rows="6" />
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
import { getShowcaseItems, addShowcaseItem, updateShowcaseItem, deleteShowcaseItem } from '../../api'

const dimensions = [
  { key: 'USER_ECOLOGY', label: '用户生态' },
  { key: 'PRODUCT_SYSTEM', label: '产品体系' },
  { key: 'MODEL_SYSTEM', label: '模型体系' },
  { key: 'DATA_SYSTEM', label: '数据体系' },
  { key: 'IP', label: '知识产权' }
]
const dimensionMap = { USER_ECOLOGY: '用户生态', PRODUCT_SYSTEM: '产品体系', MODEL_SYSTEM: '模型体系', DATA_SYSTEM: '数据体系', IP: '知识产权' }

const list = ref([])
const loading = ref(false)
const filterCategory = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)

const form = reactive({ id: null, category: 'USER_ECOLOGY', title: '', summary: '', content: '', sortOrder: 0 })

onMounted(() => loadData())

const loadData = async () => {
  loading.value = true
  try {
    const res = await getShowcaseItems(filterCategory.value || undefined)
    list.value = res.data
  } finally { loading.value = false }
}

const openDialog = (row) => {
  if (row) {
    isEdit.value = true
    Object.assign(form, row)
  } else {
    isEdit.value = false
    Object.assign(form, { id: null, category: 'USER_ECOLOGY', title: '', summary: '', content: '', sortOrder: 0 })
  }
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!form.title) { ElMessage.warning('标题不能为空'); return }
  try {
    if (isEdit.value) { await updateShowcaseItem(form.id, form) }
    else { await addShowcaseItem(form) }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (e) { ElMessage.error(e.message) }
}

const handleDelete = async (id) => {
  try { await deleteShowcaseItem(id); ElMessage.success('删除成功'); loadData() }
  catch (e) { ElMessage.error(e.message) }
}
</script>

<style scoped>
.toolbar { display: flex; gap: 12px; margin-bottom: 16px; }
</style>
