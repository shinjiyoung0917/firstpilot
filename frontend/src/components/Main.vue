<template>
  <div>
<<<<<<< HEAD
    <h1>--- 메인 페이지 ---</h1>
    <div class="site-info">
      <router-link class="btn btn-primary" to="/login"> 로그인 버튼 </router-link>
      <router-view/>
    </div>
    <div class="site-info">
      <router-link class="btn btn-primary" to="/join"> 회원가입 버튼 </router-link>
      <router-view/>
    </div>
    <div class="site-info">
      <router-link class="btn btn-primary" to="/users"> 사용자 조회 버튼 (테스트용) </router-link>
      <router-view/>
    </div>
=======
    <app-header></app-header>

    <div>
      <div class="site-info">

      </div>
    </div>

    <app-footer></app-footer>
>>>>>>> 3c2c93843f40de0a2904254b047576ff12a55626
  </div>
</template>

<script>
<<<<<<< HEAD
    export default {
        name: "Main"
    }
=======
  import http from "@/http-common"
  import Header from './layout/Header.vue'
  import Footer from './layout/Footer.vue'

  export default {
    name: "Main",
    components: {
      'app-header': Header,
      'app-footer': Footer
    },
    data() {
      return {
        email: '',
        username: '',
        password: ''
      }
    },
    methods: {
      main() {

      }
    },
    mounted() {
      this.main();
    }
  }
>>>>>>> 3c2c93843f40de0a2904254b047576ff12a55626
</script>

<style>
  .site-info {
    color: blue;
    margin-bottom: 20px;
  }

  .btn-primary {
    margin-right: 5px;
  }
</style>
