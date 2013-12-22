/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc;

import javax.sql.DataSource;

import com.alipay.zdal.client.exceptions.ZdalClientException;
import com.alipay.zdal.common.Closable;
import com.alipay.zdal.common.lang.StringUtil;

/**
 * Zdal ���⹫��������Դ,֧�ֶ�̬��������Դ��������Ϣ���л��ȹ���,������sofa2���߶������е�app.br>
 * ע�⣺
 * 1,ʹ��ǰ�����������appName,appDsName,dbmode,idcName,zdataconsoleUrl��configPath��ֵ�����ҵ���init�������г�ʼ��;
 * 2,���ȴ�zdataconsole�л�ȡ������Ϣ�������ȡ�������ٴ�configPath��ȡ������Ϣ.
 * <bean id="testZdalDataSource" class="com.alipay.zdal.client.jdbc.ZdalDataSource2" init-method="init" destroy-method="close">
 *  <property name="appName" value="appName"/>
 *  <property name="appDsName" value="appDsName"/>
 *  <property name="dbmode" value="${dbmode}"/>
 *  <property name="zdataconsoleUrl" value="${zdataconsole_service_url}"/>
 *  <property name="configPath" value="/home/admin/appName-run/jboss/deploy"/>
 *  <property name="drm" value="drm"/>
 * </bean>
 * 3,zdal����Դ��Ҫ��֪zone�����������ͨ���������������õģ���Ҫapp��deploy.sh��jbossctl.sh�ű�����.
 * 4,drm�����������Ҫ�����л�����Դ��Ȩ�أ���Ҫ�����϶�Ӧ��drm��ʾ.
 * @author ����
 * @version $Id: ZdalDataSource.java, v 0.1 2012-11-17 ����4:08:43 Exp $
 */
public final class ZdalDataSource2 extends AbstractZdalDataSource implements DataSource, Closable {

    public final void init() {
        if (super.inited.get() == true) {
            throw new ZdalClientException("ERROR ## init twice");
        }
        super.checkParameters();
        if (StringUtil.isBlank(zdataconsoleUrl)) {
            throw new IllegalArgumentException("ERROR ## the zdataconsoleUrl is null");
        }
        CONFIG_LOGGER.warn("WARN ## the zdataconsoleUrl = " + zdataconsoleUrl);
        super.initZdalDataSource();
    }

}