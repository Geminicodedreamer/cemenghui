"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      news: {
        image: "",
        title: "",
        source: "",
        content: ""
      },
      processedContent: ""
      // 存储处理后的内容
    };
  },
  onLoad(options) {
    console.log("Received options:", options);
    this.news.image = decodeURIComponent(options.image) || "";
    this.news.title = options.title || "无标题";
    this.news.source = options.source || "无来源";
    this.news.content = decodeURIComponent(options.content) || "无详细内容";
    this.processContent();
  },
  methods: {
    processContent() {
      let content = this.news.content;
      console.log("内容！！:", content);
      this.processedContent = content;
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.t($data.news.title),
    b: common_vendor.t($data.news.source),
    c: $data.news.image,
    d: $data.processedContent
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__file", "C:/Users/Lenovo/Desktop/springboot/wechatapp/cemenghui/src/pages/news/newsDetail.vue"]]);
wx.createPage(MiniProgramPage);
