<template>
    <ContentField>
        <div class="container">
            <!-- Filter Section -->
            <div class="filters">
                <div class="row">
                    <div class="col">
                        <input type="text" v-model="filters.tenantId" class="form-control" placeholder="请输入租户标识">
                    </div>
                    <div class="col">
                        <input type="text" v-model="filters.contactPerson" class="form-control" placeholder="请输入联系人">
                    </div>
                    <div class="col">
                        <input type="text" v-model="filters.phone" class="form-control" placeholder="请输入电话">
                    </div>
                    <div class="col">
                        <input type="text" v-model="filters.tenantName" class="form-control" placeholder="请输入租户名称">
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
                <button @click="addTenant" class="btn btn-success">新增</button>
                <button @click="editTenant" class="btn btn-warning">修改</button>
                <button @click="deleteTenant" class="btn btn-danger">删除</button>
                <button @click="exportTenants" class="btn btn-info">导出</button>
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
                        <td><img :src="company.photo" class="record-user-photo" alt="company photo" v-if="company.photo !== '' && company.photo !== null"></td>
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
        </div>
    </ContentField>
</template>

<script>
import ContentField from '../../../components/ContentField'
import { useStore } from 'vuex';
import { ref } from 'vue';
import $ from 'jquery';
import {
  Delete,
  Edit,
  Search,
} from '@element-plus/icons-vue'

export default {
    components: {
        ContentField
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
            tenantId: '',
            contactPerson: '',
            phone: '',
            tenantName: ''
        });

        const search = () => {
            pullPage(current_page);
        }

        const resetFilters = () => {
            filters.value = {
                tenantId: '',
                contactPerson: '',
                phone: '',
                tenantName: ''
            };
            pullPage(current_page);
        }

        const clickPage = page => {
            if (page === -2) page = current_page - 1;
            else if (page === -1) page = current_page + 1;
            let max_pages = parseInt(Math.ceil(total_companys / 10));

            if (page >= 1 && page <= max_pages) {
                pullPage(page);
            }
        }

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
        }

        const pullPage = page => {
            current_page = page;
            $.ajax({
                url: "http://127.0.0.1:3000/company/list/",
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
        }

        const selectAll = () => {
            console.log(allSelected.value)
            if (!allSelected.value) {
                selectedCompanies.value = companys.value.map(company => company.companyId);
            } else {
                selectedCompanies.value = [];
            }
        }

        const addTenant = () => {
            // Add tenant logic here
        }

        const editTenant = () => {
            // Edit tenant logic here
        }

        const deleteTenant = () => {
            // Delete tenant logic here
        }

        const exportTenants = () => {
            // Export tenants logic here
        }

        const editCompany = id => {
            console.log(id);
        }

        const deleteCompany = id => {
            console.log(id);
        }

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
            addTenant,
            editTenant,
            deleteTenant,
            exportTenants,
            editCompany,
            deleteCompany,
            Delete,
            Edit,
            Search,
        }
    }
}
</script>

<style scoped>
img.record-user-photo {
    width: 4vh;
    border-radius: 50%;
}
</style>
