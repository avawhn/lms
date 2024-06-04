import request from "@/utils/request";



export function getSourceCodeBySolutionId(id) {
    return request({
        url: "/hustoj/source/" + id,
        method: "get",
    });
}