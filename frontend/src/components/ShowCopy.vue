<template>
  <div>
    <el-table
            ref="multipleTable"
            :data="tableData"
            tooltip-effect="dark"
            style="width: 100%"
            @selection-change="handleSelectionChange">
      <el-table-column
              type="selection"
              width="55">
      </el-table-column>
      <el-table-column
              label="书名"
              width="120">
        <template slot-scope="scope">{{ scope.row.name }}</template>
      </el-table-column>
      <el-table-column
              prop="author"
              label="作者"
              width="120">
      </el-table-column>
      <el-table-column
              prop="libraryName"
              label="所在分馆"
              width="120">
      </el-table-column>
      <el-table-column
              prop="status"
              label="状态"
              width="120">
      </el-table-column>
      <el-table-column
              prop="uniqueBookMark"
              label="ISBN"
              show-overflow-tooltip>
      </el-table-column>
    </el-table>
    <div style="margin-top: 20px">
      <el-button @click="toggleSelection()">取消选择</el-button>
      <el-button @click="">预约</el-button>
    </div>
  </div>
</template>

<script>
  export default {
    name: "ShowCopy",
    data() {
      return {
        tableData: [{
          uniqueBookMark: '2016-05-03',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        },
          {
            uniqueBookMark: '2016-05-03',
            name: '王小虎',
            address: '上海市普陀区金沙江路 1518 弄'
          }
        ],
        multipleSelection: []
      }
    },
    methods: {
      toggleSelection(rows) {
        if (rows) {
          rows.forEach(row => {
            this.$refs.multipleTable.toggleRowSelection(row);
          });
        } else {
          this.$refs.multipleTable.clearSelection();
        }
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      }
    }
  }
</script>

<style scoped>

</style>
