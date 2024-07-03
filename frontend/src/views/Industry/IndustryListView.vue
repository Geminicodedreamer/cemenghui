<template>
  <ContentField>
    <div class="container">
      <!-- Filter Section -->
      <div class="filters">
        <div class="row">
          <div class="col">
            <input type="text" v-model="filters.title" class="form-control" placeholder="请输入新闻标题">
          </div>
          <div class="col">
            <input type="text" v-model="filters.order" class="form-control" placeholder="请输入排序">
          </div>
          <div class="col">
            <input type="text" v-model="filters.author" class="form-control" placeholder="请输入作者">
          </div>
          <div class="col">
            <input type="text" v-model="filters.summary" class="form-control" placeholder="请输入新闻简介">
          </div>
          <div class="col">
            <el-button @click="search" :icon="Search" circle />
            &nbsp;
            <button @click="resetFilters" class="btn btn-secondary">重置</button>
          </div>
        </div>
      </div>

      <!-- Button Action Section -->
      <div class="button-actions my-3">
        <button @click="showAddNewsDialog" class="btn btn-success">新增</button>
        &nbsp;
        <button @click="deleteNewss" class="btn btn-danger">删除</button>
        &nbsp;
        <button @click="exportNewss" class="btn btn-info">导出</button>
      </div>
      
      <!-- Table Section -->
      <table class="table table-striped table-hover mt-3" style="text-align: center;">
        <thead>
          <tr>
            <th><input type="checkbox" @click="selectAll" v-model="allSelected">&nbsp;全选</th>
            <th>新闻标题</th>
            <th>作者</th>
            <th>新闻简介</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="News in Newss" :key="News.newsid">
            <td><input v-if="(userType === 'user' && store.state.user.role === '超级管理员') || (userType === 'company' && store.state.user.username === News.tenant) || (userType === 'user' && store.state.user.companyname === News.tenant)" type="checkbox" v-model="selectedNewsIds" :value="News.newsid"></td>
            <td>{{ News.title }}</td>
            <td>{{ News.author }}</td>
            <td>{{ News.summary }}</td>
            <td>
              <el-button v-if="(userType === 'user' && store.state.user.role === '超级管理员') || (userType === 'company' && store.state.user.username === News.tenant) || (userType === 'user' && store.state.user.companyname === News.tenant)" @click="editNews(News)" type="primary" :icon="Edit" circle />
              <el-button v-if="(userType === 'user' && store.state.user.role === '超级管理员') || (userType === 'company' && store.state.user.username === News.tenant) || (userType === 'user' && store.state.user.companyname === News.tenant)" @click="deleteNews(News.newsid)" type="danger" :icon="Delete" circle />
            </td>
          </tr>
        </tbody>
      </table>
      
      <!-- Pagination Section -->
      <nav aria-label="..." class="mt-3">
        <ul class="pagination" style="float: right;">
          <li class="page-item" @click="clickPage(-2)">
            <a class="page-link">前一页</a>
          </li>
          <li :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number" @click="clickPage(page.number)">
            <a class="page-link">{{ page.number }}</a>
          </li>
          <li class="page-item" @click="clickPage(-1)">
            <a class="page-link">后一页</a>
          </li>
        </ul>
      </nav>

      <AddNewsDialog v-model:dialogVisible="isAddNewsDialogVisible" @update="handleDataUpdate"/>
      <ModifyNewsDialog v-model:dialogVisible="isModifyNewsDialogVisible" :News="selectedNews" @update="handleDataUpdate"/>
    </div>
  </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField';
import AddNewsDialog from '../../components/AddNewsDialog.vue';
import ModifyNewsDialog from '../../components/ModifyNewsDialog';
import { ElMessage } from 'element-plus';
import { ElMessageBox } from 'element-plus';
import * as XLSX from 'xlsx';
import { useStore } from 'vuex';
import { ref } from 'vue';
import $ from 'jquery';
import {
  Delete,
  Edit,
  Search,
} from '@element-plus/icons-vue';

export default {
  components: {
    ContentField,
    AddNewsDialog,
    ModifyNewsDialog,
  },
  setup() {
    const store = useStore();
    const userType = localStorage.getItem("userType");
    let Newss = ref([]);
    let selectedNewsIds = ref([]);
    let allSelected = ref(false);
    let current_page = 1;
    let total_Newss = 0;
    let pages = ref([]);
    let filters = ref({
      title: '',
      imagePath: '',
      order: '',
      author: '',
      summary: '',
    });

    const isAddNewsDialogVisible = ref(false);
    const isModifyNewsDialogVisible = ref(false);
    const selectedNews = ref(null);

    const search = () => {
      pullPage(current_page);
    };

    const resetFilters = () => {
      filters.value = {
        title: '',
        imagePath: '',
        order: '',
        author: '',
        summary: '',
      };
      pullPage(current_page);
    };

    const clickPage = (page) => {
      if (page === -2) page = current_page - 1;
      else if (page === -1) page = current_page + 1;
      let max_pages = parseInt(Math.ceil(total_Newss / 10));

      if (page >= 1 && page <= max_pages) {
        pullPage(page);
      }
    };

    const updatePages = () => {
      let max_pages = parseInt(Math.ceil(total_Newss / 10));
      let new_pages = [];
      for (let i = current_page - 2; i <= current_page + 2; i++) {
        if (i >= 1 && i <= max_pages) {
          new_pages.push({
            number: i,
            is_active: i === current_page ? 'active' : '',
          });
        }
      }
      pages.value = new_pages;
    };

    const pullPage = (page) => {
      current_page = page;
      $.ajax({
        url: 'http://127.0.0.1:3000/news/search/',
        data: {
          page: page.toString(), // 确保 page 参数为字符串形式的整数
          title: filters.value.title,
          imagePath: filters.value.imagePath,
          author: filters.value.author,
          summary: filters.value.summary,
        },
        type: 'get',
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success(resp) {
			console.log('从后端获取的news:', resp.news); // 调试输出
          Newss.value = resp.news;
          total_Newss = resp.news_count;
          updatePages();
        },
        error(resp) {
          console.log(resp);
        },
      });
    };

    const selectAll = () => {
      if (!allSelected.value) {
        selectedNewsIds.value = Newss.value.map((News) => News.newsid);
      } else {
        selectedNewsIds.value = [];
      }
      // allSelected.value = !allSelected.value;
    };

    const showAddNewsDialog = () => {
      isAddNewsDialogVisible.value = true;
    };

    const deleteNewss = () => {
      if (selectedNewsIds.value.length === 0) {
        ElMessage.error('请至少选择一条资讯进行删除');
        return;
      }

      ElMessageBox.confirm('确定删除选中的资讯?')
        .then(() => {
          let deletePromises = selectedNewsIds.value.map((newsid) => {
            return $.ajax({
              url: 'http://127.0.0.1:3000/news/del/',
              data: { id: newsid },
              type: 'get',
              headers: {
                Authorization: 'Bearer ' + store.state.user.token,
              },
            });
          });

          Promise.all(deletePromises)
            .then((responses) => {
              responses.forEach((response, index) => {
                if (response.error_message === 'success') {
                  const newsid = selectedNewsIds.value[index];
                  const indexToRemove = Newss.value.findIndex((News) => News.newsid === newsid);
                  if (indexToRemove !== -1) {
                    Newss.value.splice(indexToRemove, 1);
                  }
                } else {
                  ElMessage.error(`资讯ID ${selectedNewsIds.value[index]} 删除失败`);
                }
              });
              ElMessage.success('选中的资讯删除成功');
              pullPage(current_page);
            })
            .catch((error) => {
              console.error('删除过程中发生错误', error);
              ElMessage.error('删除失败');
            });
        })
        .catch(() => {});
    };

    const exportNewss = () => {
      $.ajax({
        url: 'http://127.0.0.1:3000/news/list/',
        type: 'GET',
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success: function (resp) {
          const data = resp.news.map((News) => ({
            'News ID': News.newsid,
            'News Title': News.title,
            'Author': News.author,
            'Summary': News.summary,
          }));

          // Convert JSON data to worksheet
          const worksheet = XLSX.utils.json_to_sheet(data);
          const workbook = XLSX.utils.book_new();
          XLSX.utils.book_append_sheet(workbook, worksheet, 'Newss');

          // Generate buffer
          const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });

          // Create a Blob object
          const blob = new Blob([excelBuffer], {
            type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8',
          });

          // Create URL for download
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', 'Newss.xlsx');

          // Append link to the body, trigger click, and remove it after download
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
        },
        error: function (err) {
          console.error('Error fetching tenant data:', err);
        },
      });
    };

    const editNews = News => {
		console.log('编辑按钮选中的News:', News); // 添加调试输出
      selectedNews.value = { ...News }; // 传递当前选中的资讯信息
      isModifyNewsDialogVisible.value = true;
	pullPage(current_page);
    };

    const deleteNews = (id) => {
      ElMessageBox.confirm('确定删除?')
        .then(() => {
          $.ajax({
            url: 'http://127.0.0.1:3000/news/del/',
            data: { id },
            type: 'get',
            headers: {
              Authorization: 'Bearer ' + store.state.user.token,
            },
            success: (resp) => {
              if (resp.error_message == 'success') {
                const index = Newss.value.findIndex((News) => News.newsid === id);
                if (index !== -1) {
                  Newss.value.splice(index, 1);
                }

                ElMessage({
                  message: '删除成功',
                  type: 'success',
                  duration: 2000,
                });
                pullPage(current_page);
              } else {
                ElMessage.error('发生错误');
              }
            },
            error: () => {
              ElMessage.error('未成功删除');
            },
          });
        })
        .catch(() => {});
    };

    const handleDataUpdate = () => {
      pullPage(current_page); // 调用 pullPage 来刷新页面数据
    };

    pullPage(current_page);

    return {
      Newss,
      selectedNewsIds,
      allSelected,
      pages,
      filters,
      clickPage,
      search,
      resetFilters,
      selectAll,
      showAddNewsDialog,
      deleteNewss,
      exportNewss,
      editNews,
      deleteNews,
      Delete,
      Edit,
      Search,
      isAddNewsDialogVisible,
      handleDataUpdate,
      isModifyNewsDialogVisible,
      selectedNews,
      store,
      userType
    };
  },
};
</script>

<style scoped>
img.record-user-photo {
  width: 4vh;
  height: 4vh;
  border-radius: 50%;
}
</style>
