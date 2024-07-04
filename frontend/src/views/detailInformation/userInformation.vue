<template>
  <ContentField>
    <div class="container">
      <div class="left-column">
        <div class="card">
          <div class="card-header">
            <h3>个人信息</h3>
          </div>
          <div class="card-body" v-if="user">
            <img v-if="user.photo !== 'No file uploaded' && user.photo !== '' && user.photo !== null" :src="user.photo" alt="User Photo" class="user-photo"/>
            <ul class="user-info">
              <li><i class="fas fa-user"></i> 用户名称: {{ user.userrealname }}</li>
              <li><i class="fas fa-id-card"></i> 昵称: {{ user.username }}</li>
              <li><i class="fas fa-phone"></i> 手机号码: {{ user.telephone }}</li>
              <li><i class="fas fa-building"></i> 所属部门: {{ user.apartmentname }}</li>
              <li><i class="fas fa-calendar-alt"></i> 创建日期: {{ user.createtime }}</li>
              <li><i class="fas fa-envelope"></i> 邮箱: {{ user.email }}</li>
              <li><i class="fas fa-venus-mars"></i> 性别: {{ user.gender }}</li>
              <li><i class="fas fa-briefcase"></i> 角色: {{ user.role }}</li>
              <li><i class="fas fa-building"></i> 公司名称: {{ user.companyname }}</li>
              <li><i class="fas fa-briefcase"></i> 岗位: {{ user.post }}</li>
            </ul>
          </div>
          <div v-else>
            <p>正在加载用户信息...</p>
          </div>
        </div>
      </div>
      <div class="right-column">
        <div class="card">
          <div class="card-header">
            <h3>基本信息</h3>
          </div>
          <div class="card-body">
            <el-form :model="form" label-width="120px">
              <el-form-item label="用户昵称" :required="true">
                <el-input v-model="form.username"></el-input>
              </el-form-item>
              <el-form-item label="手机号" :required="true">
                <el-input v-model="form.telephone"></el-input>
              </el-form-item>
              <el-form-item label="邮箱" :required="true">
                <el-input v-model="form.email"></el-input>
              </el-form-item>
              <el-form-item label="性别" :required="true">
                <el-select v-model="form.gender">
                  <el-option label="男" value="男"></el-option>
                  <el-option label="女" value="女"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitForm">保存</el-button>
                <el-button @click="resetForm">取消</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
        <div class="card">
          <div class="card-header">
            <h3>修改密码</h3>
          </div>
          <div class="card-body">
            <el-form :model="passwordForm" label-width="120px">
              <el-form-item label="当前密码" :required="true">
                <el-input v-model="passwordForm.currentPassword" type="password"></el-input>
              </el-form-item>
              <el-form-item label="新密码" :required="true">
                <el-input v-model="passwordForm.newPassword" type="password"></el-input>
              </el-form-item>
              <el-form-item label="确认新密码" :required="true">
                <el-input v-model="passwordForm.confirmNewPassword" type="password"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitPasswordForm">保存</el-button>
                <el-button @click="resetPasswordForm">取消</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
    </div>
  </ContentField>
</template>

<script>
import axios from 'axios';
import ContentField from '@/components/ContentField.vue';
import $ from 'jquery';
import { useStore } from 'vuex';
import { ref } from 'vue';
import { ElMessage } from 'element-plus';

export default {
  name: 'UserInformation',
  components: {
    ContentField,
  },
  setup() {
    const store = useStore();
    const user = ref(null);
    const form = ref({
      username: '',
      telephone: '',
      email: '',
      gender: '',
      status: 1,
      note: ''
    });

    const passwordForm = ref({
      currentPassword: '',
      newPassword: '',
      confirmNewPassword: ''
    });

    const fetchUserData = () => {
      const token = store.state.user.token;
      if (token) {
        axios.get('https://app6457.acapp.acwing.com.cn/api/user/account/info/', {
          headers: {
            Authorization: 'Bearer ' + token,
          },
        })
        .then(response => {
          if (response.data) {
            user.value = response.data;
            form.value = { ...response.data };
          }
        })
        .catch(error => {
          console.error("There was an error fetching the user data:", error);
        });
      } else {
        console.error("没有找到 token，请确保用户已登录");
      }
    };

    const submitForm = () => {
       $.ajax({
          url: 'https://app6457.acapp.acwing.com.cn/api/user/modify',
          type: 'POST',
          data: {
            userid: form.value.id,
            nickname: form.value.username,
            telephone: form.value.telephone,
            apartmentname: form.value.apartmentname,
            gender: form.value.gender,
            status: 1,
            email: form.value.email,
            role: form.value.role,
            note: form.value.note,
            post: form.value.post,
            photo: form.value.photo,
          },
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          success: (response) => {
            if (response.error_message === 'success') {
              ElMessage.success('信息修改成功');
              fetchUserData(); 
            } else {
              ElMessage.error(response.error_message);
            }
          },
          error: (error) => {
            console.error(error);
            ElMessage.error('提交失败');
          }
        });
    };

    const submitPasswordForm = () => {

      $.ajax({
          url: 'https://app6457.acapp.acwing.com.cn/api/user/password',
          type: 'POST',
          data: {
            id: user.value.id,
            password: passwordForm.value.currentPassword,
            newpassword: passwordForm.value.newPassword,
            confirmnewpassword: passwordForm.value.confirmNewPassword
          },
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          success: (response) => {
             if (response.error_message === 'success') {
                ElMessage.success('密码修改成功');
                resetPasswordForm(); // 清空密码表单
              } else {
                ElMessage.error(response.error_message);
              }
          },
          error: (error) => {
            console.error(error);
            ElMessage.error('提交失败');
          }
        });
    };

    const resetForm = () => {
      form.value = { ...user.value };
    };

    const resetPasswordForm = () => {
      passwordForm.value = {
        currentPassword: '',
        newPassword: '',
        confirmNewPassword: ''
      };
    };

    fetchUserData();

    return {
      user,
      form,
      passwordForm,
      submitForm,
      submitPasswordForm,
      resetForm,
      resetPasswordForm,
    };
  }
};
</script>

<style scoped>
.container {
  display: flex;
  padding: 20px;
  gap: 20px;
  width: 1700px;
}

.left-column {
  width: 30%;
}

.right-column {
  width: 60%;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.card {
  border: 1px solid #e1e1e1;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: 100%;
}

.card-header {
  background: #f5f5f5;
  padding: 10px 20px;
  border-bottom: 1px solid #e1e1e1;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
}

.card-body {
  padding: 20px;
}

.user-photo {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  display: block;
  margin: 0 auto 20px;
}

.user-info {
  list-style: none;
  padding: 0;
  margin: 0;
}

.user-info li {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.user-info i {
  margin-right: 10px;
}
</style>
