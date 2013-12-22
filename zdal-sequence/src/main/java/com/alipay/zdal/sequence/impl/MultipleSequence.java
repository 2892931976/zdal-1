/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.alipay.zdal.sequence.impl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.alipay.zdal.sequence.Sequence;
import com.alipay.zdal.sequence.SequenceConstants;
import com.alipay.zdal.sequence.SequenceRange;
import com.alipay.zdal.sequence.exceptions.SequenceException;

/**
 * 
 * �߿��� sequence ����ʵ��,sequence�������ơ���ǰֵ����������Сֵ�����ֵ������ʱ����޸�ʱ������ԡ�
 * �����ֹ����ķ�ʽʵ�ִ�db��ȡ��ǰsequence�Σ�ȡ�껺����������ҵ��ʹ��
 * 
 * @author zhaofeng.wang
 * @version $Id: MultipleSequenceDao.java, v 0.1 2011-12-20 ����05:32:06 zhaofeng.wang Exp $
 */
public class MultipleSequence implements Sequence {
    private static final Logger    logger            = Logger
                                                         .getLogger(SequenceConstants.ZDAL_SEQUENCE_LOG_NAME);
    private final Lock             lock              = new ReentrantLock();

    /** Ĭ�ϲ��� */
    private static final int       DEFAULT_STEP      = 1000;
    /**Ĭ��sequence����Сֵ*/
    private static final long      DEFAULT_MIN_VALUE = 0;
    /**Ĭ��sequence�����ֵ*/
    private static final long      DEFAULT_MAX_VALUE = Long.MAX_VALUE;
    /**�ڲ���*/
    private int                    innerStep         = DEFAULT_STEP;
    /** ��Сֵ*/
    private long                   minValue          = DEFAULT_MIN_VALUE;
    /** ���ֵ*/
    private long                   maxValue          = DEFAULT_MAX_VALUE;
    /**��������*/
    private String                 sequenceName;
    /** sequence ��*/
    private volatile SequenceRange currentRange;
    /** ����Դ�İ�װ��*/
    private MultipleSequenceDao    sequenceDao;

    private volatile boolean       isInitialize      = false;

    public MultipleSequence() {

    }

    /**
     * ��db��������ڼ�¼������£����õĹ��캯��,��ʱ����Ҫ�ٳ�ʼ��
     * @param sequenceDao
     * @param sequenceName    ��������
     * @param innerStep �ڲ���
     * @param minValue  ��Сֵ
     * @param maxValue  ���ֵ
     */

    public MultipleSequence(MultipleSequenceDao sequenceDao, String sequenceName, long minValue,
                            long maxValue, int innerStep) {
        this.sequenceDao = sequenceDao;
        this.sequenceName = sequenceName;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.innerStep = innerStep;
    }

    /**
     * ��ʼ����������Դ��sequence����������ڼ�¼�Ͳ���һ����db
     * 
     * @throws SequenceException
     */
    public void init() throws SequenceException {
        if (isInitialize == true) {
            throw new SequenceException("ERROR ## the MultipleSequence has inited");
        }
        if (sequenceDao == null) {
            throw new IllegalArgumentException("ERROR ## the sequenceDao is null");
        }
        if (sequenceName == null || sequenceName.trim().length() == 0) {
            throw new IllegalArgumentException("ERROR ## the sequenceName is null");
        }
        if (minValue < 0) {
            throw new IllegalArgumentException("ERROR ## the minValue is less than zero");
        }
        if (maxValue < 0) {
            throw new IllegalArgumentException("ERROR ## the maxValue is less than zero");
        }
        //        try {
        //            Thread.sleep(1500L);//sleep1.5s���ȴ�����Դ��ʼ���ɹ���С������.
        //        } catch (InterruptedException e) {
        //            logger.error("ERROR ## interrupted the sleep thread", e);
        //        }
        //��ʼ����sequence
        try {
            sequenceDao.initSequenceRecord(sequenceName, minValue, maxValue, innerStep);
        } catch (SequenceException e) {
            throw e;
        }

        logger.warn("WARN ## init the multipleSequence success,the sequenceName = " + sequenceName);
        this.isInitialize = true;
    }

    /**
     * ��ȡsequenceֵ
     * @see com.taobao.tddl.client.sequence.Sequence#nextValue()
     */
    public long nextValue() throws SequenceException {
        if (this.isInitialize == false) {
            throw new SequenceException("ERROR ## the MultipleSequence is not init");
        }
        if (currentRange == null) {
            lock.lock();
            try {
                if (currentRange == null) {
                    currentRange = sequenceDao.nextRange(sequenceName, minValue, maxValue,
                        innerStep);
                    logger.warn("WARN ## get the sequence range, from " + currentRange.getMin()
                                + " to " + currentRange.getMax() + ",the sequenceName = "
                                + sequenceName);
                }
            } finally {
                lock.unlock();
            }
        }

        long value = currentRange.getAndIncrement();
        if (value == -1) {
            lock.lock();
            try {
                for (;;) {
                    if (currentRange.isOver()) {
                        currentRange = sequenceDao.nextRange(sequenceName, minValue, maxValue,
                            innerStep);
                        logger.warn("WARN ## after over,get the sequence range, from "
                                    + currentRange.getMin() + " to " + currentRange.getMax()
                                    + ",the sequenceName = " + sequenceName);
                    }
                    value = currentRange.getAndIncrement();
                    if (value == -1) {
                        continue;
                    }
                    break;
                }
            } finally {
                lock.unlock();
            }
        }

        if (value < 0) {
            throw new SequenceException("Sequence value overflow, value = " + value);
        }

        return value;
    }

    public MultipleSequenceDao getSequenceDao() {
        return sequenceDao;
    }

    public void setSequenceDao(MultipleSequenceDao sequenceDao) {
        this.sequenceDao = sequenceDao;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String name) {
        this.sequenceName = name;
    }

    public int getInnerStep() {
        return innerStep;
    }

    public void setInnerStep(int innerStep) {
        this.innerStep = innerStep;
    }

    public long getMinValue() {
        return minValue;
    }

    public void setMinValue(long minValue) {
        this.minValue = minValue;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
    }
}
