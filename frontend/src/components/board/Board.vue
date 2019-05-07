<template>
  <div>

    <!-- Page Content -->
    <div class="container">

      <div class="row">

        <div class="col-lg-3" style="margin-top: 50px;">

          <h1 class="my-1"> 익명게시판 </h1>
          <div class="list-group" style="margin-top: 20px;">
            <router-link to="/boards/write" class="list-group-item" style="background-color: #AEBDCC; color: #2e2e2e"> 게시물 등록 </router-link>
            <router-view/>
          </div>
          <div class="list-group" style="margin-top: 20px;">
            <button class="list-group-item" style="color: #2e2e2e"> 최신순 </button>
            <!--<button class="list-group-item" style="color: #2e2e2e"> 인기순 </button>-->
          </div>

        </div>
        <!-- /.col-lg-3 -->

        <div class="col-lg-9">

          <div id="carouselExampleIndicators" class="carousel slide my-4" data-ride="carousel">
            <ol class="carousel-indicators">
              <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
              <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
              <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
            </ol>
            <div class="carousel-inner" role="listbox">
              <div class="carousel-item active">
                <img class="d-block img-fluid" src="http://placehold.it/900x350" alt="First slide">
              </div>
              <div class="carousel-item">
                <img class="d-block img-fluid" src="http://placehold.it/900x350" alt="Second slide">
              </div>
              <div class="carousel-item">
                <img class="d-block img-fluid" src="http://placehold.it/900x350" alt="Third slide">
              </div>
            </div>
            <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
              <span class="carousel-control-prev-icon" aria-hidden="true"></span>
              <span class="sr-only">Previous</span>
            </a>
            <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
              <span class="carousel-control-next-icon" aria-hidden="true"></span>
              <span class="sr-only">Next</span>
            </a>
          </div>

          <!-- 리스트 시작 -->
          <div class="row">

            <div class="col-lg-4 col-md-6 mb-4" v-for="(board, index) in boards">
              <div class="card h-100">
                <img :src="board.fileSrc"> <!--class="card-img-top" -->
                <div class="card-body">
                  <span v-if="board.updatedDate === null" style="font-size: 10px; color: #2e2e2e"> {{ board.createdDate }} </span>
                  <span v-else style="font-size: 10px; color: #2e2e2e"> {{ board.updatedDate }} 수정됨 </span>
                  <h4 class="card-title">
                    <router-link :to="{ name: 'board-details', params: { id: board.boardId, like: board.like, memberId: board.memberId }}"> {{ board.title }} </router-link>
                    <router-view/>
                  </h4>
                  <h6 style="color: #2e2e2e"> by {{ board.nickname }} </h6>
                  <p class="card-text"> {{ board.content }} </p>
                  <a class="button" href="#">Read More <i class="ti-arrow-right"></i></a>
                </div>
                <div class="card-footer">
                  <h6>
                    <img src="../../assets/hit.png" width="30" height="30"> {{ board.hitCount }}
                    <img v-if="board.like === 1" src="../../assets/like.png" width="30" height="30" @click="toUnlike(index)">
                    <img v-else src="../../assets/unlike.png" width="30" height="30" @click="toLike(index)">
                    {{ board.likeCount }}
                    <img src="../../assets/comment.png" width="30" height="30" @click=""> {{ board.commentCount }}
                  </h6>
                </div>
              </div>
            </div>

          </div>
          <!-- /.row -->

        </div>
        <!-- /.col-lg-9 -->

      </div>
      <!-- /.row -->

    </div>
    <!-- /.container -->

  </div>
</template>

<script>
  import http from "@/http-common"

  export default {
    data () {
      return {
        memberId: localStorage.getItem("memberId"),
        bottom: false,
        boards: [],
        page: 0
      }
    },
    methods: {
      addBoards() {
        let page = {
          page: this.page
        }
        http.get('/boards', { params: page })
          .then((res) => {
            if(res.status === 200) {
              for (let i in res.data.content) {
                let board = res.data.content[i];
                if (board.blockStatus === "UNBLOCKED") {
                  this.makeSnippet(board, 1);
                  this.makeSnippet(board, 2);

                  let boardInfo = {
                    boardId: board.boardId,
                    title: board.title,
                    content: board.content,
                    memberId: board.memberId,
                    nickname: board.nickname,
                    createdDate: board.createdDate,
                    updatedDate: board.updatedDate,
                    hitCount: board.hitCount,
                    likeCount: board.likeCount,
                    commentCount: board.commentCount,
                    filePath: board.filePath
                  };

                  if(board.filePath === "" || board.filePath === null) {
                    boardInfo['fileSrc'] = require("../../assets/default.jpg");
                  } else {
                    boardInfo['fileSrc'] = "http://localhost:8081/files/thumb_" + board.filePath;
                  }

                  if(board.likeBoards.length !== 0) {
                    boardInfo['like'] = 1;
                  }
                  this.boards.push(boardInfo);
                }
              }
              this.page += 1;
            }

          }).catch((e) => {
          window.alert(e);
          console.log(e);
        });
      },
      makeSnippet(board, TitleOrContent) {
        if(TitleOrContent === 1) {
          if (board.title.length > 15) {
            board.title = board.title.substring(0, 15) + "...";
          }
        } else if(TitleOrContent === 2) {
          if (board.content.length > 30) {
            board.content = board.content.substring(0, 30) + "...";
          }
        }
      },
      /* 스크롤이 최하단에 도착했는지 확인 */
      bottomVisible() {
        if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
          return true;
        } else {
          return false;
        }
      },
      toUnlike(i) {
        this.boards[i].like = 0;
        this.boards[i].likeCount -= 1;

        http.delete('/boards/' + this.boards[i].boardId + '/like')
          .then((res) => {

          }).catch((e) => {
          window.alert(e);
          console.log(e);
        });

      },
      toLike(i) {
        this.boards[i].like = 1;
        this.boards[i].likeCount += 1;

        http.post('/boards/' + this.boards[i].boardId + '/like')
          .then((res) => {

          }).catch((e) => {
          window.alert(e);
          console.log(e);
        });
      }
    },
    created() {
      if (!localStorage.getItem("memberId") || localStorage.getItem("memberId") === 'undefined') {
        window.alert("로그인이 필요한 서비스입니다.");
        this.$router.push('/login');
      } else {
        this.addBoards();
      }
    },
    mounted() {
      window.addEventListener('scroll', () => {
        this.bottom = this.bottomVisible();
      });
    },
    watch: {
      bottom(bottom) {
        if(bottom) {
          this.addBoards();
        }
      }
    }
  }
</script>
