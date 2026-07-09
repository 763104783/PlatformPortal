<template>
  <div class="admin-settings">
    <el-card>
      <template #header><h3>平台基本设置</h3></template>
      <el-form :model="form" label-width="100px" style="max-width:500px">
        <el-form-item label="平台名称">
          <el-input v-model="form.platform_name" />
        </el-form-item>
        <el-form-item label="公司名称">
          <el-input v-model="form.company_name" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSaveText" :loading="saving">保存设置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="margin-top:24px">
      <template #header><h3>Logo设置</h3></template>
      <div class="upload-section">
        <div class="preview" v-if="form.logo_path">
          <img :src="form.logo_path" alt="Logo" style="max-width:200px;max-height:100px" />
        </div>
        <el-upload
          :show-file-list="false"
          :http-request="handleUploadLogo"
          accept="image/*"
        >
          <el-button type="primary" plain>上传Logo</el-button>
        </el-upload>
      </div>
    </el-card>

    <el-card style="margin-top:24px">
      <template #header><h3>底图设置</h3></template>
      <div class="upload-section">
        <div class="preview" v-if="form.bg_image">
          <img :src="form.bg_image" alt="底图" style="max-width:400px;max-height:200px" />
        </div>
        <el-upload
          :show-file-list="false"
          :http-request="handleUploadBg"
          accept="image/*"
        >
          <el-button type="primary" plain>上传底图</el-button>
        </el-upload>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getConfigs, updateConfigs, uploadFile } from '../../api'

const form = reactive({
  platform_name: '',
  company_name: '',
  logo_path: '',
  bg_image: ''
})
const saving = ref(false)

onMounted(async () => {
  try {
    const res = await getConfigs()
    res.data.forEach(item => {
      if (form.hasOwnProperty(item.configKey)) {
        form[item.configKey] = item.configValue
      }
    })
  } catch (e) {
    console.error(e)
  }
})

const handleSaveText = async () => {
  saving.value = true
  try {
    await updateConfigs({
      platform_name: form.platform_name,
      company_name: form.company_name
    })
    ElMessage.success('保存成功')
  } catch (e) {
    ElMessage.error(e.message)
  } finally {
    saving.value = false
  }
}

const handleUploadLogo = async (options) => {
  try {
    const res = await uploadFile(options.file, 'logo_path')
    form.logo_path = res.data
    ElMessage.success('Logo上传成功')
  } catch (e) {
    ElMessage.error('上传失败')
  }
}

const handleUploadBg = async (options) => {
  try {
    const res = await uploadFile(options.file, 'bg_image')
    form.bg_image = res.data
    ElMessage.success('底图上传成功')
  } catch (e) {
    ElMessage.error('上传失败')
  }
}
</script>

<style scoped>
.upload-section {
  display: flex;
  align-items: center;
  gap: 24px;
}
.preview {
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 8px;
}
</style>
