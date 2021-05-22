<template>
  <div id="login">
    <div class="loginToHome">
      <el-form
              ref="ruleForm"
              :model="ruleForm"
              :rules="rules"
              status-icon
              label-width="80px"
              class="loginForm">
        <h3>登录</h3>
        <el-form-item
                label="用户名"
                prop="username">
          <el-input
                  type="text"
                  v-model="ruleForm.username"
                  auto-complete="off"
                  placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item
                label="密码"
                prop="password"
        >
          <el-input
                  type="password"
                  v-model="ruleForm.password"
                  auto-complete="off"
                  placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="登陆身份" prop="identity">
          <el-radio-group v-model="ruleForm.identity">
            <el-radio label="admin">普通管理员</el-radio>
            <el-radio label="superadmin">超级管理员</el-radio>
            <el-radio label="reader">普通读者</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="this.ruleForm.identity === 'admin' || this.ruleForm.identity === 'superadmin'" label="所在分馆" prop="libraryID">
          <el-radio-group v-model="ruleForm.libraryID">
            <el-radio :label="1">邯郸</el-radio>
            <el-radio :label="2">枫林</el-radio>
            <el-radio :label="3">张江</el-radio>
            <el-radio :label="4">江湾</el-radio>
          </el-radio-group>
        </el-form-item>
          <el-button
                  class="homeBut"
                  type="primary"
                  plain
                  @click="submitForm('ruleForm')"
                  :loading="logining">登录</el-button>
          <el-button
                  class="loginBut"
                  type="primary"
                  plain
                  @click="resetForm('ruleForm')">重置</el-button>
        <div class="reminder"><router-link to="/home">游客登录</router-link> | 还没有账号？先<router-link to="/register">注册</router-link></div>
      </el-form>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      let validateLibraryID = (rule, value, callback) => {
        if ((this.ruleForm.identity === 'admin' || this.ruleForm.identity === 'superadmin') && value === 0) {
          callback(new Error('请选择所在分馆'))
        }
        callback()
      }
      return {
        logining: false,
        ruleForm: {
          username: '',
          password: '',
          identity: '',
          libraryID: 0
        },
        rules: {
          username: [
            { required: true, message: '请输入账号', trigger: 'blur' },
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
          ],
          identity: [
            { required: true, message: '请选择身份', trigger: 'blur' }
          ],
          libraryID: [
            { required: true, validator: validateLibraryID, trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      submitForm(formName) {
        /*localStorage.setItem('username', 'admin')
        localStorage.setItem('role', 'superadmin')
        localStorage.setItem('libraryID', '1')
        localStorage.setItem('login', 'true')*/
        let a = new Date().getTime() + 100000000;
        localStorage.setItem('exp', a.toString())
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let libraryID = (this.ruleForm.identity === 'student') ? 0 : this.ruleForm.libraryID;
            this.$axios.post('/auth/login', {
              username: this.ruleForm.username,
              password: this.ruleForm.password,
              libraryID: libraryID
            })
                .then(resp => {
                  if (resp.status === 200 && resp.headers.hasOwnProperty('token')) {
                    //更新 vuex 的 state的值, 必须通过 mutations 提供的方法才可以
                    // 通过 commit('方法名') 就可以出发 mutations 中的指定方法
                    if ((this.ruleForm.identity === 'reader' &&
                        (resp.data.message === 'graduate' || resp.data.message === 'postgraduate' ||
                            resp.data.message === 'teacher')) ||
                        this.ruleForm.identity === resp.data.message) {
                      this.$store.commit({
                        type: 'doLogin',
                        token: resp.headers.token
                      });
                      this.$router.push({path: '/home'});
                    } else {
                      this.$message.error('与所选身份不一致，请重新登陆')
                    }
                  }
                  else{
                    this.$message.success(resp.data.message);
                  }
                }).catch(error => {
                  this.$message.error(error.response.data.message)
                })
          } else {
            this.$message.error('请正确填写表单')
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    }
  }
</script>
<style>
a {
  text-decoration: none;
  color: lightskyblue;
  font-weight: bold;
  cursor: pointer;
}
.reminder {
  margin-top: 10px;
  font-size: smaller;
  color: gray;
}
</style>
