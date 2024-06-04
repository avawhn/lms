import request from "@/utils/request";


export function listContest(query) {
    return request({
        url: "/hustoj/contest/list",
        method: "get",
        params: query,
    });
}


export function listUserContest(query) {
    return request({
        url: "/hustoj/contest/user/list",
        method: "get",
        params: query,
    });
}


export function getContestByContestId(id) {
    return request({
        url: "/hustoj/contest/" + id,
        method: "get",
    });
}


export function getContestDetailByContestId(id) {
    return request({
        url: "/hustoj/contest/detail/" + id,
        method: "get",
    });
}




// 新增测试
export function addContest(data) {
    return request({
        url: "/hustoj/contest",
        method: "post",
        data: data,
    });
}


export function submit(data) {
    return request({
        url: "/hustoj/contest/submit",
        method: "post",
        data: data,
    });
}


// 删除测试
export function delContest(ids) {
  return request({
      url: "/hustoj/contest/" + ids,
      method: "delete",
  });
}