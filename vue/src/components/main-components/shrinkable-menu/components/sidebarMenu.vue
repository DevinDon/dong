<style lang="less">
    @import '../styles/menu.less';
</style>

<template>
    <Menu ref="sideMenu" :active-name="$route.name" :open-names="openNames" :theme="menuTheme" width="auto" @on-select="changeMenu">
        <template v-for="item in menuList">
            <MenuItem :name="item.path" :key="'menuitem' + item.name" style="text-align: left"  v-if="item.role === my.role || item.role === undefined">
                <Icon :type="item.icon" :size="iconSize" :key="'menuicon' + item.name"></Icon>
                <span class="layout-text" :key="'title' + item.name">{{ item.name }}</span>
            </MenuItem>
        </template>
    </Menu>
</template>

<script>
export default {
  name: 'sidebarMenu',
  props: {
    menuList: Array,
    my: Object,
    iconSize: Number,
    menuTheme: {
      type: String,
      default: 'dark'
    },
    openNames: {
      type: Array
    }
  },

  methods: {
    changeMenu (active) {
      this.$emit('on-change', active)
    }
  },
  updated () {
    this.$nextTick(() => {
      if (this.$refs.sideMenu) {
        this.$refs.sideMenu.updateOpened()
      }
    })
  }

}
</script>
