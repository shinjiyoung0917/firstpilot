webpackJsonp([1],{JYcK:function(t,e){},NHnr:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var s=n("7+uW"),a={render:function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"container-fluid",attrs:{id:"app"}},[e("div",{staticClass:"site-info"}),this._v(" "),e("router-view")],1)},staticRenderFns:[]};var o=n("VU/8")({name:"App"},a,!1,function(t){n("JYcK")},null,null).exports,i=n("/ocq"),r={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",[n("h1",[t._v("--- 메인 페이지 ---")]),t._v(" "),n("div",{staticClass:"site-info"},[n("router-link",{staticClass:"btn btn-primary",attrs:{to:"/login"}},[t._v(" 로그인 버튼 ")]),t._v(" "),n("router-view")],1),t._v(" "),n("div",{staticClass:"site-info"},[n("router-link",{staticClass:"btn btn-primary",attrs:{to:"/signup"}},[t._v(" 회원가입 버튼 ")]),t._v(" "),n("router-view")],1),t._v(" "),n("div",{staticClass:"site-info"},[n("router-link",{staticClass:"btn btn-primary",attrs:{to:"/users"}},[t._v(" 사용자 조회 버튼 (테스트용) ")]),t._v(" "),n("router-view")],1)])},staticRenderFns:[]};var l=n("VU/8")({name:"Main"},r,!1,function(t){n("aP60")},null,null).exports,c=n("mtWM"),u=n.n(c).a.create({baseURL:"http://localhost:8081/",headers:{"Content-type":"application/json"}}),p={data:function(){return{email:"",password:""}},methods:{signup:function(){var t=this;this.email&&this.password?u.post("/users",{email:this.email,password:this.password}).then(function(e){1===e.data.result?(t.$store.commit("loginFlush",e.data.email),t.$router.push("/")):(window.alert(e.data.msg),window.location.reload())}).catch(function(t){window.alert(t),console.log(t)}):window.alert("입력란을 모두 입력하고 시도해주세요.")},cancel:function(){this.$router.replace("/")}}},d={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"login-row"},[n("div",{staticClass:"login-form"},[n("h1",[t._v("회 원 가 입")]),t._v(" "),n("p",[n("input",{directives:[{name:"model",rawName:"v-model",value:t.email,expression:"email"}],attrs:{type:"text",placeholder:"ID"},domProps:{value:t.email},on:{input:function(e){e.target.composing||(t.email=e.target.value)}}})]),t._v(" "),n("p",[n("input",{directives:[{name:"model",rawName:"v-model",value:t.password,expression:"password"}],attrs:{type:"password",placeholder:"Password"},domProps:{value:t.password},on:{input:function(e){e.target.composing||(t.password=e.target.value)}}})]),t._v(" "),n("p",[n("button",{on:{click:t.signup}},[t._v("확인")]),t._v(" "),n("button",{on:{click:t.cancel}},[t._v("취소")])])])])},staticRenderFns:[]};var v=n("VU/8")(p,d,!1,function(t){n("d5HI")},null,null).exports,m={data:function(){return{email:"",password:""}},methods:{login:function(){var t=this;this.email&&this.password?u.post("/login",{email:this.email,password:this.password}).then(function(e){1===e.data.result?(t.$store.commit("loginFlush",e.data.email),t.$router.push("/")):(window.alert(e.data.msg),window.location.reload())}).catch(function(t){window.alert(t),console.log(t)}):window.alert("입력란을 모두 입력하고 시도해주세요.")},cancel:function(){this.$router.replace("/")}}},f={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"login-row"},[n("div",{staticClass:"login-form"},[n("h1",[t._v("로 그 인")]),t._v(" "),n("p",[n("input",{directives:[{name:"model",rawName:"v-model",value:t.email,expression:"email"}],attrs:{type:"text",placeholder:"ID"},domProps:{value:t.email},on:{input:function(e){e.target.composing||(t.email=e.target.value)}}})]),t._v(" "),n("p",[n("input",{directives:[{name:"model",rawName:"v-model",value:t.password,expression:"password"}],attrs:{type:"password",placeholder:"Password"},domProps:{value:t.password},on:{input:function(e){e.target.composing||(t.password=e.target.value)}}})]),t._v(" "),n("p",[n("button",{on:{click:t.login}},[t._v("로그인")]),t._v(" "),n("button",{on:{click:t.cancel}},[t._v("취소")])])])])},staticRenderFns:[]};var h=n("VU/8")(m,f,!1,function(t){n("qCPL")},null,null).exports,w={name:"users-list",data:function(){return{users:[]}},methods:{showUsers:function(){var t=this;u.get("/users").then(function(e){t.users=e.data}).catch(function(t){console.log(t)})},refreshList:function(){this.showUsers()}},mounted:function(){this.showUsers()}},_={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"list row"},[n("div",{staticClass:"col-md-6"},[n("h4",[t._v(" 사용자 조회 결과 ~@ ")]),t._v(" "),n("ul",t._l(t.users,function(e){return n("li",[t._v("\n        "+t._s(e.email)+"\n        "+t._s(e.nickname)+"\n        ")])}),0)]),t._v(" "),n("div",{staticClass:"col-md-6"},[n("router-view",{on:{refreshData:t.refreshList}})],1)])},staticRenderFns:[]};var g=n("VU/8")(w,_,!1,function(t){n("Nffz")},null,null).exports;s.a.use(i.a);var C=new i.a({mode:"history",routes:[{path:"/",name:"main",component:l},{path:"/login",name:"login",component:h},{path:"/signup",name:"signup",component:v},{path:"/users",name:"users",component:g}]});s.a.config.productionTip=!1,new s.a({el:"#app",router:C,components:{App:o},template:"<App/>"})},Nffz:function(t,e){},aP60:function(t,e){},d5HI:function(t,e){},qCPL:function(t,e){}},["NHnr"]);
//# sourceMappingURL=app.dd0bfb8f3a1e3254a895.js.map