<style lang="less">
  @import "message.less";
</style>
<template>
  <div style="width: 100%; height: 100%; background: #fff">
    <div style="width: 100%; height: 80%; overflow: auto">
      <div v-for="item in messageList" style="width: 100%; min-height: 150px;"
           :class="item.fromId === my.uid ? myMessageClass : messageClass">
        <Card style="width:350px; height: 90%; background: #f5f5f5">
          <p slot="title" style="text-align: left">
            <Icon type="person"></Icon>
            {{ item.fromName }}
          </p>
          <p slot="extra">
            {{ item.time }}
          </p>
          <p>
            {{ item.content }}
          </p>
          <p style="text-align: right">
            接收人:{{ item.toName }}
          </p>
        </Card>
      </div>
    </div>
    <div class="ready_to_send">
      <textarea class="input_type" v-model="addModal.content" placeholder="输入要发送的内容..."></textarea>
      <div class="send_bar">
        <Select v-model="addModal.toId" style="width: 30%; text-align: center" clearable placeholder="选择发送对象">
          <Option v-for="item in userList" :value="item.value" :key="item.value">{{ item.label }}</Option>
        </Select>
        <Button type="primary" style="margin-left: 10px" @click="sendMessage">发送</Button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Message',

  data () {
    return {
      // 用户信息
      my: {},
      // 表格是否正在加载
      isTableLoading: false,
      // 自己发送的消息格式
      myMessageClass: 'my_message',
      // 除了自己的消息的格式
      messageClass: 'message',
      // 表格每页条数
      pageSize: 8,
      // 预发送信息
      addModal: {
        content: '',
        toId: '',
        toName: ''
      },
      // 留言列表
      messageList: [],
      userList: [],

      // 在表格中显示的留言列表
      showingMessageList: [],

      // 表格列
      columns: [
        {title: '姓名', key: 'name', ellipsis: true, maxWidth: 140},
        {title: '电话号码', key: 'phone', maxWidth: 200},
        {title: '留言内容', key: 'content', ellipsis: true}
      ]
    }
  },

  methods: {
    /**
     * 拉取全部留言
     */
    fetchFromMessageList: function () {
      this.isTableLoading = true

      this.utils.http.get('/api/messages', {
        type: 'from',
        userId: this.my.uid
      })
        .then(response => {
          switch (response.status) {
            case 0: {
              this.messageList = response.data.messages
              this.fetchToMessageList()
              break
            }
            case 2: {
              this.utils.message.notLoggedIn()
              break
            }
            default: {
              this.utils.message.unknownError()
            }
          }

          this.isTableLoading = false
        })
        .catch(() => {
          this.utils.message.unknownError()
          this.isTableLoading = false
        })
    },

    /**
     * 获取发送给我们的数据
     */
    fetchToMessageList: function () {
      this.isTableLoading = true

      this.utils.http.get('/api/messages', {
        type: 'to',
        userId: this.my.uid
      })
        .then(response => {
          switch (response.status) {
            case 0: {
              this.messageList = this.messageList.concat(response.data.messages)
              let moment = this.utils.moment
              this.messageList.sort((o1, o2) => {
                return  moment(o1.time).unix() - moment(o2.time).unix()
              })
              for (let item of this.messageList){
                item.time = this.utils.moment(item.time).format('YYYY-MM-DD HH:mm:ss')
              }
              break
            }
            case 2: {
              this.utils.message.notLoggedIn()
              break
            }
            default: {
              this.utils.message.unknownError()
            }
          }

          this.isTableLoading = false
        })
        .catch(() => {
          this.utils.message.unknownError()
          this.isTableLoading = false
        })
    },

    /**
     * 获取全部用户信息
     */
    getAllUser () {
      this.utils.http.get('/api/users')
        .then((response) => {
          let status = response.status
          switch (status) {
            case 0:
              this.userList = this.dealUserList(response.data.users)
              break
            case 2:
              this.utils.message.notLoggedIn()
              break
            default:
              this.utils.message.unknownError()
              break
          }
        })
    },

    /**
     * 处理拉取出的所有用户, 删除自身并且转换成 Select 接受的格式
     * @param userList 所有用户 List
     * @return {*} 删除用户自身的并且转换格式的 List
     */
    dealUserList (userList) {
      for (let index = 0; index < userList.length; index++){
        if (userList[index].id === this.my.uid) {
          userList.splice(index, 1)
          break
        }
      }
      return this.utils.changeToSelectList(userList, 'id', 'name')
    },

    /**
     * 拉取个人信息
     */
    fetchSelfInfo () {
      this.utils.http.get('/api/sessions')
        .then((response) => {
          let status = response.status
          switch (status) {
            case 0:
              this.my = response.data
              this.fetchFromMessageList()
              this.getAllUser()
              break
            case 2:
              this.utils.message.notLoggedIn()
              break
            default:
              this.utils.message.unknownError()
              break
          }
        })
    },

    /**
     * 发送信息
     */
    sendMessage: function () {
      if (this.addModal.content === '' || !this.addModal.content) {
        this.$Message.error('请输入内容')
        return
      }

      if (this.addModal.toId === '' || !this.addModal.toId) {
        this.$Message.error('请选择发送人')
        return
      }

      this.$Modal.confirm({
        title: '提示',
        content: '是否确认发送消息',
        onOk: () => {
          this.utils.http.post('/api/messages', {
            fromId: this.my.uid,
            toId: this.addModal.toId,
            content: this.addModal.content,
          })
            .then((response) => {
              let status = response.status
              switch (status) {
                case 0:
                  this.$Message.success('发送成功')
                  this.addModal.content = ''
                  this.addModal.toId = ''
                  this.fetchFromMessageList()
                  break
                case 2:
                  this.utils.message.notLoggedIn()
                  break
                default:
                  this.utils.message.unknownError()
                  break
              }
            })
        }
      })
    }
  },

  created () {
    this.fetchSelfInfo()
  }
}
</script>
