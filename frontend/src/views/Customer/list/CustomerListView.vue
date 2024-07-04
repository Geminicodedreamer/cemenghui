<template>
<ContentField>
  <div class="user-management">
    <div class="sidebar">
      <div class="organization-structure">
        <div style="display: flex;padding-bottom:5%;">
          <el-input v-model="departmentSearch" placeholder="请输入部门名称" class="search-input" />&nbsp;
          <el-button type="primary" @click="searchDepartment" circle :icon="Search"></el-button>
        </div>
        <el-tree
          :data="filteredTreeData"
          :props="defaultProps"
          @node-click="handleNodeClick"
          highlight-current
          class="tree-container">
        </el-tree>
      </div>
    </div>
    
    <div class="main-content">
      <div class="filters">
        <el-input v-model="filters.username" placeholder="请输入用户名" class="filter-input" />
        <el-input v-model="filters.phone" placeholder="请输入手机号" class="filter-input" />
        <el-select v-model="filters.status" placeholder="用户状态" class="filter-input">
          <el-option label="正常" value="1"></el-option>
          <el-option label="停用" value="0"></el-option>
        </el-select>
        <el-date-picker
          v-model="filters.createdAt"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          class="filter-input"
        />
        <el-button type="primary" @click="searchUsers">搜索</el-button>
        <el-button @click="resetFilters">重置</el-button>
      </div>
      
      <div v-if="(userType === 'user' && store.state.user.role === '超级管理员') ||(userType === 'company' && store.state.user.username === selectedCompanyName )||(userType === 'user' && store.state.user.companyname === selectedCompanyName)"  class="button-group">
        <el-button type="success" @click="addUser">新增</el-button>
        <el-button type="danger" @click="deleteSelectedUsers">删除</el-button>
        <el-button type="info">导入</el-button>
        <el-button type="warning" @click="exportUsers">导出</el-button>
      </div>
      
      <el-table :data="users" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="userId" label="用户编号"></el-table-column>
        <el-table-column prop="username" label="用户名"></el-table-column>
        <el-table-column prop="nickname" label="用户昵称"></el-table-column>
        <el-table-column prop="apartmentname" label="部门"></el-table-column>
        <el-table-column prop="telephone" label="手机号"></el-table-column>
        <el-table-column prop="status" label="状态">
          <template v-slot="scope">
            <el-switch :disabled="true" v-model="scope.row.status" active-value="1" inactive-value="0" ></el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="createtime" label="创建时间"></el-table-column>
        <el-table-column v-if="(userType === 'user' && store.state.user.role === '超级管理员') ||(userType === 'company' && store.state.user.username === selectedCompanyName )||(userType === 'user' && store.state.user.companyname === selectedCompanyName)" label="操作">
          <template v-slot="scope">
            <el-button v-if="(userType === 'user' && store.state.user.userrealname !== scope.row.username && (store.state.user.role === '超级管理员' || (store.state.user.role !== '超级管理员' && scope.row.role !== '超级管理员'))) || (userType === 'company' && scope.row.role !== '超级管理员')"
            @click="editUser(scope.row)" type="text" size="small">修改</el-button>
            <el-button v-if="(userType === 'user' && store.state.user.userrealname !== scope.row.username && (store.state.user.role === '超级管理员' || (store.state.user.role !== '超级管理员' && scope.row.role !== '超级管理员'))) || (userType === 'company' && scope.row.role !== '超级管理员')"
            @click="deleteUser(scope.row.userId)" type="text" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        @current-change="handlePageChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="total, prev, pager, next"
        :total="totalUsers">
      </el-pagination>
    </div>
    
    <AddUserDialog
      :dialogVisible="addUserDialogVisible"
      :selectedCompanyName="selectedCompanyName"
      :apartmentchoose="apartmentchoose"
      @update="searchUsers"
      @close="closeAddUserDialog"
    />

    <ModifyUserDialog
      :dialogVisible="modifyUserDialogVisible"
      :user="selectedUser"
      :apartmentchoose="apartmentchoose"
      @update="searchUsers"
      @close="closeModifyUserDialog"
    />

  </div>
</ContentField>
</template>

<script>
import ContentField from '@/components/ContentField.vue';
import { ref, onMounted } from 'vue';
import { useStore } from 'vuex';
import { Search } from '@element-plus/icons-vue';
import $ from 'jquery';
import * as XLSX from 'xlsx';
import { ElMessage , ElMessageBox } from 'element-plus';
import AddUserDialog from '@/components/AddUserDialog.vue';
import ModifyUserDialog from '@/components/ModifyUserDialog.vue';

export default {
  components: {
    ContentField,
    AddUserDialog,
    ModifyUserDialog,
  },
  setup() {
    const userType = localStorage.getItem("userType");
    const store = useStore();
    let treeData = ref([
      {
        label: '测盟会',
        children: [],
      }
    ]);
    let filteredTreeData = ref([...treeData.value]);
    let departmentSearch = ref('');
    let filters = ref({
      username: '',
      phone: '',
      status: '',
      createdAt: []
    });
    const addUserDialogVisible = ref(false);
    const selectedCompanyName = ref('');
    const modifyUserDialogVisible =  ref(false);
    const selectedUser = ref(null);
    const apartmentchoose = ref('');
    let users = ref([]);
    let currentPage = ref(1);
    let pageSize = ref(10);
    let totalUsers = ref(0);
    let selectedUsers = ref([]);

    const handleSelectionChange = (selection) => {
      selectedUsers.value = selection;
    };

    const defaultProps = {
      children: 'children',
      label: 'label'
    };

    const addUser = () => {
        openAddUserDialog();
      };

    const openAddUserDialog = () => {
      addUserDialogVisible.value = true;
    };

    const closeAddUserDialog = () => {
      addUserDialogVisible.value = false;
    };

    const handleNodeClick = (data) => {
        selectedCompanyName.value = data.label;
        apartmentchoose.value = data.children;
        console.log(selectedCompanyName.value);
        searchUsers();
      };

    const editUser = (user) => {
      console.log(user);
      selectedUser.value = user;
      modifyUserDialogVisible.value = true;
    };
    const closeModifyUserDialog = () => {
      modifyUserDialogVisible.value = false;
      selectedUser.value = null;
    };

    const fetchCompanies = () => {
      return $.ajax({
        url: "https://app6457.acapp.acwing.com.cn/api/company/list",
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        }
      });
    };

    const fetchOrganizations = () => {
      return $.ajax({
        url: "https://app6457.acapp.acwing.com.cn/api/organization/list",
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        }
      });
    };

    const initTreeData = async () => {
      try {
        const companiesResp = await fetchCompanies();
        const companies = companiesResp.companys;

        const organizationsResp = await fetchOrganizations();
        const organizations = organizationsResp.org;

        companies.forEach(company => {
          const companyNode = {
            label: company.companyName,
            children: []
          };

          organizations.forEach(org => {
            if (org.uporganization === company.companyName) {
              companyNode.children.push({
                label: org.organizationname
              });
            }
          });

          treeData.value[0].children.push(companyNode);
        });
        filteredTreeData.value = JSON.parse(JSON.stringify(treeData.value)); // 初始化过滤后的数据
      } catch (error) {
        console.error("Error fetching tree data:", error);
      }
    };

    const deleteSelectedUsers = () => {
        if (selectedUsers.value.length === 0) {
          ElMessage.warning('请至少选择一个用户进行删除');
          return;
        }

        ElMessageBox.confirm('确定删除选中的用户?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          const deletePromises = selectedUsers.value.map(user => {
            return $.ajax({
              url: `https://app6457.acapp.acwing.com.cn/api/user/del`,
              type: "get",
              data: { id: user.userId },
              headers: {
                Authorization: "Bearer " + store.state.user.token,
              }
            });
          });

          Promise.all(deletePromises)
            .then(() => {
              ElMessage.success('选中的用户已成功删除');
              searchUsers(); // 刷新用户列表
            })
            .catch(error => {
              console.error('批量删除过程中发生错误', error);
              ElMessage.error('批量删除失败');
            });
        }).catch(() => {});
      };


    onMounted(() => {
      initTreeData();
    });


    const searchDepartment = () => {
      if (!departmentSearch.value) {
        filteredTreeData.value = JSON.parse(JSON.stringify(treeData.value));
        return;
      }

      const filterNodes = (nodes) => {
        return nodes.reduce((acc, node) => {
          if (node.label.includes(departmentSearch.value)) {
            acc.push(node);
          } else if (node.children) {
            const filteredChildren = filterNodes(node.children);
            if (filteredChildren.length) {
              acc.push({ ...node, children: filteredChildren });
            }
          }
          return acc;
        }, []);
      };

      filteredTreeData.value = filterNodes(treeData.value);
    };

    const searchUsers = () => {
      const dateFormat = date => {
        const d = new Date(date);
        const yyyy = d.getFullYear();
        const MM = String(d.getMonth() + 1).padStart(2, '0');
        const dd = String(d.getDate()).padStart(2, '0');
        const hh = String(d.getHours()).padStart(2, '0');
        const mm = String(d.getMinutes()).padStart(2, '0');
        const ss = String(d.getSeconds()).padStart(2, '0');
        return `${yyyy}-${MM}-${dd} ${hh}:${mm}:${ss}`;
      };

      let starttime = '';
      let endtime = '';

      if (filters.value.createdAt && filters.value.createdAt.length === 2) {
        starttime = dateFormat(filters.value.createdAt[0]);
        endtime = dateFormat(filters.value.createdAt[1]);
      }

      pullPage(1, starttime, endtime);
    };

    const resetFilters = () => {
      filters.value = {
        username: '',
        phone: '',
        status: '',
        createdAt: []
      };
      searchUsers();
    };

    const pullPage = (page, starttime = '', endtime = '') => {
      let companyname = null;
      if(selectedCompanyName.value === null || selectedCompanyName.value === '测盟会' || selectedCompanyName.value === '') companyname = '';
      else companyname = selectedCompanyName.value;
      $.ajax({
        url: "https://app6457.acapp.acwing.com.cn/api/user/search/",
        data: {
          page,
          ...filters.value,
          companyname: companyname,
          starttime,
          endtime
        },
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          console.log('从后端获取的users:', resp.users); // 调试输出
          users.value = resp.users;
          totalUsers.value = resp.user_count;
        },
        error(resp) {
          console.log(resp);
        }
      });
    };


    const handlePageChange = (page) => {
      pullPage(page);
    };

    const deleteUser = (userId) => {
      if (!userId) {
        console.log("No user ID provided for deletion");
        return;
      }
       ElMessageBox.confirm('确定删除?')
          .then(() => {
              $.ajax({
                url: `https://app6457.acapp.acwing.com.cn/api/user/del`,
                type: "get",
                data:
                {
                  id: userId,
                },
                headers: {
                  Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                  if (resp.error_message === 'success') {
                    searchUsers(); // Refresh the user list after deletion
                    ElMessage.success(`ID ${userId}的用户成功删除`);
                  } else {
                    ElMessage.error(`删除失败 ID ${userId}:`, resp.error_message);
                  }
                },
                error(resp) {
                  ElMessage.error(`发生错误 ID ${userId}:`, resp);
                }
              });}).catch(() => {});

            };



    const exportUsers = () => {
      $.ajax({
        url: 'https://app6457.acapp.acwing.com.cn/api/user/list/',
        type: 'GET',
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success: function(resp) {
          const data = resp.users.map(user => ({
            'User ID': user.userId,
            'Username': user.username,
            'Nickname': user.nickname,
            'Phone': user.telephone,
            'Status': user.status,
            'Created At': user.createtime
          }));

          // Convert JSON data to worksheet
          const worksheet = XLSX.utils.json_to_sheet(data);
          const workbook = XLSX.utils.book_new();
          XLSX.utils.book_append_sheet(workbook, worksheet, "Users");

          // Generate buffer
          const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });

          // Create a Blob object
          const blob = new Blob([excelBuffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8' });

          // Create URL for download
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', 'Users.xlsx');

          // Append link to the body, trigger click, and remove it after download
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
        },
        error: function(err) {
          console.error('Error fetching user data:', err);
        }
      });
    };

    onMounted(() => {
      searchUsers();
    });

    pullPage(currentPage);

    return {
      departmentSearch,
      treeData,
      filteredTreeData,
      defaultProps,
      filters,
      users,
      currentPage,
      pageSize,
      totalUsers,
      handleNodeClick,
      searchDepartment,
      searchUsers,
      resetFilters,
      addUser,
      editUser,
      deleteUser,
      exportUsers,
      handlePageChange,
      Search,
      deleteSelectedUsers,
      handleSelectionChange,
      openAddUserDialog,
      closeAddUserDialog,
      addUserDialogVisible,
      selectedCompanyName,
      apartmentchoose,
      modifyUserDialogVisible,
      selectedUser,
      closeModifyUserDialog,
      store,
      userType
    };
  }
};
</script>

<style scoped>
.user-management {
  display: flex;
}

.sidebar {
  width: 20%;
  padding: 10px;
  border-right: 1px solid #e8e8e8;
}

.organization-structure {
  margin-bottom: 20px;
}

.search-input {
  width: calc(100% - 40px);
}

.tree-container {
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  padding: 10px;
}

.main-content {
  width: 80%;
  padding: 20px;
}

.filters {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.filter-input {
  width: 200px;
}

.button-group {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.el-table {
  margin-bottom: 20px;
}

.el-pagination {
  text-align: right;
}
</style>
