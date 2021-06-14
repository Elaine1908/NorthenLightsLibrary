<template>
  <div>
    <a-list
        class="comment-list"
        item-layout="horizontal"
        :data-source="comments.commentList"
    >
      <a-list-item slot="renderItem" slot-scope="itemC, indexC">
        <a-comment :author="itemC.username" :avatar="itemC.avatar" v-if="(isAdmin || !itemC.deletedByAdmin)">
          <template slot="actions">
            <span @click="replyComment(itemC)">回复</span>
            <span @click="deleteComment(indexC)" v-if="!itemC.deletedByAdmin && (itemC.username === username || isAdmin)">删除评论</span>
          </template>
          <div slot="content" v-if="(!isAdmin || (isAdmin && !itemC.deletedByAdmin))">
            <p>
              <a-rate :default-value="itemC.rate" allow-half disabled></a-rate>
              <span style="margin-left: 10px;">{{ itemC.rate }}</span>
            </p>
            <p>
              {{ itemC.content }}
            </p>
          </div>
          <div slot="content" v-else>
            <p style="{color: black; font-weight: bolder;}">-------该评论已被删除-------</p>
            <p>
              <a-rate :default-value="itemC.rate" allow-half disabled></a-rate>
              <span style="margin-left: 10px;">{{ itemC.rate }}</span>
            </p>
            <p>
              {{ itemC.content }}
            </p>
          </div>
          <a-list
              class="comment-list"
              item-layout="horizontal"
              :data-source="itemC.replyList"
              v-if="itemC.replyList.length"
          >
            <a-list-item slot="renderItem" slot-scope="itemR, indexR">
              <a-comment :author="itemR.username + ' 回复 ' + itemR.repliedUsername" :avatar="itemR.avatar" v-if="(isAdmin || !itemC.deletedByAdmin)">
                <template slot="actions">
                  <span @click="replyReply(itemC, itemR)">回复</span>
                  <span @click="deleteReply(indexC, indexR)" v-if="!itemR.deletedByAdmin && (itemC.username === username || isAdmin)">删除回复</span>
                </template>
                <p slot="content" v-if="(!isAdmin || (isAdmin && !itemR.deletedByAdmin))">
                  {{ itemR.content }}
                </p>
                <div slot="content" v-else>
                  <p style="{color: black; font-weight: bolder;}">-------该回复已被删除-------</p>
                  <p>
                    {{ itemR.content }}
                  </p>
                </div>
                <a-tooltip slot="datetime">
                  <span>{{ itemR.time }}</span>
                </a-tooltip>
              </a-comment>
            </a-list-item>
          </a-list>
          <a-comment v-if="itemC.reply" :author="username">
            <a-avatar
                slot="avatar"
                src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
                alt="Han Solo"
            />
            <div slot="content">
              <a-form-item>
                <a-textarea :rows="4" :value="value" @change="handleChangeReply" />
              </a-form-item>
              <a-form-item>
                <a-button html-type="submit" type="primary" @click="handleSubmitReply(itemC)">
                  回复评论
                </a-button>
              </a-form-item>
            </div>
          </a-comment>
          <a-comment v-if="itemC.replyToReply" :author="username + ' 回复 ' + replyToReplyUsername">
            <a-avatar
                slot="avatar"
                src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
                alt="Han Solo"
            />
            <div slot="content">
              <a-form-item>
                <a-textarea :rows="4" :value="value" @change="handleChangeReplyReply" />
              </a-form-item>
              <a-form-item>
                <a-button html-type="submit" type="primary" @click="handleSubmitReplyReply(itemC)">
                  回复回复
                </a-button>
              </a-form-item>
            </div>
          </a-comment>
          <a-tooltip slot="datetime" :title="itemC.time">
            <span>{{ itemC.time }}</span>
          </a-tooltip>
        </a-comment>
      </a-list-item>
    </a-list>
    <a-comment :author="username">
      <a-avatar
          slot="avatar"
          src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
          alt="Han Solo"
      />
      <div slot="content">
        <a-form-item>
          <a-rate v-model="rate" allow-half></a-rate>
          <span style="margin-left: 10px;">{{ rate*2 }}</span>
        </a-form-item>
        <a-form-item>
          <a-textarea :rows="4" :value="value" @change="handleChange" />
        </a-form-item>
        <a-form-item>
          <a-button html-type="submit" type="primary" @click="handleSubmit">
            添加评论
          </a-button>
        </a-form-item>
      </div>
    </a-comment>
  </div>
</template>

<script>
//import moment from 'moment';
export default {
  name: "showComment",
  data() {
    return {
      //showReply: true,
      isAdmin: (localStorage.getItem('role') === 'admin') || (localStorage.getItem('role') === 'superadmin'),
      rate: 0,
      value: '',
      replyToCommentValue: '',
      replyToReplyValue: '',
      replyToReplyUsername: '',
      replyToReplyID: '',
      username: localStorage.getItem('username'),
      comments: {
        commentList:[
        {
          commentID: 0,
          deletedByAdmin: true,
          rate: 2.5,
          replyToReply: false,
          reply: false,
          username: 'Han Solo1',
          avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
          content:
              'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
          time: '1-11',
          replyList: [{
            replyID: 0,
            deletedByAdmin: false,
            repliedUsername: 'Han Solo1',
            username: 'Sherlock Watson',
            avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
            content:
                '111We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
          time: '1-11'
          }]
        },
        {
          commentID: 1,
          deletedByAdmin: false,
          rate: 2.8,
          replyToReply: false,
          reply: false,
          username: 'Han Solo2',
          avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
          content:
              'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
          time: '1-11',
          replyList: [{
            replyID: 1,
            deletedByAdmin: false,
            repliedUsername: 'Han Solo2',
            username: 'Sam Winchester',
            avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
            content:
                '222We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
            time: '1-11'
          }]
        },
        {
          commentID: 2,
          deletedByAdmin: false,
          rate: 4.5,
          replyToReply: false,
          reply: false,
          username: 'Han Solo3',
          avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
          content:
              'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
          time: '1-11',
          replyList: [{
            replyID: 2,
            deletedByAdmin: false,
            repliedUsername: 'Han Solo3',
            username: 'Luke Skywalker',
            avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
            content:
                '333We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
            time: '1-11'
          }]
        },
      ]},
    };
  },
  created() {
    this.$axios('/useradmin/commentAndReply',{
      params: {
        isbn: this.$route.query.isbn
      }
    }).then(resp => {
      if (resp.status === 200){
        this.comments.commentList = resp.data.commentList;
        for (let i = 0; i < this.comments.commentList.length; i++) {
          this.comments.commentList[i].replyToReply = false
          this.comments.commentList[i].reply = false
          this.comments.commentList[i].avatar = 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png'
          this.comments.commentList[i].rate /= 2
          for (let j = 0; j < this.comments.commentList[i].replyList.length; j++) {
            this.comments.commentList[i].replyList[j].avatar = 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png'
          }
        }
      }
    }).catch(err => {
      if (err.response.data.message) {
        this.$message.error(err.response.data.message)
      }
      else {
        this.$message.error('没有接收到这本书本的相关信息')
        //this.$router.push({path: '/home/show'})
      }
    })
  },
  methods: {
    handleSubmit() {
      if (!this.value) {
        return;
      }
      this.$axios.post('/user/postComment', {
        isbn: this.$route.query.isbn,
        rate: (this.rate * 2),
        content: this.value
      }).then(resp => {
        this.$message.success('评论成功')
        /*this.comments.commentList.push({
          commentID: resp.data.message,
          deletedByAdmin: false,
          rate: rate,
          replyToReply: false,
          reply: false,
          username: localStorage.getItem('username'),
          avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
          content: this.value,
          time: '1-11',
          replyList: []
        })*/
        this.$router.go(0)
        this.value = '';
      }).catch(err => {
        if (err.response.data.message) {
          this.$message.error(err.response.data.message)
        }
        else {
          this.$message.error('没有成功评论')
        }
      })


    },
    handleChange(e) {
      this.value = e.target.value;
    },
    replyComment(itemC) {
      itemC.reply = !itemC.reply
      itemC.replyToReply = itemC.reply ? false : itemC.replyToReply
    },
    replyReply(itemC, itemR) {
      itemC.replyToReply = !itemC.replyToReply
      itemC.reply = itemC.replyToReply ? false : itemC.reply
      this.replyToReplyUsername = itemR.username
      this.replyToReplyID = itemR.replyID
    },
    deleteComment(indexC) {
      if (this.isAdmin) {
        this.$axios.post('/admin/deleteComment', {
          commentID: this.comments.commentList[indexC].commentID
        }).then(resp => {
          this.$message.success(resp.data.message)
          this.comments.commentList[indexC].deletedByAdmin = true
        }).catch(err => {
          if (err.response.data.message) {
            this.$message.error(err.response.data.message)
          }
          else {
            this.$message.error('没有成功删除这条评论！')
          }
        })
      } else {
        this.$axios.post('/user/deleteComment', {
          commentID: this.comments.commentList[indexC].commentID
        }).then(resp => {
          this.$message.success(resp.data.message)
          this.comments.commentList[indexC].deletedByAdmin = true
        }).catch(err => {
          if (err.response.data.message) {
            this.$message.error(err.response.data.message)
          }
          else {
            this.$message.error('没有成功删除这条评论！')
          }
        })
      }
    },
    deleteReply(indexC, indexR) {
      if (this.isAdmin) {
        this.$axios.post('/admin/deleteReply', {
          commentID: this.comments.commentList[indexC].replyList[indexR].replyID
        }).then(resp => {
          this.$message.success(resp.data.message)
          this.comments.commentList[indexC].replyList[indexR].deletedByAdmin = true
        }).catch(err => {
          if (err.response.data.message) {
            this.$message.error(err.response.data.message)
          }
          else {
            this.$message.error('没有成功删除这条回复！')
          }
        })
      } else {
        this.$axios.post('/user/deleteReply', {
          commentID: this.comments.commentList[indexC].replyList[indexR].replyID
        }).then(resp => {
          this.$message.success(resp.data.message)
          this.comments.commentList[indexC].replyList[indexR].deletedByAdmin = true
        }).catch(err => {
          if (err.response.data.message) {
            this.$message.error(err.response.data.message)
          }
          else {
            this.$message.error('没有成功删除这条回复！')
          }
        })
      }
    },
    handleSubmitReply(itemC) {
      if (!this.replyToCommentValue) {
        return;
      }

      this.$axios.post('/user/postReply', {
        commentID: itemC.commentID,
        content: this.replyToCommentValue
      }).then(resp => {
        this.$message.success('回复成功')
        /*itemC.replyList.push({
          replyID: resp.data.message,
          deletedByAdmin: false,
          repliedUsername: itemC.username,
          username: localStorage.getItem('username'),
          avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
          content: this.replyToCommentValue,
          time: '1-11'
        })*/
        this.$router.go(0)
        this.replyToCommentValue = ''
      }).catch(err => {
        if (err.response.data.message) {
          this.$message.error(err.response.data.message)
        }
        else {
          this.$message.error('没有成功删除这条回复！')
        }
      })

    },
    handleChangeReply(e) {
      this.replyToCommentValue = e.target.value;
    },
    handleChangeReplyReply(e) {
      this.replyToReplyValue = e.target.value;
    },
    handleSubmitReplyReply(itemC) {
      if (!this.replyToReplyValue) {
        return;
      }

      /*itemC.replyList.push({
        deletedByAdmin: false,
        repliedUsername: this.replyToReplyUsername,
        username: localStorage.getItem('username'),
        avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
        content: this.replyToReplyValue,
        time: '1-11'
      })*/


      this.$axios.post('/user/postReply', {
        replyID: this.replyToReplyID,
        content: this.replyToReplyValue
      }).then(resp => {
        this.$message.success(resp.data.message)
        this.$router.go(0)
        this.replyToReplyUsername = ''
        this.replyToReplyID = ''
      }).catch(err => {
        if (err.response.data.message) {
          this.$message.error(err.response.data.message)
        }
        else {
          this.$message.error('没有成功删除这条回复！')
        }
      })
    },
  },
};
</script>

<style scoped>

</style>
