
<template>
  <html lang="en">
  <Taskbar></Taskbar>
  <div id="createAccount">
    <h3>Update your posting</h3>
    <div class="container-fluid">
      <div class="input">
        <div class="inputbox">
          <p>Title:</p>
          <input
            type="text"
            class="form-control input-style"
            v-model="title"
            placeholder="Title"

          />
        </div>
        <div class="inputbox">
          <p>Description:</p>
          <input
            type="text"
            class="form-control input-style"
            v-model="description"
            placeholder="Description of your posting"
          />
        </div>
        <div class="inputbox">
          <p>Price ($):</p>
          <input
            type="number"
            class="form-control input-style"
            v-model="price"
            placeholder="Price ($)"
          />
        </div>
        <div class="inputbox">
          <p>X-Dimmension (cm):</p>
          <input
            type="number"
            class="form-control input-style"
            v-model="xDim"
            placeholder="X-Dimmension (cm)"
          />
        </div>
        <div class="inputbox">
          <p>Y-Dimmension (cm):</p>
          <input
            type="number"
            class="form-control input-style"
            v-model="yDim"
            placeholder="Y-Dimmension (cm)"
          />
        </div>
        <div class="inputbox">
          <p>Z-Dimmension (cm):</p>
          <input
            type="number"
            class="form-control input-style"
            v-model="zDim"
            placeholder="Z-Dimmension (cm)"
          />
        </div>
        <div class="inputbox">
          <p>Date:</p>
          <input
            type="date"
            class="form-control input-style"
            v-model="date"
            placeholder="Date created"
          />
        </div>
        <div class="inputbox">
          <p>Image:</p>
          <div>
            <div style="padding-bottom: 20px">
              <button v-if="!this.imageChosen" class="btn btn-danger"@click="click1">Choose a different Photo</button>
              <input type="file" ref="input1"
                     style="display: none"
                     @change="previewImage" accept="image/*" >
            </div>

            <div v-if="this.imageData != null">
              <img class="preview" style="max-height:350px; max-width:350px" :src="image">
              <br>
            </div>
          </div>
          <div style="padding-top: 20px">
            <button class="btn btn-danger" @click="onUpload" >Upload new Photo</button>
          </div>

        </div>
      </div>
    </div>
    <button class="btn btn-danger" @click="updatePost">Update Posting</button>
    <button class="btn btn-danger" @click="cancelUpdate">Cancel</button>
    <p style="padding-bottom: 25px">{{ error }}</p>
  </div>
  <Footer/>
  </html>
</template>

<script>
import axios from "axios";
import Taskbar from "./Taskbar";
import Footer from "./Footer";
import firebase from "firebase/app";
var config = require("../../config");

var frontendUrl = "https://" + config.build.host + ":" + config.build.port;
var backendUrl =
  "https://" + config.build.backendHost + ":" + config.build.backendPort;

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { "Access-Control-Allow-Origin": frontendUrl },
});
export default {
  name: "UpdatePosting",
  components: {
    Taskbar,
    Footer,
    firebase
  },
  data() {
    return {
      postingID: this.$store.getters.getActivePosting,
      email: "",
      title: "",
      description: "",
      image: "",
      price: null,
      xDim: null,
      yDim: null,
      zDim: null,
      date: "",
      gallery: "SmartArt",
      userType: "",
      error: "",
      response: [],
      imageData: null,
      uploadValue: null,
      imageChosen: false
    };
  },
  created: function () {
    this.email = this.$store.getters.getActiveUser;
    this.userType = this.$store.getters.getActiveUserType;
    AXIOS({
      method: "get",
      url: "/postings/".concat(this.postingID),
    }).then(response => {
      this.email = response.data.email,
        this.title = response.data.title,
        this.description = response.data.description,
        this.image = response.data.image,
        this.price = response.data.price,
        this.xDim = response.data.xdim,
        this.yDim = response.data.ydim,
        this.zDim = response.data.zdim,
        this.date = response.data.date,
        console.log(this.email)
    })
    .catch(e => {
      var errorMsg = e.message;
      console.log(e);
      this.error = errorMsg;
    })
  },
  methods: {
    updatePost: function () {
      if (this.title == "") {
        this.error += "Please enter a title. ";
      }
      if (this.description == "") {
        this.error += "Please enter a description. ";
      }
      if (this.price == null) {
        this.error += "Please enter a price. ";
      }
      if (this.xDim == null) {
        this.error += "Please enter the X dimmension. ";
      }
      if (this.yDim == null) {
        this.error += "Please enter the Y dimmension. ";
      }
      if (this.zDim == null) {
        this.error += "Please enter the Z dimmension. ";
      }
      if (this.image == "") {
        this.error += "Please enter an image URL. ";
      }
      if (this.error == "") {
        AXIOS({
          method: "put",
          url: "/posting/update",
          data: {
            artist: {
              email: this.email,
              gallery: this.gallery,
            },
            postingID: this.postingID,
            title: this.title,
            price: this.price,
            xdim: this.xDim,
            ydim: this.yDim,
            zdim: this.zDim,
            image: this.image,
            date: this.date,
            description: this.description,
          },
        })
          .then(response => {
            this.email = "";
            this.title = "";
            this.description = "";
            this.xDim = null;
            this.yDim = null;
            this.zDim = null;
            this.date = "";
            this.image = "";
            this.error = "";
            this.imageChosen = false;
            this.$router.push({ name: "Account" });
          })
          .catch((e) => {
            var errorMsg = e.message;
            console.log(e);
            this.error = errorMsg;
          });
      }
    },
    cancelUpdate : function () {
      this.imageChosen = false;
      this.$router.push({ name: "ViewPosting" });
    },
    click1() {
      this.$refs.input1.click()
    },

    previewImage(event) {
      this.imageChosen = true;
      this.uploadValue=0;
      this.image=null;
      this.imageData = event.target.files[0];
      this.onUpload()
    },
    onUpload(){
      this.image=null;
      const storageRef=firebase.storage().ref(`${this.imageData.name}`).put(this.imageData);
      storageRef.on(`state_changed`,snapshot=>{
          this.uploadValue = (snapshot.bytesTransferred/snapshot.totalBytes)*100;
        }, error=>{console.log(error.message)},
        ()=>{this.uploadValue=100;
          storageRef.snapshot.ref.getDownloadURL().then((url)=>{
            this.image =url;
            console.log(this.image)
          });
        }
      );
    }
  },
};
</script>

<style scoped>
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
