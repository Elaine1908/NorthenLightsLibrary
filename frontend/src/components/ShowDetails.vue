<template>
  <div>
    <div class="bookInfosBox">
      <div class="bookImage">
        <img :src="imagePathToFrontEnd">
      </div>
      <div class="bookInfos">
        <div class="infoItem">
          <h1>{{name}}</h1>
        </div>
        <div class="infoItem">
          <span>作者：{{author}}</span>
          <span>ISBN：{{isbn}}</span>
          <span>评分：{{averageRate * 2}}</span>
          <div style="clear:both;float: left;margin-bottom: 25px;">
            <el-rate
                v-model="averageRate"
                disabled
                style="{display: inline;}">
            </el-rate>
          </div>
        </div>

        <div class="description">
          <p>{{description}}</p>
        </div>
      </div>
    </div>

    <div class="bookContentBox">
      <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
        <el-menu-item index="1" @click="toComment">评论</el-menu-item>
        <el-menu-item index="2" @click="toCopy">副本情况</el-menu-item>
      </el-menu>
      <router-view/>
    </div>
  </div>
</template>

<script>
  export default {
    name: "ShowDetails",
    data() {
      return {
        name:"",
        author:"",
        isbn:"",
        averageRate:null,
        description:"",
        imagePathToFrontEnd:"",
        activeIndex: ''
      };
    },
    created() {
      if (!localStorage.getItem('login')) {
        this.$message.error('请先登录')
        this.$router.push({path: '/login'})
      }
      if (/^\/home\/showDetails\/showCopy\?isbn=(.)+/.test(this.$route.fullPath)) {
        this.activeIndex = '2'
      } else if (/^\/home\/showDetails\/showComment\?isbn=(.)+/.test(this.$route.fullPath)) {
        this.activeIndex = '1'
      } else {
        this.$router.push({path: '/home/show'})
        this.$message.error('没有获取到该书本的ISBN！')
      }
      this.axios.get('/useradmin/getBookTypeAndCopy',{
        params: {
          isbn: this.$route.query.isbn
        }
      }).then(resp => {
        if (resp.status === 200){
          this.name = resp.data.name;
          this.author = resp.data.author;
          this.isbn = resp.data.isbn;
          this.averageRate = resp.data.averageRate / 2;
          this.description=resp.data.description;
          this.imagePathToFrontEnd=resp.data.imagePathToFrontEnd;
        }
      }).catch(err => {
        if (err.response.data.message !== undefined) {
          this.$message.error(err.response.data.message)
        } else {
          this.$message.error('没有获取到对应的图书信息')
          //this.$router.push({path: '/home/show'})
        }
      })
    },
    methods: {
      handleSelect(key, keyPath) {
        console.log(key, keyPath)
      },
      toCopy(){
        let isbn = this.$route.query.isbn
        this.$router.push({path: '/home/showDetails/showCopy', query: {isbn: isbn}});
      },
      toComment() {
        let isbn = this.$route.query.isbn
        this.$router.push({path: '/home/showDetails/showComment', query: {isbn: isbn}});
      }
    },
    mounted: () => {
      //console.log('mounted函数：' + this.$route.fullPath)

    }
  }
</script>

<style scoped>
.bookInfosBox{
  min-height: 385px;
}
.bookImage{
  width:22%;
  margin-left: 20px;
  display: inline-block;
  float: left;
}
.bookInfos{
  width: 70%;
  display: inline-block;
}
.infoItem{
  margin-left: 20px;
  width: 100%;
  height: auto;
}
.description{
  float: left;
  width: 100%;
  margin-left: 20px;
  text-align: left
}
h1{
  float: left;
  font-size: 28px;
  line-height: 36px;
  color: #152844;
  font-weight: 600;
  margin-bottom: 60px;
}
span{
  clear: both;
  float: left;
  line-height: 20px;
  font-size: 16px;
  color: #787878;
  margin-bottom: 30px;
}
img {
  max-width: 100%;
  height: auto;
  display: block;
  margin: 0 auto;
  border: 1px solid #eee;
  border-radius: 4px;
}
</style>
