<template>
  <div>
    <el-form :model="form" :rules="rules" ref="form">
      <el-table :data="form.datas" highlight-current-row style="width: 100%">
        <el-table-column prop="role" label="身份" width="60"></el-table-column>

        <el-table-column prop="max_book_borrow" label="借阅副本数">
          <template slot-scope="scope">
            <template v-if="scope.row.action == 'view'">
              {{scope.row.max_book_borrow}}
            </template>
            <template v-else>
              <el-form-item :prop="'datas.'+scope.$index + '.max_book_borrow'" :rules='rules.max_book_borrow'>
                <el-input size="mini" v-model.number="scope.row.max_book_borrow" style="width: 80px;margin-left: 30px"></el-input>
              </el-form-item>
            </template>
          </template>
        </el-table-column>

        <el-table-column prop="max_borrow_time" label="借阅时长">
          <template slot-scope="scope">
            <template v-if="scope.row.action == 'view'">
              <vue-timepicker manual-input hide-dropdown @focus="focusState = 'focused'" @blur="focusState = 'blurred'" @open="dropdownState = 'opened'" @close="dropdownState = 'closed'" format="HH时mm分ss秒"></vue-timepicker>
            </template>
            <template v-else>
              <vue-timepicker manual-input hide-dropdown @focus="focusState = 'focused'" @blur="focusState = 'blurred'" @open="dropdownState = 'opened'" @close="dropdownState = 'closed'"></vue-timepicker>
              <!--              <el-form-item :prop="'datas.'+scope.$index + '.max_borrow_time'" :rules='rules.max_borrow_time'>-->
<!--                <el-input size="mini" v-model.trim="scope.row.max_borrow_time" style="width: 80px;margin-left: 30px"></el-input>-->
<!--              </el-form-item>-->
            </template>
          </template>
        </el-table-column>

        <el-table-column prop="max_reserve_time" label="预约时长">
          <template slot-scope="scope">
            <template v-if="scope.row.action == 'view'">
              {{scope.row.max_reserve_time}}
            </template>
            <template v-else>
              <el-form-item :prop="'datas.'+scope.$index + '.max_reserve_time'" :rules='rules.max_reserve_time'>
                <el-input size="mini" v-model.trim="scope.row.max_reserve_time" style="width: 80px;margin-left: 30px"></el-input>
              </el-form-item>
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
      return {
        focusState: 'blurred',

        // variable for the dropdown state
        dropdownState: 'closed',
        value1: new Date(2016, 9, 10, 18, 40),
        form: {
          datas: [
            { role: "学生", max_book_borrow:8,max_borrow_time:11,max_reserve_time:"hh" },
            { role: "老师", max_book_borrow:6,max_borrow_time:222,max_reserve_time:"joo"},
          ],
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
              type: 'number',
              trigger: 'blur',
              min: 1,
              max: 10,
              message: '最小1，最大10',
            }],
          max_borrow_time: [{
            type: 'string',
            required: true,
            trigger: 'blur',
            message: '请输入借阅时长',
          }],
          max_reserve_time: [{
            type: 'string',
            required: true,
            trigger: 'blur',
            message: '请输入预约时长',
          }],
        }
      }
    },

    created() {
      //处理数据，为已有数据添加action:'view'
      this.form.datas.map(item => {
        this.$set(item,"action","view")
        return item;
      });
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

      //编辑-保存操作
      click_save(item,index) {
        if( !this.validateField('form',index) ) return;
        item.action = "view";
      },

      //编辑-取消操作
      click_cancle(item,index) {
        this.resetField('form',index);
        item.action = "view";
      },

      //编辑操作
      click_edit(item,index) {
        item.action = "edit";
      }
    },
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
</style>
