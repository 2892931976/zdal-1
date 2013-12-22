/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.alipay.zdal.sequence.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.alipay.zdal.client.jdbc.ZdalDataSource;
import com.alipay.zdal.datasource.ZDataSource;
import com.alipay.zdal.sequence.SequenceConstants;
import com.alipay.zdal.sequence.SequenceRange;
import com.alipay.zdal.sequence.exceptions.SequenceException;

/**
 * �߿��� sequence ������Դ��������ʵ�ֹ�������<br>
 * 1������Ȩ������ڶ������Դ��ѡ��һ�����л�ȡsequence��<br>
 *    �ڵ����ڣ����ĳ������Դ���ϣ�������������Դ�����ѡ��һ���ṩ����<br>
 *    �����λʱ�����쳣��������һ����ֵ���ͽ�����Ϊ�����ã�����ÿ��2s��һ��ҵ���̶߳����������<br>
 *    ���һ�γɹ�������������Դ��Ϊ����<br>
 * 2������Դ������������sequence���Լ�sequence_log����ֶ����ԣ��ɸ���ҵ������������á�
 *
 * 
 * @author zhaofeng.wang
 * @version $Id: MultipleSequenceDao.java, v 0.1 2011-12-15 ����05:32:06 zhaofeng.wang Exp $
 */
public class MultipleSequenceDao {

    private static final Logger            logger                           = Logger
                                                                                .getLogger(SequenceConstants.ZDAL_SEQUENCE_LOG_NAME);
    /** Ĭ�ϵ�����Դ������Ϊ��֤�߿��ã�������������*/
    private final int                      DEFAULT_DATA_SOURCE_NUMBER       = 2;
    /** ����Դ���� */
    private int                            dataSourceNum                    = DEFAULT_DATA_SOURCE_NUMBER;
    /** ����Դ���б� */
    private List<SequenceDataSourceHolder> dataSourceList                   = new ArrayList<SequenceDataSourceHolder>();
    /**���Դ���*/
    private int                            retryTimes                       = 150;
    /**���Ȩ�� */
    private SequenceWeightRandom           weightRandom;
    /** sequence����Ĭ��ֵ*/
    private static final String            DEFAULT_TABLE_NAME               = "sequence";
    /** ���±�ṹ�ֶ�Ĭ��ֵ
     * �ֱ��ǣ�sequence���ơ�sequence��ǰֵ����ı�š���Сֵ�����ֵ���ڲ���������ʱ���Լ��޸�ʱ��
     */
    private static final String            DEFAULT_NAME_COLUMN_NAME         = "name";
    private static final String            DEFAULT_VALUE_COLUMN_NAME        = "value";
    private static final String            DEFAULT_MIN_VALUE_COLUMN_NAME    = "min_value";
    private static final String            DEFAULT_MAX_VALUE_COLUMN_NAME    = "max_value";
    private static final String            DEFAULT_INNER_STEP_COLUMN_NAME   = "step";
    private static final String            DEFAULT_GMT_CREATE_COLUMN_NAME   = "gmt_create";
    private static final String            DEFAULT_GMT_MODIFIED_COLUMN_NAME = "gmt_modified";

    /** �������ڵı��� Ĭ��Ϊ sequence */
    private String                         tableName                        = DEFAULT_TABLE_NAME;
    /** �洢�������Ƶ����� Ĭ��Ϊ name */
    private String                         nameColumnName                   = DEFAULT_NAME_COLUMN_NAME;
    /**�洢����ֵ������ Ĭ��Ϊ value*/
    private String                         valueColumnName                  = DEFAULT_VALUE_COLUMN_NAME;

    /** ��Сֵ������ Ĭ��Ϊ min_value*/
    private String                         minValueColumnName               = DEFAULT_MIN_VALUE_COLUMN_NAME;
    /** ���ֵ������ Ĭ��Ϊmax_value*/
    private String                         maxValueColumnName               = DEFAULT_MAX_VALUE_COLUMN_NAME;
    /** �ڲ��������� Ĭ��Ϊstep*/
    private String                         innerStepColumnName              = DEFAULT_INNER_STEP_COLUMN_NAME;
    /** ����ʱ�� Ĭ��Ϊgmt_create*/
    private String                         gmtCreateColumnName              = DEFAULT_GMT_CREATE_COLUMN_NAME;
    /** �洢����������ʱ������� Ĭ��Ϊ gmt_modified */
    private String                         gmtModifiedColumnName            = DEFAULT_GMT_MODIFIED_COLUMN_NAME;

    /** �������� adjust Ĭ��true*/
    private Boolean                        adjust                           = true;

    /**
     * ��ѯsequence��¼��sql<br>
     * ��ʽ��select value from sequence where name=? 
     */
    private String                         selectSql;

    /**
     * ����sequence��¼��sql<br>
     * ��ʽ update table_name(default��sequence) set value=? ,gmt_modified=? where name= and value=? 
     */
    private String                         updateSql;

    /**
     * ����sequence��¼��sql<br>
     * ��ʽ: insert into table_name(default:sequence)(name,value,min_value,max_value,step,gmt_create,gmt_modified) values(?,?,?,?,?,?,?)
     */
    private String                         insertSql;

    /**
     * ��ȡdb������sequence��¼��sql<br>
     * ��ʽ��select name,value,min_value,max_value,step from sequence
     */
    private String                         selectAllRecordSql;

    /**
     * ����sequence name��ȡһ��sequence��¼<br>
     * ��ʽ��select value,min_value,max_value,step from sequence where name=? 
     */
    private String                         selectSeqRecordSql;

    /** MultipleSequenceDao�Ƿ��Ѿ���ʼ�� */
    private volatile boolean               isInitialize                     = false;

    /**
     * ��ʼ��multiSequenceDao<br>
     * 1����ȡ����Դ�ĸ�����2������������� 3����ʼ����������Դ��װ����sql�Ȳ��� 4�����������log�⣬���ʼ���첽log��
     * @throws SequenceException
     */
    public void init() {
        if (isInitialize == true) {
            throw new SequenceException("ERROR ## the MultipleSequenceDao has inited");
        }
        //��������Դ����
        dataSourceNum = dataSourceList.size();
        if (dataSourceNum < DEFAULT_DATA_SOURCE_NUMBER) {
            throw new IllegalArgumentException("Ϊ��֤�߿��ã�����Դ�ĸ����������ö�������,size=" + dataSourceNum);
        }
        weightRandom = new SequenceWeightRandom(dataSourceNum);
        logger.warn("��ʼ������,����dataSourceNum=" + dataSourceNum);
        //��ʼ��SequenceDataSourceHolder����Դ��װ����һЩ����
        for (SequenceDataSourceHolder dsHolder : dataSourceList) {
            dsHolder.setParameters(getTableName(), getSelectSql(), getUpdateSql(), getInsertSql(),
                adjust);
        }
        isInitialize = true;
    }

    /**
     * ��ʼ��sequence�ĳ�ʼֵ,ÿ������Դ��Ҫȥ���һ�飬��������ھͲ���һ����¼
     * 
     * @param sequenceName sequence����
     * @throws SequenceException
     */
    public void initSequenceRecord(String sequenceName, long minValue, long maxValue, int innerStep)
                                                                                                    throws SequenceException {
        if (isInitialize == false) {
            throw new SequenceException("ERROR ## please init the MultipleSequenceDao first");
        }
        for (int index = 0; index < dataSourceNum; index++) {
            SequenceDataSourceHolder dsHolder = dataSourceList.get(index);
            dsHolder.initSequenceRecord(index, sequenceName, innerStep, innerStep * dataSourceNum,
                minValue, maxValue, getValueColumnName());
        }
    }

    /**
     * ��ȡ���е�sequence��¼
     * 
     * @return
     * @throws SQLException
     */
    public Map<String, Map<String, Object>> getAllSequenceNameRecord() throws SQLException {
        Map<String, Map<String, Object>> sequenceRecordMap = new HashMap<String, Map<String, Object>>(
            0);
        //��Ϊÿ������Դ��ı� sequence-name�� ��ֵԭ���϶���ͬ
        for (int i = 0; i < dataSourceList.size(); i++) {
            try {
                SequenceDataSourceHolder dsHolder = dataSourceList.get(i);
                sequenceRecordMap = dsHolder.getAllSequenceRecordName(getSelectAllRecord(),
                    getNameColumnName(), getMinValueColumnName(), getMaxValueColumnName(),
                    getInnerStepColumnName());
                break;
            } catch (Exception e) {
                logger.warn("The " + i + "th datasource failed,", e);
                continue;
            }
        }
        return sequenceRecordMap;
    }

    /**
     * ����sequenceName����ȡһ����¼,���ڲ�ѯsequence���ڵ�����.
     * 
     * @return
     * @throws SQLException
     * @throws SequenceException 
     */
    public Map<String, Map<String, Object>> getSequenceRecordByName(String sequenceName)
                                                                                        throws SQLException,
                                                                                        SequenceException {
        if (isInitialize == false) {
            throw new SequenceException("ERROR ## please init the MultipleSequenceDao first");
        }
        Map<String, Map<String, Object>> sequenceRecordMap = new HashMap<String, Map<String, Object>>(
            0);
        //��Ϊÿ������Դ��ı� sequence-name�� ��ֵԭ���϶���ͬ����ֻȡ0��ȿɡ�
        SequenceDataSourceHolder dsHolder = dataSourceList.get(0);
        sequenceRecordMap = dsHolder.getSequenceRecordByName(getSequenceRecordSql(),
            getMinValueColumnName(), getMaxValueColumnName(), getInnerStepColumnName(),
            sequenceName);
        return sequenceRecordMap;
    }

    /**
     *  ��ȡ��һ��sequence ��
     *  @param sequenceName sequence����
     * @see com.taobao.tddl.client.sequence.SequenceDao#nextRange(java.lang.String)
     */
    public SequenceRange nextRange(String sequenceName, long minValue, long maxValue, int innerStep)
                                                                                                    throws SequenceException {
        if (isInitialize == false) {
            throw new SequenceException("ERROR ## please init the MultipleSequenceDao first");
        }
        if (sequenceName == null || sequenceName.trim().length() == 0) {
            throw new IllegalArgumentException("ERROR ## ���б����Ʋ���Ϊ��!");
        }
        for (int i = 0; i < retryTimes; i++) {
            List<Integer> excludeIndexes = new ArrayList<Integer>(0);
            for (int j = 0; j < dataSourceNum; j++) {
                //���ѡ��
                int index = weightRandom.getRandomDataSourceIndex(excludeIndexes);
                if (index == -1) {
                    break;
                }
                SequenceDataSourceHolder dsHolder = dataSourceList.get(index);
                SequenceRange sequenceRange = null;

                sequenceRange = dsHolder.tryOnSelectedDataSource(index, sequenceName, minValue,
                    maxValue, innerStep, innerStep * dataSourceNum, excludeIndexes);

                if (sequenceRange == null) {
                    logger.warn("WARN ## ����ȥȡ sequenceRange����" + (i + 1) + "�γ���!");
                    continue;
                }
                return sequenceRange;
            }
        }
        throw new SequenceException("MultipleSequenceDaoû�п��õ�����Դ��,����Դ����dataSourceNum="
                                    + this.dataSourceNum + ",���Դ���retryTimes=" + this.retryTimes);
    }

    /**��ʽ��select value from table_name(default:sequence) where name=? */
    private String getSelectSql() {
        if (selectSql == null) {
            StringBuilder buffer = new StringBuilder();
            buffer.append("select ").append(getValueColumnName());
            buffer.append(" from ").append(getTableName());
            buffer.append(" where ").append(getNameColumnName()).append(" = ?");
            selectSql = buffer.toString();
        }
        return selectSql;
    }

    /**��ʽ��select value,min_value,max_value,step from sequence where name=? */
    public String getSequenceRecordSql() {
        if (selectSeqRecordSql == null) {
            StringBuilder buffer = new StringBuilder();
            buffer.append("select ").append(getNameColumnName()).append(",");
            buffer.append(this.getValueColumnName()).append(",");
            buffer.append(this.getMinValueColumnName()).append(",");
            buffer.append(this.getMaxValueColumnName()).append(",");
            buffer.append(this.getInnerStepColumnName());
            buffer.append(" from ").append(getTableName());
            buffer.append(" where ").append(getNameColumnName()).append("= ?");
            selectSeqRecordSql = buffer.toString();
        }
        return selectSeqRecordSql;
    }

    /**��ʽ update table_name(default��sequence) set value=? ,gmt_modified=? where name=? and value=? */
    private String getUpdateSql() {
        if (updateSql == null) {
            StringBuilder buffer = new StringBuilder();
            buffer.append("update ").append(getTableName());
            buffer.append(" set ").append(getValueColumnName()).append(" = ?, ");
            buffer.append(getGmtModifiedColumnName()).append(" = ? where ");
            buffer.append(getNameColumnName()).append(" = ? and ");
            buffer.append(getValueColumnName()).append("=?");
            updateSql = buffer.toString();
        }
        return updateSql;
    }

    /**��ʽ: insert into table_name(default:sequence)(name,value,min_value,max_value,step,gmt_create,gmt_modified) values(?,?,?,?,?,?,?) */
    private String getInsertSql() {
        if (insertSql == null) {
            StringBuilder buffer = new StringBuilder();
            buffer.append("insert into ").append(getTableName()).append("(");
            buffer.append(getNameColumnName()).append(",");
            buffer.append(getValueColumnName()).append(",");
            buffer.append(getMinValueColumnName()).append(",");
            buffer.append(getMaxValueColumnName()).append(",");
            buffer.append(getInnerStepColumnName()).append(",");
            buffer.append(getGmtCreateColumnName()).append(",");
            buffer.append(getGmtModifiedColumnName()).append(") values(?,?,?,?,?,?,?);");
            insertSql = buffer.toString();
        }
        return insertSql;
    }

    /**
     * Setter method for property <tt>retryTimes</tt>.
     * 
     * @param retryTimes value to be assigned to property retryTimes
     */
    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    /**
     * Getter method for property <tt>retryTimes</tt>.
     * 
     * @return property value of retryTimes
     */
    public int getRetryTimes() {
        return retryTimes;
    }

    /**
     * Getter method for property <tt>dataSourceList</tt>.
     * 
     * @return property value of dataSourceList
     */
    public List<SequenceDataSourceHolder> getDataSourceList() {
        return dataSourceList;
    }

    /**
     * Setter method for property <tt>dataSourceList</tt>.
     * 
     * @param dataSourceList value to be assigned to property dataSourceList
     */
    public void setDataSourceList(List<DataSource> dataSourceList) {
        if (dataSourceList.size() == 0 || dataSourceList == null) {
            throw new IllegalArgumentException("the dataSourceList is empty!");
        }
        for (DataSource ds : dataSourceList) {
            SequenceDataSourceHolder dsHolder = new SequenceDataSourceHolder(ds);
            this.dataSourceList.add(dsHolder);
        }
    }

    /**
     * ����zdaldatasource����<tt>dataSourceList</tt>
     * @param dataSource
     */
    public void setZdalDataSource(ZdalDataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("ERROR ## the ZdalDataSource is null");
        }
        if (dataSource.getDataSourcesMap() == null || dataSource.getDataSourcesMap().isEmpty()) {
//            throw new IllegalArgumentException("ERROR ## the ZdalDataSource.dataSourceMap is null");
        }
        List<ZDataSource> dses1 = new ArrayList<ZDataSource>();
        for (ZDataSource zDataSource : dataSource.getDataSourcesMap().values()) {
            dses1.add(zDataSource);
        }
        Collections.sort(dses1);
        for (ZDataSource ds : dses1) {
            SequenceDataSourceHolder dsHolder = new SequenceDataSourceHolder(ds);
            this.dataSourceList.add(dsHolder);
        }
    }

    /**��ʽ��select name,value,min_value,max_value,step from table_name(default:sequence) */
    public String getSelectAllRecord() {
        if (selectAllRecordSql == null) {
            StringBuilder buffer = new StringBuilder();
            buffer.append("select ").append(getNameColumnName()).append(",");
            buffer.append(this.getValueColumnName()).append(",");
            buffer.append(this.getMinValueColumnName()).append(",");
            buffer.append(this.getMaxValueColumnName()).append(",");
            buffer.append(this.getInnerStepColumnName());
            buffer.append(" from ").append(getTableName());
            selectAllRecordSql = buffer.toString();
        }
        return selectAllRecordSql;
    }

    /**
     * Getter method for property <tt>tableName</tt>.
     * 
     * @return property value of tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Setter method for property <tt>tableName</tt>.
     * 
     * @param tableName value to be assigned to property tableName
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Getter method for property <tt>nameColumnName</tt>.
     * 
     * @return property value of nameColumnName
     */
    public String getNameColumnName() {
        return nameColumnName;
    }

    /**
     * Setter method for property <tt>nameColumnName</tt>.
     * 
     * @param nameColumnName value to be assigned to property nameColumnName
     */
    public void setNameColumnName(String nameColumnName) {
        this.nameColumnName = nameColumnName;
    }

    /**
     * Getter method for property <tt>valueColumnName</tt>.
     * 
     * @return property value of valueColumnName
     */
    public String getValueColumnName() {
        return valueColumnName;
    }

    /**
     * Setter method for property <tt>valueColumnName</tt>.
     * 
     * @param valueColumnName value to be assigned to property valueColumnName
     */
    public void setValueColumnName(String valueColumnName) {
        this.valueColumnName = valueColumnName;
    }

    /**
     * Getter method for property <tt>gmtModifiedColumnName</tt>.
     * 
     * @return property value of gmtModifiedColumnName
     */
    public String getGmtModifiedColumnName() {
        return gmtModifiedColumnName;
    }

    /**
     * Setter method for property <tt>gmtModifiedColumnName</tt>.
     * 
     * @param gmtModifiedColumnName value to be assigned to property gmtModifiedColumnName
     */
    public void setGmtModifiedColumnName(String gmtModifiedColumnName) {
        this.gmtModifiedColumnName = gmtModifiedColumnName;
    }

    /**
     * Getter method for property <tt>adjust</tt>.
     * 
     * @return property value of adjust
     */
    public Boolean getAdjust() {
        return adjust;
    }

    /**
     * Setter method for property <tt>adjust</tt>.
     * 
     * @param adjust value to be assigned to property adjust
     */
    public void setAdjust(Boolean adjust) {
        this.adjust = adjust;
    }

    /**
     * Getter method for property <tt>gmtCreateColumnName</tt>.
     * 
     * @return property value of gmtCreateColumnName
     */
    public String getGmtCreateColumnName() {
        return gmtCreateColumnName;
    }

    /**
     * Setter method for property <tt>gmtCreateColumnName</tt>.
     * 
     * @param gmtCreateColumnName value to be assigned to property gmtCreateColumnName
     */
    public void setGmtCreateColumnName(String gmtCreateColumnName) {
        this.gmtCreateColumnName = gmtCreateColumnName;
    }

    /**
     * Setter method for property <tt>minValueColumnName</tt>.
     * 
     * @param minValueColumnName value to be assigned to property minValueColumnName
     */
    public void setMinValueColumnName(String minValueColumnName) {
        this.minValueColumnName = minValueColumnName;
    }

    /**
     * Getter method for property <tt>minValueColumnName</tt>.
     * 
     * @return property value of minValueColumnName
     */
    public String getMinValueColumnName() {
        return minValueColumnName;
    }

    /**
     * Setter method for property <tt>maxValueColumnName</tt>.
     * 
     * @param maxValueColumnName value to be assigned to property maxValueColumnName
     */
    public void setMaxValueColumnName(String maxValueColumnName) {
        this.maxValueColumnName = maxValueColumnName;
    }

    /**
     * Getter method for property <tt>maxValueColumnName</tt>.
     * 
     * @return property value of maxValueColumnName
     */
    public String getMaxValueColumnName() {
        return maxValueColumnName;
    }

    /**
    * Getter method for property <tt>innerStepColumnName</tt>.
    * 
    * @return property value of innerStepColumnName
    */
    public String getInnerStepColumnName() {
        return innerStepColumnName;
    }

    /**
     * Setter method for property <tt>innerStepColumnName</tt>.
     * 
     * @param innerStepColumnName value to be assigned to property innerStepColumnName
     */
    public void setInnerStepColumnName(String innerStepColumnName) {
        this.innerStepColumnName = innerStepColumnName;
    }

}
