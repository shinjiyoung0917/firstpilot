<template>
  <div>
    <app-header></app-header>
    <div>

    </div>
    <app-footer></app-footer>
  </div>
</template>

<script>
  import http from "@/http-common"
  import Header from '../layout/Header.vue'
  import Footer from '../layout/Footer.vue'

  export default {
    components: {
      'app-header': Header,
      'app-footer': Footer
    },
    data() {
      return {

      }
    },
    methods: {

    },
    created() {
      if (!sessionStorage.getItem("memberId")) {
        window.alert("로그인이 필요한 서비스입니다.");
        this.$router.push('/login');
      } else {

      }
    }
  }
</script>
