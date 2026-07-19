<!--
  后台管理 - 交付应用管理
  功能：交付应用的增删改查 + 批量删除 + 关联业务流程选择
  表格列：序号、应用名称、类型、URL、版本、点击量、状态、操作
  接口：
    - GET /api/app/admin/page（后台分页查询，含所有状态）
    - POST /api/app（新增，可含processIds）
    - PUT /api/app/{id}（编辑）
    - DELETE /api/app/{id}（删除）
    - DELETE /api/app/batch（批量删除）
    - GET /api/app-type/list（获取应用类型下拉）
    - GET /api/process/page（获取业务流程下拉）
-->
<template>
  <div class="admin-app">
    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="openDialog()">
        <el-icon><Plus /></el-icon> 新增应用
      </el-button>
      <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleBatchDelete">
        批量删除({{ selectedIds.length }})
      </el-button>
      <el-input
        v-model="keyword"
        placeholder="搜索应用名称..."
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
      <el-table-column prop="name" label="应用名称" min-width="140" />
      <el-table-column label="类型" width="100">
        <template #default="{ row }">
          <el-tag size="small">{{ row.appType?.name || '未分类' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="url" label="访问地址" min-width="180" show-overflow-tooltip />
      <el-table-column prop="version" label="版本" width="70" />
      <el-table-column prop="clickCount" label="点击量" width="70" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
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
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑应用' : '新增应用'" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="应用名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入应用名称" />
        </el-form-item>
        <el-form-item label="应用类型" prop="appTypeId">
          <el-select v-model="form.appTypeId" placeholder="请选择类型" style="width: 100%;">
            <el-option v-for="t in appTypes" :key="t.id" :label="t.name" :value="t.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="访问地址" prop="url">
          <el-input v-model="form.url" placeholder="http://..." />
        </el-form-item>
        <el-form-item label="封面图片">
          <div class="cover-upload">
            <!-- 封面预览 -->
            <img v-if="form.coverImage" :src="form.coverImage" class="cover-preview" />
            <div v-else class="cover-placeholder">暂无封面</div>
            <!-- 上传按钮 -->
            <div class="cover-actions">
              <el-upload
                :show-file-list="false"
                :before-upload="handleCoverUpload"
                accept="image/*"
              >
                <el-button type="primary" size="small">上传图片</el-button>
              </el-upload>
              <el-button v-if="form.coverImage" type="danger" size="small" link @click="removeCover">移除</el-button>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="版本号">
          <el-input v-model="form.version" placeholder="V1.0" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="form.description" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="关联流程">
          <el-select v-model="form.processIds" multiple placeholder="选择关联的业务流程" style="width: 100%;">
            <el-option v-for="p in processOptions" :key="p.id" :label="p.name" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="启用" inactive-text="禁用" />
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
 * 交付应用管理逻辑
 * - 分页加载应用列表（后台接口，含所有状态）
 * - 新增/编辑时可选择应用类型和关联业务流程
 * - 支持批量删除
 */
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getAppsAdmin, addApp, updateApp, deleteApp, batchDeleteApps,
  getAppTypes, getProcesses
} from '../../api'

// ==================== 状态变量 ====================
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
const appTypes = ref([])         // 应用类型下拉选项
const processOptions = ref([])   // 业务流程下拉选项

const form = reactive({
  name: '', appTypeId: null, url: '', coverImage: '',
  version: '', description: '', processIds: [], status: 1
})

const rules = {
  name: [{ required: true, message: '请输入应用名称', trigger: 'blur' }],
  url: [{ required: true, message: '请输入访问地址', trigger: 'blur' }]
}

/** 加载应用列表（后台分页接口） */
const loadData = async () => {
  loading.value = true
  try {
    const res = await getAppsAdmin({ page: page.value, size: pageSize, keyword: keyword.value || undefined })
    tableData.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

/** 加载下拉选项（应用类型 + 业务流程） */
const loadOptions = async () => {
  try {
    const [typeRes, processRes] = await Promise.all([
      getAppTypes(),
      getProcesses({ page: 1, size: 100 })
    ])
    appTypes.value = typeRes.data || []
    processOptions.value = processRes.data?.records || []
  } catch (e) {
    console.error('加载选项失败', e)
  }
}

const handleSelectionChange = (rows) => {
  selectedIds.value = rows.map(r => r.id)
}

/**
 * 处理封面图片上传，将图片转为Base64 Data URL存入表单
 * @param {File} file - 选择的图片文件
 * @returns {boolean} 阻止el-upload默认上传行为
 */
const handleCoverUpload = (file) => {
  // 验证文件类型
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('只能上传图片文件')
    return false
  }
  // 验证文件大小（限制5MB）
  if (file.size > 5 * 1024 * 1024) {
    ElMessage.error('图片大小不能超过5MB')
    return false
  }
  // 使用FileReader将图片转为Base64 Data URL
  const reader = new FileReader()
  reader.onload = (e) => {
    form.coverImage = e.target.result
    ElMessage.success('图片上传成功')
  }
  reader.onerror = () => {
    ElMessage.error('图片读取失败')
  }
  reader.readAsDataURL(file)
  return false  // 阻止el-upload默认上传行为
}

/** 移除封面图片 */
const removeCover = () => {
  form.coverImage = ''
}

/** 打开弹窗 */
const openDialog = (row = null) => {
  if (row) {
    isEdit.value = true
    editId.value = row.id
    Object.assign(form, {
      name: row.name, appTypeId: row.appTypeId, url: row.url,
      coverImage: row.coverImage || '', version: row.version || '',
      description: row.description || '', processIds: row.processIds || [], status: row.status
    })
  } else {
    isEdit.value = false
    editId.value = null
    Object.assign(form, {
      name: '', appTypeId: null, url: '', coverImage: '',
      version: '', description: '', processIds: [], status: 1
    })
  }
  dialogVisible.value = true
}

/** 提交表单 */
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    if (isEdit.value) {
      await updateApp(editId.value, { ...form })
      ElMessage.success('编辑成功')
    } else {
      await addApp({ ...form })
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
    await deleteApp(row.id)
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
    await batchDeleteApps(selectedIds.value)
    ElMessage.success('批量删除成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('批量删除失败')
  }
}

onMounted(() => {
  loadData()
  loadOptions()
})
</script>

<style scoped>
.toolbar {
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
}

/* 封面图片上传区域 */
.cover-upload {
  display: flex;
  align-items: center;
  gap: 16px;
}

.cover-preview {
  width: 100px;
  height: 60px;
  border-radius: 6px;
  object-fit: cover;
  border: 1px solid #ddd;
}

.cover-placeholder {
  width: 100px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(123, 45, 142, 0.1);
  border-radius: 6px;
  color: #999;
  font-size: 12px;
}

.cover-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
