<template>
  <div class="home">
    <h2>{{activeIndex}}</h2>
    <el-menu class="el-menu-demo" mode="horizontal" @select="handleSelect" router="true">
      <el-menu-item index="1" @click="linkToHome">首页</el-menu-item>
      <el-submenu index="2">
        <template slot="title">我的账户</template>
        <el-menu-item index="2-1" @click="logout" v-if="this.$store.state.login">登出</el-menu-item>
        <el-menu-item index="2-1" @click="linkToLogin" v-else>登录</el-menu-item>
        <el-menu-item index="2-2" @click="linkToRegister" v-if="!this.$store.state.login">注册</el-menu-item>
        <el-menu-item index="2-3" @click="linkToModifyPass" v-if="this.$store.state.login">修改密码</el-menu-item>
        <el-menu-item index="2-2" @click="linkToPersonalInformation" v-if="this.$store.state.login">个人信息</el-menu-item>
      </el-submenu>
      <el-menu-item index="3" v-if="this.$store.state.identity === 3">在线预约</el-menu-item>
      <el-menu-item index="3" v-if="this.$store.state.identity === 1 || this.$store.state.identity === 2" @click="linkToAdmin">
        管理员
      </el-menu-item>
      <span class="test-test"><i class="el-icon-user"></i> {{this.$store.state.username}}</span>
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
      activeIndex: '1',
      activeIndex2: '1'
    };
  },
  methods: {
    handleSelect(key, keyPath) {
      //console.log(key, keyPath);
    },
    linkToHome() {
      this.$router.push({path:'/home/show'})
    },
    linkToLogin() {
      this.$router.push({path:'/login'})
    },
    linkToRegister() {
      this.$router.push({path:'/register'})
    },
    linkToModifyPass() {
      this.$router.push({path:'/modifyPath'})
    },
    linkToPersonalInformation() {
      this.$router.push({path:'/personalInfo'})
    },
    linkToAdmin() {
      this.$router.push({path:'/home/admin'})
      this.activeIndex = '3'
    },
    logout(){
      this.$store.commit("doLogout");
      this.$router.push({path:'/home/show'})
    }
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
  .test-test {
    float: right;
    color: gray;
    font-size: x-small;
  }
</style>
