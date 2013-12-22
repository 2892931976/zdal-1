/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.config;

/**
 * Tair����Դ��������.
 * @author ����
 * @version $Id: TairDataSourceParameter.java, v 0.1 2013-1-13 ����10:55:34 Exp $
 */
public class TairDataSourceParameter {
    /** Tair��configserver��masterUrl */
    private String masterUrl;

    /** Tair��configServer��slaveUrl */
    private String slaveUrl;

    /** Tair��groupName */
    private String groupName;

    public String getMasterUrl() {
        return masterUrl;
    }

    public void setMasterUrl(String masterUrl) {
        this.masterUrl = masterUrl;
    }

    public String getSlaveUrl() {
        return slaveUrl;
    }

    public void setSlaveUrl(String slaveUrl) {
        this.slaveUrl = slaveUrl;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
