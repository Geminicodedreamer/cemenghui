<template>
  <view class="myContent">
    <view class="course-list">
      <view class="course-item" v-for="course in courses" :key="course.id" @click="goToDetail(course.id)">
        <image class="course-cover" :src="course.photo" mode="aspectFill"></image>
        <view class="course-info">
          <text class="course-name">{{ course.lessonname }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userType: null,
      courses: []  // 用于存储从数据库获取的课程数据
    }
  },
  onShow() {
    this.checkLoginStatus();
  },
  methods: {
    checkLoginStatus() {
      this.userType = wx.getStorageSync('userType');
      if (this.userType === "tourist") {
        this.loadFakeCourses();
        return;
      }
      const token = wx.getStorageSync('jwt_token');
      if (!token) {
        wx.navigateTo({
          url: '/pages/login/login'
        });
      } else {
        this.$store.dispatch('user/getinfo', {
          success: (res) => {
            console.log('User info fetched successfully:', res);
            this.getCourses(token); // 在成功获取用户信息后获取课程列表
          },
          error: (err) => {
            console.error('Failed to fetch user info:', err);
            wx.navigateTo({
              url: '/pages/login/login'
            });
          }
        });
      }
    },
    getCourses(token) {
      wx.request({
        url: 'http://127.0.0.1:3000/lesson/list',  // 替换为你的实际接口地址
        method: 'GET',
        header: {
          'Authorization': `Bearer ${token}` // 添加Token到请求头
        },
        success: (res) => {
          if (res.statusCode === 200) {
            console.log('Courses fetched successfully:', res.data.lessons);
            // 解析课程数据
            this.courses = res.data.lessons.map(course => ({
              id: course.id,
              lessonname: course.lessonname,
              lessonintro: course.lessonintro,
              lessonauthor: course.lessonauthor,
              photo: course.photo,
              video: course.video
            }));
          } else {
            console.error('Failed to fetch courses:', res);
          }
        },
        fail: (err) => {
          console.error('Failed to fetch courses:', err);
        }
      });
    },
    loadFakeCourses() {
      const fakeCourses = [
        {
          id: 1,
          lessonname: '原课',
          lessonintro: '这是一个原神的系统讲解。',
          lessonauthor: '王禹博',
          photo: '/static/images/Genshin.jpg',
          video: '/static/video/舞蹈课.mp4'
        },
        {
          id: 2,
          lessonname: '原课2',
          lessonintro: '这是另一个假的课程介绍。',
          lessonauthor: '假讲师',
          photo: '/static/images/Genshin.jpg',
          video: '/static/video/舞蹈课.mp4'
        },
        {
          id: 3,
          lessonname: '舞蹈课',
          lessonintro: '精品舞蹈课。',
          lessonauthor: '王禹博',
          photo: '/static/images/舞蹈课.jpg',
          video: 'https://wybcemenghui.oss-cn-beijing.aliyuncs.com/58c6f439b4bc4b77823f629a7190ccc5.mp4'
        }
      ];
      this.courses = fakeCourses;
    },
    goToDetail(id) {
      wx.navigateTo({
        url: `/pages/tech/CourseDetail?id=${id}`
      });
    }
  }
}
</script>

<style scoped>
.myContent {
  padding: 10px;
}

.course-list {
  display: flex;
  flex-wrap: wrap; /* 允许换行 */
  justify-content: space-between; /* 在行间平均分布 */
}

.course-item {
  width: 45%; /* 每个课程项占据45%的宽度，保证一行能容纳两个 */
  margin-bottom: 10px; /* 底部边距 */
  border: 1px solid #ddd; /* 边框 */
  border-radius: 8px; /* 圆角 */
  overflow: hidden; /* 溢出隐藏 */
  text-align: center; /* 居中文本 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 添加阴影 */
  background-color: #fff; /* 背景颜色 */
}

.course-item:nth-child(odd) {
  margin-right: 4%; /* 确保每行两个课程的间距 */
}

.course-cover {
  width: 100%;
  height: 120px; /* 固定高度 */
}

.course-info {
  padding: 10px;
}

.course-name {
  font-size: 16px;
  font-weight: bold;
  margin-top: 5px;
  color: #333;
}
</style>
