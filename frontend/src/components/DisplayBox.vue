<template>
  <div>
    <el-row>
      <el-col :span="6" v-for="(o, index) in bookList" :key="o" :offset="index > 0 ? 2 : 0">
        <el-card :body-style="{ padding: '0px' }">
          <img :src="o.imagePathToFrontEnd" class="image">
          <div style="padding: 14px;">
            <h2 style="margin-bottom: 5px;line-height: 20px">{{o.name}}</h2>
            <h3 style="margin-bottom: 5px;line-height: 15px">{{o.author}}</h3>
            <p style="margin-bottom: 20px;line-height: 10px">{{o.description}}</p>
            <el-button type="text" class="button" @click="reserve">预约</el-button>
            <div class="bottom clearfix">
              <time class="time">{{ currentDate }}</time>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>

export default {
  name: "DisplayBox",
  data() {
    return {
      bookList: [{
        name:'hh',
        author:'w',
        description:'very good',
        imagePathToFrontEnd:'https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png'
      }
      ],
      currentDate: new Date(),
      }
  },
  methods:{
    reserve(){

    }
  },
  created() {//初始化操作
    // const _this = this;
    this.axios.get('/useradmin/getBookTypeAndCopy', {
      params: {//暂时未用到 还未做分页
        requestedPage: 0,
        pageSize: 9999
      }
    }).then(resp => {
      if (resp.status === 200) {
        this.bookList = resp.data;
        this.$message.success(resp.data.message)
      }
    }).then(err => {
      this.$message.error(err.response.data.message)
    })
  }
}
</script>

<style scoped>

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
