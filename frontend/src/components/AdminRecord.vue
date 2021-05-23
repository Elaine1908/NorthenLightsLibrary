<template>
  <div>
    <div>
      <div style="position: absolute;right: 20px;top:22px" v-if="isSuperAdmin">
        <el-dropdown>
          <el-button type="primary" icon="el-icon-message" circle></el-button>
          <el-dropdown-menu>
            <el-dropdown-item @click.native="reminds">一键提醒</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <div class="search_bar" style="margin-top: 80px" v-if="!showBack">
        <el-form :inline="true" :model="formInline" class="demo-form-inline">
          <el-form-item>
            <el-input style="width: 300px" v-model="formInline.username" placeholder="用户名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="search">查询</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="show" v-if="showBack">
      <div style="margin-top: 30px;margin-bottom: 30px">
        <div style="position: absolute">
          <el-page-header @back="backToSearch"></el-page-header>
        </div>
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
        isSuperAdmin: localStorage.getItem('role') === 'superadmin',
        formInline: {
          username:''
        },
        showBack:false,
        reserveRecordList:[],
        borrowRecordList:[],
        returnRecordList:[],
        fineRecordList:[]
      }
    },
    methods:{
      search() {
        this.axios.get('/admin/record', {
          params: {
            username: this.formInline.username
          }
            }).then(resp => {
          if (resp.status === 200) {
            this.reserveRecordList = resp.data.reserveRecordList;
            this.borrowRecordList=resp.data.borrowRecordList;
            this.returnRecordList=resp.data.returnRecordList;
            this.fineRecordList=resp.data.fineRecordList;
            for(let i=0;i<this.fineRecordList.length;i++) {
              this.fineRecordList[i].money = ''+(this.fineRecordList[i].money/100.0).toFixed(2)+'元';
            }
            this.showBack=true;
            this.$message.success(resp.data.message)
          }
        }).then(err => {
          this.$message.error(err.response.data.message)
        })
      },
      backToSearch(){
        this.showBack=false;
      },
      reminds(){
        this.$axios.post('/superadmin/notify').then(data => {
          for(var i = 0;i<data.data.length;i++) {
            this.$message(data.data[i]);
          }
        }).catch(err => {
          for(var i = 0;i<err.response.data.message.length;i++){
            this.$message.error(err.response.data.message[i])
          }
        })
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
