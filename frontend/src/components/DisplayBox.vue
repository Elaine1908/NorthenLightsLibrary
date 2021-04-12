<template>
  <div>
    <div class="box" v-for="item in bookList">
      <a href="#">
        <el-image :src="'data:image/'+item.contentType+';base64,'+item.content"
                  class="scrollLoading"
                  lazy
        ></el-image>
        <div class="info">
          <p class="title">{{ item.name }}</p>
          <p class="description">作者:{{ item.author }}</p>
          <p class="description">所在校区:{{ item.campusName }}</p>
          <p class="description">ISBN:{{ item.isbn }}</p>
          <p class="description">出版日期:{{ item.publicationDate }}</p>
        </div>
      </a>
    </div>
  </div>
</template>

<script>

export default {
  name: "DisplayBox",
  data() {
    return {
      bookList: []
    }
  },
  created() {//初始化操作
    const _this = this;
    _this.axios.get('/search/getall', {
      params: {//暂时未用到 还未做分页
        requestedPage: 0,
        pageSize: 9999
      }
    }).then(resp => {
      if (resp.status === 200) {
        _this.bookList = resp.data.bookList;
      } else {
        alert(resp.data.message)
      }
    })
  }
}
</script>

<style scoped>
.box {
  overflow: hidden;
  float: left;
  width: 150px;
  height: 210px;
  margin: 30px 50px 30px 50px;
  border-radius: 4px;
}

.box a {
  position: relative;
  display: inline-block;
  width: 150px;
  height: 210px;
}

.box a el-image {
  width: 100%;
}

.box a .info {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 210px;
  color: transparent;
}

.box a:hover .info {
  height: 210px;
  background: rgba(0, 0, 0, .4);
}

.box a .info .title {
  font-size: 15px;
  overflow: hidden;
  line-height: 45px;
  color: transparent;
  margin: 0 5px;
  word-break: break-all;
}

.box a:hover .info .title {
  line-height: 18px;
  padding-top: 10px;
  color: #fff;
}

.box a .info .description {
  font-size: 10px;
  margin-left: 5px;
}

.box a:hover .info .description {
  line-height: 18px;
  color: #99a2aa;
}
</style>
