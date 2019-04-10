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
  import http from "@/http-common";
  import {BUS} from '@/EventBus'

  export default {
    name: "Header",
    data() {
      return {
        session: false,
        nickname: ''
      };
    },
    methods: {
      /* 세션 얻기 */
      /*session() {
        if(this.session) {  // 세션 있는 경우
          http.get("/nickname")
            .then((res) => {
              if (res.status === 200) {
                window.alert("세션 가져오기 성공");
                this.member = res.data;
                this.session = true;
              } else {
                window.alert(res.status + " 에러");
              }
            }).catch((e) => {
            window.alert(e);
            console.log(e);
          });
        } else {            // 세션 없는 경우
        }
      }*/
      // 로그아웃 구현 -> session = false; 로 세팅
    },
    mounted() {
      //this.session();
      BUS.$on('bus:call', function () {
        window.alert("3333");
        http.get("/nickname")
          .then((res) => {
            if (res.status === 200) {
              window.alert("세션 가져오기 성공 " + res.data);
              this.nickname = res.data;
              this.session = true;
            } else {
              window.alert(res.status + " 에러");
            }
          }).catch((e) => {
          window.alert(e);
          console.log(e);
        });
      })

    },
    beforeUpdate() {

    }
  }
</script>
