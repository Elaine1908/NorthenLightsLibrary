<template>
  <div class="container">
    <el-container>
      <el-container style="border: 1px solid #eee">
        <el-aside width="200px">
          <div style="position: fixed;left:70px;">
            <el-form>
              <el-input v-model="input" placeholder="You can search here" style="width: 80%"></el-input>
            </el-form>
          </div>
        </el-aside>
        <el-container>
          <el-main>
            <DisplayBox></DisplayBox>
          </el-main>
          <el-footer>Copyright@2021 软件工程第20组. All Rights Reserved.</el-footer>
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
    data(){
      return{
        input:''
      }
    }
  }
</script>

<style scoped>
  .el-footer {
    background-color: #2c3e50;
    color: white;
    text-align: center;
    line-height: 60px;
  }

  .el-aside {
    background-color: #D3DCE6;
    color: #333;
    text-align: center;
    line-height: 200px;
    height: 100%;
  }

  .el-main {
    background-color: white;
    text-align: center;
    line-height: 160px;
  }

  body > .el-container {
    margin-bottom: 40px;
  }

</style>
