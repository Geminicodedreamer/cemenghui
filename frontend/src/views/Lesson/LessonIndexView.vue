<template>
  <div>
    <ContentField>
      <h2>课程列表</h2>
      <div class="search-conditions">
        <div class="form-group">
          <label for="search-id">ID</label>
          <input type="text" class="form-control" id="search-id" v-model="searchId" placeholder="输入ID">
        </div>
        <div class="form-group">
          <label for="search-name">课程名称</label>
          <input type="text" class="form-control" id="search-name" v-model="searchName" placeholder="输入课程名称">
        </div>
      </div>
      <div class="button-group">
        <button class="btn btn-danger btn-sm" @click="deleteTenant">删除</button>
        <button class="btn btn-warning btn-sm" @click="resetFilters">重置</button>
        <button class="btn btn-primary btn-sm" @click="showAddCourseDialog">新增课程</button>
        <button class="btn btn-success btn-sm" @click="exportToExcel">导出Excel</button>
      </div>
      <table class="table">
        <thead>
          <tr>
            <th><input type="checkbox" @change="selectAll" :checked="allSelected">&nbsp;全选</th>
            <th>ID</th>
            <th>课程名称</th>
            <th>课程简介</th>
            <th>课程作者</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in paginatedList" :key="item.id">
            <td><input v-if="(userType === 'user' && store.state.user.role === '超级管理员') || (userType === 'company' && store.state.user.username === item.companyname) || (userType === 'user' && store.state.user.companyname === item.companyname)" type="checkbox" v-model="selectedLessons" :value="item.id"></td>
            <td>{{ item.id }}</td>
            <td>{{ item.lessonname }}</td>
            <td>{{ item.lessonintro }}</td>
            <td>{{ item.lessonauthor }}</td>
            <td>
              <button v-if="(userType === 'user' && store.state.user.role === '超级管理员') || (userType === 'company' && store.state.user.username === item.companyname) || (userType === 'user' && store.state.user.companyname === item.companyname)" class="btn btn-primary btn-sm" @click.prevent="showModifyCourseDialog(item)">修改</button>
              &nbsp;
              <button v-if="(userType === 'user' && store.state.user.role === '超级管理员') || (userType === 'company' && store.state.user.username === item.companyname) || (userType === 'user' && store.state.user.companyname === item.companyname)" class="btn btn-danger btn-sm" @click.prevent="del(item.id)">删除</button>
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

    <!-- 课程新增和修改对话框 -->
    <AddCourseDialog v-model:dialogVisible="isAddCourseDialogVisible" @update="handleDataUpdate"/>
    <ModifyCourseDialog v-model:dialogVisible="isModifyCourseDialogVisible" :lesson="currentLesson" @update="handleDataUpdate"/>
  </div>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import AddCourseDialog from '../../components/AddCourseDialog.vue';
import ModifyCourseDialog from '../../components/ModifyCourseDialog.vue';
import * as XLSX from 'xlsx';
import { ref, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { useStore } from 'vuex';
import $ from 'jquery';
import { ElMessageBox } from 'element-plus';

export default {
  components: {
    ContentField,
    AddCourseDialog,
    ModifyCourseDialog
  },
  setup() {
    const store = useStore();
    const list = ref([]);
    const searchId = ref('');
    const searchName = ref('');
    const currentPage = ref(1);
    const pageSize = 10;
    const selectedLessons = ref([]);
    const isAddCourseDialogVisible = ref(false);
    const isModifyCourseDialogVisible = ref(false);
    const currentLesson = ref(null);
    const userType = localStorage.getItem("userType");

    const fetchLessonList = () => {
      $.ajax({
        url: "http://127.0.0.1:3000/lesson/list",
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp && resp.lessons) {
            list.value = resp.lessons;
            console.error(list.value);
          }
        },
        error(err) {
          console.error(err);
        }
      });
    };

    const resetFilters = () => {
      searchId.value = '';
      searchName.value = '';
      fetchLessonList();
    };

    const deleteTenant = () => {
      if (selectedLessons.value.length === 0) {
        ElMessage.error('请至少选择一个课程进行删除');
        return;
      }

      ElMessageBox.confirm('确定删除选中的课程?')
        .then(() => {
          let deletePromises = selectedLessons.value.map(id => {
            return $.ajax({
              url: "http://127.0.0.1:3000/lesson/del/",
              data: { id: id },
              type: "get",
              headers: {
                Authorization: "Bearer " + store.state.user.token,
              }
            });
          });

          Promise.all(deletePromises)
            .then(responses => {
              responses.forEach((response, index) => {
                if (response.error_message === 'success') {
                  const lessonId = selectedLessons.value[index];
                  const indexToRemove = list.value.findIndex(lesson => lesson.id === lessonId);
                  if (indexToRemove !== -1) {
                    list.value.splice(indexToRemove, 1);
                  }
                } else {
                  ElMessage.error(`课程ID ${selectedLessons.value[index]} 删除失败`);
                }
              });
              selectedLessons.value = []; // 清空选中的课程
              ElMessage.success('选中的课程删除成功');
              fetchLessonList();
            })
            .catch(error => {
              console.error('删除过程中发生错误', error);
              ElMessage.error('删除失败');
            });
        })
        .catch(() => {});
    };

    const filteredList = computed(() => {
      return list.value.filter(item => {
        return (
          (searchId.value === '' || item.id.toString().includes(searchId.value)) &&
          (searchName.value === '' || item.lessonname.includes(searchName.value))
        );
      });
    });

    const totalPages = computed(() => {
      return Math.ceil(filteredList.value.length / pageSize);
    });

    const paginatedList = computed(() => {
      const start = (currentPage.value - 1) * pageSize;
      const end = currentPage.value * pageSize;
      return filteredList.value.slice(start, end);
    });

    const allSelected = computed(() => {
      return paginatedList.value.length > 0 && paginatedList.value.every(item => selectedLessons.value.includes(item.id));
    });

    const del = id => {
      ElMessageBox.confirm('是否删除').then(() =>{
      $.ajax({
        url: `http://127.0.0.1:3000/lesson/del`,
        data: { id },
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success: (resp) => {
          if (resp.error_message === "success") {
            list.value = list.value.filter(item => item.id !== id);
            ElMessage({
              message: '删除成功',
              type: 'success',
              duration: 2000
            });
          } else {
            ElMessage.error('删除失败');
          }
        },
        error: () => {
          ElMessage.error('删除失败');
        }
      });});
    };

    const showModifyCourseDialog = item => {
      console.error(item);
      currentLesson.value = { ...item }; // 将选中的课程信息传递给对话框
      isModifyCourseDialogVisible.value = true;
    };

    const showAddCourseDialog = () => {
      isAddCourseDialogVisible.value = true;
    };

    const handleDataUpdate = () => {
      fetchLessonList(); // 刷新课程列表
    };

    const changePage = page => {
      if (page > 0 && page <= totalPages.value) {
        currentPage.value = page;
      }
    };

    const selectAll = () => {
      if (allSelected.value) {
        paginatedList.value.forEach(item => {
          const index = selectedLessons.value.indexOf(item.id);
          if (index > -1) {
            selectedLessons.value.splice(index, 1);
          }
        });
      } else {
        paginatedList.value.forEach(item => {
          if (!selectedLessons.value.includes(item.id)) {
            selectedLessons.value.push(item.id);
          }
        });
      }
    };

    const exportToExcel = () => {
      const worksheet = XLSX.utils.json_to_sheet(filteredList.value);
      const workbook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(workbook, worksheet, "课程列表");
      XLSX.writeFile(workbook, "课程列表.xlsx");
    };

    onMounted(() => {
      fetchLessonList();
    });

    return {
      list,
      searchId,
      searchName,
      currentPage,
      pageSize,
      isAddCourseDialogVisible,
      isModifyCourseDialogVisible,
      currentLesson,
      filteredList,
      totalPages,
      paginatedList,
      del,
      showModifyCourseDialog,
      showAddCourseDialog,
      handleDataUpdate,
      changePage,
      exportToExcel,
      selectAll,
      selectedLessons,
      deleteTenant,
      resetFilters,
      allSelected,
      userType,
      store,
    };
  }
}
</script>

<style scoped>
.search-conditions {
  margin-bottom: 20px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
.search-conditions .form-group {
  flex: 1;
}
.button-group {
  display: flex;
  gap: 10px;
  margin-top: 10px;
  flex-wrap: wrap;
}
</style>
