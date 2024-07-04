<template>
  <el-dialog title="修改课程信息" v-model="internalDialogVisible" @close="resetForm">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="课程名称" :required="true" class="form-item">
        <el-input v-model="form.lessonname" placeholder="请输入课程名称"></el-input>
      </el-form-item>
      <el-form-item label="课程图标" class="form-item">
        <el-upload
          class="avatar-uploader"
          action="https://app6457.acapp.acwing.com.cn/api/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload">
          <template #trigger>
            <div class="upload-trigger">
              <div v-if="!form.photo">
                <Plus style="width:20px;color: #dcdfe6;"/>
              </div>
              <div v-if="form.photo" class="uploaded-image-container">
                <img :src="form.photo" class="avatar" @click="previewImage">
                <div class="image-actions">
                  <ZoomIn @click.stop="previewImage" class="icon-button"/>
                  <Delete @click.stop="deleteImage" class="icon-button"/>
                </div>
              </div>
            </div>
          </template>
        </el-upload>
        <br>
        <div class="upload-description">
          请上传 大小不超过<span class="red-bold">5MB</span> 格式为 
          <span class="red-bold">png/jpg/jpeg/gif</span> 的文件
        </div>
      </el-form-item>
      <el-form-item label="课程介绍" class="form-item">
        <el-input
          type="textarea"
          v-model="form.lessonintro"
          placeholder="请输入课程介绍"
        ></el-input>
      </el-form-item>
      <el-form-item label="课程作者" :required="true" class="form-item">
        <el-input v-model="form.lessonauthor" placeholder="请输入课程作者"></el-input>
      </el-form-item>
      <el-form-item label="课程视频" class="form-item">
        <el-upload
          class="video-uploader"
          action="https://app6457.acapp.acwing.com.cn/api/upload"
          :show-file-list="false"
          :on-success="handleVideoSuccess"
          :before-upload="beforeVideoUpload">
          <template #trigger>
            <div class="upload-trigger">
              <div v-if="!form.video">
                <Plus style="width:20px;color: #dcdfe6;"/>
              </div>
              <div v-if="form.video" class="uploaded-video-container">
                <video :src="form.video" class="video" controls @click="previewVideo"></video>
                <div class="video-actions">
                  <ZoomIn @click.stop="previewVideo" class="icon-button"/>
                  <Delete @click.stop="deleteVideo" class="icon-button"/>
                </div>
              </div>
            </div>
          </template>
        </el-upload>
        <br>
        <div class="upload-description">
          请上传 大小不超过<span class="red-bold">50MB</span> 格式为 
          <span class="red-bold">mp4/avi/mkv</span> 的文件
        </div>
      </el-form-item>
      <el-form-item class="button-container">
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">取消</el-button>
      </el-form-item>
    </el-form>
    <el-image-viewer
      v-if="imageViewerVisible"
      :url-list="[form.photo]"
      :initial-index="0"
      @close="imageViewerVisible = false"
    />
    <el-dialog
      v-if="videoViewerVisible"
      v-model="videoViewerVisible"
      width="70%"
      :before-close="handleClose">
      <video :src="form.video" controls style="width: 100%;"></video>
    </el-dialog>
  </el-dialog>
</template>

<script>
import { ElMessage, ElImageViewer } from 'element-plus';
import { Plus, ZoomIn, Delete } from '@element-plus/icons-vue'; 
import axios from 'axios';
import { useStore } from 'vuex';
import $ from 'jquery';

export default {
  components: {
    Plus,
    ZoomIn,
    Delete,
    ElImageViewer,
  },
  props: {
    dialogVisible: {
      type: Boolean,
      required: true
    },
    lesson: {
      type: Object,
      default: null
    }
  },
  data() {
    const store = useStore();
    return {
      store,
      internalDialogVisible: this.dialogVisible,
      form: {
        id: '',
        lessonname: '',
        photo: '',
        lessonintro: '',
        lessonauthor: '',
        video: '',
        companyname:'',
      },
      imageViewerVisible: false,
      videoViewerVisible: false
    };
  },
  watch: {
    dialogVisible(val) {
      this.internalDialogVisible = val;
    },
    internalDialogVisible(val) {
      this.$emit('update:dialogVisible', val);
    },
    lesson: {
      handler(newVal) {
        if (newVal) {
          this.form = { ...newVal };
        } else {
          this.resetForm();
        }
      },
      immediate: true
    }
  },
  methods: {
    uploadImage(file) {
      const formData = new FormData();
      formData.append('file', file);

      axios.post('https://app6457.acapp.acwing.com.cn/api/upload', formData)
        .then(response => {
          this.form.photo = response.data.url;
        })
        .catch(error => {
          console.error('Error uploading image: ', error);
          ElMessage.error('图片上传失败');
        });
    },
    handleAvatarSuccess(response) {
      this.form.photo = response.url;
    },
    beforeAvatarUpload(file) {
      const isImage = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif';
      const isLt5M = file.size / 1024 / 1024 < 5;

      if (!isImage) {
        ElMessage.error('上传图片只能是 JPG/PNG/GIF 格式!');
      }
      if (!isLt5M) {
        ElMessage.error('上传图片大小不能超过 5MB!');
      }
      return isImage && isLt5M;
    },
    handleVideoSuccess(response) {
      this.form.video = response.url;
    },
    beforeVideoUpload(file) {
      const isVideo = file.type === 'video/mp4' || file.type === 'video/avi' || file.type === 'video/mkv';
      const isLt50M = file.size / 1024 / 1024 < 50;

      if (!isVideo) {
        ElMessage.error('上传视频只能是 MP4/AVI/MKV 格式!');
      }
      if (!isLt50M) {
        ElMessage.error('上传视频大小不能超过 50MB!');
      }
      return isVideo && isLt50M;
    },
    previewImage() {
      this.imageViewerVisible = true;
    },
    deleteImage() {
      this.form.photo = '';
    },
    previewVideo() {
      this.videoViewerVisible = true;
    },
    deleteVideo() {
      this.form.video = '';
    },
    submitForm() {
      if (this.form.lessonname && this.form.lessonauthor) {
        console.error(this.form);
        $.ajax({
          url: 'https://app6457.acapp.acwing.com.cn/api/lesson/modify', // 后端修改公司信息的接口
          type: 'POST',
          data: {
            id: this.form.id,
            lessonname: this.form.lessonname,
            photo: this.form.photo,
            lessonintro: this.form.lessonintro,
            lessonauthor: this.form.lessonauthor,
            video: this.form.video,
            companyname: this.form.companyname,
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
      this.form = {
        id: '',
        lessonname: '',
        photo: '',
        lessonintro: '',
        lessonauthor: '',
        video: ''
      };
      this.internalDialogVisible = false;
    },

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
  width: 120px;
  height: 120px;
  display: block;
  object-fit: cover;
  cursor: pointer;
}
.upload-trigger {
  width: 120px;
  height: 120px;
  border: 1px dashed #dcdfe6;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.uploaded-image-container {
  position: relative;
  width: 120px;
  height: 120px;
}

.image-actions {
  display: flex;
  justify-content: space-around;
  position: absolute;
  bottom: 0%;
  left: 0;
  right: 0;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
}

.icon-button {
  margin-top: 40%;
  color: #fff;
  width: 20px;
  height: 20px;
}

.uploaded-video-container {
  position: relative;
  width: 120px;
  height: 120px;
}

.video {
  width: 100%;
  height: 100%;
  object-fit: cover;
  cursor: pointer;
}

.video-actions {
  display: flex;
  justify-content: space-around;
  position: absolute;
  bottom: 0%;
  left: 0;
  right: 0;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
}

.upload-description {
  margin-top: 10px;
  font-size: 12px;
  color: #888;
  white-space: pre-line;
}
.red-bold {
  color: red;
  font-weight: bold;
}
.note-description {
  margin-top: 10px;
  font-size: 12px;
  color: #888;
}
</style>
