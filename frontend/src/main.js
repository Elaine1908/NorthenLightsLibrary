import Vue from 'vue'
import './plugins/axios'
import App from './App.vue'
import router from './router'
import store from './store'
import './plugins/ant-design-vue.js'
import './plugins/element.js'
import 'element-ui/lib/theme-chalk/index.css';
import ElementUI from 'element-ui';

//import {Comment} from "ant-design-vue";
//import 'ant-design-vue/lib/comment/style/css'

axios.interceptors.request.use(config => {
    config.headers.authorization = window.sessionStorage.getItem("token");
    return config;
})

axios.defaults.withCredentials = true


Vue.prototype.$http = axios;
Vue.config.productionTip = false
Vue.use(ElementUI);

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
