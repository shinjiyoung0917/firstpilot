<template>
  <div>
    <app-header></app-header>

    <!-- Page Content -->
    <div class="container">

      <div class="row">

        <!-- Post Content Column -->
        <div class="col-lg-8">

          <!-- Title -->
          <h1 class="mt-4"> {{ this.board.title }} </h1>

          <!-- Author -->
          <p class="lead">
            by {{ this.board.nickname }}
          </p>

          <hr>

          <!-- Date/Time -->
          <p>Posted on {{ this.board.createdDate }} </p>

          <hr>

          <!-- Preview Image -->
          <img class="img-fluid rounded" src="http://placehold.it/900x300" alt="">

          <hr>

          <!-- Post Content -->
          <p class="lead">
            {{ this.board.content }}
          </p>

          <hr>

          <!-- Comments Form -->
          <div class="card my-4">
            <h5 class="card-header">댓글을 남겨주세요:</h5>
            <div class="card-body">
              <!--<form>-->
                <div class="form-group">
                  <textarea class="form-control" rows="3" v-model="content"></textarea>
                </div>
                <input type="file" id="uploadFile" name="uploadFile" @change="setFileData($event.target.files)">
                <button class="btn btn-primary" @click="write(null)">등록</button>
              <!--</form>-->
            </div>
          </div>

          <!--<div v-for="comment in comments">
            <div class="media mb-4" v-if="comment.parentId !== null">
              <img class="d-flex mr-3 rounded-circle" src="http://placehold.it/50x50" alt="">
              <div class="media-body">
                <h5 class="mt-0"> {{ comment.nickname }} </h5>
                {{ comment.content }}
              </div>
            </div>
          </div>-->

          <!-- 댓글, 대댓글 -->
          <div v-for="(comment, index) in comments">
            <div class="media mb-4" > <!-- v-if="comment.parentId === null" -->
              <img class="d-flex mr-3 rounded-circle" src="http://placehold.it/50x50" alt="">
              <div class="media-body" style="float: left">

                <h5 class="mt-0"> {{ comment.nickname }} </h5>
                {{ comment.content }}
                <div v-if="isHidden === 1">
                  <button @click=""> 대댓글 달기 </button>
                </div>
                <div v-else>
                  <button @click=""> 취소 </button>
                </div>
                <!-- 대댓글 -->
                <div v-for="childComment in childComments">
                  <div class="media mt-4" v-else-if="childComment.parentId === comment.commentId">
                    <img class="d-flex mr-3 rounded-circle" src="http://placehold.it/50x50" alt="">
                    <div class="media-body">
                      <h5 class="mt-0"> {{ childComment.nickname }} </h5>
                      {{ childComment.content }}
                    </div>
                  </div>
                </div>
                <!-- 대댓글 입력창 -->
                <div class="form-group" v-if="isHidden === 0">
                  <textarea class="form-control" rows="3" v-model="childContent"></textarea>
                  <button class="btn btn-primary" @click="write(comment.commentId)">등록</button>
                </div>

              </div>

            </div>
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
    data () {
      return {
        boardId: this.$route.params.id,
        board: '',
        comments: [],
        childComments: [],
        content: '',
        childContent: '',
        fileData: '',
        filePath: '',
        isHidden: 1
      }
    },
    methods: {
      /* 해당 게시물 정보 요청 */
      getBoardDetails() {
        let boardId = {
          boardId: this.boardId
        }
        http.get('/boards/details', { params: boardId })
          .then((res) => {
            this.board = res.data;
          }).catch((e) => {
          window.alert(e);
          console.log(e);
        });
      },
      /* 전체 댓글 정보 요청 */
      getComments() {
        http.get('/boards/' + this.boardId + '/comments')
          .then((res) => {
            if(res.status === 200) {
              for(let i in res.data) {
                let comment = res.data[i];
                if (comment.isValid === 1) {
                  if(comment.parentId === null) {  // 댓글일 경우
                    let commentInfo = {
                      commentId: comment.commentId,
                      title: comment.title,
                      content: comment.content,
                      memberId: comment.memberId,
                      nickname: comment.nickname,
                      createdDate: comment.createdDate,
                      updatedDate: comment.updatedDate,
                      parentId: comment.parentId,
                      childCount: comment.childCount,
                      filePath: comment.filePath
                    };
                    this.comments.push(commentInfo);
                  } else {                          // 대댓글일 경우
                    let childCommentInfo = {
                      commentId: comment.commentId,
                      title: comment.title,
                      content: comment.content,
                      memberId: comment.memberId,
                      nickname: comment.nickname,
                      createdDate: comment.createdDate,
                      updatedDate: comment.updatedDate,
                      parentId: comment.parentId,
                      childCount: comment.childCount,
                      filePath: comment.filePath
                    };
                    this.childComments.push(childCommentInfo);
                  }
                }
              }
              //window.alert("/// 댓글리스트 : " + JSON.stringify(this.comments));
              //window.alert("/// 대댓글리스트 : " + JSON.stringify(this.childComments));
            }
          }).catch((e) => {
          window.alert(e);
          console.log(e);
        });
      },
      /* 댓글 등록 요청 (파일 먼저 서버에 저장) */
      write(parent) {
        if(parent === null) {
          if (this.content === null || this.content === "") {
            window.alert("내용을 입력해주세요.");
            return;
          }
        } else {
          if (this.childContent === null || this.childContent === "") {
            window.alert("내용을 입력해주세요.");
            return;
          }
        }

        if(this.fileData !== '') {    // 업로드할 파일이 있을 경우
          let bodyFormData = new FormData();
          bodyFormData.set('uploadFile', this.fileData);

          // 파일 서버 디렉토리에 저장
          httpFile.post('/boards/file', bodyFormData)
            .then((res) => {
              if (res.status === 200) {
                this.filePath = res.data;
                window.alert("///1 res.data : " + res.data);
                this.writeComment(parent);
              }
            }).catch((e) => {
            window.alert(e);
            console.log(e);
          });
        } else {                      // 업로드할 파일이 없을 경우
          this.writeComment(parent);
        }
      },
      /* 선택한 파일 데이터 가져오기 */
      setFileData(files) {
        if(files.length) {
          this.fileData = files[0];
        }
      },
      /* 파일 데이터를 제외한 나머지 게시판 데이터 등록 요청 */
      writeComment(parent) {
        window.alert("parent: " + parent);
        if(parent === null) {
          let data = {
            content: this.content,
            nickname: sessionStorage.getItem("nickname"),
            parentId: parent,
            filePath: this.filePath
          }

          http.post('/boards/' + this.boardId + '/comments', data)
            .then((res) => {
              if (res.status === 200) {
                window.alert("댓글 등록을 성공적으로 완료하였습니다.");
                this.$router.replace('/boards/' + this.boardId);
                /*let comment = res.data;
                let commentInfo = {
                  commentId: comment.commentId,
                  title: comment.title,
                  content: comment.content,
                  memberId: comment.memberId,
                  nickname: comment.nickname,
                  createdDate: comment.createdDate,
                  updatedDate: comment.updatedDate,
                  parentId: comment.parentId,
                  childCount: comment.childCount,
                  filePath: comment.filePath
                };*/
                //this.comments.push(commentInfo);
              }
            }).catch((e) => {
            window.alert(e);
            console.log(e);
          });
        } else {
          let data = {
            content: this.childContent,
            nickname: sessionStorage.getItem("nickname"),
            parentId: parent,
            filePath: this.filePath
          }
          http.post('/boards/' + this.boardId + '/comments', data)
            .then((res) => {
              if (res.status === 200) {
                window.alert("대댓글 등록을 성공적으로 완료하였습니다.");
                let childComment = res.data;
                let childCommentInfo = {
                  commentId: childComment.commentId,
                  title: childComment.title,
                  content: childComment.content,
                  memberId: childComment.memberId,
                  nickname: childComment.nickname,
                  createdDate: childComment.createdDate,
                  updatedDate: childComment.updatedDate,
                  parentId: childComment.parentId,
                  childCount: childComment.childCount,
                  filePath: childComment.filePath
                };
                this.childComments.push(childCommentInfo);
              }
            }).catch((e) => {
            window.alert(e);
            console.log(e);
          });
        }
      }
    },
    created() {
      this.getBoardDetails();
      this.getComments();
    },
    mounted() {

    }
  }
</script>
