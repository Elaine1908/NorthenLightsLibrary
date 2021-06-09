<template>
  <div>
    <div class="bookInfosBox">
      <div class="bookImage">
        <img :src="imagePath">
      </div>
      <div class="bookInfos">
        <div class="infoItem">
          <h1>{{name}}</h1>
        </div>
        <div class="infoItem">
          <span>作者:{{author}}</span>
          <span>ISBN:{{isbn}}</span>
          <div style="clear:both;float: left;margin-bottom: 25px">
            <el-rate
                    v-model="averageRate"
                    disabled
                    show-score
                    text-color="#ff9900">
            </el-rate>
          </div>
        </div>

        <div class="description">
          <p>{{description}}</p>
        </div>
      </div>
    </div>

    <div class="bookContentBox">
      <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect" :router="true">
        <el-menu-item index="/home/showDetails/showComment">评论</el-menu-item>
        <el-menu-item index="/home/showDetails/showCopy">副本情况</el-menu-item>
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
        imagePath:"http://source.unsplash.com/random",
        activeIndex: '/home/showDetails/showComment',
      };
    },
    created() {
      this.axios.get('/useradmin/getBookTypeAndCopy',{
        params: {
          isbn: this.$route.query.isbn
        }
      }).then(resp => {
        if (resp.status === 200){
          this.name = resp.data.name;
          this.author = resp.data.author;
          this.isbn = resp.data.isbn;
          this.averageRate = resp.data.averageRate;
          this.description=resp.data.description
        }
      }).catch(err => {
        this.$message.error(err.response.data.message)
      })
    },
    methods: {
      handleSelect(key, keyPath) {
        console.log(key, keyPath);
      }
    }
  }
</script>

<style scoped>

.bookInfosBox{
  height: 385px;
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
