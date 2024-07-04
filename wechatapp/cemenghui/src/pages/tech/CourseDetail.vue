<template>
  <view class="course-detail">
    <video :src="course.video" controls></video>
    <view class="course-info">
      <text class="course-name">{{ course.lessonname }}</text>
      <text class="course-description">{{ course.lessonintro }}</text>
      <text class="course-teacher">讲师: {{ course.lessonauthor }}</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      course: {}
    }
  },
  onLoad(options) {
    const id = parseInt(options.id);
    if (wx.getStorageSync('userType') === 'tourist') {
      this.getFakeCourseDetail(id);
    } else {
      this.getCourseDetail(id);
    }
  },
  methods: {
    getFakeCourseDetail(id) {
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
          video: 'https://wybcemenghui.oss-cn-beijing.aliyuncs.com/58c6f439b4bc4b77823f629a7190ccc5.mp4'
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
      
      // 查找课程
      const course = fakeCourses.find(item => item.id === id);
      if (course) {
        this.course = course;
      } else {
        console.error('Course not found');
      }
    },
    getCourseDetail(id) {
      const token = wx.getStorageSync('jwt_token');
      if (!token) {
        console.error('No token found');
        return;
      }
      wx.request({
        url: 'https://app6457.acapp.acwing.com.cn/api/lesson/list', // 替换为你的实际接口地址
        method: 'GET',
        header: {
          'Authorization': `Bearer ${token}` // 添加Token到请求头
        },
        success: (res) => {
          if (res.statusCode === 200) {
            const course = res.data.lessons.find(item => item.id === id);
            if (course) {
              this.course = course;
            } else {
              console.error('Course not found');
            }
          } else {
            console.error('Failed to fetch courses:', res);
          }
        },
        fail: (err) => {
          console.error('Failed to fetch courses:', err);
        }
      });
    }
  }
}
</script>

<style scoped>
.course-detail {
  padding: 20px;
}

.course-info {
  margin-top: 20px;
}

.course-name {
  font-size: 22px;
  font-weight: bold;
}

.course-description {
  margin-top: 10px;
  color: #666;
}

.course-teacher {
  margin-top: 10px;
  color: #999;
}
</style>
