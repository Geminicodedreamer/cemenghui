"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      meetings: []
    };
  },
  onLoad() {
    this.fetchMeetingList();
  },
  methods: {
    goToDetail(meeting) {
      const meetingData = encodeURIComponent(JSON.stringify(meeting));
      common_vendor.wx$1.navigateTo({
        url: `/pages/cooperation/meetingDetail?meeting=${meetingData}`
      });
    },
    fetchMeetingList() {
      const userType = common_vendor.wx$1.getStorageSync("userType");
      this.meetings = [
        {
          meetingid: 1,
          meetingname: "测试会议 1",
          creator: "测试创建者 1",
          content: "这是一个测试会议的简介 1。",
          starttime: "2024-07-01 10:00",
          endtime: "2024-07-01 12:00",
          photo: "https://cdn.max-c.com/heybox/dailynews/img/45125a36dcd060618cb89e70d132bfd8.jpg"
        },
        {
          meetingid: 2,
          meetingname: "测试会议 2",
          creator: "测试创建者 2",
          content: "这是一个测试会议的简介 2。",
          starttime: "2024-07-02 14:00",
          endtime: "2024-07-02 16:00",
          photo: "../../static/images/Genshin.jpg"
        },
        {
          meetingid: 3,
          meetingname: "测试会议 3",
          creator: "测试创建者 3",
          content: "这是一个测试会议的简介 3。",
          starttime: "2024-07-03 09:00",
          endtime: "2024-07-03 11:00",
          photo: "https://cdn.max-c.com/heybox/dailynews/img/45125a36dcd060618cb89e70d132bfd8.jpg"
        }
      ];
      if (userType === "tourist")
        return;
      common_vendor.wx$1.request({
        url: "http://127.0.0.1:3000/meeting/list/",
        method: "GET",
        header: {
          Authorization: `Bearer ${common_vendor.wx$1.getStorageSync("jwt_token")}`
        },
        success: (resp) => {
          if (resp.errMsg === "request:ok") {
            const newMeetings = resp.data.meetings.map((meeting) => ({
              meetingid: meeting.id,
              meetingname: meeting.meetingname,
              creator: meeting.creator,
              starttime: this.formatTime(meeting.starttime),
              endtime: this.formatTime(meeting.endtime),
              photo: meeting.photo,
              content: meeting.content
            }));
            this.meetings = this.meetings.concat(newMeetings);
          } else {
            common_vendor.wx$1.showToast({
              title: "获取会议列表失败",
              icon: "none"
            });
          }
        },
        fail: (err) => {
          common_vendor.wx$1.showToast({
            title: "请求失败，请检查网络",
            icon: "none"
          });
          console.error(err);
        }
      });
    },
    formatTime(dateTimeString) {
      const date = new Date(dateTimeString);
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, "0");
      const day = date.getDate().toString().padStart(2, "0");
      const hours = date.getHours().toString().padStart(2, "0");
      const minutes = date.getMinutes().toString().padStart(2, "0");
      return `${year}-${month}-${day} ${hours}:${minutes}`;
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $data.meetings.length === 0
  }, $data.meetings.length === 0 ? {} : {
    b: common_vendor.f($data.meetings, (meeting, index, i0) => {
      return {
        a: meeting.photo,
        b: common_vendor.t(meeting.meetingname),
        c: common_vendor.t(meeting.creator),
        d: common_vendor.t(meeting.starttime),
        e: common_vendor.t(meeting.endtime),
        f: index,
        g: common_vendor.o(($event) => $options.goToDetail(meeting), index)
      };
    })
  });
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-5605a37e"], ["__file", "C:/Users/DELL/Desktop/springboot/wechatapp/cemenghui/src/pages/cooperation/meetingList.vue"]]);
wx.createPage(MiniProgramPage);
