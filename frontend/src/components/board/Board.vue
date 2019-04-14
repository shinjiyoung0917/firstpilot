<template>
  <div>
    <app-header></app-header>

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
            <button class="list-group-item" style="color: #2e2e2e"> 인기순 </button>
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
                <a href="#"><img class="card-img-top" src="http://placehold.it/700x400" alt=""></a>
                <div class="card-body">
                  <h10> 등록날짜 {{ board.createdDate }} </h10>
                  <h4 class="card-title">
                    <router-link  :to="{ name: 'board-details', params: { id: board.boardId }}"> {{ board.title }} </router-link>
                    <router-view/>
                  </h4>
                  <h7> {{ board.nickname }} </h7>
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
                  <!--<small class="text-muted">&#9733; &#9733; &#9733; &#9733; &#9734;</small> 별-->
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
    data () {
      return {
        memberId: sessionStorage.getItem("memberId"),
        bottom: false,
        boards: [],
        likeBoards: [],
        page: 0,
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
              //if (this.page !== 0) {
                for (let i in res.data.content) {
                  let board = res.data.content[i];
                  if (board.isValid === 1) {
                    let boardInfo = {
                      boardId: board.boardId,
                      title: board.title,
                      content: board.content,
                      nickname: board.nickname,
                      createdDate: board.createdDate,
                      updatedDate: board.updatedDate,
                      hitCount: board.hitCount,
                      likeCount: board.likeCount,
                      commentCount: board.commentCount,
                      filePath: board.filePath
                    };

                    for(let j in this.likeBoards) {
                      if(boardInfo.boardId === this.likeBoards[j].boardId) {
                        boardInfo['like'] = 1;
                        //window.alert("boardInfo : " + JSON.stringify(boardInfo));
                      }
                    }
                    this.boards.push(boardInfo);
                  }
                }
              /*} else {
                this.boards = res.data.content;
              }*/
              this.page += 1;

              /*if(this.bottomVisible()) {
                this.addBoards();
              }*/
            }

          }).catch((e) => {
          window.alert(e);
          console.log(e);
        });
      },
      /* 스크롤이 최하단에 도착했는지 확인 */
      bottomVisible() {
        if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
          return true;
        } else {
          return false;
        }
        /*let scrollY = window.pageYOffset;
        let visible = document.documentElement //document.documentElement.clientHeight;
        let pageHeight = document.documentElement.scrollHeight;
        let bottomOfPage = visible + scrollY >= pageHeight;
        return bottomOfPage || pageHeight < visible;*/
      },
      /* 현재 로그인한 회원이 좋아요를 누른 게시물 목록 요청 */
      getLikeBoards() {
        /*let memberId = {
          memberId: this.memberId
        }*/
        http.get('/boards/likes') //{ params: memberId }
          .then((res) => {
            this.likeBoards = res.data;
          }).catch((e) => {
          window.alert(e);
          console.log(e);
        });
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
      this.getLikeBoards();
      this.addBoards();
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

    /*created() {
      window.addEventListener('scroll', () => {
        this.bottom = this.bottomVisible();
      });

      this.addBoards();
    },
    watch: {
      bottom(bottom) {
        if(bottom) {
          this.addBoards();
        }
      }
    },
    mounted() {
    }*/
  }
</script>

