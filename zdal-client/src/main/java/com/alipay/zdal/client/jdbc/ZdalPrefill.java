/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc;

/**
 * ����Ԥ��zdal�����ģ����Ӧ����ҪԤ�ȣ��Լ�ʵ�ָýӿڣ�Ȼ��ע��ZdalDataSource�У�ʹ�����£�
 * <bean id="testZdalDataSource" class="com.alipay.zdal.client.jdbc.ZdalDataSource" init-method="init" destroy-method="close"> 
 *     <property name="appDsName" value=""/> 
 * </bean>
 * 
 * <bean class="com.alipay.tradecore.common.dal.util.ZdalDsPrefillImpl">  �����ʵ��com.alipay.zdal.client.jdbc.ZdalPrefill�ӿڣ�Ȼ�����ڲ�����ZdalDataSource.prefillZdal����.
        <property name="dataSource" ref="tradecore_tddl" />
    </bean>
 * 
 * zdal�ڲ��ᰴ��LDC�Ĳ�ͬzoneԤ�ȶ�Ӧ����Դ����С������.
 * @author ����
 * @version $Id: ZdalPrefill.java, v 0.1 2013-8-1 ����03:53:42 Exp $
 */
public interface ZdalPrefill {

    /**
     * ҵ���Ԥ�ȹ���ʵ�֣�ע�⣬���Ԥ�ȹ��ܱ���ͬ����ɣ������Ԥ�Ȳ�ͬzone�����ݿ�����;
     * ���Ԥ�ȹ��̳������⣬��Ҫ�׳��쳣.
     */
    void prefill();

}
