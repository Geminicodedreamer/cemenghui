<template>
  <view class="container">
    <scroll-view class="news-detail" scroll-y>
      <view class="news-content">
        <text class="news-title">{{ news.title }}</text>
        <view class="spacer"></view> <!-- 增加空行 -->
        <text class="news-source">来源: {{ news.source }}</text>
        <image class="news-image" :src="news.image" mode="widthFix" />
        <view v-html="processedContent"></view> <!-- 使用 v-html 显示处理后的内容 -->
      </view>
    </scroll-view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      news: {
        image: '',
        title: '',
        source: '',
        content: '',
      },
      processedContent: '', // 存储处理后的内容
    };
  },
  onLoad(options) {
    console.log('Received options:', options); // 添加日志以调试
    this.news.image = decodeURIComponent(options.image) || '';
    this.news.title = options.title || '无标题';
    this.news.source = options.source || '无来源';
    this.news.content = decodeURIComponent(options.content) || '无详细内容';
    this.processContent(); // 处理内容
  },
  methods: {
    processContent() {
      let content = this.news.content;
	  console.log('内容！！:', content); 

      // 移除 <p> 和 </p> 标签
      // content = content.replace(/<\/?p>/g, '');

      // // 将图片路径替换为实际图片显示
      // content = content.replace(/https:\/\/[^\s]+\.(jpg|jpeg|png|gif)/g, (match) => {
      //   return `<image src="${match}" mode="widthFix" style="width: 100%; margin-bottom: 20px;" />`;
      // });

      // // 将特殊字符转义
      // content = content.replace(/&quot;/g, '"')
      //                  .replace(/&amp;/g, '&')
      //                  .replace(/&lt;/g, '<')
      //                  .replace(/&gt;/g, '>')
      //                  .replace(/&nbsp;/g, ' ')
      //                  .replace(/&apos;/g, "'");

      this.processedContent = content;
    },
  },
};
</script>

<style>
.container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f5f5;
}

.news-detail {
  flex: 1;
  padding: 10px;
  background-color: white;
}

.news-content {
  padding: 10px;
}

.news-title {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 10px;
}

.news-source {
  color: #999;
  font-size: 14px;
  text-align: right;
  margin-bottom: 20px;
}

.news-image {
  width: 100%;
  height: auto;
  margin-bottom: 20px;
  border-radius: 5px;
}

.news-detail-content {
  font-size: 16px;
  white-space: pre-line; /* 保持内容中的换行 */
}

.spacer {
  height: 20px; /* 设置空行的高度 */
}
</style>
