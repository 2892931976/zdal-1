/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.alipay.zdal.sequence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.alipay.zdal.sequence.SequenceConstants;
import com.alipay.zdal.sequence.SequenceRange;
import com.alipay.zdal.sequence.exceptions.SequenceException;

/**
 * �߿���sequence ��ʹ�õ�����Դ�İ�װ��,
 * ����ʵ�ֶ�sequence��������Դ���߳��ͻָ�
 * 
 * @author zhaofeng.wang
 * @version $Id: SequenceDataSource.java, v 0.1 2011-12-22 ����04:46:44 zhaofeng.wang Exp $
 */
public class SequenceDataSourceHolder {

    private static final Logger logger              = Logger
                                                        .getLogger(SequenceConstants.ZDAL_SEQUENCE_LOG_NAME);
    /**
    * ����Դ�Ƿ���ã�Ĭ�Ͽ���
    */
    private volatile boolean    isAvailable         = true;

    /**
     * ����������Դ
     */
    private final DataSource    ds;

    /**
     * �������������ڿ���ֻ����һ��ҵ���߳�ȥ���ԣ�
     */
    private final ReentrantLock lock                = new ReentrantLock();
    /**
     * �ϴ�����ʱ��
     */
    private volatile long       lastRetryTime       = 0;
    /**
     * �쳣����
     */
    private volatile int        exceptionTimes      = 0;
    /**
     * ��һ�β����쳣��ʱ�䣬��λ����
     */
    private volatile long       firstExceptionTime  = 0;
    /**
     * ���Թ���db��ʱ������Ĭ��ֵ��Ϊ30s,��λ����
     */
    private final int           retryBadDbInterval  = 30000;

    /**
     * ��λʱ��Σ�Ĭ��Ϊ5���ӣ�����ͳ��ʱ�����ĳ��db���쳣�Ĵ�������λ����
     */
    private final int           timeInterval        = 300000;
    /**
     * ��λʱ���������쳣�Ĵ���������������ֵ�㽫����Դ��Ϊ������
     */
    private final int           allowExceptionTimes = 20;

    /** 
     * sequence������Ĭ��Ϊsequence
     */
    private String              tableName;

    /**
     * �Ƿ����Զ���������
     */
    private boolean             adjust;
    /**
     * ��ʽ��select value from sequence where name=?
     */
    private String              selectSql;
    /**
     * ��ʽupdate table_name(default:sequence) set value=? ,gmt_modified=? where name=? and value=?
     */
    private String              updateSql;
    /**
     * ��ʽ: insert into table_name(default:sequence)(name,value,min_value,max_value,step,gmt_create,gmt_modified) values(?,?,?,?,?,?,?)
     */
    private String              insertSql;

    /**
     * ���ó��õĲ���
     * @param tableName   ����
     * @param selectSql   select��sql���
     * @param updateSql   ������� 
     * @param insertSql   �������
     * @param adjust      �Ƿ����Զ���������
     */
    public void setParameters(String tableName, String selectSql, String updateSql,
                              String insertSql, boolean adjust) {
        this.tableName = tableName;
        this.selectSql = selectSql;
        this.updateSql = updateSql;
        this.insertSql = insertSql;
        this.adjust = adjust;
    }

    /**
     * ���캯��
     * @param ds
     */
    public SequenceDataSourceHolder(DataSource ds) {
        this.ds = ds;
    }

    public DataSource getDs() {
        return ds;
    }

    /**
     * �����ѡ�������Դ�ϻ�ȡsequence��
     * 
     * @param index          ����Դ���к�
     * @param sequenceName   sequence��
     * @param minValue       ��Сֵ
     * @param maxValue       ���ֵ
     * @param innerStep      �ڲ���
     * @param outStep        �ⲽ��
     * @param excludeIndexes ��¼�������Ѿ����ϵ�����Դ
     * @return               ���õ�sequence��
     * @throws SequenceException
     */
    public SequenceRange tryOnSelectedDataSource(int index, String sequenceName, long minValue,
                                                 long maxValue, int innerStep, int outStep,
                                                 List<Integer> excludeIndexes)
                                                                              throws SequenceException {
        if (isAvailable) {
            return tryOnAvailableDataSource(index, sequenceName, minValue, maxValue, innerStep,
                outStep, excludeIndexes);
        } else {
            return tryOnFailedDataSource(index, sequenceName, minValue, maxValue, innerStep,
                outStep, excludeIndexes);
        }
    }

    /**
     * �ڿ��õ�����Դ�ϻ�ȡsequence�Σ���������쳣�������ͳ��
     * @param index          ����Դ���к�
     * @param sequenceName   sequence����
     * @param minValue       ��Сֵ
     * @param maxValue       ���ֵ
     * @param innerStep      �ڲ���
     * @param outStep        �ⲽ��
     * @param excludeIndexes ��¼�������Ѿ����ϵ�����Դ
     * @return               sequence��
     * @throws SequenceException
     */
    public SequenceRange tryOnAvailableDataSource(int index, String sequenceName, long minValue,
                                                  long maxValue, int innerStep, int outStep,
                                                  List<Integer> excludeIndexes)
                                                                               throws SequenceException {

        long adjustValue = -1; //�������ֵ
        long oldValue = -1; //��ֵ��ÿ�δ�db��ȡ��������һ�θ��º��ֵ,���� �ֹ����� version�ֶ�
        long newValue = -1; //��ֵ���������µ�db��ֵ
        long beginValue = -1; // �˴μ������ص�sequenceRange����ʼֵ 
        long endValue = -1; //�˴μ������ص�sequenceRange�Ľ���ֵ
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        //��ѯһ����¼
        try {
            con = ds.getConnection();
            stmt = con.prepareStatement(selectSql);
            stmt.setString(1, sequenceName);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new SequenceException("No sequence record in the table:" + tableName
                                            + ",please initialize it!");
            }
            oldValue = rs.getLong(1);

            if (oldValue < 0 || oldValue > maxValue || oldValue < minValue) {
                StringBuilder message = new StringBuilder();
                message.append("Sequence value set error, currentValue = " + oldValue
                               + ",minValue=" + minValue + ",maxValue=" + maxValue);
                message.append(", please check table: ").append(tableName);
                throw new SequenceException(message.toString());
            }

            adjustValue = getAjustValue(index, oldValue, minValue, maxValue, innerStep, outStep,
                sequenceName, false);

            beginValue = adjustValue;
            if (beginValue >= maxValue) {
                beginValue = getAjustValue(index, minValue, minValue, maxValue, innerStep, outStep,
                    sequenceName, true);
            }
            //���㱾��sequence�εĽ���ֵ
            endValue = beginValue + innerStep;
            if (endValue > maxValue) {
                endValue = maxValue;
            } else {
                endValue = endValue - 1;
            }
            //��֤sequence�ε���ʼֵ
            if (beginValue > endValue) {
                throw new SequenceException("SEQUENCE-VALUE-ERROR:beginValue=" + beginValue
                                            + " is larg than endValue=" + endValue);
            }

            newValue = beginValue + outStep;
            //�����ֵ���������ֵ������¿�ʼ����
            if (newValue > maxValue) {
                newValue = getAjustValue(index, minValue, minValue, maxValue, innerStep, outStep,
                    sequenceName, true);
            }
        } catch (SQLException e) {
            logger.warn("WARN ## ȡsequence��Χ�����г���,db-index=" + index + ",oldValue=" + oldValue
                        + ",newValue=" + newValue, e);
            calculateExceptionTimes(index);
            excludeIndexes.add(index);
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            } catch (Exception e) {
                logger.error("ERROR ## close resources has an error", e);
            }
        }

        //���¸ü�¼
        try {
            stmt = con.prepareStatement(updateSql);
            stmt.setLong(1, newValue);
            long gmt_modified = System.currentTimeMillis();
            stmt.setTimestamp(2, new Timestamp(gmt_modified));
            stmt.setString(3, sequenceName);
            stmt.setLong(4, oldValue);

            //����db
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                //����˴�û�и��¼�¼��˵��������������ˣ�����null
                logger
                    .warn("WARN ## ����sequence��¼ʧ�ܣ�oldValue=" + oldValue + ",newValue=" + newValue);
                return null;
            }
            logger.warn("WARN ## Update the sequence of " + index + " th dataSource to " + newValue
                        + " from " + oldValue);

            return new SequenceRange(beginValue, endValue);
        } catch (SQLException e) {
            logger.warn("WARN ## ����sequence�����г���,index=" + index + ",oldValue=" + oldValue
                        + ",newValue=" + newValue, e);
            calculateExceptionTimes(index);
            excludeIndexes.add(index);
            return null;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (con != null) {
                    con.close();
                    con = null;
                }
            } catch (Exception e) {
                logger.error("ERROR ## close resources has an error", e);
            }
        }
    }

    /**
     * ���������ڵ���value����db��sequence ����Σ�����������Ϊһ��outStep��<br>
     * һ������£�db��sequence��ֵ�����й��ɵĽ��иĶ����粻���������ظĶ��Ļ���������е����������������˿������� minValue ����֮��<br>
     * �ڳ�ʼ��֮ǰ������ҵ��ָ��minValue�Լ�maxValue, �Լ�valueֵ����value����������minValue<=value<=maxValue;<br>
     * �����������������»����sequence�ĵ�����<br>
     * 1��ָ����valueֵ�����Ϲ��� value= index*innerStep;<br>
     * 2���ڴﵽ���ֵ֮�󣬻��seq����Сֵ��ʼѭ��������ָ������Сֵ�����Ϲ��� index*innnerStep;<br>
     * 
     * ���� oldValue= m*outStep+n��adjustValue=i*outStep+index*innerStep;<br>
     * ��ô adjustValue=(oldValue-oldValue% outStep)+j*outStep+index*innerStep(����j=0 or 1)<br>
     * ������̣�<br>
     *   1)oldValue%outStep= n <br>
     *   2)oldValue-oldValue%outStep= m*outStep��<br>
     *   3)adjustValue= (m+j)*outStep+index*innderStep<br>
     * ���ת���ɱȽ� m �� i �Ĵ�С����Ϊ���������<br>
     * 1)m=i=0����checkValue=index * innerStep��<br>
     * 2)m=i>0, ��checkValue= (oldValue-oldValue% outStep)+index*innerStep��<br>
     * 3)m-i=-1,��checkValue= (oldValue-oldValue% outStep)+outStep+index*innerStep��<br>
     * 
     * @param index
     * @param oldValue
     * @param innerStep
     * @param outStep
     * @param sequenceName
     * @return
     * @throws SequenceException
     */
    private long getAjustValue(int index, long oldValue, long minValue, long maxValue,
                               int innerStep, int outStep, String sequenceName, boolean isLoop)
                                                                                               throws SequenceException {
        if (minValue > maxValue || minValue + innerStep > maxValue) {
            throw new SequenceException("ERROR ## SET-VALUE-ERROR:thread-name="
                                        + Thread.currentThread().getName() + "minValue=" + minValue
                                        + ",maxValue=" + maxValue + ",innerStep=" + innerStep
                                        + ",outStep" + outStep);
        }
        long adjustValue = oldValue;
        if ((!check(index, oldValue, innerStep, outStep))) {
            if (adjust) {
                long value1 = index * innerStep;
                long value2 = (oldValue - oldValue % outStep) + index * innerStep;
                long value3 = (oldValue - oldValue % outStep) + outStep + index * innerStep;
                if (value1 >= oldValue) {
                    adjustValue = value1;
                } else if (value2 >= oldValue) {
                    adjustValue = value2;
                } else if (value3 >= oldValue) {
                    adjustValue = value3;
                }
                logger.warn("WARN ## SEQUENCE-AJUST-SUCCESS:thread-name="
                            + Thread.currentThread().getName() + ",sequenceName=" + sequenceName
                            + ",dbIndex=" + index + ",oldValue=" + oldValue + ",adjustValue="
                            + adjustValue + ",isLoop=" + isLoop);
                //�жϵ������ֵ�Ƿ�Ϸ�
                if (adjustValue < minValue || adjustValue > maxValue) {
                    throw new SequenceException("WARN ## AJUST-VALUE-FAILURED:thread-name="
                                                + Thread.currentThread().getName() + ",seqName="
                                                + sequenceName + ",dbIndex=" + index
                                                + ",ajustValue=" + adjustValue + ",minValue="
                                                + minValue + ",maxValue=" + maxValue);
                }
            } else {
                throw new SequenceException("SEQUENCE-VALUE-ERROR:thread-name="
                                            + Thread.currentThread().getName() + ",seqName="
                                            + sequenceName + ",db-index=" + index + ",oldValue="
                                            + oldValue + ",innerStep=" + innerStep + ",outStep="
                                            + outStep + ",seqֵ���󣬸��ǵ�������Χ���ˣ����޸����ݿ⣬���߿���adjust���أ�");
            }
        }

        return adjustValue;
    }

    /**
     *  ��ȡ��ǰdb�����е�sequence��¼��ָ���ֶ�ֵ
     *  
     * @param selectSql  ��select name,min_value,max_value,step from table_name(default:sequence)
     * @param nameColumn  sequence����
     * @param minValueColumnName ��Сֵ
     * @param maxValueColumnName ���ֵ
     * @param innerStepColumnName �ڲ���
     * @return Map<String, Map<String, Object>> ���key��ʶsequence���֣��ڲ�key��ʾ��С�����ֵ�Լ������ȣ�   
     * @throws SQLException
     */
    public Map<String, Map<String, Object>> getAllSequenceRecordName(String selectSql,
                                                                     String nameColumn,
                                                                     String minValueColumnName,
                                                                     String maxValueColumnName,
                                                                     String innerStepColumnName)
                                                                                                throws SQLException {
        Map<String, Map<String, Object>> records = new HashMap<String, Map<String, Object>>(0);
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        //��ѯ���м�¼
        try {
            con = ds.getConnection();
            stmt = con.prepareStatement(selectSql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString(nameColumn);
                long min = rs.getLong(minValueColumnName);
                long max = rs.getLong(maxValueColumnName);
                int step = rs.getInt(innerStepColumnName);
                if (records.get(name) == null) {
                    Map<String, Object> keyAndValue = new HashMap<String, Object>(0);
                    keyAndValue.put(minValueColumnName, min);
                    keyAndValue.put(maxValueColumnName, max);
                    keyAndValue.put(innerStepColumnName, step);
                    records.put(name, keyAndValue);
                }
            }
        } catch (SQLException e) {
            logger.error("get all the sequence record failed!", e);
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (con != null) {
                    con.close();
                    con = null;
                }
            } catch (Exception e) {
                logger.error("ERROR ## close resources has an error", e);
            }
        }
        return records;
    }

    /**
     * ����sequenceName ��ȡ��ǰdb���ָ��sequence��¼�ĸ��ֶ�ֵ
     * 
     * @param selectSql: select value,min_value,max_value,step from sequence where name=?
     * @param nameColumn          sequence����
     * @param minValueColumnName  ��Сֵ
     * @param maxValueColumnName  ���ֵ
     * @param innerStepColumnName �ڲ���
     * @return
     * @throws SQLException
     * @throws SequenceException 
     */
    public Map<String, Map<String, Object>> getSequenceRecordByName(String selectSql,
                                                                    String minValueColumnName,
                                                                    String maxValueColumnName,
                                                                    String innerStepColumnName,
                                                                    String sequenceName)
                                                                                        throws SQLException,
                                                                                        SequenceException {
        Map<String, Map<String, Object>> records = new HashMap<String, Map<String, Object>>(0);
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        //��ѯһ����¼,����ü�¼�����ڣ��ͱ��쳣
        try {
            con = ds.getConnection();
            stmt = con.prepareStatement(selectSql);
            stmt.setString(1, sequenceName);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new SequenceException("can not find the record, name=" + sequenceName);
            }
            long min = rs.getLong(minValueColumnName);
            long max = rs.getLong(maxValueColumnName);
            int step = rs.getInt(innerStepColumnName);
            if (records.get(sequenceName) == null) {
                Map<String, Object> keyAndValue = new HashMap<String, Object>(0);
                keyAndValue.put(minValueColumnName, min);
                keyAndValue.put(maxValueColumnName, max);
                keyAndValue.put(innerStepColumnName, step);
                records.put(sequenceName, keyAndValue);
            }
        } catch (SQLException e) {
            throw new SequenceException("ERROR ## get the sequence record failed,name="
                                        + sequenceName, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (con != null) {
                    con.close();
                    con = null;
                }
            } catch (Exception e) {
                logger.error("ERROR ## close resources has an error", e);
            }
        }
        return records;
    }

    /**
     * �ڵ��߳����Ե�ʱ��ȡ�����Ӻ����������ݿ⽨�����ӣ�
     * 
     * @param dbIndex
     * @return
     */
    public boolean tryToConnectDataBase(int dbIndex) {
        Long beginTime = System.currentTimeMillis();
        boolean isSussessful = true;
        String sql = "select 'x' ";
        Connection con = null;
        Statement stmt = null;
        try {
            con = ds.getConnection();
            stmt = con.createStatement();
            stmt.executeQuery(sql);
            logger.warn("ERROR ## ���߳�" + Thread.currentThread().getName() + "����sql=" + sql
                        + "������У�����Ӹ�����Դ" + dbIndex + "�ɹ�����ʱΪ:"
                        + (System.currentTimeMillis() - beginTime));
        } catch (SQLException e) {
            logger.warn("WARN ## ���߳�" + Thread.currentThread().getName() + "����sql=" + sql
                        + "������У�����Ӹ�����Դ" + dbIndex + "ʧ��,��ʱΪ:"
                        + (System.currentTimeMillis() - beginTime) + "ms", e);
            isSussessful = false;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (con != null) {
                    con.close();
                    con = null;
                }
            } catch (Exception e) {
                logger.error("ERROR ## close resources has an error", e);
            }
        }
        return isSussessful;
    }

    /**
      * �ڹ��ϵ�����Դ�Ͻ��е��߳����ԣ�ÿ��2s����һ��ҵ���߳̽�������״̬,
      * ������ʳɹ����Ǹ�����ԴΪ���ã�����ȥѰ��������������Դ��<br>
      * ���õ��Ƿ�������ʵ�ֵĲ������ơ�
      * 
      * @param index             ����Դ���
      * @param sequenceName      sequence����
      * @param minValue          ��Сֵ
      * @param maxValue          ���ֵ
      * @param innerStep         �ڲ���
      * @param outStep           �ⲽ��
      * @param excludeIndexes    ���β�ѯdb�ų����Ĺ�������Դ��ʶ����
      * @return
      */
    public SequenceRange tryOnFailedDataSource(int index, String sequenceName, long minValue,
                                               long maxValue, int innerStep, int outStep,
                                               List<Integer> excludeIndexes) {

        boolean isTry = System.currentTimeMillis() - lastRetryTime > retryBadDbInterval;
        //����2s��ʱ�������������õ�����������ʱ��ʼ���뵥�߳�����״̬
        if (isTry && lock.tryLock()) {
            try {
                boolean isSussessful = tryToConnectDataBase(index);
                if (!isSussessful) {
                    excludeIndexes.add(index);
                    return null;
                }
                this.isAvailable = true;
                exceptionTimes = 0;
                return tryOnAvailableDataSource(index, sequenceName, maxValue, minValue, outStep,
                    innerStep, excludeIndexes);
            } catch (SequenceException e) {
                logger.warn("WARN ## ���߳�" + Thread.currentThread().getName() + "���Թ�������Դ" + index
                            + "ʱʧ�ܣ�����ȥѰ���������õ�����Դ��");
                excludeIndexes.add(index);
                return null;
            } finally {
                lastRetryTime = System.currentTimeMillis();
                lock.unlock();
            }
        } else {
            excludeIndexes.add(index);
            return null;
        }
    }

    /**
     * ͳ���쳣��������Ϊ���¼��������<br>
     * (1)�������Դ�Ѿ��������ˣ���ֱ�����쳣������ͳ�ƣ�
     * (2)�����ǰʱ��͵�һ���쳣ʱ��δ����ָ����ʱ������ֵ�����쳣�����ۼƼ�1,�����������������
     * (3)�����1���쳣�������������쳣���������쳣��<br>
     * 
     * @param index        ����Դ���
     */
    private synchronized void calculateExceptionTimes(int index) {
        if (!isAvailable) {
            logger.warn("WARN ## ����Դ" + index + " �Ѿ��������ˣ�������ͳ���쳣�����ˣ�");
            return;
        }
        if (exceptionTimes == 0) {
            firstExceptionTime = System.currentTimeMillis();
        }
        long currentTime = System.currentTimeMillis();
        //С��ָ��ʱ�������ۼ��쳣����������쳣��������ĳ����ֵ�ͽ�������Դ��Ϊ�����ã������������¼���
        if (currentTime - this.firstExceptionTime <= timeInterval) {
            ++exceptionTimes;
            logger.warn("WARN ## ����Դ" + index + "��λʱ���ڵ�" + exceptionTimes + "���쳣����ǰʱ�䣺"
                        + getCurrentDateTime(currentTime) + "���״��쳣ʱ�䣺"
                        + getCurrentDateTime(firstExceptionTime) + "��ʱ����Ϊ��"
                        + (currentTime - firstExceptionTime) + "ms.");
            if (exceptionTimes >= allowExceptionTimes) {
                this.isAvailable = false;
                logger.warn("WARN ## ����Դ" + index + "��ʱ��" + getCurrentDateTime(null) + "���߳�");
            }
        } else {
            logger.warn("WARN ## ͳ���쳣����������λʱ����,�ϴε�λʱ�������쳣����Ϊ" + exceptionTimes + "��,���ڿ�ʼ���¼�����");
            this.exceptionTimes = 0;
        }
    }

    /**
     * ��ʼ��sequence ��¼�����db����ڸü�¼������������Դ˽��г�ʼ��
     * 
     * @param index             db��ʶ
     * @param sequenceName      sequence����
     * @param innerStep         �ڲ���
     * @param outStep           �ⲽ��
     * @param minValue          ��Сֵ
     * @param maxValue          ���ֵ
     * @param valueColumnName   ֵ����
     * @throws SequenceException 
     */
    public void initSequenceRecord(int index, String sequenceName, int innerStep, int outStep,
                                   long minValue, long maxValue, String valueColumnName)
                                                                                        throws SequenceException {

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = ds.getConnection();
            stmt = con.prepareStatement(selectSql);
            stmt.setString(1, sequenceName);//sequence����
            rs = stmt.executeQuery();
            int rowNum = 0;
            if (rs.next()) {
                rowNum++;
                long oldValue = rs.getLong(valueColumnName);
                if (!check(index, oldValue, innerStep, outStep)) {
                    if (adjust) {
                        adjustUpdate(con, index, oldValue, sequenceName, innerStep, outStep,
                            minValue, maxValue);
                    } else {
                        throw new SequenceException("ERROR ## ����Դ" + index
                                                    + "�ĳ�ʼֵ������,����db���߿���ajust���أ�");
                    }
                }
            }
            if (rowNum == 0) {
                if (adjust) {//�����ڣ�����������¼
                    this.adjustInsert(con, index, sequenceName, innerStep, outStep, minValue,
                        maxValue);
                } else {
                    throw new SequenceException("ERROR ## ����Դ" + index + "��sequence��Ϊ"
                                                + sequenceName + "�ĳ�ʼֵ������,����db���߿���ajust���أ�");
                }
            }
        } catch (SQLException e) {
            throw new SequenceException("ERROR ## ��ʼ��sequenceʧ��,sequence name=" + sequenceName, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (con != null) {
                    con.close();
                    con = null;
                }
            } catch (Exception e) {
                logger.error("ERROR ## close resources has an error", e);
            }
        }
    }

    /**
     * �����ڳ�ʼ�������У����³����sequenceֵ
     *  ��ʽupdate table_name(default:sequence) set value=? ,gmt_modified=? where name=? and value=?
     * @param index              db��ʶ
     * @param oldValue           old value
     * @param name               sequence��
     * @param innerStep          �ڲ���
     * @param outStep            �ⲽ��
     * @throws SequenceException
     */
    private void adjustUpdate(Connection con, int index, long oldValue, String name, int innerStep,
                              int outStep, long minValue, long maxValue) throws SequenceException {
        long newValue = getAjustValue(index, oldValue, minValue, maxValue, innerStep, outStep,
            name, false);
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(updateSql);
            stmt.setLong(1, newValue);//���º��ֵ
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));//�޸�ʱ��
            stmt.setString(3, name);//sequence����
            stmt.setLong(4, oldValue);//����ǰ��ֵ
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SequenceException("ERROR ## update the record failed!");
            }
        } catch (Exception e) {
            throw new SequenceException("ERROR ## ���� sequence ����,datasouce index=" + index
                                        + ",sequence name=" + name + ",oldvalue=" + oldValue
                                        + ",newValue=" + newValue, e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            } catch (Exception e) {
                logger.error("ERROR ## close resources has an error", e);
            }
        }
    }

    /**
     * ����һ����¼
     * ��ʽ: insert into table_name(default:sequence)(name,value,min_value,max_value,step,gmt_create,gmt_modified) values(?,?,?,?,?,?,?)
     * @param index              db���б�ʶ
     * @param name               sequence ����
     * @param innerStep          �ڲ���
     * @param minValue           ��Сֵ
     * @param maxValue           ���ֵ
     * @throws SQLException 
     * @throws SequenceException 
     * @throws SequenceException
     */
    private void adjustInsert(Connection con, int index, String name, int innerStep, int outStep,
                              long minValue, long maxValue) throws SQLException, SequenceException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(insertSql);
            stmt.setString(1, name);//�ֶ���
            long value = getAjustValue(index, minValue, minValue, maxValue, innerStep, outStep,
                name, false);
            stmt.setLong(2, value);//��ʼֵ
            stmt.setLong(3, minValue);//��Сֵ
            stmt.setLong(4, maxValue);//���ֵ
            stmt.setInt(5, innerStep);//�ڲ���
            stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));//����ʱ��
            stmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));//�޸�ʱ��
            stmt.executeUpdate();
        } catch (SQLException e) {
            //����ס�쳣���жϼ�¼�Ƿ��Ѿ����룬����Ѳ��������쳣
            if (isHaveInserted(con, name)) {
                logger.warn("WARN ## The record has inserted, name=" + name);
            } else {
                throw new SequenceException("ERROR ## the record is not insert to db,name = "
                                            + name, e);
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            } catch (Exception e) {
                logger.error("ERROR ## close resources has an error", e);
            }
        }
    }

    /**
     * �Ƿ�����˸ü�¼
     * 
     * @param sequenceName
     * @return
     */
    private boolean isHaveInserted(Connection con, String sequenceName) {
        boolean isHaveInserted = false;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(selectSql);
            stmt.setString(1, sequenceName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                isHaveInserted = true;
            }
        } catch (SQLException e) {
            logger.warn("WARN ## select the record error,sequence name=" + sequenceName);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            } catch (Exception e) {
                logger.error("ERROR ## close resources has an error", e);
            }

        }
        return isHaveInserted;
    }

    /**
     * ���ȡ�õ�sequence valueֵ�ǲ��ǺϷ�
     * 
     * @param index     ����Դ���б�ʶ
     * @param value     ��ǰsquenceֵ
     * @param innerStep �ڲ���
     * @param outStep   �ⲽ��
     * @return          �Ƿ�Ϸ�
     */
    private boolean check(int index, long value, int innerStep, int outStep) {
        return (value % outStep) == (index * innerStep);
    }

    /**
     * ��ȡ��ǰ��ʱ��ĸ�ʽ���ַ���
     * 
     * @param time
     * @return
     */
    private String getCurrentDateTime(Long time) {
        java.util.Date now;
        if (time != null) {
            now = new java.util.Date(time);
        } else {
            now = new java.util.Date();
        }
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(now);
    }

}
