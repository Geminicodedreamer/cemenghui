<template>
  <nav class="navbar navbar-expand-lg navbar-light nav-color bg-primary" data-bs-theme="dark">
  <div class="container">
    <router-link class="navbar-brand" style="font-family:STXingkai;font-size:200%; margin-right:2%;" :to="{name:'home'}">测盟汇</router-link>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarText">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <router-link class="nav-link " :to="{name:'home'}">首页</router-link>
        </li>
        <li class="nav-item">
            <router-link class="nav-link " :to="{name:'login'}">用户管理</router-link>
        </li>
        <li class="nav-item">
            <router-link class="nav-link " :to="{name:'login'}">组织管理</router-link>
        </li>
        <li class="nav-item">
            <router-link class="nav-link " :to="{name:'login'}">行业动态管理</router-link>
        </li>
        <li class="nav-item">
            <router-link class="nav-link " :to="{name:'login'}">课程管理</router-link>
        </li>
        <li class="nav-item">
            <router-link class="nav-link " :to="{name:'login'}">会议管理</router-link>
        </li>
       </ul> 
       <!--<li class="nav-item">
          <router-link class="nav-link" :to="{name:'userprofile',params:{userId:1}}">用户动态</router-link>
        </li>-->
      
      <!--取反，如果未登录展示登录与注册-->
       <!-- <ul class="navbar-nav " v-if="!$store.state.user.is_login"> -->
        <ul class="navbar-nav " v-if="!is_login"> 
        <li class="nav-item">
          <router-link class="nav-link " :to="{name:'login'}">登录</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" :to="{name:'register'}">注册</router-link>
        </li>
      </ul>
      <!--登录成功显示姓名和退出，并转到登录界面-->
      <ul class="navbar-nav " v-else>
        <li class="nav-item">
          <!-- <router-link class="nav-link " :to="{name:'userprofile',params:{userId:$store.state.user.id}}">{{$store.state.user.username}}</router-link> -->
        </li>
        <li class="nav-item">
          <router-link class="nav-link" :to="{name:'login'}" style="cursor: pointer;" @click="logout">退出</router-link>
        </li>
      </ul>
      

    </div>
  </div>
</nav>
</template>

<script>
import { useStore } from 'vuex';
export default {
    name:"NavBar",
    //退出删除掉jwt的事件
    setup(){
      let is_login = false;
      const store=useStore();
      const logout=()=>{
        store.commit('logout');
      };
      return {
        logout,
        is_login
      }
    }
    
}
</script>

<style scoped>
.nav-color{
  background-color: rgb(38,38,38);
  font-family:YouYuan;
  font-size: 120%;
}

</style>