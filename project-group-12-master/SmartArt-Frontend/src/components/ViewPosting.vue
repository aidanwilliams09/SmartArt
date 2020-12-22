ViewPosting
<template>
  <html lang="en">
    <Taskbar />
    <div style="padding-left: 100px; padding-right: 100px; padding-top: 50px; padding-bottom: 50px">
      <div class="card-deck">
          <div>
            <img class="viewImage" v-bind:src="this.image"
                 onerror="this.onerror=null;this.src='https://i.ibb.co/RYk5c57/no-Image-Available.png';">
          </div>
        <div class="col-lg-6">
          <div class="card-inner">
          <div class="card viewCard">
            <div class="card-body viewBody">
              <h1 class="card-title"><b>{{ this.title }}   ${{ this.price }}</b></h1>
              <h3 class="card-text">By: {{ this.artistName }}</h3>
            </div>
          </div>
          </div>
          <div class="card-inner">
          <div class="card viewCard">
            <div class="card-body viewBody">
              <p class="card-text">Description: {{ this.description }}</p>
            </div>
          </div>
          </div>
          <div class="card-inner">
          <div class="card viewCard">
            <div class="card-body viewBody">
              <p class="card-text">Dimensions: {{ this.xDim }}x{{ this.yDim }}x{{ this.zDim }}cm</p>
              <p class="card-text">Availability: {{ this.artStatus }}</p>
              <p class="card-text"><small class="text-muted">Posted on: {{ this.date }}</small></p>
              <div v-if="this.userType === 'buyer'">
                <button class="btn btn-danger" @click="addToCart">Add To Cart</button>
              </div>
              <div class="col-md-auto" v-if="(this.userType === 'artist' || this.userType === 'administrator')
              && this.artistEmail === this.email">
                <button class="btn btn-danger viewButtons" @click="updatePosting">Edit Post</button>
                <button class="btn btn-danger viewButtons" @click="deletePosting">Delete Post</button>
              </div>
              <p style="padding-top: 10px">{{ this.message }}</p>
            </div>
          </div>
        </div>
        </div>
      </div>
    </div>
    <div>
      <Footer/>
    </div>

  </html>
</template>


<script>
import Taskbar from "./Taskbar";
import axios from "axios";
import PostingList from "./PostingList";
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
  name: "ViewPosting",
  data() {
    return {
      email: "",
      postingID: 0,
      artist: "",
      artistEmail: "",
      gallery: "SmartArt",
      artistName: "",
      price: "",
      xDim: "",
      yDim: "",
      zDim: "",
      title: "",
      description: "",
      artStatus: "",
      date: "",
      image: "",
      message: "",
      userType: ""
    };
  },
  components: {
    Taskbar,
    Footer,
  },
  created: function () {
    this.email = this.$store.getters.getActiveUser;
    this.postingID = this.$store.getters.getActivePosting;
    this.userType = this.$store.getters.getActiveUserType;
    if(this.userType == 'buyer'){
          AXIOS.get("/buyers/".concat(this.email))
      .then((response) => {
      })
      .catch((e) => {
        this.message =
          "You must be logged in as a buyer to add items to your cart";
      });
    }
    if (this.postingID != 0) {
      AXIOS.get("/postings/".concat(this.postingID))
        .then((response) => {
          this.artistName = response.data.artist.name;
          this.artistEmail = response.data.artist.email;
          this.artist = response.data.artist;
          this.title = response.data.title;
          this.price = response.data.price;
          this.xDim = response.data.xdim;
          this.yDim = response.data.ydim;
          this.zDim = response.data.zdim;
          this.description = response.data.description;
          this.artStatus = response.data.artStatus;
          this.date = response.data.date;
          this.image = response.data.image;
        })
        .catch((e) => {
          this.message = e;
        });
    }
  },
  methods: {
    addToCart: function () {
      if(this.email != ''){
              AXIOS({
        method: "post",
        url: "/purchase/cart/add/".concat(this.postingID),
        data: {
            email: this.email,
            gallery: this.gallery
        },
      })
        .then((response) => {
          this.$router.push({ name: "Home" });
        })
        .catch((e) => {
          var errorMsg = e.message;
          console.log(e);
          this.message = errorMsg;
        });
      }else {
        this.message = "You must be logged in as a buyer to add items to your cart";
      }
    },
    updatePosting: function () {
      this.$store.dispatch("setActivePosting", this.postingID);
      console.log("test");
      this.$router.push({ name: "UpdatePosting" });
    },
    deletePosting: function () {
      if(confirm("Do you really want to delete?")){
        AXIOS({
        method: "delete",
        url: "/posting/delete",
        data: {
          postingID: this.postingID,
          artist: {
            email: this.email,
            gallery: this.gallery
          },
          gallery: this.gallery,
          price: this.price,
          xdim: this.xDim,
          ydim: this.yDim,
          zdim: this.zDim,
          description: this.description,
          artStatus: this.artStatus,
          date: this.date,
          image: this.image
        }
        })
      .then(response => {
        this.$router.push( {name: "Home"} )
      })
        .catch((e) => {
          var errorMsg = e.message;
          console.log(e);
          this.message = errorMsg;
        })
    }}
  },
};
</script>


<style>
.viewImage {
  max-width: 35vw;
  max-height: 80vh;
  object-fit: cover;
}
.viewButtons {
  border-radius: 3px !important;
  border: none !important;
  box-shadow: none !important;
  transform: scale(1);
  transition: all 0.5s ease;
}
.viewButtons:hover {
  box-shadow: 2px 4px 20px 4px #2e2e2d !important;
  transform: scale(1.1) perspective(1px)
}

.card-inner {
  margin-bottom: 3rem;
}

.viewCard {
  box-shadow: 4px 8px 20px 4px #2e2e2d;
}

.viewBody {
  background-color: #F0F0F0;
}

</style>
