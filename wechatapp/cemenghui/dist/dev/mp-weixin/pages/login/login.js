"use strict";
const common_vendor = require("../../common/vendor.js");
const config = {
  appid: "wx1665b7f96eb29d8b",
  appSecret: "10e511717ad8fe349211c7efc82812d9"
};
const _sfc_main = {
  data() {
    return {
      userInfo: {}
    };
  },
  methods: {
    goHome() {
      common_vendor.index.switchTab({
        url: "/pages/index/index"
      });
    },
    async getUserProfile() {
      common_vendor.wx$1.getUserProfile({
        desc: "完善用户信息",
        success: (infoRes) => {
          const { userInfo } = infoRes;
          this.userInfo = userInfo;
          console.log(JSON.stringify(userInfo, null, 2));
          common_vendor.wx$1.setStorageSync("userInfo", userInfo);
          common_vendor.wx$1.switchTab({
            url: "/pages/my/my"
          });
        }
      });
    },
    async getOpenId() {
      const { appid, appSecret } = config;
      const { code } = await common_vendor.wx$1.login();
      let url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + appSecret + "&js_code=" + code + "&grant_type=authorization_code";
      common_vendor.wx$1.request({
        url,
        success: (result) => {
          const { openid } = result.data;
          console.log("openid", openid);
        },
        fail(err) {
          console.log(err.message);
        }
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.userInfo.avatarUrl,
    b: common_vendor.t($data.userInfo.nickName),
    c: common_vendor.o((...args) => $options.getUserProfile && $options.getUserProfile(...args)),
    d: common_vendor.o((...args) => $options.goHome && $options.goHome(...args))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-cdfe2409"], ["__file", "C:/Users/DELL/Desktop/springboot/wechatapp/cemenghui/src/pages/login/login.vue"]]);
wx.createPage(MiniProgramPage);
