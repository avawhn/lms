import request from '@/utils/request'

// 查询物料变更列表
export function listMaterialRecord(query) {
  return request({
    url: '/lms/material/record/list',
    method: 'get',
    params: query
  })
}


export function listAllYear() {
    return request({
        url: "/lms/material/record/year",
        method: "get",
    });
}

// 查询物料变更详细
export function getMaterialRecord(id) {
    return request({
        url: "/lms/material/record/" + id,
        method: "get",
    });
}

// 新增物料变更
export function addMaterialRecord(data) {
    return request({
        url: "/lms/material/record",
        method: "post",
        data: data,
    });
}




export function statisticsYear(data) {
    return request({
        url: "/lms/material/record/statistics/year",
        method: "post",
        data: data,
    });
}




// 修改物料变更
export function updateMaterialRecord(data) {
    return request({
        url: "/lms/material/record",
        method: "put",
        data: data,
    });
}

// 删除物料变更
export function delMaterialRecord(id) {
    return request({
        url: "/lms/material/record/" + id,
        method: "delete",
    });
}
