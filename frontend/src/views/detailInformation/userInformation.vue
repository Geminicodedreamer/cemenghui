<template>
  <ContentField>
    <div class="container">
      <div class="card">
        <div class="card-header">
          <h3>个人信息</h3>
        </div>
        <div class="card-body" v-if="user">
          <img :src="user.photo" alt="User Photo" class="user-photo"/>
          <ul class="user-info">
            <li>
              <i class="fas fa-user"></i> 用户名称: {{ user.username }}
            </li>
            <li>
              <i class="fas fa-id-card"></i> 昵称: {{ user.nickname }}
            </li>
            <li>
              <i class="fas fa-phone"></i> 手机号码: {{ user.telephone }}
            </li>
            <li>
              <i class="fas fa-building"></i> 所属部门: {{ user.apartmentname }}
            </li>
            <li>
              <i class="fas fa-calendar-alt"></i> 创建日期: {{ user.createtime }}
            </li>
          </ul>
        </div>
        <div v-else>
          <p>正在加载用户信息...</p>
        </div>
      </div>
    </div>
  </ContentField>
</template>

<script>
import axios from 'axios';
import ContentField from '../../components/ContentField';
import { useStore } from 'vuex';
import { ref } from 'vue';

export default {
  name: 'UserInformation',
  components: {
    ContentField,
  },
  setup() {
    const store = useStore();
    const user = ref(null);

    const fetchUserData = () => {
      const token = store.state.token;
      if (token) {
        axios.get('http://127.0.0.1:3000/user/list', {
          headers: {
            Authorization: 'Bearer ' + store.state.user.token,
          },
        })
        .then(response => {
          if (response.data) {
            user.value = response.data; // 假设返回的数据就是用户信息
          }
        })
        .catch(error => {
          console.error("There was an error fetching the user data:", error);
        });
      } else {
        console.error("没有找到 token，请确保用户已登录");
      }
    };

    fetchUserData();

    return {
      user,
    };
  }
};
</script>

<style scoped>
.user-information {
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
