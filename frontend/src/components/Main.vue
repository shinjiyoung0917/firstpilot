<template>
  <div>
    <app-header></app-header>
    <div>
      <div class="site-info">
        <router-link class="btn btn-primary" to="/members"> 사용자 조회 버튼 (테스트용) </router-link>
        <router-view/>
      </div>
    </div>
    <app-footer></app-footer>
  </div>
</template>

<script>
  import http from "@/http-common"
  //import axios from 'axios'
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
        /*http.get('/', bodyFormData)
          .then((res) => {

          }).catch((e) => {
          window.alert(e);
          console.log(e);
        });*/
      }
    },
    mounted() {
      this.main();
    }
  }
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
