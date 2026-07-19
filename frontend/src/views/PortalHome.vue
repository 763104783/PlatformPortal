<!--
  门户欢迎页 - 平台首页
  布局：左右分栏（左60%平台展示 + 右40%入口卡片）
  功能：
    1. 左侧展示平台Logo、名称、公司名、版本号
    2. 右侧提供"产品展示"和"业绩宣贯"两个入口
    3. 从后端动态加载平台配置信息
-->
<template>
  <div class="portal-home tech-bg">
    <div class="portal-container">
      <!-- 左侧：平台品牌展示区（占60%宽度，突出平台名称） -->
      <div class="portal-left fade-in-up">
        <div class="brand-area">
          <!-- 平台Logo -->
          <div class="brand-logo">
            <img v-if="config.logo_path" :src="config.logo_path" alt="平台Logo" class="logo-img" />
            <div v-else class="logo-placeholder">
              <el-icon :size="56"><Monitor /></el-icon>
            </div>
          </div>
          <!-- 平台名称（大标题） -->
          <h1 class="brand-name gradient-text">{{ config.platform_name || 'XX平台' }}</h1>
          <!-- 平台副标题 -->
          <p class="brand-subtitle">模型驱动架构 · 标准赋能平台</p>
          <!-- 科技装饰线 -->
          <div class="tech-line brand-line"></div>
          <!-- 公司名称 -->
          <p class="brand-company">{{ config.company_name || 'XX公司' }}</p>
          <!-- 版本号 -->
          <p class="brand-version">{{ config.platform_version || 'V1.0.0' }}</p>
        </div>
      </div>

      <!-- 右侧：功能入口区（占40%宽度） -->
      <div class="portal-right fade-in-up">
        <!-- 入口卡片1：产品展示 -->
        <div class="entry-card glass-card" @click="$router.push('/product')">
          <div class="entry-icon">
            <el-icon :size="42"><Grid /></el-icon>
          </div>
          <div class="entry-info">
            <h3>产品展示</h3>
            <p>浏览交付应用、业务编排与模型能力</p>
          </div>
          <el-icon class="entry-arrow"><ArrowRight /></el-icon>
        </div>

        <!-- 入口卡片2：业绩宣贯 -->
        <div class="entry-card glass-card" @click="$router.push('/showcase')">
          <div class="entry-icon">
            <el-icon :size="42"><TrendCharts /></el-icon>
          </div>
          <div class="entry-info">
            <h3>业绩宣贯</h3>
            <p>了解公司产品体系、技术成果与知识产权</p>
          </div>
          <el-icon class="entry-arrow"><ArrowRight /></el-icon>
        </div>

        <!-- 底部辅助入口 -->
        <div class="portal-links">
          <el-button text type="info" @click="$router.push('/workbench')">
            <el-icon><Operation /></el-icon> 工作台
          </el-button>
          <el-button v-if="userStore.isAdmin" text type="warning" @click="$router.push('/admin')">
            <el-icon><Setting /></el-icon> 后台管理
          </el-button>
          <el-button v-else text type="info" @click="$router.push('/login')">
            <el-icon><User /></el-icon> 登录
          </el-button>
        </div>
      </div>
    </div>

    <!-- 底部版权信息 -->
    <footer class="portal-footer">
      <p>© 2026 {{ config.company_name || 'XX公司' }} · {{ config.platform_name || 'XX平台' }}</p>
    </footer>
  </div>
</template>

<script setup>
/**
 * 门户欢迎页逻辑
 * - 页面加载时从后端获取平台配置（名称、Logo、公司名、版本号）
 * - 根据用户登录状态显示不同入口按钮
 */
import { ref, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import { getConfigs } from '../api'

const userStore = useUserStore()
/** 平台配置对象（从后端动态获取） */
const config = ref({})

onMounted(async () => {
  // 加载平台配置信息（公开接口，无需登录）
  try {
    const res = await getConfigs()
    // 后端返回数组格式 [{configKey, configValue}]，转换为对象方便使用
    const configMap = {}
    res.data.forEach(item => {
      configMap[item.configKey] = item.configValue
    })
    config.value = configMap
  } catch (e) {
    console.error('加载平台配置失败', e)
  }
})
</script>

<style scoped>
/* 页面整体布局：全屏垂直居中 */
.portal-home {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

/* 主容器：左右分栏 */
.portal-container {
  display: flex;
  align-items: center;
  width: 100%;
  max-width: 1200px;
  padding: 40px;
  gap: 60px;
}

/* 左侧品牌区（60%） */
.portal-left {
  flex: 6;
  text-align: center;
}

.brand-area {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

/* Logo样式 */
.logo-img {
  width: 80px;
  height: 80px;
  border-radius: 16px;
  object-fit: cover;
}

.logo-placeholder {
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--primary-gradient);
  border-radius: 16px;
  color: #fff;
}

/* 平台名称（大标题） */
.brand-name {
  font-size: 42px;
  font-weight: 800;
  letter-spacing: 2px;
}

/* 副标题 */
.brand-subtitle {
  font-size: 16px;
  color: #B8A9C9;
  letter-spacing: 4px;
}

/* 装饰线 */
.brand-line {
  width: 160px;
  margin: 8px 0;
}

/* 公司名和版本号 */
.brand-company {
  font-size: 14px;
  color: #8B7B9B;
}

.brand-version {
  font-size: 12px;
  color: #6B5B7B;
}

/* 右侧入口区（40%） */
.portal-right {
  flex: 4;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 入口卡片 */
.entry-card {
  display: flex;
  align-items: center;
  padding: 28px 24px;
  cursor: pointer;
  gap: 16px;
}

.entry-icon {
  color: #9B4DCA;
  flex-shrink: 0;
}

.entry-info h3 {
  font-size: 20px;
  color: #fff;
  margin-bottom: 6px;
}

.entry-info p {
  font-size: 13px;
  color: #B8A9C9;
}

.entry-arrow {
  margin-left: auto;
  color: #6B5B7B;
  font-size: 18px;
}

/* 底部辅助链接 */
.portal-links {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-top: 8px;
}

/* 底部版权 */
.portal-footer {
  position: absolute;
  bottom: 24px;
  text-align: center;
}

.portal-footer p {
  font-size: 12px;
  color: #6B5B7B;
}
</style>
