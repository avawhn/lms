import request from "@/utils/request";

export function listProblem(query) {
    return request({
        url: "/hustoj/problem/list",
        method: "get",
        params: query,
    });
}



// 新增题目
export function addProblem(data) {
  return request({
      url: "/hustoj/problem",
      method: "post",
      data: data,
  });
}


export function getProblemById(id) {
    return request({
        url: "/hustoj/problem/" + id,
        method: "get",
    });
}
