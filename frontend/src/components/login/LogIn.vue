<template>
  <div class="login-row">
    <div class="login-form">
      <h1>로 그 인</h1>
      <form @submit.prevent="login" id="loginForm">
      <p>
        <input type="email" placeholder="ID" v-model="email" name="username">
      </p>
      <p>
        <input type="password" placeholder="Password" v-model="password" name="password">
       </p>
      <!--
      <b-form-group id="exampleInputGroup1" label="Username:" label-for="exampleInput1">
        <b-form-input id="exampleInput1" type="username" v-model="form.username" required placeholder="Enter username"></b-form-input>
      </b-form-group>
      <b-form-group id="exampleInputGroup2" label="Password:" label-for="exampleInput2">
        <b-form-input id="exampleInput2" type="password" v-model="form.password" required placeholder="Enter password"></b-form-input>
      </b-form-group>
      -->
      <p>
        <button> 로그인 </button> <!-- @click="login" -->
        <button @click="cancel"> 취소 </button>
      </p>
      </form>
    </div>
  </div>
</template>

<script>
  import http from "@/http-common";

  export default {
    data() {
      return {
        email: '',
        username: '',
        password: ''
      }
    },
    methods: {
      login() {
        if (this.email && this.password) {
          /*let data = {
            username: this.email,
            password: this.password
          }*/
          // loginForm = new FormData();
          // loginForm.add("email", this.email);
          // loginForm.add("password", this.password);
          window.alert("// " + this.email + ", " + this.password);

          http.post('/login', {
            params: {
              username: this.email,
              password: this.password
            }
          })
            .then((res) => {
              //console.log("data", data, "this", res)

              /*if (res.data.result === 1) {
                window.alert("로그인 성공");
                this.$store.commit('loginFlush', res.data.email);
                this.$router.push('/');
              } else {
                window.alert("로그인 실패");
                //window.alert(res.data.msg);
                window.location.reload();
              }*/
            }).catch((e) => {
              window.alert(e);
              console.log(e);
            });
        }
        else {
          window.alert('입력란을 모두 입력하고 시도해주세요.');
        }
      },
      cancel() {
        this.$router.replace('/');
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
