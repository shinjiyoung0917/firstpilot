<template>
  <div>

    <div class="site-info masthead bg-primary text-white text-center">

      <div class="container">
        <img class="img-fluid mb-5 d-block mx-auto" src="../assets/profile.png" alt="">
        <h2 class="text-uppercase mb-0">Start Anonymous Board</h2>
        <h1 class="text-uppercase mb-0">Blah Blah</h1>
        <hr class="star-light" id="star-light">
        <h2 class="font-weight-light mb-0">Blah Blah와 함께하세요 !</h2>
      </div>

    </div>

  </div>
</template>

<script>
  export default {
    name: "Main"
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
