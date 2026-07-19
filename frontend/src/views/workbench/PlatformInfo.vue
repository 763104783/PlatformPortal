<!--
  工作台 - 平台信息页
  功能：展示平台基础信息和统计数据概览
  接口：
    - GET /api/config/list（平台配置）
    - GET /api/stats/overview（统计数据）
-->
<template>
  <div class="platform-info">
    <!-- 平台基础信息卡片 -->
    <el-card class="info-card">
      <template #header><span>平台基础信息</span></template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="平台名称">{{ config.platform_name || '-' }}</el-descriptions-item>
        <el-descriptions-item label="公司名称">{{ config.company_name || '-' }}</el-descriptions-item>
        <el-descriptions-item label="版本号">{{ config.platform_version || '-' }}</el-descriptions-item>
        <el-descriptions-item label="平台Logo">
          <img v-if="config.logo_path" :src="config.logo_path" style="height: 40px;" />
          <span v-else>未设置</span>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 统计数据概览 -->
    <el-card class="stats-card" style="margin-top: 20px;">
      <template #header><span>平台数据概览</span></template>
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in statItems" :key="item.label">
          <div class="stat-item">
            <div class="stat-value">{{ item.value }}</div>
            <div class="stat-label">{{ item.label }}</div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
/**
 * 平台信息页逻辑
 * - 展示平台配置和统计数据
 */
import { ref, computed, onMounted } from 'vue'
import { getConfigs, getStatsOverview } from '../../api'

const config = ref({})
const stats = ref({})

/** 统计项列表 */
const statItems = computed(() => [
  { label: '交付应用', value: stats.value.appCount || 0 },
  { label: '业务流程', value: stats.value.processCount || 0 },
  { label: '数据模型', value: stats.value.dataModelCount || 0 },
  { label: '算法模型', value: stats.value.algoModelCount || 0 }
])

onMounted(async () => {
  try {
    const [configRes, statsRes] = await Promise.all([getConfigs(), getStatsOverview()])
    // 转换配置数组为对象
    const configMap = {}
    configRes.data.forEach(item => { configMap[item.configKey] = item.configValue })
    config.value = configMap
    stats.value = statsRes.data || {}
  } catch (e) {
    console.error('加载数据失败', e)
  }
})
</script>

<style scoped>
.stat-item {
  text-align: center;
  padding: 20px;
  background: rgba(123, 45, 142, 0.1);
  border: 1px solid rgba(123, 45, 142, 0.15);
  border-radius: 8px;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #C9A0E0;
}

.stat-label {
  font-size: 14px;
  color: #6B5B7B;
  margin-top: 8px;
}
</style>
