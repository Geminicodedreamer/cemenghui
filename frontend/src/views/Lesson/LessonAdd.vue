<template>
  <div>
    <ContentField>
      添加课程
      <form @submit.prevent="createCourse">
        <div class="form-group">
          <label for="course-name">课程名称</label>
          <input type="text" class="form-control" id="course-name" v-model="course.name" placeholder="请输入课程名称" required>
        </div>
        <div class="form-group">
          <label for="course-intro">课程简介</label>
          <input type="text" class="form-control" id="course-intro" v-model="course.intro" placeholder="请输入课程简介" required>
        </div>
        <div class="form-group">
          <label for="course-order">课程排序</label>
          <input type="number" class="form-control" id="course-order" v-model="course.order" placeholder="请输入课程排序" required>
        </div>
        <div class="form-group">
          <label for="course-author">课程作者</label>
          <input type="text" class="form-control" id="course-author" v-model="course.author" placeholder="请输入课程作者" required>
        </div>
        <div class="form-group">
          <label for="course-cover">课程封面</label>
          <input type="file" class="form-control-file" id="course-cover" @change="handleFileChange" accept="image/*">
        </div>
        <div v-if="course.coverPreview" class="form-group preview-container">
          <label>封面预览</label>
          <img :src="course.coverPreview" alt="Course Cover" class="img-thumbnail">
        </div>
        <div class="form-group">
          <label for="course-video">课程视频 (mp4)</label>
          <input type="file" class="form-control-file" id="course-video" @change="handleVideoChange" accept="video/mp4">
        </div>
        <div v-if="course.videoPreview" class="form-group preview-container">
          <label>视频预览</label>
          <video :src="course.videoPreview" controls class="video-thumbnail"></video>
        </div>
        <button type="submit" class="btn btn-primary">确定</button>
      </form>
      <div v-if="errorMessage" class="alert alert-danger" role="alert">
        {{ errorMessage }}
      </div>
    </ContentField>
  </div>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import axios from 'axios';

export default {
  components: {
    ContentField
  },
  data() {
    return {
      course: {
        name: '',
        intro: '',
        order: '',
        author: '',
        cover: null,
        video: null,
        coverPreview: null,
        videoPreview: null,
        showAddDialog: false,
      },
      errorMessage: ''
    }
  },
  methods: {
    handleFileChange(event) {
      const file = event.target.files[0];
      this.course.cover = file;
      this.course.coverPreview = URL.createObjectURL(file);
    },
    handleVideoChange(event) {
      const file = event.target.files[0];
      this.course.video = file;
      this.course.videoPreview = URL.createObjectURL(file);
    },
    createCourse() {
      if (!this.course.name || !this.course.intro || !this.course.order || !this.course.author) {
        this.errorMessage = '所有字段都是必填项';
        return;
      }

      // 清除错误信息
      this.errorMessage = '';

      // 处理课程创建逻辑，确保包含封面和视频文件
      let formData = new FormData();
      formData.append('name', this.course.name);
      formData.append('intro', this.course.intro);
      formData.append('order', this.course.order);
      formData.append('author', this.course.author);
      if (this.course.cover) {
        formData.append('cover', this.course.cover);
      }
      if (this.course.video) {
        formData.append('video', this.course.video);
      }

      // 假设你有一个创建课程的 API，可以用 axios 发送请求
      axios.post('/api/courses', formData)
        .then(() => {
          // 处理成功逻辑
          this.$emit('close'); // 向父组件发送关闭弹窗的事件
        })
        .catch(() => {
          // 处理错误逻辑
          this.errorMessage = '课程创建失败，请重试';
        });
    }
  }
}
</script>

<style scoped>
.preview-container {
  margin-top: 10px;
}

.img-thumbnail {
  max-width: 100%;
  max-height: 400px;
  display: block;
  margin: 10px auto;
}

.video-thumbnail {
  max-width: 100%;
  max-height: 400px;
  display: block;
  margin: 10px auto;
}
</style>
