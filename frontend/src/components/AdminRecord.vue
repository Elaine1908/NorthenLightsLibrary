<template>
  <div>
  <div class="search_bar" style="margin-bottom: 60px">
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="formInline.username" placeholder="用户名"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="toRes">查询</el-button>
      </el-form-item>
    </el-form>
  </div>
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
                  prop="reason"
                  label="罚款原因">
          </el-table-column>
          <el-table-column
                  prop="money"
                  label="罚款金额">
          </el-table-column>
          <el-table-column
                  prop="status"
                  label="是否支付">
          </el-table-column>
        </el-table>
      </template>
    </div>
  </div>
</template>

<script>
  export default {
    name: "AdminRecord",
    data(){
      return{
        type: 'reserveRecordList',
        formInline: {
          usernaem:''
        }
      }
    },
    methods:{
      toRes(){
        this.$message('hhhhh');
        this.$router.push({name: 'ShowReservation'});
      }
    }
  }
</script>

<style scoped>
  .search_bar {
    width: 100%;
    height: 40px;
    margin-bottom: 30px;
  }
</style>
