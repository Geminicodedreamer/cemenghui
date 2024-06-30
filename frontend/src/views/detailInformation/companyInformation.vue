<template>
  <ContentField>
    <div class="container">
      <!-- Company Information Section -->
      <div class="card">
        <div class="card-header">
          <h3>公司信息</h3>
        </div>
        <div class="card-body">
          <img :src="company.photo" alt="Company Photo" class="company-photo"/>
          <ul class="company-info">
            <li>
              <i class="fas fa-building"></i> 公司名称: {{ company.companyname }}
            </li>
            <li>
              <i class="fas fa-user-tie"></i> 联系人: {{ company.ownername }}
            </li>
            <li>
              <i class="fas fa-phone"></i> 电话号码: {{ company.telephone }}
            </li>
            <li>
              <i class="fas fa-user-shield"></i> 管理员: {{ company.adminname }}
            </li>
          </ul>
        </div>
      </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField';
import { useStore } from 'vuex';
import { ref } from 'vue';
import $ from 'jquery';

export default {
  components: {
    ContentField,
  },
  setup() {
    const store = useStore();
    const company = ref({
      companyname: '',
      photo: '',
      ownername: '',
      telephone: '',
      adminname: '',
      is_login: false,
    });

    const fetchCompanyInfo = () => {
      const token = store.state.user.token;
      if (token) {
        $.ajax({
          url: "http://127.0.0.1:3000/company/list",
          type: "get",
          headers: {
            Authorization: 'Bearer ' + store.state.user.token,
          },
          success: (resp) => {
            if (resp.error_message === "success") {
              store.commit("updateUser", {
                id: resp.id,
                companyname: resp.companyName,
                photo: resp.photo,
                ownername: resp.ownername,
                telephone: resp.telephone,
                adminname: resp.adminname,
                is_login: true,
              });
              company.value = store.state;
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

    fetchCompanyInfo();

    return {
      company,
    };
  },
};
</script>

<style scoped>
.company-information {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.card {
  border: 1px solid #e1e1e1;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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
</style>
