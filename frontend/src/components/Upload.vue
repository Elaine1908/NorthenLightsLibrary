<template>
  <el-form
      class="up-form"
      id="form"
      ref="form"
      :model="form"
      status-icon
      :rules="rules"
      label-position="left"
      label-width="100px">
    <el-form-item
        label="上传封面" prop="bookCover">
      <el-checkbox-group v-show="false" v-model="form.bookCover"></el-checkbox-group>
      <el-upload
          v-model="form.bookCover"
          v-if="isShowUpload"
          class="upload-demo"
          drag
          action="#"
          list-type="picture"
          :file-list="fileList"
          :auto-upload="false"
          :limit="1"
          :show-file-list="false"
          :on-change="imgSaveToUrl"
          :accept="'image/*'">
        <i class="el-icon-upload" style="color:#409EFF"></i>
        <div class="el-upload__text text">
          将图片拖到此处，或
          <em>点击上传</em>
        </div>
        <div
            class="el-upload__tip text"
            slot="tip">
          提示：可支持PNG、JPG、BMP，图片大小不超过4M,长边不大于4096像素，请保证识别的部分为图片的主题部分
        </div>
      </el-upload>
      <el-row v-if="isShowImgUpload" style="padding-left:10%;padding-right:10%;">
        <el-col :span="4" style="color:white">1</el-col>
        <el-col :span="16">
          <div style="position:relative;">
            <el-image
                :src="localUrl"
                :preview-src-list="[localUrl]"
                style="width:100%;height:auto;"
                fit="scale-down"
            ></el-image>
          </div>
          <div style="padding: 5px;">
            <el-button type="primary" :loading="false" size="small" @click="uploadButtonClick">重新上传</el-button>
          </div>
        </el-col>
        <el-col :span="4" style="color:white">1</el-col>
      </el-row>
    </el-form-item>
    <el-form-item label="书本名称" prop="name">
      <el-input v-model="form.name"></el-input>
    </el-form-item>
    <el-form-item label="ISBN" prop="isbn">
      <el-input v-model="form.isbn"></el-input>
    </el-form-item>
    <el-form-item label="出版时间" prop="publishDate">
      <el-date-picker
          v-model="form.publishDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择日期">
      </el-date-picker>
    </el-form-item>
    <el-form-item label="作者" prop="author">
      <el-input v-model="form.author"></el-input>
    </el-form-item>
    <el-form-item label="简介" prop="description">
      <el-input type="textarea" v-model="form.description"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit(form)">立即上传</el-button>
      <el-button>取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import {request} from '../plugins/axios.js'

export default {
  name: "Upload",
  data() {
    let validateCampusID = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请填写校区'))
      } else {
        callback()
      }
    }
    let validateBookName = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请填写书本名称'))
      } else {
        callback()
      }
    }
    let validateISBN = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请填写书本的ISBN编号'))
      }
      //不知道这个编号的格式是什么
      else {
        callback()
      }
    }
    let validatePublicationDate = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请填写出版日期'))
      } else {
        callback()
      }
    }
    let validateAuthor = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请填写作者'))
      } else {
        callback()
      }
    }
    return {
      dialogImageUrl: '',
      dialogVisible: false,
      disabled: false,
      isShowImgUpload: false,
      isShowUpload: true,
      fileList: [],
      localUrl: '',

      form: {
        bookCover: null,
        name: '',
        isbn: '',
        publishDate: '',
        author: '',
        description: ''
      },
      rules: {
        bookCover: [
          {required: true, message: '请上传图片', trigger: 'change', type: 'object'}
        ],
        name: [
          {required: true, validator: validateBookName, trigger: 'blur'},
        ],
        isbn: [
          {validator: validateISBN, trigger: 'blur', required: true},
        ],
        publishDate: [
          {validator: validatePublicationDate, trigger: 'blur', required: true},
        ],
        author: [
          {validator: validateAuthor, trigger: 'blur', required: true},
        ],
        description: [
          {required: true, message: '请填写简介', trigger: 'blur'}
        ]
      }
    };
  },
  methods: {
    imgSaveToUrl(event, fileList) {
      // 获取上传图片的本地URL，用于上传前的本地预览
      var URL = null;
      if (window.createObjectURL != undefined) {
        // basic
        URL = window.createObjectURL(event.raw);
      } else if (window.URL != undefined) {
        // mozilla(firefox)
        URL = window.URL.createObjectURL(event.raw);
      } else if (window.webkitURL != undefined) {
        // webkit or chrome
        URL = window.webkitURL.createObjectURL(event.raw);
      }
      // 转换后的地址为 blob:http://xxx/7bf54338-74bb-47b9-9a7f-7a7093c716b5
      this.localUrl = URL;
      this.isShowImgUpload = true;//呈现本地预览组件
      this.isShowUpload = false;//隐藏上传组件
      this.fileList = fileList;
      this.form.bookCover = event.raw
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    uploadButtonClick() {
      this.fileList = []
      this.form.localUrl = ''
      this.isShowImgUpload = false;//隐藏本地预览
      this.isShowUpload = true;//显示上传组件
    },
    onSubmit() {
      let fd = new FormData();
      fd.append('bookcoverimage', this.fileList[0].raw);
      fd.append('name', this.form.name);
      fd.append('author', this.form.author);
      fd.append('description', this.form.description);
      fd.append('isbn', this.form.isbn);
      fd.append('publicationDate', this.form.publishDate);
      this.$refs.form.validate(valid => {
        if (valid) {
          request({
            url: '/admin/uploadNewBook',
            method: 'post',
            data: fd
          }).then((rep) => {
            alert(rep.data.message)
            this.$router.push('/home/show')
          }).catch((err) => {
            alert(err.response.data.message)
          })
        } else {
          alert('请填写完所有内容！')
        }
      })
    }
  },
  mounted() {
    if (!this.$store.state.login) {
      alert('你没登录，不能上传')
      this.$router.push('/login');
    }
  }
}
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.up-form {
  width: 90%;
}
</style>