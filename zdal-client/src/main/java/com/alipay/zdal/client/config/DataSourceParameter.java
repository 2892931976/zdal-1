/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.config;

import java.util.Map;

import com.alipay.zdal.client.config.bean.PhysicalDataSourceBean;

/**
 * ����Դ������Ϣ�ĸ�������.
 * @author ����
 * @version $Id: DataSourceParameter.java, v 0.1 2012-11-17 ����4:06:30 Exp $
 */
public class DataSourceParameter {

    private String              jdbcUrl     = "";

    private String              userName    = "";

    private String              password    = "";

    /** ���ӳ��л����С������ */
    private int                 minConn;

    /** ���ӳ��л����������� */

    private int                 maxConn;

    private String              driverClass = "";

    private int                 blockingTimeoutMillis;

    private int                 idleTimeoutMinutes;

    private int                 preparedStatementCacheSize;

    private int                 queryTimeout;

    /** 1000,10 10����ͨ��1000��sql*/
    private String              sqlValve;

    /** 1000,10 10����ͨ��1000������*/
    private String              transactionValve;

    /** table1,100,60;table2,10,1  table1��60����ͨ��100��sql��table2��1����ͨ��1��sql */
    private String              tableValve;

    private boolean             prefill;

    private Map<String, String> connectionProperties;

    public Map<String, String> getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(Map<String, String> connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMinConn() {
        return minConn;
    }

    public void setMinConn(int minConn) {
        this.minConn = minConn;
    }

    public int getMaxConn() {
        return maxConn;
    }

    public void setMaxConn(int maxConn) {
        this.maxConn = maxConn;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public int getBlockingTimeoutMillis() {
        return blockingTimeoutMillis;
    }

    public void setBlockingTimeoutMillis(int blockingTimeoutMillis) {
        this.blockingTimeoutMillis = blockingTimeoutMillis;
    }

    public int getIdleTimeoutMinutes() {
        return idleTimeoutMinutes;
    }

    public void setIdleTimeoutMinutes(int idleTimeoutMinutes) {
        this.idleTimeoutMinutes = idleTimeoutMinutes;
    }

    public int getPreparedStatementCacheSize() {
        return preparedStatementCacheSize;
    }

    public void setPreparedStatementCacheSize(int preparedStatementCacheSize) {
        this.preparedStatementCacheSize = preparedStatementCacheSize;
    }

    public int getQueryTimeout() {
        return queryTimeout;
    }

    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    public String getSqlValve() {
        return sqlValve;
    }

    public void setSqlValve(String sqlValve) {
        this.sqlValve = sqlValve;
    }

    public String getTransactionValve() {
        return transactionValve;
    }

    public void setTransactionValve(String transactionValve) {
        this.transactionValve = transactionValve;
    }

    public String getTableValve() {
        return tableValve;
    }

    public void setTableValve(String tableValve) {
        this.tableValve = tableValve;
    }

    public static DataSourceParameter valueOf(PhysicalDataSourceBean bean) {
        DataSourceParameter paramter = new DataSourceParameter();
        paramter.setBlockingTimeoutMillis(bean.getBlockingTimeoutMillis());
        paramter.setDriverClass(bean.getDriverClass());
        paramter.setIdleTimeoutMinutes(bean.getIdleTimeoutMinutes());
        paramter.setJdbcUrl(bean.getJdbcUrl());
        paramter.setMaxConn(bean.getMaxConn());
        paramter.setMinConn(bean.getMinConn());
        paramter.setPassword(bean.getPassword());
        paramter.setPreparedStatementCacheSize(bean.getPreparedStatementCacheSize());
        paramter.setQueryTimeout(bean.getQueryTimeout());
        paramter.setUserName(bean.getUserName());
        paramter.setPrefill(bean.isPrefill());
        paramter.setConnectionProperties(bean.getConnectionProperties());
        return paramter;
    }

    public boolean getPrefill() {
        return prefill;
    }

    public void setPrefill(boolean prefill) {
        this.prefill = prefill;
    }
}
