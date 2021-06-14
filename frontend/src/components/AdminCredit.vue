<template>
  <div>
    <el-form
            ref="form"
            class="copy-form"
            :model="creditData"
            :rules="rules"
            status-icon
            label-position="left"
            label-width="100px">
      <el-form-item label="用户名" prop="username" style="width: 280px">
        <el-input v-model="creditData.username"></el-input>
      </el-form-item>
      <el-form-item label="信用分" prop="to" style="width: 280px">
        <el-input v-model="creditData.to"></el-input>
      </el-form-item>
      <el-form-item style="float: left;margin-top: 15px">
        <el-button type="primary" @click="reset">重置信用</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  export default {
    name: "AdminCredit",
    data() {
      return {
        creditData: {
          username: '',
          to: ''
        },
        rules: {
          username: [
            {required: true, message: '请填写用户名', trigger: 'blur'}
          ],
          to: [
            {required: true, message: '请填写重置的信用分', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      reset() {
        this.$refs.form.validate(valid => {
          if (valid) {
            this.axios.post('/superadmin/resetCredit', {
                username: this.creditData.username,
                to: this.creditData.to
            }).then(resp => {
              if (resp.status === 200) {
                this.$message.success("重置成功");
              }
            }).catch(err => {
              this.$message.error("重置失败")
            })
          } else {
            this.$message.error('请填写完整所有内容');
          }
        })
      }
    }
  }
</script>

<style scoped>

</style>
