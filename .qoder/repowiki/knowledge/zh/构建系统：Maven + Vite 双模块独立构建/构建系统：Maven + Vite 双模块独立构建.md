---
kind: build_system
name: 构建系统：Maven + Vite 双模块独立构建
category: build_system
scope:
    - '**'
source_files:
    - backend/pom.xml
    - frontend/package.json
    - frontend/vite.config.js
---

本仓库采用前后端分离的双模块结构，各自使用独立的构建工具链，不存在统一的顶层 Makefile、Dockerfile 或 CI 流水线。

## 后端（Spring Boot）
- 构建工具：Maven（backend/pom.xml），继承 spring-boot-starter-parent:2.7.17，打包为 jar。
- 关键依赖：MyBatis-Plus 3.5.3.1、SQLite JDBC 3.39.3.0、Lombok、Spring Boot Test。
- 插件：仅配置了 spring-boot-maven-plugin，并通过 <excludes> 排除 Lombok 以避免将其打入最终 jar。
- 运行方式：通过 Maven 的 spring-boot:run 或直接执行 java -jar target/platform-1.0.0.jar。
- 数据库初始化：启动时由 DatabaseInitializer.java 读取 resources/schema.sql 自动建库建表，数据持久化为 SQLite 文件 platform.db。

## 前端（Vue 3）
- 构建工具：Vite 5.4（frontend/package.json + vite.config.js）。
- 脚本命令：npm run dev 启动开发服务器（端口 5173），并代理 /api 与 /uploads 到后端 http://localhost:8080；npm run build 生产构建输出至 frontend/dist/；npm run preview 本地预览构建产物。
- 静态资源托管：构建后的静态文件需由后端 Spring Boot 的 WebMvcConfig 映射到 classpath:/static/ 目录供浏览器访问。

## 约定与约束
- 前后端版本均硬编码在各自的清单文件中（pom.xml 中 version=1.0.0，package.json 中 version=1.0.0），无统一版本号管理。
- 开发阶段通过 Vite 反向代理跨域访问后端；生产部署时需将前端 dist 拷贝至后端 src/main/resources/static 或由 Nginx 等外部 Web 服务器托管。
- 仓库中未发现 Dockerfile、docker-compose、CI 配置文件（如 .github/workflows、Jenkinsfile 等），也未见任何顶层构建脚本（Makefile / build.sh）。

## 开发者须知
- 修改后端依赖后需在 backend/ 下执行 mvn package 重新打包。
- 修改前端依赖后需在 frontend/ 下执行 npm install，构建产物位于 frontend/dist/。
- 如需容器化或 CI 集成，需自行补充 Dockerfile 与流水线配置。