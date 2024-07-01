"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  methods: {
    // 登出方法
    logout() {
      common_vendor.wx$1.removeStorageSync("userInfo");
      this.is_login = false;
      this.avatarUrl = "";
      this.nickName = "";
      common_vendor.wx$1.switchTab({
        url: "/pages/my/my"
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.o((...args) => $options.logout && $options.logout(...args))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-7a487483"], ["__file", "C:/Users/DELL/Desktop/springboot/wechatapp/cemenghui/src/pages/setting/setting.vue"]]);
wx.createPage(MiniProgramPage);
