<template>
  <div>
    <el-form
            :model="form"
            :rules="rules"
            ref="form">
      <el-table
              :data="form.datas"
              highlight-current-row
              style="width: 100%">

        <el-table-column
                prop="id"
                label="序号"
                width="60"></el-table-column>
        <el-table-column
                prop="username"
                label="用户名">
          <template slot-scope="scope">
            <template v-if="scope.row.action == 'view'">
              {{scope.row.username}}
            </template>
            <template v-else>
              <el-form-item
                      :prop="'datas.'+scope.$index + '.username'"
                      :rules='rules.username'>
                <el-input
                        size="mini"
                        v-model.trim="scope.row.username"
                        style="width: 120px;"></el-input>
              </el-form-item>
            </template>
          </template>
        </el-table-column>

        <el-table-column
                prop="email"
                label="Email">
          <template slot-scope="scope">
            <template v-if="scope.row.action == 'view'">
              {{scope.row.email}}
            </template>
            <template v-else>
              <el-form-item
                      :prop="'datas.'+scope.$index + '.email'"
                      :rules='rules.email'>
                <el-input
                        size="mini"
                        v-model.trim="scope.row.email"
                        style="width: 200px;"></el-input>
              </el-form-item>
            </template>
          </template>
        </el-table-column>

        <el-table-column prop="operation" label="操作">
          <template slot-scope="scope">
            <template v-if="scope.row.action == 'view'">
              <el-button size="mini" @click="click_delete(scope.row, scope.$index)">删除</el-button>
            </template>
            <template v-else-if="scope.row.action == 'add'">
              <el-button size="mini" @click="click_add( scope.row, scope.$index)">新增</el-button>
              <el-button size="mini" @click="click_reset(scope.row, scope.$index)">重置</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
  </div>
</template>
<script>



</script>
<script>
  export default {
    data() {
      var validEmail = (rule, value, callback) => {
        let emailPat = /^\w+@[a-zA-Z0-9]{2,10}(?:\.[a-z]{2,4}){1,3}$/
        if (value === '') {
          callback(new Error('请输入邮箱'))
        } else if (!emailPat.test(value)) {
          callback(new Error('请输入使用雷·汤普森创立的标准E-mail格式的邮箱'))
        } else {
          callback();
        }
      }
      return {
        form: {
          datas: []
        },

        //表单验证规则
        rules: {
          username: [{
            type: 'string',
            required: true,
            trigger: 'blur',
            message: '请输入用户名',
          }],
          email: [{
            validator: validEmail,
            required: true,
            trigger: 'blur'
          }],
        }
      }
    },

    created() {
      //显示已有管理员列表
      this.axios.get('/superadmin/showAdmin').then(resp => {
        if (resp.status === 200) {
          this.form.datas = resp.data.admin;
        } else {
          this.$message(resp.data.message);
        }
      })
      //处理数据，为已有数据添加action:'view'
      this.form.datas.map(item => {
        this.$set(item,"action","view")
        return item;
      });

      //再插入一条添加操作的数据
      this.form.datas.unshift({
        id:undefined,
        name:undefined,
        email:undefined,
        action: "add"
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

      //新增操作
      click_add(item,index) {
        if( !this.validateField('form',index) ) return;
        //模拟新增一条数据
        this.$axios.post('/superadmin/addAdmin', {
          username: item.username,
          password:'111111',//默认管理员密码为111111 后续通过修改密码修改
          email: item.email
            }).then(data => {
              if(data.status==200) {
                let itemClone = JSON.parse(JSON.stringify(item));
                itemClone.id = this.form.datas.length;
                itemClone.action = "view";
                this.form.datas.push(itemClone);
                this.resetField('form', index);
                this.$message.success('添加成功');
              }
            }).catch(err => {
              this.$message.error(err.response.data.message)
            })
        let itemClone = JSON.parse(JSON.stringify(item));
        itemClone.id = this.form.datas.length;
        itemClone.action = "view";
        this.form.datas.push(itemClone);
        this.resetField('form',index);
      },

      //新增-重置操作
      click_reset(item,index) {
        this.resetField('form',index);
      },

      //删除操作 还没有删除的接口
      click_delete(item,index) {
        this.$confirm("确定删除该条数据(ID" + item.id + ")吗?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
            .then(() => {
              //模拟删除一条数据
              this.form.datas.splice(index,1);
            })
            .catch(() => {});
      },

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
</style>
