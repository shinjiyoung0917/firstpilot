<template>
  <div>
    <app-header></app-header>

    <div class="signup-row">
      <div class="signup-form">
        <div v-if="this.isSignUp === 0">
          <h1>회 원 가 입</h1>
          <p>
            이메일
            <input type="text" placeholder="yourname@example.com" v-model="email" @keyup="checkEmailPattern"> <!--  v-bind:memberEmail="email" -->
            <button @click="reqAuthKey"> 인증 </button>
            <br><span id="checkEmailPatternResult" style="color: #B94A48;"></span>
          </p>
          <p v-if="this.isCreatedAuthKey === 1">
            인증코드
            <input type="text" v-model="inputAuthKey">
            <span id="countTime" style="color: #B94A48;"> {{ this.min }} : {{ this.sec }} </span>
            <button @click="checkAuthKey"> 확인 </button>
          </p>
          <p>
            비밀번호
            <input type="password" placeholder="" v-model="password" @keyup="checkPasswordPattern">
            <br><span id="checkPasswordPatternResult" style="color: #B94A48;"></span>
          </p>
          <p>
            비밀번호 재확인
            <input type="password" placeholder="" v-model="passwordRepeat" @keyup="checkPasswordRepeat">
            <br><span id="checkPasswordRepeatResult" style="color: #B94A48;"></span>
          </p>
          <button @click="signup"> 가입 </button>
          <button @click="cancel"> 취소 </button>
        </div>
        <div v-else>
          <h1>회원이 되신 것을 축하드립니다 !</h1>
          <p>
            이메일 :
            <span id="email"> {{ this.email }} </span>
          </p>
          <p>
            닉네임 :
            <span id="nickname"> {{ this.nickname }} </span>
            <br><span style="color: #B94A48"> (* 위의 닉네임은 임시 닉네임이며, 변경이 가능합니다.) </span>
          </p>
          <p>
            <router-link class="btn btn-primary" to="/"><button> 홈 </button></router-link>
            <router-view/>
            <router-link class="btn btn-primary" to="/login"><button> 로그인 </button></router-link>
            <router-view/>
          </p>
        </div>
      </div>
      <!-- <div class='toast' style='display:none' id="toast">토스트 띄우기 테스트</div> -->
    </div>

    <app-footer></app-footer>
  </div>
</template>


<script>
  import http from "@/http-common"
  import Header from '../layout/Header.vue'
  import Footer from '../layout/Footer.vue'

  export default {
    components: {
      'app-header': Header,
      'app-footer': Footer
    },
    data() {
      return {
        email: '',
        password: '',
        passwordRepeat: '',
        nickname: '',
        authKey: '',
        inputAuthKey: '',
        isCreatedAuthKey: 0,
        isUsedAuthKey: 0,
        isSignUp: 0,
        min: 3,
        sec: 0,
        submitted: false
      }
    },
    methods: {
      /* 이메일 패턴 체크 */
      checkEmailPattern() {
        const expText = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;

        if(!expText.test(this.email)) {
          document.getElementById('checkEmailPatternResult').innerHTML = "잘못된 이메일 형식입니다.";
          return false;
        } else {
          document.getElementById('checkEmailPatternResult').innerHTML = "올바른 이메일 형식입니다.";
          return true;
        }
      },
      /* 비밀번호 패턴 체크 */
      checkPasswordPattern() {
        const expText =  /^[A-Za-z0-9]{6,12}$/;

        if(!expText.test(this.password)) {
          document.getElementById('checkPasswordPatternResult').innerHTML = "6~12자리의 문자, 숫자의 조합을 사용하세요.";
        } else {
          document.getElementById('checkPasswordPatternResult').innerHTML = " ";
        }
      },
      /* 비밀번호 재확인 체크 */
      checkPasswordRepeat() {
        if(this.password !== this.passwordRepeat){
          document.getElementById('checkPasswordRepeatResult').innerHTML = "비밀번호와 동일하게 입력해주세요.";
        }  else {
          document.getElementById('checkPasswordRepeatResult').innerHTML = " ";
        }
      },
      /* 인증코드 요청 */
      reqAuthKey() {
        //$('#toast').fadeIn(4000).delay(1000).fadeOut(400);

        let check = this.checkEmailPattern();
        if(!check) {
          window.alert("이메일을 다시 한 번 확인하신 후 시도해주세요.");
        } else {
          let data = {
            email: this.email
          }

          http.post("/auth", data)
            .then(res => {
              if(res.data.email === "Already-Exist") {
                window.alert("이미 가입된 이메일입니다.")
              } else {
                this.authKey = res.data.authKey;
                this.isCreatedAuthKey = 1;
                window.alert("인증코드를 메일로 발송하였습니다.\n확인하신 후 인증코드를 입력해주세요.");
                //this.getTime();
              }
            }).catch(e => {
              window.alert(e);
              console.log(e);
            });
        }
      },
      /* 인증코드 만료 시간 카운팅 */
      getTime() {
        //window.setTimeout("getTime();", 1000);
      },
      /* 인증코드 확인 */
      checkAuthKey() {
        if(this.inputAuthKey === this.authKey) {
          this.isUsedAuthKey = 1;
          window.alert("이메일 인증이 완료되었습니다.");
        } else {
          window.alert("인증코드를 다시 한 번 확인해주세요.");
        }

      },
      /* 회원가입 요청 */
      signup() {
        if(this.isUsedAuthKey === 0) {
          window.alert("이메일 인증을 진행해주세요.");
        } else if(!this.email || !this.password) {
          window.alert("입력란을 모두 입력하신 후 시도해주세요.");
        } else {
          let data = {
            email: this.email,
            password: this.password
          }

          http.post('/members', data)
            .then((res) => {
              if(res.data) {
                if(res.data.email === "Already-Exist") {
                  window.alert("이미 가입된 이메일입니다.")
                } else {
                  this.isSignUp = 1;
                  this.nickname = res.data.nickname;
                }
              } else {
                window.alert("이메일과 비밀번호를 다시 한 번 확인해주세요.");
              }
          }).catch((e) => {
            window.alert(e);
            console.log(e);
          });
        }
      },
      cancel() {
        this.$router.replace('/')
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
    height: 800px;
    width: 60%;
    background-color: white;
    box-shadow: 3px 3px 3px 3px #d0d0d0;
  }
  h1 {
    padding-bottom: 6%;
  }
  .signup-form input {
    border-style: none;
    border-bottom: solid black 1px;
    height: 70px;
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

/*
  .toast {
    width: 250px;
    height: 20px;
    height:auto;
    position: fixed;
    left: 50%;
    margin-left:-125px;
    bottom: 100px;
    z-index: 9999;
    background-color: #383838;
    color: #F0F0F0;
    font-family: Calibri;
    font-size: 15px;
    padding: 10px;
    text-align:center;
    border-radius: 2px;
    -webkit-box-shadow: 0px 0px 24px -1px rgba(56, 56, 56, 1);
    -moz-box-shadow: 0px 0px 24px -1px rgba(56, 56, 56, 1);
    box-shadow: 0px 0px 24px -1px rgba(56, 56, 56, 1);
   }
*/
</style>
