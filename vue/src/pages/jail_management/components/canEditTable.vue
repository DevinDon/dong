<style lang="less">
    @import './editable-table.less';
</style>

<template>
    <div>
      <div class="title">
        <Button type="info" @click="showAddModal" long>添加</Button>
      </div>
        <Table :ref="refs" :columns="columnsList" :data="thisTableData" border disabled-hover></Table>
      <Modal title="添加监狱" v-model="isAddModalVisible" width="320" @on-ok="handleAdd" @on-cancel="resetAddForm">
        <Form ref="addForm" :model="addForm" :rules="formRules" label-position="top" @submit.native.prevent>
          <FormItem label="名称" prop="name">
            <Input v-model="addForm.name" placeholder="请输入监狱名称" :clearable="true"/>
          </FormItem>
          <FormItem label="监狱地址" prop="address">
            <Input v-model="addForm.address" placeholder="请输入监狱地址" :clearable="true"/>
          </FormItem>
        </Form>
      </Modal>
    </div>
</template>

<script>
const regulationButton = (vm, h, currentRow, index) => {
  return h('Button', {
    props: {
      type: 'primary'
    },
    style: {
      margin: '0 5px'
    },
    on: {
      'click': () => {
        vm.isEditModalVisible = true
        vm.editForm = vm.thisTableData[index]
      }
    }
  }, '调控')
}

const editButton = (vm, h, currentRow, index) => {
  return h('Button', {
    props: {
      type: currentRow.editting ? 'success' : 'primary',
      loading: currentRow.saving
    },
    style: {
      margin: '0 5px'
    },
    on: {
      'click': () => {
        if (!currentRow.editting) {
          if (currentRow.edittingCell) {
            for (let name in currentRow.edittingCell) {
              currentRow.edittingCell[name] = false
              vm.edittingStore[index].edittingCell[name] = false
            }
          }
          vm.edittingStore[index].editting = true
          vm.thisTableData = JSON.parse(JSON.stringify(vm.edittingStore))
        } else {
          vm.editJail(index)
        }
      }
    }
  }, currentRow.editting ? '保存' : '编辑')
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
        vm.utils.http.delete('/jail/delete', {
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
    }
  },
  data () {
    return {
      columns: [],
      thisTableData: [],
      edittingStore: [],
      isAddModalVisible: false,
      addForm: {
        name: '',
        address: ''
      },
      // 表单校验规则
      formRules: {
        name: [
          {type: 'string', required: true, message: '请填写名称'},
          {max: 12, message: '名称过长'}
        ],
        address: [
          {type: 'string', required: true, message: '请填写监狱地址'},
          {max: 25, message: '监狱地址长度最大为25'}
        ]
      }
    }
  },
  created () {
    this.init()
  },
  methods: {

    /**
     * 修改监狱信息
     */
    editJail (index) {
      let editJail = this.edittingStore[index]
      this.edittingStore[index].saving = true
      let edittingRow = this.edittingStore[index]
      edittingRow.editting = false
      edittingRow.saving = false
      this.utils.http.put('/jail/update', {
        id: editJail.id,
        name: editJail.name,
        address: editJail.address
      })
        .then((response) => {
          let status = response.code
          if (status !== 200) {
            this.edittingStore[index] = JSON.parse(JSON.stringify(this.thisTableData[index]))
            this.thisTableData = this.handleBackdata(this.thisTableData)
            this.dealError(status)
          } else {
            this.thisTableData = JSON.parse(JSON.stringify(this.edittingStore))
            this.$Message.success('修改监狱信息成功')
            this.$emit('change')
          }
        })
    },
    /**
     * 添加监狱
     */
    handleAdd () {
      this.validateAdd()
        .then(
          () => {
            this.utils.http.post('/jail', {
              name: this.addForm.name,
              address: this.addForm.address
            })
              .then((response) => {
                let status = response.code
                if (status === 200) {
                  this.$Message.success('添加监狱成功')
                  this.$emit('change')
                } else {
                  this.dealError(status)
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
    dealError (status) {
      switch (status) {
        default:
          this.utils.message.unknownError()
          break
      }
    },
    /**
     * 重置添加表单
     */
    resetAddForm () {
      this.addForm.brightness = 50
    },
    /**
     * 展示添加模块
     */
    showAddModal () {
      this.isAddModalVisible = true
    },
    /**
     * 检验添加是否合法
     * @return {Promise<any>} reject 不合法 resolve 合法
     */
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
    /**
     * 界面初始化
     */
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
        if (item.editable) {
          item.render = (h, param) => {
            let currentRow = this.thisTableData[param.index]
            if (!currentRow.editting) {
              return h('span', currentRow[item.key])
            } else {

              if (item.key === 'channel') {
                return h('InputNumber', {
                  props: {
                    style: {
                      width: '100%'
                    },
                    min: 1,
                    value: currentRow[item.key]
                  },
                  on: {
                    'on-change' (event) {
                      let key = param.column.key
                      vm.edittingStore[param.index][key] = event
                    }
                  }
                })
              }

              return h('Input', {
                props: {
                  type: 'text',
                  value: currentRow[item.key]
                },
                on: {
                  'on-change' (event) {
                    let key = param.column.key
                    vm.edittingStore[param.index][key] = event.target.value
                  }
                }
              })
            }
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
              } else if (item === 'regulation') {
                children.push(regulationButton(this, h, currentRowData, param.index))
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
