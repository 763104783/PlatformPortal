---
kind: configuration_system
name: Spring Boot 应用配置与运行时平台配置系统
category: configuration_system
scope:
    - '**'
source_files:
    - backend/src/main/resources/application.yml
    - backend/src/main/java/com/xx/platform/config/WebMvcConfig.java
    - backend/src/main/java/com/xx/platform/config/MybatisPlusConfig.java
    - backend/src/main/java/com/xx/platform/service/impl/ConfigServiceImpl.java
    - backend/src/main/java/com/xx/platform/entity/PlatformConfig.java
    - backend/src/main/resources/schema.sql
---

## 1. 使用的系统与框架
- Spring Boot 2.7：通过 application.yml 集中管理服务器端口、数据源、文件上传、MyBatis-Plus 等启动期配置。
- SQLite（嵌入式）：数据库连接直接指向进程内 platform.db，无需外部数据库服务。
- MyBatis-Plus：在 application.yml 中声明 mapper 扫描路径、驼峰映射、日志实现及 SQLite 自增主键策略。
- @Value + 默认值：业务层通过 @Value("${upload.path:./uploads/}") 注入可覆盖的运行时参数。
- 数据库持久化平台配置：除 Spring 自身配置外，平台级“可运营配置”（名称、Logo、底图等）以 platform_config 表形式存储，提供 CRUD API 动态更新。

## 2. 关键文件与包
- backend/src/main/resources/application.yml — 全局启动配置（server、datasource、mybatis-plus、upload.path）
- backend/src/main/java/com/xx/platform/config/WebMvcConfig.java — CORS、静态资源 /uploads/** 映射
- backend/src/main/java/com/xx/platform/config/MybatisPlusConfig.java — 分页插件 Bean 注册
- backend/src/main/java/com/xx/platform/service/impl/ConfigServiceImpl.java — 读取 upload.path、文件落盘、调用 PlatformConfigMapper 读写运行配置
- backend/src/main/java/com/xx/platform/entity/PlatformConfig.java — 平台配置实体（configKey / configValue / description）
- backend/src/main/resources/schema.sql — 初始化 SQL（含 platform_config 表结构）
- backend/platform.db — 运行时 SQLite 数据文件（含已写入的平台配置）

## 3. 架构与设计约定
- 分层加载顺序：Spring Boot 优先从 application.yml 加载；业务代码再通过 @Value 或数据库查询获取运行时配置，形成“启动配置 + 运行时配置”两层模型。
- 配置来源：
  - 环境相关（端口、DB URL、上传目录）→ application.yml，部署时可通过环境变量或外部配置文件覆盖。
  - 运营相关（平台名称、Logo 链接等）→ platform_config 表，由前端 Admin 页面经 REST API 修改，实时生效。
- 文件上传路径：upload.path 同时被 WebMvcConfig（静态资源映射）和 ConfigServiceImpl（落盘）共享，保证访问与存储一致。
- CORS 开发友好：allowedOriginPatterns("*") 允许任意前端地址跨域，便于本地 Vite 开发调试。
- SQLite 适配：DbType.SQLITE + id-type: auto，避免多数据库切换成本。

## 4. 开发者应遵循的规则
- 新增启动期配置：统一追加到 application.yml，并在需要处用 @Value("${xxx:默认值}") 注入，保持默认值安全可用。
- 新增运行时平台配置项：先在 schema.sql 中插入初始记录，再在 PlatformConfigController 暴露的接口中支持读写，不要硬编码在代码里。
- 文件上传路径变更：同步修改 application.yml 的 upload.path 以及 WebMvcConfig.addResourceHandlers 中的映射路径，确保两者一致。
- 敏感信息：当前未引入加密或外部密钥管理，生产部署时应将 application.yml 移出容器并通过环境变量注入，避免将 platform.db 提交到版本库。