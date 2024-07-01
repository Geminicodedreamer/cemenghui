<template>
  <view class="meeting-list">
    <view v-if="meetings.length === 0" class="meeting-item">
      <view class="meeting-info">
        <text class="meeting-name">暂无会议数据</text>
      </view>
    </view>
    <view v-else>
      <view v-for="(meeting, index) in meetings" :key="index" class="meeting-item" @click="goToDetail(meeting)">
        <image :src="meeting.photo" class="meeting-photo"></image>
        <view class="meeting-info">
          <text class="meeting-name">{{ meeting.meetingname }}</text>
          <text class="meeting-creator">创建者: {{ meeting.creator }}</text>
          <text class="meeting-time">开始时间: {{ meeting.starttime }}</text>
          <text class="meeting-time">结束时间: {{ meeting.endtime }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      meetings: []
    }
  },
  onLoad() {
    this.fetchMeetingList();
  },
  methods: {
    goToDetail(meeting) {
      const meetingData = encodeURIComponent(JSON.stringify(meeting));
      wx.navigateTo({
        url: `/pages/cooperation/meetingDetail?meeting=${meetingData}`
      });
    },
    fetchMeetingList() {
      const userType = wx.getStorageSync("userType");
      
      // 初始化 meetings 数据
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
      
      if (userType === "tourist") return;

      wx.request({
        url: "http://127.0.0.1:3000/meeting/list/",
        method: "GET",
        header: {
          Authorization: `Bearer ${wx.getStorageSync("jwt_token")}`
        },
        success: (resp) => {
          if (resp.errMsg === "request:ok") {
            const newMeetings = resp.data.meetings.map(meeting => ({
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
            wx.showToast({
              title: '获取会议列表失败',
              icon: 'none'
            });
          }
        },
        fail: (err) => {
          wx.showToast({
            title: '请求失败，请检查网络',
            icon: 'none'
          });
          console.error(err);
        }
      });
    },
    formatTime(dateTimeString) {
      // 将时间字符串转换为 Date 对象
      const date = new Date(dateTimeString);
      // 提取年月日时分
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      const hours = date.getHours().toString().padStart(2, '0');
      const minutes = date.getMinutes().toString().padStart(2, '0');
      // 返回格式化后的字符串
      return `${year}-${month}-${day} ${hours}:${minutes}`;
    }
  }
}
</script>

<style scoped>
.meeting-list {
  padding: 20px;
}

.meeting-item {
  display: flex;
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #ccc;
  border-radius: 10px;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  cursor: pointer;
}

.meeting-photo {
  width: 100px;
  height: 100px;
  margin-right: 15px;
  border-radius: 5px;
  object-fit: cover;
}

.meeting-info {
  display: flex;
  flex-direction: column;
}

.meeting-name {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
}

.meeting-creator, .meeting-time {
  font-size: 14px;
  color: #666;
}
</style>
