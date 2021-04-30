`<template>
  <el-row>
    <el-col class="menu">
      <el-menu
          :default-active="$route.path"
          :router="true"
          class="el-menu-vertical-demo">
        <el-menu-item index="/home/admin/returnBooks">
          <i class="el-icon-s-promotion"></i>
          <span slot="title">现场还书</span>
        </el-menu-item>
        <el-menu-item index="/home/admin/borrowBooks">
          <i class="el-icon-document"></i>
          <span slot="title">现场借书</span>
        </el-menu-item>
        <el-menu-item index="/home/admin/fetchBooks">
          <i class="el-icon-collection"></i>
          <span slot="title">现场取书</span>
        </el-menu-item>
        <el-menu-item index="/home/admin/upload">
          <i class="el-icon-upload"></i>
          <span slot="title">上传新书</span>
        </el-menu-item>
        <el-menu-item index="/home/admin/addCopy">
          <i class="el-icon-notebook-1"></i>
          <span slot="title">添加副本</span>
        </el-menu-item>
        <el-menu-item index="/home/admin/addAdmin" v-if="isSuperAdmin">
          <i class="el-icon-s-custom"></i>
          <span slot="title">新管理员</span>
        </el-menu-item>
      </el-menu>
    </el-col>
    <el-col class="content">
      <router-view/>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "Admin",
  data() {
    return {
      isSuperAdmin: localStorage.getItem('role') === 'superadmin'
    }
  },
  methods: {

  },
  mounted() {
    if (!localStorage.getItem('login')) {
      this.$message.error('请先登录')
      this.$router.push('/login')
    } else if (localStorage.getItem('role') !== 'admin' && localStorage.getItem('role') !== 'superadmin') {
      this.$message.error('您不是管理员，无法访问该页面')
      this.$router.push('/login')
    } else if (parseInt(localStorage.getItem('exp')) < ((new Date().getTime())/1000)) {
      this.$message.error('登录过期，请先登录')
      this.$router.push('/login')
    }
  }
}
</script>

<style>
.menu {
  width: 12%;
  float: left;
}
.content {
  float: right;
  width: 85%;
}
.router-link-active {
  color: deepskyblue;
}
.admin-link {
  text-decoration: none;
  color: inherit;
}
</style>
