<template>
  <div class="admin-users">
    <div class="toolbar">
      <el-button type="primary" @click="openDialog()">
        <el-icon><Plus /></el-icon> 新增用户
      </el-button>
    </div>

    <el-table :data="list" stripe style="width:100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column label="角色" width="120">
        <template #default="{ row }">
          <el-tag :type="row.role === 'ADMIN' ? 'danger' : 'info'">{{ row.role === 'ADMIN' ? '管理员' : '普通用户' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180">
        <template #default="{ row }">{{ formatDate(row.createTime) }}</template>
      </el-table-column>
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

    <el-pagination
      v-model:current-page="page"
      :page-size="10"
      :total="total"
      layout="total, prev, pager, next"
      style="margin-top:16px;justify-content:flex-end"
      @current-change="loadData"
    />

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑用户' : '新增用户'" width="400px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名" required>
          <el-input v-model="form.username" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="密码" :required="!isEdit">
          <el-input v-model="form.password" type="password" :placeholder="isEdit ? '留空不修改' : '请输入密码'" show-password />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role">
            <el-option label="管理员" value="ADMIN" />
            <el-option label="普通用户" value="USER" />
          </el-select>
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
import { getUsers, addUser, updateUser, deleteUser } from '../../api'

const list = ref([])
const loading = ref(false)
const page = ref(1)
const total = ref(0)
const dialogVisible = ref(false)
const isEdit = ref(false)

const form = reactive({ id: null, username: '', password: '', role: 'USER' })

onMounted(() => loadData())

const loadData = async () => {
  loading.value = true
  try {
    const res = await getUsers({ page: page.value, size: 10 })
    list.value = res.data.records
    total.value = Number(res.data.total)
  } finally { loading.value = false }
}

const formatDate = (d) => d ? new Date(d).toLocaleString('zh-CN') : '-'

const openDialog = (row) => {
  if (row) {
    isEdit.value = true
    Object.assign(form, { ...row, password: '' })
  } else {
    isEdit.value = false
    Object.assign(form, { id: null, username: '', password: '', role: 'USER' })
  }
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!form.username) { ElMessage.warning('用户名不能为空'); return }
  if (!isEdit.value && !form.password) { ElMessage.warning('密码不能为空'); return }
  try {
    if (isEdit.value) {
      const data = { ...form }
      if (!data.password) delete data.password
      await updateUser(form.id, data)
    } else {
      await addUser(form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (e) { ElMessage.error(e.message) }
}

const handleDelete = async (id) => {
  try { await deleteUser(id); ElMessage.success('删除成功'); loadData() }
  catch (e) { ElMessage.error(e.message) }
}
</script>

<style scoped>
.toolbar { display: flex; gap: 12px; margin-bottom: 16px; }
</style>
