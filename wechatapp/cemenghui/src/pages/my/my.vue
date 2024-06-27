<template>
  <view class="myContent">
    <view class="header">
      <image width="100%" height="100%" class="headbgImage" src="/static/images/weather.jpg"></image>
      <view v-if="is_login">
        <van-image width="100%" height="100%" round class="myPhoto" :src="avatarUrl"></van-image>
        <text class="myInformation">{{nickName}}</text>
      </view>
      <view v-else>
        <van-image width="100%" height="100%" round class="myPhoto" src="/static/images/unlogin.png" @click="login">
        </van-image>
        <text class="myInformation" @click="login">登录</text>
      </view>
    </view>
    <view class="middle-num">
      <view class="oner">
        <view class="zi">
          <text class="zier">{{coupon}}<text class="zisan">%</text></text>
          <text class="quan">资料完善</text>
        </view>
      </view>
      <view class="twoer">|</view>
      <view class="three">
        <view class="zi">
          <text class="zier">{{integral}}\n</text>
          <text class="quan">活跃度</text>
        </view>
      </view>
    </view>
    <view class="mydingdan">
      <view class="row1">
        <van-cell is-link url="" title="用户权益" value="展开" link-type="navigateTo"/>
      </view>
      <view class="zhifu">
        <van-grid :gutter="3" square column-num="5">
          <van-grid-item v-for="(item,key) in icon" :key="key" :url="item.toUrl" :text="item.text" :icon="item.photoSrc" />
        </van-grid>
      </view>
    </view>
    <view class="liebiao">
      <van-cell v-for="(liebiao,key) in lbIcon" :key="key" :icon="liebiao.photoSrc" :title="liebiao.text" :url="liebiao.toUrl">
        <van-icon slot="right-icon" class="custom-icon" />
      </van-cell>
    </view>
  </view>
</template>

<script>
import { mapActions, mapState } from 'vuex';
export default {
  data() {
    return {
      coupon: 6,
      integral: 300,
      icon: [],
      lbIcon:[],
    }
  },
  computed: {
    ...mapState('user', ['is_login', 'username', 'photo']),
    avatarUrl() {
      return this.photo;
    },
    nickName() {
      return this.username;
    }
  },
  onLoad() {
    this.checkLoginStatus();
    //支付导航
    this.tabBarIcon();
    //列表单元格
    this.liebiaoIcon();
  },
  onShow() {
    this.checkLoginStatus();
  },
  methods: {
    ...mapActions('user', ['getinfo', 'logout']),
    checkLoginStatus() {
      const token = wx.getStorageSync('jwt_token');
     if(token) {
        this.getinfo({
          success: (res) => {
            console.log('User info fetched successfully:', res);
          },
          error: (err) => {
            console.error('Failed to fetch user info:', err);
          }
        });
      }
    },
    login() {
      wx.navigateTo({
        url: '/pages/login/login'
      });
    },
    tabBarIcon() {
      var data = {
        "icons": [{
          "photoSrc": "/static/icons/record.png",
          "text": "浏览记录",
          "toUrl": ""
        },
          {
            "photoSrc": "/static/icons/change.png",
            "text": "指标变化",
            "toUrl": ""
          },
          {
            "photoSrc": "/static/icons/time.png",
            "text": "在线时长",
            "toUrl": ""
          },
          {
            "photoSrc": "/static/icons/achievement.png",
            "text": "公司成果",
            "toUrl": ""
          },
          {
            "photoSrc": "/static/icons/more.png",
            "text": "更多",
            "toUrl": ""
          }
        ]
      };
      this.icon = data.icons
    },
    //列表单元格
    liebiaoIcon() {
      var data = {
        "icons": [{
          "photoSrc": "/static/list/info.png",
          "text": "基本信息",
          "toUrl": "/pagesGoods/goods/index"
        },
          {
            "photoSrc": "/static/list/like.png",
            "text": "我的收藏",
            "toUrl": ""
          },
          {
            "photoSrc": "/static/list/shopping.png",
            "text": "商品采购",
            "toUrl": ""
          },
          {
            "photoSrc": "/static/list/myspace.png",
            "text": "我的职位",
            "toUrl": ""
          },
          {
            "photoSrc": "/static/list/about.png",
            "text": "关于我们",
            "toUrl": ""
          },
          {
            "photoSrc": "/static/list/setting.png",
            "text": "设置",
            "toUrl": "/pages/setting/setting"
          }
        ]
      };
      this.lbIcon = data.icons
    },
  }
}
</script>

<style>
page {
  background: #eaeaea;
}

.myContent {
  width: 100%;
  height: 100%;
  margin: 0 auto;
}

.header {
  flex-direction: row;
  display: flex;
  height: 200rpx;
}

.headbgImage {
  height: 25%;
  width: 100%;
  position: absolute;
  z-index: 1;
  border-bottom-right-radius: 70px;
  border-bottom-left-radius: 70px;
}

.myPhoto {
  height: 90rpx;
  width: 90rpx;
  position: absolute;
  z-index: 2;
  margin-left: 3%;
  margin-top: 3%;
  border-radius: 50% 50%;
}

.myInformation {
  position: absolute;
  z-index: 2;
  margin-left: 18%;
  margin-top: 6%;
  text-align: center;
  font-size: large;
  font-weight: bold;
}

.middle-num {
  position: absolute;
  z-index: 2;
  display: flex;
  flex-wrap: nowrap;
  background: white;
  width: 95%;
  margin-left: 20rpx;
  height: 160rpx;
  border-radius: 10px;
  margin-top: -50rpx;
}

.oner {
  flex-grow: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}

.twoer {
  flex-grow: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  color: #eaeaea;
}

.three {
  flex-grow: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}

.zier {
  font-size: 48rpx;
  color: #333333;
}

.zisan {
  font-size: 24rpx;
  color: #333333;
}

.quan {
  font-size: 24rpx;
  color: #999999;
}

.zi {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.mydingdan {
  display: flex;
  flex-wrap: nowrap;
  background: #fff;
  width: 95%;
  height: 250rpx;
  margin-left: 20rpx;
  border-radius: 10px;
  margin-top: 130rpx;
  flex-direction: column;
}

.mydingdan .row1 .van-cell {
  position: inherit;
  border-radius: 10px 10px 0px 0px;
}

.zhifu {
  margin-top: 5rpx
}

.liebiao {
  align-items: center;
  height: 600rpx;
  flex-wrap: nowrap;
  background: #fff;
  width: 91%;
  border-radius: 10px;
  flex-direction: column;
  padding: 2%;
  margin: 20rpx 20rpx 10rpx 20rpx;
}

.liebiao .van-cell {
  height: 16%;
  font-size: 17px;
  align-items: center;
}

.liebiao .van-icon{
  height: 200%;
}
</style>
