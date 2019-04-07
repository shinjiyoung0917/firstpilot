<template>
  <div class="signup-row">
    <div class="signup-form">
      <h1>메 일 인 증</h1>
      <p>
        인증 코드
        <input type="text" v-model="authCode">
        남은시간
        <span id="checkPasswordRepeatResult" style="color: #B94A48;"></span>
      </p>
      <p>
        <button v-on:click="checkAuthCode"> 확인 </button>
      </p>
    </div>
  </div>
</template>

<script>
  import http from "@/http-common";

  export default {
    //props: ["email"],
    data() {
      return {
        email: '',
        authCode: ''
      }
    },
    methods: {
      /* 인증코드 확인 */
      checkAuthCode() {
        http.get("/auth")
          .then(res => {
            if(res.auth === this.authCode) {

            } else {
              window.alert("인증코드를 다시 한 번 확인해주세요.");
            }
          }).catch(e => {
          window.alert(e);
          console.log(e);
        });

      }
    }
  }
</script>

<style>
  .signup-row {
    clear: both;
    padding-top: 4%;
  }
  .signup-form {
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
  .signup-form input {
    border-style: none;
    border-bottom: solid black 1px;
    height: 30px;
    width: 80%;
    padding-left: 2%;
    outline: none;
  }
  .signup-form p:last-child {
    text-align: right;
    padding-right: 15%;
  }
  .signup-form button {
    height: 35px;
    width: 80px;
    border: none;
    border-radius: 5px;
  }
  .signup-form button:first-child {
    background: #33c197;
    color: white;
  }
</style>
