"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      userType: null,
      searchQuery: "",
      news: []
    };
  },
  computed: {
    filteredNews() {
      return this.news.filter(
        (item) => item.title.includes(this.searchQuery) || item.author.includes(this.searchQuery)
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
      common_vendor.wx$1.navigateTo({
        url: `/pages/news/newsDetail?title=${item.title}&source=${item.author}&content=${encodeURIComponent(item.content)}&image=${encodeURIComponent(item.imagePath)}`
      });
    },
    checkLoginStatus() {
      this.userType = common_vendor.wx$1.getStorageSync("userType");
      if (this.userType === "tourist")
        return;
      const token = common_vendor.wx$1.getStorageSync("jwt_token");
      if (!token) {
        common_vendor.wx$1.navigateTo({
          url: "/pages/login/login"
        });
      } else {
        this.$store.dispatch("user/getinfo", {
          success: (res) => {
            console.log("User info fetched successfully:", res);
          },
          error: (err) => {
            console.error("Failed to fetch user info:", err);
            common_vendor.wx$1.navigateTo({
              url: "/pages/login/login"
            });
          }
        });
      }
    },
    fetchNewsList() {
      const token = common_vendor.wx$1.getStorageSync("jwt_token");
      if (!token) {
        this.news = [
          {
            imagePath: "/static/images/image1.jpg",
            title: "【要闻】江苏出台政策措施助推快交工业软件自主创新",
            author: "江苏省快交产业协会中心",
            content: "这是新闻1的详细内容..."
          },
          {
            imagePath: "/static/images/image2.jpg",
            title: "2023博世工业4.0创新（开源）应用典范案例集",
            author: "博世集团",
            content: "这是新闻2的详细内容..."
          },
          {
            imagePath: "/static/images/image3.jpg",
            title: "【政策解读】省委办公厅印发《关于加快工业互联网创新发展的若干政策措施》",
            author: "省委办公厅",
            content: "这是新闻3的详细内容..."
          }
        ];
        return;
      }
      common_vendor.wx$1.request({
        url: "http://localhost:3000/news/list",
        method: "GET",
        header: {
          Authorization: `Bearer ${token}`
        },
        success: (res) => {
          const response = res.data;
          console.log("从服务器获取到新闻列表数据之后立即打印news list:", response);
          if (response.error_message === "success" || response.news) {
            this.news = response.news;
            this.news.join();
            console.log("将响应数据赋值给组件的news数据属性之后打印:", this.news);
          } else {
            console.error("Failed to fetch news list:", response);
          }
        },
        fail: (err) => {
          console.error("Error fetching news list:", err);
        }
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.o((...args) => $options.onInput && $options.onInput(...args)),
    b: common_vendor.f($options.filteredNews, (item, index, i0) => {
      return {
        a: item.imagePath,
        b: common_vendor.t(item.title),
        c: common_vendor.t(item.author),
        d: index,
        e: common_vendor.o(($event) => $options.goToDetail(item), index)
      };
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-e5b8984c"], ["__file", "C:/Users/chenzhanpeng/Desktop/实训/wechatapp/cemenghui/src/pages/news/news.vue"]]);
wx.createPage(MiniProgramPage);
