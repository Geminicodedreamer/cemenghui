<template>
  <ContentField>
    <div class="container">
      <!-- Filter Section -->
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
      <div class="button-actions my-3">
        <button @click="showAddTenantDialog" class="btn btn-success">新增</button>
        <button @click="editTenant" class="btn btn-warning">修改</button>
        <button @click="deleteTenant" class="btn btn-danger">删除</button>
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
            <th>操作</th>
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
            <td>
              <el-button @click="editCompany(company.companyId)" type="primary" :icon="Edit" circle />
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

      <AddCompanyDialog v-model:dialogVisible="isAddTenantDialogVisible" @update-company-list="handleNewCompanyAdded"  />
    </div>
  </ContentField>
</template>

<script>
import ContentField from '../../../components/ContentField';
import AddCompanyDialog from '../../../components/AddCompanyDialog';
import { ElMessage } from 'element-plus';
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
  },
  setup() {
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

    const search = () => {
      pullPage(current_page);
    };

    const resetFilters = () => {
      filters.value = {
        tenantId: '',
        contactPerson: '',
        phone: '',
        tenantName: '',
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
        url: "http://127.0.0.1:3000/company/search/",
        data: {
          page,
          ...filters.value
        },
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
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
      console.log(isAddTenantDialogVisible.value);
    };

    const editTenant = () => {
      // Edit tenant logic here
    };

    const deleteTenant = () => {
      // Delete tenant logic here
    };

    const exportTenants = () => {
    $.ajax({
        url: 'http://127.0.0.1:3000/company/list/',
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

    const editCompany = id => {
      console.log(id);
    };

      const deleteCompany = id => {
        $.ajax({
          url: "http://127.0.0.1:3000/company/del/",
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
                message: 'Tenant deleted successfully',
                type: 'success',
                duration: 2000
              });
            } else {
              ElMessage.error('Error occurred while deleting the tenant');
            }
          },
          error: (resp) => {
            console.error('Error deleting tenant:', resp);
            ElMessage.error('Failed to delete tenant');
          }
        });
      };

    const  handleNewCompanyAdded = (newCompany) => {
    this.companys.push(newCompany);
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
      editTenant,
      deleteTenant,
      exportTenants,
      editCompany,
      deleteCompany,
      Delete,
      Edit,
      Search,
      isAddTenantDialogVisible,
      handleNewCompanyAdded,
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
