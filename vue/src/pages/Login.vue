<style lang="less">
    @import './login.less';
</style>

<template>
  <div class="login" @keydown.enter="handleSubmit">
    <div class="login-con">
      <Card :bordered="false">
        <p slot="title">
          <Icon type="log-in"></Icon>
          欢迎登录
        </p>
        <div class="form-con">
          <Form ref="loginForm" :model="form" :rules="rules">
            <FormItem prop="userName">
              <Input v-model="form.username" placeholder="请输入用户名">
              <span slot="prepend">
                <Icon :size="16" type="person"></Icon>
              </span>
              </Input>
            </FormItem>
            <FormItem prop="password">
              <Input type="password" v-model="form.password" placeholder="请输入密码">
              <span slot="prepend"><Icon :size="14" type="locked"></Icon></span>
              </Input>
            </FormItem>
            <FormItem>
              <Button @click="handleSubmit" type="primary" long>登录</Button>
            </FormItem>
          </Form>
        </div>
      </Card>
    </div>
  </div>
</template>

<script>
export default {
  name: 'login',
  data () {
    return {
      myInfo: {},
      form: {
        username: '',
        password: ''
      },
      rules: {
        username: [
          { required: true, message: '账号不能为空', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    handleSubmit () {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.utils.http.post('/user/login', {
            username: this.form.username,
            password: this.form.password
          })
            .then((response) => {
              let status = response.code
              switch (status) {
                case 200:
                  window.myInfo = response.results
                  this.$router.push({
                    name: 'userManagement'
                  })
                  break
                case 2:
                  this.utils.message.notLoggedIn()
                  break
                case 422:
                  this.$Message.error('账号/密码错误')
                  break
                default:
                  this.utils.message.unknownError()
                  break
              }
            })
        }
      })
    }
  }
}
</script>

<style>

</style>
