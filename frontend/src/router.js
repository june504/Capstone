
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import RentalManager from "./components/RentalManager"

import RepairManager from "./components/RepairManager"

import StoreManager from "./components/StoreManager"


import ToyList from "./components/ToyList"
import PaymentManager from "./components/PaymentManager"

export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/rentals',
                name: 'RentalManager',
                component: RentalManager
            },

            {
                path: '/repairs',
                name: 'RepairManager',
                component: RepairManager
            },

            {
                path: '/stores',
                name: 'StoreManager',
                component: StoreManager
            },


            {
                path: '/toyLists',
                name: 'ToyList',
                component: ToyList
            },
            {
                path: '/payments',
                name: 'PaymentManager',
                component: PaymentManager
            },



    ]
})
