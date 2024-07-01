"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      meeting: {},
      formattedContent: [],
      isTourist: false
    };
  },
  onLoad(options) {
    try {
      const userType = common_vendor.wx$1.getStorageSync("userType");
      this.isTourist = userType === "tourist";
      console.log("Encoded meeting data:", options.meeting);
      const meeting = JSON.parse(decodeURIComponent(options.meeting));
      this.meeting = meeting;
      console.log("Parsed meeting data:", this.meeting);
      this.formatContent();
    } catch (error) {
      console.error("Error parsing meeting data:", error);
    }
  },
  methods: {
    navigateToReceipt() {
      const meetingData = encodeURIComponent(JSON.stringify(this.meeting));
      common_vendor.wx$1.navigateTo({
        url: `/pages/cooperation/receipt?meeting=${meetingData}`
      });
    },
    formatContent() {
      if (this.meeting.content) {
        this.formattedContent = this.meeting.content.replace(/<p>/g, '<p class="content-paragraph">');
      }
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $data.meeting.photo,
    b: common_vendor.t($data.meeting.meetingname),
    c: common_vendor.t($data.meeting.creator),
    d: $data.formattedContent,
    e: common_vendor.t($data.meeting.starttime),
    f: common_vendor.t($data.meeting.endtime),
    g: !$data.isTourist
  }, !$data.isTourist ? {
    h: common_vendor.o((...args) => $options.navigateToReceipt && $options.navigateToReceipt(...args))
  } : {});
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-8251b1ed"], ["__file", "C:/Users/DELL/Desktop/springboot/wechatapp/cemenghui/src/pages/cooperation/meetingDetail.vue"]]);
wx.createPage(MiniProgramPage);
