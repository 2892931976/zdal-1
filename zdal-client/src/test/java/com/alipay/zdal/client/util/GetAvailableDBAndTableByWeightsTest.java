/**
 * 
 */
package com.alipay.zdal.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alipay.zdal.client.jdbc.ZdalDataSource;
import com.alipay.zdal.client.util.dispatchanalyzer.ZdalDatasourceIntrospector;

/**�ع鲻�����ȫ����ԣ���������������������db��tb�ĺ�
 * @author xiaoju.luo
 * @version $Id: DBAndTableByWeights.java,v 0.1 2012-5-14 ����04:07:46 xiaoju.luo
 *          Exp $
 */
public class GetAvailableDBAndTableByWeightsTest {
    private static Log                             logger           = LogFactory
                                                                        .getLog("GetAvailableDBAndTableByWeightsTest.class");
    private static final String                    APPNAME          = "allavailable";
    private static final String                    APPDSNAME        = "test";
    private static final String                    DBMODE           = "dev";
    private static final String                    IDCNAME          = "gzone";
    private static final String                    ZDATACONSOLE_URL = "http://zdataconsole.stable.alipay.net:8080";
    private static final String                    CONFIG_PATH      = "./config";

    private static final Map<String, String>       keyWeightConfig  = new HashMap<String, String>();
    static {
        keyWeightConfig.put("group_0", "master_0:10,master_2:10");
        keyWeightConfig.put("group_1", "master_1:10,master_3:10");
    }

    private static final Map<String, List<String>> shardingRules    = new HashMap<String, List<String>>();
    static {
        List<String> rules = new ArrayList<String>();
        rules.add("Integer.valueOf(#card_no#.substring(9,10),10)%2;");
        shardingRules.put("card_no_month", rules);
    }

    private static final Map<String, String>       tbNumForEachDb   = new HashMap<String, String>();
    static {
        tbNumForEachDb.put("card_no_month", "24");
    }

    private ZdalDataSource                         dataSource       = null;
    JdbcTemplate                                   tddlJT;
    ZdalDatasourceIntrospector                        td;
    String                                         tableName;

    @Before
    public void setUp() {
        dataSource = new ZdalDataSource();
        dataSource.setAppName(APPNAME);
        dataSource.setAppDsName(APPDSNAME);
        dataSource.setDbmode(DBMODE);
        dataSource.setZone(IDCNAME);
        dataSource.setZdataconsoleUrl(ZDATACONSOLE_URL);
        dataSource.setConfigPath(CONFIG_PATH);
        dataSource.setKeyWeightConfig(keyWeightConfig);
        dataSource.setShardingRules(shardingRules);
        dataSource.setTbNumForEachDb(tbNumForEachDb);
        dataSource.init();

        td = new ZdalDatasourceIntrospector();
        td.setTargetDataSource(dataSource);
        td.setCloseDBLimitNumber(0);
        td.init();

    }

    @Test
    //Ĭ�ϲ�������ȷ�������,�� group0����ȫ��������db server down������Ҫdebug�۲�
    public void testGetAvailableDBAndTableByWeights0() {
        tableName = "card_no_month";
        for (int i = 0; i < 10; i++) {
            String[] generateDT = td.getAvailableDBAndTableByWeights(tableName, 0, false);
            String dbNo = generateDT[0];
            int dbIndex1 = Integer.parseInt(dbNo);
            String tbName = generateDT[1];
            logger.warn("db is:" + dbNo + "; tb is:" + tbName);
            System.out.println(td.isDataBaseAvailable(dbIndex1, 0));
            Assert.assertTrue(td.isDataBaseAvailable(dbIndex1, 0));
        }

    }

    @Test
    //Ĭ�ϲ�������ȷ�������,�� group1����ȫ��
    public void testGetAvailableDBAndTableByWeights1() {
        tableName = "card_no_month";
        for (int i = 0; i < 10; i++) {
            String[] generateDT = td.getAvailableDBAndTableByWeights(tableName, 1, false);
            String dbNo = generateDT[0];
            int dbIndex1 = Integer.parseInt(dbNo);
            String tbName = generateDT[1];
            logger.warn("db is:" + dbNo + "; tb is:" + tbName);
            System.out.println(td.isDataBaseAvailable(dbIndex1, 1));
            Assert.assertTrue(td.isDataBaseAvailable(dbIndex1, 1));
        }

    }

    @Test
    //����Ϊtrueʱ��ȷ�������,�� group1����ȫ��
    public void testGetAvailableDBAndTableByWeights2() {
        tableName = "card_no_month";
        for (int i = 0; i < 10; i++) {
            String[] generateDT = td.getAvailableDBAndTableByWeights(tableName, 1, true);
            String dbNo = generateDT[0];
            int dbIndex1 = Integer.parseInt(dbNo);
            String tbName = generateDT[1];
            logger.warn("db is:" + dbNo + "; tb is:" + tbName);
            // System.out.println(td.isDataBaseAvailable(dbIndex1, 1));
            Assert.assertTrue(td.isDataBaseAvailable(dbIndex1, 1));
        }

    }

    @Test
    public void testGetAvailableDBAndTableByWeightsException() {
        //ҵ����Ĵ���������������в�����
        tableName = "card_month";
        for (int i = 0; i < 10; i++) {
            try {
                td.getAvailableDBAndTableByWeights(tableName, 0, false);
            } catch (IllegalArgumentException e) {
                Assert.assertTrue(e.getMessage().contains("�߼�����δ�ҵ�"));
            } catch (Exception e) {
                Assert.fail();
            }

        }

    }

    @Test
    public void testGetAvailableDBAndTableByWeightsException1() {
        //�����ڵķ����쳣����
        tableName = "card_no_month";
        for (int i = 0; i < 10; i++) {
            try {
                td.getAvailableDBAndTableByWeights(tableName, 3, false);
            } catch (IllegalArgumentException e) {
                Assert.assertTrue(e.getMessage().contains(
                    "The groupNum can't be more than 1,it is3"));
            } catch (Exception e) {
                Assert.fail();
            }

        }

    }

    @After
    public void tearDown() {
        //        try {
        //            dataSource.close();
        //        } catch (Throwable e) {
        //            logger.error("", e);
        //        }
    }
}
