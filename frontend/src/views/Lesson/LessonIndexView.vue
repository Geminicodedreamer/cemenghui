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
        <button class="btn btn-primary" @click="filterList">查询</button>
        <button class="btn btn-primary" @click="addCourse">新增</button>
        <button class="btn btn-secondary" @click="exportToExcel">导出Excel</button>
        <button class="btn btn-warning" @click="resetFilters">重置</button>
      </div>
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>课程名称</th>
            <th>课程简介</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in paginatedList" :key="item.id">
            <td>{{ item.id }}</td>
            <td v-text="item.LessonName"></td>
            <td>{{ item.LessonIntro }}</td>
            <td>
              <a href="#" @click.prevent="del(item.id)">删除</a>
            </td>
            <td>
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
  </div>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import * as XLSX from 'xlsx';

export default {
  components: {
    ContentField
  },
  data() {
    return {
      list: [
        { id: 1, LessonName: '高等数学', LessonIntro: '很牛逼的课程，真的巨牛逼，学不明白一点' },
        { id: 2, LessonName: '大学英语', LessonIntro: '很牛逼的课程，真的巨牛逼，学不明白一点' },
        { id: 3, LessonName: '大英二', LessonIntro: '很牛逼的课程，真的巨牛逼，学不明白一点' }
      ],
      searchId: '',
      searchName: '',
      currentPage: 1,
      pageSize: 10
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
      this.$router.push('lessonModify');
      let newLessonName = prompt("修改课程名称", item.LessonName);
      if (newLessonName !== null && newLessonName !== "") {
        item.LessonName = newLessonName;
      }
      let newLessonIntro = prompt("修改课程简介", item.LessonIntro);
      if (newLessonIntro !== null && newLessonIntro !== "") {
        item.LessonIntro = newLessonIntro;
      }
    },
    changePage(page) {
      if (page > 0 && page <= this.totalPages) {
        this.currentPage = page;
      }
    },
    filterList() {
      this.currentPage = 1; // 查询后重置为第一页
    },
    resetFilters() {
      this.searchId = '';
      this.searchName = '';
      this.filterList();
    },
    addCourse() {
      this.$router.push('lessonAdd'); // 假设你已经在路由中配置了lesson-add路径
    },
    exportToExcel() {
      const worksheet = XLSX.utils.json_to_sheet(this.filteredList);
      const workbook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(workbook, worksheet, "课程列表");
      XLSX.writeFile(workbook, "课程列表.xlsx");
    }
  },
  mounted() {
    this.filterList(); // 初始化时过滤列表
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
</style>
