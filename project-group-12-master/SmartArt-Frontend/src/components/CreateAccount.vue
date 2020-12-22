Create account
<template>
  <html lang="en">
    <Taskbar></Taskbar>
    <div id="createAccount">
      <h3>Create your account</h3>
      <div class="container-fluid">
        <div class="input">
          <div class="inputbox">
            <input
              type="text"
              class="form-control input-style"
              v-model="name"
              placeholder="Full Name"
            />
          </div>
          <div class="inputbox">
            <input
              type="email"
              class="form-control input-style"
              v-model="email"
              placeholder="Email"
            />
          </div>
          <div class="inputbox">
            <input
              type="password"
              class="form-control input-style"
              v-model="password"
              placeholder="Password"
            />
          </div>
          <div class="inputbox">
            <input
              type="password"
              class="form-control input-style"
              v-model="confirmPassword"
              placeholder="Confirm Password"
            />
          </div>
        </div>
      </div>
      <div>
        <b-dropdown id="dropdown-1" text="Select your user type" class="m-md-2">
          <b-dropdown-item-btn @click="setBuyer">Buyer</b-dropdown-item-btn>
          <b-dropdown-item-btn @click="setArtist">Artist</b-dropdown-item-btn>
          <b-dropdown-item-btn @click="setAdmin">Administrator</b-dropdown-item-btn>
        </b-dropdown>
      </div>
      <button class="btn btn-danger" @click="createAcc"
        >Create Account</button
      >
      <button class="btn btn-danger" @click="toLogin"
        >Login</button
      >
      <p>{{ error }}</p>


    <Footer></Footer>
    </div>
  </html>

</template>

<script>
import axios from "axios";
import Taskbar from './Taskbar'
import Footer from './Footer'
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
      password: "",
      confirmPassword: "",
      gallery: "SmartArt",
      userType: "",
      error: "",
    };
  },
  methods: {
    createAcc: function () {
      if(this.name == ""){
        this.error = "Please enter your name";
      }
      else if(this.email == ""){
        this.error = "Please enter your email";
      }
      else if (this.password != this.confirmPassword) {
        this.error = "Your passwords do not match";
      } else if (this.userType == "") {
        this.error = "Please select a user type";
      } else {
        AXIOS({
          method: "post",
          url: "/".concat(this.userType).concat("/").concat("create"),
          data: {
            email: this.email,
            name: this.name,
            password: this.password,
            gallery: this.gallery,
          },
        })
          .then((response) => {
            this.$store.dispatch("setActiveUser", this.email);
            this.$store.dispatch("setActiveUserType", this.userType);

            this.email = "";
            this.name = "";
            this.password = "";
            this.confirmPassword = "";
            this.userType = "";
            this.error = "";

            this.$router.push({ name: "Home" });

          })
          .catch((e) => {
            var errorMsg = "Please enter a valid email, name and password";
            console.log(e);
            this.error = errorMsg;
          });
      }
    },

    toLogin: function () {
      this.$router.push({ name: "Login" });
    },
    setBuyer: function () {
      this.userType = "buyer";
    },
    setArtist: function () {
      this.userType = "artist";
    },
    setAdmin: function () {
      this.userType = "administrator";
    },
  },
};
</script>

<style scoped>
#createAccount {
  padding-top: 20vh;
}
.input {
  top: 20px;
  padding: 30px 30px;
  width: 450px;
}
.inputbox {
  position: relative;
  text-align: center;
  align-self: center;
  padding: 5px 5px;
}
.container-fluid {
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: auto;
  white-space: nowrap;
}
</style>

