<template>
  <el-form
      :model="ruleForm"
      status-icon :rules="rules"
      ref="ruleForm"
      label-position="left"
      label-width="80px"
      class="demo-ruleForm"
  >
    <h3>注册</h3>
    <el-form-item
        label="用户名"
        prop="username">
      <el-input
          type="input"
          v-model="ruleForm.username"
          autocomplete="off"
          placeholder="学/工号"
          @change="autoFill()"
          required
      ></el-input>
    </el-form-item>
    <el-form-item
        label="密码"
        prop="passWord">
      <el-input
          type="password"
          v-model="ruleForm.passWord"
          autocomplete="off"
          required
      ></el-input>
    </el-form-item>
    <el-form-item
        label="确认密码"
        prop="checkPass">
      <el-input
          type="password"
          v-model="ruleForm.checkPass"
          autocomplete="off"
          required
      ></el-input>
    </el-form-item>
    <el-form-item
        label="选择身份"
        prop="identity">
      <el-radio-group v-model="ruleForm.identity">
        <el-radio label="teacher">教师</el-radio>
        <el-radio label="undergraduate">本科生</el-radio>
        <el-radio label="postgraduate">研究生</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item
        label="邮箱"
        prop="email">
      <span class="validate">
        <span class="validate-input">{{this.fillEmail}}</span>
        <el-button type="primary" plain @click="validateEmail" class="validate-button">验证邮箱</el-button>
      </span>
    </el-form-item>
    <el-form-item
        label="验证码"
        prop="captcha">
      <el-input v-model="ruleForm.captcha"></el-input>
    </el-form-item>
    <el-button
          type="primary"
          plain
          @click="submitForm('ruleForm')"
      >提交
      </el-button>
      <el-button
          type="primary"
          plain
          @click="resetForm('ruleForm')"
      >重置
      </el-button>
    <div class="reminder"><router-link to="/home/show">游客登录</router-link> | 已有账号？<router-link to="/login">登录</router-link></div>
  </el-form>
</template>

<script>


export default {
  name: "Register",
  data() {
    var validatePass = (rule, value, callback) => {
      let pat1 = /^[0-9]+$/;
      let pat2 = /^[a-z]+$/i;
      let pat3 = /^[-_]+$/;
      if (value === '') {
        callback(new Error('请输入密码'));
      } else if (value.length < 6 || value.length > 32) {
        callback(new Error('密码长度应为6-32个字符'))
      } else if (pat1.test(value) || pat2.test(value) || pat3.test(value)) {
        callback(new Error('字母数字或者特殊字符-_'))
      } else if (this.ruleForm.username !== '') {
        let patUserName = new RegExp(this.ruleForm.username, 'i')
        if (patUserName.test(value)) {
          callback(new Error('密码不能包含用户名'))
        }
        callback()
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.ruleForm.passWord) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    let validUsername = (rule, value, callback) => {
      let pat = /^\d{11}$/
      if (value === '') {
        callback(new Error('请填写学/工号'))
      }
      else if (!pat.test(value)) {
        callback(new Error('请注意学/工号的正确格式'))
      } else {
        callback();
      }
    }
    return {
      ruleForm: {
        passWord: '',
        checkPass: '',
        username: '',
        identity: '',
        captcha: ''
      },
      rules: {
        passWord: [
          {validator: validatePass, trigger: 'blur', required: true},
        ],
        checkPass: [
          {validator: validatePass2, trigger: 'blur', required: true},
        ],
        username: [
          {validator: validUsername, trigger: 'blur', required: true},
        ],
        identity: [
          {required: true, message: '请选择身份', trigger: 'change'}
        ],
        captcha: [
          {required: true, message: '请填写验证码', trigger: 'blur'}
        ]
      },
      fillEmail: ''
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post('/auth/register', {
            username: this.ruleForm.username,
            passWord: this.ruleForm.passWord,
            passWordAgain: this.ruleForm.checkPass,
            captcha: this.ruleForm.captcha,
            role: this.ruleForm.identity
          }).then(data => {
            if (data.status === 200) {
              this.$router.push({
                path: '/login'
              });
              this.$message.success(data.data.message)
            }
          }).catch(err => {
            this.$message.error(err.response.data.message)
          })
        } else {
          this.$message.error('请正确填写所有信息')
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    autoFill() {
      this.fillEmail = this.ruleForm.username === '' ? '' : (this.ruleForm.username + '@fudan.edu.cn')
    },
    validateEmail() {
      if (this.fillEmail !== "") {
        this.$axios.post('/auth/sendEmailCaptcha', {
          email: this.fillEmail
        }).then(resp => {
          this.$message.success(resp.data.message)
        }).catch(err => {
          this.$message.error(err.response.data.message)
        })
      } else {
        this.$message.error("请先填写用户名再验证学邮")
      }
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
.validate {
  display: flex;
}
.validate-input {
  width: 80%;
  border: solid 1px lightgray;
  border-radius: 4px;
  text-align: left;
  padding-left: 15px;
}
.validate-button {
  margin-left: 10px;
}
</style>