<!--
  后台管理 - 用户管理
  功能：用户的增删改查 + 批量删除
  表格列：序号、用户名、角色、创建时间、操作
  接口：
    - GET /api/user/page
    - POST /api/user
    - PUT /api/user/{id}
    - DELETE /api/user/{id}
    - DELETE /api/user/batch
-->
<template>
  <div class="admin-users">
    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="openDialog()">
        <el-icon><Plus /></el-icon> 新增用户
      </el-button>
      <el-button type="danger" :disabled="!selectedIds.length" @click="handleBatchDelete">
        批量删除({{ selectedIds.length }})
      </el-button>
    </div>

    <!-- 数据表格 -->
    <el-table :data="list" stripe v-loading="loading" @selection-change="onSelectionChange">
      <el-table-column type="selection" width="45" />
      <el-table-column label="序号" width="60">
        <template #default="{ $index }">{{ (page - 1) * 10 + $index + 1 }}</template>
      </el-table-column>
      <el-table-column prop="username" label="用户名" min-width="150" />
      <el-table-column label="角色" width="120">
        <template #default="{ row }">
          <el-tag :type="row.role === 'ADMIN' ? 'danger' : 'info'" size="small">
            {{ row.role === 'ADMIN' ? '管理员' : '普通用户' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180">
        <template #default="{ row }">{{ formatDate(row.createTime) }}</template>
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
      :page-size="10"
      :total="total"
      layout="total, prev, pager, next"
      style="margin-top:16px; justify-content:flex-end;"
      @current-change="loadData"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑用户' : '新增用户'" width="400px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名" required>
          <el-input v-model="form.username" :disabled="isEdit" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" :required="!isEdit">
          <el-input
            v-model="form.password"
            type="password"
            :placeholder="isEdit ? '留空不修改' : '请输入密码'"
            show-password
          />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role" style="width: 100%;">
            <el-option label="管理员" value="ADMIN" />
            <el-option label="普通用户" value="USER" />
          </el-select>
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
 * 用户管理逻辑
 * - 后端分页
 * - 编辑时密码可选（留空不修改）
 * - 批量删除使用后端批量接口
 */
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUsers, addUser, updateUser, deleteUser, batchDeleteUsers } from '../../api'

// ==================== 状态变量 ====================
const list = ref([])
const loading = ref(false)
const page = ref(1)
const total = ref(0)
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const selectedIds = ref([])

const form = reactive({ id: null, username: '', password: '', role: 'USER' })

const onSelectionChange = (rows) => {
  selectedIds.value = rows.map(r => r.id)
}

/** 加载用户列表（后端分页） */
const loadData = async () => {
  loading.value = true
  try {
    const res = await getUsers({ page: page.value, size: 10 })
    list.value = res.data?.records || []
    total.value = Number(res.data?.total || 0)
  } catch (e) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

/** 格式化日期 */
const formatDate = (d) => d ? new Date(d).toLocaleString('zh-CN') : '-'

/** 打开弹窗 */
const openDialog = (row = null) => {
  if (row) {
    isEdit.value = true
    Object.assign(form, { ...row, password: '' })
  } else {
    isEdit.value = false
    Object.assign(form, { id: null, username: '', password: '', role: 'USER' })
  }
  dialogVisible.value = true
}

/** 保存用户 */
const handleSave = async () => {
  if (!form.username) {
    ElMessage.warning('用户名不能为空')
    return
  }
  if (!isEdit.value && !form.password) {
    ElMessage.warning('密码不能为空')
    return
  }
  submitting.value = true
  try {
    if (isEdit.value) {
      const data = { role: form.role }
      if (form.password) data.password = form.password
      await updateUser(form.id, data)
    } else {
      await addUser(form)
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
    await ElMessageBox.confirm(`确定删除用户「${row.username}」吗？`, '提示', { type: 'warning' })
    await deleteUser(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('删除失败')
  }
}

/** 批量删除 */
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 个用户吗？`, '批量删除', { type: 'warning' })
    await batchDeleteUsers(selectedIds.value)
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
  gap: 12px;
  margin-bottom: 16px;
}
</style>
