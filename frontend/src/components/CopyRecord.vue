<template>
  <el-table
          :data="recordList"
          stripe
          style="width: 100%">
    <el-table-column
            prop="time"
            label="日期"
            width="180">
    </el-table-column>
    <el-table-column
            prop="type"
            label="类型">
    </el-table-column>
    <el-table-column
            prop="username"
            label="用户名"
            width="180">
    </el-table-column>
    <el-table-column
            prop="uniqueBookMark"
            label="ISBN">
    </el-table-column>
    <el-table-column
            prop="libraryName"
            label="所在分馆">
    </el-table-column>
    <el-table-column
            prop="adminName"
            label="管理员">
    </el-table-column>
  </el-table>
</template>

<script>
  export default {
    name: "CopyRecord",
    data() {
      return {
        recordList: []
      }
    },
    created() {
      this.axios.get('/admin/recordOfBook',{
        params: {
          isbn: this.$route.query.isbn
        }
      }).then(resp => {
        if (resp.status === 200){
          this.recordList = resp.data.recordList;
        }
      }).catch(err => {
        this.$message.error(err.response.data.message)
      })
    }
  }
</script>

<style scoped>

</style>
