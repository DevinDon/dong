<template>
    <div style="width:100%;height:100%;" id="data_source_con"></div>
</template>

<script>
import echarts from 'echarts'

export default {
  name: 'dataSourcePie',
  data () {
    return {
      areaList: [],
      pieData: []
    }
  },
  mounted () {
    this.fetchLightCountOfEachArea()
  },
  methods: {
    /**
     * 拉取统计数据
     */
    fetchLightCountOfEachArea () {
      this.utils.http.get('/api/areas/lights/statistics')
        .then((response) => {
          let status = response.status
          switch (status) {
            case 0:
              this.areaList = this.getAreaList(response.data)
              this.pieData = this.getPieData(response.data)
              this.paint()
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

    getAreaList (list) {
      let areaList = []
      for (let item of list) {
        areaList.push(item.name)
      }
      return areaList
    },

    getPieData (list) {
      let pieData = []
      for (let item of list) {
        let map = {}
        map.value = item.lightCount
        map.name = item.name
        map.itemStyle = {normal: {color: this.getColor()}}
        pieData.push(map)
      }
      return pieData
    },

    getColor () {
      return '#'+Math.floor(Math.random()*16777215).toString(16);
    },

    paint () {
      let dataSourcePie = echarts.init(document.getElementById('data_source_con'))

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'right',
          data: this.areaList
        },
        series: [
          {
            name: '灯光数量',
            type: 'pie',
            radius: '66%',
            center: ['50%', '60%'],
            data: this.pieData,
            itemStyle: {
              emphasis: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }
      dataSourcePie.setOption(option)
      window.addEventListener('resize', function () {
        dataSourcePie.resize()
      })
    }
  }
}
</script>
