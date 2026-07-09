import request from './request'

/** 登录 */
export function login(data) {
  return request.post('/auth/login', data)
}

/** 登出 */
export function logout() {
  return request.post('/auth/logout')
}

/** 获取当前用户信息 */
export function getUserInfo() {
  return request.get('/auth/info')
}

/** 获取用户列表 */
export function getUsers(params) {
  return request.get('/users', { params })
}

/** 新增用户 */
export function addUser(data) {
  return request.post('/users', data)
}

/** 更新用户 */
export function updateUser(id, data) {
  return request.put(`/users/${id}`, data)
}

/** 删除用户 */
export function deleteUser(id) {
  return request.delete(`/users/${id}`)
}

/** 获取应用列表 */
export function getApps(params) {
  return request.get('/apps', { params })
}

/** 获取应用详情 */
export function getAppDetail(id) {
  return request.get(`/apps/${id}`)
}

/** 新增应用 */
export function addApp(data) {
  return request.post('/apps', data)
}

/** 更新应用 */
export function updateApp(id, data) {
  return request.put(`/apps/${id}`, data)
}

/** 删除应用 */
export function deleteApp(id) {
  return request.delete(`/apps/${id}`)
}

/** 记录点击 */
export function clickApp(id) {
  return request.post(`/apps/${id}/click`)
}

/** 获取分类列表 */
export function getCategories() {
  return request.get('/categories')
}

/** 新增分类 */
export function addCategory(data) {
  return request.post('/categories', data)
}

/** 更新分类 */
export function updateCategory(id, data) {
  return request.put(`/categories/${id}`, data)
}

/** 删除分类 */
export function deleteCategory(id) {
  return request.delete(`/categories/${id}`)
}

/** 获取宣贯数据列表 */
export function getShowcaseItems(category) {
  return request.get('/showcase', { params: { category } })
}

/** 获取宣贯详情 */
export function getShowcaseDetail(id) {
  return request.get(`/showcase/${id}`)
}

/** 新增宣贯项 */
export function addShowcaseItem(data) {
  return request.post('/showcase', data)
}

/** 更新宣贯项 */
export function updateShowcaseItem(id, data) {
  return request.put(`/showcase/${id}`, data)
}

/** 删除宣贯项 */
export function deleteShowcaseItem(id) {
  return request.delete(`/showcase/${id}`)
}

/** 获取平台配置 */
export function getConfigs() {
  return request.get('/config')
}

/** 更新平台配置 */
export function updateConfigs(data) {
  return request.put('/config', data)
}

/** 上传文件 */
export function uploadFile(file, fileKey) {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('fileKey', fileKey)
  return request.post('/config/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/** 获取统计数据 */
export function getStatsOverview() {
  return request.get('/stats/overview')
}
