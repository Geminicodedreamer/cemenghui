<template>
  <ContentField>
    <div class="container">
      <div class="filters">
        <div class="row">
          <div class="col">
            <input type="text" v-model="filters.symbol" class="form-control" placeholder="请输入租户标识">
          </div>
          <div class="col">
            <input type="text" v-model="filters.ownername" class="form-control" placeholder="请输入联系人">
          </div>
          <div class="col">
            <input type="text" v-model="filters.telephone" class="form-control" placeholder="请输入电话">
          </div>
          <div class="col">
            <input type="text" v-model="filters.companyname" class="form-control" placeholder="请输入租户名称">
          </div>
          <div class="col">
            <el-button @click="search" :icon="Search" circle />
            &nbsp;
            <button @click="resetFilters" class="btn btn-secondary">重置</button>
          </div>
        </div>
      </div>

      <!-- Button Action Section -->
      <div v-if="userType === 'user' && store.state.user.role === '超级管理员'" class="button-actions my-3">
        <button @click="showAddTenantDialog" class="btn btn-success">新增</button>
        &nbsp;
        <button @click="showEditAdminDialog" class="btn btn-warning">修改管理员</button>
        &nbsp;
        <button @click="deleteTenant" class="btn btn-danger">删除</button>
        &nbsp;
        <button @click="exportTenants" class="btn btn-info">导出全部租户数据到excel</button>
      </div>
      
      <!-- Table Section -->
      <table class="table table-striped table-hover mt-3" style="text-align: center;">
        <thead>
          <tr>
            <th><input type="checkbox" @click="selectAll" v-model="allSelected">&nbsp;全选</th>
            <th>租户标识</th>
            <th>企业图标</th>
            <th>联系人</th>
            <th>电话</th>
            <th>租户名称</th>
            <th>管理员</th>
            <th v-if="userType === 'user' && store.state.user.role === '超级管理员'">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="company in companys" :key="company.companyId">
            <td><input type="checkbox" v-model="selectedCompanies" :value="company.companyId"></td>
            <td>序号：{{ company.companyId }} &nbsp; &nbsp;{{ company.symbol }}</td>
            <td><img :src="company.photo" class="record-user-photo" alt="company photo" v-if="company.photo !== 'No file uploaded' && company.photo !== '' && company.photo !== null"></td>
            <td>{{ company.ownername }}</td>
            <td>{{ company.telephone }}</td>
            <td>{{ company.companyName }}</td>
            <td>{{ company.adminname }}</td>
            <td v-if="userType === 'user' && store.state.user.role === '超级管理员'">
               <el-button @click="editCompany(company)" type="primary" :icon="Edit" circle />
              <el-button @click="deleteCompany(company.companyId)" type="danger" :icon="Delete" circle />
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

      <AddCompanyDialog v-model:dialogVisible="isAddTenantDialogVisible" @update="handleDataUpdate"/>
      <ModifyCompanyDialog v-model:dialogVisible="isModifyTenantDialogVisible" :company="selectedCompany" @update="handleDataUpdate"/>

      <el-dialog title="修改管理员" v-model="isEditAdminDialogVisible" @close="resetEditAdminForm">
        <el-form :model="editAdminForm" label-width="120px">
          <el-form-item label="管理员名字" :required="true">
            <el-input v-model="editAdminForm.adminname" placeholder="请输入管理员名字"></el-input>
          </el-form-item>
          <el-form-item class="button-container">
            <el-button type="primary" @click="submitEditAdminForm">提交</el-button>
            <el-button @click="resetEditAdminForm">取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
  </ContentField>
</template>

<script>
import ContentField from '../../../components/ContentField';
import AddCompanyDialog from '../../../components/AddCompanyDialog';
import ModifyCompanyDialog from '../../../components/ModifyCompanyDialog';
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
    AddCompanyDialog,
    ModifyCompanyDialog, 
  },
  setup() {
    const userType = localStorage.getItem('userType');
    const store = useStore();
    let companys = ref([]);
    let selectedCompanies = ref([]);
    let allSelected = ref(false);
    let current_page = 1;
    let total_companys = 0;
    let pages = ref([]);
    let filters = ref({
      symbol: '',
      ownername: '',
      telephone: '',
      companyname: '',
    });

    const isAddTenantDialogVisible = ref(false);
    const isModifyTenantDialogVisible = ref(false);
    const selectedCompany = ref(null);
    const isEditAdminDialogVisible = ref(false); 
   const editAdminForm = ref({
      adminname: ''
    });


    const search = () => {
      pullPage(current_page);
    };

    const resetFilters = () => {
      filters.value = {
        symbol: '',
        ownername: '',
        telephone: '',
        companyname: '',
      };
      pullPage(current_page);
    };

    const clickPage = page => {
      if (page === -2) page = current_page - 1;
      else if (page === -1) page = current_page + 1;
      let max_pages = parseInt(Math.ceil(total_companys / 10));

      if (page >= 1 && page <= max_pages) {
        pullPage(page);
      }
    };

    const updatePages = () => {
      let max_pages = parseInt(Math.ceil(total_companys / 10));
      let new_pages = [];
      for (let i = current_page - 2; i <= current_page + 2; i++) {
        if (i >= 1 && i <= max_pages) {
          new_pages.push({
            number: i,
            is_active: i === current_page ? "active" : "",
          });
        }
      }
      pages.value = new_pages;
    };

    const pullPage = page => {
      current_page = page;
      $.ajax({
        url: "https://app6457.acapp.acwing.com.cn/api/company/search/",
        data: {
          page,
          ...filters.value
        },
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
			console.log('从后端获取的companys:', resp.companys); // 调试输出
          companys.value = resp.companys;
          total_companys = resp.company_count;
          updatePages();
        },
        error(resp) {
          console.log(resp);
        }
      })
    };

    const selectAll = () => {
      if (!allSelected.value) {
        selectedCompanies.value = companys.value.map(company => company.companyId);
      } else {
        selectedCompanies.value = [];
      }
    };

    const showAddTenantDialog = () => {
      isAddTenantDialogVisible.value = true;
      pullPage(current_page);
    };

    const showEditAdminDialog = () => {
      if (selectedCompanies.value.length === 0) {
        ElMessage.error('请至少选择一个租户进行修改');
        return;
      }
      isEditAdminDialogVisible.value = true;
    };


    const deleteTenant = () => {
      if (selectedCompanies.value.length === 0) {
        ElMessage.error('请至少选择一个租户进行删除');
        return;
      }

      ElMessageBox.confirm('确定删除选中的租户?')
        .then(() => {
          let deletePromises = selectedCompanies.value.map(companyId => {
            return $.ajax({
              url: "https://app6457.acapp.acwing.com.cn/api/company/del/",
              data: { id: companyId },
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
                  const companyId = selectedCompanies.value[index];
                  const indexToRemove = companys.value.findIndex(company => company.companyId === companyId);
                  if (indexToRemove !== -1) {
                    companys.value.splice(indexToRemove, 1);
                  }
                } else {
                  ElMessage.error(`租户ID ${selectedCompanies.value[index]} 删除失败`);
                }
              });
              ElMessage.success('选中的租户删除成功');
              pullPage(current_page);
            })
            .catch(error => {
              console.error('删除过程中发生错误', error);
              ElMessage.error('删除失败');
            });
        })
        .catch(() => {});
    };

 const submitEditAdminForm = () => {
      if (!editAdminForm.value || !editAdminForm.value.adminname) {
        ElMessage.error('管理员名字不能为空');
        return;
      }

      const updatePromises = selectedCompanies.value.map(companyId => {
        return $.ajax({
          url: "https://app6457.acapp.acwing.com.cn/api/company/modifyadmin/",
          data: $.param({
            companyid: companyId,
            adminname: editAdminForm.value.adminname,
          }),
          type: "post",
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          contentType: 'application/x-www-form-urlencoded'
        });
      });

      Promise.all(updatePromises)
        .then(responses => {
          responses.forEach((response, index) => {
            if (response.error_message === 'success') {
              const companyId = selectedCompanies.value[index];
              const companyToUpdate = companys.value.find(company => company.companyId === companyId);
              if (companyToUpdate) {
                companyToUpdate.adminname = editAdminForm.value.adminname;
              }
            } else {
              ElMessage.error(`租户ID ${selectedCompanies.value[index]} 更新管理员失败`);
            }
          });
          ElMessage.success('选中的租户管理员更新成功');
          isEditAdminDialogVisible.value = false;
          pullPage(current_page);
        })
        .catch(error => {
          console.error('更新管理员过程中发生错误', error);
          ElMessage.error('更新管理员失败');
        });

        selectedCompanies.value = [];
    };

    const resetEditAdminForm = () => {
      editAdminForm.value = { adminname: '' };
      isEditAdminDialogVisible.value = false;
    };

    const exportTenants = () => {
    $.ajax({
        url: 'https://app6457.acapp.acwing.com.cn/api/company/list/',
        type: 'GET',
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success: function(resp) {
          const data = resp.companys.map(company => ({
            'Tenant ID': company.companyId,
            'Tenant Name': company.companyName,
            'Contact': company.ownername,
            'Phone': company.telephone,
            'Admin Name': company.adminname,
            'Symbol': company.symbol
          }));

          // Convert JSON data to worksheet
          const worksheet = XLSX.utils.json_to_sheet(data);
          const workbook = XLSX.utils.book_new();
          XLSX.utils.book_append_sheet(workbook, worksheet, "Tenants");

          // Generate buffer
          const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });

          // Create a Blob object
          const blob = new Blob([excelBuffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8' });

          // Create URL for download
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', 'Tenants.xlsx');

          // Append link to the body, trigger click, and remove it after download
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
        },
        error: function(err) {
          console.error('Error fetching tenant data:', err);
        }
      });
  };

    const editCompany = company => {
		console.log('编辑按钮选中的company:', company); // 添加调试输出
      selectedCompany.value = { ...company }; // 传递当前选中的公司信息
      isModifyTenantDialogVisible.value = true;
      pullPage(current_page);
    };

      const deleteCompany = id => {
          ElMessageBox.confirm('确定删除?')
          .then(() => {
            $.ajax({
          url: "https://app6457.acapp.acwing.com.cn/api/company/del/",
          data: { id },
          type: "get", 
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          success: (resp) => {
            if (resp.error_message == "success") {
              const index = companys.value.findIndex(company => company.companyId === id);
              if (index !== -1) {
                companys.value.splice(index, 1);
              }

              ElMessage({
                message: '删除成功',
                type: 'success',
                duration: 2000
              });
              pullPage(current_page);
            } else {
              ElMessage.error('发生错误');
            }
          },
          error: () => {
            ElMessage.error('未成功删除');
          }
        });}).catch(() => {})
        
    };
    const handleDataUpdate = () => {
      pullPage(current_page);  // 调用 pullPage 来刷新页面数据
    };

    pullPage(current_page);

    return {
      companys,
      selectedCompanies,
      allSelected,
      pages,
      filters,
      clickPage,
      search,
      resetFilters,
      selectAll,
      showAddTenantDialog,
      deleteTenant,
      exportTenants,
      editCompany,
      deleteCompany,
      Delete,
      Edit,
      Search,
      isAddTenantDialogVisible,
      handleDataUpdate,
      isModifyTenantDialogVisible, 
      selectedCompany,
      showEditAdminDialog,
      submitEditAdminForm,
      resetEditAdminForm,
      isEditAdminDialogVisible,
      editAdminForm,
      userType,
      store
    };
  }
}
</script>

<style scoped>
img.record-user-photo {
  width: 4vh;
  height: 4vh;
  border-radius: 50%;
}
</style>
