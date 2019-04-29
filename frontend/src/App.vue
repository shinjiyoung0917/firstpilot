<template>

  <div id="app" class="container-fluid">

    <div class="site-info">
    </div>

    <router-view/>
  </div>

</template>

<script>
  import Header from './components/layout/Header.vue'
  import Footer from './components/layout/Footer.vue'

  export default {
    name: 'App',
    components: {
      'app-header': Header,
      'app-footer': Footer
    }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
.site-info {
  color: blue;
  margin-bottom: 20px;
}

.btn-primary {
  margin-right: 5px;
}

.container-fluid {
  text-align: center;
}
</style>
