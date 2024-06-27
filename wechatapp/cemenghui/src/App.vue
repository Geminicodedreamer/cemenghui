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
      const token = wx.getStorageSync('jwt_token');
      if (token) {
        wx.request({
          url: 'http://127.0.0.1:3000/user/account/info/',
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
