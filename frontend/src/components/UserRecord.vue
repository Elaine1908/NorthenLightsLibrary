<template>
  <div class="show">
    <div style="margin-top: 30px">
      <el-radio-group v-model="type" style="margin-bottom: 30px;">
        <el-radio-button label="reserveRecordList">预约记录</el-radio-button>
        <el-radio-button label="borrowRecordList">借阅记录</el-radio-button>
        <el-radio-button label="returnRecordList">还书记录</el-radio-button>
        <el-radio-button label="fineRecordList">罚款记录</el-radio-button>
      </el-radio-group>
    </div>
    <template v-if="type=='reserveRecordList'">
      <el-table
              :data="reserveRecordList"
              border
              style="width: 100%">
        <el-table-column
                prop="time"
                label="日期"
                width="180">
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
      </el-table>
    </template>
    <template v-if="type=='borrowRecordList'">
      <el-table
              :data="borrowRecordList"
              border
              style="width: 100%">
        <el-table-column
                prop="time"
                label="日期"
                width="180">
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
    <template v-if="type=='returnRecordList'">
      <el-table
              :data="returnRecordList"
              border
              style="width: 100%">
        <el-table-column
                prop="time"
                label="日期"
                width="180">
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
    <template v-if="type=='fineRecordList'">
      <el-table
              :data="fineRecordList"
              border
              style="width: 100%">
        <el-table-column
                prop="fineID"
                label="罚款ID"
                width="80">
        </el-table-column>
        <el-table-column
                prop="time"
                label="日期"
                width="180">
        </el-table-column>
        <el-table-column
                prop="username"
                label="用户名"
                width="160">
        </el-table-column>
        <el-table-column
                prop="reason"
                label="罚款原因"
                width="220">
        </el-table-column>
        <el-table-column
                prop="money"
                label="罚款金额">
        </el-table-column>
        <el-table-column
                prop="status"
                label="是否支付">
        </el-table-column>
        <el-table-column
                label="操作">
          <template slot-scope="scope">
            <el-button @click="payFine(scope.row.fineID)" v-if="scope.row.status=='未支付'">缴纳</el-button>
            <el-button v-if="scope.row.status=='已支付'" disabled>缴纳</el-button>
          </template>
        </el-table-column>
      </el-table>
    </template>
  </div>
</template>

<script>
  export default {
    name:"UserRecord",
    data() {
      return {
        type: 'reserveRecordList',
        reserveRecordList:[],
        borrowRecordList:[],
        returnRecordList:[],
        fineRecordList:[{
          status:'已支付'
        },{
          status: '未支付'
        }]
      }
    },
    created() {
      this.axios.get('/user/myRecord').then(resp => {
        if (resp.status === 200){
          this.reserveRecordList=resp.data.reserveRecordList;
          this.borrowRecordList=resp.data.borrowRecordList;
          this.returnRecordList=resp.data.returnRecordList;
          this.fineRecordList=resp.data.fineRecordList;
          for(let i=0;i<this.fineRecordList.length;i++) {
            this.fineRecordList[i].money = ''+(this.fineRecordList[i].money/100.0).toFixed(2)+'元';
          }
        } else {
          this.$message(resp.data.message);
        }
      }).catch(err => {
        this.$message.error(err.response.data.message)
      })
    },
    methods:{
      payFine(item){
        this.$axios.post('/user/payfine', {
          fineID: item
        }).then(data => {
          if(data.status==200) {
            this.$message.success('支付成功');
          }
        }).catch(err => {
          this.$message.error(err.response.data.message)
        })
      }
    }
  }
</script>

<style>

</style>
