// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
<<<<<<< HEAD

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  //router,
  //render: h => h(App)
=======
import BootstrapVue from 'bootstrap-vue';
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.use(BootstrapVue)
Vue.config.productionTip = false
//Vue.prototype.$EventBus = new Vue();

/* eslint-disable no-new */
new Vue({
>>>>>>> 3c2c93843f40de0a2904254b047576ff12a55626
  el: '#app',
  router,
  components: { App },
  template: '<App/>'

});
//.$mount("#app");
