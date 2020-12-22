import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        activeUser: '',
        activeUserType: '',
        activePosting: '',
        activePurchase: '',
        activeDeliveryType: ''
    },
    mutations: {
        setActiveUser(state, payload) {
            state.activeUser = payload
        },
        setActiveUserType(state, payload) {
            state.activeUserType = payload
        },
        setActivePosting(state, payload) {
            state.activePosting = payload
        },
        setActivePurchase(state, payload) {
            state.activePurchase = payload
        },
        setActiveDeliveryType(state, payload) {
            state.activeDeliveryType = payload
        }
    },
    actions: {
        setActiveUser({ commit }, payload) {
            commit('setActiveUser', payload)
        },
        setActiveUserType({ commit }, payload) {
            commit('setActiveUserType', payload)
        },
        setActivePosting({ commit }, payload) {
            commit('setActivePosting', payload)
        },
        setActivePurchase({ commit }, payload) {
            commit('setActivePurchase', payload)
        },
        setActiveDeliveryType({ commit }, payload) {
            commit('setActiveDeliveryType', payload)
        }
    },
    modules: {},
    getters: {
        getActiveUser(state) {
            return state.activeUser
        },
        getActiveUserType(state) {
            return state.activeUserType
        },
        getActivePosting(state) {
            return state.activePosting
        },
        getActivePurchase(state) {
            return state.activePurchase
        },
        getActiveDeliveryType(state) {
            return state.activeDeliveryType
        }
    }
})