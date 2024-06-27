"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  methods: {
    ...common_vendor.mapActions("user", ["logout"]),
    handleLogout() {
      this.logout();
      common_vendor.wx$1.removeStorageSync("jwt_token");
      this.$store.commit("user/logout");
      common_vendor.wx$1.switchTab({
        url: "/pages/my/my"
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.o((...args) => $options.handleLogout && $options.handleLogout(...args))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-7a487483"], ["__file", "C:/Users/DELL/Desktop/springboot/wechatapp/cemenghui/src/pages/setting/setting.vue"]]);
wx.createPage(MiniProgramPage);
