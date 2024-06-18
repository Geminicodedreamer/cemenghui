<template>
  <div class="news-form">
    <input v-model="newsItem.title" placeholder="新闻标题">
    <textarea v-model="newsItem.content" placeholder="新闻内容"></textarea>
    <input v-model="newsItem.author" placeholder="作者">
    <input v-model="newsItem.summary" placeholder="新闻简介">
    <input type="file" @change="onFileChange">
    <button @click="submit">{{ isEditing ? '修改' : '新增' }}资讯</button>
  </div>
</template>

<script>
export default {
  props: ['initialNewsItem', 'isEditing'],
  data() {
    return {
      newsItem: this.initialNewsItem || {
        title: '',
        content: '',
        author: '',
        summary: '',
        imagePath: ''
      }
    }
  },
  methods: {
    submit() {
      this.$emit(this.isEditing ? 'edit' : 'create', this.newsItem)
    },
    onFileChange(event) {
      const file = event.target.files[0]
      this.newsItem.imagePath = URL.createObjectURL(file)
    }
  }
}
</script>

<style scoped>
.news-form {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

button {
  padding: 5px 10px;
  cursor: pointer;
}
</style>
