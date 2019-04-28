<template>
  <div>
    <app-header></app-header>

    <!-- Page Content -->
    <div class="container">

      <div class="row">

        <!-- Post Content Column -->
        <div class="col-lg-8">
          <div style="text-align: right" v-if="this.board.memberId === memberId">
            <router-link class="btn btn-primary" :to="{ name: 'edit-board', params: { id: this.boardId }}">  수정 </router-link>
            <router-view/>
            <button class="btn btn-primary" @click="deleteBoard"> 삭제 </button>
          </div>

          <!-- Title -->
          <h1 class="mt-4"> {{ this.board.title }} </h1>

          <!-- Author -->
          <p class="lead">
            by {{ this.board.nickname }}
          </p>

          <hr>

          <!-- Date/Time -->
          <p v-if="this.board.updatedDate === null">Posted on {{ this.board.createdDate }} </p>
          <p v-else>Posted on {{ this.board.updatedDate }} 수정됨 </p>

          <hr>

          <!-- Preview Image -->
          <img :src="this.board.fileSrc">

          <hr>

          <!-- Post Content -->
          <p class="lead">
            {{ this.board.content }}
          </p>

          <hr>

          <div>
            <h6>
              <img src="../../assets/hit.png" width="30" height="30"> {{ this.board.hitCount }}
              <img v-if="this.board.like === 1" src="../../assets/like.png" width="30" height="30" @click="toUnlike">
              <img v-else src="../../assets/unlike.png" width="30" height="30" @click="toLike">
              {{ this.board.likeCount }}
              <img src="../../assets/comment.png" width="30" height="30"> {{ this.board.commentCount }}
            </h6>
          </div>

          <!-- Comments Form -->
          <div class="card my-4">
            <h5 class="card-header">댓글을 남겨주세요:</h5>

            <div class="card-body">
              <!--<form>-->
              <div class="form-group">
                <textarea class="form-control" rows="3" v-model="content"></textarea>
              </div>
              <div style="text-align: right">
                <input type="file" id="uploadFileForComment" name="uploadFile" @change="setFileData($event.target.files)">
                <button class="btn btn-primary" @click="write(null, null)">등록</button>
              </div>
              <!--</form>-->
            </div>

          </div>

          <div v-if="this.comments.length !== 0">
            <div><button class="list-group-item" @click="resortComments()" style="color: #2e2e2e"> 시간순 </button></div>
            <div><button class="list-group-item" @click="resortComments()" style="color: #2e2e2e"> 최신순 </button></div>
          </div>

          <hr>

          <!-- 댓글, 대댓글 -->
          <!-- 댓글 -->
          <div v-for="(comment, index) in comments">
            <div class="media mb-4" >

              <img class="d-flex mr-3 rounded-circle" src="http://placehold.it/50x50" alt="">

              <div class="media-body" style="text-align: left">

                <h5 class="mt-0"> {{ comment.nickname }} </h5>
                <div :id="'existedComment' + index" style="display: block">
                  <div>
                    {{ comment.content }}
                    <br>

                    <span v-if="comment.updatedDate === null" class="mt-0" style="font-size: 10px"> {{ comment.createdDate }} </span>
                    <span v-else class="mt-0" style="font-size: 10px"> {{ comment.updatedDate }} 수정됨 </span>

                    <button class="btn btn-group-toggle" @click="showChildCommentInputArea(index)" style="font-size: small"> 대댓글 달기 </button>

                    <img :src="comment.fileSrc">
                  </div>

                  <div :id="'editAndDeleteButton' + index" style="text-align: right" v-if="comment.memberId === memberId && comment.blockStatus === 'UNBLOCKED' ">
                    <button class="btn btn-dark" @click="showCommentEditArea(index, comment.content, null)"> 수정 </button>
                    <button class="btn btn-dark" @click="deleteComment(index, comment.commentId, null)"> 삭제 </button>
                  </div>
                </div>

                <!-- 댓글 수정란 -->
                <div :id="'editComment' + index" style="display: none">
                  <textarea v-model="editContent" rows="3" style="width: 100%"></textarea>
                  <input type="file" id="uploadFileForEditComment" name="uploadFile" @change="setFileData($event.target.files)">
                  <img :src="comment.fileSrc">

                  <button class="btn btn-dark" @click="editComment(index, comment.commentId, null)"> 수정 </button>
                  <button class="btn btn-dark" @click="hideCommentEditArea(index, null)"> 취소 </button>
                </div>


                <!-- 대댓글 -->
                <div>
                  <div v-for="(childComment, index) in childComments">
                    <div class="media mt-4" v-if="childComment.parentId === comment.commentId"> <!-- 이 부분에 이전 댓글 더 보기 버튼 만들기 -->
                      <div v-if="comment.childCount > 3">
                        <button class="list-group-item" @click="addChildComments(index, comment.commentId)" style="color: rgb(46, 46, 46);"> 이전 댓글 더보기 </button>
                      </div>

                      <img class="d-flex mr-3 rounded-circle" src="http://placehold.it/50x50" alt="">
                      <div class="media-body">
                        <h5 class="mt-0"> {{ childComment.nickname }} </h5>
                        <div :id="'existedChildComment' + index" style="display: block">
                          <div>
                            {{ childComment.content }}
                            <br>

                            <span v-if="childComment.updatedDate === null" class="mt-0" style="font-size: 10px"> {{ childComment.createdDate }} </span>
                            <span v-else class="mt-0" style="font-size: 10px"> {{ childComment.updatedDate }} 수정됨 </span>

                            <img :src="childComment.fileSrc">
                          </div>
                          <div style="text-align: right" v-if="childComment.memberId === memberId && childComment.blockStatus === 'UNBLOCKED' ">
                            <button class="btn btn-dark" @click="showCommentEditArea(index, childComment.content, childComment.parentId)"> 수정 </button>
                            <button class="btn btn-dark" @click="deleteComment(index, childComment.commentId, childComment.parentId)"> 삭제 </button>
                          </div>
                        </div>

                        <!-- 대댓글 수정란 -->
                        <div :id="'editChildComment' + index" style="display: none">
                          <textarea v-model="editContent" rows="3" style="width: 100%"></textarea>
                          <input type="file" id="uploadFileForEditChildComment" name="uploadFile" @change="setFileData($event.target.files)">
                          <img :src="childComment.fileSrc">

                          <button class="btn btn-dark" @click="editComment(index, childComment.commentId, childComment.parentId)"> 수정 </button>
                          <button class="btn btn-dark" @click="hideCommentEditArea(index, childComment.parentId)"> 취소 </button>
                        </div>

                      </div>
                    </div>
                  </div>

                  <!-- 대댓글 입력창 -->
                  <div :id="index" style="display: none">
                    <textarea v-model="childContent" :id="index" rows="3" style="width: 100%"></textarea>
                    <input type="file" id="uploadFileForChildComment" name="uploadFile" @change="setFileData($event.target.files)">
                    <button class="btn btn-dark" @click="write(comment.commentId, index)"> 등록 </button>
                    <button class="btn btn-dark" @click="hideChildCommentInputArea(index)"> 취소 </button>
                  </div>

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
        memberId: '',
        boardId: this.$route.params.id,
        board: '',
        likeBoard: '',
        comments: [],
        childComments: [],
        content: '',
        childContent: '',
        editContent: '',
        isEditing: 0,
        fileData: '',
        filePath: ''
      }
    },
    methods: {
      getBoardDetails() {
        http.get('/boards/' + this.boardId)
          .then((res) => {
            if(res.status === 200) {
              window.alert("=> " + JSON.stringify(res.data));
              window.alert("=> " + JSON.stringify(res.data.likeBoards));

              this.board = res.data;

              //this.board.member.likeBoards

              if(this.board.filePath === "" || this.board.filePath === null) {
                this.board['fileSrc'] = require("../../assets/default.jpg");
              } else {
                this.board['fileSrc'] = "http://localhost:8081/files/" + this.board.filePath;
              }
              if(this.likeBoard === 1) {
                this.board['like'] = 1;
              }

              for(let i in this.board.comments) {
                let comment = this.board.comments[i];
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
                    filePath: comment.filePath,
                    blockStatus: comment.blockStatus
                  };
                  if(comment.filePath !== "" && comment.filePath !== null)  {
                    commentInfo['fileSrc'] = "http://localhost:8081/files/thumb_" + comment.filePath;
                  }
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
                    filePath: comment.filePath,
                    blockStatus: comment.blockStatus
                  };
                  if(comment.filePath !== "" && comment.filePath !== null)  {
                    childCommentInfo['fileSrc'] = "http://localhost:8081/files/thumb_" + comment.filePath;
                  }
                  this.childComments.unshift(childCommentInfo);
                }
              }
              this.memberId = Number(sessionStorage.getItem("memberId"));
            }
          }).catch((e) => {
          window.alert(e);
          console.log(e);
        });
      },
      requestCheckLikeBoard() {
        http.get('/boards/likes')
          .then((res) => {
            this.likeBoard = res.data;
          }).catch((e) => {
          window.alert(e);
          console.log(e);
        });
      },
      deleteBoard() {
        let confirmFlag = confirm("해당 게시물을 삭제하시겠습니까?");

        if(confirmFlag) {
          http.delete('/boards/' + this.boardId)
            .then((res) => {
              if (res.status === 200) {
                window.alert("게시물을 성공적으로 삭제하였습니다.");
                this.$router.replace('/boards');
              }
            }).catch((e) => {

          });
        } else {
        }
      },
      toUnlike() {
        this.board.like = 0;
        this.board.likeCount -= 1;

        http.delete('/boards/' + this.boardId + '/like')
          .then((res) => {

          }).catch((e) => {
          window.alert(e);
          console.log(e);
        });
      },
      toLike() {
        this.board.like = 1;
        this.board.likeCount += 1;

        http.post('/boards/' + this.boardId + '/like')
          .then((res) => {

          }).catch((e) => {
          window.alert(e);
          console.log(e);
        });
      },
      /* 댓글 등록 요청 (파일 먼저 서버에 저장) */
      write(parent, index) {
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
                if(parent === null) {
                  this.writeComment("HAVE_FILE");
                } else {
                  this.writeChildComment(parent, index, "HAVE_FILE");
                }
              }
            }).catch((e) => {
            window.alert(e);
            console.log(e);
          });
        } else {
          if(parent === null) {
            this.writeComment("NO_FILE");
          } else {
            this.writeChildComment(parent, index, "NO_FILE");
          }
        }
      },
      /* 선택한 파일 데이터 가져오기 */
      setFileData(files) {
        if(files.length) {
          this.fileData = files[0];
        }
      },
      /* 파일 데이터를 제외한 나머지 댓글 정보 등록 요청 */
      writeComment(filePresence) {
        if(filePresence === "HAVE_FILE") {
          var data = {
            boardId: this.boardId,
            memberId: this.memberId,
            content: this.content,
            nickname: sessionStorage.getItem("nickname"),
            parentId: null,
            filePath: this.filePath
          };
        } else if(filePresence === "NO_FILE") {
          var data = {
            boardId: this.boardId,
            memberId: this.memberId,
            content: this.content,
            nickname: sessionStorage.getItem("nickname"),
            parentId: null
          };
        }
        http.post('/boards/' + this.boardId + '/comments', data)
          .then((res) => {
            if (res.status === 200) {
              window.alert("댓글 등록을 성공적으로 완료하였습니다.");
              this.content = '';
              this.board.commentCount += 1;
              this.showComments(res.data);
            }
          }).catch((e) => {
          window.alert(e);
          console.log(e);
        });
      },
      /* 파일 데이터를 제외한 나머지 대댓글 정보 등록 요청 */
      writeChildComment(parent, index) {
        let data = {
          boardId: this.boardId,
          memberId: this.memberId,
          content: this.childContent,
          nickname: sessionStorage.getItem("nickname"),
          parentId: parent,
          filePath: this.filePath
        }

        http.post('/boards/' + this.boardId + '/comments', data)
          .then((res) => {
            if (res.status === 200) {
              window.alert("대댓글 등록을 성공적으로 완료하였습니다.");
              this.childContent = '';
              document.getElementById(index).style.display = 'none';
              this.board.commentCount += 1;
              this.showChildComments(res.data);
            }
          }).catch((e) => {
          window.alert(e);
          console.log(e);
        });
      },
      showComments(commentsData) {
        if(commentsData.filePath !== "" && commentsData.filePath !== null)  {
          commentsData['fileSrc'] = "http://localhost:8081/files/thumb_" + commentsData.filePath;
        }
        this.comments.push(commentsData);
      },
      showChildComments(commentsData) {
        if(commentsData.filePath !== "" && commentsData.filePath !== null)  {
          commentsData['fileSrc'] = "http://localhost:8081/files/thumb_" + commentsData.filePath;
        }
        this.childComments.push(commentsData);
      },
      resortComments() {
        this.comments.reverse();
      },
      showChildCommentInputArea(index) {
        document.getElementById(index).style.display = 'block';
      },
      hideChildCommentInputArea(index) {
        document.getElementById(index).style.display = 'none';
      },
      editComment(index, commentId, parent) {
        window.alert(index + ", " + commentId + ", " + parent);
        let data = {
          memberId: this.memberId,
          //nickname: sessionStorage.getItem("nickname"),
          content: this.editContent,
          filePath: this.comments[index].filePath,
          parentId: this.comments[index].parentId,
          childCount: this.comments[index].childCount,
          createdDate: this.comments[index].createdDate,
          // 파일이름 객체 넣어야함
          blockStatus: this.comments[index].blockStatus
        }

        http.put('/boards/' + this.boardId + '/comments/' + commentId, data)
          .then((res) => {
            if(res.status === 200) {
              window.alert('댓글이 성공적으로 수정되었습니다.');
              if(parent === null) {
                this.comments[index].content = this.editContent;
              } else {
                this.childComments[index].content = this.editContent;
              }
              this.hideCommentEditArea(index, parent);
            }
          }).catch((e) => {
          window.alert(e);
          console.log(e);
        });
      },
      showCommentEditArea(index, content, parent) {
        if(this.isEditing !== 1) {
          this.isEditing = 1;
          this.editContent = content;
          if (parent === null) {
            document.getElementById("existedComment" + index).style.display = 'none';
            document.getElementById("editComment" + index).style.display = 'block';
          } else {
            document.getElementById("existedChildComment" + index).style.display = 'none';
            document.getElementById("editChildComment" + index).style.display = 'block';
          }
        } else {
          window.alert("현재 수정중인 댓글이 존재합니다.");
        }
      },
      hideCommentEditArea(index, parent) {
        this.isEditing = 0;
        this.editContent = '';
        if(parent === null) {
          document.getElementById("existedComment" + index).style.display = 'block';
          document.getElementById("editComment" + index).style.display = 'none';
        } else {
          document.getElementById("existedChildComment" + index).style.display = 'block';
          document.getElementById("editChildComment" + index).style.display = 'none';
        }
      },
      deleteComment(index, commentId, parent) {
        let confirmFlag = confirm("해당 게시물을 삭제하시겠습니까?");

        if(confirmFlag){
          http.delete('/boards/' + this.boardId + '/comments/' + commentId)
            .then((res) => {
              if(res.status === 200) {
                window.alert('댓글이 성공적으로 삭제되었습니다.');
                document.getElementById("editAndDeleteButton" + index).style.display = 'none';
                if(parent === null) {
                  this.comments[index].content = "삭제된 댓글입니다.";
                } else {
                  this.childComments[index].content = "삭제된 댓글입니다.";
                }
                this.board.commentCount -= 1;
              }
            }).catch((e) => {
            window.alert(e);
            console.log(e);
          });
        } else{
        }
      },
      addChildComments(index, parent) {

      }
    },
    created() {
      if (!sessionStorage.getItem("memberId") || sessionStorage.getItem("memberId") === 'undefined') {
        window.alert("로그인이 필요한 서비스입니다.");
        this.$router.push('/login');
      } else {
        this.requestCheckLikeBoard();
        this.getBoardDetails();
      }
    }
  }
</script>
