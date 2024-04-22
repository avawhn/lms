package com.lms.common.utils.minio;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * MinIO 工具类
 *
 * @author whn
 */
@Component
public class MinioUtils {

    /* MinIO 客户端 */
    private final MinioClient minioClient;


    public MinioUtils(MinioConfig config) {
        minioClient =
                MinioClient.builder()
                        .endpoint(config.getEndpoint())
                        .credentials(config.getAccessKey(), config.getSecretKey())
                        .build();
    }


    /* ################################### bucket ################################### */
    @SneakyThrows
    public boolean bucketExists(String bucket) {
        return minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(bucket)
                        .build()
        );
    }

    @SneakyThrows
    public void createBucket(String bucket) {
        boolean exists = this.bucketExists(bucket);

        if (exists) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucket)
                            .build()
            );
        }
    }

    @SneakyThrows
    public List<Bucket> listBuckets(Map<String, String> queryParams) {
        ListBucketsArgs args = ListBucketsArgs.builder()
                .extraQueryParams(queryParams)
                .build();
        return minioClient.listBuckets(args);
    }

    @SneakyThrows
    public List<Bucket> listBuckets() {
        return minioClient.listBuckets();
    }


    /* ################################### object ################################### */

    @SneakyThrows
    protected List<MinioObject> convertResult(Iterable<Result<Item>> results) {
        List<MinioObject> list = new ArrayList<>();

        for (Result<Item> r : results) {
            MinioObject obj = new MinioObject(r.get());
            // 去掉 etag 中的双引号
            String etag = obj.getEtag();
            obj.setEtag(etag.substring(1, etag.length() - 1));
            list.add(obj);
        }

        return list;
    }

    @SneakyThrows
    public List<MinioObject> listObjects(String bucket, boolean recursive) {
        ListObjectsArgs args = ListObjectsArgs.builder()
                .bucket(bucket)
                .recursive(recursive)
                .build();
        return this.convertResult(minioClient.listObjects(args));
    }


    @SneakyThrows
    public List<MinioObject> listObjects(String bucket, boolean recursive, String startAfter, int maxKeys) {
        ListObjectsArgs args = ListObjectsArgs.builder()
                .bucket(bucket)
                .recursive(recursive)
                .startAfter(startAfter)
                .maxKeys(maxKeys)
                .build();
        return this.convertResult(minioClient.listObjects(args));
    }


    @SneakyThrows
    public void putObject(String bucket, String objName, InputStream stream, long objectSize, long partSize) {
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucket)
                .object(objName)
                .stream(stream, objectSize, partSize)
                .build();
        minioClient.putObject(args);
    }

    @SneakyThrows
    public void downloadObject(String bucket, String object, String filename) {
        DownloadObjectArgs args = DownloadObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .filename(filename)
                .build();
        minioClient.downloadObject(args);
    }


    @SneakyThrows
    public void removeObject(String bucket, String object) {
        RemoveObjectArgs args = RemoveObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .build();
        minioClient.removeObject(args);
    }


    public Iterable<Result<DeleteError>> removeObjects(String bucket, List<String> objects) {
        List<DeleteObject> objs = new LinkedList<>();
        for (String name : objects) {
            objs.add(new DeleteObject(name));
        }
        RemoveObjectsArgs args = RemoveObjectsArgs.builder()
                .objects(objs)
                .bucket(bucket)
                .build();
        return minioClient.removeObjects(args);
    }


    @SneakyThrows
    public String generatePresignedObjectUrl(Method method,
                                             String bucket, String object,
                                             int duration, TimeUnit unit,
                                             Map<String, String> reqParams) {
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .method(method)
                .bucket(bucket)
                .object(object)
                .expiry(duration, unit)
                .extraQueryParams(reqParams)
                .build();


        return minioClient.getPresignedObjectUrl(args);
    }

    // @SneakyThrows
    // public String getObject(String bucket, String object) {
    //     GetObjectArgs args = GetObjectArgs.builder()
    //             .bucket(bucket)
    //             .object(object)
    //             .build();
    //     GetObjectResponse resp = minioClient.getObject(args);
    //
    //     return "";
    // }
}