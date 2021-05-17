<template>
  <el-form
          class="return-form"
          id="form"
          ref="form"
          :model="form"
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
      <el-button @click="submitForm" type="primary">立即还书</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
  export default {
    name: "ReturnBooks",
    data() {
      return {
        form: {
          domains: [{
            value: ''
          }]
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
            this.$axios.post('/admin/receiveBookFromUser', {
              uniqueBookMarkList: isbnList
            }).then(data => {
              this.$message.info(data.data.message)
              this.$refs.form.resetFields()
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
      } else if (localStorage.getItem('role') !== 'admin' && localStorage.getItem('role') !== 'superadmin') {
        this.$message.error('您不是管理员，无法访问该页面')
        this.$router.push('/home/show')
      } else if (parseInt(localStorage.getItem('exp')) < ((new Date().getTime())/1000)) {
        this.$message.error('登录过期，请先登录')
        this.$router.push('/login')
      }
    }
  }
</script>

<style scoped>
  .return-form {
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
