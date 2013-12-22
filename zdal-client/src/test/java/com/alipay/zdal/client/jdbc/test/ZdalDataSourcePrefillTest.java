/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc.test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.alipay.zdal.client.ThreadLocalString;
import com.alipay.zdal.client.jdbc.ZdalDataSource;
import com.alipay.zdal.client.jdbc.ZdalPrefill;
import com.alipay.zdal.client.util.ThreadLocalMap;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: XtsZdalDataSourceTest.java, v 0.1 2012-9-5 ����04:29:55 xiaoqing.zhouxq Exp $
 */
public class ZdalDataSourcePrefillTest {
    /**ȫ�������� */
    public static final String       taskMutexTable            = "timeout_task_mutex";

    /**ģ�⽻�׺�ǰ׺ */
    public static final String       fakeTradeNoPref           = "20101103123456";

    /** ����10��50������� */
    private static final String      TRADE_DATE                = "20140101";

    /**ģ�����⽻�׺�(seq��Χ[0,799999]) */
    private static final String      FAKE_MAIN_TRADENO_SEQ     = "588888";

    /**ģ��failover�⽻��(8seq��Χ[800000,999999]) */
    private static final String      FAKE_FAILOVER_TRADENO_SEQ = "888888";

    /** ���׺�β�ַ� */
    private static final String      TRADE_NO_END              = "0";

    /** ��������(50��)������failover��(10��)��Ӧ�����⽻�׺ţ���60���� */
    public static final List<String> fakeTradeNo4Trade         = new ArrayList<String>(60);

    /**·�ɵ����ױ��ģ�⽻�׺� */
    static {

        //��������Ľ��׺�
        for (int i = 0, j = 0; i < 50; i++) {
            j = i * 2;
            fakeTradeNo4Trade.add(TRADE_DATE + FAKE_MAIN_TRADENO_SEQ
                                  + (j < 10 ? TRADE_NO_END + j : j));
        }

        //����failover��Ľ��׺�
        for (int i = 0; i < 10; i++) {
            fakeTradeNo4Trade.add(TRADE_DATE + FAKE_FAILOVER_TRADENO_SEQ + i + TRADE_NO_END);
        }
    };

    public static class TradeCoreZdalPrefill implements ZdalPrefill {

        private ZdalDataSource dataSource = null;
        private String         sql        = "select PLATFORM,CHANNEL_PARTNER,GOODS_TYPE,GOODS_SUB_TYPE,BUYER_MARKER_MEMO,SELLER_MARKER_MEMO,CHANNEL,"
                                            + "PRODUCT,PAY_CHANNEL,OTHER_SERVICE_FEE,RELATION_PRO,ID,TRADE_NO,OUT_TRADE_NO,SELLER_ACCOUNT,"
                                            + "SELLER_LOGIN_EMAIL,BUYER_ACCOUNT,BUYER_LOGIN_EMAIL,SELLER_TYPE,BUYER_TYPE,TRADE_FROM,TRADE_EMAIL,"
                                            + "OPERATOR_ROLE,TRADE_STATUS,TOTAL_FEE,SERVICE_FEE_RATIO,SERVICE_FEE,CURRENCY,SELLER_ACTION,"
                                            + "BUYER_ACTION,GMT_CREATE,SELLER_USER_ID,BUYER_USER_ID,ADDITIONAL_TRD_STATUS,TRADE_TYPE,"
                                            + "SELLER_FULLNAME,BUYER_FULLNAME,SELLER_NICK,BUYER_NICK,GOODS_TITLE,GMT_LAST_MODIFIED_DT,STOP_TIMEOUT,"
                                            + "GATHERING_TYPE,BUYER_MARKER,SELLER_MARKER "
                                            + "from trade_base a where (trade_no = ?) "
                                            + "and gmt_create < to_date(substr(?,1,8),'yyyyMMdd')+2 "
                                            + "and gmt_create > to_date(substr(?,1,8),'yyyyMMdd')-1";

        public void prefill() {
            for (String tradeNo : fakeTradeNo4Trade) {
                // �Ƿ����쳣
                boolean hasError = false;

                long startTime = System.currentTimeMillis();
                Connection conn = null;
                PreparedStatement pst = null;
                try {
                    conn = dataSource.getConnection();
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, tradeNo);
                    pst.setString(2, tradeNo);
                    pst.setString(3, tradeNo);
                    pst.executeQuery();
                } catch (Throwable ex) {
                    hasError = true;
                    System.out.println("��ʼ��ϵͳ��Դʱ��Ԥ��sql��ѯ�����쳣,tradeNo=" + tradeNo);
                } finally {

                    long elapseTime = System.currentTimeMillis() - startTime;

                    System.out.println("[(INIT_DS," + getDsName() + (hasError ? ",N," : ",Y,")
                                       + elapseTime + "ms)]");
                    try {
                        if (pst != null) {
                            pst.close();
                        }
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //InitializingBean���ƽ������ݳ�ʼ�����ܷ�����TradeDalAccessInterceptor��ʼ��֮ǰ��
                    //�ʳ���DB�쳣�ŵ�threadLocal�еĿ��ܲ��ᱻ������˴���������߳��е�condition����ֹ������ѯʹ�ò������Լ���condition
                    //                    TddlHelper.clearTddlCondition();
                }
            }
        }

        private String getDsName() {
            Map<String, DataSource> mapDs = (Map<String, DataSource>) ThreadLocalMap
                .get(ThreadLocalString.GET_ID_AND_DATABASE);

            if (mapDs != null) {
                for (Map.Entry<String, DataSource> entry : mapDs.entrySet()) {

                    return entry.getKey();
                }
            }
            return null;
        }

        public ZdalDataSource getDataSource() {
            return dataSource;
        }

        public void setDataSource(ZdalDataSource dataSource) {
            this.dataSource = dataSource;
        }

    }

    /**
     * 
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        String appName = "tradecore";
        String appDsName = "tradecore50";
        String dbmode = "dev";
        String idcName = "gz00";
        String zdataconsoleUrl = "http://zdataconsole.stable.alipay.net:8080";
        String configPath = "./config";
        ZdalDataSource dataSource = new ZdalDataSource();
        dataSource.setAppName(appName);
        dataSource.setAppDsName(appDsName);
        dataSource.setDbmode(dbmode);
        dataSource.setZone(idcName);

        dataSource.setZdataconsoleUrl(zdataconsoleUrl);
        dataSource.setConfigPath(configPath);
        dataSource.init();

        TradeCoreZdalPrefill prefill = new TradeCoreZdalPrefill();
        prefill.setDataSource(dataSource);

        prefill.getDataSource().prefillZdal(prefill);

        //        Thread.sleep(1000L);
        //
        //        for (int i = 0; i < 1; i++) {
        //            new ThreadTask(dataSource).start();
        //        }

    }

    private static class ThreadTask extends Thread {
        private DataSource dataSource;

        public ThreadTask(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        public void run() {
            for (int i = 0; i < 1; i++) {
                Connection conn = null;
                PreparedStatement pst1 = null;
                ResultSet rs = null;
                try {
                    conn = dataSource.getConnection();
                    pst1 = conn
                        .prepareStatement("select *  from cs_action_log where USER_ID=? AND  ACTION_DT > ? and ACTION_DT < ? order by action_dt limit ?,?");
                    pst1.setString(1, "2088102118589070");

                    pst1.setDate(2, Date.valueOf("2012-01-01"));
                    pst1.setDate(3, Date.valueOf("2015-01-01"));
                    pst1.setInt(4, 0);
                    pst1.setInt(5, 100);
                    rs = pst1.executeQuery();
                    int j = 0;
                    while (rs.next()) {
                        System.out.println(rs.getString(1) + "----" + j);
                        j++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (rs != null) {
                            rs.close();
                        }
                        if (pst1 != null) {
                            pst1.close();
                        }
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
