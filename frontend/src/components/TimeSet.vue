<template>
  <div>
    <el-form :model="setForm" :rules="rules" ref="setForm" class="setForm">
      <el-table :data="setForm.datas" highlight-current-row style="width: 100%">
        <el-table-column prop="role" label="身份" width="80px"></el-table-column>

        <el-table-column prop="max_book_borrow" label="借阅副本数" style="width:100px ">
          <template slot-scope="scope">
            <template v-if="scope.row.action === 'view'">
              {{scope.row.max_book_borrow}}
            </template>
            <template v-else>
              <el-form-item :prop="'datas.'+scope.$index + '.max_book_borrow'" :rules='
              {required: true, validator: (rule, value, callback) => {
                if (value < 1 && value > 10) {
                 callback(new Error("请输入1-10的数字"))
              } else if (value === ""){
              callback(new Error("请输入一个数字"))
              } else {callback()} }, trigger: "blur"}'>
                <input class="timeInput" style="width:60px" size="mini" v-model.number="scope.row.max_book_borrow" onkeyup = "value=value.replace(/[^\d]/g,'')"></input>
              </el-form-item>
            </template>
          </template>
        </el-table-column>

        <el-table-column prop="max_borrow_time" label="借阅时长">
          <template slot-scope="scope">
            <template v-if="scope.row.action == 'view'">
              {{scope.row.max_borrow_time.day}}天{{scope.row.max_borrow_time.hour}}时{{scope.row.max_borrow_time.min}}分{{scope.row.max_borrow_time.sec}}秒
            </template>
            <template v-else>
              <input placeholder="天" class="timeInput" v-model.number="scope.row.max_borrow_time.day" onkeyup = "value=value.replace(/[^\d]/g,'')"></input>天
              <input placeholder="时" class="timeInput" v-model.number="scope.row.max_borrow_time.hour" onkeyup = "value=value.replace(/[^\d]/g,'')"></input>时
              <input placeholder="分" class="timeInput" v-model.number="scope.row.max_borrow_time.min" onkeyup = "value=value.replace(/[^\d]/g,'')"></input>分
              <input placeholder="秒" class="timeInput" v-model.number="scope.row.max_borrow_time.sec" onkeyup = "value=value.replace(/[^\d]/g,'')"></input>秒
            </template>
          </template>
        </el-table-column>

        <el-table-column prop="max_reserve_time" label="预约时长">
          <template slot-scope="scope">
            <template v-if="scope.row.action == 'view'">
              {{scope.row.max_reserve_time.day}}天{{scope.row.max_reserve_time.hour}}时{{scope.row.max_reserve_time.min}}分{{scope.row.max_reserve_time.sec}}秒
            </template>
            <template v-else>
              <input placeholder="天" class="timeInput" v-model.number="scope.row.max_reserve_time.day" onkeyup="this.value=this.value.replace(/^0(0+|\d+)|[^\d]+/g,'')"></input>天
              <input placeholder="时" class="timeInput" v-model.number="scope.row.max_reserve_time.hour"></input>时
              <input placeholder="分" class="timeInput" v-model.number="scope.row.max_reserve_time.min"></input>分
              <input placeholder="秒" class="timeInput" v-model.number="scope.row.max_reserve_time.sec"></input>秒
            </template>
          </template>
        </el-table-column>

        <el-table-column prop="operation" label="操作">
          <template slot-scope="scope">
            <template v-if="scope.row.action == 'view'">
              <el-button size="mini" @click="click_edit(scope.row, scope.$index)">编辑</el-button>
            </template>
            <template v-else>
              <el-button size="mini" @click="click_save(scope.row, scope.$index)">保存</el-button>
              <el-button size="mini" @click="click_cancle(scope.row, scope.$index)">取消</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
  </div>
</template>
<script>
  import VueTimepicker from 'vue2-timepicker/src/vue-timepicker.vue'
  export default {
    components:{
      'vue-timepicker':VueTimepicker
    },
    data() {
      let validateAmount = (rule, value, callback) => {
        if (value < 1 && value > 10) {
          callback(new Error('请输入1-10的数字'))
        } else {
          callback()
        }
      }
      return {
        setForm: {
          datas: [],
        },

        //表单验证规则
        rules: {
          max_book_borrow: [{
            type: 'number',
            required: true,
            trigger: 'blur',
            message: '请输入最大借阅副本数',
          },
            {
              /*type: 'number',
              trigger: 'blur',
              validator: validateAmount*/
              // min: 1,
              // max: 10,
              // message: '最小1，最大10',
            }]
        }
      }
    },

    created() {
      this.axios.get('/superadmin/userConfiguration').then(resp => {
        if (resp.status === 200) {
          this.setForm.datas = resp.data.userConfigurationList;
          for (let i = 0; i < this.setForm.datas.length; i++) {
            this.setForm.datas[i].role = resp.data.userConfigurationList[i].role;
            this.setForm.datas[i].max_book_borrow = resp.data.userConfigurationList[i].maxBookBorrow;
            this.setForm.datas[i].max_borrow_time = resp.data.userConfigurationList[i].maxBorrowTime;
            this.setForm.datas[i].max_reserve_time = resp.data.userConfigurationList[i].maxReserveTime;
          }
          //处理数据，为已有数据添加action:'view'
          this.setForm.datas.map(item => {
            this.$set(item,"action","view")
            return item;
          });
        } else {
          this.$message(resp.data.message);
        }
      })
    },

    methods: {
      //对部分表单字段进行校验
      validateField(form,index){
        let result = true;
        for (let item of this.$refs[form].fields) {
          if(item.prop.split(".")[1] == index){
            this.$refs[form].validateField(item.prop,(error)=>{
              if(error!=""){
                result = false;
              }
            });
          }
          if(!result) break;
        }
        return result;
      },

      //对部分表单字段进行重置
      resetField(form,index){
        this.$refs[form].fields.forEach(item=>{
          if(item.prop.split(".")[1] == index){
            item.resetField();
          }
        })
      },

      //编辑-保存操作(相当于提交按钮)
      click_save(item,index) {
        if (item.max_borrow_time.day === undefined) {
          item.max_borrow_time.day = 0
        }
        if (item.max_borrow_time.hour === undefined) {
          item.max_borrow_time.hour = 0
        }
        if (item.max_borrow_time.min === undefined) {
          item.max_borrow_time.min = 0
        }
        if (item.max_borrow_time.sec === undefined) {
          item.max_borrow_time.sec = 0
        }
        item.max_borrow_time.min += Math.floor(item.max_borrow_time.sec / 60)
        item.max_borrow_time.sec %= 60
        item.max_borrow_time.hour += Math.floor(item.max_borrow_time.min / 60)
        item.max_borrow_time.min %= 60
        item.max_borrow_time.day += Math.floor(item.max_borrow_time.hour / 24)
        item.max_borrow_time.hour %= 24
        if( !this.validateField('setForm',index) ) return;
        item.action = "view";
        this.$axios.post('/superadmin/setUserConfiguration', {
          role: item.role,
          maxBookBorrow:item.max_book_borrow,
          maxBorrowTime: {
            day:item.max_borrow_time.day,
            hour:item.max_borrow_time.hour,
            min:item.max_borrow_time.min,
            sec:item.max_borrow_time.sec,
          },
          maxReserveTime:{
            day:item.max_reserve_time.day,
            hour:item.max_reserve_time.hour,
            min:item.max_reserve_time.min,
            sec:item.max_reserve_time.sec,
          }
        }).then(data => {
          if(data.status==200) {
            this.$message.success('修改成功');
          }
        }).catch(err => {
          this.$message.error(err.response.data.message)
        })
      },

      //编辑-取消操作
      click_cancle(item,index) {
        this.resetField('setForm',index);
        item.action = "view";
      },

      //编辑操作
      click_edit(item,index) {
        item.action = "edit";
      }
    },
    mounted: () => {
      if (!localStorage.getItem('login')) {
        this.$message.error('请先登录')
        this.$router.push('/login')
      }
      else if (localStorage.getItem('role') !== 'admin' && localStorage.getItem('role') !== 'superadmin') {
        this.$message.error('您不是管理员，无法访问该页面')
        this.$router.push('/home/show')
      }
      else if (parseInt(localStorage.getItem('exp')) < ((new Date().getTime())/1000)) {
        this.$message.error('登录过期，请先登录')
        this.$router.push('/login')
      }
    }
  }
</script>


<style>
  .el-table .cell{
    overflow: visible;
  }
  .el-form-item{
    margin-bottom: 0;
  }
  .el-form-item__error{
    padding-top:0;
    margin-top:-3px;
  }
  .timeInput{
    width: 22px;
    border:1px solid #DCDFE6;
    height: 24px;
    color:#606266;
    border-radius: 4px;
    text-align: center;
    font-size: 10px;
    margin-right:5px;
  }
  .timeInput:focus {
    border: 1px solid #409EFF;
    outline: none;
  }
</style>
