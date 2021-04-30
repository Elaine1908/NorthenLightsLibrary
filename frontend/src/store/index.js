import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    // 保存登录状态
    token: localStorage.getItem('token') || null,
    username: localStorage.getItem('username') || null,
    role: localStorage.getItem('role') || null,
    exp: localStorage.getItem('exp') || null,
    login: localStorage.getItem('login') || false
  },
  // mutations: 专门书写方法,用来更新 state 中的值
  mutations: {
    // 登录
    doLogin(state, payload) {
      let jwt = require('jsonwebtoken')
      let tokenContent = jwt.decode(payload.token)
      localStorage.setItem('token', payload.token)
      localStorage.setItem('username', tokenContent.username)
      localStorage.setItem('role', tokenContent.role)
      localStorage.setItem('exp', tokenContent.exp)
      localStorage.setItem('login', true)
    },
    // 退出
    doLogout(state) {
      localStorage.removeItem('token')
      localStorage.removeItem('username')
      localStorage.removeItem('role')
      localStorage.removeItem('exp')
      localStorage.removeItem('login')
    }
  }
})
