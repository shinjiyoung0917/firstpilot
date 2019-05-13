<template>
  <div>

    <!-- Page Content -->
    <div class="container">

      <div class="row">

        <div class="col-lg-3" style="margin-top: 50px;">

          <h1 class="my-1"> 대시보드 </h1>
          <div class="list-group" style="margin-top: 20px;">
            <button @click="addBoards" class="list-group-item" style="background-color: #AEBDCC; color: #2e2e2e"> 나의 게시물 </button>
          </div>
          <div class="list-group" style="margin-top: 20px;">
            <button @click="addComments" class="list-group-item" style="background-color: #AEBDCC; color: #2e2e2e"> 내가 작성한 댓글 </button>
          </div>
          <div class="list-group" style="margin-top: 20px;">
            <button @click="showNicknameEditArea" class="list-group-item" style="background-color: #AEBDCC; color: #2e2e2e"> 닉네임 수정 </button>
          </div>
          <div class="list-group" style="margin-top: 20px;">
            <button @click="withdrawal" class="list-group-item" style="background-color: #AEBDCC; color: #2e2e2e"> 회원탈퇴 </button>
          </div>

        </div>
        <!-- /.col-lg-3 -->

        <div class="col-lg-9" style="padding-top: 30px;">

          <div class="carousel slide my-4" data-ride="carousel" id="nicknameField" style="display: block">
            <div class="form-group has-success">
              <h2 class="text-center text-uppercase text-secondary mb-0"> 나의 닉네임  {{ this.nickname }}</h2>
              <!-- <input type="text" class="form-control" id="inputSuccess4"> -->
            </div>
          </div>

          <div class="form-group floating-label-form-group controls mb-0 pb-2" id="nicknameEditField" style="display: none">
            <input type="text" class="form-control" id="name" aria-invalid="false" required="required" v-model="editedNickname" data-validation-required-message="닉네임을 입력해주세요.">
            <p class="help-block text-danger"></p>
            <button class="btn btn-dark" @click="editNickname"> 수정 </button>
            <button class="btn btn-dark" @click="hideNicknameEditArea"> 취소 </button>
          </div>

          <hr class="star-dark mb-5">

          <!-- 게시물 리스트 시작 -->
          <div class="row" v-if="this.boardsOrComments === 1">

            <div class="col-lg-4 col-md-6 mb-4" v-for="(board, index) in boards">
              <div class="card h-100">
                <img :src="board.fileSrc"> <!--class="card-img-top" -->
                <div class="card-body">
                  <span v-if="board.updatedDate === null" style="font-size: 10px; color: #2e2e2e"> {{ board.createdDate }} </span>
                  <span v-else style="font-size: 10px; color: #2e2e2e"> {{ board.updatedDate }} </span>
                  <h4 class="card-title">
                    <router-link  :to="{ name: 'board-details', params: { id: board.boardId, like: board.like, memberId: board.memberId }}"> {{ board.title }} </router-link>
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
                  <!--<small class="text-muted">&#9733; &#9733; &#9733; &#9733; &#9734;</small> 별-->
                </div>
              </div>
            </div>

            <div v-if="this.boards.length === 0">
              <!-- 게시물 없는 이미지 -->
              <span style="font-size: 30px">
                아직 작성하신 글이 없네요. <br>
                게시물을 등록하여 회원들과 생각을 공유해보세요.
              </span>
            </div>

          </div>
          <!-- /.row -->

          <!-- 댓글 리스트 시작 -->
          <div class="row" v-if="this.boardsOrComments === 2">
            <div class="container">
              <div class="list-group">
              <div v-for="comment in comments">

                <router-link :to="{ name: 'board-details', params: { id: comment.boardId }}" class="list-group-item">
                  <h4 class="list-group-item-heading"> {{ comment.content }} </h4>
                  <p v-if="comment.updatedDate === null" class="list-group-item-text"> {{ comment.createdDate }} </p>
                  <p v-else class="list-group-item-text"> {{ comment.updatedDate }} </p>
                </router-link>

              </div>
              </div>
            </div>
          </div>

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
  import {BUS} from '@/EventBus'

  export default {
    data () {
      return {
        nickname: localStorage.getItem("nickname"),
        editedNickname: localStorage.getItem("nickname"),
        memberId: localStorage.getItem("memberId"),
        member: '',
        boards: [],
        bottom: false,
        page: 0,
        comments: [],
        boardsOrComments: 1
      }
    },
    methods: {
      addBoards() {
        this.boardsOrComments = 1;
        let page = {
          page: this.page
        };

        http.get('/dashboards/boards', { params: page })
          .then((res) => {
            if(res.status === 200) {
              for (let i in res.data.content) {
                let board = res.data.content[i];

                if (board.blockStatus === 'UNBLOCKED') {
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
                  this.member = board.member;
                }
              }
              this.page += 1;
            }

          }).catch((e) => {
          if(e.response.data.path === "/login") {
            this.$router.replace(e.response.data.path);
          }
          console.log(e);
        });
      },
      makeSnippet(data, TitleOrContent) {
        if(TitleOrContent === 1) {
          if (data.title.length > 15) {
            data.title = data.title.substring(0, 15) + "...";
          }
        } else if(TitleOrContent === 2) {
          if (data.content.length > 30) {
            data.content = data.content.substring(0, 30) + "...";
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
          window.alert(e.response.data);
          console.log(e.response.data);
        });

      },
      toLike(i) {
        this.boards[i].like = 1;
        this.boards[i].likeCount += 1;

        http.post('/boards/' + this.boards[i].boardId + '/like')
          .then((res) => {

          }).catch((e) => {
          window.alert(e.response.data);
          console.log(e.response.data);
        });
      },
      addComments() {
        this.boardsOrComments = 2;

        http.get('/dashboards/comments')
          .then((res) => {
            if(res.status === 200) {
              for (let i in res.data) {
                let comment = res.data[i];

                if (comment.blockStatus === 'UNBLOCKED') {
                  this.makeSnippet(comment, 2);

                  let commentInfo = {
                    content: comment.content,
                    createdDate: comment.createdDate,
                    updatedDate: comment.updatedDate,
                    filePath: comment.filePath,
                    boardId: comment.boardId
                  };

                  this.comments.push(commentInfo);
                  this.comments.reverse();
                }
              }
            }
          }).catch((e) => {
          if(e.response.data.path === "/login") {
            this.$router.replace(e.response.data.path);
          }
          console.log(e);
        });
      },
      showNicknameEditArea() {
        http.get('/members/' + this.memberId + '/change-period')
          .then((res) => {
            if(res.status === 200) {
              if(res.data === true) {
                document.getElementById("nicknameField").style.display = 'none';
                document.getElementById("nicknameEditField").style.display = 'block';
              } else {
                window.alert("닉네임을 변경하신지 7일 이내로, 변경이 불가합니다.");
              }
            }
          }).catch((e) => {
          window.alert(e.response.data);
          console.log(e.response.data);
        });
      },
      hideNicknameEditArea() {
        document.getElementById("nicknameField").style.display = 'block';
        document.getElementById("nicknameEditField").style.display = 'none';
      },
      editNickname() {
        let data = {
          nickname: this.editedNickname
        };

        http.put('/members', data)
          .then((res) => {
            if(res.status === 200) {
              if(res.data === null || res.data === "") {
                window.alert("닉네임을 변경할 수 없습니다.");
                this.nickname = localStorage.getItem("nickname");
              } else {
                window.alert("닉네임 수정이 성공적으로 완료되었습니다.");
              }
              this.hideNicknameEditArea();
              this.nickname = this.editedNickname;
              BUS.$emit('nickname:update', this.nickname);
            }
          }).catch((e) => {
          window.alert(e.response.data);
          console.log(e.response.data);
          this.nickname = localStorage.getItem("nickname");
        });
      },
      withdrawal() {
        let confirmFlag = confirm("회원탈퇴를 진행하시겠습니까?\n오직 회원만 서비스 이용가능합니다.");

        if(confirmFlag) {
          http.delete('/members')
            .then((res) => {
              if (res.status === 200) {
                window.alert("회원탈퇴를 성공적으로 진행하였습니다.\n지금까지 이용해주셔서 감사합니다.");
                this.removeSession();
                BUS.$emit('bus:call', '');
              }
            }).catch((e) => {
            window.alert(e.response.data);
            console.log(e.response.data);
          });
          this.$router.replace('/');
        } else {
        }
      },
      removeSession() {
        http.post('/logout')
          .then((res) => {
            if(res.status === 200) {
              localStorage.removeItem("nickname"); // 도메인 키와 데이터 모두 삭제, 특정 세션 삭제
              localStorage.removeItem("memberId");
              localStorage.clear();               // 저장된 모든 값 삭제, 세션 전체 삭제
            }
          }).catch((e) => {
          window.alert(e.response.data);
          console.log(e.response.data);
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
        if(bottom && this.boardsOrComments === 1) {
          this.addBoards();
        }
      }
    }
  }
</script>
