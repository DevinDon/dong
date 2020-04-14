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
            监狱列表
          <Icon type="android-remove"></Icon>
        </p>
        <div class="edittable-table-height-con">
          <can-edit-table refs="table2" v-model="editInlineData" :columns-list="editInlineColumns"
                          v-on:change="showJailList"></can-edit-table>
        </div>
      </Card>
      <Page
        :total="totalNum"
        :page-size="pageSize"
        @on-change="showJailList"
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
  name: 'jailManagement',
  components: {
    canEditTable
  },
  data () {
    return {
      pageNum: 1,
      pageSize: 8,
      totalNum: 0,
      editInlineColumns: [
        {
          title: '编号',
          width: 80,
          align: 'center',
          key: 'id'
        },
        {
          title: '监狱名称',
          align: 'center',
          key: 'name',
          editable: true
        },
        {
          title: '监狱地址',
          align: 'center',
          key: 'address',
          editable: true
        },
        {
          title: '操作',
          align: 'center',
          key: 'handle',
          handle: ['edit', 'delete']
        }
      ],
      editInlineData: [
      ]
    }
  },
  methods: {
    /**
     * 分页拉取所有监狱
     * @param pageNum 如果没有参数, 则是初始化 如果有参数, 则拉取参数页的数据
     */
    showJailList (pageNum) {
      if (pageNum) {
        this.pageNum = pageNum
      }
      this.utils.http.get('/jail', {
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
    this.showJailList()
  }
}
</script>

<style scoped>

</style>
