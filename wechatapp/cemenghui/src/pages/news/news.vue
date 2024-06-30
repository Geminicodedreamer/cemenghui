<template>
  <view class="container">
    <view class="myContent">
      <!-- 页面内容 -->
      <view class="search-bar">
        <input type="text" placeholder="请输入搜索关键词" @input="onInput" />
      </view>
      <view class="news-list">
        <view class="news-item" v-for="(item, index) in filteredNews" :key="index" @click="goToDetail(item)">
          <image class="news-image" :src="item.imagePath" mode="aspectFill" />
          <view class="news-content">
            <text class="news-title">{{ item.title }}</text>
            <text class="news-source">{{ item.author }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userType: null,
      searchQuery: '',
      news: [
	  ]
    };
  },
  computed: {
    filteredNews() {
      return this.news.filter(item => 
        item.title.includes(this.searchQuery) || item.author.includes(this.searchQuery)
      );
    }
  },
  onShow() {
    this.checkLoginStatus();
    this.fetchNewsList();
  },
  methods: {
    onInput(event) {
      this.searchQuery = event.target.value;
    },
    goToDetail(item) {
      wx.navigateTo({
        url: `/pages/news/newsDetail?title=${item.title}&source=${item.author}&content=${encodeURIComponent(item.content)}&image=${encodeURIComponent(item.imagePath)}`
      });
    },
    checkLoginStatus() {
      this.userType = wx.getStorageSync('userType');
      if (this.userType === "tourist") return;
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
    },
    fetchNewsList() {
      const token = wx.getStorageSync("jwt_token");
      if (!token) {
		  this.news=[{
			imagePath: '/static/images/image1.jpg',
			title: '【要闻】江苏出台政策措施助推快交工业软件自主创新',
			author: '江苏省快交产业协会中心',
			content: '这是新闻1的详细内容...'
		  },
		  {
			imagePath: '/static/images/image2.jpg',
			title: '2023博世工业4.0创新（开源）应用典范案例集',
			author: '博世集团',
			content: '这是新闻2的详细内容...'
		  },
		  {
			imagePath: '/static/images/image3.jpg',
			title: '【政策解读】省委办公厅印发《关于加快工业互联网创新发展的若干政策措施》',
			author: '省委办公厅',
			content: '这是新闻3的详细内容...'
		  },];
        return;
      }

      wx.request({
        url: 'http://localhost:3000/news/list',
        method: 'GET',
        header: {
          Authorization: `Bearer ${token}`
        },
        success: (res) => {
          const response = res.data;
          console.log('从服务器获取到新闻列表数据之后立即打印news list:', response); // 添加调试输出
          if (response.error_message === "success" || response.news) {
            this.news = response.news;
			this.news.join();
            console.log('将响应数据赋值给组件的news数据属性之后打印:', this.news); // 添加调试输出
          } else {
            console.error('Failed to fetch news list:', response);
          }
        },
        fail: (err) => {
          console.error('Error fetching news list:', err);
        }
      });
    }
  }
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}

.myContent {
  flex: 1;
}

.search-bar {
  padding: 10px;
  background-color: white;
}

.search-bar input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.news-list {
  flex: 1;
  padding: 10px;
}

.news-item {
  display: flex;
  background-color: white;
  padding: 15px;
  margin-bottom: 10px;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.news-image {
  width: 80px;
  height: 80px;
  margin-right: 10px;
  border-radius: 5px;
}

.news-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.news-title {
  font-size: 16px;
  font-weight: bold;
}

.news-source {
  margin-top: 5px;
  color: #999;
  font-size: 12px;
}
</style>
