import Axois from 'axios'
import Qs from 'qs'
import Moment from 'moment'

export default {
  /**
   * http 请求 GET, PUT, POST, DELETE
   */
  http: {

    /**
     * GET 请求
     * @param url 请求路径
     * @param params 请求的参数
     * @returns {*} 请求后的数据
     */
    get (url, params) {
      return ajax(url, 'GET', params)
    },

    /**
     * POST 请求
     * @param url 请求路径
     * @param params 请求的参数
     * @returns {*} 请求后的数据
     */
    post (url, params) {
      return ajax(url, 'POST', params)
    },

    /**
     * PUT 请求
     * @param url 请求路径
     * @param params 请求的参数
     * @returns {*} 请求后的数据
     */
    put (url, params) {
      return ajax(url, 'PUT', params)
    },

    /**
     * DELETE 请求
     * @param url 请求路径
     * @param params 请求的参数
     * @returns {*} 请求后的数据
     */
    delete (url, params) {
      return ajax(url, 'DELETE', params)
    }
  },

  /**
   * 将数组转换成 Select 组件所需的格式
   * @param list 原始数组
   * @param label 对应 label 的字段
   * @param value 对应 value 的字段
   * @return {Array} 转换后的数组
   */
  changeToSelectList (list, value, label) {
    let selectList = []
    for (let item of list) {
      let newItem = {}
      newItem.value = item[value]
      newItem.label = item[label]
      selectList.push(newItem)
    }
    return selectList
  },

  moment: Moment,

  /**
   * 消息提示
   */
  message: {

    /**
     * 未登录
     */
    notLoggedIn: function () {
      window.vm.$Message.error('请登录后再进行操作')
      window.vm.$router.push({name: 'login'})
    },

    unknownError: function () {
      window.vm.$Message.error('未知错误')
    }
  }
}

function ajax (url, requestMethod, params = {}) {
  url = url.startsWith('/') ? '/' + url.substring(1) : url
  let contentType = 'application/x-www-form-urlencoded'
  let hasFile = false
  let method = requestMethod === 'GET' ? requestMethod : 'POST'
  if (requestMethod === 'PUT' || requestMethod === 'DELETE') {
    params._method = requestMethod
  }
  return new Promise((resolve, reject) => {
    // 请求
    Axois.request({
      url: url,
      method: method,
      headers: {
        'Content-Type': contentType
      },
      data: requestMethod === 'GET' ? undefined : params,
      params: requestMethod === 'GET' ? params : undefined,
      transformRequest: [
        (data) => hasFile ? data : Qs.stringify(data)
      ],
      timeout: 10000
    })
      .then((result) => {
        resolve(result.data)
      })
      .catch((error) => {
        error.cause = 'request'
        reject(error)
      })
  })
}
