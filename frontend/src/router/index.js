import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/components/Main.vue'
<<<<<<< HEAD
import Join from '@/components/join/Join.vue';
import LogIn from '@/components/login/LogIn.vue'
import UserList from '@/components/UserList.vue'; // 테스트
=======
import SignUp from '@/components/member/SignUp.vue'
import LogIn from '@/components/member/LogIn.vue'
import Board from '@/components/board/Board.vue'
import WriteBoard from '@/components/board/WriteBoard.vue'
import EditBoard from '@/components/board/EditBoard.vue'
import BoardDetails from '@/components/board/BoardDetails.vue'
import Dashboard from '@/components/dashboard/Dashboard.vue'
import ErrorPage from '@/components/ErrorPage.vue'
import UserList from '@/components/UserList.vue' // 테스트
>>>>>>> 3c2c93843f40de0a2904254b047576ff12a55626

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
<<<<<<< HEAD
=======
      path: '/main',
      name: 'main',
      component: Main
    },
    {
>>>>>>> 3c2c93843f40de0a2904254b047576ff12a55626
      path: '/login',
      name: 'login',
      component: LogIn
    },
    {
<<<<<<< HEAD
      path: '/join',
      name: 'join',
      component: Join
    },

    {
      path: '/users',
      name: 'users',
=======
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
      path: '/boards/:id/edit',
      name: 'edit-board',
      component: EditBoard
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
      path: '/404',
      name: 'error-page',
      component: ErrorPage
    },
    {
      path: '*',
      redirect: '/404'
    },
    {
      path: '/members',
      name: 'members',
>>>>>>> 3c2c93843f40de0a2904254b047576ff12a55626
      component: UserList
    }
  ]
})
