package com.alipay.zdal.sequence;

import com.alipay.zdal.sequence.exceptions.SequenceException;

/**
 * ����DAO�ӿ�
 *
 * 
 * @author ����
 * @version $Id: SequenceDao.java, v 0.1 2013-4-3 ����01:36:02 Exp $
 */
public interface SequenceDao {
    /**
     * ȡ����һ�����õ���������
     *
     * @param name ��������
     * @return ������һ�����õ���������
     * @throws SequenceException
     */
    SequenceRange nextRange(String name) throws SequenceException;

}
