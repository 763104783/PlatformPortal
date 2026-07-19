/**
 * XX平台 API 接口封装
 * 
 * 本文件封装了所有与后端交互的API请求函数。
 * 按照业务模块分组，每个函数对应一个后端REST接口。
 * 
 * 接口基础路径：/api（在request.js中配置）
 * 认证方式：请求头 Authorization: {token}
 */

import request from './request'

// ==================== 认证模块 ====================

/**
 * 用户登录
 * POST /api/auth/login
 * @param {Object} data - { username, password }
 * @returns {Promise} - { token, userId, username, role }
 */
export function login(data) {
  return request.post('/auth/login', data)
}

/**
 * 用户登出
 * POST /api/auth/logout
 */
export function logout() {
  return request.post('/auth/logout')
}

/**
 * 获取当前登录用户信息
 * GET /api/auth/info
 * @returns {Promise} - { id, username, role }
 */
export function getUserInfo() {
  return request.get('/auth/info')
}

// ==================== 用户管理模块 ====================

/**
 * 分页查询用户列表（管理员）
 * GET /api/user/page
 * @param {Object} params - { page, size }
 */
export function getUsers(params) {
  return request.get('/user/page', { params })
}

/**
 * 新增用户（管理员）
 * POST /api/user
 * @param {Object} data - { username, password, role }
 */
export function addUser(data) {
  return request.post('/user', data)
}

/**
 * 编辑用户（管理员）
 * PUT /api/user/{id}
 * @param {number} id - 用户ID
 * @param {Object} data - { role, password? }
 */
export function updateUser(id, data) {
  return request.put(`/user/${id}`, data)
}

/**
 * 删除用户（管理员）
 * DELETE /api/user/{id}
 */
export function deleteUser(id) {
  return request.delete(`/user/${id}`)
}

/**
 * 批量删除用户（管理员）
 * DELETE /api/user/batch
 * @param {Array} ids - 用户ID数组
 */
export function batchDeleteUsers(ids) {
  return request.delete('/user/batch', { data: { ids } })
}

// ==================== 平台配置模块 ====================

/**
 * 获取平台配置列表（公开）
 * GET /api/config/list
 * @returns {Promise} - [{ configKey, configValue, description }]
 */
export function getConfigs() {
  return request.get('/config/list')
}

/**
 * 更新平台配置（管理员）
 * PUT /api/config/update
 * @param {Object} data - { platform_name, company_name, ... }
 */
export function updateConfigs(data) {
  return request.put('/config/update', data)
}

/**
 * 上传文件（管理员）
 * POST /api/config/upload
 * @param {File} file - 图片文件
 * @param {string} fileKey - 配置键名
 * @returns {Promise} - 文件访问路径
 */
export function uploadFile(file, fileKey) {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('fileKey', fileKey)
  return request.post('/config/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// ==================== 应用类型模块 ====================

/**
 * 获取所有应用类型列表（公开）
 * GET /api/app-type/list
 * @returns {Promise} - [{ id, name, description, sortOrder }]
 */
export function getAppTypes() {
  return request.get('/app-type/list')
}

/**
 * 新增应用类型（管理员）
 * POST /api/app-type
 */
export function addAppType(data) {
  return request.post('/app-type', data)
}

/**
 * 编辑应用类型（管理员）
 * PUT /api/app-type/{id}
 */
export function updateAppType(id, data) {
  return request.put(`/app-type/${id}`, data)
}

/**
 * 删除应用类型（管理员）
 * DELETE /api/app-type/{id}
 */
export function deleteAppType(id) {
  return request.delete(`/app-type/${id}`)
}

/**
 * 批量删除应用类型（管理员）
 * DELETE /api/app-type/batch
 */
export function batchDeleteAppTypes(ids) {
  return request.delete('/app-type/batch', { data: { ids } })
}

// ==================== 业务流程模板模块 ====================

/**
 * 获取所有流程模板列表（公开）
 * GET /api/process-template/list
 */
export function getProcessTemplates() {
  return request.get('/process-template/list')
}

/**
 * 新增流程模板（管理员）
 * POST /api/process-template
 */
export function addProcessTemplate(data) {
  return request.post('/process-template', data)
}

/**
 * 编辑流程模板（管理员）
 * PUT /api/process-template/{id}
 */
export function updateProcessTemplate(id, data) {
  return request.put(`/process-template/${id}`, data)
}

/**
 * 删除流程模板（管理员）
 * DELETE /api/process-template/{id}
 */
export function deleteProcessTemplate(id) {
  return request.delete(`/process-template/${id}`)
}

/**
 * 批量删除流程模板（管理员）
 * DELETE /api/process-template/batch
 */
export function batchDeleteProcessTemplates(ids) {
  return request.delete('/process-template/batch', { data: { ids } })
}

// ==================== 数据实体模型模块 ====================

/**
 * 分页查询数据实体模型（公开）
 * GET /api/data-model/page
 * @param {Object} params - { page, size, keyword? }
 */
export function getDataModels(params) {
  return request.get('/data-model/page', { params })
}

/**
 * 获取数据实体模型详情（公开）
 * GET /api/data-model/{id}
 */
export function getDataModelDetail(id) {
  return request.get(`/data-model/${id}`)
}

/**
 * 新增数据实体模型（管理员）
 * POST /api/data-model
 */
export function addDataModel(data) {
  return request.post('/data-model', data)
}

/**
 * 编辑数据实体模型（管理员）
 * PUT /api/data-model/{id}
 */
export function updateDataModel(id, data) {
  return request.put(`/data-model/${id}`, data)
}

/**
 * 删除数据实体模型（管理员）
 * DELETE /api/data-model/{id}
 */
export function deleteDataModel(id) {
  return request.delete(`/data-model/${id}`)
}

/**
 * 批量删除数据实体模型（管理员）
 * DELETE /api/data-model/batch
 */
export function batchDeleteDataModels(ids) {
  return request.delete('/data-model/batch', { data: { ids } })
}

// ==================== 算法能力模型模块 ====================

/**
 * 分页查询算法能力模型（公开）
 * GET /api/algo-model/page
 */
export function getAlgoModels(params) {
  return request.get('/algo-model/page', { params })
}

/**
 * 获取算法能力模型详情（公开）
 * GET /api/algo-model/{id}
 */
export function getAlgoModelDetail(id) {
  return request.get(`/algo-model/${id}`)
}

/**
 * 新增算法能力模型（管理员）
 * POST /api/algo-model
 */
export function addAlgoModel(data) {
  return request.post('/algo-model', data)
}

/**
 * 编辑算法能力模型（管理员）
 * PUT /api/algo-model/{id}
 */
export function updateAlgoModel(id, data) {
  return request.put(`/algo-model/${id}`, data)
}

/**
 * 删除算法能力模型（管理员）
 * DELETE /api/algo-model/{id}
 */
export function deleteAlgoModel(id) {
  return request.delete(`/algo-model/${id}`)
}

/**
 * 批量删除算法能力模型（管理员）
 * DELETE /api/algo-model/batch
 */
export function batchDeleteAlgoModels(ids) {
  return request.delete('/algo-model/batch', { data: { ids } })
}

// ==================== 业务流程模块 ====================

/**
 * 分页查询业务流程（公开，含关联模板）
 * GET /api/process/page
 * @param {Object} params - { page, size, keyword? }
 */
export function getProcesses(params) {
  return request.get('/process/page', { params })
}

/**
 * 获取业务流程详情（公开，含关联模板列表）
 * GET /api/process/{id}
 */
export function getProcessDetail(id) {
  return request.get(`/process/${id}`)
}

/**
 * 新增业务流程（管理员，可含templateIds）
 * POST /api/process
 * @param {Object} data - { name, description, author, templateIds: [] }
 */
export function addProcess(data) {
  return request.post('/process', data)
}

/**
 * 编辑业务流程（管理员）
 * PUT /api/process/{id}
 */
export function updateProcess(id, data) {
  return request.put(`/process/${id}`, data)
}

/**
 * 删除业务流程（管理员）
 * DELETE /api/process/{id}
 */
export function deleteProcess(id) {
  return request.delete(`/process/${id}`)
}

/**
 * 批量删除业务流程（管理员）
 * DELETE /api/process/batch
 */
export function batchDeleteProcesses(ids) {
  return request.delete('/process/batch', { data: { ids } })
}

// ==================== 交付应用模块 ====================

/**
 * 分页查询交付应用（前台展示，只查启用状态）
 * GET /api/app/page
 * @param {Object} params - { page, size, keyword?, typeId?, sortField? }
 */
export function getApps(params) {
  return request.get('/app/page', { params })
}

/**
 * 分页查询交付应用（后台管理，查所有状态）
 * GET /api/app/admin/page
 */
export function getAppsAdmin(params) {
  return request.get('/app/admin/page', { params })
}

/**
 * 获取交付应用详情（公开，含关联信息）
 * GET /api/app/{id}
 */
export function getAppDetail(id) {
  return request.get(`/app/${id}`)
}

/**
 * 记录应用点击次数（公开）
 * POST /api/app/{id}/click
 */
export function clickApp(id) {
  return request.post(`/app/${id}/click`)
}

/**
 * 新增交付应用（管理员，可含processIds）
 * POST /api/app
 * @param {Object} data - { name, url, appTypeId, processIds: [], ... }
 */
export function addApp(data) {
  return request.post('/app', data)
}

/**
 * 编辑交付应用（管理员）
 * PUT /api/app/{id}
 */
export function updateApp(id, data) {
  return request.put(`/app/${id}`, data)
}

/**
 * 删除交付应用（管理员）
 * DELETE /api/app/{id}
 */
export function deleteApp(id) {
  return request.delete(`/app/${id}`)
}

/**
 * 批量删除交付应用（管理员）
 * DELETE /api/app/batch
 */
export function batchDeleteApps(ids) {
  return request.delete('/app/batch', { data: { ids } })
}

// ==================== 宣贯数据模块 ====================

/**
 * 获取宣贯数据列表（公开）
 * GET /api/showcase/list
 * @param {string} category - 维度类别（可选）
 */
export function getShowcaseItems(category) {
  return request.get('/showcase/list', { params: { category } })
}

/**
 * 获取宣贯数据详情（公开）
 * GET /api/showcase/{id}
 */
export function getShowcaseDetail(id) {
  return request.get(`/showcase/${id}`)
}

/**
 * 新增宣贯数据（管理员）
 * POST /api/showcase
 */
export function addShowcaseItem(data) {
  return request.post('/showcase', data)
}

/**
 * 编辑宣贯数据（管理员）
 * PUT /api/showcase/{id}
 */
export function updateShowcaseItem(id, data) {
  return request.put(`/showcase/${id}`, data)
}

/**
 * 删除宣贯数据（管理员）
 * DELETE /api/showcase/{id}
 */
export function deleteShowcaseItem(id) {
  return request.delete(`/showcase/${id}`)
}

/**
 * 批量删除宣贯数据（管理员）
 * DELETE /api/showcase/batch
 */
export function batchDeleteShowcaseItems(ids) {
  return request.delete('/showcase/batch', { data: { ids } })
}

// ==================== 统计模块 ====================

/**
 * 获取平台概览统计数据（公开）
 * GET /api/stats/overview
 * @returns {Promise} - { appCount, processCount, dataModelCount, algoModelCount, userCount, totalClicks, appTypeStats, topApps }
 */
export function getStatsOverview() {
  return request.get('/stats/overview')
}
