"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      pictures: [
        { id: "1", url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/2cc62ab6ff7d4f79ae43748eac23fa66.jpg" },
        { id: "2", url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/f7f356b83e324a60885eff0d6031fb8d.jpg" },
        { id: "3", url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/8638899dedff419ea39e1f762e54ad74.png" },
        { id: "4", url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/6c3cc46b865d4874b9add0fb4bb67c38.jpg" },
        { id: "5", url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/a9ff39927e63409b9cc59cd7e11a0cbe.jpg" }
      ],
      logos: [
        { id: "1", url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/fd8d470f0a9a4521af5ec26fc325968e.png" },
        { id: "2", url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/b5f98982c1ce4a3bb5afa68e05161cc9.png" },
        { id: "3", url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/0f2562de773f457e9390237a76c89342.png" },
        { id: "4", url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/366a023a6287403c8e0f3ec0b7129606.png" },
        { id: "5", url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/2d4ca4280f0a461aa70d415c14888e40.png" },
        { id: "6", url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/d70a284945f34598a03fb4168ef4cd60.png" },
        { id: "7", url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/ea399a055b794113824c4cecee84ab96.png" },
        { id: "8", url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/1176ff9f9a6546deb156d28c214229fa.png" },
        { id: "9", url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/bb1209d3296a4edfb42853b798c744ca.png" },
        { id: "10", url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/8d18380a6b134b5ebefd34cbffa67f84.png" },
        { id: "11", url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/8efb856e4a2245dfad2f36694f5d9ae9.png" },
        { id: "12", url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/87d07c7b7ee244e7a0ec740f6317a79f.png" }
      ]
    };
  },
  methods: {
    onPreviewImage(url) {
      common_vendor.index.previewImage({
        urls: this.pictures.map((v) => v.url),
        current: url
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.f($data.pictures, (item, k0, i0) => {
      return {
        a: common_vendor.o(($event) => $options.onPreviewImage(item.url), item.id),
        b: item.url,
        c: item.id
      };
    }),
    b: common_vendor.f($data.logos, (logo, k0, i0) => {
      return {
        a: logo.url,
        b: logo.id
      };
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-83a5a03c"], ["__file", "C:/Users/DELL/Desktop/springboot/wechatapp/cemenghui/src/pages/index/index.vue"]]);
wx.createPage(MiniProgramPage);
