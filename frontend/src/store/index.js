import Vue from 'vue'
import Vuex from 'vuex'
import Storage from "../assets/js/Storage";

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    // 保存登录状态
    token: localStorage.getItem('token') || null,
    userDetails: localStorage.getItem('userDetails') || null,
    login: localStorage.getItem('login') || false
  },
  // mutations: 专门书写方法,用来更新 state 中的值
  mutations: {
    // 登录
    doLogin(state, payload) {
      let jwt = require('jsonwebtoken')
      let tokenContent = jwt.decode(payload.token)
      localStorage.setItem('token', payload.token)
      localStorage.setItem('userDetails', tokenContent)
      localStorage.setItem('login', true)
    },
    // 退出
    doLogout(state) {
      localStorage.removeItem('token')
      localStorage.removeItem('userDetails')
      localStorage.removeItem('login')
    }
  }
})
