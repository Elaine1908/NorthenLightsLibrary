<template>
  <div class="container">
    <el-container>
      <el-container style="border: 1px solid #eee">
        <el-container>
          <el-main>
            <div class="search_bar" style="margin-bottom: 60px">
              <div style="position: absolute">
                <el-page-header @back="showAll" v-if="showBack">
                </el-page-header>
              </div>
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
                  <el-button type="primary" @click="search">查询</el-button>
                </el-form-item>
              </el-form>
            </div>
            <div class="masonry">
              <div class="item" v-for="(o, index) in bookList">
                <el-card :body-style="{ padding: '0px' }" style="margin-bottom: 60px">
                  <img :src="o.imagePathToFrontEnd" class="image">
                  <div style="padding: 14px;">
                    <h2 style="margin-bottom: 5px;line-height: 20px;font-size: 20px;text-align: center">{{o.name}}</h2>
                    <h3 style="margin-bottom: 5px;line-height: 16px;font-size: 15px;text-align: center">作者:{{o.author}}</h3>
                    <div class="bottom clearfix">
                      <el-rate
                              style="text-align: center"
                              v-model="o.averageRate"
                              disabled
                              text-color="#ff9900">
                      </el-rate>
                    </div>
                    <el-button type="text" class="button" @click="showDetails(o.isbn)" v-if="roleShow === 'undergraduate' || roleShow === 'postgraduate' || roleShow === 'teacher'">预约</el-button>
                    <el-button type="text" class="button" @click="showDetails(o.isbn)" v-else>查看详情</el-button>
                  </div>
                </el-card>
              </div>
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
        bookList:[{
          name: 'hhh',
          isbn: '111-222-444'
        }],
        showBack:false,
        currentDate: new Date(),
        roleShow:localStorage.getItem('role')
      }
    },
    created() {
      this.showAll();
    },
    methods: {
      search() {
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
            this.showBack=true;
            this.$message.success(resp.data.message)
          }
        }).then(err => {
          this.$message.error(err.response.data.message)
        })
      },
      showDetails(isbn){
        this.$router.push({path: '/home/showDetails', query: {isbn: isbn}});
      },
      showAll(){
        this.axios.get('/useradmin/getAllBookType').then(resp => {
          if (resp.status === 200){
            this.bookList=resp.data.bookTypeList;
            for (let i = 0; i < this.bookList.length; i++) {
              this.bookList[i].averageRate /= 2
            }
            this.showBack=false;
          }
        }).catch(err => {
          this.$message.error(err.response.data.message)
        })
      }
    }
  }
</script>

<style scoped>
  .search_bar {
    text-align: center;
    width: 100%;
    height: 40px;
    margin-bottom: 30px;
  }
  .masonry {
    width: 100%;
    margin: 20px auto;
    columns: 3;
    column-gap: 30px;
  }
  .item {
    width: 80%;
    break-inside: avoid;
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
    margin-top: 10px;
    margin-bottom: 20px;
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
