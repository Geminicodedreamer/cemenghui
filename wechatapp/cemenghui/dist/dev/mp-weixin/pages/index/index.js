"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      pictures: [
        { id: "1", url: "https://cdn.max-c.com/heybox/dailynews/img/45125a36dcd060618cb89e70d132bfd8.jpg" },
        { id: "2", url: "https://tse2-mm.cn.bing.net/th/id/OIP-C.gICCbkQ2MKPStJn5Bwfb7gHaEo?rs=1&pid=ImgDetMain" },
        { id: "3", url: "https://tse4-mm.cn.bing.net/th/id/OIP-C.SYmgDiCWoD5PidsjvVXHswHaEK?rs=1&pid=ImgDetMain" },
        { id: "4", url: "https://ts1.cn.mm.bing.net/th/id/R-C.e614ae6e42d36f6198d958621eded587?rik=7Pif%2bhY5bv01WQ&riu=http%3a%2f%2fi2.hdslb.com%2fbfs%2farchive%2f395ada4fd33ab5427427a40509fe6fd5ac9da88c.jpg&ehk=F4VNJg1MYgnC49qC4HKAtCKkTTMIEfChYCSz2XfTQmM%3d&risl=&pid=ImgRaw&r=0" },
        { id: "5", url: "https://pic1.zhimg.com/70/v2-15f60b69642ba46964509c51d2645202_1440w.avis?source=172ae18b&biz_tag=Post" }
      ]
    };
  },
  onLoad() {
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
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-83a5a03c"], ["__file", "C:/Users/DELL/Desktop/springboot/wechatapp/cemenghui/src/pages/index/index.vue"]]);
wx.createPage(MiniProgramPage);
