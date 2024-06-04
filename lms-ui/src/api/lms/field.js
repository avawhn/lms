import request from '@/utils/request'



// 查询模板字段详细
export function getFieldByTemplateId(id) {
  return request({
    url: '/lms/audit/template/field/tid/' + id,
    method: 'get'
  })
}




// 查询模板字段列表
export function listField(query) {
  return request({
    url: '/lms/audit/template/field/list',
    method: 'get',
    params: query
  })
}

// 查询模板字段详细
export function getField(id) {
  return request({
    url: '/lms/audit/template/field/' + id,
    method: 'get'
  })
}

// 新增模板字段
export function addField(data) {
  return request({
    url: '/lms/audit/template/field',
    method: 'post',
    data: data
  })
}

// 修改模板字段
export function updateField(data) {
  return request({
    url: '/lms/audit/template/field',
    method: 'put',
    data: data
  })
}

// 删除模板字段
export function delField(id) {
  return request({
    url: '/lms/audit/template/field/' + id,
    method: 'delete'
  })
}
