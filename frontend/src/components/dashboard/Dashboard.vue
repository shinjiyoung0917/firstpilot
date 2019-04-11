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

    }
  }
</script>
