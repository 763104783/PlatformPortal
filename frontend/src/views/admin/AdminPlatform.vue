<!--
  后台管理 - 平台信息管理
  功能：
    1. 展示和编辑平台基础配置（名称、公司名、版本号、Logo）
    2. Logo图片上传
    3. 保存配置
  接口：
    - GET /api/config/list（获取配置）
    - PUT /api/config/update（更新配置）
    - POST /api/config/upload（上传Logo）
-->
<template>
  <div class="admin-platform">
    <el-card>
      <template #header>
        <span>平台基础信息配置</span>
      </template>

      <el-form :model="form" label-width="120px" style="max-width: 600px;">
        <!-- 平台名称 -->
        <el-form-item label="平台名称">
          <el-input v-model="form.platform_name" placeholder="请输入平台名称" />
        </el-form-item>

        <!-- 公司名称 -->
        <el-form-item label="公司名称">
          <el-input v-model="form.company_name" placeholder="请输入公司名称" />
        </el-form-item>

        <!-- 版本号 -->
        <el-form-item label="版本号">
          <el-input v-model="form.platform_version" placeholder="如 V1.0.0" />
        </el-form-item>

        <!-- Logo上传 -->
        <el-form-item label="平台Logo">
          <div class="logo-upload">
            <!-- 预览 -->
            <img v-if="form.logo_path" :src="form.logo_path" class="logo-preview" />
            <div v-else class="logo-placeholder">暂无Logo</div>
            <!-- 上传按钮 -->
            <el-upload
              :show-file-list="false"
              :before-upload="handleUpload"
              accept="image/*"
            >
              <el-button type="primary" size="small">上传Logo</el-button>
            </el-upload>
          </div>
        </el-form-item>

        <!-- 保存按钮 -->
        <el-form-item>
          <el-button type="primary" :loading="saving" @click="handleSave">保存配置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
/**
 * 平台信息管理逻辑
 * - 加载现有配置填充表单
 * - 上传Logo后自动更新预览
 * - 保存时批量更新所有配置项
 */
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getConfigs, updateConfigs, uploadFile } from '../../api'

// ==================== 状态变量 ====================
const form = reactive({
  platform_name: '',
  company_name: '',
  platform_version: '',
  logo_path: ''
})
const saving = ref(false)  // 保存中状态

/** 加载平台配置 */
const loadConfig = async () => {
  try {
    const res = await getConfigs()
    // 将数组格式转换为对象
    res.data.forEach(item => {
      if (form.hasOwnProperty(item.configKey)) {
        form[item.configKey] = item.configValue
      }
    })
  } catch (e) {
    ElMessage.error('加载配置失败')
  }
}

/**
 * 处理Logo上传
 * @param {File} file - 选择的图片文件
 */
const handleUpload = async (file) => {
  try {
    const res = await uploadFile(file, 'logo_path')
    form.logo_path = res.data  // 后端返回文件访问路径
    ElMessage.success('Logo上传成功')
  } catch (e) {
    ElMessage.error('Logo上传失败')
  }
  return false  // 阻止el-upload默认上传行为
}

/** 保存配置 */
const handleSave = async () => {
  saving.value = true
  try {
    await updateConfigs({ ...form })
    ElMessage.success('配置保存成功')
  } catch (e) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  loadConfig()
})
</script>

<style scoped>
.logo-upload {
  display: flex;
  align-items: center;
  gap: 16px;
}

.logo-preview {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  object-fit: cover;
  border: 1px solid #ddd;
}

.logo-placeholder {
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  border-radius: 8px;
  color: #999;
  font-size: 12px;
}
</style>
