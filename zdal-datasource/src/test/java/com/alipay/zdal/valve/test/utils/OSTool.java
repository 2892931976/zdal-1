package com.alipay.zdal.valve.test.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import com.alipay.zdal.valve.test.objects.DSLog;
import com.alipay.zdal.valve.test.objects.SQLLog;
import com.alipay.zdal.valve.test.objects.TXLog;

/**
 * 
 * 
 * @author liangjie.li
 * @version $Id: OSTool.java, v 0.1 2012-8-17 ����5:07:47 liangjie.li Exp $
 */
public class OSTool {
    private String logNameDS         = "valve-ds-monitor.log";
    private String logNameSQL        = "valve-sql-monitor.log";
    private String logNameTX         = "valve-tx-monitor.log";
    private String logNameTair       = "valve-tair-monitor.log";
    private String logNameTairThread = "valve-tair-thread-monitor.log";
    private String logNameDV         = "zdatavalve.log";

    public String getLogNameTairThread() {
        return logNameTairThread;
    }

    public void setLogNameTairThread(String logNameTairThread) {
        this.logNameTairThread = logNameTairThread;
    }

    public String getLogNameDS() {
        return logNameDS;
    }

    public void setLogNameDS(String logNameDS) {
        this.logNameDS = logNameDS;
    }

    public String getLogNameTair() {
        return logNameTair;
    }

    public void setLogNameTair(String logNameTair) {
        this.logNameTair = logNameTair;
    }

    public String getLogNameSQL() {
        return logNameSQL;
    }

    public void setLogNameSQL(String logNameSQL) {
        this.logNameSQL = logNameSQL;
    }

    public String getLogNameTX() {
        return logNameTX;
    }

    public void setLogNameTX(String logNameTX) {
        this.logNameTX = logNameTX;
    }

    public String getLogNameDV() {
        return logNameDV;
    }

    public void setLogNameDV(String logNameDV) {
        this.logNameDV = logNameDV;
    }

    /**
     * �û���Ŀ¼
     * @return �û���Ŀ¼
     * @author wei.yao
     */
    public String getUserHome() {
        return System.getProperty("user.home");
    }

    /**
     * ������
     * @return ""-��;������
     * @author wei.yao
     */
    public String getHostName() {
        String hostNameString = "";
        try {
            hostNameString = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostNameString;
    }

    /**
     * zdatasource��־�ļ���
     * @param index ds-����Դ sql-sql tx-���� dv-zdatavalve tair-tair
     * @param date null-���� yyyy-mm-dd-ָ������
     * @return �ļ�����·��
     * @author wei.yao
     */
    public String getLogName(String index, String date) {
        String pathString = getLogPath();
        if ("ds".equals(index)) {
            pathString += logNameDS;
        }
        if ("sql".equals(index)) {
            pathString += logNameSQL;
        }
        if ("tx".equals(index)) {
            pathString += logNameTX;
        }
        if ("dv".equals(index)) {
            pathString += logNameDV;
        }
        if ("tair".equals(index)) {
            pathString += logNameTair;
        }
        if ("tairthread".equals(index)) {
            pathString += logNameTairThread;
        }
        if (date != null) {//�Զ����ļ���
            pathString += "." + date;
        } else {//Ĭ���ļ���
            if (!new File(pathString).exists()) {//���յ��ļ�
                pathString += "." + getDate() + "_" + getHour();//���ն��ļ�
                if (!new File(pathString).exists()) {//�ļ�������
                    pathString = "��־�ļ�������";
                }
            }
        }
        return pathString;
    }

    /**
     * ��־·��
     * @return ��־·��
     * @author wei.yao
     */
    public String getLogPath() {
        return getUserHome() + "/logs/valve/";
    }

    /**
     * �ļ������ļ�����
     * @param path �ļ���
     * @return �ļ�����
     * @author wei.yao
     */
    public int getFileNum(String path) {
        File dir = new File(path);
        if (!dir.exists() || dir.list() == null) {
            return 0;
        }
        return dir.list().length;
    }

    /**
     * �ļ����N�м�¼
     * @param file �ļ�����·��
     * @param lastline ��������,���������������������п�ʼ;-1-ȫ����¼
     * @return ���N�м�¼
     * @author wei.yao
     */
    public Object[] getRecord(String file, int lastline) {
        Vector<String> record = new Vector<String>();
        RandomAccessFile raf = null;
        if (lastline == -1) {
            lastline = 2147483647;
        }
        try {
            raf = new RandomAccessFile(file, "r");
            long size = getLineNum(file);
            String contentString;
            for (long i = 1; i <= size; i++) {
                contentString = raf.readLine();
                if ("".equals(contentString)) {
                    i--;//�հ��в�����
                } else {
                    if (i > (size - lastline)) {
                        record.add(contentString);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return record.toArray();
    }

    /**
     * ������־��¼���󼯺�
     * @param records ��־��¼
     * @return 
     * @author wei.yao
     */
    public TXLog[] getRecTXLog(Object[] records) {
        TXLog[] tXLogArray = new TXLog[records.length];
        for (int i = 0; i < records.length; i++) {
            tXLogArray[i] = new TXLog(records[i].toString());
        }
        return tXLogArray;
    }

    /**
     * sql��־��¼���󼯺�
     * @param records ��־��¼
     * @return 
     * @author wei.yao
     */
    public SQLLog[] getRecSQLLog(Object[] records) {
        SQLLog[] logArray = new SQLLog[records.length];
        for (int i = 0; i < records.length; i++) {
            logArray[i] = new SQLLog(records[i].toString());
        }
        return logArray;
    }

    /**
     * ����Դ��־��¼���󼯺�
     * @param records ��־��¼
     * @return 
     * @author wei.yao
     */
    public DSLog[] getRecDSLog(Object[] records) {
        if (records.length == 0) {
            return null;
        }
        DSLog[] logArray = new DSLog[records.length];
        for (int i = 0; i < records.length; i++) {
            logArray[i] = new DSLog(records[i].toString());
        }
        return logArray;
    }

    /**
     * �ļ�����
     * @param file �ļ�����·��
     * @return �ļ�����
     * @author wei.yao
     */
    public long getLineNum(String file) {
        int lineNum = 0;
        RandomAccessFile raf = null;
        String cont;
        try {
            raf = new RandomAccessFile(file, "r");
            while ((cont = raf.readLine()) != null) {
                if (!"".equals(cont)) {
                    lineNum++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lineNum;
    }

    /**
     * һ���ļ�ȫ������
     * @param file �ļ������·��
     * @return �ļ�����
     */
    public long getLineNum(String[] file) {
        int lineNum = 0;
        for (int i = 0; i < file.length; i++) {
            lineNum += getLineNum(file[i]);
        }
        return lineNum;
    }

    /**
     * ������ͬ�ļ������� ���磺a.log;a.log.2011;a.log.2012 ͳ�������ļ�
     * @param dir ·��
     * @param fileIndex �ļ����Ӵ�
     * @return �ļ�������
     * @author wei.yao
     */
    public long getLineNum(String dir, String fileIndex) {
        int lineNum = 0;
        Object[] files = getFiles(dir, fileIndex);
        for (int i = 0; i < files.length; i++) {
            lineNum += getLineNum(dir + files[i]);
        }
        return lineNum;
    }

    /**
     * �ļ��Ƿ����
     * @param file ����·��
     * @return true-���� false-������
     * @author wei.yao
     */
    public boolean fileIsExsit(String file) {
        return new File(file).exists();
    }

    /**
     * ɾ���ļ�
     * @param filePath ��ɾ���ļ�����·��
     * @author wei.yao
     */
    public void fileDel(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            System.out.println("ɾ���ļ����" + file.delete());
        }
    }

    /**
     * ϵͳ����
     * @return ϵͳ����
     * @author wei.yao
     */
    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * ϵͳʱ��
     * @return ϵͳʱ��
     * @author wei.yao
     */
    public String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * ϵͳʱ��
     * @param partString ʱ���ʽ
     * @return ϵͳʱ��
     * @author wei.yao
     */
    public String getTime(String partString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(partString);
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * ��ǰʱ�������룬ǰ��1����
     * @return ʱ�������� yyyy-MM-dd HH:mm:ss
     * @author wei.yao
     */
    public Object[] getTimeRegion() {
        Calendar calendar = Calendar.getInstance();
        int error = 60;
        Vector<String> timeRegion = new Vector<String>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar.add(Calendar.SECOND, -error);
        for (int i = 0; i < (error * 2); i++) {
            timeRegion.add(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.SECOND, 1);
        }
        return timeRegion.toArray();
    }

    /**
     * ����ʱ���Ƿ��ڵ�ǰʱ�������� ���1����
     * @param timeActual
     * @return true-��
     * @author wei.yao
     */
    public boolean timeEquals(String timeActual) {
        boolean res = false;
        Object[] timeRegion = getTimeRegion();
        for (int i = 0; i < timeRegion.length; i++) {
            //			Log.info(timeRegion[i] + "");
            if (timeRegion[i].equals(timeActual)) {
                res = true;
                break;
            }
        }
        return res;
    }

    /**
     * ϵͳСʱ
     * @return ϵͳСʱ
     * @author wei.yao
     */
    public String getHour() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * ����ϵͳʱ��
     * @param time hh:mm:ss
     * @author wei.yao
     */
    public void setHostTime(String time) {
        try {
            if (getOSName().equals("windows")) {
                Runtime.getRuntime().exec("cmd /c time " + time);
            }
            if (getOSName().equals("linux")) {
                //ûʵ��
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ����ϵͳ����
     * @return windows- linux(unix)-
     * @author wei.yao
     */
    public String getOSName() {
        String os = System.getProperty("os.name").toLowerCase();
        String osActualString = "unkown";
        if (os.indexOf("win") >= 0) {
            osActualString = "windows";
        }
        if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
            osActualString = "linux";
        }
        return osActualString;
    }

    /**
     * ������ͬ���ļ�
     * @param dir ·��
     * @param fileIndex �ļ����������ַ���
     * @return ��ͬ���ļ�
     * @author wei.yao
     */
    public Object[] getFiles(String dir, String fileIndex) {
        File dirString = new File(dir);
        Vector<String> sameFileName = new Vector<String>();
        String[] listStrings = dirString.list();
        for (int i = 0; i < listStrings.length; i++) {
            if (listStrings[i].contains(fileIndex)) {
                sameFileName.add(listStrings[i]);
            }
        }
        return sameFileName.toArray();
    }

}