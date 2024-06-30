"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      username: "",
      password: ""
    };
  },
  methods: {
    ...common_vendor.mapActions("user", ["login", "getinfo"]),
    goHome() {
      common_vendor.wx$1.switchTab({
        url: "/pages/index/index"
      });
    },
    tourist() {
      common_vendor.wx$1.setStorageSync("userType", "tourist");
      common_vendor.wx$1.setStorageSync("username", "游客");
      common_vendor.wx$1.switchTab({
        url: "/pages/my/my"
      });
    },
    handleLogin() {
      const { username, password } = this;
      this.login({
        username,
        password,
        success: (resp) => {
          this.getinfo({
            success: () => {
              common_vendor.wx$1.switchTab({
                url: "/pages/my/my"
              });
            },
            error: () => {
              common_vendor.wx$1.showToast({
                title: "获取用户信息失败",
                icon: "none"
              });
            }
          });
        },
        error: () => {
          common_vendor.wx$1.showToast({
            title: "登录失败",
            icon: "none"
          });
        }
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.username,
    b: common_vendor.o(($event) => $data.username = $event.detail.value),
    c: $data.password,
    d: common_vendor.o(($event) => $data.password = $event.detail.value),
    e: common_vendor.o((...args) => $options.handleLogin && $options.handleLogin(...args)),
    f: common_vendor.o((...args) => $options.goHome && $options.goHome(...args)),
    g: common_vendor.o((...args) => $options.tourist && $options.tourist(...args))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-cdfe2409"], ["__file", "C:/Users/李昊喆/Desktop/TheLast/cemenghui/wechatapp/cemenghui/src/pages/login/login.vue"]]);
wx.createPage(MiniProgramPage);
