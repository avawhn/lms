package com.lms.common.utils.minio;

import io.minio.messages.Item;
import io.minio.messages.Owner;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * @author whn
 */
@Data
public class MinioObject implements Serializable {
    private static final long serialVersionUID = -3011956881101780276L;

    /* except DeleteMarker */
    private String etag;

    private String objectName;

    private ZonedDateTime lastModified;

    private Owner owner;

    /* except DeleteMarker */
    private long size;

    /* except DeleteMarker, not in case of MinIO server. */
    private String storageClass;

    /* except ListObjects V1 */
    private boolean isLatest;

    /* except ListObjects V1 */
    private String versionId;

    private Map<String, String> userMetadata;

    private boolean isDir = false;

    private boolean isDeleteMarker = false;


    public MinioObject(Item item) {
        this.etag = item.etag();
        this.isDir = item.isDir();
        this.objectName = item.objectName();
        this.owner = item.owner();
        this.size = item.size();
        this.isDeleteMarker = item.isDeleteMarker();
        this.isLatest = item.isLatest();
        this.lastModified = item.lastModified();
        this.userMetadata = item.userMetadata();
        this.versionId = item.versionId();
        this.storageClass = item.storageClass();
    }
}
