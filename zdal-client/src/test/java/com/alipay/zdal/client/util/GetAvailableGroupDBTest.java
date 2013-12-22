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

import com.alipay.zdal.client.jdbc.ZdalDataSource;
import com.alipay.zdal.client.util.dispatchanalyzer.ZdalDatasourceIntrospector;

/**���Է���ȫ������У�����ҵ�����sharding�ֶ����Լ�ֵ���ؾ���������ڵ�dbid
 * @author xiaoju.luo
 * @version $Id: GroupDBTest.java,v 0.1 2012-5-14 ����04:03:49 xiaoju.luo Exp $
 */
public class GetAvailableGroupDBTest {

    private static Log                             logger           = LogFactory
                                                                        .getLog("GetAvailableGroupDBTest.class");
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
    public void testGetAvailableGroupDB() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("card_no", "2012042913");
        //for (int i = 0; i < 1000000; i++) {
        for (int i = 0; i < 100; i++) {
            String db = td.getAvailableGroupDB(parameters);
            logger.warn("db=" + db);
            int dbIndex0 = Integer.parseInt(db);
            //����������db�Ƿ����
            boolean bl = td.isDataBaseAvailable(dbIndex0, 1);
            //�������ڿ���db��index
            List<Integer> avaDB = td.getAvailableDBIndexes(1);
            //�������ڲ�����db��index
            List<Integer> navaDB = td.getNotAvailableDBIndexes(1);
            //�����ж����ɵ�db��group 1������Ч��
            Assert.assertTrue(bl);
            //�ж����ɵ�db�������ڲ����õ������
            Assert.assertFalse(navaDB.contains(dbIndex0));
            //�ж����ɵ�db�����ڿ��õ������
            Assert.assertTrue(avaDB.contains(dbIndex0));
        }

    }

    // �����sharding�ֶ�
    @Test
    public void testGetAvailableGroupDBException() {

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("card_no09", "2012042913");

        for (long i = 0; i < 10; i++) {
            try {
                td.getAvailableGroupDB(parameters);
            } catch (IllegalArgumentException e) {
                Assert.assertTrue(e.getMessage().contains("����������õ��ֶ�"));
            } catch (Exception e) {
                Assert.fail("��Ԥ���쳣");
            }
        }
    }

    @After
    public void tearDown() {

    }

}
