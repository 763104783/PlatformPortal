import axios from 'axios'

/**
 * Axios实例配置
 * 基础URL通过Vite代理到后端
 */
const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器：自动添加token
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = token
    }
    return config
  },
  error => Promise.reject(error)
)

// 响应拦截器：统一错误处理
request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      // 401未授权
      if (res.code === 401) {
        localStorage.removeItem('token')
        localStorage.removeItem('role')
        window.location.href = '/login'
      }
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res
  },
  error => {
    return Promise.reject(error)
  }
)

export default request
