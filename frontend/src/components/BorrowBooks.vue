<template>
  <el-form
      class="form"
      ref="form"
      :model="form"
      :rules="rules"
      status-icon
      label-position="left"
      label-width="100px">
    <el-form-item
        v-for="(domain, index) in form.domains"
        :label="'ISBN ' + (index + 1)"
        :prop="'domains.' + index + '.value'"
        :rules="{required: true, message: 'ISBN不能为空', trigger: 'blur'}">
      <el-input v-model="domain.value" class="return-input"></el-input>
      <span @click.prevent="removeDomain(domain)" class="icon"><i class="el-icon-delete"></i></span>
    </el-form-item>
    <el-form-item>
      <el-button @click="addDomain">新增书本</el-button>
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
        domains: [{
          value: ''
        }],
        username: ''
      },
      rules: {
        username: [
          {validator: validUsername, trigger: 'blur', required: true},
        ]
      }
    }
  },
  methods: {
    removeDomain(item) {
      let index = this.form.domains.indexOf(item)
      if (index !== -1) {
        this.form.domains.splice(index, 1)
      }
    },
    addDomain() {
      this.form.domains.push({
        value: ''
      });
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          let isbnList = new Array(this.form.domains.length)
          for (let i = 0; i < isbnList.length; i++) {
            isbnList[i] = this.form.domains[i].value
          }
          this.$axios.post('/admin/lendBookToUser', {
            uniqueBookMarkList: isbnList,
            username: this.form.username
          }).then(data => {
            this.$message.info(data.data.message)
            this.$ref.form.resetFields()
          }).catch(err => {
            this.$message.error(err.response.data.message)
          })
        }
        else {
          this.$message('请填写完整所有内容')
        }
      })
    }
  }
}
</script>

<style>
.form {
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
