<template>
  <el-form
      class="borrow-form"
      ref="form"
      :model="form"
      status-icon
      label-position="left"
      label-width="100px">
    <el-form-item label="ISBN" prop="isbn">
      <el-input v-model="form.isbn" class="return-input"></el-input>
    </el-form-item>
    <el-form-item label="读者学/工号" prop="username">
      <el-input v-model="form.username" class="return-input"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm">立即借书</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  name: "BorrowBooks",
  data() {
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
      form: {
        isbn: '',
        username: ''
      },
      rules: {
        username: [
          {validator: validUsername, trigger: 'blur', required: true},
        ],
        isbn: [
          {required: true, message: '请输入ISBN', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$axios.post('/admin/lendBookToUser', {
            uniqueBookMark: this.form.isbn,
            username: this.form.username
          }).then(data => {
            this.$router.go(0)
            this.$message.success(data.data.message)
          }).catch(err => {
            this.$message.error(err.response.data.message)
          })
        }
        else {
          this.$message('请填写完整所有内容')
        }
      })
    }
  },
  mounted() {
    if (!localStorage.getItem('login')) {
      this.$message.error('请先登录')
      this.$router.push('/login')
    } else if (localStorage.getItem('role') !== 'admin' && localStorage.getItem('role') !== 'superadmin') {
      this.$message.error('您不是管理员，无法访问该页面')
      this.$router.push('/login')
    } else if (parseInt(localStorage.getItem('exp')) < ((new Date().getTime())/1000)) {
      this.$message.error('登录过期，请先登录')
      this.$router.push('/login')
    }
  }
}
</script>

<style>
.borrow-form {
  text-align: left;
  width: 90%;
}
.return-input {
  width: 300px;
}
.icon {
  margin-left: 20px;
  font-size: larger;
}
.icon:hover {
  color: #409EFF;
  cursor: pointer;
}
</style>