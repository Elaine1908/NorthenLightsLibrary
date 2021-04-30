<template>
  <div>
    <el-form
        class="fetch-form"
        id="form"
        ref="form"
        :model="form"
        status-icon
        label-position="left"
        label-width="100px">
      <el-form-item
          label="读者学/工号" prop="username"
          :rules="{required: true, message: '请输入你的学/工号', trigger: 'blur'}">
        <el-input v-model="form.username"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submit">立即查询</el-button>
      </el-form-item>
    </el-form>
    <el-table
        ref="fetchTable"
        :data="tableData"
        style="width: 100%"
        v-if="showTable"
        @selection-change="handleSelectionChange">
      <el-table-column type="selection"></el-table-column>
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-row>
              <el-col span="13">
                <el-row>
                  <el-form-item label="书籍名称">
                    <span>{{ props.row.name }}</span>
                  </el-form-item>
                </el-row>
                <el-row>
                  <el-form-item label="作者名称">
                    <span>{{ props.row.author }}</span>
                  </el-form-item>
                </el-row>
                <el-row>
                  <el-form-item label="ISBN">
                    <span>{{ props.row.uniqueBookMark }}</span>
                  </el-form-item>
                </el-row>
              </el-col>
              <el-col span="8">
                <el-form-item>
                  <div class="box">
                    <a href="#">
                      <el-image :src="props.row.imagePath"
                                style="border-radius: 4px"
                                class="scrollLoading"
                                lazy
                      ></el-image>
                    </a>
                  </div>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column
          label="书籍名称"
          prop="name">
      </el-table-column>
      <el-table-column
          label="作者名称"
          prop="author">
      </el-table-column>
      <el-table-column
          label="借书时间"
          prop="borrowDate">
      </el-table-column>
    </el-table>
    <el-form-item>
      <el-button type="primary" v-if="showTable" @click="submitReservation">提交预约</el-button>
    </el-form-item>
  </div>
</template>

<script>
export default {
  name: "FetchBooks",
  data() {
    return {
      submittedUsername: '',
      showTable: false,
      form: {
        username: ''
      },
      tableData: [],
      multipleSelection: []
    }
  },
  methods: {
    submit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$axios('/admin/showReservation', {
            params: {
              username: this.form.username
            }
          }).then(data => {
            this.showTable = true
            this.tableData = data.data.reservedBooks;
            this.submittedUsername = this.form.username
          }).catch(err => {
            this.$message.error(err.response.data.message)
          })
        }
        else {
          this.$message.error('请输入读者学/工号')
        }
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    submitReservation() {
      if (this.multipleSelection === []) {
        this.$message.error('请至少选择一本书籍再提交')
      } else {
        let reservationList = new Array(this.multipleSelection.length);
        for (let i = 0; i < reservationList.length; i++) {
          reservationList[i] = this.multipleSelection.uniqueBookMark;
        }
        this.$axios.post('/admin/lendReservedBookToUser', {
          username: this.submittedUsername,
          uniqueBookMarkList: reservationList
        }).then(data => {
          this.$router.go(0);
          this.$message.success(data.data.message)
        }).catch(err => {
          this.$message.error(err.response.data.message)
        })
      }
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
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
.fetch-form {
  width: 40%;
  text-align: left;
}
</style>
