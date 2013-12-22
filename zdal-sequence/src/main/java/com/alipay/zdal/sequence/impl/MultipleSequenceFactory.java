/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.sequence.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.alipay.zdal.common.lang.StringUtil;


/**
 * �������ݿ����sequence��¼����ʼ����sequence��factory
 * ����sequence name��Ϊkey��factory���ȡ��Ӧ��multipleSequence����
 * Ȼ���� multipleSequence������ͨ���ӿ�nextValue()��ȡsequenceֵ
 * @author zhaofeng.wang
 * @version $Id: MultipleSequenceFactory.java, v 0.1 2012-3-7 ����07:46:46 zhaofeng.wang Exp $
 */
public class MultipleSequenceFactory { 

    private static final Logger              logger              = Logger
                                                                  .getLogger(MultipleSequenceFactory.class);
    private final Lock                    lock                = new ReentrantLock();
    /**
     * ���sequence��keyΪsequence���ƣ�valueΪ��Ӧ��MultipleSequence����
     */
    private Map<String, MultipleSequence> multipleSequenceMap = new ConcurrentHashMap<String, MultipleSequence>();
    /**
     * ��ȡsequence��DAO���������е�factory�ﹲ��ͬһ��DAO
     */
    private MultipleSequenceDao           multipleSequenceDao;

    /**
     * ��ʼ��multipleSequence�Ĺ���
     * ������Դ���ȡsequence�ļ�¼����ÿһ����¼���д������ɶ�Ӧ��multipleSequence���󣬼��ص��ڴ���
     * @throws Exception
     */
    public void init() {
        if (multipleSequenceDao == null) {
            throw new IllegalArgumentException("The sequenceDao is null!");
        }
        initMultipleSequenceMap();
    }

    /**
     * ��ʼ�� multipleSequenceMap,��db��ļ�¼��ʼ����multipleSequenceMap��
     */
    private void initMultipleSequenceMap() {
        Map<String, Map<String, Object>> sequenceRecords = null;
        //��ȡȫ����sequence��¼
        try {
            sequenceRecords = multipleSequenceDao.getAllSequenceNameRecord();
            if (sequenceRecords == null) {
                throw new IllegalArgumentException("ERROR ## The sequenceRecord is null!");
            }
            for (Map.Entry<String, Map<String, Object>> sequenceRecord : sequenceRecords.entrySet()) {
                String seqName = sequenceRecord.getKey().trim();
                Map<String, Object> sequeceRecordvalue = sequenceRecord.getValue();
                long min = (Long) sequeceRecordvalue.get(multipleSequenceDao
                    .getMinValueColumnName());
                long max = (Long) sequeceRecordvalue.get(multipleSequenceDao
                    .getMaxValueColumnName());
                int step = (Integer) sequeceRecordvalue.get(multipleSequenceDao
                    .getInnerStepColumnName());
                MultipleSequence multipleSequence = new MultipleSequence(multipleSequenceDao,
                    seqName, min, max, step);
                try {
                    multipleSequence.init();
                    multipleSequenceMap.put(seqName, multipleSequence);
                } catch (Exception e) {
                    logger.error("ERROR ## init the sequenceName = " + seqName + " has an error:",
                        e);
                }
            }
        } catch (Exception e) {
            logger.error("ERROR ## init the multiple-Sequence-Map failed!", e);
        }
    }

    /**
     * ����sequence name��ʼ��������¼��multipleSequenceMap
     * @param sequenceName sequence name
     * @throws Exception 
     */
    private void initOneMultipleSequenceRecord(String sequenceName) throws Exception {
        Map<String, Map<String, Object>> sequenceRecords = null;
        //��ȡȫ����sequence��¼
        try {
            sequenceRecords = multipleSequenceDao.getSequenceRecordByName(sequenceName);
            if (sequenceRecords == null) {
                throw new IllegalArgumentException("The sequenceRecord is null,sequenceName="
                                                   + sequenceName);
            }
            for (Map.Entry<String, Map<String, Object>> sequenceRecord : sequenceRecords.entrySet()) {
                String seqName = sequenceRecord.getKey().trim();
                Map<String, Object> sequeceRecordvalue = sequenceRecord.getValue();
                long min = (Long) sequeceRecordvalue.get(multipleSequenceDao
                    .getMinValueColumnName());
                long max = (Long) sequeceRecordvalue.get(multipleSequenceDao
                    .getMaxValueColumnName());
                int step = (Integer) sequeceRecordvalue.get(multipleSequenceDao
                    .getInnerStepColumnName());
                MultipleSequence multipleSequence = new MultipleSequence(multipleSequenceDao,
                    seqName, min, max, step);
                multipleSequence.init();
                multipleSequenceMap.put(seqName, multipleSequence);
            }

        } catch (Exception e) {
            logger.error("init the multipleSequenceMap failed!", e);
            throw e;
        }
    }

    /**
     * �ⲿ���ýӿڣ�����sequence name ��ȡsequence value
     * �����sequence��multipleSequenceMap�ﲻ���ڣ���ȥdb���һ���Ƿ���ڣ�
     * ������ھ����ɶ�Ӧ��multipleSequence���󲢼��ص��ڴ棬���򱨴�
     * 
     * @param sequenceName
     * @return
     * @throws Exception 
     */
    public long getNextValue(String sequenceName) throws Exception {
        if (StringUtil.isBlank(sequenceName)) {
            throw new IllegalArgumentException("The sequence name can not be null!");
        }
        MultipleSequence multipleSequence = multipleSequenceMap.get(sequenceName);
        if (multipleSequence != null) {
            return multipleSequence.nextValue();
        } else {
            try {
                lock.lock();
                if (multipleSequenceMap.get(sequenceName) == null) {
                    initOneMultipleSequenceRecord(sequenceName);
                }
                return multipleSequenceMap.get(sequenceName).nextValue();
            } finally {
                lock.unlock();
            }
        }
    }

    /**
    * Setter method for property <tt>sequenceDao</tt>.
    * 
    * @param sequenceDao value to be assigned to property sequenceDao
    */
    public void setMultipleSequenceDao(MultipleSequenceDao sequenceDao) {
        this.multipleSequenceDao = sequenceDao;
    }

    /**
     * Getter method for property <tt>sequenceDao</tt>.
     * 
     * @return property value of sequenceDao
     */
    public MultipleSequenceDao getMultipleSequenceDao() {
        return multipleSequenceDao;
    }
}
