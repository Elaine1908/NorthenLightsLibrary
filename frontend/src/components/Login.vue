<template>
  <div id="login">
    <div class="loginToHome">
      <el-form
              ref="ruleForm"
              :model="ruleForm"
              :rules="rules"
              status-icon
              label-width="80px"
              class="loginForm"
      >
        <h3>登录</h3>
        <el-form-item
                label="用户名"
                prop="username"
        >
          <el-input
                  type="text"
                  v-model="ruleForm.username"
                  auto-complete="off"
                  placeholder="请输入用户名"
          ></el-input>
        </el-form-item>
        <el-form-item
                label="密码"
                prop="password"
        >
          <el-input
                  type="password"
                  v-model="ruleForm.password"
                  auto-complete="off"
                  placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
                  class="homeBut"
                  type="primary"
                  plain
                  @click="submitForm('ruleForm')"
                  :loading="logining"
          >登录</el-button>
          <el-button
                  class="loginBut"
                  type="primary"
                  plain
                  @click="resetForm('ruleForm')"
          >重置</el-button>
        </el-form-item>
        <span class="reminder">还没有账号？先<a @click="linkToRegister">注册</a></span>
      </el-form>

    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        logining: false,
        ruleForm: {
          username: '',
          password: ''
        },
        rules: {
          username: [
            { required: true, message: '请输入账号', trigger: 'blur' },
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
          ]
        }
      }
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$axios.post('/auth/login', {
              username: this.ruleForm.username,
              password: this.ruleForm.password
            })
                .then(resp => {
                  if (resp.status === 200) {
                    this.$router.replace({path: '/'});
                    //更新 vuex 的 state的值, 必须通过 mutations 提供的方法才可以
                    // 通过 commit('方法名') 就可以出发 mutations 中的指定方法
                    this.$store.commit("doLogin",this.ruleForm.username);
                  } else{
                    alert(resp.data.message);
                  }
                })
                .catch(error => {
                  alert(error.response.data.message)
                  console.log(error.response)
                })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      linkToRegister() {
        this.$router.push('/register')
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
  font-size: smaller;
  color: gray;
}
</style>
