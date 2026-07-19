---
kind: frontend_style
name: Vue 3 + Element Plus 紫色科技风主题系统
category: frontend_style
scope:
    - '**'
source_files:
    - frontend/src/assets/style.css
    - frontend/src/main.js
    - frontend/package.json
    - frontend/vite.config.js
    - frontend/src/views/PortalHome.vue
---

## 样式体系概览

本项目采用 **Vue 3 + Vite** 前端架构，基于 **Element Plus** 组件库构建，通过全局 CSS 变量与 scoped 样式实现统一的「紫色科技军事风」视觉主题。

## 核心样式架构

### 设计令牌（Design Tokens）
所有颜色、渐变、阴影等视觉资产集中在 `frontend/src/assets/style.css` 中，通过 CSS 自定义属性定义：
- 主色系：紫荆花紫（`#7B2D8E`）及其明暗变体
- 背景色：深色底（`#0D0B1A`）配合半透明卡片
- 点缀色：金色（`#C9A84C`）用于强调
- 渐变：`--primary-gradient` 三色渐变贯穿标题与装饰元素

### 组件库主题覆盖
在 `style.css` 中直接覆盖 Element Plus 的 CSS 变量（如 `--el-button-bg-color`），统一按钮、表单等原生组件的视觉风格，无需引入额外主题包。

### 通用视觉模块
- `.tech-bg`：多层径向渐变营造科技感背景
- `.glass-card`：毛玻璃卡片（backdrop-filter + 半透明背景 + 边框）
- `.gradient-text`：渐变文字效果
- `.tech-line`：渐变分割线
- `.fade-in-up`：入场动画

### Vue 组件内样式策略
各页面组件使用 `<style scoped>` 编写局部样式，仅作用于当前组件模板。组件通过 class 名组合使用全局样式类（如 `tech-bg`、`glass-card`、`gradient-text`），形成「全局基础样式 + 组件局部样式」的分层结构。

## 依赖与工具链

| 类别 | 技术栈 | 作用 |
|------|--------|------|
| 框架 | Vue 3.4 + Vite 5.4 | 单页应用与构建工具 |
| UI 库 | Element Plus 2.5 | 企业级组件库，提供表格、表单、弹窗等基础控件 |
| 图标 | @element-plus/icons-vue 2.3 | 官方图标集，全局注册 |
| 状态管理 | Pinia 2.1 | 轻量级状态管理 |
| 图表 | ECharts 5.5 | 数据可视化 |
| HTTP | Axios 1.6 | 请求封装（见 `src/api/request.js`） |

无 Tailwind、SCSS/Less、PostCSS 等预处理或原子化方案，纯原生 CSS + CSS 变量。

## 开发约定

1. **颜色使用**：优先引用 `var(--xxx)` 变量，禁止硬编码颜色值
2. **卡片风格**：统一使用 `.glass-card` 类，保持毛玻璃一致性
3. **背景处理**：大面积背景使用 `.tech-bg`，避免纯色块
4. **文案强调**：大标题使用 `.gradient-text`，小标签使用金色 `--accent-gold`
5. **交互反馈**：hover 状态遵循 `translateY(-2px) + box-shadow` 模式
6. **响应式**：未引入媒体查询框架，布局主要依赖 Flex/Grid 自适应
7. **动画**：复用 `.fade-in-up` 等预定义动画，不随意新增 keyframes