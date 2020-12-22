Home
<!DOCTYPE html>
<template>
  <html style="color: white">
      <body style="align-items: center; text-align: center">
      <div id="splashpage" style="height: 100vh">
        <Taskbar/>
        <h1 id=quote>
          <b>Your Gallery,<br> Reimagined.</b>
        </h1>
        <div>
          <a class="ct-btn-scroll" @click="scrollMeTo('section2')">
            <img src="https://raw.githubusercontent.com/solodev/scroll-down-anchor/master/images/arrow-down-1.png">
          </a>
        </div>
      </div>
      <div ref="section2" style="margin-top: 50px">
        <PostingList v-bind:postingList="postingList" />
      </div>
        <Footer/>
    </body>
  </html>
</template>

<script>
import axios from "axios";
import PostingList from "./PostingList";
import Taskbar from "./Taskbar";
import Footer from "./Footer";
var config = require("../../config");

var frontendUrl = "https://" + config.build.host + ":" + config.build.port;
var backendUrl =
  "https://" + config.build.backendHost + ":" + config.build.backendPort;

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { "Access-Control-Allow-Origin": frontendUrl },
});
export default {
  name: "SmartArt",
  city: "Montreal",
  commission: "",
  data() {
    return {
      slide: 0,
      sliding: null,
      artists: [],
      buyers: [],
      admins: [],
      postingList: [],
      errorArtist: "",
      errorBuyer: "",
      errorPosting: "",
      errorAdmin: "",
      response: [],
    };
  },
  components: {
    PostingList,
    Taskbar,
    Footer,
  },
  created: function () {
    AXIOS.get("/artists")
      .then((response) => {
        this.artists = response.data;
      })
      .catch((e) => {
        this.errorArtist = e;
      });
    AXIOS.get("/buyers")
      .then((response) => {
        this.buyers = response.data;
      })
      .catch((e) => {
        this.errorBuyer = e;
      });
    AXIOS.get("/administrators")
      .then((response) => {
        this.admins = response.data;
      })
      .catch((e) => {
        this.errorAdmin = e;
      });
    AXIOS.get("/postings")
      .then((response) => {
        this.postingList = response.data;
      })
      .catch((e) => {
        this.errorPosting = e;
      });
  },
  methods: {
    scrollMeTo(refName) {
      var element = this.$refs[refName];
      var top = element.offsetTop;

      window.scrollTo({
        top: top,
        left: 0,
        behavior: 'smooth'
      });
    },
  }
};
</script>

<style>
html {
  scroll-behavior: smooth;
}
body {
  overflow: scroll;
}

#welcome h2 {
  text-align: center;
  margin-top: 15px;
  margin-bottom: 10px;
  font-size: 37px;
}
#welcome h3 {
  text-align: center;
  margin-top: 5px;
  margin-bottom: 0px;
  font-size: 30px;
  font-family: Lucida;
  font-style: oblique;
  color: #d21f3c;
}

#postingCard {
  padding-left: 100px;
  padding-right: 100px;
  padding-top: 1px;
  padding-bottom: 1px;
}

#quote {
  text-align: center;
  padding-top: 8vh;
  font-family: Palatino;
  font-variant: small-caps;
  font-size: 10vh
}

#splashpage {
  background-image: url("https://images.unsplash.com/photo-1518998053901-5348d3961a04?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3367&amp;q=80");
  height: 100%;
  background-repeat: no-repeat;
  background-size: cover;
}

.ct-btn-scroll {
  width: 49px;
  height: 49px;
  position: absolute;
  bottom: 15%;
  left: 0;
  right: 0;
  margin: auto;
  padding-top: 50px;
  z-index: 9;
  border-radius: 50%;
}
</style>


