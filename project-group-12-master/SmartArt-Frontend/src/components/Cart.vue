Cart
<!DOCTYPE html>
<template>
  <html style="color: white">
    <Taskbar />
    <body style="align-items: center; text-align: center">
      <div style="padding-top: 30px"></div>
      <section id="cart">
        <div class="cartTitle">
          <h1>My Cart</h1>
        </div>
        <div class="top divider">
          <hr />
        </div>
        <div class="row cartText">
          <table style = "width: 100%">
            <tr>
              <td style = "width:9.5%;"></td>
              <td style = "width:12%">Image</td>
              <td style = "width:13%">Title</td>
              <td style = "width:1%"></td>
              <td style = "width:24%">Price</td>
              <td style = "width:12.5%"></td>
            </tr>
          </table>
          <table style = "width: 90%">
            <li
              v-for="posting in cartPostings"
              v-bind:key="posting.postingID"
            >
              <tr>
                <td style = "width:15%"></td>
                <td style = "width:20%">
                  <img
                    v-bind:src="posting.image"
                    style="max-height: 100px; width: auto"
                  />
                </td>
                <td style = "width:20%">
                    {{ posting.title }}
                </td>
                <td style="width: 4%"></td>
                <td style="width: 36%">
                    ${{ posting.price }}
                </td>
                <td>
                  <div class="removeButton">
                    <a href="http://127.0.0.1:8087/#/cart">
                      <button
                        type="button"
                        class="btn btn-danger"
                        @click="removePosting(posting)"
                      >
                        Remove
                      </button>
                    </a>
                  </div>
                </td>
                <td style="width: 14%"></td>
              </tr>
            </li>
          </table>
        </div>
        <div class="bot divider">
          <hr />
        </div>
        <div class="row">
          <div class="columns;" style="width: 70%"></div>
          <div class="columns;" style="width: 5%; text-align: left">Total</div>
          <div class="columns;" style="width: 5%"></div>
          <div class="columns;" style="width: 5%; text-align: right">
            ${{ this.totalPrice }}
          </div>
        </div>
      </section>
      <section id="buttons">
        <div class="row">
          <div class="columns" style="width: 62%"></div>
          <div class="columns" style="width: 17%">
            <b-dropdown
              id="dropdown-1"
              text="Select your delivery type"
              class=""
            >
              <b-dropdown-item-btn @click="setPickUp"
                >Pick-Up</b-dropdown-item-btn
              >
              <b-dropdown-item-btn @click="setShipped"
                >Shipped</b-dropdown-item-btn
              >
            </b-dropdown>
          </div>
          <div class="columns;" style="width: 17%">
            <div class="purchaseButton">
              <a>
                <button type="button" class="btn btn-danger" @click="toConf">
                  Confirm Purchase
                </button>
              </a>
            </div>
          </div>
        </div>
      </section>
    </body>
    <Footer/>
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
  components: {
    PostingList,
    Taskbar,
    Footer,
  },
  data() {
    return {
      cart: null,
      cartPostings: [],
      totalPrice: 0.00,
      email: null,
      deliveryType: null,
      purchaseID: null,
      gallery: "SmartArt",
      response: [],
    };
  },
  created: function () {
    this.email = this.$store.getters.getActiveUser;
    AXIOS.get("/purchases/cart/".concat(this.email))
      .then((response) => {
        this.cart = response.data;
        if (cart != null) {
          this.purchaseID = response.data.purchaseID;
          this.totalPrice = response.data.totalPrice;
          this.cartPostings = response.data.postings;
          document.getElementById("cartPostings").innerHTML = this.cartPostings;
          document.getElementById("totalPrice").innerHTML = this.totalPrice;
        }
      })
      .catch((e) => {
        this.errorPosting = e;
      });
  },
  methods: {
    removePosting: function (posting) {
      this.cartPostings.splice(this.cartPostings.indexOf(posting), 1);
      if(this.email != ''){
              AXIOS({
        method: "delete",
        url: "/purchase/cart/remove/".concat(posting.postingID),
        data: {
            email: this.email,
            gallery: this.gallery
          },
      })
        .then((response) => {
          this.$router.push({ name: "Home" });
        })
        .catch((e) => {
          console.log(e);
        });
      }
    },
    setPickUp: function () {
      this.$store.dispatch("setActiveDeliveryType", "PickUp");
    },
    setShipped: function () {
      this.$store.dispatch("setActiveDeliveryType", "Shipped");
    },
    toConf: function (){
      this.$router.push({ name: "OrderConfirmation" });
    }
  },
};
</script>


<style>
hr {
  width: 75%;
  background: black;
}

#cart {
  border-top: 1px solid #c1c1c1;
  border-bottom: 1px solid #c1c1c1;
  padding-top: 0.9375rem;
  padding-bottom: 0.9375rem;
  background-color: rgba(216, 216, 216, 0.2);
  text-align: center;
  margin-top: 1rem;
  margin-bottom: 1rem;
}
#buttons {
  padding-top: 0.9375rem;
  padding-bottom: 0.9375rem;
  text-align: center;
  margin-top: 1rem;
  margin-bottom: 1rem;
  margin-left: 6rem;
  margin-right: 6rem;
}
li {
    list-style-type: none;
}
tr>td{
  padding-bottom: 2em;
}

</style>
