<template>
  <div class="news-management-container">
    <h2>资讯管理</h2>
    <NewsForm />
    <el-form :model="searchForm" label-width="80px" class="search-form">
      <el-row :gutter="8" class="form-row">
        <el-col :span="4" class="form-col">
          <el-form-item label="新闻标题">
            <el-input v-model="searchForm.newsTitle" placeholder="请输入新闻标题" class="input-width"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="5" class="form-col">
          <el-form-item label="新闻图片">
            <el-input v-model="searchForm.newsImagePath" placeholder="请输入新闻图片路径" class="input-width"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="4" class="form-col">
          <el-form-item label="排序">
            <el-input v-model="searchForm.order" placeholder="请输入排序" class="input-width"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="4" class="form-col">
          <el-form-item label="作者">
            <el-input v-model="searchForm.author" placeholder="请输入作者" class="input-width"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="4" class="form-col">
          <el-form-item label="新闻简介">
            <el-input v-model="searchForm.newsDescription" placeholder="请输入新闻简介" class="input-width"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="4" class="form-actions">
          <el-button type="primary" @click="searchNews">搜索</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-col>
      </el-row>
    </el-form>
    <div class="button-group">
      <el-button type="success" @click="addNews">新增资讯</el-button>
      <el-button type="primary" @click="editNews">修改</el-button>
      <el-button type="danger" @click="confirmDeleteSelected">删除</el-button>
      <el-button type="info" @click="exportNews">导出</el-button>
    </div>
    <el-table
      :data="paginatedNewsList"
      style="width: 100%; margin-top: 20px;"
      @selection-change="handleSelectionChange"
      class="news-table"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="newsTitle" label="新闻标题" width="180"></el-table-column>
      <el-table-column prop="author" label="作者" width="180"></el-table-column>
      <el-table-column prop="newsDescription" label="新闻简介"></el-table-column>
      <el-table-column label="操作" width="180">
        <template v-slot="scope">
          <el-button type="primary" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" size="mini" @click="confirmDeleteSingle(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-container">
      <el-pagination
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="totalNews"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="pageSize"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        style="margin-top: 20px;"
      ></el-pagination>
    </div>

    <!-- 引入 AddNewsDialog 和 EditNewsDialog 组件 -->
    <AddNewsDialog v-model:dialogVisible="dialogVisible" />
    <EditNewsDialog v-model:dialogVisible="editDialogVisible" :newsData="currentNews" />
  </div>
</template>

<script>
import NewsForm from '../../components/NewsForm.vue'
import AddNewsDialog from '../../components/AddNewsDialog.vue'
import EditNewsDialog from '../../components/EditNewsDialog.vue'

export default {
  components: {
    NewsForm,
    AddNewsDialog,
    EditNewsDialog
  },
  data() {
    return {
      searchForm: {
        newsTitle: '',
        newsImagePath: '',
        order: '',
        author: '',
        newsDescription: ''
      },
      newsList: [
        { newsTitle: '新闻标题1', author: '作者1', newsDescription: '新闻简介1' },
        { newsTitle: '新闻标题2', author: '作者2', newsDescription: '新闻简介2' },
        { newsTitle: '新闻标题3', author: '作者3', newsDescription: '新闻简介3' },
        // 添加更多新闻条目以测试分页
      ],
      filteredNewsList: [],
      currentPage: 1,
      pageSize: 10,
      totalNews: 0,
      selectedRows: [],
      dialogVisible: false,
      editDialogVisible: false,
      currentNews: {}
    }
  },
  computed: {
    paginatedNewsList() {
      const start = (this.currentPage - 1) * this.pageSize
      const end = start + this.pageSize
      return this.filteredNewsList.slice(start, end)
    }
  },
  methods: {
    searchNews() {
      const { newsTitle, newsImagePath, order, author, newsDescription } = this.searchForm
      this.filteredNewsList = this.newsList.filter(news => {
        return (
          (!newsTitle || news.newsTitle.includes(newsTitle)) &&
          (!newsImagePath || news.newsImagePath.includes(newsImagePath)) &&
          (!order || news.order.includes(order)) &&
          (!author || news.author.includes(author)) &&
          (!newsDescription || news.newsDescription.includes(newsDescription))
        )
      })
      this.totalNews = this.filteredNewsList.length
      this.currentPage = 1
    },
    resetForm() {
      this.searchForm = {
        newsTitle: '',
        newsImagePath: '',
        order: '',
        author: '',
        newsDescription: ''
      }
      this.filteredNewsList = this.newsList
      this.totalNews = this.newsList.length
      this.currentPage = 1
    },
    openDialog() {
      this.dialogVisible = true
    },
    addNews() {
      this.openDialog()
    },
    editNews() {
      // 修改新闻逻辑
    },
    confirmDeleteSelected() {
      this.$confirm('确认删除选中的新闻吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteSelectedNews()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    confirmDeleteSingle(row) {
      this.$confirm('确认删除这条新闻吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.handleDelete(row)
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    deleteSelectedNews() {
      this.newsList = this.newsList.filter(news => !this.selectedRows.includes(news))
      this.filteredNewsList = this.newsList
      this.totalNews = this.newsList.length
      this.selectedRows = []
    },
    handleDelete(row) {
      this.newsList = this.newsList.filter(news => news !== row)
      this.filteredNewsList = this.newsList
      this.totalNews = this.newsList.length
    },
    exportNews() {
      // 导出新闻逻辑
    },
    handleEdit(row) {
      this.currentNews = row
      this.editDialogVisible = true
    },
    handleSizeChange(newSize) {
      this.pageSize = newSize
    },
    handleCurrentChange(newPage) {
      this.currentPage = newPage
    },
    handleSelectionChange(val) {
      this.selectedRows = val
    }
  },
  mounted() {
    this.filteredNewsList = this.newsList
    this.totalNews = this.newsList.length
  }
}
</script>

<style scoped>
.news-management-container {
  background-color: white; /* 背景色设置为白色 */
  padding: 20px; /* 内边距 */
  border-radius: 8px; /* 圆角 */
}

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
  padding-right: 8px; /* 调整列间距 */
}

.button-group {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
}

.search-form {
  background-color: white; /* 背景色设置为白色 */
  padding: 10px; /* 内边距 */
  border-radius: 8px; /* 圆角 */
}

.news-table {
  background-color: white; /* 背景色设置为白色 */
  border-radius: 8px; /* 圆角 */
}
</style>
