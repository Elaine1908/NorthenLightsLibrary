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
              width="120">
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
        <el-button @click="reserve(scope.row.uniqueBookMark)">预约</el-button>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    name: "ShowCopy",
    data() {
      return {
        tableData: [{
          uniqueBookMark: this.$route.query.isbn,
          name: '王小虎',
          status: '上海市普陀区金沙江路 1518 弄'
        }
        ]
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
      reserve(uniqueBookMark){
        this.$axios.post('/user/reserveBook',{
          params:{
            uniqueBookMark:uniqueBookMark
          }
        }).then(data => {
          this.$message.success(data.data.message)
        }).catch(err => {
          this.$message.error(err.response.data.message)
        })
      }
    }
  }
</script>

<style scoped>

</style>
