<template>
  <el-dialog title="添加课程" v-model="internalDialogVisible" @close="resetForm">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="课程排序" :required="true" class="form-item">
        <el-input v-model="form.id" placeholder="请输入课程排序" type="number"></el-input>
      </el-form-item>
      <el-form-item label="课程名称" :required="true" class="form-item">
        <el-input v-model="form.lessonname" placeholder="请输入课程名称"></el-input>
      </el-form-item>
      <el-form-item label="课程简介" :required="true" class="form-item">
        <el-input v-model="form.lessonintro" placeholder="请输入课程简介"></el-input>
      </el-form-item>
      <el-form-item label="课程作者" :required="true" class="form-item">
        <el-input v-model="form.lessonauthor" placeholder="请输入课程作者"></el-input>
      </el-form-item>
      <el-form-item label="课程封面" class="form-item">
        <el-upload
          class="avatar-uploader"
          action="http://127.0.0.1:3000/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :on-error="handleUploadError"
          :before-upload="beforeAvatarUpload">
          <template #trigger>
            <div class="upload-trigger">
              <div v-if="!form.photoUrl"><Plus style="width:20px;color: #dcdfe6;" /></div>
              <img v-if="form.photoUrl" :src="form.photoUrl" class="avatar">
            </div>
          </template>
        </el-upload>
        <br>
        <div class="upload-description">
          请上传 大小不超过<span class="red-bold">10MB</span> 格式为 
          <span class="red-bold">png/jpg/jpeg/gif</span> 的文件
        </div>
      </el-form-item>
      <el-form-item label="课程视频" class="form-item">
        <el-upload
          class="video-uploader"
          action="http://127.0.0.1:3000/upload"
          :show-file-list="false"
          :on-success="handleVideoSuccess"
          :on-error="handleUploadError"
          :before-upload="beforeVideoUpload">
          <template #trigger>
            <div class="upload-trigger">
              <div v-if="!form.videoUrl"><Plus style="width:20px;color: #dcdfe6;" /></div>
              <video v-if="form.videoUrl" :src="form.videoUrl" controls class="video-preview"></video>
            </div>
          </template>
        </el-upload>
        <br>
        <div class="upload-description">
          请上传 大小不超过<span class="red-bold">10MB</span> 格式为 
          <span class="red-bold">mp4</span> 的文件
        </div>
      </el-form-item>
      <el-form-item class="button-container">
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import { ElMessage } from 'element-plus';
import { useStore } from 'vuex';
import $ from 'jquery';
import { Plus } from '@element-plus/icons-vue'; // 引入图标

export default {
  components: {
    Plus,
  },
  props: {
    dialogVisible: {
      type: Boolean,
      required: true
    }
  },
  data() {
    const store = useStore();
    return {
      internalDialogVisible: this.dialogVisible,
      form: {
        id: '',
        lessonname: '',
        lessonintro: '',
        lessonauthor: '',
        photoUrl: '', // 用于存储上传后的图片URL
        videoUrl: '', // 用于存储上传后的视频URL
        companyname: '',
      },
      store,
    };
  },
  watch: {
    dialogVisible(val) {
      this.internalDialogVisible = val;
    },
    internalDialogVisible(val) {
      this.$emit('update:dialogVisible', val);
    }
  },
  methods: {
    handleAvatarSuccess(response) {
      this.form.photoUrl = response.url; // 假设后端返回的图片 URL 在 response.url 中
    },
    handleVideoSuccess(response) {
      this.form.videoUrl = response.url; // 假设后端返回的视频 URL 在 response.url 中
    },
    handleUploadError(err) {
      ElMessage.error('上传失败');
      console.error(err);
    },
    beforeAvatarUpload(file) {
      const isImage = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif';
      const isLt10M = file.size / 1024 / 1024 < 10;

      if (!isImage) {
        ElMessage.error('上传图片只能是 JPG/PNG/GIF 格式!');
      }
      if (!isLt10M) {
        ElMessage.error('上传图片大小不能超过 10MB!');
      }
      return isImage && isLt10M;
    },
    beforeVideoUpload(file) {
      const isVideo = file.type === 'video/mp4';
      const isLt10M = file.size / 1024 / 1024 < 10;

      if (!isVideo) {
        ElMessage.error('上传视频只能是 MP4 格式!');
      }
      if (!isLt10M) {
        ElMessage.error('上传视频大小不能超过 10MB!');
      }
      return isVideo && isLt10M;
    },
    submitForm() {
      if (this.form.id && this.form.lessonname && this.form.lessonintro && this.form.lessonauthor) {
      console.log('即将提交的表单数据:', this.form);
      let companyname = null;
      const userType = localStorage.getItem("userType");
      if(userType === 'user') companyname = this.store.state.user.companyname;
      else companyname = this.store.state.user.username;
        $.ajax({
          url: 'http://127.0.0.1:3000/lesson/add', // 后端添加课程信息的接口
          type: 'POST',
          data: {
            id: this.form.id,
            lessonname: this.form.lessonname,
            lessonintro: this.form.lessonintro,
            lessonauthor: this.form.lessonauthor,
            photo: this.form.photoUrl, // 使用上传后的图片 URL
            video: this.form.videoUrl, // 使用上传后的视频 URL
            companyname: companyname,
          },
          headers: {
            Authorization: "Bearer " + this.store.state.user.token,
          },
          success: (response) => {
            if (response.error_message === 'success') {
              ElMessage.success('表单提交成功');
              this.internalDialogVisible = false;
              this.$emit('update');
              this.resetForm();
            } else {
              ElMessage.error(response.error_message);
            }
          },
          error: (error) => {
            console.error(error);
            ElMessage.error('提交失败');
          }
        });
      } else {
        ElMessage.error('请完整填写表单');
      }
    },
    resetForm() {
      this.$refs.form.resetFields();
      this.form.id= '';
      this.form.lessonname= '';
      this.form.lessonintro= '';
      this.form.lessonauthor= '';
      this.form.photoUrl= ''; // 用于存储上传后的图片URL
      this.form.videoUrl= '';
      this.internalDialogVisible = false;
    }
  }
};
</script>

<style scoped>
.form-item {
  margin-bottom: 20px;
}
.button-container {
  text-align: right;
}
.avatar {
  width: 100px;
  height: 100px;
  display: block;
  margin-top: 10px;
}
.upload-trigger {
  width: 120px;
  height: 120px;
  bid: 1px dashed #dcdfe6;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.upload-description {
  margin-top: 10px;
  font-size: 12px;
  color: #888;
}
.red-bold {
  color: red;
  font-weight: bold;
}
.video-preview {
  width: 100%;
  height: auto;
}
</style>