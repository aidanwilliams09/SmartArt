Account
<template>
  <html lang="en">
    <Taskbar />
    <div id="account" style="padding-top: 20px">
      <h1 style="font-family: Palatino;font-variant: small-caps;"><b>My Account</b></h1>
      <div class="info">
        <p class="header">Name : {{ this.name }}</p>
        <p class="header">Email : {{ this.email }}</p>
        <p class="header" style="padding-bottom: 20px;">User Type : {{ this.userType }}</p>
        <button class="btn btn-danger" @click="logOut"
        >Logout</button>
      </div>
    </div>
    <div v-if="this.userType === 'administrator'" class="pad">
      <b-button
        @click="editCommission"
        pill
        variant="outline-secondary"
        class="pad"
        >Edit Gallery Commission</b-button
      >
    </div>
    <div v-if="this.editCom">
      <div class="inputbox">
        <p>Enter the New Commission [0,1]:</p>
        <input
          type="number"
          class="form-control input-style"
          v-model="commission"
          placeholder="Commission [0,1]"
        />
        <b-button
        @click="changeCommission"
        pill
        variant="outline-secondary"
        class="pad"
        >Confirm Changes</b-button
      >
      <p>{{ this.error }}</p>
      </div>
    </div>
    <p class="listHeader"><b>{{ this.listType }}</b></p>

    <div v-if="this.userType === 'buyer'" ref="section2" style="margin-top: 50px">
      <PurchaseList v-bind:purchaseList="purchaseList" />
    </div>
    <div v-else ref="section2" style="margin-top: 50px">
      <PostingList v-bind:postingList="postingList" />
    </div>
    <div style="padding-bottom: 50px">

    </div>
    <Footer />
  </html>
</template>

<script>
import Taskbar from "./Taskbar";
import PostingList from "./PostingList";
import PurchaseList from "./PurchaseList";
import axios from "axios";
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
  name: "Account",
  data() {
    return {
      email: "",
      listType: "",
      name: "",
      purchaseList: [],
      postingList: [],
      password: "",
      error: "",
      userType: "",
      editCom: false,
      commission: null,
      gallery: "SmartArt",
    };
  },
  components: {
    Taskbar,
    PostingList,
    PurchaseList,
    Footer,
  },
  methods: {
    logOut() {
      this.$store.dispatch("setActiveUser", "");
      this.$store.dispatch("setActiveUserType", "");
      this.$router.push({ name: "Home" });
    },
    editCommission() {
      this.editCom = !this.editCom;
    },
    changeCommission(){
      if(this.commission == null){
        this.error += "Please enter a commission. "
      }
      if(this.commission < 0 || this.commission > 1){
        this.error += "Commission must be between 0 and 1. "
      }
      if(this.error == ""){
        AXIOS({
          method: "put",
          url: "/gallery/update",
          data: {
            name: this.gallery,
            commission: this.commission,
          },
        })
          .then((response) => {
            this.error = "Success"
            this.commission = null
          })
          .catch((e) => {
            var errorMsg = e.message;
            console.log(e);
            this.error = errorMsg;
          });
      }
    }
  },
  created: function () {
    this.email = this.$store.getters.getActiveUser;
    this.userType = this.$store.getters.getActiveUserType;
    if (this.email != "") {
      AXIOS.get("/".concat(this.userType).concat("s/").concat(this.email))
        .then((response) => {
          this.name = response.data.name;
          if (this.userType == "artist") {
            this.listType = "My Postings";
          } else if (this.userType == "buyer") {
            this.listType = "My Purchases";
          } else if (this.userType == "administrator") {
            this.listType = "All Postings";
          }
        })
        .catch((e) => {
          this.error = e;
        });
    }
    if (this.userType == "artist") {
      AXIOS.get("/postings/artist/".concat(this.email))
        .then((response) => {
          this.postingList = response.data;
        })
        .catch((e) => {
          this.error = e;
        });
    } else if (this.userType == "buyer") {
      AXIOS.get("/purchases/buyer/".concat(this.email))
        .then((response) => {
          this.purchaseList = response.data;
        })
        .catch((e) => {
          this.error = e;
        });
    } else if (this.userType == "administrator") {
      AXIOS.get("/postings")
        .then((response) => {
          this.postingList = response.data;
        })
        .catch((e) => {
          this.error = e;
        });
    }
  },
};
</script>

<style scoped>
.info {
  position: relative;
  text-align: center;
  padding: 20px 200px;
}

.header {
  font-size: 20pt;

}
.listHeader {
  font-size: 32pt;
  padding-top: 50px;
  font-family: Palatino;
  font-variant: small-caps;
}

#account {

}
.pad {
  padding: 20px;
  position: relative;
  text-align: center;
  align-self: center;
}
.inputbox {
  position: relative;
  text-align: center;
  align-self: center;
  align-content: center;
  width: 30%;
  padding: 5px 5px;
}
</style>
