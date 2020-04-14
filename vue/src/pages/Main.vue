<style lang="less">
  @import "./main.less";
</style>
<template>
  <div class="main" :class="{'main-hide-text': shrink}">
    <div class="sidebar-menu-con" :style="{width: shrink?'60px':'200px', overflow: shrink ? 'visible' : 'auto'}">
      <scroll-bar ref="scrollBar">
        <shrinkable-menu
          :theme="menuTheme"
          :my="my"
          :menu-list="menuList">
          <div slot="top" class="logo-con">
            <img src="../../static/logo.jpg" key="max-logo"/>
          </div>
        </shrinkable-menu>
      </scroll-bar>
    </div>
    <div class="main-header">
      <div class="header-middle-con">
        <div class="main-breadcrumb">
        </div>
      </div>
      <div class="header-avator-con">
        <div class="user-dropdown-menu-con">
          <Row type="flex" justify="end" align="middle" class="user-dropdown-innercon">
            <Dropdown transfer trigger="click" @on-click="handleClickUserDropdown">
              <a href="javascript:void(0)">
                <span class="main-user-name">{{ my.name }}</span>
                <Icon type="arrow-down-b"></Icon>
              </a>
              <DropdownMenu slot="list">
                <DropdownItem name="changePassword">修改密码</DropdownItem>
                <DropdownItem name="loginout" divided>退出登录</DropdownItem>
              </DropdownMenu>
            </Dropdown>
            <Avatar :src="avatorPath" style="margin-left: 10px;"></Avatar>
          </Row>
        </div>
      </div>
    </div>
    <div class="single-page-con" :style="{left: shrink?'0':'200px'}">
      <div class="single-page" style="width: calc(100% - 20px); height: 90%;">
        <router-view></router-view>
      </div>
    </div>
    <Modal title="修改密码" v-model="isChangePasswordModalVisible" width="320" @on-ok="handleEdit" @on-cancel="resetEditForm">
      <Form ref="user" :model="user" :rules="formRules" label-position="top" @submit.native.prevent>
        <FormItem label="旧密码" prop="oldPassword">
          <Input type="password" v-model="user.oldPassword" placeholder="请输入新密码" :clearable="true"/>
        </FormItem>
        <FormItem label="新密码" prop="newPassword">
          <Input type="password" v-model="user.newPassword" placeholder="再次输入新密码" :clearable="true"/>
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>
<script>
import shrinkableMenu from '../components/main-components/shrinkable-menu/shrinkable-menu'
import scrollBar from '../components/my-components/scroll-bar/vue-scroller-bars'

export default {
  components: {
    shrinkableMenu,
    scrollBar
  },
  data () {
    const vm = this
    return {
      isChangePasswordModalVisible: false,
      menuTheme: 'dark',
      shrink: false,
      isFullScreen: false,
      openedSubmenuArr: '',
      my: {},
      user: {
        newPassword: '',
        oldPassword: ''
      },
      formRules: {
        newPassword: [
          {type: 'string', required: true, message: '请输入新密码'},
          // {min: 8, max: 16, message: '密码长度为8到16'}
        ],
        oldPassword: [
          {type: 'string', required: true, message: '请输入旧密码'},
          // {min: 8, max: 16, message: '密码长度为8到16'}
        ]
      },
      menuList: [
        {name: '用户管理', icon: 'person', path: 'userManagement'},
        {name: '监狱管理', icon: 'home', path: 'jailManagement'},
        {name: '牢房管理', icon: 'ios-grid-view', path: 'jailCellManagement'},
        {name: '囚犯管理', icon: 'ios-body', path: 'prisonerManage'}
      ],
      avatorPath: require('../../static/zhanghu.png')
    }
  },
  computed: {
  },
  methods: {
    /**
     * 右上角下拉菜单选项
     * @param name 选择的功能
     */
    handleClickUserDropdown (name) {
      switch (name) {
        case 'changePassword':
          this.showChangePasswordModal()
          break
        case 'loginout':
          this.loginOut()
          break
      }
    },

    /**
     * 退出登录
     */
    loginOut () {
      this.$Modal.confirm({
        title: '提示',
        content: '是否退出登录',
        onOk: () => {
          this.$router.push({name: 'login'})
        }
      })
    },

    /**
     * 显示修改密码弹窗
     */
    showChangePasswordModal () {
      this.isChangePasswordModalVisible = true
    },

    /**
     * 重置修改密码
     */
    resetEditForm () {
      this.user.newPassword = ''
      this.user.oldPassword = ''
    },

    /**
     * 修改密码
     */
    handleEdit () {
      this.validateEditPassword()
        .then(() => {
          this.utils.http.put('/api/users/' + this.my.uid + '/password', {
            oldPassword: this.user.oldPassword,
            newPassword: this.user.newPassword
          })
            .then((response) => {
              let status = response.status
              switch (status) {
                case 0:
                  this.$Message.success('修改成功')
                  this.user.newPassword = ''
                  this.user.oldPassword = ''
                  break
                case 2:
                  this.utils.message.notLoggedIn()
                  break
                case 4:
                  this.$Message.error('禁止操作别人账户')
                  break
                case 5:
                  this.$Message.error('账户不存在')
                  break
                case 6:
                  this.$Message.error('密码错误')
                  break
                default:
                  this.utils.message.unknownError()
                  break
              }
            })
        })
        .catch(() => {
          this.$nextTick(() => { this.isChangePasswordModalVisible = true })
        })
    },

    /**
     * 检验是否合法
     */
    validateEditPassword () {
      return new Promise((resolve, reject) => {
        this.$refs.user.validate()
          .then(passed => {
            if (passed) {
              resolve()
            }

            let err = new Error()
            err.cause = 'validate'
            reject(err)
          })
      })
    }
  },
  created () {
    this.my = window.myInfo
  }
}
</script>
