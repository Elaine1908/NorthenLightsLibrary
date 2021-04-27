<template>
  <el-table
          :data="userInfo"
          border
          style="width: 100%">
    <el-table-column
            prop="username"
            label="用户名"
            width="180">
    </el-table-column>
    <el-table-column
            prop="role"
            label="身份"
            width="180">
    </el-table-column>
    <el-table-column
            prop="credit"
            label="信誉积分">
    </el-table-column>
  </el-table>
</template>

<script>
  export default {
    name: "UserInfo",
    data() {
      return {
        userInfo: [{
          username: 'hello',
          credit: 100,
          role: 'hello'
        }]
      }
    },
    created() {//初始化操作
      this.axios.get('/user/userinfo').then(resp => {
        if (resp.status === 200) {
          this.userInfo[0] = resp.data;
          console.log(this.userInfo)
          console.log(resp.data)
        } else {
          this.$message(resp.data.message);
        }
      })
    }
  }
</script>

<style scoped>

</style>
