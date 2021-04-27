import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    // 保存登录状态
    login: false,
    username:'未登录',
    identity: 0,
    campusID: 0
  },
  // mutations: 专门书写方法,用来更新 state 中的值
  mutations: {
    // 登录
    doLogin(state, payload) {
      state.login = true;
      localStorage.setItem('token', payload.token)
      state.username = payload.username;
      switch (payload.identity) {
        case 'admin':
          state.identity = 2;
          break;
        case 'superadmin':
          state.identity = 1;
          break;
        case 'teacher':
        case 'student':
          state.identity = 3;
      }
      if (state.identity !== payload.loginIdentity) {
        this.$message.info('您填写的身份与账号不匹配，已经以' + payload.identity + '的身份登录')
      }
      state.campusID = payload.campusID;
      let jwt = require('jsonwebtoken')
      console.log(jwt.decode(payload.token))
    },
    // 退出
    doLogout(state) {
      state.login = false;
      localStorage.removeItem('token')
      state.username='未登录';
      state.identity = 0;
      state.campusID = 0
    }
  }
})
