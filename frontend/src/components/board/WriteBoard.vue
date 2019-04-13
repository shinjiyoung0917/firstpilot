<template>
  <div>
    <app-header></app-header>

    <!-- Page Content -->
    <div class="container">

      <div class="row">

        <!-- Post Content Column -->
        <div class="col-lg-8">

          <!-- Title -->
          <input type="text" class="mt-4 form-control" placeholder="제목을 입력해주세요." v-model="title" />

          <!-- Author -->
          <p class="lead">
            by {{ this.nickname }}
          </p>

          <hr>

          <!-- Preview Image -->
          <img class="img-fluid rounded" src="http://placehold.it/900x300" alt=""> <!-- 기본 이미지 넣기 -->

          <hr>

          <!-- Post Content -->
          <textarea class="mt-4 form-control" rows="5" placeholder="내용을 입력해주세요." v-model="content"></textarea>

          <hr>

          <!-- File -->
          <input type="file" id="uploadFile" name="uploadFile" @change="setFileData($event.target.files)">

          <hr>

          <!-- Write Button -->
          <div style="padding-bottom: 50px;">
            <button class="btn btn-primary" @click="write"> 등록 </button>
            <router-link to="/boards" class="btn btn-primary"> 취소 </router-link>
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
        memberId: sessionStorage.getItem("memberId"),
        nickname: sessionStorage.getItem("nickname"),
        title: '',
        content: '',
        fileData: '',
        filePath: ''
      }
    },
    methods: {
      write() {
        if(this.title === null || this.title === "") {
          window.alert("제목을 입력해주세요.")
        } else if(this.content === null || this.content === "") {
          window.alert("내용을 입력해주세요.")
        } else {
          let bodyFormData = new FormData();
          bodyFormData.append('file', this.fileData);

          if(this.fileData !== '') {    // 업로드할 파일이 있을 경우
            let bodyFormData = new FormData();
            bodyFormData.set('uploadFile', this.fileData);

            // 파일 서버 디렉토리에 저장
            httpFile.post('/boards/file', bodyFormData)
              .then((res) => {
                if (res.status === 200) {
                  this.filePath = res.data;
                  this.writeData();
                }
              }).catch((e) => {
              window.alert(e);
              console.log(e);
            });
          } else {                      // 업로드할 파일이 없을 경우
            this.writeData();
          }
        }
      },
      /* 파일 데이터를 제외한 나머지 게시판 데이터 등록 요청 */
      writeData() {
        let data = {
          memberId: this.memberId,
          nickname: this.nickname,
          title: this.title,
          content: this.content,
          filePath: this.filePath
        }

        http.post('/boards', data)
          .then((res) => {
            if(res.status === 200) {
              window.alert("게시물 등록을 성공적으로 완료하였습니다.");
              this.$router.push('/boards');
            }
          }).catch((e) => {
          window.alert(e);
          console.log(e);
        });
      },
      /* 선택한 파일 데이터 가져오기 */
      setFileData(files) {
        if(files.length) {
          this.fileData = files[0];
        }
      }
    },
    mounted() {
    }
  }
</script>
