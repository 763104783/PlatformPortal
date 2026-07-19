---
kind: error_handling
name: Spring Boot + Vue 前后端统一错误处理机制
category: error_handling
scope:
    - '**'
source_files:
    - backend/src/main/java/com/xx/platform/common/GlobalExceptionHandler.java
    - backend/src/main/java/com/xx/platform/common/Result.java
    - frontend/src/api/request.js
---

## 1. 系统/方案概述
后端基于 Spring Boot 的 `@RestControllerAdvice` 全局异常处理器，配合统一的 `Result<T>` 响应体封装；前端通过 Axios 拦截器对业务码与 HTTP 状态进行统一解析。整体采用「业务异常抛 RuntimeException → 全局捕获 → Result.error」的约定式模式，未引入自定义异常类或错误码枚举。

## 2. 关键文件与包
- `backend/src/main/java/com/xx/platform/common/GlobalExceptionHandler.java`：全局异常捕获，分别处理 `RuntimeException` 与兜底 `Exception`。
- `backend/src/main/java/com/xx/platform/common/Result.java`：统一响应结构（code/message/data），提供 success/error 静态工厂方法。
- `frontend/src/api/request.js`：Axios 实例，请求拦截器自动注入 Authorization token，响应拦截器按 code 做 401 跳转与错误拒绝。

## 3. 架构与约定
- 控制器与服务层在参数校验失败、权限不足、资源不存在等场景直接 `throw new RuntimeException("..."）`，由 `GlobalExceptionHandler.handleRuntimeException` 转为 `Result.error(e.getMessage())`。
- 对于需要区分语义的错误（如登录过期、未登录），Controller 中直接使用 `Result.error(401, "...")` 返回，前端响应拦截器据此清除本地状态并跳转到 `/login`。
- 全局异常处理器对未捕获异常使用 `e.printStackTrace()` 打印堆栈并以 `500` 返回“服务器内部错误”，无结构化日志输出。
- 前端仅对 `res.code !== 200` 做统一拒绝，业务调用方需自行 catch 并提示用户。

## 4. 开发者应遵循的规则
- 业务错误一律通过抛出 `RuntimeException` 表达，避免在 Controller 中分散 try-catch；需要特定业务码时使用 `Result.error(code, message)` 显式返回。
- 不在异常消息中拼接敏感信息（如数据库细节），仅保留对用户友好的描述。
- 新增错误类型时优先复用现有 code 值（200/401/500），如需扩展应在 `Result` 中集中定义常量，而非散落魔法数字。
- 前端统一通过 `request.js` 发起请求，不要绕过拦截器；对 401 场景依赖其自动清理 token 并跳转行为，不要在业务代码重复实现。
- 当前未定义业务异常基类与错误码枚举，后续建议将 `RuntimeException` 替换为带 code/message 的自定义异常，以便更精确地映射到前端提示。