<!--
  后台管理 - 宣贯数据管理
  功能：宣贯数据的增删改查 + 批量删除 + 按维度筛选
  表格列：序号、类别、标题、摘要、排序、操作
  接口：
    - GET /api/showcase/list?category=XXX
    - POST /api/showcase
    - PUT /api/showcase/{id}
    - DELETE /api/showcase/{id}
    - DELETE /api/showcase/batch
-->
<template>
  <div class="admin-showcase">
    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="openDialog()">
        <el-icon><Plus /></el-icon> 新增宣贯项
      </el-button>
      <el-button type="danger" :disabled="!selectedIds.length" @click="handleBatchDelete">
        批量删除({{ selectedIds.length }})
      </el-button>
      <!-- 维度筛选下拉 -->
      <el-select v-model="filterCategory" placeholder="全部类别" clearable @change="loadData" style="width:150px; margin-left: auto;">
        <el-option v-for="dim in dimensions" :key="dim.key" :label="dim.label" :value="dim.key" />
      </el-select>
    </div>

    <!-- 数据表格（前端分页） -->
    <el-table :data="pagedList" stripe v-loading="loading" @selection-change="onSelectionChange">
      <el-table-column type="selection" width="45" />
      <el-table-column label="序号" width="60">
        <template #default="{ $index }">{{ (page - 1) * pageSize + $index + 1 }}</template>
      </el-table-column>
      <el-table-column label="类别" width="120">
        <template #default="{ row }">
          <el-tag size="small">{{ dimensionMap[row.category] || row.category }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="标题" min-width="150" />
      <el-table-column prop="summary" label="摘要" min-width="200" show-overflow-tooltip />
      <el-table-column prop="sortOrder" label="排序" width="70" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="openDialog(row)">编辑</el-button>
          <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 前端分页 -->
    <el-pagination
      v-model:current-page="page"
      :page-size="pageSize"
      :total="allList.length"
      layout="total, prev, pager, next"
      style="margin-top:16px; justify-content:flex-end;"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑宣贯项' : '新增宣贯项'" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="类别" required>
          <el-select v-model="form.category" style="width: 100%;">
            <el-option v-for="dim in dimensions" :key="dim.key" :label="dim.label" :value="dim.key" />
          </el-select>
        </el-form-item>
        <el-form-item label="标题" required>
          <el-input v-model="form.title" placeholder="请输入标题" />
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
        <el-button type="primary" :loading="submitting" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * 宣贯数据管理逻辑
 * - 支持按五大维度筛选
 * - 前端分页（数据量较小）
 * - 批量删除使用后端批量接口
 */
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getShowcaseItems, addShowcaseItem, updateShowcaseItem,
  deleteShowcaseItem, batchDeleteShowcaseItems
} from '../../api'

/** 五大维度定义 */
const dimensions = [
  { key: 'USER_ECOLOGY', label: '用户生态' },
  { key: 'PRODUCT_SYSTEM', label: '产品体系' },
  { key: 'MODEL_SYSTEM', label: '模型体系' },
  { key: 'DATA_SYSTEM', label: '数据体系' },
  { key: 'IP', label: '知识产权' }
]
const dimensionMap = {
  USER_ECOLOGY: '用户生态', PRODUCT_SYSTEM: '产品体系',
  MODEL_SYSTEM: '模型体系', DATA_SYSTEM: '数据体系', IP: '知识产权'
}

// ==================== 状态变量 ====================
const allList = ref([])          // 全部数据（前端分页）
const loading = ref(false)
const filterCategory = ref('')   // 筛选维度
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const page = ref(1)
const pageSize = 10
const selectedIds = ref([])

/** 前端分页计算 */
const pagedList = computed(() => {
  const start = (page.value - 1) * pageSize
  return allList.value.slice(start, start + pageSize)
})

const onSelectionChange = (rows) => {
  selectedIds.value = rows.map(r => r.id)
}

/** 加载数据 */
const loadData = async () => {
  loading.value = true
  try {
    const res = await getShowcaseItems(filterCategory.value || undefined)
    allList.value = res.data || []
  } catch (e) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

/** 打开弹窗 */
const openDialog = (row = null) => {
  if (row) {
    isEdit.value = true
    Object.assign(form, { ...row })
  } else {
    isEdit.value = false
    Object.assign(form, { id: null, category: 'USER_ECOLOGY', title: '', summary: '', content: '', sortOrder: 0 })
  }
  dialogVisible.value = true
}

const form = reactive({ id: null, category: 'USER_ECOLOGY', title: '', summary: '', content: '', sortOrder: 0 })

/** 保存（新增或编辑） */
const handleSave = async () => {
  if (!form.title) {
    ElMessage.warning('标题不能为空')
    return
  }
  submitting.value = true
  try {
    if (isEdit.value) {
      await updateShowcaseItem(form.id, form)
    } else {
      await addShowcaseItem(form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (e) {
    ElMessage.error('保存失败')
  } finally {
    submitting.value = false
  }
}

/** 单条删除 */
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除「${row.title}」吗？`, '提示', { type: 'warning' })
    await deleteShowcaseItem(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('删除失败')
  }
}

/** 批量删除（使用后端批量接口） */
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 条记录吗？`, '批量删除', { type: 'warning' })
    await batchDeleteShowcaseItems(selectedIds.value)
    ElMessage.success('批量删除成功')
    selectedIds.value = []
    loadData()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('批量删除失败')
  }
}

onMounted(() => loadData())
</script>

<style scoped>
.toolbar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}
</style>
