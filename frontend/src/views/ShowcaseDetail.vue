<template>
  <div class="detail-page tech-bg">
    <header class="detail-header">
      <el-button text @click="$router.back()" style="color:#fff">
        <el-icon><ArrowLeft /></el-icon> 返回
      </el-button>
    </header>
    <main class="detail-main" v-if="item">
      <div class="detail-card glass-card fade-in-up">
        <div class="detail-badge">
          <el-tag>{{ dimensionLabel }}</el-tag>
        </div>
        <h1>{{ item.title }}</h1>
        <div class="tech-line" style="margin: 20px 0;"></div>
        <p class="detail-summary">{{ item.summary }}</p>
        <div class="detail-content" v-html="item.content"></div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getShowcaseDetail } from '../api'

const route = useRoute()
const item = ref(null)

const dimensionMap = {
  USER_ECOLOGY: '用户生态',
  PRODUCT_SYSTEM: '产品体系',
  MODEL_SYSTEM: '模型体系',
  DATA_SYSTEM: '数据体系',
  IP: '知识产权'
}

const dimensionLabel = computed(() => dimensionMap[item.value?.category] || '')

onMounted(async () => {
  try {
    const res = await getShowcaseDetail(route.params.id)
    item.value = res.data
  } catch (e) {
    console.error(e)
  }
})
</script>

<style scoped>
.detail-page {
  min-height: 100vh;
}

.detail-header {
  padding: 16px 40px;
  background: rgba(13, 11, 26, 0.95);
  border-bottom: 1px solid var(--border-color);
}

.detail-main {
  max-width: 900px;
  margin: 0 auto;
  padding: 40px;
}

.detail-card {
  padding: 48px;
  background: rgba(255, 255, 255, 0.05);
}

.detail-badge {
  margin-bottom: 16px;
}

.detail-card h1 {
  font-size: 32px;
  color: #fff;
  font-weight: 700;
}

.detail-summary {
  font-size: 16px;
  color: #B8A9C9;
  line-height: 1.8;
  margin-bottom: 24px;
}

.detail-content {
  font-size: 15px;
  color: #D0C4DC;
  line-height: 2;
}

/* Tag标签紫色主题 */
:deep(.el-tag) {
  --el-tag-bg-color: rgba(123, 45, 142, 0.15);
  --el-tag-border-color: rgba(123, 45, 142, 0.3);
  --el-tag-text-color: #B8A9C9;
}
</style>
