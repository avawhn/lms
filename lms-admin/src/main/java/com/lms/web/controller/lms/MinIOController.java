package com.lms.web.controller.lms;

import com.lms.common.core.domain.R;
import com.lms.common.utils.minio.MinioObject;
import com.lms.common.utils.minio.MinioUtils;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author whn
 */
@RestController
@RequestMapping("/minio")
public class MinIOController {
    @Resource
    private MinioUtils minioUtils;


    @Value("${minio.duration.get}")
    private int getMethodDuration;

    @Value("${minio.duration.put}")
    private int putMethodDuration;

    @GetMapping("/pre/signed/url")
    public R<String> getPutPreSignedUrl(@RequestParam("bucket") String bucket,
                                        @RequestParam("filename") String filename) {
        String url = minioUtils
                .generatePresignedObjectUrl(
                        Method.PUT,
                        bucket, filename,
                        putMethodDuration, TimeUnit.SECONDS,
                        null
                );
        return R.ok(url);
    }

    @GetMapping("/bucket")
    public R<List<Bucket>> getBucket() {
        return R.ok(minioUtils.listBuckets());
    }

    @PostMapping("/bucket/create")
    public R<String> createBucket(String bucket) {
        minioUtils.createBucket(bucket);
        return R.ok();
    }

    @GetMapping("/bucket/exists/{bucket}")
    public R<Boolean> bucketExists(@PathVariable("bucket") String bucket) {
        return R.ok(minioUtils.bucketExists(bucket));
    }

    // @GetMapping("/obj/{bucket}/{obj}")
    // public R<String> getObject(@PathVariable("bucket") String bucket,
    //                                 @PathVariable("obj") String obj) {
    //     return R.ok(minioUtils.getObject(bucket, obj));
    // }

    @GetMapping("/obj/list/{bucket}")
    public R<List<MinioObject>> listObjects(@PathVariable("bucket") String bucket) {
        return R.ok(minioUtils.listObjects(bucket, false));
    }

    @GetMapping("/obj/list/recursive/{bucket}")
    public R<List<MinioObject>> listObjectRecursive(@PathVariable("bucket") String bucket) {
        return R.ok(minioUtils.listObjects(bucket, true));
    }

    @GetMapping("/obj/list/recursive")
    public R<List<MinioObject>> listObjectsRecursiveLimit(String bucket, String start, int max) {
        return R.ok(minioUtils.listObjects(bucket, true, start, max));
    }

    @PostMapping("/obj/download")
    public R<String> downloadObject(@RequestBody HashMap<String, String> param) {
        minioUtils.downloadObject(
                param.get("bucket"),
                param.get("obj"),
                param.get("filename")
        );

        return R.ok();
    }

    @GetMapping("/obj/preview/{bucket}/{obj}")
    public R<String> getPreviewUrl(@PathVariable("bucket") String bucket,
                                   @PathVariable("obj") String obj) {
        return R.ok(minioUtils
                .generatePresignedObjectUrl(Method.GET,
                        bucket, obj,
                        getMethodDuration, TimeUnit.SECONDS,
                        null
                )
        );
    }

    @DeleteMapping("/obj/{bucket}/{obj}")
    public R<String> removeObject(@PathVariable("bucket") String bucket,
                                  @PathVariable("obj") String obj) {
        minioUtils.removeObject(bucket, obj);
        return R.ok();
    }

    @DeleteMapping("/obj")
    public R<String> removeObjects() {
        return R.ok();
    }

    @PutMapping("/obj")
    public R<String> putObject(MultipartFile file, String bucket) {
        InputStream stream = null;
        try {
            stream = file.getInputStream();
            minioUtils.putObject(bucket, "1.jpeg", stream, stream.available(), 5 * 1024 * 1024);
        } catch (IOException e) {
            return R.fail("失败");
        }
        return R.ok();
    }
}
