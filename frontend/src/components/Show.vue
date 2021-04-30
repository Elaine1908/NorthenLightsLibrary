<template>
  <div class="container">
    <el-container>
      <el-container style="border: 1px solid #eee">
        <el-container>
          <el-main>
            <div class="search_bar">
              <el-form :inline="true" :model="formInline" class="demo-form-inline">
                <el-form-item>
                  <el-input v-model="formInline.bookName" placeholder="书名"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-input v-model="formInline.author" placeholder="作者名"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-input v-model="formInline.isbn" placeholder="ISBN"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="onSubmit">查询</el-button>
                </el-form-item>
              </el-form>
            </div>
            <div>
              <el-row>
                <el-col :span="6" v-for="(o, index) in bookList" :offset="index > 0 ? 2 : 0">
                  <el-card :body-style="{ padding: '0px' }">
                    <img :src="o.imagePathToFrontEnd" class="image">
                    <div style="padding: 14px;">
                      <h2 style="margin-bottom: 5px;line-height: 20px">{{o.name}}</h2>
                      <h3 style="margin-bottom: 5px;line-height: 15px">{{o.author}}</h3>
                      <p style="margin-bottom: 20px;line-height: 10px">{{o.description}}</p>
                      <el-button type="text" class="button" @click="showCopy(o.isbn)">预约</el-button>
                      <div class="bottom clearfix">
                        <time class="time">{{ currentDate }}</time>
                      </div>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </div>
          </el-main>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<script>

  export default {
    name: "Show",
    data() {
      return {
        formInline: {
          bookName: '',
          author: '',
          isbn:''
        },
        bookList:[],
        currentDate: new Date(),
      }
    },
    created() {
      this.axios.get('/useradmin/getAllBookType').then(resp => {
        if (resp.status === 200){
          this.bookList=resp.data.bookTypeList;
        } else {
          this.$message(resp.data.message);
        }
      }).catch(err => {
        this.$message.error(err.response.data.message)
      })
    },
    methods: {
      onSubmit() {
        this.axios.get('/useradmin/getBookType',
            {
              params:{
                isbn:this.formInline.isbn,
                author:this.formInline.author,
                bookName:this.formInline.bookName
              }
            }).then(resp => {
          if (resp.status === 200) {
            this.bookList = resp.data.bookTypeList;
            this.$message.success(resp.data.message)
          }
        }).then(err => {
          this.$message.error(err.response.data.message)
        })
      },
      showCopy(isbn){
        if(localStorage.getItem('login')) {
          this.$router.push({path: '/home/showCopy', query: {isbn: isbn}});
          if (localStorage.getItem('role') !== 'student') {
            this.$message.error('您不是读者，无法预约')
          }
        }else {
          this.$message.error("请先登录");
        }
      }
    }
  }
</script>

<style scoped>
  .search_bar {
    width: 100%;
    height: 40px;
    margin-bottom: 30px;
  }
  .time {
    font-size: 13px;
    color: #999;
  }

  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .image {
    width: 100%;
    display: block;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }
  .button:hover{
    cursor: pointer;
  }
</style>
