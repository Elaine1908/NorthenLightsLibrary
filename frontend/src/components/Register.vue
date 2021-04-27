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
        label="邮箱"
        prop="email">
      <el-input
          type="email"
          v-model="ruleForm.email"
          autocomplete="off"
          placeholder="邮箱"
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
      ruleForm: {
        passWord: '',
        checkPass: '',
        username: '',
        email: ''
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
        email: [
          {validator: validEmail, trigger: 'blur', required: true},
        ]
      },
      emailValue: '123'
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
            email: this.ruleForm.email
          }).then(data => {
            if (data.status === 200) {
              this.$router.push({
                path: '/login'
              });
            }
            console.log(data.data.message)
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
      this.ruleForm.email = this.ruleForm.username === '' ? '' : (this.ruleForm.username + '@fudan.edu.cn')
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