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
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
  import http from "@/http-common";

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
        http.get("/users")
          .then(response => {
            this.users = response.data; // JSON이 자동으로 파싱됨
          })
          .catch(e => {
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
