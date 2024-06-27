"use strict";
Object.defineProperty(exports, Symbol.toStringTag, { value: "Module" });
const common_vendor = require("./common/vendor.js");
const store_index = require("./store/index.js");
require("./store/user.js");
if (!Math) {
  "./pages/index/index.js";
  "./pages/my/my.js";
  "./pages/login/login.js";
  "./pages/cooperation/cooperation.js";
  "./pages/setting/setting.js";
  "./pages/news/news.js";
  "./pages/tech/tech.js";
}
const _sfc_main = {
  onLaunch() {
    console.log("App Launch");
    this.checkLoginStatus();
  },
  onShow() {
    console.log("App Show");
    this.checkLoginStatus();
  },
  methods: {
    checkLoginStatus() {
      const token = common_vendor.wx$1.getStorageSync("jwt_token");
      if (token) {
        common_vendor.wx$1.request({
          url: "http://127.0.0.1:3000/user/account/info/",
          method: "GET",
          header: {
            Authorization: `Bearer ${token}`
          },
          success: (res) => {
            if (res.data.error_message === "success") {
              store_index.store.commit("user/updateUser", {
                id: res.data.id,
                username: res.data.username,
                photo: res.data.photo,
                is_login: true
              });
              store_index.store.commit("user/updatePullingInfo", false);
            } else {
              store_index.store.commit("user/logout");
            }
          },
          fail: () => {
            store_index.store.commit("user/logout");
          }
        });
      } else {
        store_index.store.commit("user/logout");
      }
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {};
}
const App = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__file", "C:/Users/DELL/Desktop/springboot/wechatapp/cemenghui/src/App.vue"]]);
function createApp() {
  const app = common_vendor.createSSRApp(App);
  app.use(store_index.store);
  app.config.globalProperties.$store = store_index.store;
  return {
    app
  };
}
createApp().app.mount("#app");
exports.createApp = createApp;
