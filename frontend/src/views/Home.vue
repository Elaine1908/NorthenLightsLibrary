<template>
  <div class="home">
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect" :router="true">
      <el-menu-item index="/home/show">首页</el-menu-item>
      <el-submenu index="2">
        <template slot="title">我的账户</template>
        <el-menu-item @click="logout" v-if="isLogin">登出</el-menu-item>
        <el-menu-item index="/login" v-else>登录</el-menu-item>
        <el-menu-item index="/register" v-if="!isLogin">注册</el-menu-item>
        <el-menu-item index="/modifyPassword" v-if="isLogin">修改密码</el-menu-item>
        <el-menu-item index="/home/user" v-if="isLogin">个人信息</el-menu-item>
      </el-submenu>
      <el-menu-item index="/home/admin">
        管理员
      </el-menu-item>
      <span class="username"><i class="el-icon-user"></i></span>
    </el-menu>
    <router-view class="upload-form"/>
  </div>
</template>

<script>
// @ is an alias to /src

export default {
  name: 'Home',
  data() {
    return {
      activeIndex: '',
      isLogin: localStorage.getItem('login')
    };
  },
  methods: {
    handleSelect(key, keyPath) {
      //console.log(key, keyPath);
    },
    logout(){
      this.$store.commit("doLogout");
      this.$router.go(0)
    }
  },
  mounted: function () {
    if (/^\/home\/admin*/.test(this.$route.path)) {
      this.activeIndex = '/home/admin'
    } else if (/^\/home\/show*/.test(this.$route.path)) {
      this.activeIndex = '/home/show'
    } else if (/^\/home\/user*/.test(this.$route.path)) {
      this.activeIndex = '/home/user'
    }
    this.isLogin = localStorage.getItem('login')
  }
}
</script>

<style>
  .home {
    margin: 20px 5%;
  }

  .home-link {
    text-decoration: none;
  }

  .upload-form {
    margin: 40px 0 0 0;
  }

  .is-active {
    font-weight: bold;
  }
  .username {
    float: right;
    color: gray;
    font-size: x-small;
  }
</style>
