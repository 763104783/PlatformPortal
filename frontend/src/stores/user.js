import { defineStore } from 'pinia'
import { login as loginApi, getUserInfo } from '../api'

/**
 * 用户状态管理
 */
export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userId: null,
    username: '',
    role: localStorage.getItem('role') || ''
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    isAdmin: (state) => state.role === 'ADMIN'
  },

  actions: {
    /** 登录 */
    async login(username, password) {
      const res = await loginApi({ username, password })
      this.token = res.data.token
      this.userId = res.data.userId
      this.username = res.data.username
      this.role = res.data.role
      localStorage.setItem('token', this.token)
      localStorage.setItem('role', this.role)
      return res
    },

    /** 登出 */
    logout() {
      this.token = ''
      this.userId = null
      this.username = ''
      this.role = ''
      localStorage.removeItem('token')
      localStorage.removeItem('role')
    },

    /** 获取用户信息 */
    async fetchUserInfo() {
      if (!this.token) return
      try {
        const res = await getUserInfo()
        this.userId = res.data.id
        this.username = res.data.username
        this.role = res.data.role
      } catch (e) {
        this.logout()
      }
    }
  }
})
