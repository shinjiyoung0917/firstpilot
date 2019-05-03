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
          if(this.fileData !== '') {    // 업로드할 파일이 있을 경우
            let bodyFormData = new FormData();
            bodyFormData.set('uploadFile', this.fileData);

            httpFile.post('/boards/file', bodyFormData)
              .then((res) => {
                if (res.status === 200) {
                  this.filePath = res.data;
                  this.writeData("HAVE_FILE");
                }
              }).catch((e) => {
              window.alert(e);
              console.log(e);
            });
          } else {
            this.writeData("NO_FILE");
          }
        }
      },
      setFileData(files) {
        if(files.length) {
          this.fileData = files[0];
        }
      },
      writeData(filePresence) {
        let data = {
          memberId: this.memberId,
          nickname: this.nickname,
          title: this.title,
          content: this.content,
        };
        if(filePresence === "HAVE_FILE") {
          data['filePath'] = this.filePath;
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
      }
    },
    created() {
      if (!sessionStorage.getItem("memberId") || sessionStorage.getItem("memberId") === 'undefined') {
        window.alert("로그인이 필요한 서비스입니다.");
        this.$router.push('/login');
      }
    }
  }
</script>
