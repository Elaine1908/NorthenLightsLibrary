import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    // 保存登录状态
    login: true,
    username:'Admin',
    identity: 1,
    campusID: 1
  },
  // mutations: 专门书写方法,用来更新 state 中的值
  mutations: {
    // 登录
    doLogin(state,value) {
      state.login = true;
      state.username=value;
    },
    // 退出
    doLogout(state) {
      state.login = false;
      state.username='我的账户';
    }
  }
})
