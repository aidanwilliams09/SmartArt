Taskbar
<template>
  <div>
    <nav class="navbar navbar-light navbar-expand-md">
      <div class="container-fluid">
        <a class="navbar-brand" href="#/home"
          ><img id="Logo" src="../assets/SmartArt.png" /></a
        ><button
          data-toggle="collapse"
          class="navbar-toggler"
          data-target="#navcol-1"
          @click="toHome"
        >
          <span class="sr-only">Toggle navigation</span
          ><span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navcol-1">
          <ul class="nav navbar-nav ml-auto">
          <li class="nav-item" @click="toAcc">
              <a class="nav-link">Account</a>
            </li>
            <li class="nav-item" @click="toContactUs">
              <a class="nav-link">Contact Us</a>
            </li>
            <li class="nav-item" @click="toPostOrCart">
              <a class="nav-link" href="#">{{ this.buttonType }}</a>
            </li>
           
          </ul>
        </div>
      </div>
    </nav>
  </div>
</template>


<script>
export default {
  name: "Taskbar",
  data() {
    return {
      email: "",
      error: "",
      userType: "",
      buttonType: "",
    };
  },
  created: function () {
    this.email = this.$store.getters.getActiveUser;
    this.userType = this.$store.getters.getActiveUserType;
    if (this.userType == "artist" || this.userType == "administrator") {
      this.buttonType = "Post Art";
    } else if (this.userType == "buyer") {
      this.buttonType = "View Cart";
    }
  },
  methods: {
    toAcc() {
      console.log(this.$store.getters.getActiveUser);
      if (this.$store.getters.getActiveUser == "") {
        this.$router.push({ name: "Login" });
      } else {
        this.$router.push({ name: "Account" });
      }
    },
    toHome() {
      this.$router.push({ name: "Home" });
    },
    toContactUs() {
      this.$router.push({ name: "ContactUs" });
    },
    toPostOrCart() {
      if (this.userType == "artist" || this.userType == "administrator") {
        this.$router.push({ name: "CreatePosting" });
      }else if(this.userType == 'buyer'){
        this.$router.push({ name: "Cart" });
      }
    },
  },
};
</script>

<style scoped>
.container-fluid {
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: auto;
  white-space: nowrap;
}
</style>