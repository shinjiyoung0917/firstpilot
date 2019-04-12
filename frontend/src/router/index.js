import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/components/Main.vue'
import SignUp from '@/components/member/SignUp.vue'
import LogIn from '@/components/member/LogIn.vue'
import Board from '@/components/board/Board.vue'
import WriteBoard from '@/components/board/WriteBoard.vue'
import BoardDetails from '@/components/board/BoardDetails.vue'
import Dashboard from '@/components/dashboard/Dashboard.vue'
import UserList from '@/components/UserList.vue' // 테스트

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
      path: '/main',
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
      path: '/boards',
      name: 'board',
      component: Board
    },
    {
      path: '/boards/write',
      name: 'write-board',
      component: WriteBoard
    },
    {
      path: '/boards/:id',
      name: 'board-details',
      component: BoardDetails
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: Dashboard
    },
    {
      path: '/members',
      name: 'members',
      component: UserList
    }
  ]
})
