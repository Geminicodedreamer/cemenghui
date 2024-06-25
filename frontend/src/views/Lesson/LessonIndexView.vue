<template>
  <div>
    <ContentField>
      课程列表
      <div class="search-conditions">
        <div class="form-group">
          <label for="search-id">ID</label>
          <input type="text" class="form-control" id="search-id" v-model="searchId" placeholder="输入ID">
        </div>
        <div class="form-group">
          <label for="search-name">课程名称</label>
          <input type="text" class="form-control" id="search-name" v-model="searchName" placeholder="输入课程名称">
        </div>
        <button class="btn btn-primary" @click="addCourse">新增</button>
        <button class="btn btn-secondary" @click="exportToExcel">导出Excel</button>
        <button class="btn btn-danger" @click="batchDelete">批量删除</button>
        <button class="btn btn-warning" @click="resetFilters">重置</button>
      </div>
      <table class="table">
        <thead>
          <tr>
            <th></th>
            <th>ID</th>
            <th>课程名称</th>
            <th>课程简介</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in paginatedList" :key="item.id">
            <td><input type="checkbox" v-model="selectedItems" :value="item.id"></td>
            <td>{{ item.id }}</td>
            <td v-text="item.LessonName"></td>
            <td>{{ item.LessonIntro }}</td>
            <td>
              <a href="#" @click.prevent="del(item.id)">删除</a>
              <a href="#" @click.prevent="modify(item)">修改</a>
            </td>
          </tr>
        </tbody>
      </table>
      <nav aria-label="Page navigation example">
        <ul class="pagination">
          <li class="page-item" :class="{ disabled: currentPage === 1 }">
            <a class="page-link" href="#" @click.prevent="changePage(currentPage - 1)">Previous</a>
          </li>
          <li class="page-item" v-for="page in totalPages" :key="page" :class="{ active: currentPage === page }">
            <a class="page-link" href="#" @click.prevent="changePage(page)">{{ page }}</a>
          </li>
          <li class="page-item" :class="{ disabled: currentPage === totalPages }">
            <a class="page-link" href="#" @click.prevent="changePage(currentPage + 1)">Next</a>
          </li>
        </ul>
      </nav>
    </ContentField>

    <!-- 弹窗 -->
    <div v-if="showDialog" class="modal">
      <div class="modal-content" style="width: 60%;">
        <span class="close" @click="closeDialog">&times;</span>
        <div v-if="isAdding">
          <lesson-add @close="closeDialog"></lesson-add>
        </div>
        <div v-else>
          <label>课程名称:</label>
          <input type="text" v-model="editingItem.LessonName"><br>
          <label>课程简介:</label>
          <input type="text" v-model="editingItem.LessonIntro"><br>
          <label>课程作者:</label>
          <input type="text" v-model="editingItem.LessonAuthor"><br>
          <label>课程序号:</label>
          <input type="text" v-model="editingItem.LessonNumber"><br>
          <button @click="saveChanges">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import LessonAdd from './LessonAdd.vue';
import * as XLSX from 'xlsx';

export default {
  components: {
    ContentField,
    LessonAdd
  },
  data() {
    return {
      list: [
        { id: 1, LessonName: '高等数学', LessonIntro: '很牛逼的课程，真的巨牛逼，学不明白一点', LessonAuthor: '作者1', LessonNumber: '001' },
        { id: 2, LessonName: '大学英语', LessonIntro: '很牛逼的课程，真的巨牛逼，学不明白一点', LessonAuthor: '作者2', LessonNumber: '002' },
        { id: 3, LessonName: '大英二', LessonIntro: '很牛逼的课程，真的巨牛逼，学不明白一点', LessonAuthor: '作者3', LessonNumber: '003' }
      ],
      searchId: '',
      searchName: '',
      currentPage: 1,
      pageSize: 10,
      showDialog: false,
      isAdding: false,
      editingItem: { id: null, LessonName: '', LessonIntro: '', LessonAuthor: '', LessonNumber: '' },
      selectedItems: []
    }
  },
  computed: {
    filteredList() {
      return this.list.filter(item => {
        return (
          (this.searchId === '' || item.id.toString().includes(this.searchId)) &&
          (this.searchName === '' || item.LessonName.includes(this.searchName))
        )
      });
    },
    totalPages() {
      return Math.ceil(this.filteredList.length / this.pageSize);
    },
    paginatedList() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = this.currentPage * this.pageSize;
      return this.filteredList.slice(start, end);
    }
  },
  methods: {
    del(id) {
      this.list = this.list.filter(item => item.id !== id)
    },
    modify(item) {
      this.editingItem = { id: item.id, LessonName: item.LessonName, LessonIntro: item.LessonIntro, LessonAuthor: item.LessonAuthor, LessonNumber: item.LessonNumber };
      this.showDialog = true;
      this.isAdding = false;
    },
    addCourse() {
      this.showDialog = true;
      this.isAdding = true;
      this.editingItem = { id: null, LessonName: '', LessonIntro: '', LessonAuthor: '', LessonNumber: '' };
    },
    saveChanges() {
      const index = this.list.findIndex(item => item.id === this.editingItem.id);
      if (index !== -1) {
        this.list[index].LessonName = this.editingItem.LessonName;
        this.list[index].LessonIntro = this.editingItem.LessonIntro;
        this.list[index].LessonAuthor = this.editingItem.LessonAuthor;
        this.list[index].LessonNumber = this.editingItem.LessonNumber;
      }
      this.closeDialog();
    },
    closeDialog() {
      this.showDialog = false;
      this.isAdding = false;
      this.editingItem = { id: null, LessonName: '', LessonIntro: '', LessonAuthor: '', LessonNumber: '' };
    },
    changePage(page) {
      if (page > 0 && page <= this.totalPages) {
        this.currentPage = page;
      }
    },
    resetFilters() {
      this.searchId = '';
      this.searchName = '';
    },
    exportToExcel() {
      if (confirm('确定要导出Excel吗？')) {
        const worksheet = XLSX.utils.json_to_sheet(this.filteredList);
        const workbook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(workbook, worksheet, "课程列表");
        XLSX.writeFile(workbook, "课程列表.xlsx");
      }
    },
    batchDelete() {
      if (confirm('确定要批量删除所选课程吗？')) {
        this.selectedItems.forEach(id => {
          this.list = this.list.filter(item => item.id !== id);
        });
        this.selectedItems = [];
      }
    }
  },
  mounted() {
    // 初始化时过滤列表
  }
}
</script>

<style scoped>
.search-conditions {
  margin-bottom: 20px;
}
.search-conditions .form-group {
  margin-bottom: 15px;
}
/* 弹窗样式 */
.modal {
  display: block;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0,0,0,0.4);
}

.modal-content {
  background-color: #fefefe;
  margin: 15% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 80%;
  max-width: 600px; /* 最大宽度限制 */
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
</style>
