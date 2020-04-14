<style scoped>
  .time{
    font-size: 14px;
    font-weight: bold;
    text-align: left;
  }
  .title{
    padding-left: 5px;
    text-align: left;
  }
  .content{
    padding-left: 5px;
    text-align: left;
  }
</style>
<template>
  <div>
    <div  v-if="my.role === 1" style="width: 100%; height: 800px; background: #fff; overflow: auto; padding: 10px">
      <Timeline style="width: 100%">
        <TimelineItem v-for="item in showingNoticeList">
          <p class="time">{{ item.insertTime }}</p>
          <p class="title">{{ item.title }}</p>
          <p class="content">{{ item.content }}</p>
        </TimelineItem>
      </Timeline>
    </div>
    <div v-if="my.role === 0">
      <div>
        <!--按钮组-->
        <div style="text-align: right; margin-bottom: 24px;">
          <Button
            type="success"
            size="large"
            style="margin-right: 10px;"
            @click="isAddModalVisible = true"
          >
            添加
          </Button>
          <Button
            type="error"
            size="large"
            style="margin-right: 10px;"
            :disabled="selectedNoticeList.length === 0"
            @click="handleMassDelete"
          >
            批量删除
          </Button>
        </div>

        <Table
          :columns="columns"
          :data="showingNoticeList"
          :loading="isTableLoading"
          @on-selection-change="selection => {this.selectedNoticeList = selection}"
        />

        <Page
          :total="totalNum"
          :page-size="pageSize"
          @on-change="showNoticeList"
          show-total
          show-elevator
          :styles="{ marginTop: '16px', textAlign: 'right' }"
        />
      </div>

      <!--添加对话框-->
      <Modal
        title="添加公告"
        v-model="isAddModalVisible"
        width="320"
        @on-ok="handleAdd"
        @on-cancel="resetAddForm"
      >
        <Form
          ref="addForm"
          :model="addForm"
          :rules="formRules"
          label-position="top"
          @submit.native.prevent
        >
          <FormItem
            label="标题"
            prop="title"
          >
            <Input
              v-model="addForm.title"
              placeholder="请输入标题"
              :clearable="true"
            />
          </FormItem>

          <FormItem
            label="内容"
            prop="content"
          >
            <Input
              v-model="addForm.content"
              placeholder="请输入标题"
              type="textarea"
            />
          </FormItem>
        </Form>
      </Modal>

      <!--修改对话框-->
      <Modal
        title="修改公告"
        v-model="isEditModalVisible"
        width="320"
        @on-ok="handleEdit"
        @on-cancel="resetEditForm"
      >
        <Form
          ref="editForm"
          :model="editForm"
          :rules="formRules"
          label-position="top"
          @submit.native.prevent
        >
          <FormItem
            label="标题"
            prop="title"
          >
            <Input
              v-model="editForm.title"
              placeholder="请输入标题"
              :clearable="true"
            />
          </FormItem>

          <FormItem
            label="内容"
            prop="content"
          >
            <Input
              v-model="editForm.content"
              placeholder="请输入内容"
              type="textarea"
            />
          </FormItem>
        </Form>
      </Modal>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Notice',

  data () {
    return {
      // 自身属性
      my: {},
      // 表格是否正在加载
      isTableLoading: false,

      // 表格每页条数
      pageSize: 8,

      // 当前页码
      pageNum: 1,

      totalNum: 0,

      // 添加对话框是否可见
      isAddModalVisible: false,

      // 修改对话框是否可见
      isEditModalVisible: false,

      // 通知列表
      noticeList: [],

      // 在表格中显示的列表
      showingNoticeList: [],

      // 选中的通知
      selectedNoticeList: [],

      // 表格列
      columns: [
        {type: 'selection', width: 60, align: 'center'},
        {title: '标题', key: 'title', ellipsis: true},
        {title: '内容', key: 'content', ellipsis: true},
        {title: '添加时间', key: 'insertTime', maxWidth: 200},
        {
          title: '操作',
          align: 'center',
          width: 140,
          render: (h, params) => {
            // 声明 JSX 中需要使用的组件
            const Button = 'Button'

            let index = params.index
            let id = params.row.id
            let name = params.row.name

            return (
              <div style="line-height: 48px;">
                <Button
                  type="primary"
                  size="small"
                  style="margin-right: 10px;"
                  onClick={() => { this.showEditModal(index) }}
                >
                  修改
                </Button>

                <Button
                  type="error"
                  size="small"
                  style="margin-right: 10px"
                  onClick={() => { this.handleDelete(id, name) }}
                >
                  删除
                </Button>
              </div>
            )
          }
        }
      ],

      // 添加表单
      addForm: {
        title: '',
        content: ''
      },

      // 修改表单
      editForm: {
        id: -1,
        title: '',
        content: ''
      },

      // 表单校验规则
      formRules: {

        // 标题
        title: [
          {type: 'string', required: true, message: '请填写标题'},
          {max: 50, message: '标题过长'}
        ],

        // 内容
        content: [
          {type: 'string', required: true, message: '请填写内容'},
          {max: 400, message: '内容过长'}
        ]
      }
    }
  },

  methods: {

    /**
     * 拉取全部通知
     */
    fetchNoticeList: function () {
      this.isTableLoading = true

      this.utils.http.get('/api/notices', {
        pageNum: this.pageNum,
        pageSize: this.pageSize
      })
        .then(response => {
          switch (response.status) {
            case 0: {
              let noticeList = response.data.notices

              this.totalNum = response.data.count

              // 加工信息
              for (let i = 0; i < noticeList.length; i++) {
                let notice = noticeList[i]

                notice.insertTime = this.utils.moment(notice.insertTime).format('YYYY-MM-DD HH:mm:ss')
              }

              this.showingNoticeList = noticeList
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
     * 在表格中显示通知
     *
     * @param pageNum 页码
     */
    showNoticeList: function (pageNum) {
      this.pageNum = pageNum

      let startIndex = (pageNum - 1) * this.pageSize
      let endIndex = startIndex + this.pageSize

      this.showingNoticeList = this.noticeList.slice(startIndex, endIndex)
    },

    /**
     * 处理删除的流程
     *
     * @param id 通知 ID
     */
    handleDelete: function (id) {
      this.$Modal.confirm({
        title: '确认删除',
        content: '您确认要删除这条通知吗？',
        onOk: () => {
          this.requestDelete(id)
            .then(response => {
              switch (response.status) {
                case 0: {
                  this.$Message.success('删除成功')
                  this.fetchNoticeList()
                  break
                }
                case 2: {
                  this.utils.message.notLoggedIn()
                  break
                }
                default: {
                  this.utils.message.unknownError()
                  break
                }
              }
            }).catch(() => {
              this.utils.message.unknownError()
            })
        }
      })
    },

    /**
     * 提交删除请求
     *
     * @param {number} id 通知 ID
     * @return {Promise} 请求成功时 resolve，传出参数为响应值
     * 请求失败时 reject，error cause = request
     */
    requestDelete: function (id) {
      return this.utils.http.delete('/api/notices/' + id)
    },

    /**
     * 处理批量删除流程
     */
    handleMassDelete: function () {
      // 弹出确认对话框
      this.$Modal.confirm({
        title: '确认删除',
        content: '您确认要删除这' + this.selectedNoticeList.length + '条通知吗？',
        onOk: () => {
          // 生成 ID List
          let idList = []
          for (let notice of this.selectedNoticeList) {
            idList.push(notice.id)
          }

          // 批量删除
          this.requestMassDelete(idList)
        }
      })
    },

    /**
     * 请求批量删除通知
     *
     * @param idList 通知 ID 集合
     */
    requestMassDelete: function (idList) {
      this.utils.http.delete('/api/notices', {
        idList: JSON.stringify(idList)
      })
        .then(response => {
          switch (response.status) {
            case 0: {
              this.$Message.success('删除成功')
              this.fetchNoticeList()
              break
            }
            case 2: {
              this.utils.message.notLoggedIn()
              break
            }
            default: {
              this.utils.message.unknownError()
              break
            }
          }
        })
        .catch(() => {
          this.utils.message.unknownError()
        })
    },

    /**
     * 处理添加流程
     */
    handleAdd: function () {
      // 验证
      this.validateAdd()
      // 提交请求
        .then(() => { return this.requestAdd() })
        // 请求完成
        .then((response) => {
          switch (response.status) {
            // 添加成功
            case 0: {
              this.$Message.success('添加成功')
              this.resetAddForm()
              this.fetchNoticeList()
              break
            }
            // 未登录
            case 2: {
              this.utils.message.notLoggedIn()
              break
            }
            default: {
              this.utils.message.unknownError()
              break
            }
          }
        })
        .catch((err) => {
          switch (err.cause) {
            case 'validate': {
              this.$nextTick(() => { this.isAddModalVisible = true })
              break
            }
            case 'request': {
              this.utils.message.unknownError()
              break
            }
            default: {
              break
            }
          }
        })
    },

    /**
     * 验证添加表单
     *
     * @returns {Promise} 验证通过时 resolve，否则 reject
     */
    validateAdd: function () {
      return new Promise((resolve, reject) => {
        this.$refs.addForm.validate()
          .then(passed => {
            if (passed) {
              resolve()
            }

            let err = new Error()
            err.cause = 'validate'
            reject(err)
          })
      })
    },

    /**
     * 提交添加请求
     *
     * @returns {Promise} 请求成功时 resolve，传出参数为响应值
     * 请求失败时 reject，error cause = 'request'
     */
    requestAdd: function () {
      let form = this.addForm

      return this.utils.http.post('/api/notices', {
        title: form.title,
        content: form.content
      })
    },

    /**
     * 重置添加表单
     */
    resetAddForm: function () {
      this.$refs.addForm.resetFields()
    },

    /**
     * 显示修改对话框
     *
     * @param index 待修改的通知在 showingNoticeList 中的索引
     */
    showEditModal: function (index) {
      let notice = this.showingNoticeList[index]
      let form = this.editForm

      form.id = notice.id
      form.title = notice.title
      form.content = notice.content

      this.isEditModalVisible = true
    },

    /**
     * 处理修改流程
     */
    handleEdit: function () {
      // 验证表单
      this.validateEdit()
      // 提交请求
        .then(() => { return this.requestEdit() })
        // 请求完成
        .then(response => {
          switch (response.status) {
            case 0: {
              this.$Message.success('通知修改成功')
              this.fetchNoticeList()
              break
            }
            case 2: {
              this.utils.message.notLoggedIn()
              break
            }
            default: {
              this.utils.message.unknownError()
              break
            }
          }
        })
        .catch(err => {
          switch (err.cause) {
            case 'validate': {
              this.$nextTick(() => { this.isEditModalVisible = true })
              break
            }
            case 'request': {
              this.utils.message.unknownError()
              break
            }
            default: {
              break
            }
          }
        })
    },

    /**
     * 验证修改表单
     *
     * @return {Promise} 验证通过时 resolve，否则 reject
     */
    validateEdit: function () {
      return new Promise((resolve, reject) => {
        this.$refs.editForm.validate()
          .then(passed => {
            if (passed) {
              resolve()
            }

            let err = new Error()
            err.cause = 'validate'
            reject(err)
          })
      })
    },

    /**
     * 提交修改请求
     */
    requestEdit: function () {
      let form = this.editForm
      let url = '/api/notices/' + form.id

      return this.utils.http.put(url, {
        title: form.title,
        content: form.content
      })
    },

    /**
     * 重置修改表单
     */
    resetEditForm: function () {
      this.$refs.editForm.resetFields()
      this.editForm.id = -1
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
  },

  created () {
    this.fetchNoticeList()
    this.fetchSelfInfo()
  }
}
</script>
