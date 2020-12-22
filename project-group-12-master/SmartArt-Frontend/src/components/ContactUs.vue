ContactUs
<template>
  <html lang="en">
    <Taskbar></Taskbar>
    <div id="contactUs">
      <div class="embed-responsive embed-responsive-21by9">
        <iframe
          class="embed-responsive-item"
          src="https://gregorywalfish.typeform.com/to/G2YT2vry"
        ></iframe>
      </div>
      <p>{{ error }}</p>
    </div>
    <Footer/>
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
      password: "",
      confirmPassword: "",
      gallery: "SmartArt",
      userType: "",
      error: "",
    };
  },
  methods: {
    toHome() {
      this.$router.push({ name: "Home" });
    },
  },
};
</script>

<style scoped>
</style>

