// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import BootstrapVue from "bootstrap-vue"
import App from './App'
import router from './router'
import store from './store'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import AOS from 'aos'
import 'aos/dist/aos.css'
import ImageUploader from 'vue-image-upload-resize'
import firebase from "firebase";

Vue.use(BootstrapVue)
Vue.use(ImageUploader)
Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App },
  created () {
    AOS.init()
      // Your web app's Firebase configuration
      // For Firebase JS SDK v7.20.0 and later, measurementId is optional
      var firebaseConfig = {
        apiKey: "AIzaSyDyFodORoQ6nUTFQtbsd7kg6q1Z2_R_upQ",
        authDomain: "smartart-1120e.firebaseapp.com",
        databaseURL: "https://smartart-1120e.firebaseio.com",
        projectId: "smartart-1120e",
        storageBucket: "smartart-1120e.appspot.com",
        messagingSenderId: "29683050370",
        appId: "1:29683050370:web:5f0a80fba5d16ee191119b",
        measurementId: "G-4C4JNSMNY7"
      };
      // Initialize Firebase
      firebase.initializeApp(firebaseConfig);
      //firebase.analytics();
    }

})
