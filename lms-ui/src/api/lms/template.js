import request from '@/utils/request'




// 查询审核模板列表
export function listAllTemplate() {
  return request({
    url: '/lms/template/list/all',
    method: 'get',
  })
}


// 查询审核模板列表
export function listTemplate(query) {
  return request({
    url: '/lms/template/list',
    method: 'get',
    params: query
  })
}

// 查询审核模板详细
export function getTemplate(id) {
  return request({
    url: '/lms/template/' + id,
    method: 'get'
  })
}

// 新增审核模板
export function addTemplate(data) {
  return request({
    url: '/lms/template',
    method: 'post',
    data: data
  })
}



// 新增审核模板
export function addTemplateV2(data) {
  return request({
    url: '/lms/template/v2',
    method: 'post',
    data: data
  })
}



// 修改审核模板
export function updateTemplate(data) {
  return request({
    url: '/lms/template',
    method: 'put',
    data: data
  })
}

// 删除审核模板
export function delTemplate(id) {
  return request({
    url: '/lms/template/' + id,
    method: 'delete'
  })
}
