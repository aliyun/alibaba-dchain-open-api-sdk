package com.alibaba.dchain.api.pop;

import com.alibaba.dchain.api.ClientConfig;

/**
 * @author 开帆
 * @date 2022/02/16
 */
public class PopClientConfig implements ClientConfig {
    /**
     * the endpoint
     */
    private String endpoint;
    /**
     * the regionId
     */
    private String regionId;
    /**
     * the accessKey Id
     */
    private String accessKeyId;
    /**
     * the accessKey Secret
     */
    private String accessKeySecret;

    public PopClientConfig() {
    }

    public PopClientConfig(String endpoint, String regionId, String accessKeyId, String accessKeySecret) {
        this.endpoint = endpoint;
        this.regionId = regionId;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }
}
