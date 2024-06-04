import request from "@/utils/request";

// 查询比赛视频列表
export function listVideo(query) {
    return request({
        url: "/lms/competition/video/list",
        method: "get",
        params: query,
    });
}

export function getVideoListByGrade(grade) {
    return request({
        url: "/lms/competition/video/grade/" + grade,
        method: "get",
    });
}


// 查询比赛视频详细
export function getVideo(id) {
    return request({
        url: "/lms/competition/video/" + id,
        method: "get",
    });
}

// 新增比赛视频
export function addVideo(data) {
    return request({
        url: "/lms/competition/video",
        method: "post",
        data: data,
    });
}

// 修改比赛视频
export function updateVideo(data) {
    return request({
        url: "/lms/competition/video",
        method: "put",
        data: data,
    });
}

// 删除比赛视频
export function delVideo(id) {
    return request({
        url: "/lms/competition/video/" + id,
        method: "delete",
    });
}
