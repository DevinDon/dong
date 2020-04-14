import Vue from 'vue'
import Router from 'vue-router'
import login from '@/pages/Login.vue'
import main from '@/pages/Main.vue'
import home from '@/pages/home/Home'
import userManage from '@/pages/user_management/UserManagement'
import prisonerManage from '@/pages/prisoner_management/PrisonerManagement'
import jailCellManagement from '@/pages/jail_cell_management/JailCellManagement.vue'
import jailManagement from '@/pages/jail_management/JailManagement'
import message from '@/pages/message/Message'
import notice from '@/pages/notice/Notice'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: {name: 'login'}
    },
    {
      path: '/login',
      name: 'login',
      component: login
    },
    {
      path: '/management',
      component: main,
      redirect: {name: 'home'},
      children: [
        {
          path: 'home',
          name: 'home',
          component: home
        },
        {
          path: 'user_management',
          name: 'userManagement',
          component: userManage
        },
        {
          path: 'prisoner_management',
          name: 'prisonerManage',
          component: prisonerManage
        },
        {
          path: 'jail_cell_management',
          name: 'jailCellManagement',
          component: jailCellManagement
        },
        {
          path: 'jail_management',
          name: 'jailManagement',
          component: jailManagement
        },
        {
          path: 'message',
          name: 'message',
          component: message
        },
        {
          path: 'notice',
          name: 'notice',
          component: notice
        }
      ]
    }
  ]
})
