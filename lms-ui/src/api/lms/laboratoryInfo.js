import request from "@/utils/request";


export function getLaboratoryInfo() {
    return request({
        url: "/lms/laboratory/info",
        method: "get",
    });
}

export function addLaboratoryImage(data) {
    return request({
        url: "/lms/laboratory/info",
        method: "post",
        data: data,
    });
}


export function updateLaboratoryInfo(data) {
    return request({
        url: "/lms/laboratory/info",
        method: "put",
        data: data,
    });
}


export function delLaboratoryInfo(ids) {
    return request({
        url: "/lms/laboratory/info/" + ids,
        method: "delete",
    });
}
