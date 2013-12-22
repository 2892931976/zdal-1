package com.alipay.zdal.common.monitor;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Ϊ�˲�����־��ӡ��stdout����⼸���������common-monitor ��jar��Ǩ�Ƴ���
 * @author zhaofeng.wang
 * @version $Id: ValueObject.java,v 0.1 2012-9-12 ����09:57:50 zhaofeng.wang Exp $
 */
public class ValueObject {
    public static final int               NUM_VALUES = 2;

    private final AtomicReference<long[]> values     = new AtomicReference<long[]>();

    public ValueObject() {
        long[] init = new long[NUM_VALUES];
        this.values.set(init);
    }

    public ValueObject(long value1, long value2) {
        this();
        addCount(value1, value2);
    }

    public void addCount(long value1, long value2) {
        long[] current;
        long[] update = new long[NUM_VALUES];
        do {
            current = values.get();
            update[0] = current[0] + value1;
            update[1] = current[1] + value2;
        } while (!values.compareAndSet(current, update));

    }

    /**
     * Should only be used by log writer to deduct written counts.
     * This method does not affect stat rules.
     */
    void deductCount(long value1, long value2) {
        long[] current;
        long[] update = new long[NUM_VALUES];
        do {
            current = values.get();
            update[0] = current[0] - value1;
            update[1] = current[1] - value2;
        } while (!values.compareAndSet(current, update));
    }

    /**
     * @deprecated �ֱ��ȡvalue1��value2�����޷���֤���ߵ�һ����
     * @see getValues()
     */
    public long getValue1() {
        return values.get()[0];
    }

    /**
     * @deprecated �ֱ��ȡvalue1��value2�����޷���֤���ߵ�һ����
     * @see getValues()
     */
    public long getValue2() {
        return values.get()[1];
    }

    /**
     * ԭ�ӻ���ȡ����ֵ
     */
    public long[] getValues() {
        return values.get();
    }
}
