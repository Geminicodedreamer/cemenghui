<template>
  <view class="myContent">
    <!-- 页面内容 -->
  </view>
</template>

<script>
export default {
  data() {
    return {
      userType: null,
    }
  },
  onShow() {
    this.checkLoginStatus();
  },
  methods: {
    checkLoginStatus() {
      this.userType = wx.getStorageSync('userType');
      if(this.userType === "tourist") return;
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
