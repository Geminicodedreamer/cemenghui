<template>
  <view>
    <button class="logout-btn" @click="handleLogout">退出登录</button>
  </view>
</template>

<script>
import { mapActions } from 'vuex';

export default {
  methods: {
    ...mapActions('user', ['logout']),
    handleLogout() {
      this.logout();
      // 清除本地存储的用户信息
      wx.removeStorageSync('jwt_token');
      wx.removeStorageSync("userType");
      wx.removeStorageSync("username");
      // 更新页面数据
      this.$store.commit('user/logout');
      // 跳转到登录页面
      wx.switchTab({
        url: '/pages/my/my'
      });
    }
  }
}
</script>

<style scoped lang="scss">
.logout-btn {
  margin-top: 40px;
}
</style>
