<template>
  <!-- Navigation -->
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
    <div class="container">
      <a class="navbar-brand" href="#">Start Anonymous Board</a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarResponsive">
        <ul class="navbar-nav ml-auto">
          <li class="nav-item active">
            <a class="nav-link" href="#"> 홈
              <span class="sr-only">(current)</span>
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#"> 익명 게시판 </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#"> 나의 페이지 </a> <!-- 나의 페이지에서 회원탈퇴 구현하기 -->
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#"> 로그아웃 </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#"> {{ this.nickname }}님 환영합니다. </a>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
  //import {BUS} from '@/EventBus'

  export default {
    name: "Header",
    data() {
      return {
        session: false,
        nickname: sessionStorage.getItem("nickname")
      };
    },
    methods: {
      logout() {
        // sessionStorage.removeItem("nickname"); // 도메인 키와 데이터 모두 삭제, 특정 세션 삭제
        // sessionStorage.clar(); // 저장된 모든 값 삭제, 세션 전체 삭제
      }

      /*setIsSession() {
        BUS.$on('bus:call', function () {
          window.alert("Login.vue에서 emit해서 이벤트 받은 함수");
        })
      },*/
    }

    /*watch: {
     session: function () {
       window.alert("this.session을 watch하는 함수");
       this.nickname = sessionStorage.getItem("nickname");
     }
   },
    created() {
    },
    beforeUpdate() {
      this.nickname = sessionStorage.getItem("nickname");
    }*/
  }
</script>
