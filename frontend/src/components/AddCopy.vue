<template>
<el-form
  ref="form"
  class="copy-form"
  :model="form"
  :rules="rules"
  status-icon
  label-position="left"
  label-width="100px">
  <el-form-item label="ISBN" prop="isbn">
    <el-input v-model="form.isbn"></el-input>
  </el-form-item>
  <el-form-item label="校区" prop="libraryID" placeholder="请选择校区">
    <el-select v-model="form.libraryID">
      <el-option label="邯郸" value="1"></el-option>
      <el-option label="枫林" value="2"></el-option>
      <el-option label="江湾" value="4"></el-option>
      <el-option label="张江" value="3"></el-option>
    </el-select>
  </el-form-item>
  <el-form-item label="副本数量" prop="number">
    <el-input-number v-model="form.number"></el-input-number>
  </el-form-item>
  <el-form-item>
    <el-button type="primary" @click="submitForm">添加副本</el-button>
  </el-form-item>
</el-form>
</template>

<script>
export default {
  name: "AddCopy",
  data() {
    return {
      form: {
        isbn: '',
        libraryID: '',
        number: ''
      },
      rules: {
        isbn: [
          {required: true, message: '请填写ISBN', trigger: 'blur'}
        ],
        libraryID: [
          {required: true, message: '请填写校区', trigger: 'blur'}
        ],
        number: [
          {required: true, message: '请填写副本数量', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$axios.post('/admin/addBookCopy', {
            isbn: this.form.isbn,
            libraryID: this.form.libraryID,
            number: this.form.number
          }).then(data => {
            if (data.status === 200) {
              this.$router.go(0)
              this.$message.success(data.data.message)
            }
          }).catch(err => {
            this.$message.error(err.response.data.message)
          })
        }
        else {
          this.$message.error('请填写完整所有内容')
        }
      })
    }
  },
  mounted() {
    if (!localStorage.getItem('login')) {
      this.$message.error('请先登录')
      this.$router.push('/login')
    }
    if (localStorage.getItem('role') !== 'admin' && localStorage.getItem('role') !== 'superadmin') {
      this.$message.error('您不是管理员，无法访问该页面')
      this.$router.push('/login')
    }
    if (parseInt(localStorage.getItem('exp')) < ((new Date().getTime())/1000)) {
      this.$message.error('登录过期，请先登录')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.copy-form {
  text-align: left;
  width: 400px;
}
</style>
