import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/components/Main.vue'
import SignUp from '@/components/signup/SignUp.vue';
//import AuthMail from '@/components/signup/AuthMail.vue';
import SignUpComplete from '@/components/signup/SignUpComplete.vue'
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
      path: '/signup',
      name: 'signup',
      component: SignUp
      /*children : [
        {
          path: '/signup/auth',
          name: 'auth',
          component: AuthMail,
          props: true
        }
      ]*/
    },
    {
      path: '/signup/complete',
      name: 'signup-complete',
      component: SignUpComplete
    },
    {
      path: '/users',
      name: 'users',
      component: UserList
    }
  ]
})
