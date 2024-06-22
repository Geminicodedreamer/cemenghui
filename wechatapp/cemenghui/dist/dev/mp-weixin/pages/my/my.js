"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      coupon: 6,
      integral: 300,
      icon: [],
      lbIcon: [],
      is_login: false,
      avatarUrl: "",
      nickName: ""
    };
  },
  onLoad() {
    this.getUserInfo();
    this.tabBarIcon();
    this.liebiaoIcon();
  },
  onShow() {
    this.getUserInfo();
  },
  methods: {
    getUserInfo() {
      const userInfo = common_vendor.wx$1.getStorageSync("userInfo");
      if (userInfo) {
        this.avatarUrl = userInfo.avatarUrl;
        this.nickName = userInfo.nickName;
        this.is_login = true;
      } else {
        this.is_login = false;
        this.avatarUrl = "";
        this.nickName = "";
      }
    },
    login() {
      common_vendor.wx$1.navigateTo({
        url: "/pages/login/login"
      });
    },
    tabBarIcon() {
      var data = {
        "icons": [
          {
            "photoSrc": "/static/icons/record.png",
            "text": "体重记录",
            "toUrl": ""
          },
          {
            "photoSrc": "/static/icons/change.png",
            "text": "体脂变化",
            "toUrl": ""
          },
          {
            "photoSrc": "/static/icons/time.png",
            "text": "锻炼时长",
            "toUrl": ""
          },
          {
            "photoSrc": "/static/icons/achievement.png",
            "text": "今日成果",
            "toUrl": ""
          },
          {
            "photoSrc": "/static/icons/more.png",
            "text": "更多",
            "toUrl": ""
          }
        ]
      };
      this.icon = data.icons;
    },
    //列表单元格
    liebiaoIcon() {
      var data = {
        "icons": [
          {
            "photoSrc": "/static/list/info.png",
            "text": "基本信息",
            "toUrl": "/pagesGoods/goods/index"
          },
          {
            "photoSrc": "/static/list/like.png",
            "text": "我的收藏",
            "toUrl": ""
          },
          {
            "photoSrc": "/static/list/shopping.png",
            "text": "商品购买",
            "toUrl": ""
          },
          {
            "photoSrc": "/static/list/myspace.png",
            "text": "我的空间",
            "toUrl": ""
          },
          {
            "photoSrc": "/static/list/about.png",
            "text": "关于我们",
            "toUrl": ""
          },
          {
            "photoSrc": "/static/list/setting.png",
            "text": "设置",
            "toUrl": "/pages/setting/setting"
          }
        ]
      };
      this.lbIcon = data.icons;
    }
  }
};
if (!Array) {
  const _component_van_image = common_vendor.resolveComponent("van-image");
  const _component_van_cell = common_vendor.resolveComponent("van-cell");
  const _component_van_grid_item = common_vendor.resolveComponent("van-grid-item");
  const _component_van_grid = common_vendor.resolveComponent("van-grid");
  const _component_van_icon = common_vendor.resolveComponent("van-icon");
  (_component_van_image + _component_van_cell + _component_van_grid_item + _component_van_grid + _component_van_icon)();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $data.is_login
  }, $data.is_login ? {
    b: common_vendor.p({
      width: "100%",
      height: "100%",
      round: true,
      src: $data.avatarUrl
    }),
    c: common_vendor.t($data.nickName)
  } : {
    d: common_vendor.o($options.login),
    e: common_vendor.p({
      width: "100%",
      height: "100%",
      round: true,
      src: "/static/images/unlogin.png"
    }),
    f: common_vendor.o((...args) => $options.login && $options.login(...args))
  }, {
    g: common_vendor.t($data.coupon),
    h: common_vendor.t($data.integral),
    i: common_vendor.p({
      isLink: true,
      url: "",
      title: "健身计划",
      value: "展开",
      linkType: "navigateTo"
    }),
    j: common_vendor.f($data.icon, (item, key, i0) => {
      return {
        a: key,
        b: "93cdf742-4-" + i0 + ",93cdf742-3",
        c: common_vendor.p({
          url: item.toUrl,
          text: item.text,
          icon: item.photoSrc
        })
      };
    }),
    k: common_vendor.p({
      gutter: 3,
      square: true,
      columnNum: "5"
    }),
    l: common_vendor.f($data.lbIcon, (liebiao, key, i0) => {
      return {
        a: "93cdf742-6-" + i0 + "," + ("93cdf742-5-" + i0),
        b: key,
        c: "93cdf742-5-" + i0,
        d: common_vendor.p({
          icon: liebiao.photoSrc,
          title: liebiao.text,
          url: liebiao.toUrl
        })
      };
    })
  });
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__file", "C:/Users/DELL/Desktop/springboot/wechatapp/cemenghui/src/pages/my/my.vue"]]);
wx.createPage(MiniProgramPage);
