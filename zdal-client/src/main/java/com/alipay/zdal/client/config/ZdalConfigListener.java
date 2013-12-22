/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.config;

import java.util.Map;
import java.util.Set;

import com.alipay.zdal.client.scalable.DataSourceBindingChangeException;
import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.scalable.impl.ScaleConnectionPoolException;

/**
 * Zdal client����ʱ����Ҫ��ʼ��DataSource,����DRM�İ汾�źŴ�zdataconsole�л�ȡapp������Դ������Ϣ�����ҳ�ʼ��DataSource<br>
 * ��app����ʱ�����������Ϣ�����仯����Ҫ����zdal client������zdataconsole�л�ȡ������Ϣ�����ұȶԸ������ԣ���̬��������Դ����Ϊ.
 * @author ����
 * @version $Id: ZdalConfigListener.java, v 0.1 2012-11-17 ����4:29:22 Exp $
 */
public interface ZdalConfigListener {

    /**
     * ͨ��drm�����л���Ϣ.
     * @param keyWeights ���͵�ֵ.
     */
    void resetWeight(Map<String, String> keyWeights);

    /**
     * ͨ��drm���Ͷ�̬���������ܷ��ʵ��߼�����Դ.
     * @param zoneDs
     */
    void resetZoneDs(Set<String> zoneDs);

    /**
     * ͨ��drm����·�ɵ��Ǳ�zone������Դ�׳��쳣���Ǽ�¼��־�����throwException=true �����׳��쳣�����throwException=false���Ǽ�¼��־.
     * @param throwException
     */
    void resetZoneDsThreadException(boolean throwException);

    /**
     * Dealing with DRM pushed operation of reset data source bindings between logical data source
     * and physical datasource
     * @throws DataSourceBindingChangeException
     */
    void resetDataSourceBinding(String physicalDbId, String logicalDbId, LocalTxDataSourceDO dataSourceSetting) 
    		throws DataSourceBindingChangeException;
    
    /**
     * Dealing with DRM pushed operation of reset data source connection pool size
     * @throws ScaleConnectionPoolException
     */
    void resetConnectionPoolSize(String physicalDbId, int minSize, int maxSize) throws ScaleConnectionPoolException;
}
