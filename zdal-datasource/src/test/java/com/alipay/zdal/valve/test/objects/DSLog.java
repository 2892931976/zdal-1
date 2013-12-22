package com.alipay.zdal.valve.test.objects;

public class DSLog {
    private String timeString;
    private String dsString;
    private String minSize;
    private String MaxSize;
    private String availableConnections;
    private String connectionCount;
    private String connectionInUseCount;
    private String maxConnectionsInUseCount;
    private String ConnectionCreatedCount;
    private String ConnectionDestroyedCount;
    private String hostName;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public String getDsString() {
        return dsString;
    }

    public void setDsString(String dsString) {
        this.dsString = dsString;
    }

    /**
     * ��������Դ��־��¼<br>
     * 2012-04-17 11:28:54;ʱ��<br>DefaultDS;����Դ����<br>5;��С������<br>20;���������<br>
     * 20;����������<br>5;��������<br>0;����ʹ�õ�������<br>1;��ʷ��������ֵ<br>
     * 5;�������ۼ�ֵ<br>0;���ٵ���������<br>BJG-AP53289��������<br>
     * @param log
     */
    public DSLog(String log) {
        String[] centStrings = log.split(";");
        timeString = centStrings[0];
        dsString = centStrings[1];
        minSize = centStrings[2];
        MaxSize = centStrings[3];
        availableConnections = centStrings[4];
        connectionCount = centStrings[5];
        connectionInUseCount = centStrings[6];
        maxConnectionsInUseCount = centStrings[7];
        ConnectionCreatedCount = centStrings[8];
        ConnectionDestroyedCount = centStrings[9];
        hostName = centStrings[10];
    }

    public String getMinSize() {
        return minSize;
    }

    public void setMinSize(String minSize) {
        this.minSize = minSize;
    }

    public String getMaxSize() {
        return MaxSize;
    }

    public void setMaxSize(String maxSize) {
        MaxSize = maxSize;
    }

    /**
     * ��ǰ����������
     * @return ��ǰ����������
     */
    public String getAvailableConnections() {
        return availableConnections;
    }

    public void setAvailableConnections(String availableConnections) {
        this.availableConnections = availableConnections;
    }

    public String getConnectionCount() {
        return connectionCount;
    }

    public void setConnectionCount(String connectionCount) {
        this.connectionCount = connectionCount;
    }

    public String getConnectionInUseCount() {
        return connectionInUseCount;
    }

    public void setConnectionInUseCount(String connectionInUseCount) {
        this.connectionInUseCount = connectionInUseCount;
    }

    public String getMaxConnectionsInUseCount() {
        return maxConnectionsInUseCount;
    }

    public void setMaxConnectionsInUseCount(String maxConnectionsInUseCount) {
        this.maxConnectionsInUseCount = maxConnectionsInUseCount;
    }

    public String getConnectionCreatedCount() {
        return ConnectionCreatedCount;
    }

    public void setConnectionCreatedCount(String connectionCreatedCount) {
        ConnectionCreatedCount = connectionCreatedCount;
    }

    public String getConnectionDestroyedCount() {
        return ConnectionDestroyedCount;
    }

    public void setConnectionDestroyedCount(String connectionDestroyedCount) {
        ConnectionDestroyedCount = connectionDestroyedCount;
    }

    @Override
    public String toString() {
        return "DSLog [timeString=" + timeString + ", dsString=" + dsString + ", minSize="
               + minSize + ", MaxSize=" + MaxSize + ", availableConnections="
               + availableConnections + ", connectionCount=" + connectionCount
               + ", connectionInUseCount=" + connectionInUseCount + ", maxConnectionsInUseCount="
               + maxConnectionsInUseCount + ", ConnectionCreatedCount=" + ConnectionCreatedCount
               + ", ConnectionDestroyedCount=" + ConnectionDestroyedCount + "]";
    }

    public static void main(String[] args) {
    }
}
