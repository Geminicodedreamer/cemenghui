<template>
<nav class="navbar navbar-expand-lg navbar-light nav-color bg-primary" data-bs-theme="blue">
  <div class="container">
    <router-link class="navbar-brand" style="font-family:STXingkai;font-size:200%; margin-right:2%;font-weight: normal;" :to="{name: 'home'}">测盟汇</router-link>
    <div class="collapse navbar-collapse" id="navbarText">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <router-link :class="route_name == 'customer' ? 'nav-link active' : 'nav-link'" :to="{name: 'customer'}">用户</router-link>
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
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            {{ $store.state.user.username }}
          </a>
          <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
            <li>

                <router-link class="dropdown-item" :to="{name: '404'}">个人信息</router-link>
            </li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item" href="#" @click="logout">退出</a></li>
          </ul>
        </li>
      </ul>
      <ul class="navbar-nav" v-else-if="!$store.state.user.pulling_info">
        <li class="nav-item">
          <router-link class="nav-link" :to="{name: 'user_account_login' }" role="button">
            登录
          </router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" :to="{name: 'user_account_register'}" role="button">
            注册
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

export default {
    setup() {
        const store = useStore();
        const route = useRoute();
        let route_name = computed(() => route.name)

        const logout = () => {
          store.dispatch("logout");
        }

        return {
            route_name,
            logout
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

</style>