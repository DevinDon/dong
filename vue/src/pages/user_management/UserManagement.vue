<style lang="less">
  @import '../../styles/common.less';
  @import './components/table.less';
</style>
<template>
  <div>
    <Row class="margin-top-10">
      <Col span="24">
      <Card>
        <p slot="title">
          <Icon type="android-remove"></Icon>
          用户列表
          <Icon type="android-remove"></Icon>
        </p>
        <div class="edittable-table-height-con">
          <can-edit-table refs="table2" v-model="editInlineData" :columns-list="editInlineColumns"
                          :roleList="role" :my="my" v-on:change="showUserList"></can-edit-table>
        </div>
      </Card>
      <Page
        :total="totalNum"
        :page-size="pageSize"
        @on-change="showUserList"
        show-total
        show-elevator
        :styles="{ marginTop: '16px', textAlign: 'right' }"
      />
      </Col>
    </Row>
  </div>
</template>

<script>
import canEditTable from './components/canEditTable.vue'

export default {
  name: 'userManagement',
  components: {
    canEditTable
  },
  data () {
    return {
      my: {},
      pageNum: 1,
      pageSize: 8,
      totalNum: 0,
      editInlineColumns: [
        {
          title: '序号',
          width: 80,
          type: 'index',
          align: 'center'
        },
        {
          title: '昵称',
          align: 'center',
          key: 'name',
          editable: true
        },
        {
          title: '账号',
          align: 'center',
          key: 'username'
        },
        {
          title: '密码',
          align: 'center',
          key: 'password',
          editable: true
        },
        {
          title: '操作',
          align: 'center',
          width: 190,
          key: 'handle',
          handle: ['edit', 'delete']
        }
      ],
      editInlineData: []
    }
  },
  methods: {
    /**
     * 获取所有用户
     */
    showUserList (pageNum) {
      if (pageNum) {
        this.pageNum = pageNum
      }
      this.utils.http.get('/user', {
        pageNum: this.pageNum,
        pageSize: this.pageSize
      })
        .then((response) => {
          let status = response.code
          switch (status) {
            case 200:
              this.editInlineData = response.results
              this.totalNum = response.page.total
              break
            default:
              this.utils.message.unknownError()
              break
          }
        })
    }
  },
  created () {
    this.showUserList()
    this.my = window.myInfo
  }
}
</script>

<style scoped>

</style>
