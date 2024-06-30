<template>
<ContentField>
  <div class="user-management">
    <div class="sidebar">
      <div class="organization-structure">
        <el-input v-model="departmentSearch" placeholder="请输入部门名称" class="search-input" />
        <el-tree
          :data="treeData"
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
          <el-option label="Active" value="active"></el-option>
          <el-option label="Inactive" value="inactive"></el-option>
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
      
      <div class="button-group">
        <el-button type="primary" @click="addUser">新增</el-button>
        <el-button type="success" @click="editUser">修改</el-button>
        <el-button type="danger" @click="deleteUser">删除</el-button>
        <el-button type="info" >导入</el-button>
        <el-button type="warning" @click="exportUsers">导出</el-button>
      </div>
      
      <el-table :data="users" style="width: 100%">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="用户编号" width="120"></el-table-column>
        <el-table-column prop="username" label="用户名"></el-table-column>
        <el-table-column prop="nickname" label="用户昵称"></el-table-column>
        <el-table-column prop="department" label="部门"></el-table-column>
        <el-table-column prop="phone" label="手机号"></el-table-column>
        <el-table-column prop="status" label="状态">
          <template v-slot="scope">
            <el-switch v-model="scope.row.status" active-value="active" inactive-value="inactive"></el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间"></el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-button @click="editUser(scope.row)" type="text" size="small">修改</el-button>
            <el-button @click="deleteUser(scope.row)" type="text" size="small">删除</el-button>
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
  </div>
  </ContentField>
</template>

<script>
import ContentField from '@/components/ContentField.vue';

export default {
  components:{
    ContentField,
  },
  data() {
    return {
      departmentSearch: '',
      treeData: [
        {
          label: '深圳总公司',
          children: [
            { label: '研发部门' },
            { label: '市场部门' },
            { label: '测试部门' },
            { label: '财务部门' },
            { label: '运维部门' }
          ]
        },
        {
          label: '长沙分公司',
          children: [
            { label: '市场部门' },
            { label: '财务部门' }
          ]
        }
      ],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      filters: {
        username: '',
        phone: '',
        status: '',
        createdAt: []
      },
      users: [],
      currentPage: 1,
      pageSize: 10,
      totalUsers: 0
    };
  },
  methods: {
    handleNodeClick(data) {
      console.log(data);
      // Handle node click to filter users by department
    },
    searchUsers() {
      // Implement search logic here
    },
    resetFilters() {
      this.filters = {
        username: '',
        phone: '',
        status: '',
        createdAt: []
      };
      this.searchUsers();
    },
    addUser() {
      // Implement add user logic here
    },
    editUser(user) {
      console.log(user);
    },
    deleteUser(user) {
      console.log(user);
    },
    exportUsers() {
    },
    handlePageChange(page) {
      this.currentPage = page;
      this.searchUsers();
    }
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
  margin-bottom: 10px;
  width: 100%;
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
