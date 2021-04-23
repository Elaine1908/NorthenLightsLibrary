<template>
  <el-row>
    <el-col class="menu">
      <el-menu
          class="el-menu-vertical-demo">
        <el-menu-item index="1">
            <span slot="title">
              <router-link to="/home/admin/returnBooks" class="admin-link">
                <i class="el-icon-menu"></i> 现场还书
              </router-link>
            </span>
        </el-menu-item>
        <el-menu-item index="2">
            <span slot="title">
              <router-link to="/home/admin/borrowBooks" class="admin-link">
                <i class="el-icon-document"></i> 现场借书
              </router-link></span>
        </el-menu-item>
        <el-menu-item index="3">
          <i class="el-icon-setting"></i>
          <span slot="title">有四个字</span>
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
  methods: {

  }
}
</script>

<style>
.menu {
  width: 20%;
  float: left;
}
.content {
  float: right;
  display: contents;
}
.router-link-active {
  color: deepskyblue;
}
.admin-link {
  text-decoration: none;
  color: inherit;
}
</style>