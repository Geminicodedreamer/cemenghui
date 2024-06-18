<template>
  <div>
    <h3>{{ newsItem.title }}</h3>
    <p>{{ newsItem.summary }}</p>
    <img :src="newsItem.imagePath" alt="新闻图片">
    <p>作者: {{ newsItem.author }}</p>
    <button @click="edit">修改</button>
    <button @click="remove">删除</button>
  </div>
</template>

<script>
export default {
  props: ['newsItem'],
  methods: {
    edit() {
      this.$emit('edit', this.newsItem)
    },
    remove() {
      this.$emit('delete', this.newsItem.id)
    }
  }
}
</script>
