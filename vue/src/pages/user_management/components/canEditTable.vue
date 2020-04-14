<style lang="less">
    @import './editable-table.less';
</style>

<template>
    <div>
        <Button type="info" long style="margin-bottom: 20px;" @click="showAddModal">添加</Button>
        <Table :ref="refs" :columns="columnsList" :data="thisTableData" border disabled-hover></Table>
      <Modal title="添加账号" v-model="isAddModalVisible" width="320" @on-ok="handleAdd" @on-cancel="resetAddForm">
        <Form ref="addForm" :model="addForm" :rules="formRules" label-position="top" @submit.native.prevent>
          <FormItem label="昵称" prop="name">
            <Input v-model="addForm.name" placeholder="请输入昵称" :clearable="true"/>
          </FormItem>
          <FormItem label="用户名" prop="username">
            <Input v-model="addForm.username" placeholder="请输入用户名" :clearable="true"/>
          </FormItem>
          <FormItem label="密码" prop="password">
            <Input type="password" v-model="addForm.password" placeholder="请输入密码" :clearable="true"/>
          </FormItem>
        </Form>
      </Modal>
    </div>
</template>

<script>
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
          vm.editUser(index)
        }
      }
    }
  }, currentRow.editting ? '保存' : '编辑')
}
const deleteButton = (vm, h, currentRow, index) => {
  // 假如用户为自身则不能删除
  if (vm.thisTableData[index].id === vm.my.id) {
    return
  }
  return h('Poptip', {
    props: {
      confirm: true,
      title: '您确定要删除这条数据吗?',
      transfer: true
    },
    on: {
      'on-ok': () => {
        vm.utils.http.delete('/user/delete', {
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
const changeRole = (vm, currentRow, item) => {
  if (item.key !== 'role') {
    return currentRow[item.key]
  }
  for (let areaItem of vm.roleList) {
    if (currentRow[item.key] === areaItem.value) {
      return areaItem.label
    }
  }
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
    roleList: Array
  },
  data () {
    return {
      columns: [],
      thisTableData: [],
      edittingStore: [],
      isAddModalVisible: false,
      addForm: {
        name: '',
        username: '',
        password: ''
      },
      // 表单校验规则
      formRules: {
        name: [
          {type: 'string', required: true, message: '请填写昵称'},
          {max: 12, message: '内容过长'}
        ],
        username: [
          {type: 'string', required: true, message: '请填写用户名'},
          {max: 16, message: '内容过长'}
        ],
        password: [
          {type: 'string', required: true, message: '请填写密码'},
          {max: 16, message: '内容过长'}
        ]
      }
    }
  },
  created () {
    this.init()
  },
  methods: {

    /**
     * 修改指定下标数据
     * @param index 修改的数组下标
     */
    editUser (index) {
      let editUser = this.edittingStore[index]
      this.edittingStore[index].saving = true
      let edittingRow = this.edittingStore[index]
      edittingRow.editting = false
      edittingRow.saving = false
      this.utils.http.put('/user/update', {
        id: editUser.id,
        name: editUser.name,
        password: editUser.password
      })
        .then((response) => {
          let status = response.code
          if (status !== 200) {
            this.edittingStore[index] = JSON.parse(JSON.stringify(this.thisTableData[index]))
            this.thisTableData = this.handleBackdata(this.thisTableData)
            this.dealError(status)
          } else {
            this.$Message.success('修改成功')
            this.thisTableData = JSON.parse(JSON.stringify(this.edittingStore))
          }

        })
    },
    /**
     * 处理错误信息
     */
    dealError (status) {
      switch (status) {
        case 2:
          this.utils.message.notLoggedIn()
          break
        case 4:
          this.$Message.error('昵称太长')
          break
        case 5:
          this.$Message.error('操作用户不存在')
          break
        case 6:
          this.$Message.error('用户不存在')
          break
        case 421:
          this.$Message.error('用户名已存在')
          break
        default:
          this.utils.message.unknownError()
          break
      }
    },
    /**
     * 添加用户方法
     */
    handleAdd () {
      this.validateAdd()
        .then(
          () => {
            this.utils.http.post('/user', {
              name: this.addForm.name,
              username: this.addForm.username,
              password: this.addForm.password
            })
              .then((response) => {
                let status = response.code
                if (status === 200) {
                  this.$Message.success('添加用户成功')
                  this.$emit('change')
                } else {
                  this.dealError(status)
                }
              })
          }
        )
        .catch((err) => {
          // 判断错误类型,给出相应提示
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
      this.$refs.addForm.resetFields()
    },

    /**
     * 显示添加弹窗
     */
    showAddModal () {
      this.isAddModalVisible = true
    },

    /**
     * 检验表单添加的合法性
     * @returns {Promise<any>} 合法返回 resolve() 违法则返回 reject
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
     * 初始化布局
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
            // 判断当前行是否正在进行编辑
            if (!currentRow.editting) {
              return h('span', changeRole(this, currentRow, item))
            } else {
              // 角色不可修改
              if (item.key === 'role') {
                return h('span', changeRole(this, currentRow, item))
              }

              if (item.key !== 'username') {
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
              return h('span', changeRole(this, currentRow, item))
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
