import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/home',
    name: 'LoginRegister',
    component: () => import('@/views/LoginRegister'),
    children: [
      {
        path: 'register',
        name: 'Register',
        component: () => import('@/components/Register')
      },
      {
        path: 'login',
        name: 'Login',
        component: () => import('@/components/Login')
      }
    ]

  },
  {
    path: '/home',
    redirect: 'home/show',
    name: 'Home',
    component: () => import('@/views/Home'),
    children: [
      {
        path: 'upload',
        name: 'Upload',
        component: () => import('@/components/Upload')
      },
      {
        path: 'show',
        name: 'Show',
        component: () => import('@/components/Show'),
        children:[
          {
            path:'details',
            name:'Details',
            component:() => import('@/components/Details')
          }
        ]
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
