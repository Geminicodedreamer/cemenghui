<template>
  <div>
    <ContentField>
      <div class="container">
        <!-- 搜索区域 -->
        <div class="search-area">
          <div class="row">
            <div class="col">
              <el-input v-model="filters.searchName" placeholder="请输入部门名称" class="form-control" style="width: 300px;">
                <template #prepend>部门名称:</template>
              </el-input>
            </div>
            <div class="col">
              <el-select v-model="filters.searchStatus" placeholder="状态" class="form-control" style="width: 120px;">
                <el-option label="全部" value=""></el-option>
                <el-option label="正常" value="正常"></el-option>
                <el-option label="停用" value="停用"></el-option>
              </el-select>
            </div>
            <div class="col d-flex justify-content-between">
              <el-button type="primary" @click="filterList" class="mr-2">查询</el-button>
              <el-button @click="resetFilters" class="btn btn-secondary ml-2">重置</el-button>
            </div>
          </div>
        </div>
      </div>
      <!-- 按钮操作区域 -->
      <div  v-if="(userType === 'user' && store.state.user.role === '超级管理员')"  class="button-actions my-3">
        <button @click="showAddOrganizationDialogCompany(null)" class="btn btn-success">新增</button>
      </div>

      <!-- 表格区域 -->
      <div class="table-container">
        <el-table :data="filteredTreeData" style="width: 100%" row-key="id" ref="table"
          @selection-change="handleSelectionChange"
          :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
          <el-table-column type="selection" width="50" />
          <el-table-column prop="organizationname" label="部门名称" />
          <el-table-column prop="id" label="排序" />
          <el-table-column prop="status" label="状态" />
          <el-table-column prop="creattime" label="创建时间" />
          <el-table-column label="操作" width="180">
            <template #default="scope">
              <div v-if="(userType === 'user' && store.state.user.role === '超级管理员') ||( userType === 'company' && store.state.user.username === scope.row.organizationname )||( userType === 'user' && store.state.user.companyname === scope.row.organizationname) || ( userType === 'company' && store.state.user.username === scope.row.uporganization) || ( userType === 'user' && store.state.user.companyname === scope.row.uporganization)" class="button-group">
                <el-button v-if="scope.row.uporganization !== null" type="primary" @click="showModifyOrganizationDialog(scope.row)">
                  修改
                </el-button>
                <el-button type="success" @click="showAddOrganizationDialogCompany(scope.row)">
                  新增
                </el-button>
                <el-button v-if="scope.row.uporganization !== null" type="danger" @click="confirmDelete(scope.row)">
                  删除
                </el-button>
                </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </ContentField>

    <AddOrganizationDialog v-model:dialogVisible="isAddOrganizationDialogVisible" :company="selectcompany" @update="handleDataUpdate"/>
    <ModifyOrganizationDialog v-model:dialogVisible="isModifyOrganizationDialogVisible" :organization="currentorganization" @update="handleDataUpdate"/>
  </div>
</template>

<script>
import ContentField from '../../components/ContentField.vue';
import { ref, computed, onMounted, nextTick } from 'vue';
import { ElMessageBox, ElMessage } from 'element-plus';
import { useStore } from 'vuex';
import $ from 'jquery';
import AddOrganizationDialog from '@/components/AddOrganizationDialog';
import ModifyOrganizationDialog from '@/components/ModifyOrganizationDialog';

export default {
  components: {
    ContentField,
    AddOrganizationDialog,
    ModifyOrganizationDialog,
  },
  setup() {
    const store = useStore();
    const userType = localStorage.getItem("userType");
    const list = ref([]);
    const filters = ref({
      searchName: '',
      searchStatus: ''
    });
    const selectedOrganizations = ref([]);
    const isAddOrganizationDialogVisible = ref(false);
    const isModifyOrganizationDialogVisible = ref(false);
    const currentorganization = ref(null);
    const selectcompany = ref(null);
    const fetchOrganizationList = () => {
      $.ajax({
        url: "https://app6457.acapp.acwing.com.cn/api/organization/list",
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp) {
          if (resp && resp.organizations) {
            list.value = resp.organizations;
          }
        },
        error(err) {
          console.error(err);
        }
      });
    };

    const resetFilters = () => {
      filters.value.searchName = '';
      filters.value.searchStatus = '';
      fetchOrganizationList();
    };

    const filterList = () => {
      // This will automatically trigger computed property `filteredTreeData` to recalculate
    };

    const confirmDelete = (organization) => {
      ElMessageBox.confirm('确定删除该部门?')
        .then(() => {
          $.ajax({
            url: `https://app6457.acapp.acwing.com.cn/api/organization/del`,
            type: "get",
            data: { id: organization.id },
            headers: {
              Authorization: "Bearer " + store.state.user.token,
            },
            success: (resp) => {
              if (resp.error_message === 'success') {
                fetchOrganizationList(); // 刷新部门列表
                ElMessage.success('删除成功');
              } else {
                ElMessage.error('删除失败');
              }
            },
            error: () => {
              ElMessage.error('删除失败');
            }
          });
        })
        .catch(() => {});
    };

    const filteredTreeData = computed(() => {
      const filterTree = (data) => {
        return data
          .map((item) => {
            const children = item.children ? filterTree(item.children) : [];
            if (
              (filters.value.searchName === '' || item.organizationname.includes(filters.value.searchName)) &&
              (filters.value.searchStatus === '' || item.status === filters.value.searchStatus)
            ) {
              return { ...item, children };
            }
            if (children.length) {
              return { ...item, children };
            }
            return null;
          })
          .filter(item => item !== null);
      };
      return filterTree(list.value);
    });

    const showModifyOrganizationDialog = item => {
      nextTick(() => {
        currentorganization.value = { ...item };
        isModifyOrganizationDialogVisible.value = true;
      });
    };

    const showAddOrganizationDialogCompany = item =>{
      if(item !== null) selectcompany.value = item.organizationname;
      else selectcompany.value = "";
      showAddOrganizationDialog();
    }

    const showAddOrganizationDialog = () => {
      nextTick(() => {
        isAddOrganizationDialogVisible.value = true;
      });
    };

    const handleDataUpdate = () => {
      fetchOrganizationList(); // 刷新部门列表
    };

    const handleSelectionChange = (selection) => {
      selectedOrganizations.value = selection.map(item => item.id);
    };

    onMounted(() => {
      fetchOrganizationList();
    });

    return {
      list,
      filters,
      isAddOrganizationDialogVisible,
      isModifyOrganizationDialogVisible,
      currentorganization,
      showModifyOrganizationDialog,
      showAddOrganizationDialog,
      handleDataUpdate,
      selectedOrganizations,
      resetFilters,
      filterList,
      confirmDelete,
      filteredTreeData,
      handleSelectionChange,
      store,
      userType,
      showAddOrganizationDialogCompany,
      selectcompany
    };
  }
};
</script>

<style scoped>
img.record-user-photo {
  width: 4vh;
  height: 4vh;
  border-radius: 50%;
}

.search-area {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.label {
  margin-right: 10px;
  font-size: 14px;
  line-height: 32px;
}

.search-input {
  margin-right: 10px;
  flex-grow: 1;
}

.status-select {
  margin-right: 10px;
  width: 150px;
}

.add-button {
  margin-bottom: 20px;
}

.organization-table {
  width: 100%;
}

.table-container {
  max-height: 600px;
  overflow-y: auto;
}

.tree-container {
  max-height: 600px;
  overflow-y: auto;
}

.dialog-footer {
  text-align: right;
}

.d-flex {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.mr-2 {
  margin-right: 10px;
}

.ml-2 {
  margin-left: 10px;
}
.button-group {
  display: flex;
  gap: 10px;
  justify-content: flex-start; /* 使按钮组向左对齐 */
  margin-left: -10px; /* 向左移动按钮组 */
}

.el-button {
  border-radius: 5px;
  font-size: 10px;
  padding: 8px 11px;
}

.el-button.primary {
  background-color: #409eff;
  color: white;
}

.el-button.success {
  background-color: #67c23a;
  color: white;
}

.el-button.danger {
  background-color: #f56c6c;
  color: white;
}
</style>
