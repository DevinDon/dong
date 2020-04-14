<template>
  <div>
    <Row class="margin-top-10">
      <Col span="24">
      <Card>
        <p slot="title">
          <Icon type="android-remove"></Icon>
          囚犯列表
          <Icon type="android-remove"></Icon>
        </p>
        <div class="edittable-table-height-con">
          <Select v-model="jailCellId" style="width: 100%; margin-bottom: 20px" clearable @on-change="selectJailCellId">
            <Option v-for="item in jailCellList" :value="item.value" :key="item.value">{{ item.label }}</Option>
          </Select>
          <can-edit-table refs="table2" v-model="editInlineData" :columns-list="editInlineColumns" :my="my" :jail-cell-list ='jailCellList'
                          v-on:change="selectJailCellId"></can-edit-table>
        </div>
      </Card>
      <Page
        :total="totalNum"
        :page-size="pageSize"
        @on-change="showPrisonerList"
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
  name: 'prisonerManage',
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
          title: '囚犯编号',
          key: 'id',
          align: 'center'
        },
        {
          title: '囚犯罪行',
          align: 'center',
          key: 'violation'
        },
        {
          title: '囚犯信息',
          align: 'center',
          key: 'infoId'
        },
        {
          title: '操作',
          align: 'center',
          width: 190,
          key: 'handle',
          handle: ['edit', 'delete']
        }
      ],
      editInlineData: [],
      jailCellList: [],
      jailCellId: undefined
    }
  },
  methods: {
    /**
     * 分页获取犯人
     */
    showPrisonerList (pageNum) {
      if (pageNum) {
        this.pageNum = pageNum
      }
      this.listPrisonerList()
    },

    selectJailCellId () {
      if (!this.jailCellId) {
        return
      }
      this.listPrisonerList()
    },

    listPrisonerList () {
      this.utils.http.get('/prisoner', {
        jailCellId: this.jailCellId,
        'page.pageNum': this.pageNum,
        'page.pageSize': this.pageSize
      })
        .then((response) => {
          let status = response.code
          switch (status) {
            case 200:
              this.editInlineData = response.results
              this.totalNum = response.page.total
              break
            case 2:
              this.utils.message.notLoggedIn()
              break
            case 4:
              this.$Message.error('页码错误')
              break
            default:
              this.utils.message.unknownError()
              break
          }
        })
    },
    /**
     * 拉取所有牢房
     */
    fetchAllJailCell () {
      this.utils.http.post('/jail/cell/list', {
        'page.pageNum': 1,
        'page.pageSize': 9999999
      })
        .then((response) => {
          let status = response.code
          switch (status) {
            case 200:
              let jailCellList = this.changeJailCell(response.results)
              this.jailCellList = this.utils.changeToSelectList(jailCellList, 'id', 'jailCellName')
              break
            default:
              this.utils.message.unknownError()
              break
          }
        })
    },
    changeJailCell (jailCellList) {
      for (let jailCell of jailCellList) {
        jailCell.jailCellName = jailCell.jailName + ' 牢房' + jailCell.id
      }
      jailCellList.unshift({id: -1, jailCellName: '未分配'})
      return jailCellList
    }
  },
  created () {
    this.fetchAllJailCell()
  }
}
</script>

<style scoped>

</style>
