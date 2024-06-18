<template>
  <div class="news-management">
    <h1>资讯管理</h1>
    <div class="search-area">
      <input v-model="searchQuery.title" placeholder="新闻标题">
      <input v-model="searchQuery.summary" placeholder="新闻简介">
      <input v-model="searchQuery.imagePath" placeholder="新闻图片路径">
      <input v-model="searchQuery.author" placeholder="作者">
      <button @click="searchNews">搜索</button>
    </div>
    <div class="news-action">
      <button @click="createNews">新增资讯</button>
    </div>
    <NewsList :news="news" @edit="editNews" @delete="deleteNews" />
  </div>
</template>

<script>
import NewsList from '@/components/NewsList.vue'

export default {
  name: 'NewsManagement',
  components: { NewsList },
  data() {
    return {
      searchQuery: {
        title: '',
        summary: '',
        imagePath: '',
        author: ''
      },
      news: [
        {
          id: 1,
          title: '新闻标题1',
          summary: '新闻简介1',
          imagePath: 'path/to/image1.jpg',
          author: '作者1',
          content: '新闻内容1'
        },
        {
          id: 2,
          title: '新闻标题2',
          summary: '新闻简介2',
          imagePath: 'path/to/image2.jpg',
          author: '作者2',
          content: '新闻内容2'
        }
      ]
    }
  },
  methods: {
    searchNews() {
      console.log('搜索查询:', this.searchQuery)
    },
    createNews() {
      const newNews = {
        id: this.news.length + 1,
        title: '新新闻标题',
        summary: '新新闻简介',
        imagePath: '',
        author: '新作者',
        content: '新新闻内容'
      }
      this.news.push(newNews)
    },
    editNews(newsItem) {
      const index = this.news.findIndex(item => item.id === newsItem.id)
      if (index !== -1) {
        this.news.splice(index, 1, newsItem)
      }
    },
    deleteNews(newsId) {
      this.news = this.news.filter(item => item.id !== newsId)
    }
  }
}
</script>

<style scoped>
.news-management {
  padding: 20px;
}

.search-area {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.news-action {
  margin-bottom: 20px;
}

button {
  padding: 5px 10px;
  cursor: pointer;
}
</style>
