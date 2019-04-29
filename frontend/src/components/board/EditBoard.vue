<template>
  <div>
    <app-header></app-header>

    <!-- Page Content -->
    <div class="container">

      <div class="row">

        <!-- Post Content Column -->
        <div class="col-lg-8">

          <!-- Title -->
          <input type="text" class="mt-4 form-control" v-model="board.title"/>

          <!-- Author -->
          <p class="lead">
            by {{ this.nickname }}
          </p>

          <hr>

          <!-- Preview Image -->
          <img :src="this.board.fileSrc" class="img-fluid rounded" alt="">

          <hr>

          <!-- Post Content -->
          <textarea class="mt-4 form-control" rows="5" v-model="board.content"></textarea>

          <hr>

          <!-- File -->
          <input type="file" id="uploadFile" name="uploadFile" @change="setFileData($event.target.files)">

          <hr>

          <!-- Write Button -->
          <div style="padding-bottom: 50px;">
            <button class="btn btn-primary" @click="edit"> 수정 </button>
            <router-link class="btn btn-primary" :to="{ name: 'board-details', params: { id: this.boardId }}" > 취소 </router-link>
          </div>

        </div>

        <!-- Sidebar Widgets Column -->
        <div class="col-md-4">

          <!-- Search Widget -->
          <div class="card my-4">
            <h5 class="card-header">Search</h5>
            <div class="card-body">
              <div class="input-group">
                <input type="text" class="form-control" placeholder="Search for...">
                <span class="input-group-btn">
                <button class="btn btn-secondary" type="button">Go!</button>
              </span>
              </div>
            </div>
          </div>

          <!-- Categories Widget -->
          <div class="card my-4">
            <h5 class="card-header">Categories</h5>
            <div class="card-body">
              <div class="row">
                <div class="col-lg-6">
                  <ul class="list-unstyled mb-0">
                    <li>
                      <a href="#">Web Design</a>
                    </li>
                    <li>
                      <a href="#">HTML</a>
                    </li>
                    <li>
                      <a href="#">Freebies</a>
                    </li>
                  </ul>
                </div>
                <div class="col-lg-6">
                  <ul class="list-unstyled mb-0">
                    <li>
                      <a href="#">JavaScript</a>
                    </li>
                    <li>
                      <a href="#">CSS</a>
                    </li>
                    <li>
                      <a href="#">Tutorials</a>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>

        </div>

      </div>
      <!-- /.row -->

    </div>
    <!-- /.container -->

    <app-footer></app-footer>
  </div>
</template>

<script>
  import http from "@/http-common"
  import httpFile from "@/http-fileUpload"
  import Header from '../layout/Header.vue'
  import Footer from '../layout/Footer.vue'

  export default {
    components: {
      'app-header': Header,
      'app-footer': Footer
    },
    data() {
      return {
        boardId: this.$route.params.id,
        memberId: sessionStorage.getItem("memberId"),
        nickname: sessionStorage.getItem("nickname"),
        board: '',
        fileData: ''
      }
    },
    methods: {
      getBoardData() {
        http.get('/boards/' + this.boardId)
          .then((res) => {
            this.board = res.data;

            if(this.board.filePath === "" || this.board.filePath === null) {
              this.board['fileSrc'] = require("../../assets/default.jpg");
            } else {
              this.board['fileSrc'] = "http://localhost:8081/files/" + this.board.filePath;
            }
          }).catch((e) => {
          window.alert(e);
          console.log(e);
        });
      },
      /* 게시물 수정 요청 (파일 먼저 서버에 저장) */
      edit() {
        if(this.board.title === null || this.board.title === "") {
          window.alert("제목을 입력해주세요.")
        } else if(this.board.content === null || this.board.content === "") {
          window.alert("내용을 입력해주세요.")
        } else {
          if(this.fileData !== '') {
            let bodyFormData = new FormData();
            bodyFormData.set('uploadFile', this.fileData);

            httpFile.post('/boards/file', bodyFormData)
              .then((res) => {
                if (res.status === 200) {
                  this.board.filePath = res.data;
                  this.editData();
                }
              }).catch((e) => {
              window.alert(e);
              console.log(e);
            });
          } else {
            this.editData();
          }
        }
      },
      /* 선택한 파일 데이터 가져오기 */
      setFileData(files) {
        if(files.length) {
          this.fileData = files[0];
        }
      },
      /* 파일 데이터를 제외한 나머지 게시판 데이터 등록 요청 */
      editData() {
        let data = {
          memberId: this.memberId,
          nickname: this.nickname,
          title: this.board.title,
          content: this.board.content,
          filePath: this.board.filePath,
          likeCount: this.board.likeCount,
          commentCount: this.board.commentCount,
          hitCount: this.board.hitCount,
          createdDate: this.board.createdDate,
          blockStatus: this.board.blockStatus
        }

        http.put('/boards/' + this.boardId, data)
          .then((res) => {
            if(res.status === 200) {
              window.alert("게시물 수정을 성공적으로 완료하였습니다.");
              this.$router.push('/boards/' + this.boardId);
            }
          }).catch((e) => {
          window.alert(e);
          console.log(e);
        });
      }
    },
    created() {
      if (!sessionStorage.getItem("memberId") || sessionStorage.getItem("memberId") === 'undefined') {
        window.alert("로그인이 필요한 서비스입니다.");
        this.$router.push('/login');
      } else {
        this.getBoardData();
      }
    }
  }
</script>
