package com.medmeeting.m.zhiyi.UI.Entity;

public class LiveStream {

    /**
     * id : 187
     * programId : 100154
     * streamName : yihuibao-test_20170720134648869_100154
     * streamVoerTime : 1500533208000
     * pushUrl : rtmp://pili-publish.medmeeting.com/yihuibao-test/yihuibao-test_20170720134648869_100154?e=1500533208&token=r0_GHWBaOY4cbuUfQHAOsj0KoGAo_648Xc1SYCfe:xsc8dXiNEuZvbU28xu9RJpjczDQ=
     * rtmpPlayUrl : rtmp://pili-live-rtmp.medmeeting.com/yihuibao-test/yihuibao-test_20170720134648869_100154
     * hlsPlayUrl : http://pili-live-hls.medmeeting.com/yihuibao-test/yihuibao-test_20170720134648869_100154.m3u8
     * status : A
     * createTime : 1500529608000
     * firstUseTime : null
     * lastUseTime : null
     */

    private int id;
    private int programId;
    private String streamName;
    private long streamVoerTime;
    private String pushUrl;
    private String rtmpPlayUrl;
    private String hlsPlayUrl;
    private String status;
    private long createTime;
    private Object firstUseTime;
    private Object lastUseTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public long getStreamVoerTime() {
        return streamVoerTime;
    }

    public void setStreamVoerTime(long streamVoerTime) {
        this.streamVoerTime = streamVoerTime;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }

    public String getRtmpPlayUrl() {
        return rtmpPlayUrl;
    }

    public void setRtmpPlayUrl(String rtmpPlayUrl) {
        this.rtmpPlayUrl = rtmpPlayUrl;
    }

    public String getHlsPlayUrl() {
        return hlsPlayUrl;
    }

    public void setHlsPlayUrl(String hlsPlayUrl) {
        this.hlsPlayUrl = hlsPlayUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Object getFirstUseTime() {
        return firstUseTime;
    }

    public void setFirstUseTime(Object firstUseTime) {
        this.firstUseTime = firstUseTime;
    }

    public Object getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(Object lastUseTime) {
        this.lastUseTime = lastUseTime;
    }
}
