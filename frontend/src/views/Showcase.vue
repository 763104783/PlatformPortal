<!--
  公司产品业绩宣贯页
  布局：顶部标题 + ECharts图表区 + 维度Tab + 数据卡片列表
  功能：
    1. 顶部饼图展示五大维度数据分布概览
    2. Tab切换五大维度：用户生态/产品体系/模型体系/数据体系/知识产权
    3. 下方卡片展示当前维度的具体数据项
    4. 点击卡片跳转详情页
  接口：GET /api/showcase/list?category=XXX
-->
<template>
  <div class="showcase-page tech-bg">
    <!-- 顶部导航栏 -->
    <header class="showcase-header">
      <div class="header-inner">
        <el-button text @click="$router.push('/')" style="color:#fff">
          <el-icon><ArrowLeft /></el-icon> 返回首页
        </el-button>
        <h2 class="page-title gradient-text">公司产品业绩宣贯</h2>
      </div>
    </header>

    <main class="showcase-main">
      <!-- ECharts 饼图：展示五大维度数据量分布 -->
      <div class="overview-section">
        <div ref="chartRef" class="overview-chart"></div>
      </div>

      <!-- 五大维度Tab切换 -->
      <div class="dimension-tabs">
        <el-radio-group v-model="activeCategory" @change="loadItems">
          <el-radio-button v-for="dim in dimensions" :key="dim.key" :value="dim.key">
            {{ dim.label }}
          </el-radio-button>
        </el-radio-group>
      </div>

      <!-- 数据项卡片网格 -->
      <div v-loading="loading" class="items-grid">
        <div
          v-for="item in items"
          :key="item.id"
          class="item-card glass-card fade-in-up"
          @click="$router.push(`/showcase/${item.id}`)"
        >
          <h4>{{ item.title }}</h4>
          <p>{{ item.summary || '暂无摘要' }}</p>
          <div class="item-footer">
            <span class="item-time">{{ item.publishTime || '' }}</span>
            <el-button type="primary" text size="small">查看详情 →</el-button>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <el-empty v-if="!loading && items.length === 0" description="暂无该维度数据" />
    </main>
  </div>
</template>

<script setup>
/**
 * 业绩宣贯页逻辑
 * - 页面加载时并行请求五个维度数据，绘制饼图
 * - Tab切换时按维度筛选数据列表
 */
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getShowcaseItems } from '../api'

/**
 * 五大维度定义
 * key: 对应数据库showcase_data表的category字段
 * label: 前端显示名称
 */
const dimensions = [
  { key: 'USER_ECOLOGY', label: '用户生态' },
  { key: 'PRODUCT_SYSTEM', label: '产品体系' },
  { key: 'MODEL_SYSTEM', label: '模型体系' },
  { key: 'DATA_SYSTEM', label: '数据体系' },
  { key: 'IP', label: '知识产权' }
]

// ==================== 状态变量 ====================
const activeCategory = ref('USER_ECOLOGY')  // 当前选中维度
const items = ref([])                        // 当前维度的数据列表
const loading = ref(false)                   // 加载状态
const chartRef = ref(null)                   // ECharts容器DOM引用

/**
 * 加载当前维度的宣贯数据
 * 接口：GET /api/showcase/list?category=XXX
 */
const loadItems = async () => {
  loading.value = true
  try {
    const res = await getShowcaseItems(activeCategory.value)
    items.value = res.data || []
  } catch (e) {
    console.error('加载宣贯数据失败', e)
  } finally {
    loading.value = false
  }
}

/**
 * 初始化ECharts饼图
 * 并行请求五个维度的数据量，绘制环形饼图展示分布
 */
const initChart = () => {
  if (!chartRef.value) return
  const chart = echarts.init(chartRef.value)

  // 并行请求所有维度数据，统计各维度数据量
  Promise.all(dimensions.map(d => getShowcaseItems(d.key)))
    .then(results => {
      const data = results.map((r, i) => ({
        name: dimensions[i].label,
        value: (r.data || []).length
      }))

      chart.setOption({
        title: {
          text: '产品业绩体系概览',
          left: 'center',
          textStyle: { color: '#fff', fontSize: 16 }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}项 ({d}%)'
        },
        // 紫荆花紫色系配色
        color: ['#7B2D8E', '#9B4DCA', '#5A1D6E', '#C9A84C', '#B8A9C9'],
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],  // 环形图
          center: ['50%', '55%'],
          data: data,
          label: {
            color: '#B8A9C9',
            fontSize: 12
          },
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowColor: 'rgba(123, 45, 142, 0.5)'
            }
          }
        }]
      })

      // 窗口大小变化时自适应
      window.addEventListener('resize', () => chart.resize())
    })
    .catch(() => {})
}

onMounted(async () => {
  await loadItems()
  await nextTick()
  initChart()
})
</script>

<style scoped>
.showcase-page {
  min-height: 100vh;
}

.showcase-header {
  padding: 16px 40px;
  background: rgba(13, 11, 26, 0.95);
  border-bottom: 1px solid var(--border-color);
}

.header-inner {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  gap: 16px;
}

.page-title {
  font-size: 24px;
  font-weight: 700;
}

.showcase-main {
  max-width: 1400px;
  margin: 0 auto;
  padding: 32px 40px;
}

/* 图表区域 */
.overview-section {
  margin-bottom: 40px;
}

.overview-chart {
  width: 100%;
  height: 350px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 12px;
  border: 1px solid var(--border-color);
}

/* 维度Tab */
.dimension-tabs {
  margin-bottom: 24px;
}

/* 卡片网格 */
.items-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
  min-height: 200px;
}

.item-card {
  padding: 24px;
  cursor: pointer;
}

.item-card h4 {
  font-size: 18px;
  color: #fff;
  margin-bottom: 12px;
}

.item-card p {
  font-size: 14px;
  color: #B8A9C9;
  line-height: 1.6;
  margin-bottom: 16px;
}

.item-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.item-time {
  font-size: 12px;
  color: #6B5B7B;
}

/* ========================================
 * Element Plus 组件紫色科技风主题覆盖
 * ======================================== */

/* 维度切换按钮组 */
:deep(.el-radio-group) {
  --el-border-color-base: rgba(123, 45, 142, 0.3);
}

:deep(.el-radio-button__inner) {
  background: rgba(255, 255, 255, 0.06) !important;
  border-color: rgba(123, 45, 142, 0.3) !important;
  color: #B8A9C9 !important;
}

:deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
  background: var(--primary-color) !important;
  border-color: var(--primary-color) !important;
  color: #fff !important;
  box-shadow: -1px 0 0 0 var(--primary-color) !important;
}

:deep(.el-radio-button__inner:hover) {
  color: #fff !important;
}

/* 空状态文字 */
:deep(.el-empty__description p) {
  color: #6B5B7B;
}

/* Loading遮罩 */
:deep(.el-loading-mask) {
  background: rgba(13, 11, 26, 0.7);
}
</style>
