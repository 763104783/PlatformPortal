<template>
  <div class="showcase-page tech-bg">
    <header class="showcase-header">
      <div class="header-inner">
        <el-button text @click="$router.push('/')" style="color:#fff">
          <el-icon><ArrowLeft /></el-icon> 返回首页
        </el-button>
        <h2 class="page-title gradient-text">产品宣贯</h2>
      </div>
    </header>

    <main class="showcase-main">
      <!-- 维度概览图表 -->
      <div class="overview-section">
        <div ref="chartRef" class="overview-chart"></div>
      </div>

      <!-- 五大维度卡片 -->
      <div class="dimension-tabs">
        <el-radio-group v-model="activeCategory" @change="loadItems">
          <el-radio-button v-for="dim in dimensions" :key="dim.key" :label="dim.key">
            {{ dim.label }}
          </el-radio-button>
        </el-radio-group>
      </div>

      <div class="items-grid">
        <div
          v-for="item in items"
          :key="item.id"
          class="item-card glass-card fade-in-up"
          @click="$router.push(`/showcase/${item.id}`)"
        >
          <h4>{{ item.title }}</h4>
          <p>{{ item.summary || '暂无摘要' }}</p>
          <el-button type="primary" text>查看详情 →</el-button>
        </div>
      </div>

      <el-empty v-if="items.length === 0" description="暂无数据" />
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getShowcaseItems } from '../api'

const dimensions = [
  { key: 'USER_ECOLOGY', label: '用户生态' },
  { key: 'PRODUCT_SYSTEM', label: '产品体系' },
  { key: 'MODEL_SYSTEM', label: '模型体系' },
  { key: 'DATA_SYSTEM', label: '数据体系' },
  { key: 'IP', label: '知识产权' }
]

const activeCategory = ref('USER_ECOLOGY')
const items = ref([])
const chartRef = ref(null)

onMounted(async () => {
  await loadItems()
  await nextTick()
  initChart()
})

const loadItems = async () => {
  try {
    const res = await getShowcaseItems(activeCategory.value)
    items.value = res.data
  } catch (e) {
    console.error(e)
  }
}

const initChart = () => {
  if (!chartRef.value) return
  const chart = echarts.init(chartRef.value)

  // 获取各维度数据量
  Promise.all(dimensions.map(d => getShowcaseItems(d.key)))
    .then(results => {
      const data = results.map((r, i) => ({
        name: dimensions[i].label,
        value: r.data.length
      }))

      chart.setOption({
        title: {
          text: '产品体系概览',
          left: 'center',
          textStyle: { color: '#fff', fontSize: 16 }
        },
        tooltip: { trigger: 'item' },
        color: ['#7B2D8E', '#9B4DCA', '#5A1D6E', '#C9A84C', '#B8A9C9'],
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['50%', '55%'],
          data: data,
          label: {
            color: '#B8A9C9',
            fontSize: 12
          },
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(123, 45, 142, 0.5)'
            }
          }
        }]
      })
    })
    .catch(() => {})
}
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

.dimension-tabs {
  margin-bottom: 24px;
}

.items-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
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
</style>
