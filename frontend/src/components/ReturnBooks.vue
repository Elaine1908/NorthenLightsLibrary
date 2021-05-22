<template>
  <el-form
      class="return-form"
      ref="returnForm"
      :model="returnForm"
      status-icon
      label-width="70px">
    <div v-for="(domain, index) in this.returnForm.domains" class="form-region">
      <el-form-item
          :label="'ISBN ' + (index + 1)"
          :prop="'domains.' + index + '.uniqueBookMark'"
          :rules="{required: true, message: 'ISBN不能为空', trigger: 'blur'}">
        <el-input v-model="domain.uniqueBookMark"></el-input>
      </el-form-item>
      <el-form-item
          :label="'状态 ' + (index + 1)"
          :prop="'domains.' + index + '.status'"
          :rules="{required: true, message: '状态不能为空', trigger: 'blur'}">
        <el-select v-model="domain.status" class="return-status">
          <el-option label="完好" value="ok"></el-option>
          <el-option label="损坏" value="damaged"></el-option>
          <el-option label="丢失" value="lost"></el-option>
        </el-select>
      </el-form-item>
      <span @click.prevent="removeDomain(domain)" class="icon"><i class="el-icon-delete"></i></span>
    </div>
    <el-form-item>
      <el-button @click="addDomain">新增书本</el-button>
      <el-button @click="submitForm" type="primary">现场还书</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
  export default {
    name: "ReturnBooks",
    data() {
      return {
        returnForm: {
          domains: [{
            uniqueBookMark: '',
            status: ''
          }]
        }
      }

    },
    methods: {
      removeDomain(item) {
        let index = this.returnForm.domains.indexOf(item)
        if (index !== -1) {
          this.returnForm.domains.splice(index, 1)
        }
      },
      addDomain() {
        this.returnForm.domains.push({
          uniqueBookMark: '',
          status: ''
        });
      },
      submitForm() {
        this.$refs.returnForm.validate(valid => {
          if (valid) {
            this.$axios.post('/admin/receiveBookFromUser', {
              uniqueBookMarkList: this.returnForm.domains
            }).then(resp => {
              this.$refs.returnForm.resetFields()
              for (let i = 0; i < resp.data.length; i++) {
                setTimeout(() => {
                  this.$message(resp.data[i])
                }, 100)
              }
            }).catch(err => {
              this.$message.error(err.response.data.message)
            })
          } else {
            this.$message.error("请正确填写表单")
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
  .icon {
    padding-top: 8px;
    margin-left: 20px;
    font-size: larger;
  }
  .icon:hover {
    color: #409EFF;
    cursor: pointer;
  }
  .return-status {
    width: 120px;
  }
  .form-region {
    display: flex;
  }
  .return-form {
    text-align: left;
  }
</style>
