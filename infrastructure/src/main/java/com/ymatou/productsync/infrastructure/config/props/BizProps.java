package com.ymatou.productsync.infrastructure.config.props;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import org.springframework.stereotype.Component;

/**
 * Created by zhangyifan on 2016/12/21.
 */
@Component
@DisconfFile(fileName = "biz.properties")
public class BizProps {

    private int readCount;

    private String sentFailedCron;
    private String syncFailedCron;
    private String syncTimeoutCron;
    private String syncCustomCron;
    private String filingSyncCmdCron;
    private String syncCheckCron;

    private int sentFailedSize;
    private int syncFailedSize;
    private int syncTimeoutSize;
    private int syncCustomSize;
    private int filingSyncCmdSize;
    private int syncCheckSize;

    private int sentFailedThreshold;
    private int syncTimeoutThreshold;
    private int filingSyncThreshold;

    private int lockerThreshold;

    private Boolean filingSyncHistory;

    @DisconfFileItem(name = "readCount")
    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }


    @DisconfFileItem(name = "sentFailedCron")
    public String getSentFailedCron() {
        return sentFailedCron;
    }

    public void setSentFailedCron(String sentFailedCron) {
        this.sentFailedCron = sentFailedCron;
    }

    @DisconfFileItem(name = "syncFailedCron")
    public String getSyncFailedCron() {
        return syncFailedCron;
    }

    public void setSyncFailedCron(String syncFailedCron) {
        this.syncFailedCron = syncFailedCron;
    }

    @DisconfFileItem(name = "syncTimeoutCron")
    public String getSyncTimeoutCron() {
        return syncTimeoutCron;
    }

    public void setSyncTimeoutCron(String syncTimeoutCron) {
        this.syncTimeoutCron = syncTimeoutCron;
    }

    @DisconfFileItem(name = "syncCustomCron")
    public String getSyncCustomCron() {
        return syncCustomCron;
    }

    public void setSyncCustomCron(String syncCustomCron) {
        this.syncCustomCron = syncCustomCron;
    }

    @DisconfFileItem(name = "filingSyncCmdCron")
    public String getFilingSyncCmdCron() {
        return filingSyncCmdCron;
    }

    public void setFilingSyncCmdCron(String filingSyncCmdCron) {
        this.filingSyncCmdCron = filingSyncCmdCron;
    }

    @DisconfFileItem(name = "syncCheckCron")
    public String getSyncCheckCron() {
        return syncCheckCron;
    }

    public void setSyncCheckCron(String syncCheckCron) {
        this.syncCheckCron = syncCheckCron;
    }

    @DisconfFileItem(name = "sentFailedSize")
    public int getSentFailedSize() {
        return sentFailedSize;
    }

    public void setSentFailedSize(int sentFailedSize) {
        this.sentFailedSize = sentFailedSize;
    }

    @DisconfFileItem(name = "syncFailedSize")
    public int getSyncFailedSize() {
        return syncFailedSize;
    }

    public void setSyncFailedSize(int syncFailedSize) {
        this.syncFailedSize = syncFailedSize;
    }

    @DisconfFileItem(name = "syncTimeoutSize")
    public int getSyncTimeoutSize() {
        return syncTimeoutSize;
    }

    public void setSyncTimeoutSize(int syncTimeoutSize) {
        this.syncTimeoutSize = syncTimeoutSize;
    }

    @DisconfFileItem(name = "syncCustomSize")
    public int getSyncCustomSize() {
        return syncCustomSize;
    }

    public void setSyncCustomSize(int syncCustomSize) {
        this.syncCustomSize = syncCustomSize;
    }

    @DisconfFileItem(name = "filingSyncCmdSize")
    public int getFilingSyncCmdSize() {
        return filingSyncCmdSize;
    }

    public void setFilingSyncCmdSize(int filingSyncCmdSize) {
        this.filingSyncCmdSize = filingSyncCmdSize;
    }

    @DisconfFileItem(name = "syncCheckSize")
    public int getSyncCheckSize() {
        return syncCheckSize;
    }

    public void setSyncCheckSize(int syncCheckSize) {
        this.syncCheckSize = syncCheckSize;
    }

    @DisconfFileItem(name = "sentFailedThreshold")
    public int getSentFailedThreshold() {
        return sentFailedThreshold;
    }

    public void setSentFailedThreshold(int sentFailedThreshold) {
        this.sentFailedThreshold = sentFailedThreshold;
    }

    @DisconfFileItem(name = "syncTimeoutThreshold")
    public int getSyncTimeoutThreshold() {
        return syncTimeoutThreshold;
    }

    public void setSyncTimeoutThreshold(int syncTimeoutThreshold) {
        this.syncTimeoutThreshold = syncTimeoutThreshold;
    }

    @DisconfFileItem(name = "filingSyncThreshold")
    public int getFilingSyncThreshold() {
        return filingSyncThreshold;
    }

    public void setFilingSyncThreshold(int filingSyncThreshold) {
        this.filingSyncThreshold = filingSyncThreshold;
    }

    @DisconfFileItem(name = "filingSyncHistory")
    public Boolean getFilingSyncHistory() {
        return filingSyncHistory;
    }

    public void setFilingSyncHistory(Boolean filingSyncHistory) {
        this.filingSyncHistory = filingSyncHistory;
    }


    @DisconfFileItem(name = "lockerThreshold")
    public int getLockerThreshold() {
        return lockerThreshold;
    }

    public void setLockerThreshold(int lockerThreshold) {
        this.lockerThreshold = lockerThreshold;
    }
}
