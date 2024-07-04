<template>
  <ContentField>
    <div class="container">
      <!-- Company Information Section -->
      <div class="left-column">
        <div class="card">
          <div class="card-header">
            <h3>公司信息</h3>
          </div>
          <div class="card-body">
            <img v-if="company.photo !== 'No file uploaded' && company.photo !== '' && company.photo !== null" :src="company.photo" alt="Company Photo" class="company-photo"/>
            <ul class="company-info">
              <li><i class="fas fa-building"></i> 公司名称: {{ company.username }}</li>
              <li><i class="fas fa-user-tie"></i> 联系人: {{ company.ownername }}</li>
              <li><i class="fas fa-phone"></i> 电话号码: {{ company.telephone }}</li>
              <li><i class="fas fa-user-shield"></i> 管理员: {{ company.adminname }}</li>
            </ul>
          </div>
        </div>
      </div>
      <div class="right-column">
        <div class="card">
          <div class="card-header">
            <h3>修改公司信息</h3>
          </div>
          <div class="card-body">
            <el-form :model="form" label-width="120px">
              <el-form-item label="公司名称" :required="true">
                <el-input v-model="form.username"></el-input>
              </el-form-item>
              <el-form-item label="联系人" :required="true">
                <el-input v-model="form.ownername"></el-input>
              </el-form-item>
              <el-form-item label="电话号码" :required="true">
                <el-input v-model="form.telephone"></el-input>
              </el-form-item>
              <el-form-item label="公司图片" class="form-item">
                <el-upload
                  class="avatar-uploader"
                  action="https://app6457.acapp.acwing.com.cn/api/upload"
                  :show-file-list="false"
                  :on-success="handleAvatarSuccess"
                  :before-upload="beforeAvatarUpload">
                  <template #trigger>
                    <div class="upload-trigger">
                      <div v-if="!form.photo" ><Plus style="width:20px;color: #dcdfe6;"/></div>
                      <img v-if="form.photo" :src="form.photo" class="avatar">
                    </div>
                  </template>
                </el-upload>
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
import ContentField from '@/components/ContentField.vue';
import $ from 'jquery';
import { useStore } from 'vuex';
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { Plus } from '@element-plus/icons-vue'; // 引入图标

export default {
  name: 'CompanyInformation',
  components: {
    ContentField,
    Plus,
  },
  setup() {
    const store = useStore();
    const company = ref({
      username: '',
      photo: '',
      ownername: '',
      telephone: '',
      adminname: '',
    });

    const form = ref({
      companyname: '',
      ownername: '',
      telephone: '',
      adminname: '',
      photo: '',
    });

    const passwordForm = ref({
      currentPassword: '',
      newPassword: '',
      confirmNewPassword: ''
    });

    const fetchCompanyInfo = () => {
      const token = store.state.user.token;
      if (token) {
        $.ajax({
          url: "https://app6457.acapp.acwing.com.cn/api/company/account/info/",
          type: "get",
          headers: {
            Authorization: 'Bearer ' + store.state.user.token,
          },
          success: (resp) => {
            if (resp.error_message === "success") {
              company.value = resp;
              form.value = { ...resp };
            }
          },
          error: (resp) => {
            console.error("公司信息获取失败", resp);
          }
        });
      } else {
        console.error("没有找到 token，请确保用户已登录");
      }
    };

    const submitForm = () => {
      $.ajax({
        url: 'https://app6457.acapp.acwing.com.cn/api/company/modify',
        type: 'POST',
        data: {
          companyid: form.value.id,
          companyname: form.value.username,
          photo: form.value.photo,
          ownername: form.value.ownername,
          telephone: form.value.telephone,
          note: form.value.note,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success: (response) => {
          if (response.error_message === 'success') {
            ElMessage.success('信息修改成功');
            fetchCompanyInfo();
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
        url: 'https://app6457.acapp.acwing.com.cn/api/company/password',
        type: 'POST',
        data: {
          id: company.value.id,
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
            resetPasswordForm();
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
      form.value = { ...company.value };
    };

    const resetPasswordForm = () => {
      passwordForm.value = {
        currentPassword: '',
        newPassword: '',
        confirmNewPassword: ''
      };
    };

    const handleAvatarSuccess = (response) => {
      form.value.photo = response.url; // 假设后端返回的图片 URL 在 response.url 中
    };

    const beforeAvatarUpload = (file) => {
      const isImage = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isImage) {
        ElMessage.error('上传图片只能是 JPG/PNG/GIF 格式!');
      }
      if (!isLt2M) {
        ElMessage.error('上传图片大小不能超过 2MB!');
      }
      return isImage && isLt2M;
    };

    fetchCompanyInfo();

    return {
      company,
      form,
      passwordForm,
      submitForm,
      submitPasswordForm,
      resetForm,
      resetPasswordForm,
      handleAvatarSuccess,
      beforeAvatarUpload,
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

.company-photo {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  display: block;
  margin: 0 auto 20px;
}

.company-info {
  list-style: none;
  padding: 0;
  margin: 0;
}

.company-info li {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.company-info i {
  margin-right: 10px;
}

.upload-trigger {
  width: 120px;
  height: 120px;
  border: 1px dashed #dcdfe6;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}
</style>
