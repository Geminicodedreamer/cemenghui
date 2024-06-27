<template>
  <view class="myContent">
    <!-- 页面内容 -->
  </view>
</template>

<script>
export default {
  data() {
    return {
      // 页面数据
    }
  },
  onShow() {
    this.checkLoginStatus();
  },
  methods: {
    checkLoginStatus() {
      const token = wx.getStorageSync('jwt_token');
      if (!token) {
        wx.navigateTo({
          url: '/pages/login/login'
        });
      } else {
        this.$store.dispatch('user/getinfo', {
          success: (res) => {
            console.log('User info fetched successfully:', res);
          },
          error: (err) => {
            console.error('Failed to fetch user info:', err);
            wx.navigateTo({
              url: '/pages/login/login'
            });
          }
        });
      }
    }
  }
}
</script>

<style scoped>
/* 页面样式 */
</style>
