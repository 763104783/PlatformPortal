<template>
  <div class="portal-home tech-bg">
    <!-- 顶部导航 -->
    <header class="portal-header">
      <div class="header-content">
        <div class="logo-area">
          <img v-if="config.logo_path" :src="config.logo_path" alt="Logo" class="logo-img" />
          <div v-else class="logo-placeholder">
            <el-icon :size="32"><Monitor /></el-icon>
          </div>
          <div class="logo-text">
            <h1 class="platform-name">{{ config.platform_name || 'XX平台' }}</h1>
            <p class="company-name">{{ config.company_name || 'XX公司' }}</p>
          </div>
        </div>
        <nav class="header-nav">
          <el-button type="primary" plain @click="$router.push('/apps')">
            <el-icon><Grid /></el-icon> 应用导航
          </el-button>
          <el-button type="primary" plain @click="$router.push('/showcase')">
            <el-icon><Document /></el-icon> 产品宣贯
          </el-button>
          <el-button v-if="userStore.isAdmin" type="warning" plain @click="$router.push('/admin/apps')">
            <el-icon><Setting /></el-icon> 管理后台
          </el-button>
          <el-button v-else text @click="$router.push('/login')">
            <el-icon><User /></el-icon> 登录
          </el-button>
        </nav>
      </div>
    </header>

    <!-- 主视觉区域 -->
    <main class="portal-main">
      <div class="hero-section fade-in-up">
        <div class="hero-content">
          <h2 class="hero-title gradient-text">{{ config.platform_name || 'XX平台' }}</h2>
          <p class="hero-subtitle">多领域Web应用矩阵 · 技术实力雄厚 · 业绩卓越</p>
          <div class="tech-line hero-line"></div>
          <p class="hero-desc">集成公司全部Web应用产品，提供一站式应用导航与产品宣贯服务</p>
        </div>

        <!-- 统计概览 -->
        <div class="stats-row" v-if="stats">
          <div class="stat-item glass-card">
            <div class="stat-number">{{ stats.appCount || 0 }}</div>
            <div class="stat-label">收录应用</div>
          </div>
          <div class="stat-item glass-card">
            <div class="stat-number">{{ stats.totalClicks || 0 }}</div>
            <div class="stat-label">总访问量</div>
          </div>
          <div class="stat-item glass-card">
            <div class="stat-number">{{ stats.categoryCount || 0 }}</div>
            <div class="stat-label">应用分类</div>
          </div>
          <div class="stat-item glass-card">
            <div class="stat-number">{{ stats.userCount || 0 }}</div>
            <div class="stat-label">系统用户</div>
          </div>
        </div>
      </div>

      <!-- 入口卡片 -->
      <div class="entry-cards fade-in-up">
        <div class="entry-card glass-card" @click="$router.push('/apps')">
          <div class="entry-icon">
            <el-icon :size="48"><Grid /></el-icon>
          </div>
          <h3>应用导航</h3>
          <p>浏览全部Web应用，快速进入业务系统</p>
          <el-button type="primary" plain>进入导航 →</el-button>
        </div>
        <div class="entry-card glass-card" @click="$router.push('/showcase')">
          <div class="entry-icon">
            <el-icon :size="48"><TrendCharts /></el-icon>
          </div>
          <h3>产品宣贯</h3>
          <p>了解公司产品体系、技术成果与知识产权</p>
          <el-button type="primary" plain>查看详情 →</el-button>
        </div>
      </div>
    </main>

    <!-- 底部 -->
    <footer class="portal-footer">
      <div class="tech-line"></div>
      <p>© 2026 {{ config.company_name || 'XX公司' }} · {{ config.platform_name || 'XX平台' }}</p>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import { getConfigs, getStatsOverview } from '../api'

const userStore = useUserStore()
const config = ref({})
const stats = ref(null)

onMounted(async () => {
  // 加载平台配置
  try {
    const res = await getConfigs()
    const configMap = {}
    res.data.forEach(item => {
      configMap[item.configKey] = item.configValue
    })
    config.value = configMap
  } catch (e) {
    console.error('加载配置失败', e)
  }

  // 加载统计数据
  try {
    const res = await getStatsOverview()
    stats.value = res.data
  } catch (e) {
    console.error('加载统计失败', e)
  }
})
</script>

<style scoped>
.portal-home {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.portal-header {
  padding: 16px 40px;
  background: rgba(13, 11, 26, 0.9);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid var(--border-color);
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo-area {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-img {
  width: 40px;
  height: 40px;
  border-radius: 8px;
}

.logo-placeholder {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--primary-gradient);
  border-radius: 8px;
  color: #fff;
}

.platform-name {
  font-size: 20px;
  color: #fff;
  font-weight: 700;
}

.company-name {
  font-size: 12px;
  color: #B8A9C9;
}

.header-nav {
  display: flex;
  gap: 8px;
}

.portal-main {
  flex: 1;
  max-width: 1400px;
  margin: 0 auto;
  padding: 60px 40px;
  width: 100%;
}

.hero-section {
  text-align: center;
  margin-bottom: 60px;
}

.hero-title {
  font-size: 48px;
  font-weight: 800;
  margin-bottom: 12px;
}

.hero-subtitle {
  font-size: 18px;
  color: #B8A9C9;
  margin-bottom: 20px;
}

.hero-line {
  width: 200px;
  margin: 0 auto 20px;
}

.hero-desc {
  font-size: 14px;
  color: #8B7B9B;
}

.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  margin-top: 48px;
}

.stat-item {
  padding: 24px;
  text-align: center;
}

.stat-number {
  font-size: 36px;
  font-weight: 700;
  color: #9B4DCA;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #B8A9C9;
}

.entry-cards {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 32px;
  max-width: 900px;
  margin: 0 auto;
}

.entry-card {
  padding: 48px 32px;
  text-align: center;
  cursor: pointer;
}

.entry-icon {
  color: #9B4DCA;
  margin-bottom: 16px;
}

.entry-card h3 {
  font-size: 24px;
  color: #fff;
  margin-bottom: 12px;
}

.entry-card p {
  font-size: 14px;
  color: #B8A9C9;
  margin-bottom: 24px;
}

.portal-footer {
  padding: 24px 40px;
  text-align: center;
}

.portal-footer p {
  margin-top: 16px;
  font-size: 12px;
  color: #6B5B7B;
}
</style>
