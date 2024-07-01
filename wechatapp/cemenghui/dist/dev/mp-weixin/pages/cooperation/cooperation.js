"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      userType: null,
      categories: [
        {
          name: "会议研讨",
          description: "各类会议和研讨会的信息展示。",
          icon: "🗣️"
        },
        {
          name: "标准定制",
          description: "标准定制服务的简介和信息展示。",
          icon: "📏"
        },
        {
          name: "技术培训",
          description: "技术培训课程的简介和报名信息。",
          icon: "📚"
        },
        {
          name: "工具研发",
          description: "工具研发项目的进展和成果展示。",
          icon: "🔧"
        },
        {
          name: "公益行动",
          description: "各类公益行动的简介和参与方式。",
          icon: "🌍"
        }
      ]
    };
  },
  onShow() {
    this.checkLoginStatus();
  },
  methods: {
    checkLoginStatus() {
      this.userType = common_vendor.wx$1.getStorageSync("userType");
      if (this.userType === "tourist")
        return;
      const token = common_vendor.wx$1.getStorageSync("jwt_token");
      if (!token) {
        common_vendor.wx$1.navigateTo({
          url: "/pages/login/login"
        });
      } else {
        this.$store.dispatch("user/getinfo", {
          success: (res) => {
            console.log("User info fetched successfully:", res);
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
    handleCategoryClick(categoryName) {
      if (categoryName === "会议研讨") {
        common_vendor.wx$1.navigateTo({
          url: "/pages/cooperation/meetingList"
        });
      }
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.f($data.categories, (category, index, i0) => {
      return {
        a: common_vendor.t(category.icon),
        b: common_vendor.t(category.name),
        c: common_vendor.t(category.description),
        d: index,
        e: common_vendor.o(($event) => $options.handleCategoryClick(category.name), index)
      };
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-d3bc45b1"], ["__file", "C:/Users/DELL/Desktop/springboot/wechatapp/cemenghui/src/pages/cooperation/cooperation.vue"]]);
wx.createPage(MiniProgramPage);
