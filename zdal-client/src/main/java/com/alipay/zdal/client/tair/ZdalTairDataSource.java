/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.tair;

import java.io.File;
import java.io.Serializable;

import com.alipay.cloudengine.kernel.spi.work.ApplicationWorkingAreaAware;
import com.alipay.sofa.common.conf.Configration;
import com.alipay.sofa.service.api.client.ApplicationConfigrationAware;
import com.alipay.zdal.client.exceptions.ZdalClientException;
import com.alipay.zdal.common.Closable;
import com.alipay.zdal.common.Constants;
import com.alipay.zdal.common.lang.StringUtil;
import com.taobao.tair.DataEntry;
import com.taobao.tair.Result;
import com.taobao.tair.TairManager;

/**
 * Zdal ���⹫��������Դ,���ڷ���Tair,��֧�ֶ�̬��������Դ��Ȩ��,������sofa3.<br>
 * ע�⣺
 * 1,ʹ��ǰ�����������appName,appDsName��ֵ�����ҵ���init�������г�ʼ��,configPath,dbmode,zdataconsoleUrl ��sofa3���Զ�����;
 * 2,���ȴ�zdataconsole�л�ȡ������Ϣ�������ȡ�������ٴ�configPath��ȡ������Ϣ.
 * <bean id="testZdalDataSource" class="com.alipay.zdal.client.tair.ZdalTairDataSource" init-method="init" destroy-method="close">
 *  <property name="appDsName" value="appDsName"/>
 * </bean>
 * 3,zdal����Դ��Ҫ��֪zone�����������ͨ���������������õģ���Ҫapp��deploy.sh��jbossctl.sh�ű�����.
 * @author ����
 * @version $Id: ZdalTairDataSource.java, v 0.1 2012-11-17 ����4:08:43 Exp $
 */
public final class ZdalTairDataSource extends AbstractZdalTairDataSource
                                                                        implements
                                                                        Closable,
                                                                        TairManager,
                                                                        ApplicationWorkingAreaAware,
                                                                        ApplicationConfigrationAware {
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

    /** 
     * @see com.alipay.cloudengine.kernel.api.work.ApplicationWorkingAreaAware#setWorkingArea(java.lang.String)
     */
    public void setWorkingArea(String workingArea) {
        if (StringUtil.isBlank(workingArea)) {
            throw new IllegalArgumentException("ERROR ## the workingArea is not set by sofa3");
        }
        if (!workingArea.endsWith(File.separator)) {//����������ļ��ķָ�����β���Ͳ����ļ��ָ���.
            workingArea += File.separator;
        }
        workingArea += Constants.WORKING_PATH_SUFFIX;//��������Դ�����ļ����ڵ���Ŀ¼.
        this.configPath = workingArea;
    }

    /** 
     * @see com.alipay.sofa.service.api.client.ApplicationConfigrationAware#setConfigration(com.alipay.sofa.common.conf.Configration)
     */
    public void setConfigration(Configration configration) {
        this.zdataconsoleUrl = configration.getPropertyValue(Constants.ZDATACONSOLE_SERVICE_URL);
        this.dbmode = configration.getPropertyValue(Constants.DBMODE);
        this.appName = configration.getSysAppName();
    }
    
    public Result<DataEntry> get(int namespace, Serializable key){
    	
    	return null;
    }

	@Override
	public Result<DataEntry> get(int namespace, Serializable key, int expireTime) {
		// TODO Auto-generated method stub
		return null;
	}

}
