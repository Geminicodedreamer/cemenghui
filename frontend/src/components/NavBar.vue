<template>
<nav class="navbar navbar-expand-lg navbar-light nav-color bg-primary" data-bs-theme="blue">
  <div class="container">
    <router-link class="navbar-brand" style="font-family:STXingkai;font-size:200%; margin-right:2%;font-weight: normal;" :to="{name: 'home'}">测盟汇</router-link>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarText">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              信息管理
            </a>
            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
              <li>
                  <router-link class="dropdown-item" :to="{name: 'customer'}">用户</router-link>
              </li>
              <li><hr class="dropdown-divider"></li>
              <li>
                  <router-link class="dropdown-item" :to="{name: 'company'}">企业租户</router-link>
              </li>
            </ul>
        </li>
        <li class="nav-item">
          <router-link :class="route_name == 'organization' ? 'nav-link active' : 'nav-link'" :to="{name: 'organization'}">组织</router-link>
        </li>
        <li class="nav-item">
          <router-link :class="route_name == 'industry' ? 'nav-link active' : 'nav-link'" :to="{name: 'industry'}">行业动态</router-link>
        </li>
        <li class="nav-item">
          <router-link :class="route_name == 'lesson' ? 'nav-link active' : 'nav-link'" :to="{name: 'lesson'}">课程管理</router-link>
        </li>
        <li class="nav-item">
          <router-link :class="route_name == 'meeting' ? 'nav-link active' : 'nav-link'" :to="{name: 'meeting'}">会议管理</router-link>
        </li>
      </ul>
      <ul class="navbar-nav" v-if="$store.state.user.is_login">
        <li>
          <img :src="$store.state.user.photo" class="record-user-photo" v-if="$store.state.user.photo !== 'No file uploaded' && $store.state.user.photo !== '' && $store.state.user.photo !== null">
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            {{ $store.state.user.username }}
          </a>
          <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
              <li>
                <a class="dropdown-item" @click="handleAccountInfoRoute">详细信息</a>
              </li>
              <li><hr class="dropdown-divider"></li>
              <li><a class="dropdown-item"  @click="logout">退出</a></li>
            </ul>
        </li>
      </ul>
      <ul class="navbar-nav" v-else-if="!$store.state.user.pulling_info">
        <li class="nav-item">
          <router-link class="nav-link" :to="{name: 'user_account_login' }" role="button">
            管理员登录
          </router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" :to="{name: 'company_account_login' }" role="button">
            企业登录
          </router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" :to="{name: 'company_account_register'}" role="button">
            企业注册
          </router-link>
        </li>
      </ul>
    </div>
  </div>
</nav>
</template>

<script>
import { useRoute } from 'vue-router'
import { computed } from 'vue'
import { useStore } from 'vuex';
import router from '../router/index'

export default {
    setup() {
        const store = useStore();
        const route = useRoute();
        let route_name = computed(() => route.name)

        const logout = () => {
          store.dispatch("logout");
          router.push({name : 'home'});
        }

        const handleAccountInfoRoute = () => {
          const userType = localStorage.getItem("userType");
          const routeName = userType === "user" ? 'user_account_info' : 'company_account_info';
          router.push({ name: routeName });
        }

        return {
            route_name,
            logout,
            handleAccountInfoRoute
        }
    }
}
</script>

<style scoped>
.nav-color{
  font-family:YouYuan;
  font-size: 120%;
  font-weight: bolder;
  color:white;
}

.navbar-nav .nav-link,
.navbar-brand,
.navbar-nav {
  color: white !important;
}

.navbar-nav .nav-link.active {
  color: yellow !important; /* Active 链接的颜色设置为黄色 */
  font-weight: bold; /* Active 链接加粗 */
}

img.record-user-photo {
  width: 6vh;
  height: 6vh;
  border-radius: 50%;
}

</style>