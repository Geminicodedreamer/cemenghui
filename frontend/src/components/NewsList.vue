<template>
  <div class="news-list">
    <ul>
      <li v-for="item in news" :key="item.id" class="news-item">
        <div class="news-info">
          <h3>{{ item.title }}</h3>
          <p>{{ item.summary }}</p>
          <img :src="item.imagePath" alt="新闻图片" v-if="item.imagePath">
          <p>作者: {{ item.author }}</p>
        </div>
        <div class="news-actions">
          <button @click="edit(item)">修改</button>
          <button @click="remove(item.id)">删除</button>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  props: ['news'],
  methods: {
    edit(newsItem) {
      this.$emit('edit', newsItem)
    },
    remove(newsId) {
      this.$emit('delete', newsId)
    }
  }
}
</script>

<style scoped>
.news-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.news-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #ccc;
  padding-bottom: 10px;
}

.news-info {
  flex-grow: 1;
}

.news-actions {
  display: flex;
  gap: 10px;
}

button {
  padding: 5px 10px;
  cursor: pointer;
}
</style>
