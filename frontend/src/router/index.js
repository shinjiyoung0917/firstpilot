import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/components/Main.vue'
import Join from '@/components/join/Join.vue';
import LogIn from '@/components/login/LogIn.vue'
import UserList from '@/components/UserList.vue'; // 테스트

Vue.use(Router)

export default new Router({
  mode: "history",
  routes: [
    {
      path: '/',
      name: 'main',
      component: Main
    },
    {
      path: '/login',
      name: 'login',
      component: LogIn
    },
    {
      path: '/join',
      name: 'join',
      component: Join
    },

    {
      path: '/users',
      name: 'users',
      component: UserList
    }
  ]
})
