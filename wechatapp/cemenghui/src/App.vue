<template>
  <view>
    <slot></slot>
  </view>
</template>

<script>
import store from './store'

export default {
  onLaunch() {
    console.log('App Launch')
    this.checkLoginStatus();
  },
  onShow() {
    console.log('App Show')
    this.checkLoginStatus();
  },
  methods: {
    checkLoginStatus() {
      const userType = wx.getStorageSync("userType");
      if(userType === "tourist") return;
      const token = wx.getStorageSync('jwt_token');
      if (token) {
        wx.request({
          url: 'https://app6457.acapp.acwing.com.cn/api/user/account/info/',
          method: 'GET',
          header: {
            Authorization: `Bearer ${token}`
          },
          success: (res) => {
            if (res.data.error_message === 'success') {
              store.commit('user/updateUser', {
                id: res.data.id,
                username: res.data.username,
                photo: res.data.photo,
                is_login: true
              });
              store.commit('user/updatePullingInfo', false);
            } else {
              store.commit('user/logout');
            }
          },
          fail: () => {
            store.commit('user/logout');
          }
        });
      } else {
        store.commit('user/logout');
      }
    }
  }
}
</script>

<style>
@import "wxcomponents/vant/common/index.wxss";

::-webkit-scrollbar {
  width: 0;
  height: 0;
  background-color: transparent;
}
</style>
