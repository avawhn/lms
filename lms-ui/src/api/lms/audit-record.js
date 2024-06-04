import request from '@/utils/request'

// 查询审核记录列表
export function listRecord(query) {
  return request({
    url: '/lms/audit/record/list',
    method: 'get',
    params: query
  })
}

export function listRecordByUserId(id) {
    return request({
        url: "/lms/audit/record/list/user/" + id,
        method: "get",
    });
}


export function listRecordByAuditorId(id) {
    return request({
        url: "/lms/audit/record/list/" + id,
        method: "get",
    });
}

export function revokeRecord(id) {
    return request({
        url: "/lms/audit/record/revoke/" + id,
        method: "put",
    });
}


export function passRecord(id) {
    return request({
        url: "/lms/audit/record/pass/" + id,
        method: "put",
    });
}


export function refuseRecord(id) {
    return request({
        url: "/lms/audit/record/refuse/" + id,
        method: "put",
    });
}


// 查询审核记录详细
export function getRecord(id) {
  return request({
    url: '/lms/audit/record/' + id,
    method: 'get'
  })
}

// 新增审核记录
export function addRecord(data) {
  return request({
    url: '/lms/audit/record',
    method: 'post',
    data: data
  })
}

// 修改审核记录
export function updateRecord(data) {
  return request({
    url: '/lms/audit/record',
    method: 'put',
    data: data
  })
}

// 删除审核记录
export function delRecord(id) {
  return request({
    url: '/lms/audit/record/' + id,
    method: 'delete'
  })
}
