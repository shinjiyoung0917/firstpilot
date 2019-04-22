<template>
  <div class="list row">
    <div class="col-md-6">
      <h4> 사용자 조회 결과 ~@ </h4>
      <ul>
        <li v-for="user in users">
          {{ user.email }}
          {{ user.nickname }}
          <!--<router-link :to="{ name: 'users' }">
          </router-link>-->
        </li>
      </ul>
    </div>
    <div class="col-md-6">
<<<<<<< HEAD
      <router-view @refreshData="refreshList"></router-view>
=======
      <router-view></router-view>
>>>>>>> 3c2c93843f40de0a2904254b047576ff12a55626
    </div>
  </div>
</template>

<script>
<<<<<<< HEAD
  import http from "@/http-common";
=======
  import http from "@/http-common"
>>>>>>> 3c2c93843f40de0a2904254b047576ff12a55626

  export default {
    name: "users-list",
    data() {
      return {
        users: []
      };
    },
    methods: {
      /* eslint-disable no-console */
      showUsers() {
<<<<<<< HEAD
        http
          .get("/users")
          .then(response => {
            this.users = response.data; // JSON이 자동으로 파싱됨
          })
          .catch(e => {
=======
        http.get("/members")
          .then(res => {
            this.users = res.data; // JSON이 자동으로 파싱됨
          })
          .catch(e => {
            window.alert(e);
>>>>>>> 3c2c93843f40de0a2904254b047576ff12a55626
            console.log(e);
          });
      },
      refreshList() {
        this.showUsers();
      }
      /* eslint-enable no-console */
    },
    mounted() {
      this.showUsers();
    }
  };
</script>

<style>
  .list {
    text-align: left;
    max-width: 450px;
    margin: auto;
  }
</style>
