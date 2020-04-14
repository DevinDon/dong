import Vue from 'vue'
import App from './App'
import router from './router'
import iView from 'iview'
import utils from '@/utils'
import 'iview/dist/styles/iview.css'

Vue.config.productionTip = false
Vue.use(iView)
Vue.prototype.utils = utils

/* eslint-disable no-new */
window.vm = new Vue({
  el: '#app',
  router,
  render: h => h(App)
})
