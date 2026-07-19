<!--
  后台管理 - 流程模板管理
  功能：流程模板的增删改查 + 批量删除
  表格列：序号、模板名称、描述、创建时间、操作
  接口：
    - GET /api/process-template/list
    - POST /api/process-template
    - PUT /api/process-template/{id}
    - DELETE /api/process-template/{id}
    - DELETE /api/process-template/batch
-->
<template>
  <div class="admin-process-tpl">
    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="openDialog()">
        <el-icon><Plus /></el-icon> 新增模板
      </el-button>
      <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleBatchDelete">
        批量删除({{ selectedIds.length }})
      </el-button>
    </div>

    <!-- 数据表格 -->
    <el-table :data="tableData" v-loading="loading" @selection-change="handleSelectionChange" stripe>
      <el-table-column type="selection" width="50" />
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column prop="name" label="模板名称" min-width="150" />
      <el-table-column prop="description" label="描述" min-width="250" show-overflow-tooltip />
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="openDialog(row)">编辑</el-button>
          <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑模板' : '新增模板'" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="模板名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * 流程模板管理逻辑
 */
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getProcessTemplates, addProcessTemplate, updateProcessTemplate,
  deleteProcessTemplate, batchDeleteProcessTemplates
} from '../../api'

const tableData = ref([])
const loading = ref(false)
const selectedIds = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const formRef = ref(null)
const editId = ref(null)

const form = reactive({ name: '', description: '' })
const rules = { name: [{ required: true, message: '请输入模板名称', trigger: 'blur' }] }

/** 加载列表 */
const loadData = async () => {
  loading.value = true
  try {
    const res = await getProcessTemplates()
    tableData.value = res.data || []
  } catch (e) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleSelectionChange = (rows) => {
  selectedIds.value = rows.map(r => r.id)
}

const openDialog = (row = null) => {
  if (row) {
    isEdit.value = true
    editId.value = row.id
    form.name = row.name
    form.description = row.description || ''
  } else {
    isEdit.value = false
    editId.value = null
    form.name = ''
    form.description = ''
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    if (isEdit.value) {
      await updateProcessTemplate(editId.value, { ...form })
      ElMessage.success('编辑成功')
    } else {
      await addProcessTemplate({ ...form })
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除「${row.name}」吗？`, '提示', { type: 'warning' })
    await deleteProcessTemplate(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('删除失败')
  }
}

const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 条记录吗？`, '批量删除', { type: 'warning' })
    await batchDeleteProcessTemplates(selectedIds.value)
    ElMessage.success('批量删除成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('批量删除失败')
  }
}

onMounted(() => loadData())
</script>

<style scoped>
.toolbar {
  margin-bottom: 16px;
  display: flex;
  gap: 12px;
}
</style>
