<template>
  <el-table
          stripe
          :data="tableData.filter(data => !search || data.name.toLowerCase().includes(search.toLowerCase()))"
          style="width: 100%">
    <el-table-column
            label="Name"
            prop="name">
    </el-table-column>
    <el-table-column
            label="Email"
            prop="email">
    </el-table-column>
    <el-table-column
            label="Identity"
            prop="identity">
    </el-table-column>
    <el-table-column
            align="right">
      <template slot="header" slot-scope="scope">
        <el-input
                v-model="search"
                size="mini"
                placeholder="输入关键字搜索"/>
      </template>
      <template slot-scope="scope">
        <el-button
                size="mini"
                @click="handleEdit(scope.$index, scope.row)">Edit</el-button>
        <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">Delete</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
export default {
  name: "AddAdmin",
  data() {
    return {
      tableData: [{
        name: '王小虎',
        email: 'www',
        identity: '超级管理员'
      }, {
        name: '王小虎',
        email: 'www',
        identity: '超级管理员'
      }, {
        name: '王小虎',
        email: 'www',
        identity: '超级管理员'
      }, {
        name: '王小虎',
        email: 'www',
        identity: '超级管理员'
      }],
      search: ''
    }
  },
  methods: {
    handleEdit(index, row) {
      console.log(index, row);
    },
    handleDelete(index, row) {
      console.log(index, row);
    }
  },
}
</script>

<style scoped>

</style>
