<template>
  <view class="app">
    <view class="form">
      <input type="text" v-model="username" placeholder="用户名" />
      <input type="password" v-model="password" placeholder="密码" />
      <button @click="handleLogin">登录</button>
    </view>
    <view class="gohome" @click="goHome">暂不登录</view>
  </view>
</template>

<script>
import { mapActions } from 'vuex';

export default {
  data() {
    return {
      username: '',
      password: ''
    }
  },
  methods: {
    ...mapActions('user', ['login', 'getinfo']),
    goHome() {
      wx.switchTab({
        url: '/pages/index/index'
      });
    },
    handleLogin() {  // 重命名此方法以避免与 Vuex action 冲突
      const {username, password} = this;
      this.login({
        username,
        password,
        success: (resp) => {
          this.getinfo({
            success: () => {
              wx.switchTab({
                url: '/pages/my/my'
              });
            },
            error: () => {
              wx.showToast({
                title: '获取用户信息失败',
                icon: 'none'
              });
            }
          });
        },
        error: () => {
          wx.showToast({
            title: '登录失败',
            icon: 'none'
          });
        }
      });
    }
  }
}
</script>

<style lang="scss" scoped>
.app {
  text-align: center;
  padding: 20px;
}

.form {
  display: flex;
  flex-direction: column;
  align-items: center;

  input {
    width: 80%;
    padding: 10px;
    margin: 10px 0;
    border: 1px solid #ccc;
    border-radius: 5px;
  }

  button {
    width: 80%;
    padding: 10px;
    background-color: #007aff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
}

.gohome {
  margin-top: 20px;
  color: #007aff;
  cursor: pointer;
}
</style>
