View Purchase
<template>
  <html lang="en">
    <Taskbar />
    <div id="account">
      <h2>Purchase: {{ this.purchaseID }}</h2>
      <div class="info">
        <p class="header">Total Price : ${{ this.totalPrice }}</p>
        <p class="header">Purchased at : {{ this.time }}</p>
      </div>
    </div>
    <div v-if="this.cancelWindow <= this.time" class="pad">
      <b-button
        @click="cancelPurchase"
        pill
        variant="outline-secondary"
        class="btn btn-danger"
        >Cancel Purchase</b-button
      >
    </div>
    <p class="listHeader">Postings</p>
    <div ref="section2" style="margin-top: 50px">
      <PostingList v-bind:postingList="postingList" />
    </div>
    <Footer />
  </html>
</template>

<script>
import Taskbar from "./Taskbar";
import PostingList from "./PostingList";
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
  name: "ViewPurchase",
  data() {
    return {
      purchaseID: null,
      totalPrice: null,
      time: "",
      email: "",
      gallery: "SmartArt",
      postingList: [],
      error: "",
      cancelWindow: null,
    };
  },
  components: {
    Taskbar,
    PostingList,
    Footer,
  },
  methods: {
    cancelPurchase() {
      AXIOS({
        method: "delete",
        url: "/purchase/cancel",
        data: {
          purchaseID: this.purchaseID,
          buyer: {
            email: this.email,
            gallery: this.gallery,
          },
        },
      })
        .then((response) => {
          this.$router.push({ name: "Account" });
        })
        .catch((e) => {
          console.log(e);
        });
    },
  },
  created: function () {
    this.purchaseID = this.$store.getters.getActivePurchase;
    this.email = this.$store.getters.getActiveUser
    var today = new Date();
    this.cancelWindow =
      today.getHours() + ":" + today.getMinutes()-10 + ":" + today.getSeconds();
    console.log(this.cancelWindow);
    AXIOS.get("/purchases/".concat(this.purchaseID))
      .then((response) => {
        this.totalPrice = response.data.totalPrice;
        this.postingList = response.data.postings;
        this.time = Date(response.data.time);
      })
      .catch((e) => {
        this.error = e;
      });
  },
};
</script>

<style scoped>
.info {
  position: relative;
  text-align: left;
  align-self: left;
  padding: 20px 200px;
}

.header {
  font-size: 20pt;
}
.listHeader {
  font-size: 22pt;
}
</style>
