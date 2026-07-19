<!--
  后台管理 - 应用类型管理
  功能：应用类型的增删改查（CRUD）+ 批量删除
  表格列：序号、类型名称、描述、排序、操作
  接口：
    - GET /api/app-type/list（获取列表）
    - POST /api/app-type（新增）
    - PUT /api/app-type/{id}（编辑）
    - DELETE /api/app-type/{id}（删除）
    - DELETE /api/app-type/batch（批量删除）
-->
<template>
  <div class="admin-app-type">
    <!-- 工具栏：新增 + 批量删除 -->
    <div class="toolbar">
      <el-button type="primary" @click="openDialog()">
        <el-icon><Plus /></el-icon> 新增类型
      </el-button>
      <el-button
        type="danger"
        :disabled="selectedIds.length === 0"
        @click="handleBatchDelete"
      >
        批量删除({{ selectedIds.length }})
      </el-button>
    </div>

    <!-- 数据表格 -->
    <el-table
      :data="tableData"
      v-loading="loading"
      @selection-change="handleSelectionChange"
      stripe
    >
      <!-- 复选框列 -->
      <el-table-column type="selection" width="50" />
      <!-- 序号列（自动计算） -->
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column prop="name" label="类型名称" min-width="150" />
      <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
      <el-table-column prop="sortOrder" label="排序" width="80" />
      <!-- 操作列 -->
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="openDialog(row)">编辑</el-button>
          <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑应用类型' : '新增应用类型'"
      width="500px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="类型名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入类型名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0" :max="999" />
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
 * 应用类型管理逻辑
 * - 加载类型列表
 * - 新增/编辑弹窗表单
 * - 单条删除（确认框）
 * - 批量删除（确认框）
 */
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getAppTypes, addAppType, updateAppType, deleteAppType, batchDeleteAppTypes
} from '../../api'

// ==================== 状态变量 ====================
const tableData = ref([])        // 表格数据
const loading = ref(false)       // 加载状态
const selectedIds = ref([])      // 选中的ID列表
const dialogVisible = ref(false) // 弹窗显示
const isEdit = ref(false)        // 是否编辑模式
const submitting = ref(false)    // 提交中
const formRef = ref(null)        // 表单引用
const editId = ref(null)         // 编辑的记录ID

// 表单数据
const form = reactive({
  name: '',
  description: '',
  sortOrder: 0
})

// 表单校验规则
const rules = {
  name: [{ required: true, message: '请输入类型名称', trigger: 'blur' }]
}

/** 加载数据列表 */
const loadData = async () => {
  loading.value = true
  try {
    const res = await getAppTypes()
    tableData.value = res.data || []
  } catch (e) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

/** 表格选中变化 */
const handleSelectionChange = (rows) => {
  selectedIds.value = rows.map(r => r.id)
}

/** 打开新增/编辑弹窗 */
const openDialog = (row = null) => {
  if (row) {
    // 编辑模式：填充表单
    isEdit.value = true
    editId.value = row.id
    form.name = row.name
    form.description = row.description || ''
    form.sortOrder = row.sortOrder || 0
  } else {
    // 新增模式：清空表单
    isEdit.value = false
    editId.value = null
    form.name = ''
    form.description = ''
    form.sortOrder = 0
  }
  dialogVisible.value = true
}

/** 提交表单（新增或编辑） */
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    if (isEdit.value) {
      await updateAppType(editId.value, { ...form })
      ElMessage.success('编辑成功')
    } else {
      await addAppType({ ...form })
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

/** 单条删除 */
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除「${row.name}」吗？`, '提示', { type: 'warning' })
    await deleteAppType(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('删除失败')
  }
}

/** 批量删除 */
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 条记录吗？`, '批量删除', { type: 'warning' })
    await batchDeleteAppTypes(selectedIds.value)
    ElMessage.success('批量删除成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('批量删除失败')
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.toolbar {
  margin-bottom: 16px;
  display: flex;
  gap: 12px;
}
</style>
