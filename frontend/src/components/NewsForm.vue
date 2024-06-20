<template>
  <el-form :model="form" label-width="px">

  </el-form>
</template>

<script>
export default {
  data() {
    return {
      form: {
        newsTitle: '',
        newsDescription: '',
        newsImagePath: '',
        author: '',
        order: ''
      }
    }
  },
  methods: {
    searchNews() {
      // 搜索新闻逻辑
    },
    resetForm() {
      this.form.newsTitle = ''
      this.form.newsDescription = ''
      this.form.newsImagePath = ''
      this.form.author = ''
      this.form.order = ''
    }
  }
}
</script>

<style scoped>
.input-width {
  width: 100%; /* 设置输入框宽度为100% */
}

.form-actions {
  display: flex;
  align-items: center;
}

.el-form-item__label {
  text-align: left; /* 左对齐标签文本 */
}

.form-row {
  margin-bottom: 20px; /* 调整行间距 */
}

.form-col {
  padding-right: 5px; /* 调整列间距 */
}
</style>
