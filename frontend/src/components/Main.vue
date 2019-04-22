<template>
  <div>
    <app-header></app-header>

    <div>
      <div class="site-info">

      </div>
    </div>

    <app-footer></app-footer>
  </div>
</template>

<script>
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
