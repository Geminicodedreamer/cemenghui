<template>
  <view class="container">
    <swiper indicator-dots="true" indicator-color="#fff" indicator-active-color="#00BBFF" class="banner" autoplay="true">
      <swiper-item v-for="item in pictures" :key="item.id">
        <image @tap="onPreviewImage(item.url)" :src="item.url" class="banner-image"></image>
      </swiper-item>
    </swiper>

    <view class="warning">
      <text class="title-text">
        测盟汇
      </text>
    </view>

    <view class="content">
      <view class="news-section">
        <view class="news-header">
          <text class="news-title">技术交流会</text>
          <text class="news-date">2023.08</text>
        </view>
        <text class="news-description">温州新世纪技术创新基地基础软硬件产品质量监督交流论坛</text>
      </view>

      <view class="association-section">
        <view class="association-title">中国电子医器管理协会</view>
        <view class="association-subtitle">计算机软硬件和信息系统质量监督评价分会</view>
        <view class="association-details">
          计算机软硬件和信息系统质量监督评价分会是中国电子医器管理协会的分支机构，负责对全国计算机软硬件和信息系统的质量进行监督评价，旨在提高产品质量，促进产业发展。
        </view>
      </view>
    </view>
    <view class="logos-section">
      <view class="logo-item" v-for="logo in logos" :key="logo.id">
        <image :src="logo.url" class="logo-image"></image>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      pictures: [
        { id: '1', url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/2cc62ab6ff7d4f79ae43748eac23fa66.jpg" },
        { id: '2', url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/f7f356b83e324a60885eff0d6031fb8d.jpg" },
        { id: '3', url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/8638899dedff419ea39e1f762e54ad74.png" },
        { id: '4', url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/6c3cc46b865d4874b9add0fb4bb67c38.jpg" },
        { id: '5', url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/a9ff39927e63409b9cc59cd7e11a0cbe.jpg" },
      ],
      logos: [
        { id: '1', url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/fd8d470f0a9a4521af5ec26fc325968e.png" },
        { id: '2', url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/b5f98982c1ce4a3bb5afa68e05161cc9.png" },
        { id: '3', url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/0f2562de773f457e9390237a76c89342.png" },
        { id: '4', url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/366a023a6287403c8e0f3ec0b7129606.png" },
        { id: '5', url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/2d4ca4280f0a461aa70d415c14888e40.png" },
        { id: '6', url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/d70a284945f34598a03fb4168ef4cd60.png" },
        { id: '7', url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/ea399a055b794113824c4cecee84ab96.png" },
        { id: '8', url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/1176ff9f9a6546deb156d28c214229fa.png" },
        { id: '9', url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/bb1209d3296a4edfb42853b798c744ca.png" },
        { id: '10', url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/8d18380a6b134b5ebefd34cbffa67f84.png" },
        { id: '11', url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/8efb856e4a2245dfad2f36694f5d9ae9.png" },
        { id: '12', url: "https://wybcemenghui.oss-cn-beijing.aliyuncs.com/87d07c7b7ee244e7a0ec740f6317a79f.png" }
      ]
    }
  },
  methods: {
    onPreviewImage(url) {
      uni.previewImage({
        urls: this.pictures.map(v => v.url),
        current: url
      });
    }
  }
}
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}

.banner {
  width: 750rpx;
  height: 450rpx;
  margin-top: 15px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.banner-image {
  width: 95%;
  height: 100%;
  margin-left: 2.5%;
  border-radius: 35px;
}

.warning {
  margin-top: 20px;
  text-align: center;
}

.title-text {
  font-family: 'STXingkai';
  font-size: 60px;
  color: #00BBFF;
}

.content {
  padding: 20px;
  background-color: white;
  border-radius: 10px;
  margin: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.news-section {
  margin-bottom: 20px;
}

.news-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.news-title {
  font-size: 36rpx;
  font-weight: bold;
}

.news-date {
  font-size: 28rpx;
  color: #888;
}

.news-description {
  font-size: 28rpx;
}

.association-section {
  margin-bottom: 20px;
}

.association-title {
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 10px;
}

.association-subtitle {
  font-size: 28rpx;
  color: #888;
  margin-bottom: 10px;
}

.association-details {
  font-size: 28rpx;
}

.association-details view {
  margin-bottom: 10px;
}

.logos-section {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
  margin-top: 20px;
}

.logo-item {
  width: 80rpx;
  height: 80rpx;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 10px;
}

.logo-image {
  width: 100%;
  height: 100%;
}
</style>
