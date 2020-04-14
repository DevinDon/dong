<style lang="less">
    @import "./home.less";
    @import "../../styles/common.less";
</style>
<template>
  <div class="home-main">
    <Row :gutter="10">
      <Col :md="24" :lg="8">
      <Row class-name="home-page-row1" :gutter="10">
        <Col :md="12" :lg="24" :style="{marginBottom: '10px'}">
        <Card style="padding-bottom: 14px">
          <Row type="flex" class="user-infor">
            <Col span="8">
            <Row class-name="made-child-con-middle" type="flex" align="middle">
              <img v-if="my.role === 0" class="avator-img" src="../../../static/0.jpg" style="width: 100%; height: 100%"/>
              <img v-if="my.role === 1" class="avator-img" src="../../../static/1.jpg" style="width: 100%; height: 100%"/>
            </Row>
            </Col>
            <Col span="16" style="padding-left:6px;">
            <Row class-name="made-child-con-middle" type="flex" align="middle">
              <div>
                <b class="card-user-infor-name">{{ my.name }}</b>
                <p>{{ my.name }}的账号</p>
              </div>
            </Row>
            </Col>
          </Row>
          <div class="line-gray"></div>
          <Row class="margin-top-8">
            <Col span="8">
            <p class="notwrap">上次登录时间:</p></Col>
            <Col span="16" class="padding-left-8">
            <template v-if="my.lastLoginTime">
              {{ my.lastLoginTime }}
            </template>
            <template v-else>
              第一次登录
            </template>
            </Col>
          </Row>
        </Card>
        </Col>
      </Row>
      </Col>
      <Col :md="24" :lg="16">
      <Row :gutter="5">
        <Col :xs="24" :sm="12" :md="12" :style="{marginBottom: '10px'}">
        <infor-card
          id-name="user_created_count"
          :end-val="count.technicianCount"
          iconType="android-person-add"
          color="#2d8cf0"
          intro-text="技术员总数"
        ></infor-card>
        </Col>
        <Col :xs="24" :sm="12" :md="12" :style="{marginBottom: '10px'}">
        <infor-card
          id-name="visit_count"
          :end-val="count.areaCount"
          iconType="map"
          color="#64d572"
          :iconSize="50"
          intro-text="地区数量"
        ></infor-card>
        </Col>
        <Col :xs="24" :sm="12" :md="12" :style="{marginBottom: '10px'}">
        <infor-card
          id-name="collection_count"
          :end-val="count.lightCount"
          iconType="lightbulb"
          color="#ffd572"
          intro-text="灯泡数量"
        ></infor-card>
        </Col>
        <Col :xs="24" :sm="12" :md="12" :style="{marginBottom: '10px'}">
        <infor-card
          id-name="transfer_count"
          :end-val="count.lightControllerCount"
          iconType="load-d"
          color="#f25e43"
          intro-text="控制器数量"
        ></infor-card>
        </Col>
      </Row>
      </Col>
      <Col :md="24" :lg="32" :style="{marginTop: '60px'}">
      <Card>
        <p slot="title" class="card-title">
          <Icon type="ios-pulse-strong"></Icon>
          各地区灯光数量统计
        </p>
        <div class="data-source-row">
          <data-source-pie></data-source-pie>
        </div>
      </Card>
      </Col>
    </Row>
  </div>
</template>

<script>
import dataSourcePie from './components/dataSourcePie.vue'
import inforCard from '@/pages/home/components/inforCard'

export default {
  name: 'home',
  components: {
    dataSourcePie,
    inforCard
  },
  data () {
    return {
      my: {},
      count: {
        technicianCount: 0,
        areaCount: 0,
        lightCount: 0,
        lightControllerCount: 0
      },
      showAddNewTodo: false,
      newToDoItemValue: ''
    }
  },
  methods: {
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
              this.my.lastLoginTime = this.utils.moment(this.my.lastLoginTime).format('YYYY-MM-DD HH:mm:ss')
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

    /**
     * 拉取统计数据
     */
    fetchStatisticsInfo () {
      this.utils.http.get('/api/statistics/general')
        .then((response) => {
          let status = response.status
          switch (status) {
            case 0:
              this.count = response.data
              break
            case 2:
              this.utils.message.notLoggedIn()
              break
            default:
              this.utils.message.unknownError()
              break
          }
        })
    }
  },
  created () {
    // this.fetchSelfInfo()
    // this.fetchStatisticsInfo()
  }
}
</script>
