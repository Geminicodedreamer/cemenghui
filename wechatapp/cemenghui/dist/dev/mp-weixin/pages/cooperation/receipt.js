"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      company: "",
      name: "",
      genderIndex: null,
      genderOptions: ["男", "女"],
      phone: "",
      email: "",
      transportationIndex: null,
      transportationOptions: ["飞机", "高铁", "汽车"],
      arrivalTime: "",
      message: ""
    };
  },
  methods: {
    onGenderChange(e) {
      this.genderIndex = e.detail.value;
    },
    onTransportationChange(e) {
      this.transportationIndex = e.detail.value;
    },
    onArrivalTimeChange(e) {
      this.arrivalTime = e.detail.value;
    },
    submitReceipt() {
      if (!this.company || !this.name || this.genderIndex === null || !this.phone || !this.email || this.transportationIndex === null || !this.arrivalTime) {
        common_vendor.wx$1.showToast({
          title: "请填写所有必填项",
          icon: "none"
        });
        return;
      }
      const receipt = {
        company: this.company,
        name: this.name,
        gender: this.genderOptions[this.genderIndex],
        phone: this.phone,
        email: this.email,
        transportation: this.transportationOptions[this.transportationIndex],
        arrivalTime: this.arrivalTime,
        message: this.message
      };
      console.log("Receipt submitted:", receipt);
      common_vendor.wx$1.showToast({
        title: "提交成功",
        icon: "success",
        duration: 2e3,
        success: () => {
          setTimeout(() => {
            common_vendor.wx$1.navigateBack();
          }, 2e3);
        }
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.company,
    b: common_vendor.o(($event) => $data.company = $event.detail.value),
    c: $data.name,
    d: common_vendor.o(($event) => $data.name = $event.detail.value),
    e: common_vendor.t($data.genderIndex !== null ? $data.genderOptions[$data.genderIndex] : "选择性别"),
    f: $data.genderOptions,
    g: common_vendor.o((...args) => $options.onGenderChange && $options.onGenderChange(...args)),
    h: $data.phone,
    i: common_vendor.o(($event) => $data.phone = $event.detail.value),
    j: $data.email,
    k: common_vendor.o(($event) => $data.email = $event.detail.value),
    l: common_vendor.t($data.transportationIndex !== null ? $data.transportationOptions[$data.transportationIndex] : "选择到达方式"),
    m: $data.transportationOptions,
    n: common_vendor.o((...args) => $options.onTransportationChange && $options.onTransportationChange(...args)),
    o: common_vendor.t($data.arrivalTime ? $data.arrivalTime : "选择到达时间"),
    p: common_vendor.o((...args) => $options.onArrivalTimeChange && $options.onArrivalTimeChange(...args)),
    q: $data.message,
    r: common_vendor.o(($event) => $data.message = $event.detail.value),
    s: common_vendor.o((...args) => $options.submitReceipt && $options.submitReceipt(...args))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-d086d0e7"], ["__file", "C:/Users/Lenovo/Desktop/springboot/wechatapp/cemenghui/src/pages/cooperation/receipt.vue"]]);
wx.createPage(MiniProgramPage);
