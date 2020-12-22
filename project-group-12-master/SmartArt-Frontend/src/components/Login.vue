Login
<template>
  <html lang="en">
    <Taskbar/>
    <div id="loginPage">
      <h3>Welcome</h3>
      <div class="container-fluid">
        <div class="input">
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
        </div>
      </div>
      <div>
        <b-dropdown id="dropdown-1" text="Select your user type" class="m-md-2">
          <b-dropdown-item-btn @click="setBuyer">Buyer</b-dropdown-item-btn>
          <b-dropdown-item-btn @click="setArtist">Artist</b-dropdown-item-btn>
          <b-dropdown-item-btn @click="setAdmin"
            >Administrator</b-dropdown-item-btn
          >
        </b-dropdown>
      </div>
      <button class="btn btn-danger" @click="tryLogin"
        >Login</button
      >
      <button class="btn btn-danger" @click="toCreate"
        >Create Account</button
      >
      <p>{{ error }}</p>
    </div>
    <Footer/>
  </html>
</template>

<script>
import Taskbar from "./Taskbar";
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
  name: "Login",
  data() {
    return {
      email: "",
      password: "",
      error: "",
      userType: "",
    };
  },
  components: {
    Taskbar,
    Footer,
  },
  methods: {
    tryLogin: function () {
      if(this.name == ""){
        this.error = "Please enter your name";
      }
      else if(this.email == ""){
        this.error = "Please enter your email";
      }
      else if (this.userType == "") {
        this.error = "Please select a user type";
      } else {
        AXIOS({
          method: "post",
          url: "/".concat(this.userType).concat("/").concat("login"),
          data: {
            email: this.email,
            password: this.password,
          },
        })
          .then((response) => {
            this.$store.dispatch("setActiveUser", this.email);
            this.$store.dispatch("setActiveUserType", this.userType);
            this.email = "";
            this.password = "";
            this.error = "";
            this.userType = "";
            this.$router.push({ name: "Home" });
          })
          .catch((e) => {
            var errorMsg = "Please enter a valid email and password"
            console.log(e);
            this.error = errorMsg;
          });
      }
    },
    toCreate: function () {
      this.$router.push({ name: "CreateAccount" });
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

#loginPage {
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

