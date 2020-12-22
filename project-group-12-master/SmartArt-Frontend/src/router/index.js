import Vue from 'vue';
import Router from 'vue-router';
import Login from "../components/Login";
import CreateAccount from "../components/CreateAccount";
import Home from "../components/Home";
import Cart from "../components/Cart";
import Account from "../components/Account";
import ViewPosting from "../components/ViewPosting";
import CreatePosting from "../components/CreatePosting"
import OrderConfirmation from "../components/OrderConfirmation";
import ContactUs from "../components/ContactUs";
import Footer from "../components/Footer";
import ViewPurchase from "../components/ViewPurchase"
import UpdatePosting from "../components/UpdatePosting";

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/home'
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/createAccount',
      name: 'CreateAccount',
      component: CreateAccount
    },
    {
      path: '/home',
      name: 'Home',
      component: Home
    },
    {
      path: '/cart',
      name: 'Cart',
      component: Cart
    },
    {
      path: '/account',
      name: 'Account',
      component: Account
    },
    {
      path: '/viewPosting',
      name: 'ViewPosting',
      component: ViewPosting
    },
    {
      path: '/createPosting',
      name: 'CreatePosting',
      component: CreatePosting
    },
    {
      path: '/updatePosting',
      name: 'UpdatePosting',
      component: UpdatePosting
    },
    {
      path: '/orderConfirmation',
      name: 'OrderConfirmation',
      component: OrderConfirmation
    },
    {
      path: '/contactUs',
      name: 'ContactUs',
      component: ContactUs
    },
    {
      path: '/footer',
      name: 'Footer',
      component: Footer
    },
    {
      path: '/viewPurchase',
      name: 'ViewPurchase',
      component: ViewPurchase
    },
  ]
})
