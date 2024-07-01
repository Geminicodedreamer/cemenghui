"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      userType: null
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
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {};
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-c9ec16af"], ["__file", "C:/Users/DELL/Desktop/springboot/wechatapp/cemenghui/src/pages/tech/tech.vue"]]);
wx.createPage(MiniProgramPage);
