---
kind: dependency_management
name: 多语言依赖管理（Maven + npm）
category: dependency_management
scope:
    - '**'
source_files:
    - backend/pom.xml
    - frontend/package.json
    - frontend/package-lock.json
---

本项目为前后端分离的全栈工程，分别采用 Maven 与 npm 作为依赖管理工具，各自独立声明、锁定版本。

后端：Maven（Spring Boot）
- 构建系统：Maven，继承 spring-boot-starter-parent:2.7.17，Java 目标版本 1.8。
- 依赖声明位置：backend/pom.xml。核心依赖包括 Spring Boot Web、MyBatis-Plus（通过 properties 中的 mybatis-plus.version 统一版本）、SQLite JDBC 驱动、Lombok（optional）、Spring Boot Test。
- 打包插件：spring-boot-maven-plugin，并在 excludes 中排除 Lombok，避免打入最终 jar。
- 版本策略：公共依赖使用 properties 集中定义版本号；第三方库直接写死具体版本，未引入 BOM 或父 POM 做全仓统一。
- 仓库/镜像：pom.xml 内未配置私有仓库或阿里云镜像，默认使用中央仓库。
- 锁文件：无 lockfile，Maven 默认解析行为由远程仓库决定，可重现性依赖本地缓存。

前端：npm（Vite + Vue 3）
- 包清单：frontend/package.json，使用 ES Module 模式（type: module）。
- 运行时依赖：vue、vue-router、pinia、axios、element-plus、echarts、@element-plus/icons-vue。
- 开发依赖：@vitejs/plugin-vue、vite。
- 版本策略：全部使用 ^ 语义化范围，允许小版本自动升级。
- 锁文件：frontend/package-lock.json（lockfileVersion 3），记录精确解析结果并包含 sha512 integrity，用于 CI 可重现安装。
- 仓库/镜像：package-lock.json 中 resolved 指向 https://registry.npmjs.org，未发现 .npmrc 或私有源配置。

整体约定与建议
- 前后端各自维护独立的依赖清单，不存在跨模块共享的依赖聚合层。
- 当前缺少统一的依赖升级流程与漏洞扫描机制，建议后端引入 BOM 或集中版本属性表，配合 versions-maven-plugin 定期扫描过期依赖；前端将关键依赖从 ^ 改为固定版本或借助 Dependabot/Renovate 自动化 PR；在 CI 中加入 mvn dependency:analyze 与 npm audit，阻断高危漏洞合并。