<template>
  <view class="meeting-detail">
    <image :src="meeting.photo" class="meeting-photo"></image>
    <view class="meeting-info">
      <text class="meeting-name">{{ meeting.meetingname }}</text>
      <text class="meeting-creator">创建者: {{ meeting.creator }}</text>
      <view class="meeting-content">
        <text class="section-title">会议内容:</text>
        <rich-text :nodes="formattedContent"></rich-text>
      </view>
      <text class="meeting-time">开始时间: {{ meeting.starttime }}</text>
      <text class="meeting-time">结束时间: {{ meeting.endtime }}</text>
    </view>
    <button v-if="!isTourist" @click="navigateToReceipt" class="receipt-button">填写参会回执</button>
  </view>
</template>

<script>
export default {
  data() {
    return {
      meeting: {},
      formattedContent: [],
      isTourist: false
    }
  },
  onLoad(options) {
    try {
      const userType = wx.getStorageSync("userType");
      this.isTourist = userType === "tourist";
      
      console.log('Encoded meeting data:', options.meeting); // 添加日志
      const meeting = JSON.parse(decodeURIComponent(options.meeting));
      this.meeting = meeting;
      console.log('Parsed meeting data:', this.meeting); // 添加日志
      this.formatContent();
    } catch (error) {
      console.error('Error parsing meeting data:', error);
    }
  },
  methods: {
    navigateToReceipt() {
      const meetingData = encodeURIComponent(JSON.stringify(this.meeting));
      wx.navigateTo({
        url: `/pages/cooperation/receipt?meeting=${meetingData}`
      });
    },
    formatContent() {
      if (this.meeting.content) {
        // 简单的替换处理，如果需要复杂的处理，可以使用正则表达式
        this.formattedContent = this.meeting.content.replace(/<p>/g, '<p class="content-paragraph">');
      }
    }
  }
}
</script>

<style scoped>
.meeting-detail {
  padding: 20px;
}

.meeting-photo {
  width: 100%;
  height: 200px;
  border-radius: 10px;
  object-fit: cover; /* Ensure the image fills the container while maintaining aspect ratio */
  margin-bottom: 20px;
}

.meeting-info {
  display: flex;
  flex-direction: column;
}

.meeting-name {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
}

.meeting-creator, .meeting-time {
  font-size: 18px;
  color: #666;
  margin-bottom: 5px;
}

.meeting-content {
  margin-bottom: 10px;
}

.section-title {
  font-size: 18px;
  color: #666;
}

.content-paragraph {
  font-size: 18px;
  color: #666;
}

.receipt-button {
  width: 100%;
  padding: 10px;
  background-color: #007BFF;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 18px;
  margin-top: 20px;
}
</style>
