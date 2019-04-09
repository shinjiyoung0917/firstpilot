import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/components/Main.vue'
import SignUp from '@/components/signup/SignUp.vue';
import LogIn from '@/components/login/LogIn.vue'
import Board from '@/components/board/Board.vue'
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
      path: '/signup',
      name: 'signup',
      component: SignUp
    },
    {
      path: '/board',
      name: 'board',
      component: Board
    },
    {
      path: '/members',
      name: 'members',
      component: UserList
    }
  ]
})
