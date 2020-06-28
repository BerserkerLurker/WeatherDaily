package com.onadasoft.weatherdaily.event;

public class DataSyncEvent {
    private String syncStatusMsg;
    private int syncStatusCode;

    public DataSyncEvent(String syncStatusMsg, int syncStatusCode) {
        this.syncStatusMsg = syncStatusMsg;
        this.syncStatusCode = syncStatusCode;
    }

    public String getSyncStatusMsg() {
        return syncStatusMsg;
    }

    public int getSyncStatusCode() {
        return syncStatusCode;
    }
}
