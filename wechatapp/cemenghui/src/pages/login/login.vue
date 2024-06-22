<template>
  <view class="app">
    <view class="imgDiv">
      <image :src="userInfo.avatarUrl"></image>
    </view>
    <view>{{userInfo.nickName}}</view>
    <button @click="getUserProfile">
      微信登录
    </button>
    <view class="gohome" @click="goHome">暂不登录</view>
  </view>
</template>

<script>
const config = {
  appid: 'wx1665b7f96eb29d8b',
  appSecret: '10e511717ad8fe349211c7efc82812d9'
}
export default {
  data() {
    return {
      userInfo: {}
    }
  },
  methods: {
    goHome() {
      uni.switchTab({
        url: '/pages/index/index'
      });
    },
    async getUserProfile() {
      wx.getUserProfile({
        desc: "完善用户信息",
        success: (infoRes) => {
          const { userInfo } = infoRes;
          this.userInfo = userInfo;
          console.log(JSON.stringify(userInfo, null, 2));

          // 存储用户信息到本地存储
          wx.setStorageSync('userInfo', userInfo);

          // 跳转到 my 页面
          wx.switchTab({
            url: '/pages/my/my'
          });
        }
      });
    },
    async getOpenId() {
      const { appid, appSecret } = config;
      const { code } = await wx.login();
      // 微信服务地址
      let url =
          'https://api.weixin.qq.com/sns/jscode2session?appid=' +
          appid +
          '&secret=' + appSecret + '&js_code=' +
          code + '&grant_type=authorization_code';

      wx.request({
        url,
        success: result => {
          const { openid } = result.data;
          console.log('openid', openid);
        },
        fail(err) {
          console.log(err.message)
        }
      });
    },
  }
}
</script>

<style lang="scss" scoped>
.app {
  text-align: center;
  padding: 20px;
}

.imgDiv {
  width: 200rpx;
  height: 200rpx;
  margin: 50px auto;

  image {
    width: 100%;
    height: 100%;
    border-radius: 50%;
  }
}
</style>
