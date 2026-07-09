# XX平台 API接口说明文档

> 基础URL: `http://localhost:8080/api`  
> 认证方式: 请求头 `Authorization: {token}`  
> 统一响应格式: `{ code: 200, message: "操作成功", data: ... }`

---

## 1. 认证模块

### 1.1 登录
- **POST** `/auth/login`
- 请求体: `{ "username": "admin", "password": "admin123" }`
- 响应: `{ "token": "xxx", "userId": 1, "username": "admin", "role": "ADMIN" }`

### 1.2 登出
- **POST** `/auth/logout`
- 需要认证: 是

### 1.3 获取当前用户信息
- **GET** `/auth/info`
- 需要认证: 是
- 响应: 用户对象（不含密码）

---

## 2. 用户管理（仅管理员）

### 2.1 用户列表
- **GET** `/users?page=1&size=10`
- 需要认证: ADMIN

### 2.2 新增用户
- **POST** `/users`
- 请求体: `{ "username": "user1", "password": "123456", "role": "USER" }`

### 2.3 编辑用户
- **PUT** `/users/{id}`
- 请求体: `{ "username": "user1", "password": "newpwd", "role": "USER" }`

### 2.4 删除用户
- **DELETE** `/users/{id}`

---

## 3. Web应用管理

### 3.1 应用列表（公开）
- **GET** `/apps?page=1&size=12&categoryId=1&keyword=xxx&sortField=clickCount&sortOrder=desc`
- 参数说明:
  - `page`: 页码，默认1
  - `size`: 每页数量，默认12
  - `categoryId`: 分类ID（可选）
  - `keyword`: 搜索关键词（可选，匹配名称和简介）
  - `sortField`: 排序字段（可选，支持 clickCount/name）
  - `sortOrder`: 排序方向 asc/desc

### 3.2 应用详情（公开）
- **GET** `/apps/{id}`

### 3.3 新增应用（管理员）
- **POST** `/apps`
- 请求体:
```json
{
  "name": "应用名称",
  "description": "功能简介",
  "categoryId": 1,
  "coverImage": "/uploads/xxx.png",
  "version": "1.0.0",
  "detail": "详细介绍HTML",
  "url": "https://app.example.com",
  "sortOrder": 0,
  "status": 1
}
```

### 3.4 编辑应用（管理员）
- **PUT** `/apps/{id}`

### 3.5 删除应用（管理员）
- **DELETE** `/apps/{id}`

### 3.6 记录点击（公开）
- **POST** `/apps/{id}/click`

---

## 4. 应用分类

### 4.1 分类列表（公开）
- **GET** `/categories`

### 4.2 新增分类（管理员）
- **POST** `/categories`
- 请求体: `{ "name": "分类名", "sortOrder": 0 }`

### 4.3 编辑分类（管理员）
- **PUT** `/categories/{id}`

### 4.4 删除分类（管理员）
- **DELETE** `/categories/{id}`

---

## 5. 宣贯数据

### 5.1 宣贯项列表（公开）
- **GET** `/showcase?category=USER_ECOLOGY`
- category可选值: `USER_ECOLOGY` / `PRODUCT_SYSTEM` / `MODEL_SYSTEM` / `DATA_SYSTEM` / `IP`

### 5.2 宣贯项详情（公开）
- **GET** `/showcase/{id}`

### 5.3 新增宣贯项（管理员）
- **POST** `/showcase`
- 请求体:
```json
{
  "category": "USER_ECOLOGY",
  "title": "标题",
  "summary": "摘要",
  "content": "详细内容",
  "sortOrder": 0
}
```

### 5.4 编辑宣贯项（管理员）
- **PUT** `/showcase/{id}`

### 5.5 删除宣贯项（管理员）
- **DELETE** `/showcase/{id}`

---

## 6. 平台配置

### 6.1 获取所有配置（公开）
- **GET** `/config`
- 响应: 配置项数组 `[{ "configKey": "platform_name", "configValue": "XX平台", "description": "平台名称" }, ...]`

### 6.2 批量更新配置（管理员）
- **PUT** `/config`
- 请求体: `{ "platform_name": "新名称", "company_name": "新公司名" }`

### 6.3 上传文件（管理员）
- **POST** `/config/upload`
- Content-Type: `multipart/form-data`
- 参数: `file`(文件), `fileKey`(配置key: logo_path / bg_image)
- 响应: 文件访问路径

---

## 7. 统计

### 7.1 总览统计（公开）
- **GET** `/stats/overview`
- 响应:
```json
{
  "appCount": 10,
  "totalClicks": 500,
  "userCount": 5,
  "categoryCount": 4,
  "categoryStats": { "1": 3, "2": 4 },
  "topApps": [...]
}
```

---

## 默认账户

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | 管理员 |

---

## 启动说明

### 后端
```bash
cd backend
mvn spring-boot:run
# 启动在 http://localhost:8080
```

### 前端
```bash
cd frontend
npm install
npm run dev
# 启动在 http://localhost:5173
# 已配置代理，/api 请求自动转发到后端8080端口
```
