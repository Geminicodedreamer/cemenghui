"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      course: {}
    };
  },
  onLoad(options) {
    const id = parseInt(options.id);
    if (common_vendor.wx$1.getStorageSync("userType") === "tourist") {
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
          video: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/58c6f439b4bc4b77823f629a7190ccc5.mp4"
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
      const course = fakeCourses.find((item) => item.id === id);
      if (course) {
        this.course = course;
      } else {
        console.error("Course not found");
      }
    },
    getCourseDetail(id) {
      const token = common_vendor.wx$1.getStorageSync("jwt_token");
      if (!token) {
        console.error("No token found");
        return;
      }
      common_vendor.wx$1.request({
        url: "http://127.0.0.1:3000/lesson/list",
        // 替换为你的实际接口地址
        method: "GET",
        header: {
          "Authorization": `Bearer ${token}`
          // 添加Token到请求头
        },
        success: (res) => {
          if (res.statusCode === 200) {
            const course = res.data.lessons.find((item) => item.id === id);
            if (course) {
              this.course = course;
            } else {
              console.error("Course not found");
            }
          } else {
            console.error("Failed to fetch courses:", res);
          }
        },
        fail: (err) => {
          console.error("Failed to fetch courses:", err);
        }
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.course.video,
    b: common_vendor.t($data.course.lessonname),
    c: common_vendor.t($data.course.lessonintro),
    d: common_vendor.t($data.course.lessonauthor)
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-22ba86a4"], ["__file", "C:/Users/chenzhanpeng/Desktop/实训/wechatapp/cemenghui/src/pages/tech/CourseDetail.vue"]]);
wx.createPage(MiniProgramPage);
