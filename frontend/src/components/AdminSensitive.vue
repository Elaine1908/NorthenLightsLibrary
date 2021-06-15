<template>
<div>
  <el-row v-for="(itemR, indexR) in wordList">
    <el-col :span="itemC.span" v-for="(itemC, indexC) in itemR">
      <div class="word-display">
        <span>{{itemC.word}}</span>
        <span class="icon" @click="deleteWord(itemC, indexR, indexC)"><i class="el-icon-close"></i></span>
      </div>
    </el-col>
  </el-row>
  <el-form
      class="wordForm"
      ref="wordForm"
      :model="wordForm"
      :rules="rules"
      status-icon
      label-position="left"
      label-width="100px">
    <el-form-item
        v-for="(domain, index) in wordForm.domains"
        :label="'敏感词 ' + (index + 1)"
        :prop="'domains.' + index + '.value'"
        :rules="rules.sensitiveWords">
      <el-input v-model="domain.value" class="sensitive-input"></el-input>
      <span @click.prevent="removeDomain(domain)" class="icon"><i class="el-icon-delete"></i></span>
    </el-form-item>
    <el-form-item>
      <el-button @click="addDomain">新增词汇</el-button>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm">立即添加</el-button>
    </el-form-item>
  </el-form>
</div>
</template>

<script>
  export default {
    name: "AdminSensitive",
    data() {
      let validateWord = (rule, value, callback) => {
        if (value.length > 14) {
          callback(new Error('敏感词不能超过14个字'))
        } else if (value === '') {
          callback(new Error('敏感词不能为空'))
        } else {
          callback()
        }
      }
      return {
        wordList: [
            [{
          word: '傻逼',
              span: 3
            }, {
          word: 'nmsl',
              span: 3
            }, {
          word: '你有病你有病你有',
              span: 4
            },{
              word: '傻逼',
              span: 3
            }, {
              word: 'nmsl',
              span: 3
            }, {
              word: '你有病',
              span: 3
            }, {
              word: '你有病',
              span: 3
            }],
        [{
          word: '神经病',
          span: 3
        },{
          word: '甘霖娘你有病你有病你有',
          span: 5
        }],
        [{
          word: '甘霖娘你有病你有病你有你有病',
          span: 6
        }]],
        wordForm: {
          domains: [{
            value: ''
          }]
        },
        rules: {
          sensitiveWords: [
            {validator: validateWord, required: true, trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      deleteWord(itemC, indexR, indexC) {
        let tmp = itemC.word
        let list = []
        list.push(tmp)
        this.$axios.post('/admin/removeFromSensitive', {
          removeFromSensitiveList: list
        }).then(resp => {
          this.$message.success(resp.data.message)
          this.wordList[indexR].splice(indexC, 1)
        }).catch(err => {
          if (err.response.data.message) {
            this.$message.error(err.response.data.message)
          }
          else {
            this.$message.error('没有删除成功')
            //this.$router.push({path: '/home/show'})
          }
        })
      },
      removeDomain(item) {
        let index = this.wordForm.domains.indexOf(item)
        if (index !== -1) {
          this.wordForm.domains.splice(index, 1)
        }
      },
      addDomain() {
        this.wordForm.domains.push({
          value: ''
        });
      },
      submitForm() {
        this.$refs.wordForm.validate(valid => {
          console.log(valid)
          if (valid) {
            let list = new Array(this.wordForm.domains.length)
            for (let i = 0; i < list.length; i++) {
              list[i] = this.wordForm.domains[i].value
            }
            this.$axios.post('/admin/addToSensitive', {
              addToSensitiveList: list
            }).then(resp => {
              this.$router.go(0)
            }).catch(err => {
              if (err.response.data.message) {
                this.$message.error(err.response.data.message)
              }
              else {
                this.$message.error('添加失败')
                //this.$router.push({path: '/home/show'})
              }
            })
          }
          else {
            this.$message.error('请填写完整所有内容')
          }
        })
      }
    },
    created() {
      this.$axios('/admin/sensitiveWordList')
      .then(resp => {
        this.wordList = [[]]
        let list = resp.data.sensitiveWordList
        let index = 0;
        let acc = 0;
        for (let i = 0; i < list.length; i++) {
          if (acc >= 20) {
            this.wordList.push([])
            index++
            acc = 0
          }
          if (list[i].length <= 5) {
            this.wordList[index].push({
              word: list[i],
              span: 3
            })
            acc += 3
          } else if (list[i].length <= 8) {
            this.wordList[index].push({
              word: list[i],
              span: 4
            })
            acc += 4
          } else if (list[i].length <= 11) {
            this.wordList[index].push({
              word: list[i],
              span: 5
            })
            acc += 5
          } else if (list[i].length <= 14) {
            this.wordList[index].push({
              word: list[i],
              span: 6
            })
            acc += 6
          } else {
            this.$message.error('“' + list[i] + '”' + '太长了，不适合作为敏感词汇')
          }
        }
      }).catch(err => {
        if (err.response.data.message) {
          this.$message.error(err.response.data.message)
        }
        else {
          this.$message.error('网络不稳定')
          //this.$router.push({path: '/home/show'})
        }
      })
    }
  }
</script>

<style scoped>
.word-display {
  text-align: center;
  border: 1px solid lightgray;
  border-radius: 4px;
  margin: 10px;
  padding: 10px 0;
  box-shadow: 0 0 3px lightgrey;
  font-weight: bold;
}
.icon {
  padding-top: 8px;
  margin-left: 5px;
  font-size: larger;
}
.icon:hover {
  color: #409EFF;
  cursor: pointer;
}
.sensitive-input {
  width: 300px;
}
.wordForm {
  margin-top: 50px;
}
</style>
