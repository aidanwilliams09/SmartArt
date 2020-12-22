Order Confirmation
<template>
  <html lang="en">
    <Taskbar></Taskbar>
    <div id="orderConfirmation">
      <h3>Your purchase is complete, {{ this.name }}.</h3>
      <h3>Order Confirmation: #{{ this.purchaseID }}</h3>
      <h3>Thank you for supporting local artists!</h3>
      <br />
      <button type="button" class="btn btn-danger btn-lg" @click="toHome">
        Browse Other Art
      </button>
      <p>{{ error }}</p>
    </div>
    <Footer />
  </html>
</template>

<script>
import axios from "axios";
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
  name: "CreateAccount",
  components: {
    Taskbar,
    Footer,
  },
  data() {
    return {
      email: "",
      name: "",
      purchaseID: 0,
      totalPrice: 0,
      purchaseType: "",
      postings: [],
      gallery: "SmartArt",
      error: "",
    };
  },
  created: function () {
    this.email = this.$store.getters.getActiveUser;
    this.deliveryType = this.$store.getters.getActiveDeliveryType;
    console.log("/purchase/make/".concat(this.deliveryType));
    AXIOS.get("/buyers/".concat(this.email))
      .then((response) => {
        this.name = response.data.name;
      })
      .catch((e) => {
        this.error = e;
      });
    AXIOS.get("/purchases/cart/".concat(this.email))
      .then((response) => {
        this.purchaseID = Number(response.data.purchaseID);
        this.postings = response.data.postings;
        this.totalPrice = Number(response.data.totalPrice);
        AXIOS({
          method: "post",
          url: "/purchase/make/".concat(this.deliveryType),
          data: {
            purchaseID: this.purchaseID,
            totalPrice: this.totalPrice,
            buyer: {
              email: this.email,
              gallery: this.gallery,
            },
          },
        })
          .then((response) => {
            console.log("purchased");
          })
          .catch((e) => {
            var errorMsg = e.message;
            console.log(e);
            console.log(this.purchaseID);
            this.error = errorMsg;
          });
      })
      .catch((e) => {
        this.error = e;
      });
  },
  methods: {
    toHome() {
      this.$router.push({ name: "Home" });
    },
  },
};
</script>

<style scoped>
#orderConfirmation {
  padding-top: 20vh;
}

</style>

