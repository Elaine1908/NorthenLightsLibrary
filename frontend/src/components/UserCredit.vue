<template>
  <div>
    <el-table
            :data="creditRecord"
            border
            style="width: 100%">
      <el-table-column
              prop="time"
              label="日期"
              width="180">
      </el-table-column>
      <el-table-column
              prop="amount"
              label="信用分"
              width="180">
      </el-table-column>
      <el-table-column
              prop="description"
              label="原因">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    name: "UserCredit",
    data() {
      return {
        creditRecord:[]
      }
    },
    created() {//初始化操作
      this.axios.get('/user/myCreditRecord').then(resp => {
        if (resp.status === 200) {
          this.creditRecord=resp.data.myCreditRecordList;
        } else {
          this.$message(resp.data.message);
        }
      }).catch(err => {
        this.$message.error(err.response.data.message)
      })
    }
  }
</script>

<style scoped>

</style>
