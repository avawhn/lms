import request from "@/utils/request";
import axios from "axios";

export function getUploadPreSignedUrl(bucket, filename) {
    const query = {
        bucket,
        filename,
    };
    return request({
        url: "/minio/pre/signed/url",
        method: "get",
        params: query,
    });
}

export function uploadFile(url, file) {
    return axios.put(url, file, {
        headers: {
            "Content-Type": file.type, // 设置请求头的 Content-Type，使用文件的 MIME 类型
        },
    });
}

export function deleteFile(bucket, filename) {
    return request({
        url: "/minio/obj",
        method: "delete",
        data: { bucket, filename },
    });
}

export function getPreviewUrl(bucket, filename) {
    return request({
        url: "/minio/obj/preview",
        method: "get",
        params: { bucket: bucket, obj: filename },
    });
}
