package com.alipay.zdal.common;

import com.alipay.zdal.common.lang.StringUtil;
import com.alipay.zdal.common.monitor.MonitorLogUtil;
import com.alipay.zdal.common.util.BoundedConcurrentHashMap;
import com.alipay.zdal.common.util.NagiosUtils;

public class Monitor {
    private static final BoundedConcurrentHashMap<String, String> sqlToMD5Map   = new BoundedConcurrentHashMap<String, String>();
    private static MD5Maker                                       md5Maker      = MD5Maker
                                                                                    .getInstance();
    public static final String                                    KEY1          = "ZDAL";
    public static final String                                    KEY1_TABLE    = KEY1 + "_TABLE|";
    public static final String                                    KEY2_EXEC_SQL = KEY1 + "_SQL|";

    public static volatile String                                 APPNAME       = KEY1;

    public enum RECORD_TYPE {
        RECORD_SQL, MD5, NONE
    }

    public enum ZdalConfigKey {
        statKeyRecordType, statKeyLeftCutLen, statKeyRight
    }

    private static volatile RECORD_TYPE       recordType                                               = RECORD_TYPE.NONE;
    private static volatile int               left                                                     = 0;                                                    //�����������ٸ��ַ�
    private static volatile int               right                                                    = 0;                                                    //�����������ٸ��ַ�

    public static final String                KEY3_TAIR_HIT_RATING                                     = "TAIR_HIT_RATING";

    /**
     * ���¼���������õ�ʱ����ܺ�ʱ
     */
    public static final String                KEY3_GET_DB_AND_TABLES                                   = "GET_DB_ANDTABLES_SUCCESS";
    /**
     * ִ��sql����ʱ�䣬�����������ݿ��ִ��ʱ�����ʱ��
     */
    public static final String                KEY3_EXECUTE_A_SQL_SUCCESS                               = "EXECUTE_A_SQL_SUCCESS";
    /**
    * ��ʱ����£�tddl�õ���ʱ��
    */
    public static final String                KEY3_EXECUTE_TIMEOUT_ZDAL                                = "EXECUTE_TIMEOUT_ZDAL";
    /**
     * ��ʱ����£����ݿ�ִ���õ���ʱ��
     */
    public static final String                KEY3_EXECUTE_TIMEOUT_DB                                  = "EXECUTE_TIMEOUT_DB";
    /**
     * ��������£�tddlִ���õ���ʱ��
     */
    public static final String                KEY3_EXECUTE_SUCCESS_ZDAL                                = "EXECUTE_SUCCESS_ZDAL";
    /**
     * ��������£����ݿ�ִ���õ���ʱ��
     */
    public static final String                KEY3_EXECUTE_SUCCESS_DB                                  = "EXECUTE_SUCCESS_DB";
    /**
     * �ܹ�ִ���˼����⣬������
     */
    public static final String                KEY3_EXECUTE_A_SQL_SUCCESS_DBTAB                         = "EXECUTE_A_SQL_SUCCESS_DBTAB";
    /**
     * ִ��sql����ʱ�䣬�����������ݿ��ִ��ʱ�����ʱ��
     */
    public static final String                KEY3_EXECUTE_A_SQL_TIMEOUT                               = "EXECUTE_A_SQL_TIMEOUT";

    public static final String                KEY3_EXECUTE_A_SQL_TIMEOUT_DBTAB                         = "EXECUTE_A_SQL_TIMEOUT_DBTAB";

    public static final String                KEY3_EXECUTE_A_SQL_EXCEPTION                             = "EXECUTE_A_SQL_WITH_EXCEPTION";

    public static final String                KEY3_EXECUTE_A_SQL_EXCEPTION_DBTAB                       = "EXECUTE_A_SQL_WITH_EXCEPTION_DBTAB";

    public static final String                KEY2_REPLICATION_SQL                                     = KEY1
                                                                                                         + "_REPLICATION_SQL|";

    /**
     * ���Ƶ��ӿ�ɹ�������д��ʱ����ܺķ�ʱ��
     */
    public static final String                KEY3_COPY_2_SLAVE_SUCCESS                                = "COPY_2_SLAVE_SUCCESS";

    /**
     * ��¼���������񵽸�����ʼ��ִ��֮�������ĵ�ʱ��
     */
    public static final String                KEY3_COPY_2_SLAVE_SUCCESS_TIME_CONSUMING_IN_THREADPOOL   = "COPY_2_SLAVE_SUCCESS_TIME_CONSUMING_IN_THREADPOOL";
    /**
     * ���Ƶ��ӿⳬʱ��Ҫ��¼��ѯ+д��sql���ķѵ�ʱ�䡣
     */
    public static final String                KEY3_COPY_2_SLAVE_TIMEOUT                                = "COPY_2_SLAVE_TIMEOUT";

    /**
     * ��¼���������񵽸�����ʼ��ִ��֮�������ĵ�ʱ��
     */
    public static final String                KEY3_COPY_2_SLAVE_TIMEOUT_TIME_CONSUMING_IN_THREADPOOL   = "COPY_2_SLAVE_TIMEOUT_TIME_CONSUMING_IN_THREADPOOL";
    /**
     * ���Ƶ��ӿ��쳣��������������ͻ��Ϊ���³ɹ����������
     */
    public static final String                KEY3_COPY_2_SLAVE_EXCEPTION                              = "COPY_2_SLAVE_EXCEPTION";

    public static final String                KEY3_COPY_2_SLAVE_EXCEPTION_TIME_CONSUMING_IN_THREADPOOL = "COPY_2_SLAVE_EXCEPTION_TIME_CONSUMING_IN_THREADPOOL";

    /**
     * ��log��ʱ��
     */
    public static final String                KEY3_WRITE_LOG_SUCCESS                                   = "WRITE_LOG_SUCCESS";

    public static final String                KEY3_WRITE_LOG_EXCEPTION                                 = "WRITE_LOG_EXCEPTION";

    //public static final StatMonitor statMonitor = StatMonitor.getInstance();
    public static final StatMonitorSingleHash statMonitor                                              = StatMonitorSingleHash
                                                                                                           .getInstance();
    static {
        statMonitor.start();
    }

    public static void addMonitor(String key1, String key2, String key3, long value1, long value2) {
        //һ��ʱ���ڲ���־���ʧ���ʺ�ƽ����Ӧʱ��
        if (KEY3_WRITE_LOG_SUCCESS.equals(key3)) {
            statMonitor.addStat(key1, "", NagiosUtils.KEY_INSERT_LOGDB_FAIL_RATE, 0);
            statMonitor.addStat(key1, "", NagiosUtils.KEY_INSERT_LOGDB_TIME_AVG, value1);
        } else if (KEY3_WRITE_LOG_EXCEPTION.equals(key3)) {
            statMonitor.addStat(key1, "", NagiosUtils.KEY_INSERT_LOGDB_FAIL_RATE, 1);
        }
        //һ��ʱ�����и��Ƶ�ʧ���ʺ�ƽ����Ӧʱ��
        else if (KEY3_COPY_2_SLAVE_SUCCESS.equals(key3)) {
            statMonitor.addStat(key1, "", NagiosUtils.KEY_REPLICATION_FAIL_RATE, 0);
            statMonitor.addStat(key1, "", NagiosUtils.KEY_REPLICATION_TIME_AVG, value1);
        } else if (KEY3_WRITE_LOG_EXCEPTION.equals(key3)) {
            statMonitor.addStat(key1, "", NagiosUtils.KEY_REPLICATION_FAIL_RATE, 1);
        }
    }

    public static String buildTableKey1(String virtualTableName) {
        return KEY1_TABLE + virtualTableName;
    }

    /**
     * ��¼sql
     * ����¼sql
     * ��¼ǰ��ȡsql
     * ��¼���ȡsql
     * ��¼md5
     * 
     * �������
     * 
     * @param sql
     * @return
     */
    public static String buildExecuteSqlKey2(String sql) {
        switch (recordType) {
            case RECORD_SQL:
                String s = fillTabWithSpace(sql);
                if (left > 0) {
                    s = StringUtil.left(s, left);
                }
                if (right > 0) {
                    s = StringUtil.right(s, right);
                }
                return s;
            case MD5:
                String s1 = fillTabWithSpace(sql);
                if (left > 0) {
                    s1 = StringUtil.left(s1, left);
                }
                if (right > 0) {
                    s1 = StringUtil.right(s1, right);
                }
                String md5 = sqlToMD5Map.get(s1);
                if (md5 != null) {
                    return md5;
                } else {
                    String sqlmd5 = md5Maker.getMD5(s1);
                    StringBuilder sb = new StringBuilder();
                    sb.append("[md5]").append(sqlmd5).append(" [sql]").append(s1);
                    sqlToMD5Map.put(s1, sqlmd5);
                    return sqlmd5;
                }
            case NONE:
                return "";
            default:
                throw new IllegalArgumentException("������Ҫ��ļ�¼log����! " + recordType);
        }

    }

    public static String buildExecuteDBAndTableKey1(String realDSKey, String realTable) {
        StringBuilder sb = new StringBuilder();
        sb.append(KEY1).append("|").append(realDSKey).append("|").append(realTable);
        return sb.toString();
    }

    /**
     * ���ݸ��ƹ�������Ҫ�õ���sql��key
     * 
     * @param sql
     * @return
     */
    public static String buildReplicationSqlKey2(String sql) {
        return buildExecuteSqlKey2(sql);
    }

    public static void add(String key1, String key2, String key3, long value1, long value2) {
        MonitorLogUtil.addStat(key1, key2, key3, value1, value2);
        //		StatLog.addStat(APPNAME,key1,key2,key3,value1,value2);
        //statMonitor.addStat(key1, key2, key3, value1); 
        //NagiosUtils.addNagiosLog(key1+"_"+key2+"_"+key3, value1);
        addMonitor(key1, key2, key3, value1, value2);
    }

    /*public static void add(String key1, String key2, String key3) {
    	MonitorLog.addStat(key1, key2, key3);
    	StatLog.addStat(APPNAME,key1,key2, key3);
    	//statMonitor.addStat(key1, key2, key3); 
    	//NagiosUtils.addNagiosLog(key1+"_"+key2+"_"+key3, 1);
    }*/

    /*public static void add(String key1, String key2, String key3, long value) {
    	MonitorLog.addStatValue2(key1, key2, key3, value);
    	//monitor logӦ����value1Ĭ�ϸ���1��Ȼ��value1/value2,��statLogģ��
    	StatLog.addStat(APPNAME,key1,key2,key3,1,value);
    	//statMonitor.addStat(key1, key2, key3, value); 
    	//NagiosUtils.addNagiosLog(key1+"_"+key2+"_"+key3, value);
    }*/

    public static void setAppName(String appname) {
        if (appname != null) {
            APPNAME = appname;
            MonitorLogUtil.setAppName(appname);
        }
    }

    /**
     * ������/t /s ȫ���滻Ϊ/s
    * @param str
    * @return
    */
    public static String fillTabWithSpace(String str) {
        if (str == null) {
            return null;
        }

        int sz = str.length();
        StringBuilder buffer = new StringBuilder(sz);

        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                buffer.append(str.charAt(i));
            } else {
                buffer.append(" ");
            }
        }

        return buffer.toString();
    }
}
