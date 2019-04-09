<template>
  <div>
    <h1>--- 메인 페이지 ---</h1>
    <div class="site-info">
      <router-link class="btn btn-primary" to="/login"> 로그인 버튼 </router-link>
      <router-view/>
    </div>
    <div class="site-info">
      <router-link class="btn btn-primary" to="/signup"> 회원가입 버튼 </router-link>
      <router-view/>
    </div>
    <div class="site-info">
      <router-link class="btn btn-primary" to="/members"> 사용자 조회 버튼 (테스트용) </router-link>
      <router-view/>
    </div>
  </div>
</template>

<script>
    export default {
        name: "Main"
    }
</script>

<style>
  .site-info {
    color: blue;
    margin-bottom: 20px;
  }

  .btn-primary {
    margin-right: 5px;
  }
</style>
