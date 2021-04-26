<template>
  <el-form
    :model="modifyForm"
    status-icon :rules="rules"
    ref="modifyForm"
    label-width="80px"
    class="demo-ruleForm">
    <h3>修改密码</h3>
    <el-form-item
        label="旧密码"
        prop="oldPassword">
      <el-input
          type="password"
          v-model="modifyForm.oldPassword"
          auto-complete="off"
          placeholder="旧密码"></el-input>
    </el-form-item>
    <el-form-item
      label="新密码"
      prop="newPassword">
      <el-input
        type="password"
        v-model="modifyForm.newPassword"
        auto-complete="off"
        placeholder="新密码"></el-input>
    </el-form-item>
    <el-form-item
      label="确认密码"
      prop="newPasswordAgain">
        <el-input
          type="password"
          v-model="modifyForm.newPasswordAgain"
          auto-complete="off"
          placeholder="确认密码"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button
          type="primary"
          @click="submitForm()"
      >提交
      </el-button>
      <el-button
          @click="resetForm()"
      >重置
      </el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  name: "ModifyPassword",
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
      } else if (this.modifyForm.username !== '') {
        let patUserName = new RegExp(this.$store.state.username, 'i')
        if (patUserName.test(value)) {
          callback(new Error('密码不能包含用户名'))
        }
        callback()
      } else {
        if (this.modifyForm.newPasswordAgain !== '') {
          this.$refs.modifyForm.validateField('checkPass');
        }
        callback();
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.modifyForm.newPasswordAgain) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    }
    return {
      modifyForm: {
        newPassword: '',
        newPasswordAgain: '',
        oldPassword: ''
      },
      rules: {
        oldPassword: [
          {required: true, message: '请输入原来的密码', trigger: 'blur'}
        ],
        newPassword: [
          {validator: validatePass, trigger: 'blur', required: true}
        ],
        newPasswordAgain: [
          {validator: validatePass2, trigger: 'blur', required: true}
        ]
      }
    }
  },
  methods: {
    submitForm() {
      this.$refs.modifyForm.validate((valid) => {
        if (valid) {
          this.$axios.post('/auth/changePassword', {
            oldPassword: this.modifyForm.oldPassword,
            newPassword: this.modifyForm.newPassword,
            username: this.$store.state.username
          }).then(data => {
            if (data.status === 200) {
              this.$router.push({
                path: '/home'
                //后续可能会修改
              })
            }
            alert(data.data.message)
          }).catch(err => {
            alert(err.response.data.message)
          })
        } else {
          alert('请正确填写所有信息')
        }
      })
    },
    resetForm() {
      this.$refs.modifyForm.resetFields();
    }
  },
  mounted() {
    if (!this.$store.state.login) {
      alert('你没登录，不能修改密码')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>

</style>