import request from "@/utils/request";

export function listSolution(query) {
    return request({
        url: "/hustoj/solution/list",
        method: "get",
        params: query,
    });
}

export function listSolutionRecord(query) {
    return request({
        url: "/hustoj/solution/record/list",
        method: "get",
        params: query,
    });
}

export function getSolutionBySolutionId(id) {
    return request({
        url: "/hustoj/solution/" + id,
        method: "get",
    });
}

export function getSolutionDetailBySolutionId(id) {
    return request({
        url: "/hustoj/solution/detail/" + id,
        method: "get",
    });
}

export function getPassRateStatisticsByUserId(id) {
    return request({
        url: "/hustoj/solution/passRate/statistics/" + id,
        method: "get",
    });
}




// 新增答案
export function addSolution(data) {
    return request({
        url: "/hustoj/solution",
        method: "post",
        data: data,
    });
}


export function testCode(data) {
    return request({
        url: "/hustoj/solution/test",
        method: "post",
        data: data,
    });
}