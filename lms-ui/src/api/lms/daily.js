import request from '@/utils/request'

// 查询周报列表
export function listDaily(query) {
  return request({
    url: '/lms/daily/list',
    method: 'get',
    params: query
  })
}

// 查询周报详细
export function getDaily(id) {
  return request({
    url: '/lms/daily/' + id,
    method: 'get'
  })
}

// 新增周报
export function addDaily(data) {
  return request({
    url: '/lms/daily',
    method: 'post',
    data: data
  })
}

// 修改周报
export function updateDaily(data) {
  return request({
    url: '/lms/daily',
    method: 'put',
    data: data
  })
}

// 删除周报
export function delDaily(id) {
  return request({
    url: '/lms/daily/' + id,
    method: 'delete'
  })
}
