<template>
  <div class="signup-row">
    <div class="signup-form">
      <h1>회원이 되신 것을 축하드립니다 !</h1>
      <!--<p>
        이메일 :
        <span id="email"> {{ email }} </span>
      </p>
      <p>
        닉네임 :
        <span id="nickname"> {{ nickname }} </span>
      </p>-->
      <p>
        <router-link class="btn btn-primary" to="/"><button> 홈 </button></router-link>
        <router-view/>
        <router-link class="btn btn-primary" to="/login"><button> 로그인 </button></router-link>
        <router-view/>
      </p>
    </div>
  </div>
</template>


<script>
  import http from "@/http-common";

  export default {
    data() {
      return {
        email: '',
        nickname: ''
      }
    },
    props: ['userEmail'],
    methods: {
      userData() {

      }
    },
    mounted() {
      this.userData();
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
