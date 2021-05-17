<template>
    <el-form
            :model="form"
            :rules="rules"
            ref="form">
      <el-table
              :data="form.datas"
              highlight-current-row
              style="width: 100%">
        <el-table-column
                prop="type"
                label="类型">
          <template slot-scope="scope">
            <template>
              {{scope.row.type}}
            </template>
          </template>
        </el-table-column>

        <el-table-column
                prop="time"
                label="时长(天)">
          <template slot-scope="scope">
            <template v-if="scope.row.action == 'view'">
              {{scope.row.time}}
            </template>
            <template v-else>
              <el-form-item
                      :prop="'datas.'+scope.$index + '.time'"
                      :rules='rules.time'>
                <el-input
                        size="mini"
                        v-model.number="scope.row.time"
                        style="width: 60px;"></el-input>
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
</template>

<script>
  export default {
    name: "TimeSet",
    data() {
      return {
        form: {
          datas: [
            {type: "借阅", time:20 },
            {type: "预约", time:32 },
          ],
        },

        //表单验证规则
        rules: {
          time: [{
            type: 'number',
            required: true,
            trigger: 'blur',
            message: '请输入时长',
          },
            {
              type: 'number',
              trigger: 'blur',
              min: 1,
              max: 60,
              message: '最少1天，最多60天',
            }]
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
    }
  }
</script>

<style scoped>
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
