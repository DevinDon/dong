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
          牢房列表
          <Icon type="android-remove"></Icon>
        </p>
        <Select v-model="jailId" style="width: 100%; margin-bottom: 20px" clearable @on-change="showJailCellList">
          <Option v-for="item in jailList" :value="item.value" :key="item.value">{{ item.label }}</Option>
        </Select>
        <div class="edittable-table-height-con">
          <can-edit-table refs="table2" v-model="editInlineData" :columns-list="editInlineColumns"
            :jail-list="jailList" :jail-info-list="jailInfoList" v-on:change="showJailCellList"></can-edit-table>
        </div>
      </Card>
      <Page
        :total="totalNum"
        :page-size="pageSize"
        @on-change="showJailCellList"
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
  name: 'jailCellManagement',
  components: {
    canEditTable
  },
  data () {
    return {
      my: {},
      pageNum: 1,
      pageSize: 8,
      totalNum: 0,
      jailList: [],
      jailInfoList: [],
      jailId: -1,
      editInlineColumns: [
        {
          title: '牢房序号',
          width: 80,
          align: 'center',
          key: 'id'
        },
        {
          title: '监狱名称',
          align: 'center',
          key: 'jailName'
        },
        {
          title: '监狱地址',
          align: 'center',
          key: 'address'
        },
        {
          title: '牢房大小',
          align: 'center',
          key: 'size',
          editable: true
        },
        {
          title: '操作',
          align: 'center',
          key: 'handle',
          handle: ['regulation', 'delete']
        }
      ],
      editInlineData: []
    }
  },
  methods: {
    /**
     * 拉取所有牢房列表
     */
    fetchJailList () {
      this.utils.http.get('/jail', {
        pageSize: 99999,
        pageNum: 1
      })
        .then((response) => {
          let status = response.code
          switch (status) {
            case 200:
              this.jailInfoList = response.results;
              this.jailList = this.utils.changeToSelectList(response.results, 'id', 'name')
              break
            default:
              this.utils.message.unknownError()
              break
          }
        })
    },

    showJailCellList () {
      if (!this.jailId) {
        return
      }
      this.utils.http.post('/jail/cell/list', {
        jailId: this.jailId,
        'page.pageNum': this.pageNum,
        'page.pageSize': this.pageSize
      })
        .then((response) => {
          let status = response.code
          switch (status) {
            case 200:
              console.log(response.results)
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
    this.fetchJailList()
  }
}
</script>

<style scoped>

</style>
