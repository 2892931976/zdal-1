package com.alipay.zdal.sequence;

import com.alipay.zdal.sequence.exceptions.SequenceException;

/**
 * ���нӿ�
 *
 * 
 * @author ����
 * @version $Id: Sequence.java, v 0.1 2013-4-3 ����01:35:50 Exp $
 */
public interface Sequence {
    /**
     * ȡ��������һ��ֵ
     *
     * @return ����������һ��ֵ
     * @throws SequenceException
     */
    long nextValue() throws SequenceException;
}
