<style lang="less">
    @import './editable-table.less';
</style>

<template>
  <div>
    <div class="title">
      <Button type="info" style="width: 100%" @click="showAddModal">添加</Button>
    </div>
    <Table :ref="refs" :columns="columnsList" :data="thisTableData" border disabled-hover></Table>
    <Modal title="修改牢房" v-model="isEditModalVisible" width="320" @on-ok="changeJailCell">
      <Form ref="editForm" :model="editForm" :rules="formRules" label-position="top" @submit.native.prevent>
        <FormItem label="牢房编号" prop="id">
          <div>{{editForm.id}}</div>
        </FormItem>
        <FormItem label="监狱名称" prop="jailId">
          <Select v-model="editForm.jailId" style="width: 100%" clearable @on-change="changeJailAddress">
            <Option v-for="item in jailList" :value="item.value" :key="item.value">{{ item.label }}</Option>
          </Select>
        </FormItem>
        <FormItem label="监狱地址">
          <div>{{editForm.address}}</div>
        </FormItem>
        <FormItem label="牢房大小" prop="size">
          <InputNumber :min="1" v-model="editForm.size" placeholder="请输入牢房大小" :clearable="true">
          </InputNumber>
        </FormItem>
      </Form>
    </Modal>

    <Modal title="添加牢房" v-model="isAddModalVisible" width="320" @on-ok="handleAdd" @on-cancel="resetAddForm">
      <Form ref="addForm" :model="addForm" :rules="formRules" label-position="top" @submit.native.prevent>
        <FormItem label="监狱名称" prop="jailId">
          <Select v-model="addForm.jailId" style="width: 100%" clearable @on-change="getJailAddress">
            <Option v-for="item in jailList" :value="item.value" :key="item.value">{{ item.label }}</Option>
          </Select>
        </FormItem>
        <FormItem label="监狱地址">
          <div>{{addForm.address}}</div>
        </FormItem>
        <FormItem label="牢房大小" prop="size">
          <InputNumber :min="1" v-model="addForm.size" placeholder="请输入牢房大小" :clearable="true">
          </InputNumber>
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
  }, '修改')
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
        vm.utils.http.delete('/jail/cell/delete', {
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
/**
 * 匹配到相应的数据
 * @param vm 当前域
 * @param value 类型的id
 * @return 返回类型的名称
 */
const showType = (vm, value) => {
  for (let item of vm.lightType) {
    if (value === item.value) {
      return item.label
    }
  }
}

/**
 * 匹配到相应的数据
 * @param vm
 * @param value 类型的id
 * @return string 返回控制id
 */
const showController = (vm, value) => {
  for (let item of vm.controllerList) {
    if (value === item.value) {
      return item.label
    }
  }
}

/**
 * 匹配到相应的地区
 * @param vm
 * @param value 类型的id
 * @return string 返回控制id
 */
const showArea = (vm, value) => {
  for (let item of vm.areaList) {
    if (value === item.value) {
      return item.label
    }
  }
}
export default {
  name: 'canEditTable',
  props: {
    refs: String,
    columnsList: Array,
    jailList: Array,
    jailInfoList: Array,
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
      isEditModalVisible: false,
      jailId: 0,
      addForm: {
        jail: -1,
        address: '',
        size: 1
      },
      editForm: {
        id: 0,
        jail: -1,
        address: '',
        size: 1
      },
      // 表单校验规则
      formRules: {
        jailId: [
          {required: true, message: '请选择监狱'}
        ],
        size: [
          {type: 'number', required: true, message: '请填写牢房大小'}
        ]
      },
      number: 19
    }
  },
  created () {
    this.init()
  },
  methods: {

    getJailAddress () {
      if (!this.addForm.jailId) {
        this.addForm.address = ''
        return
      }
      for (let jailInfo of this.jailInfoList) {
        if (jailInfo.id === this.addForm.jailId) {
          this.addForm.address = jailInfo.address
        }
      }
    },

    changeJailAddress () {
      if (!this.editForm.jailId) {
        this.editForm.address = ''
        return
      }
      for (let jailInfo of this.jailInfoList) {
        if (jailInfo.id === this.editForm.jailId) {
          this.editForm.address = jailInfo.address
        }
      }
    },
    /**
     * 添加菜单
     */
    handleAdd () {
      this.validateAdd()
        .then(() => {
          this.utils.http.post('/jail/cell', {
            name: this.addForm.name,
            jailId: this.addForm.jailId,
            size: this.addForm.size
          })
            .then((response) => {
              let status = response.code
              if (status === 200) {
                this.$emit('change')
                this.$Message.success('添加牢房成功')
              } else {
                this.dealError(status)
              }
            })
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
     * 重置添加表单
     */
    resetAddForm () {
      this.addForm.size = 1
      this.addForm.jail = undefined
      this.addForm.address = ''
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
     *
     * @param index 修改的下标
     */
    handleEdit: function (index) {
      let editLight = this.edittingStore[index]
      this.edittingStore[index].saving = true
      let edittingRow = this.edittingStore[index]
      edittingRow.editting = false
      edittingRow.saving = false
      this.utils.http.put('/api/lights/' + editLight.id, {
        name: editLight.name,
        controllerId: editLight.controllerId,
        number: editLight.number,
        type: editLight.type,
        areaId: editLight.areaId
      })
        .then((response) => {
          let status = response.status
          if (status !== 0) {
            this.edittingStore[index] = JSON.parse(JSON.stringify(this.thisTableData[index]))
            this.thisTableData = this.handleBackdata(this.thisTableData)
            this.dealError(status)
          } else {
            this.thisTableData = JSON.parse(JSON.stringify(this.edittingStore))
            this.$Message.success('修改灯光属性成功')
            this.$emit('change')
          }
        })
    },

    /**
     * 修改牢房信息
     */
    changeJailCell () {
      this.utils.http.put('/jail/cell/update', {
        id: this.editForm.id,
        jailId: this.editForm.jailId,
        size: this.editForm.size
      })
        .then((response) => {
          let status = response.code
          if (status === 200) {
            this.$Message.success('修改牢房成功')
            this.$emit('change')
          } else {
            this.thisTableData = this.handleBackdata(this.thisTableData)
            this.dealError(status)
          }
        })
    },

    /**
     * 处理错误
     */
    dealError (status) {
      switch (status) {
        default:
          this.utils.message.unknownError()
          break
      }
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
              if (item.key === 'type') {
                return h('span', showType(this, currentRow[item.key]))
              }
              if (item.key === 'controllerId') {
                return h('span', showController(this, currentRow[item.key]))
              }
              if (item.key === 'areaId') {
                return h('span', showArea(this, currentRow[item.key]))
              }
              return h('span', currentRow[item.key])
            } else {
              // // 若为属性为控制器 则变为下拉选项
              // if (item.key === 'controllerId') {
              //   return h('Select', {
              //     props: {
              //       value: currentRow[item.key]
              //     },
              //     on: {
              //       'on-change' (event) {
              //         let key = param.column.key
              //         vm.edittingStore[param.index][key] = event
              //       }
              //     }
              //   }, this.controllerList.map((item) => {
              //     return h('Option', {
              //       props: {
              //         value: item.value
              //       }
              //     }, item.label)
              //   }))
              // }
              //
              // if (item.key === 'areaId') {
              //   return h('Select', {
              //     props: {
              //       value: currentRow[item.key]
              //     },
              //     on: {
              //       'on-change' (event) {
              //         let key = param.column.key
              //         vm.edittingStore[param.index][key] = event
              //       }
              //     }
              //   }, this.areaList.map((item) => {
              //     return h('Option', {
              //       props: {
              //         value: item.value
              //       }
              //     }, item.label)
              //   }))
              // }
              //
              // // 若为属性为灯光类型 则变为下拉选项
              // if (item.key === 'type') {
              //   return h('Select', {
              //     props: {
              //       value: currentRow[item.key]
              //     },
              //     on: {
              //       'on-change' (event) {
              //         let key = param.column.key
              //         vm.edittingStore[param.index][key] = event
              //       }
              //     }
              //   }, this.lightType.map((item) => {
              //     return h('Option', {
              //       props: {
              //         value: item.value
              //       }
              //     }, item.label)
              //   }))
              // }
              //
              // if (item.key === 'number') {
              //   return h('InputNumber', {
              //     props: {
              //       style: {
              //         width: '100%'
              //       },
              //       min: 1,
              //       value: currentRow[item.key]
              //     },
              //     on: {
              //       'on-change' (event) {
              //         let key = param.column.key
              //         vm.edittingStore[param.index][key] = event
              //       }
              //     }
              //   })
              // }

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
