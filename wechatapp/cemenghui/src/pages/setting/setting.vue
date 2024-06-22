<template>
  <view>
    <button class="logout-btn" @click="logout">退出登录</button>
  </view>
</template>

<script>
export default {
  methods: {
    // 登出方法
    logout() {
      // 清除用户登录状态
      wx.removeStorageSync('userInfo');
      // 更新页面数据
      this.is_login = false;
      this.avatarUrl = '';
      this.nickName = '';
      // 跳转到登录页面
      wx.switchTab({
        url: '/pages/my/my'
      });
    },
  }
}
</script>


<style scoped lang="scss">
.logout-btn{
  margin-top: 40px;
}
</style>
