import request from '@/utils/request'

// 查询设备列表
export function listDevice(query) {
  return request({
    url: '/lms/device/list',
    method: 'get',
    params: query
  })
}



export function statisticsTypeCount() {
    return request({
        url: "/lms/device/statistics/type/count",
        method: "get",
    });
}




// 查询设备详细
export function getDevice(deviceId) {
  return request({
    url: '/lms/device/' + deviceId,
    method: 'get'
  })
}

// 新增设备
export function addDevice(data) {
  return request({
    url: '/lms/device',
    method: 'post',
    data: data
  })
}

// 修改设备
export function updateDevice(data) {
  return request({
    url: '/lms/device',
    method: 'put',
    data: data
  })
}

// 删除设备
export function delDevice(deviceId) {
  return request({
    url: '/lms/device/' + deviceId,
    method: 'delete'
  })
}
