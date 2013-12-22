/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.scalable;

import com.alipay.zdal.datasource.LocalTxDataSourceDO;

/**
 * ����֧������Դ֮��󶨹�ϵ�ı�Ľӿ�<p>
 * ��������Դ��֧�����ĳ����£�Ϊ�˸��õ�֧��ҵ��Ŀ���Scale up��������Դ��Ϊ�߼�����Դ����������Դ�Ӷ�֧��
 * ��Ӧ�������ݼ��������ݿ������ܸ���Ч͸�����߼�����Դ����һ�ķ�ʽ����������Դ��
 * ���������ݺ���Ҫ�ı�ԭ�еİ󶨹�ϵ(ԭ�еİ󶨵Ĺ�ϵ������Ӧ�ö��õ�app-dbmode-zone-ds.xml)�С�
 * @author xiang.yangx
 *
 */
public interface IDataSourceBindingChangeSupport {

    /**
     * ���°��߼�����Դ����������Դ֮��Ĺ�ϵ
     * @param logicalDbName �߼�����Դ���Ψһ���ƣ������Ƕ�����DS�����ļ��е�����
     * @param phyicalDbName ��������Դ���Ψһ���ƣ������Ƕ�����DS�����ļ��е�����
     * @param phyicalDbParameters ��������Դ�����ò�����֧�������µ�����Դ
     * @throws DataSourceBindingChangeException
     */
    void reBindingDataSource(String phyicalDbName, String logicalDbName,
                             LocalTxDataSourceDO phyicalDbParameters)
                                                                     throws DataSourceBindingChangeException;
}
