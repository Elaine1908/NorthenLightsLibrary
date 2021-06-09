<template>
  <div>
    <div class="bookInfosBox">
      <div class="bookImage">
        <img src="http://source.unsplash.com/random" alt="图解机器学习算法">
      </div>
      <div class="bookInfos">
        <div class="infoItem">
          <h1>图解机器学习算法</h1>
        </div>
        <div class="infoItem">
          <span>作者:</span>
          <span>ISBN:</span>
        </div>
      </div>
      <el-button type="primary" style="float: left;margin-left: 50px">预约</el-button>
    </div>

    <div class="bookContentBox">
      <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
        <el-menu-item index="1">图书介绍</el-menu-item>
        <el-menu-item index="2">评论</el-menu-item>
      </el-menu>
    </div>
  </div>
</template>

<script>
  export default {
    name: "ShowDetails",
    data() {
      return {
        activeIndex: '1'
      };
    },
  }
</script>

<style scoped>

.bookInfosBox{
  height: 380px;
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
