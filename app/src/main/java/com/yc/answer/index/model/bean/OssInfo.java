package com.yc.answer.index.model.bean;

/**
 * Created by wanglin  on 2018/4/23 16:04.
 * 阿里云相关参数
 */

public class OssInfo {

    /**
     * status : 200
     * AccessKeyId : STS.FWFUBQtUaYefzJvBJcppA296r
     * AccessKeySecret : 9uVkDNvXTuAkqc2YyVtaHRnSws7npn7hhSmNmL51CdZ7
     * Expiration : 2018-04-23T07:41:16Z
     * SecurityToken : CAISnAN1q6Ft5B2yfSjIp5TzHvjlmYpA7qeNeGzHpko2fP9t3fydkDz2IHxLfXFgBu8cv/k1mm5Q6P0ZlqJ4T55IQ1Dza8J148yNJKdCpM6T1fau5Jko1bctcAr6UmxJta2/SuH9S8ynkJ7PD3nPii50x5bjaDymRCbLGJaViJlhHNZ1Ow6jdmhpCctxLAlvo9N4UHzKLqSVLwLNiGjdB1YKwg1nkjFT5KCy3sC74BjTh0GYr+gOvNbVI4O4V8B2IIwdI9Cux75ffK3bzAtN7wRL7K5skJFc/TDOsrP6BEJKsTGHKPbz+N9iJxNiHIxYfZRJt//hj/Z1l/XOnoDssXZ3MPpSTj7USfL4orTNE/j7Mc0iJ/SpeSbLy47KKJj5vgRjIxBmPQhRKd06MS0yW15+TCiEb7Gm9F3NQH/6Evbcj/FtjscrkgSzpYfQHTXVHeXFixR/E4QnckYlOyQR2WHcaaIce2ROCQg7WO/NHdosNkED8/225VOJDTcTx3VWruD4YOjNpqccZIPwU5RLy4MBY45ctG8nX6Aw2zprdqd+GoABfZ6fI6d6eRWEyIPKXzOy9DgflG2+Ig6JCdUKzbxUnCJBAjJoYrSMitApIarzPr+PX+2Sqdb3R5qn+d94NVFAjSRyBGybuF68AkQbv4BDA7oAK7K5rUQiZxzu0QwcOGsSW+TpVIfqpNa6nwdqmgBvHB0bllgSyR/TxoTi6iR7OTY=
     */

    private int status;
    private String AccessKeyId;
    private String AccessKeySecret;
    private String Expiration;
    private String SecurityToken;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAccessKeyId() {
        return AccessKeyId;
    }

    public void setAccessKeyId(String AccessKeyId) {
        this.AccessKeyId = AccessKeyId;
    }

    public String getAccessKeySecret() {
        return AccessKeySecret;
    }

    public void setAccessKeySecret(String AccessKeySecret) {
        this.AccessKeySecret = AccessKeySecret;
    }

    public String getExpiration() {
        return Expiration;
    }

    public void setExpiration(String Expiration) {
        this.Expiration = Expiration;
    }

    public String getSecurityToken() {
        return SecurityToken;
    }

    public void setSecurityToken(String SecurityToken) {
        this.SecurityToken = SecurityToken;
    }
}
