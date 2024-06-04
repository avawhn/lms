import request from "@/utils/request";

// 查询奖项列表
export function listAward(query) {
    return request({
        url: "/lms/award/list",
        method: "get",
        params: query,
    });
}

export function getAllGrade() {
    return request({
        url: "/lms/award/grade/all",
        method: "get",
    });
}

// 查询奖项详细
export function getLatestAward() {
    return request({
        url: "/lms/award/latest",
        method: "get",
    });
}



// 查询奖项详细
export function getAward(id) {
    return request({
        url: "/lms/award/" + id,
        method: "get",
    });
}



// 查询奖项详细
export function getAwardDetail(id) {
    return request({
        url: "/lms/award/detail/" + id,
        method: "get",
    });
}

// 新增奖项
export function addAward(data) {
    return request({
        url: "/lms/award",
        method: "post",
        data: data,
    });
}

// 修改奖项
export function updateAward(data) {
    return request({
        url: "/lms/award",
        method: "put",
        data: data,
    });
}

// 删除奖项
export function delAward(id) {
    return request({
        url: "/lms/award/" + id,
        method: "delete",
    });
}
