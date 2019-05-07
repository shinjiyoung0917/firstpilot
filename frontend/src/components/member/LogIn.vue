<template>
  <div>

    <div class="login-row">
      <div class="login-form">
        <h1> 로 그 인 </h1>

        <form @submit.prevent="login" id="loginForm" method="POST">
          <p>
            <input type="email" placeholder="ID" v-model="email" name="username">
          </p>
          <p>
            <input type="password" placeholder="Password" v-model="password" name="password">
          </p>
          <p>
            <button type="submit"> 로그인 </button>
            <button @click="cancel"> 취소 </button>
          </p>
        </form>
      </div>
    </div>

  </div>
</template>

<script>
  import http from "@/http-common"
  import {BUS} from '@/EventBus'

  export default {
    name: "Login",
    data() {
      return {
        email: '',
        username: '',
        password: '',
        nickname: '',
        memberId: ''
      }
    },
    methods: {
      login() {
        if (this.email && this.password) {
          let bodyFormData = new FormData();
          bodyFormData.set('username', this.email);
          bodyFormData.append('password', this.password);

          http.post('/login', bodyFormData)
            .then((res) => {
              if(res.status === 200) {
                this.setSessionToBrowser();
                //this.$store.commit('loginFlush', res.data.email);
              } else {
                window.alert("로그인에 실패하였습니다.\n이메일과 비밀번호를 다시 한 번 확인해주세요.");
                window.location.reload();
              }
            }).catch((e) => {
              window.alert("로그인에 실패하였습니다.\n이메일과 비밀번호를 다시 한 번 확인해주세요.");
              console.log(e);
            });
        }
        else {
          window.alert('입력란을 모두 입력하고 시도해주세요.');
        }
      },
      cancel() {
        this.$router.replace('/');
      },
      setSessionToBrowser() {
        http.get("/session")
          .then((res) => {
            if (res.status === 200) {
              this.nickname = res.data.nickname;
              this.memberId = res.data.memberId;

              localStorage.setItem("nickname", this.nickname);
              localStorage.setItem("memberId", this.memberId);

              BUS.$emit('nickname:login', this.nickname);

              this.$router.replace('/');
            } else {
              window.alert(res.status + " 에러");
            }
          }).catch((e) => {
          window.alert(e);
          console.log(e);
        });
      }
    }
  }
</script>

<style>
  .login-row {
    clear: both;
    padding-top: 4%;
  }
  .login-form {
    margin: 0 auto;
    padding-top: 4%;
    padding-left: 10%;
    height: 400px;
    width: 50%;
    background-color: white;
    box-shadow: 3px 3px 3px 3px #d0d0d0;
  }
  h1 {
    padding-bottom: 6%;
  }
  .login-form input {
    border-style: none;
    border-bottom: solid black 1px;
    height: 30px;
    width: 80%;
    padding-left: 2%;
    outline: none;
  }
  .login-form p:last-child {
    text-align: right;
    padding-right: 15%;
  }
  .login-form button {
    height: 35px;
    width: 80px;
    border: none;
    border-radius: 5px;
  }
  .login-form button:first-child {
    background: #33c197;
    color: white;
  }
</style>


