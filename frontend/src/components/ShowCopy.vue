<template>
  <div>
    <el-table
            ref="multipleTable"
            :data="tableData"
            tooltip-effect="dark"
            style="width: 100%">
      <el-table-column
              label="书名"
              width="120">
        <template slot-scope="scope">{{ scope.row.name }}</template>
      </el-table-column>
      <el-table-column
              prop="author"
              label="作者"
              width="120">
        <template slot-scope="scope">{{ scope.row.author }}</template>
      </el-table-column>
      <el-table-column
              prop="uniqueBookMark"
              label="ISBN"
              width="200">
        <template slot-scope="scope">{{ scope.row.uniqueBookMark }}</template>
      </el-table-column>
      <el-table-column
              prop="libraryName"
              label="所在分馆"
              width="120">
        <template slot-scope="scope">{{ scope.row.libraryName }}</template>
      </el-table-column>
      <el-table-column
              prop="status"
              label="状态"
              width="120">
        <template slot-scope="scope">{{ scope.row.status }}</template>
      </el-table-column>
      <el-table-column
              prop="borrower"
              label="借书者"
              width="120">
        <template slot-scope="scope">{{ scope.row.borrower }}</template>
      </el-table-column>
      <el-table-column>
        <el-button slot-scope="scope" class="button" @click="reserve(scope.row.uniqueBookMark)">预约</el-button>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    name: "ShowCopy",
    data() {
      return {
        tableData: []
      }
    },
    created() {
      this.axios.get('/useradmin/getBookTypeAndCopy',{
        params:{
          isbn:this.$route.query.isbn
        }
      }).then(resp => {
        if (resp.status === 200){
          this.tableData=resp.data.bookCopies;
        } else {
          this.$message(resp.data.message);
        }
      })
    },
    methods: {
      reserve:function(uniqueBookMark){
        this.$axios.post('/user/reserveBook',{
          uniqueBookMark:uniqueBookMark
        }).then(data => {
          this.$message.success(data.data.message)
        }).catch(err => {
          this.$message.error(err.response.data.message)
        })
      }
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

<style scoped>

</style>
