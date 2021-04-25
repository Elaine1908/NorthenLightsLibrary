<template>
  <div class="container">
    <div style="height: 30px">
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
    <el-container>
      <el-container style="border: 1px solid #eee">
        <el-container>
          <el-main>
            <DisplayBox></DisplayBox>
          </el-main>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<script>
  import DisplayBox from "@/components/DisplayBox";
  export default {
    name: "Show",
    components:{
      DisplayBox
    },
    // 搜索还没有写
    data() {
      return {
        formInline: {
          bookName: '',
          author: '',
          isbn:''
        }
      }
    },
    methods: {
      onSubmit() {
        console.log('submit!');
      }
    }
  }
</script>

<style scoped>

</style>
