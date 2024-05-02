import request from '@/utils/request'

// 查询分组管理列表
export function listGroup(query) {
  return request({
    url: '/lms/group/list/v2',
    method: 'get',
    params: query
  })
}

// 查询分组管理详细
export function getGroup(groupId) {
  return request({
    url: '/lms/group/' + groupId,
    method: 'get'
  })
}

// 新增分组管理
export function addGroup(data) {
  return request({
    url: '/lms/group',
    method: 'post',
    data: data
  })
}

// 修改分组管理
export function updateGroup(data) {
  return request({
    url: '/lms/group',
    method: 'put',
    data: data
  })
}

// 删除分组管理
export function delGroup(groupId) {
  return request({
    url: '/lms/group/' + groupId,
    method: 'delete'
  })
}
