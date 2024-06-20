<template>
  <div>
    <div v-for="news in filteredNews" :key="news.id" class="news-item">
      <h2>{{ news.title }}</h2>
      <p>{{ news.description }}</p>
      <img :src="news.imagePath" alt="新闻图片" />
      <p>作者: {{ news.author }}</p>
      <button @click="showEditForm(news)">修改</button>
      <button @click="deleteNews(news.id)">删除</button>
    </div>
    <div v-if="showForm" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeForm">&times;</span>
        <NewsForm :news="selectedNews" @save="saveNews" @cancel="closeForm" />
      </div>
    </div>
  </div>
</template>

<script>
import NewsForm from './NewsForm.vue';

export default {
  components: {
    NewsForm,
  },
  data() {
    return {
      newsList: [
        { id: 1, title: '新闻标题1', description: '新闻简介1', imagePath: '', author: '作者1' },
        { id: 2, title: '新闻标题2', description: '新闻简介2', imagePath: '', author: '作者2' },
      ],
      searchTitle: '',
      searchDescription: '',
      searchImagePath: '',
      searchAuthor: '',
      showForm: false,
      selectedNews: null,
    };
  },
  computed: {
    filteredNews() {
      return this.newsList.filter(news => {
        return (
          news.title.includes(this.searchTitle) &&
          news.description.includes(this.searchDescription) &&
          news.imagePath.includes(this.searchImagePath) &&
          news.author.includes(this.searchAuthor)
        );
      });
    },
  },
  methods: {
    searchNews() {
      // 因为computed已经完成过滤，所以这里可以为空
    },
    showAddForm() {
      this.selectedNews = null;
      this.showForm = true;
    },
    showEditForm(news) {
      this.selectedNews = { ...news };
      this.showForm = true;
    },
    saveNews(news) {
      if (news.id) {
        const index = this.newsList.findIndex(n => n.id === news.id);
        this.newsList.splice(index, 1, news);
      } else {
        news.id = Date.now();
        this.newsList.push(news);
      }
      this.showForm = false;
    },
    deleteNews(id) {
      this.newsList = this.newsList.filter(news => news.id !== id);
    },
    closeForm() {
      this.showForm = false;
    },
  },
};
</script>

<style>
body {
  font-family: Arial, sans-serif;
}

.news-item {
  background: #fff;
  padding: 15px;
  margin: 10px 0;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.news-item h2 {
  margin: 0 0 10px;
}

.news-item img {
  max-width: 100%;
  height: auto;
}

.modal {
  display: flex;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.5);
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: #fefefe;
  padding: 20px;
  border-radius: 10px;
  width: 80%;
  max-width: 500px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
  animation: fadeIn 0.3s;
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>
