<style lang="less">
    @import './editable-table.less';
</style>

<template>
    <div>
      <Button type="info" long style="margin-bottom: 20px;" @click="showAddModal">添加</Button>
      <Table :ref="refs" :columns="columnsList" :data="thisTableData" border disabled-hover></Table>
      <Modal title="添加囚犯" v-model="isAddModalVisible" width="320" @on-ok="handleAdd" @on-cancel="resetAddForm">
        <Form ref="addForm" :model="addForm" :rules="formRules" label-position="top" @submit.native.prevent>
          <FormItem label="囚犯罪行" prop="violation">
            <Input v-model="addForm.violation" placeholder="请输入囚犯罪行" :clearable="true"/>
          </FormItem>
          <FormItem label="牢房" prop="jailCellId">
            <Select v-model="addForm.jailCellId" style="width: 100%" clearable>
              <Option v-for="item in jailCellList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>
          </FormItem>
        </Form>
      </Modal>
      <Modal title="修改囚犯信息" v-model="isEditModalVisible" width="320" @on-ok="editPrisoner">
        <Form ref="editForm" :model="editForm" :rules="formRules" label-position="top" @submit.native.prevent>
          <FormItem label="囚犯罪行" prop="violation">
            <Input v-model="editForm.violation" placeholder="请输入囚犯罪行" :clearable="true"/>
          </FormItem>
          <FormItem label="牢房" prop="jailCellId">
            <Select v-model="editForm.jailCellId" style="width: 100%" clearable>
              <Option v-for="item in jailCellList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>
          </FormItem>
        </Form>
      </Modal>
      <Modal title="囚犯详细信息" v-model="isInfoModalVisible" width="320" @on-ok="dealInfoData">
        <Form ref="infoForm" :model="infoForm" :rules="infoFormRules" label-position="top" @submit.native.prevent>
          <FormItem label="囚犯姓名" prop="name">
            <Input v-model="infoForm.name" placeholder="请输入囚犯姓名" :clearable="true"/>
          </FormItem>
          <FormItem label="囚犯身份证" prop="idCard">
            <Input v-model="infoForm.idCard" placeholder="请输入囚犯身份信息" :clearable="true"/>
          </FormItem>
          <FormItem label="囚犯出生日期" prop="birthTime">
            <Date-picker v-model="infoForm.birthTime" format="yyyy-MM-dd" type="date" placeholder="选择日期"
                         @on-change="changeBirthTime"
            ></Date-picker>
          </FormItem>
        </Form>
      </Modal>
    </div>
</template>

<script>
const editButton = (vm, h, currentRow, index) => {
  return h('Button', {
    props: {
      type: 'primary',
      loading: currentRow.saving
    },
    style: {
      margin: '0 5px'
    },
    on: {
      'click': () => {
        vm.editForm = currentRow
        vm.isEditModalVisible = true
      }
    }
  }, '编辑')
}
const infoButton = (vm, h, currentRow, index) => {
  return h('Button', {
    props: {
      type: currentRow.infoId === -1 ? 'success' : 'primary',
      loading: currentRow.saving
    },
    style: {
      margin: '0 5px'
    },
    on: {
      'click': () => {
        vm.dealPrisonerInfo(currentRow.infoId, currentRow.id)
      }
    }
  }, currentRow.infoId === -1 ? '新增' : '编辑')
}
const deleteButton = (vm, h, currentRow, index) => {
  return h('Poptip', {
    props: {
      confirm: true,
      title: '您确定要删除这条数据吗?',
      transfer: true
    },
    on: {
      'on-ok': () => {
        vm.utils.http.delete('/prisoner/delete', {
          id: vm.thisTableData[index].id
        })
          .then((response) => {
            let status = response.code
            switch (status) {
              case 200:
                vm.$Message.success('删除成功')
                vm.thisTableData.splice(index, 1)
                vm.edittingStore.splice(index, 1)
                break
              case 2:
                this.utils.message.notLoggedIn()
                break
              default:
                vm.utils.message.unknownError()
                break
            }
          })
      }
    }
  }, [
    h('Button', {
      style: {
        margin: '0 5px'
      },
      props: {
        type: 'error',
        placement: 'top'
      }
    }, '删除')
  ])
}
export default {
  name: 'canEditTable',
  props: {
    my: Object,
    refs: String,
    columnsList: Array,
    value: Array,
    url: String,
    editIncell: {
      type: Boolean,
      default: false
    },
    hoverShow: {
      type: Boolean,
      default: false
    },
    jailCellList: Array
  },
  data () {
    return {
      columns: [],
      thisTableData: [],
      edittingStore: [],
      isAddModalVisible: false,
      isEditModalVisible: false,
      isInfoModalVisible: false,
      infoForm: {},
      addForm: {
        violation: '',
        jailCellId: ''
      },
      editForm: {
        id: undefined,
        violation: '',
        jailCellId: ''
      },
      // 表单校验规则
      formRules: {
        violation: [
          {type: 'string', required: true, message: '请填写罪行'},
          {max: 32, message: '内容过长'}
        ]
      },
      infoFormRules: {
        name: [
          {type: 'string', required: true, message: '请填写囚犯姓名'},
          {max: 32, message: '内容过长'}
        ],
        idCard: [
          {type: 'string', required: true, message: '请填写囚犯身份证'},
          {max: 32, message: '身份证最大32位'},
        ],
        birthTime: [
          {type: 'date', required: true, message: '请选择囚犯出生日期'}
        ]
      }
    }
  },
  created () {
    this.init()
  },
  methods: {
    changeBirthTime(time) {
      this.infoForm.birthTime = time + ' 00:00:00'
    },
    timeStamp2String (time) {
      let datetime = new Date()
      datetime.setTime(time)
      let year = datetime.getFullYear()
      let month = datetime.getMonth() + 1
      let date = datetime.getDate()
      let hour = datetime.getHours()
      let minute = datetime.getMinutes()
      let second = datetime.getSeconds()
      let mseconds = datetime.getMilliseconds()
      return year + '-' + month + '-' + date + ' ' + hour + ':' + minute + ':' + second + '.' + mseconds
    },
    dealPrisonerInfo (infoId, id) {
      if (infoId !== -1) {
        this.utils.http.get('/prisoner/info', {
          id: infoId
        })
          .then((response) => {
            let status = response.code
            if (status !== 200) {
              this.dealError(status)
            } else {
              let result = response.results
              let birthTime = '' + this.timeStamp2String(result.birthTime)
              console.log(birthTime)
              result.birthTime = birthTime
              console.log(result)
              this.infoForm = result
            }
          })
      } else {
        this.infoForm = {}
      }
      this.infoForm.prisonerId = id
      this.isInfoModalVisible = true
    },
    dealInfoData () {
      console.log(this.infoForm.birthTime)
      if (this.infoForm.id) {
        this.validateInfo()
          .then(
            this.utils.http.post('/prisoner/info/update', {
              id: this.infoForm.id,
              name: this.infoForm.name,
              idCard: this.infoForm.idCard,
              birthTime: this.infoForm.birthTime
            })
              .then((response) => {
                let status = response.code
                if (status !== 200) {
                  this.dealError(status)
                } else {
                  this.thisTableData = JSON.parse(JSON.stringify(this.edittingStore))
                  this.$Message.success('修改成功')
                  this.$emit('change')
                }
              })
          )
      } else {
        this.validateInfo()
          .then(
            this.utils.http.post('/prisoner/info', {
              prisonerId: this.infoForm.prisonerId,
              name: this.infoForm.name,
              idCard: this.infoForm.idCard,
              birthTime: this.infoForm.birthTime
            })
              .then((response) => {
                let status = response.code
                if (status !== 200) {
                  this.dealError(status)
                } else {
                  this.thisTableData = JSON.parse(JSON.stringify(this.edittingStore))
                  this.$Message.success('添加成功')
                  this.$emit('change')
                }
              })
          )
      }
    },

    /**
     * 编辑囚犯
     */
    editPrisoner () {
      this.validateEdit()
        .then(
          this.utils.http.put('/prisoner/update', {
            id: this.editForm.id,
            violation: this.editForm.violation,
            jailCellId: this.editForm.jailCellId
          })
            .then((response) => {
              let status = response.code
              if (status !== 200) {
                this.dealError(status, response.msg)
              } else {
                this.thisTableData = JSON.parse(JSON.stringify(this.edittingStore))
                this.$Message.success('修改成功')
                this.$emit('change')
              }
            })
        )
    },

    /**
     * 添加囚犯
     */
    handleAdd () {
      this.validateAdd()
        .then(
          () => {
            this.utils.http.post('/prisoner', {
              violation: this.addForm.violation,
              jailCellId: this.addForm.jailCellId
            })
              .then((response) => {
                let status = response.code
                if (status === 200) {
                  this.$Message.success('添加囚犯成功')
                  this.$emit('change')
                } else {
                  this.dealError(status, response.msg)
                }
              })
          }
        )
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
    dealError (status, msg) {
      switch (status) {
        case 2:
          this.utils.message.notLoggedIn()
          break
        case 4:
          this.$Message.error('昵称太长')
          break
        case 5:
          this.$Message.error('技术员不存在')
          break
        case 6:
          this.$Message.error('地区不存在')
          break
        default:
          this.$Message.error(msg)
          break
      }
    },
    resetAddForm () {
      this.$refs.addForm.resetFields()
    },
    showAddModal () {
      this.isAddModalVisible = true
    },
    validateInfo () {
      return new Promise((resolve, reject) => {
        this.$refs.infoForm.validate()
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
    validateEdit () {
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
    validateAdd () {
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
    init () {
      let vm = this
      let editableCell = this.columnsList.filter(item => {
        if (item.editable) {
          if (item.editable === true) {
            return item
          }
        }
      })
      let cloneData = JSON.parse(JSON.stringify(this.value))
      let res = []
      res = cloneData.map((item, index) => {
        let isEditting = false
        if (this.thisTableData[index]) {
          if (this.thisTableData[index].editting) {
            isEditting = true
          } else {
            for (const cell in this.thisTableData[index].edittingCell) {
              if (this.thisTableData[index].edittingCell[cell] === true) {
                isEditting = true
              }
            }
          }
        }
        if (isEditting) {
          return this.thisTableData[index]
        } else {
          this.$set(item, 'editting', false)
          let edittingCell = {}
          editableCell.forEach(item => {
            edittingCell[item.key] = false
          })
          this.$set(item, 'edittingCell', edittingCell)
          return item
        }
      })
      this.thisTableData = res
      this.edittingStore = JSON.parse(JSON.stringify(this.thisTableData))
      this.columnsList.forEach(item => {
        if (item.key === 'infoId') {
          item.render = (h, param) => {
            let currentRowData = this.thisTableData[param.index]
            let children = []
            children.push(infoButton(this, h, currentRowData, param.index))
            return h('div', children)
          }
        }
        if (item.handle) {
          item.render = (h, param) => {
            let currentRowData = this.thisTableData[param.index]
            let children = []
            item.handle.forEach(item => {
              if (item === 'edit') {
                children.push(editButton(this, h, currentRowData, param.index))
              } else if (item === 'delete') {
                children.push(deleteButton(this, h, currentRowData, param.index))
              }
            })
            return h('div', children)
          }
        }
      })
    },
    handleBackdata (data) {
      let clonedData = JSON.parse(JSON.stringify(data))
      clonedData.forEach(item => {
        delete item.editting
        delete item.edittingCell
        delete item.saving
      })
      return clonedData
    }
  },
  watch: {
    value (data) {
      this.init()
    }
  }
}
</script>
