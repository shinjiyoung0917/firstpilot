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

          <li class="nav-item" v-if="this.nickname !== null && this.nickname !== 'undefined'" id="header-navigation-bar3">
            <router-link to="/dashboard" style="color: #888888;"> 나의 페이지 </router-link> <!-- 나의 페이지에서 회원탈퇴 구현하기 -->
            <router-view/>
          </li>

          <li class="nav-item" v-if="this.nickname !== null && this.nickname !== 'undefined'" id="header-navigation-bar4">
            <a @click="logout" style="color: #888888;" href=""> 로그아웃 </a>
          </li>

          <li class="nav-item" v-else id="header-navigation-bar5">
            <router-link to="/login" style="color: #888888;"> 로그인 </router-link>
            <router-view/>
          </li>

          <li class="nav-item" v-if="this.nickname !== null && this.nickname !== 'undefined'" id="header-navigation-bar6">
            <span style="color: cornsilk"> {{ this.nickname }}님 환영합니다. </span>
          </li>

          <li class="nav-item" v-else id="header-navigation-bar7">
            <router-link to="/signup" style="color: #888888;"> 회원가입 </router-link>
            <router-view/>
          </li>

        </ul>

      </div>

    </div>
  </nav>
</template>

<script>
  import http from "@/http-common"
  import {BUS} from '@/EventBus'

  export default {
    name: "Header",
    data() {
      return {
        nickname: sessionStorage.getItem("nickname")
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
      changeNicknameInSession() {
        BUS.$on('bus:call', function (nickname) {
          window.alert("Dashboard.vue에서 emit해서 이벤트를 받음");
          this.nickname = nickname;
        });
      }
    },
    beforeCreate() {
      http.get("/session")
        .then((res) => {
          if (res.status === 200) {
            if(res.data !== null) {
              sessionStorage.setItem("nickname", res.data.nickname);
              sessionStorage.setItem("memberId", res.data.memberId);
            } else {
              // 세션 초기화?
              sessionStorage.removeItem("nickname"); // 도메인 키와 데이터 모두 삭제, 특정 세션 삭제
              sessionStorage.removeItem("memberId");
              sessionStorage.clear();               // 저장된 모든 값 삭제, 세션 전체 삭제
            }
          } else {
            sessionStorage.removeItem("nickname");
            sessionStorage.removeItem("memberId");
            sessionStorage.clear();
          }
        }).catch((e) => {
        sessionStorage.removeItem("nickname");
        sessionStorage.removeItem("memberId");
        sessionStorage.clear();
      });
    }
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
