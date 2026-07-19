<!--
  后台管理 - 业务流程管理
  功能：业务流程的增删改查 + 批量删除 + 关联流程模板选择
  表格列：序号、流程名称、制作人、关联模板数、创建时间、操作
  接口：
    - GET /api/process/page（分页查询）
    - POST /api/process（新增，可含templateIds）
    - PUT /api/process/{id}（编辑）
    - DELETE /api/process/{id}（删除）
    - DELETE /api/process/batch（批量删除）
    - GET /api/process-template/list（获取模板下拉）
-->
<template>
  <div class="admin-process">
    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="openDialog()">
        <el-icon><Plus /></el-icon> 新增流程
      </el-button>
      <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleBatchDelete">
        批量删除({{ selectedIds.length }})
      </el-button>
      <el-input
        v-model="keyword"
        placeholder="搜索流程名称..."
        clearable
        style="width: 200px; margin-left: auto;"
        @clear="loadData"
        @keyup.enter="loadData"
      />
    </div>

    <!-- 数据表格 -->
    <el-table :data="tableData" v-loading="loading" @selection-change="handleSelectionChange" stripe>
      <el-table-column type="selection" width="50" />
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column prop="name" label="流程名称" min-width="150" />
      <el-table-column prop="author" label="制作人" width="100" />
      <el-table-column label="关联模板" width="100">
        <template #default="{ row }">{{ row.templates?.length || 0 }}个</template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="openDialog(row)">编辑</el-button>
          <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="page"
      :page-size="pageSize"
      :total="total"
      layout="total, prev, pager, next"
      style="margin-top: 16px; justify-content: flex-end;"
      @current-change="loadData"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑流程' : '新增流程'" width="550px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="流程名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入流程名称" />
        </el-form-item>
        <el-form-item label="制作人">
          <el-input v-model="form.author" placeholder="请输入制作人" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="关联模板">
          <el-select v-model="form.templateIds" multiple placeholder="选择关联的流程模板" style="width: 100%;">
            <el-option v-for="t in templateOptions" :key="t.id" :label="t.name" :value="t.id" />
          </el-select>
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
 * 业务流程管理逻辑
 * - 分页加载流程列表
 * - 新增/编辑时可选择关联的流程模板
 */
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getProcesses, addProcess, updateProcess, deleteProcess,
  batchDeleteProcesses, getProcessTemplates
} from '../../api'

const tableData = ref([])
const loading = ref(false)
const selectedIds = ref([])
const keyword = ref('')
const page = ref(1)
const pageSize = 10
const total = ref(0)
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const formRef = ref(null)
const editId = ref(null)
const templateOptions = ref([])  // 模板下拉选项

const form = reactive({ name: '', author: '', description: '', templateIds: [] })
const rules = { name: [{ required: true, message: '请输入流程名称', trigger: 'blur' }] }

/** 加载流程列表 */
const loadData = async () => {
  loading.value = true
  try {
    const res = await getProcesses({ page: page.value, size: pageSize, keyword: keyword.value || undefined })
    tableData.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

/** 加载模板下拉选项 */
const loadTemplates = async () => {
  try {
    const res = await getProcessTemplates()
    templateOptions.value = res.data || []
  } catch (e) {
    console.error('加载模板失败', e)
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
    form.author = row.author || ''
    form.description = row.description || ''
    form.templateIds = row.templateIds || (row.templates?.map(t => t.id) || [])
  } else {
    isEdit.value = false
    editId.value = null
    form.name = ''
    form.author = ''
    form.description = ''
    form.templateIds = []
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    if (isEdit.value) {
      await updateProcess(editId.value, { ...form })
      ElMessage.success('编辑成功')
    } else {
      await addProcess({ ...form })
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
    await deleteProcess(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('删除失败')
  }
}

const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 条记录吗？`, '批量删除', { type: 'warning' })
    await batchDeleteProcesses(selectedIds.value)
    ElMessage.success('批量删除成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('批量删除失败')
  }
}

onMounted(() => {
  loadData()
  loadTemplates()
})
</script>

<style scoped>
.toolbar {
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
}
</style>
