<template>
  <div>
    <app-header></app-header>

    <div class="site-info masthead bg-primary text-white text-center">

      <div class="container">
        <img class="img-fluid mb-5 d-block mx-auto" src="../assets/profile.png" alt="">
        <h1 class="text-uppercase mb-0">Start Anonymous Board</h1>
        <hr class="star-light" id="star-light">
        <h2 class="font-weight-light mb-0">Join Us !</h2>
      </div>

    </div>

    <app-footer></app-footer>
  </div>
</template>

<script>
  import Header from './layout/Header.vue'
  import Footer from './layout/Footer.vue'

  export default {
    name: "Main",
    components: {
      'app-header': Header,
      'app-footer': Footer
    }
  }
</script>

<style>
  .site-info {
    color: blue;
  }

  .bg-primary {
    background-color: #18bc9c!important;
  }

  #star-light:after {
    color: #fff;
    background-color: #18bc9c;
  }

  *, ::after, ::before {
    box-sizing: border-box;
  }
</style>
