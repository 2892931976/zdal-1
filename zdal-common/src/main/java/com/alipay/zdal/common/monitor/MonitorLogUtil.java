package com.alipay.zdal.common.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.alipay.zdal.common.Constants;

public class MonitorLogUtil implements MonitorLogUtilMBean {
    private static final Logger                 statLog             = Logger
                                                                        .getLogger(Constants.ZDAL_MONITOR_LOGNAME);

    private static final String                 WRITETHREAD_NAME    = "LOGSTAT-WRITE";

    private static final String                 SPLITTER_1          = "\t";
    private static final String                 SPLITTER_2          = "\n";

    private static final Map<Keys, ValueObject> datas               = new ConcurrentHashMap<Keys, ValueObject>(
                                                                        100);

    private static final ReentrantLock          lock                = new ReentrantLock();

    private static final ReentrantLock          timerLock           = new ReentrantLock();
    private static final Condition              condition           = timerLock.newCondition();

    /*private static final Calendar cal = Calendar.getInstance(); */
    private static final SimpleDateFormat       formatter           = new SimpleDateFormat(
                                                                        "yyyy-MM-dd HH:mm:ss");
    private static final DecimalFormat          decimalformat       = new DecimalFormat("#.##");

    //JVM�����Ϣ
    private static final MemoryMXBean           memoryMXBean        = ManagementFactory
                                                                        .getMemoryMXBean();
    private static final ThreadMXBean           threadMXBean        = ManagementFactory
                                                                        .getThreadMXBean();

    private static String                       appName             = null;

    /** <p>
     * д�����ݵ�ʱ����.(ie.���ݲ���ʱ��)
     * </p>
     */
    static long                                 waitTime            = 120000L;

    static boolean                              jvmInfoPower        = true;

    private static final String                 KEY_LEVEL_JVM       = "JVM";
    private static final String                 KEY_LEVEL_CUP       = "CPU";
    private static final String                 KEY_LEVEL_MEMORY    = "MEMORY";
    private static final String                 KEY_LEVEL_SITUATION = "SITUATION";
    private static final String                 KEY_LEVEL_USAGE     = "USAGE";
    private static final String                 KEY_LEVEL_THREAD    = "THREAD";
    private static final String                 KEY_LEVEL_TOTAL     = "TOTAL";

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        MonitorLogUtil.waitTime = waitTime;
        MonitorLogUtil.runWriteThread();
    }

    public boolean getJVMInfoPower() {
        return jvmInfoPower;
    }

    public void setJVMInfoPower(boolean power) {
        MonitorLogUtil.jvmInfoPower = power;
    }

    /** д�����ݵ��߳� */
    private static Thread writeThread = null;

    static { //�����߳�
        runWriteThread();
    }

    /**
     * ִ��д���߳�
     */
    public static void runWriteThread() {

        if (null != writeThread) { // ����̻߳����ڣ���interruptһ��
            try {
                writeThread.interrupt();
            } catch (Exception e) {
                statLog.error("ERROR ## interrupt write thread error", e);
            }
        }
        // ��ʼ���߳�
        writeThread = new Thread(new Runnable() {
            public void run() {
                // �ȴ�waitTime��
                while (true) {
                    timerLock.lock();
                    try {
                        condition.await(waitTime, TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                        statLog.error("ERROR ## wait error", e);
                    } finally {
                        timerLock.unlock();
                    }

                    MonitorLogUtil.writeLog();
                }
            }
        });
        writeThread.setName(WRITETHREAD_NAME);
        writeThread.start();
    }

    private static void writeLog() {
        // ����д��
        Map<Keys, ValueObject> tmp = new HashMap<Keys, ValueObject>(datas.size()); // ����һ����ʱ��������Ϊ�˱�֤���ݶ�д���ļ���ż���datas������
        StringBuilder sb = new StringBuilder();

        for (Keys key : datas.keySet()) {
            long[] values = datas.get(key).getValues();
            long value1 = values[0];
            long value2 = values[1];
            if (0 == value1 && 0 == value2)
                continue;

            sb.append(key.getString(SPLITTER_1)).append(SPLITTER_1);
            sb.append(value1).append(SPLITTER_1);
            sb.append(value2).append(SPLITTER_1);
            synchronized (formatter) {
                sb.append(formatter.format(Calendar.getInstance().getTime())).append(SPLITTER_1);
            }
            sb.append(SPLITTER_2);
            tmp.put(key, new ValueObject(value1, value2));
        }

        if (jvmInfoPower) {
            sb.append(getJvmInfo());
        }

        statLog.warn("WARN ## \n" + sb);
        // ѭ�����Ѿ�д���ļ������ݴ�datas�м���
        for (Keys key : tmp.keySet()) {
            long[] values = tmp.get(key).getValues();
            datas.get(key).deductCount(values[0], values[1]);
        }
    }

    /**
     * keyOne,keyTwo,keyThree���ܴ�������\n�Ļ���
     * @param keyOne
     * @param keyTwo
     * @param keyThree
     */
    public static void addStat(String keyOne, String keyTwo, String keyThree) {
        Keys keys = new Keys(keyOne, keyTwo, keyThree);
        addStat(keys, 1, 0);
    }

    /**
     * keyOne,keyTwo,keyThree���ܴ�������\n�Ļ���
     * @param keyOne
     * @param keyTwo
     * @param keyThree
     * @param value
     */
    public static void addStatValue2(String keyOne, String keyTwo, String keyThree, long value) {
        Keys keys = new Keys(keyOne, keyTwo, keyThree);
        addStat(keys, 1, value);
    }

    /**
     * keyOne,keyTwo,keyThree���ܴ�������\n�Ļ���
     * @param keyOne
     * @param keyTwo
     * @param keyThree
     * @param value1
     * @param value2
     */
    public static void addStat(String keyOne, String keyTwo, String keyThree, long value1,
                               long value2) {
        Keys keys = new Keys(keyOne, keyTwo, keyThree);
        addStat(keys, value1, value2);
    }

    public static void addStat(Keys keys, long value1, long value2) {
        getAppName(keys.getAppName());
        ValueObject v = getValueObject(keys);
        v.addCount(value1, value2);
    }

    /**
     * @deprecated
     * 
     * */
    public static void addStatWithAppName(String appName, String keyOne, String keyTwo,
                                          String keyThree, long value1, long value2) {
        Keys keys = new Keys(appName, keyOne, keyTwo, keyThree);
        addStat(keys, value1, value2);
    }

    protected synchronized static void getAppName(String name) {
        if (name != null) {
            if (appName == null) {
                appName = name;
            }
        }
    }

    public synchronized static void setAppName(String name) {
        appName = name;
    }

    /**
     * ���������ȷ�����᷵��null������keys�޶�Ӧ��ValueObjectʱ����֮��
     */
    protected static ValueObject getValueObject(Keys keys) {
        ValueObject v = datas.get(keys);
        if (null == v) {
            lock.lock();
            try {
                v = datas.get(keys);
                if (null == v) {
                    v = new ValueObject();
                    datas.put(keys, v);
                }
            } finally {
                lock.unlock();
            }
        }
        return v;
    }

    public MonitorLogUtil() {

    }

    /**
       * ���jvm��Ϣ
       * */
    private static String getJvmInfo() {

        StringBuilder sb = new StringBuilder();

        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();

        //jvm �ڴ���Ϣ TC  JVM  MEMORY SITUATION  1024  856  2009-04-01 18:06:56  vm4.qa 
        if (appName != null) {
            sb.append(appName).append(SPLITTER_1);
        }
        sb.append(KEY_LEVEL_JVM).append(SPLITTER_1);
        sb.append(KEY_LEVEL_MEMORY).append(SPLITTER_1);
        sb.append(KEY_LEVEL_SITUATION).append(SPLITTER_1);
        sb.append(formatValue(memoryUsage.getMax())).append(SPLITTER_1);
        sb.append(formatValue(memoryUsage.getUsed())).append(SPLITTER_1);
        synchronized (formatter) {
            sb.append(formatter.format(Calendar.getInstance().getTime())).append(SPLITTER_1);
        }
        sb.append(SPLITTER_2);

        //jvm CPU��Ϣ TC  JVM  CPU    USAGE        68    0   2009-04-01 18:06:56  vm4.qa 
        if (appName != null) {
            sb.append(appName).append(SPLITTER_1);
        }

        sb.append(KEY_LEVEL_JVM).append(SPLITTER_1);
        sb.append(KEY_LEVEL_CUP).append(SPLITTER_1);
        sb.append(KEY_LEVEL_USAGE).append(SPLITTER_1);
        sb.append(formatNanosecond(threadMXBean.getCurrentThreadCpuTime())).append(SPLITTER_1);
        synchronized (formatter) {
            sb.append(formatter.format(Calendar.getInstance().getTime())).append(SPLITTER_1);
        }
        sb.append(SPLITTER_2);

        // jvm �߳���Ϣ  TC  JVM  THREAD TOTAL  100  0  2009-04-01 18:06:56  vm4.qa 
        if (appName != null) {
            sb.append(appName).append(SPLITTER_1);
        }
        sb.append(KEY_LEVEL_JVM).append(SPLITTER_1);
        sb.append(KEY_LEVEL_THREAD).append(SPLITTER_1);
        sb.append(KEY_LEVEL_TOTAL).append(SPLITTER_1);
        sb.append(threadMXBean.getDaemonThreadCount()).append(SPLITTER_1);
        synchronized (formatter) {
            sb.append(formatter.format(Calendar.getInstance().getTime())).append(SPLITTER_1);
        }

        return sb.toString();

    }

    /**
    * ���ֽڵ�λתΪM������С������λ
    * */
    private static String formatValue(long value) {
        Double tempValue = new Double(value) / 1024 / 1024;
        return decimalformat.format(tempValue);
    }

    private static String formatNanosecond(long value) {
        Double tempValue = new Double(value) / 1000000000;
        return decimalformat.format(tempValue);
    }

    public static void main(String[] args) {

        Thread TestwriteThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    timerLock.lock();
                    try {
                        condition.await(5, TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                        statLog.error("ERROR ## wait error", e);
                    } finally {
                        timerLock.unlock();
                    }
                    MonitorLogUtil.addStat("TC2", null, null, 541, 256);
                    MonitorLogUtil.addStat("TC1", "SyncCreateOrder", "ERROR", 541, 256); //ok
                    MonitorLogUtil.addStat("TC3", "SyncCreateOrder", null, 541, 256);
                    MonitorLogUtil.addStat("UIC4", "SyncCreateOrder", "timout", 5621, 10);
                    MonitorLogUtil.addStat("IC5", "SyncCreateOrder", "normal", 999, 5221);
                    MonitorLogUtil.addStatWithAppName("appName", "IC6", "SyncCreateOrder",
                        "normal", 999, 5221);
                    MonitorLogUtil.addStatWithAppName("appName1", null, null, null, 999, 5221);//ok
                    MonitorLogUtil.addStatWithAppName("appName2", "SyncCreateOrder", null, null,
                        999, 5221);
                    MonitorLogUtil.addStatWithAppName("appName3", "SyncCreateOrder", null, null,
                        999, 5221);
                }
            }
        });
        TestwriteThread.setName("TestTreadName");
        TestwriteThread.start();
    }
}
