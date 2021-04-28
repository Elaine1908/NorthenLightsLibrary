<template>
  <el-table
          :data="tableData"
          style="width: 100%">
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
</template>

<script>
  export default {
    name: "UserBorrowed",
    data() {
      return {
        tableData: []
      }
    },
    created() {//初始化操作
      this.axios.get('/user/userinfo').then(resp => {
        if (resp.status === 200){
          this.tableData=resp.data.borrowedBooks;
        } else {
          this.$message(resp.data.message);
        }
      })
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

  .box {
    width: 150px;
    height: 210px;
  }

  .box a {
    width: 150px;
    height: 210px;
  }

  .box a el-image {
    width: 100%;
  }
</style>
