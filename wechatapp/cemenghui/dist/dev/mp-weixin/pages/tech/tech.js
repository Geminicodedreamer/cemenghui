"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      userType: null,
      courses: []
      // 用于存储从数据库获取的课程数据
    };
  },
  onShow() {
    this.checkLoginStatus();
  },
  methods: {
    checkLoginStatus() {
      this.userType = common_vendor.wx$1.getStorageSync("userType");
      if (this.userType === "tourist") {
        this.loadFakeCourses();
        return;
      }
      const token = common_vendor.wx$1.getStorageSync("jwt_token");
      if (!token) {
        common_vendor.wx$1.navigateTo({
          url: "/pages/login/login"
        });
      } else {
        this.$store.dispatch("user/getinfo", {
          success: (res) => {
            console.log("User info fetched successfully:", res);
            this.getCourses(token);
          },
          error: (err) => {
            console.error("Failed to fetch user info:", err);
            common_vendor.wx$1.navigateTo({
              url: "/pages/login/login"
            });
          }
        });
      }
    },
    getCourses(token) {
      common_vendor.wx$1.request({
        url: "https://app6457.acapp.acwing.com.cn/api/lesson/list",
        // 替换为你的实际接口地址
        method: "GET",
        header: {
          "Authorization": `Bearer ${token}`
          // 添加Token到请求头
        },
        success: (res) => {
          if (res.statusCode === 200) {
            console.log("Courses fetched successfully:", res.data.lessons);
            this.courses = res.data.lessons.map((course) => ({
              id: course.id,
              lessonname: course.lessonname,
              lessonintro: course.lessonintro,
              lessonauthor: course.lessonauthor,
              photo: course.photo,
              video: course.video
            }));
          } else {
            console.error("Failed to fetch courses:", res);
          }
        },
        fail: (err) => {
          console.error("Failed to fetch courses:", err);
        }
      });
    },
    loadFakeCourses() {
      const fakeCourses = [
        {
          id: 1,
          lessonname: "原课",
          lessonintro: "这是一个原神的系统讲解。",
          lessonauthor: "王禹博",
          photo: "/static/images/Genshin.jpg",
          video: "/static/video/舞蹈课.mp4"
        },
        {
          id: 2,
          lessonname: "原课2",
          lessonintro: "这是另一个假的课程介绍。",
          lessonauthor: "假讲师",
          photo: "/static/images/Genshin.jpg",
          video: "/static/video/舞蹈课.mp4"
        },
        {
          id: 3,
          lessonname: "舞蹈课",
          lessonintro: "精品舞蹈课。",
          lessonauthor: "王禹博",
          photo: "/static/images/舞蹈课.jpg",
          video: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/58c6f439b4bc4b77823f629a7190ccc5.mp4"
        }
      ];
      this.courses = fakeCourses;
    },
    goToDetail(id) {
      common_vendor.wx$1.navigateTo({
        url: `/pages/tech/CourseDetail?id=${id}`
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.f($data.courses, (course, k0, i0) => {
      return {
        a: course.photo,
        b: common_vendor.t(course.lessonname),
        c: course.id,
        d: common_vendor.o(($event) => $options.goToDetail(course.id), course.id)
      };
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-c9ec16af"], ["__file", "C:/Users/Lenovo/Desktop/springboot/wechatapp/cemenghui/src/pages/tech/tech.vue"]]);
wx.createPage(MiniProgramPage);
