<template>
  <!-- Navigation -->
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
    <div class="container">

      <router-link to="/" style="color: #888888;"> Start Anonymous Board </router-link>
      <router-view />

      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarResponsive">

        <ul class="navbar-nav ml-auto" >

          <li class="nav-item active" id="header-navigation-bar1">
            <router-link to="/" style="color: #888888;"> 홈
              <span class="sr-only">(current)</span>
            </router-link>
            <router-view/>
          </li>

          <li class="nav-item" id="header-navigation-bar2">
            <router-link to="/boards" style="color: #888888;"> 익명게시판 </router-link>
            <router-view/>
          </li>

          <li class="nav-item" v-if="this.nickname" id="header-navigation-bar3">
            <router-link to="/dashboard" style="color: #888888;"> 나의 페이지 </router-link> <!-- 나의 페이지에서 회원탈퇴 구현하기 -->
            <router-view/>
          </li>

          <li class="nav-item" v-if="this.nickname" id="header-navigation-bar4">
            <a @click="logout" style="color: #888888;" href=""> 로그아웃 </a>
          </li>

          <li class="nav-item" v-else id="header-navigation-bar5">
            <router-link to="/login" style="color: #888888;"> 로그인 </router-link>
            <router-view/>
          </li>

          <li class="nav-item" v-if="this.nickname" id="header-navigation-bar6">
            <span style="color: cornsilk"> {{ this.nickname }}님 환영합니다. </span>
          </li>

          <li class="nav-item" v-else id="header-navigation-bar7">
            <router-link to="/signup" style="color: #888888;"> 회원가입 </router-link>
            <router-view/>
          </li>

          <!-- 삭제할 버튼 -->
          <li class="nav-item" style="margin-left: 30px">
            <button @click="checkSession">세션테스트</button>
          </li>

        </ul>

      </div>

    </div>
  </nav>
</template>

<script>
  import http from "@/http-common"
  //import {BUS} from '@/EventBus'

  export default {
    name: "Header",
    data() {
      return {
        nickname:  sessionStorage.getItem("nickname")
      };
    },
    methods: {
      logout() {
        if(this.nickname !== null) {
          http.post('/logout')
            .then((res) => {
              if(res.status === 200) {
                window.alert("로그아웃이 성공적으로 완료되었습니다. \n 좋은 하루 되세요!");
                sessionStorage.removeItem("nickname"); // 도메인 키와 데이터 모두 삭제, 특정 세션 삭제
                sessionStorage.removeItem("memberId");
                sessionStorage.clear();               // 저장된 모든 값 삭제, 세션 전체 삭제
                this.nickname = null;
              }
            }).catch((e) => {
            window.alert(e);
            console.log(e);
          });
          this.$router.replace('/');
        } else {
          window.alert("이미 로그아웃 처리 완료되었습니다.");
        }
      },

      /*setIsSession() {
        BUS.$on('bus:call', function () {
          window.alert("Login.vue에서 emit해서 이벤트 받은 함수");
        })
      }*/
    },
    beforeCreate() {
      http.get("/session")
        .then((res) => {
          if (res.status === 200) {
            sessionStorage.setItem("nickname", res.data.nickname);
            sessionStorage.setItem("memberId", res.data.memberId);
          } else {
            // 세션 초기화?
            sessionStorage.removeItem("nickname"); // 도메인 키와 데이터 모두 삭제, 특정 세션 삭제
            sessionStorage.removeItem("memberId");
            sessionStorage.clear();               // 저장된 모든 값 삭제, 세션 전체 삭제
          }
        }).catch((e) => {
          // 세션 초기화?
          sessionStorage.removeItem("nickname"); // 도메인 키와 데이터 모두 삭제, 특정 세션 삭제
          sessionStorage.removeItem("memberId");
          sessionStorage.clear();               // 저장된 모든 값 삭제, 세션 전체 삭제
      });
    }
    /*watch: {
     session: function () {
       window.alert("this.session을 watch하는 함수");
       this.nickname = sessionStorage.getItem("nickname");
     }
   }*/
  }
</script>

<style>
  #header-navigation-bar1 {
    margin-left: 30px;
  }
  #header-navigation-bar2 {
    margin-left: 30px;
  }
  #header-navigation-bar3 {
    margin-left: 30px;
  }
  #header-navigation-bar4 {
    margin-left: 30px;
  }
  #header-navigation-bar5 {
       margin-left: 30px;
  }
  #header-navigation-bar6 {
    margin-left: 30px;
  }
  #header-navigation-bar7 {
    margin-left: 30px;
  }

</style>


<!--
=> {"data":"<!DOCTYPE html><html><head><meta charset=utf-8><meta name=viewport content=\"width=device-width,initial-scale=1\"><title>index.html</title><link href=/static/css/app.080528edf04bdec7fe8cdb93e6d1d6fb.css rel=stylesheet></head><body><div id=app></div><script type=text/javascript src=/static/js/manifest.2ae2e69a05c33dfc65f8.js></script><script type=text/javascript src=/static/js/vendor.643ced6211c8b29297de.js></script><script type=text/javascript src=/static/js/app.238eab6620327602eef9.js></script></body></html><script src=https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js></script>",
"status":200,
"statusText":"",
"headers":{"pragma":"no-cache","date":"Thu, 11 Apr 2019 08:14:06 GMT","x-content-type-options":"nosniff","last-modified":"Thu, 11 Apr 2019 08:13:38 GMT","x-frame-options":"DENY","content-language":"ko-KR","cache-control":"no-cache, no-store, max-age=0, must-revalidate","accept-ranges":"bytes","content-type":"text/html","content-length":"599","x-xss-protection":"1; mode=block","expires":"0"},
"config":{"transformRequest":{},"transformResponse":{},"timeout":0,"xsrfCookieName":"XSRF-TOKEN","xsrfHeaderName":"X-XSRF-TOKEN","maxContentLength":-1,"headers":{"Accept":"application/json, text/plain, */*"},"method":"post","baseURL":"http://localhost:8081/","url":"http://localhost:8081/logout"},
"request":{}}
-->
