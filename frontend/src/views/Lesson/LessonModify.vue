
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
          <input type="file" class="form-control-file" id="course-cover" @change="handleFileChange">
        </div>
        <div class="form-group">
          <label for="course-video">课程视频 (mp4)</label>
          <input type="file" class="form-control-file" id="course-video" @change="handleVideoChange" accept="video/mp4">
        </div>
        <button type="submit" class="btn btn-primary">确定</button>
      </form>
      <!-- 错误提示模态框 -->
      <div class="modal fade" id="errorModal" tabindex="-1" role="dialog" aria-labelledby="errorModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="errorModalLabel">错误</h5>
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              {{ errorMessage }}
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
            </div>
          </div>
        </div>
      </div>
    </ContentField>
  </div>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import $ from 'jquery';
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
        video: null
      },
      errorMessage: ''
    }
  },
  methods: {
    handleFileChange(event) {
      this.course.cover = event.target.files[0];
    },
    handleVideoChange(event) {
      this.course.video = event.target.files[0];
    },
    createCourse() {
      if (!this.course.name || !this.course.intro || !this.course.order || !this.course.author) {
        this.errorMessage = '所有字段都是必填项';
        $('#errorModal').modal('show');
        return;
      }

      this.$router.push('/lesson'); // 返回课程列表页面
    }
  }
}
</script>

<style scoped>
</style>
