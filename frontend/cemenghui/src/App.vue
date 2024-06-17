<template>
  <div>
    <NavBar />
  </div>
</template>

<script>
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap';
import $ from 'jquery';
import NavBar from './components/NavBar.vue'
export default {
  name: 'App',
  components: {
    NavBar,
  },
  setup() {


    $.ajax({
      url: "http://127.0.0.1:3000/user/account/login/",
      type: "post",
      data: {
        username: "yxc2",
        password: "123",
      },
      success(resp) {
        console.log(resp);
      },
      error(resp) {
        console.log(resp);
      }
    })
  }
}
</script>

<style>
</style>
