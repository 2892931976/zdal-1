/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc;

import java.io.File;

import javax.sql.DataSource;

import com.alipay.cloudengine.kernel.spi.work.ApplicationWorkingAreaAware;
import com.alipay.sofa.common.conf.Configration;
import com.alipay.sofa.service.api.client.ApplicationConfigrationAware;
import com.alipay.zdal.client.config.ZdalConfigurationLoader;
import com.alipay.zdal.client.config.drm.ZdalDrmPushListener;
import com.alipay.zdal.client.exceptions.ZdalClientException;
import com.alipay.zdal.client.scalable.DataSourceBindingChangeException;
import com.alipay.zdal.client.scalable.IDataSourceBindingChangeSupport;
import com.alipay.zdal.common.Closable;
import com.alipay.zdal.common.Constants;
import com.alipay.zdal.common.lang.StringUtil;
import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.ZDataSource;
import com.alipay.zdal.datasource.scalable.ZScalableDataSource;
import com.alipay.zdal.datasource.scalable.impl.ScaleConnectionPoolException;

/**
 * Zdal ���⹫��������Դ,֧�ֶ�̬��������Դ��������Ϣ���л��ȹ���,������sofa3.<br>
 * ע�⣺
 * 1,ʹ��ǰ�����������appName,appDsName��ֵ�����ҵ���init�������г�ʼ��,configPath,dbmode,zdataconsoleUrl ��sofa3���Զ�����;
 * 2,���ȴ�zdataconsole�л�ȡ������Ϣ�������ȡ�������ٴ�configPath��ȡ������Ϣ.
 * <bean id="testZdalDataSource" class="com.alipay.zdal.client.jdbc.ZdalDataSource" init-method="init" destroy-method="close">
 *  <property name="appDsName" value="appDsName"/>
 *  <property name="drm" value="drm"/>
 * </bean>
 * 3,zdal����Դ��Ҫ��֪zone�����������ͨ���������������õģ���Ҫapp��deploy.sh��jbossctl.sh�ű�����.
 * 4,drm�����������Ҫ�����л�����Դ��Ȩ�أ���Ҫ�����϶�Ӧ��drm��ʾ.
 * @author ����
 * @version $Id: ZdalDataSource.java, v 0.1 2012-11-17 ����4:08:43 Exp $
 */
public class ZdalDataSource extends AbstractZdalDataSource implements DataSource, Closable,
                                                          ApplicationWorkingAreaAware,
                                                          ApplicationConfigrationAware,
                                                          IDataSourceBindingChangeSupport{

    private static final String ZDAL_CONFIG_LOCAL = "zdalConfigLocal";

    /** �Ƿ�ӱ��ػ�ȡ����.  */
    private boolean             zdalConfigLocal   = false;

    public void init() {
        if (super.inited.get() == true) {
            throw new ZdalClientException("ERROR ## init twice");
        }
        super.checkParameters();
        if (zdalConfigLocal == false) {//��Զ��zdataconsole�л�ȡ���ã��ͱ�����zdataconsoleUrl.
            if (StringUtil.isBlank(zdataconsoleUrl)) {
                throw new IllegalArgumentException(
                    "ERROR ## the zdalConfigLocal = false,but zdataconsoleUrl is null");
            }
        } else {
            zdataconsoleUrl = null;
        }
        CONFIG_LOGGER.warn("WARN ## the zdataconsoleUrl = " + zdataconsoleUrl);
        try {
            this.zdalConfig = ZdalConfigurationLoader.getInstance().getZdalConfiguration(appName, 
            		dbmode, zone, appDsName, zdataconsoleUrl, configPath);
            dbConfigType = zdalConfig.getDataSourceConfigType();
            if (this.zdalConfig == null) {
                throw new ZdalClientException(
                    "ERROR ## Load "+ appName + " and dbMode " + dbmode + " and Zone " +  zone
                + " config is null, please check the config file is exist or not.");
            }
            defaultDbType = zdalConfig.getDbType();
            initDataSources(zdalConfig);
            setZdalConfigListener(new ZdalDrmPushListener(this));
            initDrmListener();
            CONFIG_LOGGER.info("WARN ## init Zdal with zoneDs " + zdalConfig.getZoneDs()
                               + ", the appDsName = " + appDsName);
            this.inited.set(true);
        } catch (Exception e) {
            CONFIG_LOGGER.error("zdal init fail,config:" + this.toString(), e);
            throw new ZdalClientException(e);
        }
    }
    
    public void initializeFromLocal(){
    	if (super.inited.get() == true) {
            throw new ZdalClientException("ERROR ## init twice");
        }
        super.checkParameters();
        if (zdalConfigLocal == false) {//��Զ��zdataconsole�л�ȡ���ã��ͱ�����zdataconsoleUrl.
            if (StringUtil.isBlank(zdataconsoleUrl)) {
                throw new IllegalArgumentException(
                    "ERROR ## the zdalConfigLocal = false,but zdataconsoleUrl is null");
            }
        } else {
            zdataconsoleUrl = null;
        }
        CONFIG_LOGGER.warn("WARN ## the zdataconsoleUrl = " + zdataconsoleUrl);
        try {
            this.zdalConfig = ZdalConfigurationLoader.getInstance().getZdalConfiguration(appName, 
            		dbmode, zone, appDsName, zdataconsoleUrl, configPath);

            if (this.zdalConfig == null) {
                throw new ZdalClientException(
                    "ERROR ## Load "+ appName + " and dbMode " + dbmode + " and Zone " +  zone
                + " config is null, please check the config file is exist or not.");
            }
            defaultDbType = zdalConfig.getDbType();
            initDataSources(zdalConfig);
            initDrmListener();
            CONFIG_LOGGER.info("WARN ## init Zdal with zoneDs " + zdalConfig.getZoneDs()
                               + ", the appDsName = " + appDsName);
            this.inited.set(true);
        } catch (Exception e) {
            CONFIG_LOGGER.error("zdal init fail,config:" + this.toString(), e);
            throw new ZdalClientException(e);
        }
    }
    
    /**
     * Since some of test cases still reference with initV3; thus, keep initV3 only for test purpose.
     */
    public void initV3(){
    	init();
    }
    
    /**
     * ��ҵ�񷽵��ô���Ԥ��.
     */
    public void prefillZdal(ZdalPrefill prefill) {
        if (super.inited.get() == false) {
            throw new ZdalClientException("ERROR ## zdatasource not init");
        }
        CONFIG_LOGGER.warn("WARN ## start to prefill zdal,the appDsName = " + getAppDsName());
        startPrefillZdal();
        try {
            prefill.prefill();
        } catch (Exception e) {
            CONFIG_LOGGER.error("ERROR ## prefill zdal has an error,can be ignore,the zone = "
                                + super.getZone() + " the zoneDs = "
                                + super.getZdalConfig().getZoneDs());
        } finally {
            endPrefillZdal();
        }
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
        try {
            if (configration.getPropertyValue(ZDAL_CONFIG_LOCAL) != null
                && configration.getPropertyValue(ZDAL_CONFIG_LOCAL).trim().length() > 0) {
                this.zdalConfigLocal = Boolean.parseBoolean(configration
                    .getPropertyValue(ZDAL_CONFIG_LOCAL));
            }
        } catch (Exception e) {
            this.zdalConfigLocal = false;
        }
    }

    public boolean isZdalConfigLocal() {
        return zdalConfigLocal;
    }

    public void setZdalConfigLocal(boolean zdalConfigLocal) {
        this.zdalConfigLocal = zdalConfigLocal;
    }

	@Override
	public void reBindingDataSource(String phyicalDbName, String logicalDbName,
			LocalTxDataSourceDO phyicalDbParameters)
			throws DataSourceBindingChangeException {
		if( null == logicalDbName || "".equalsIgnoreCase(logicalDbName) ){
			CONFIG_LOGGER.error("reBindingDataSource failed with an empty logicalDbName.");
			return;
		}
		if( null == phyicalDbName || "".equalsIgnoreCase(phyicalDbName) ){
			CONFIG_LOGGER.error("reBindingDataSource failed with an empty phyicalDbName.");
			return;
		}
		String existedPhyicalDbName = this.getZdalConfig().getMasterLogicPhysicsDsNames().get(logicalDbName);
		if( null == existedPhyicalDbName ){
			CONFIG_LOGGER.error("reBindingDataSource failed with a not exist logicalDbName " + logicalDbName);
		}
		if( null == getZdalConfig().getDataSourceParameters().get(phyicalDbName) && null == phyicalDbParameters ){
			CONFIG_LOGGER.error("reBindingDataSource failed with an empty phyicalDbParameters for DB " + phyicalDbName);
			return;
		}
		if( null == phyicalDbParameters ){
			getZdalConfig().getMasterLogicPhysicsDsNames().put(logicalDbName, phyicalDbName);
		}else{
			try {
				phyicalDbParameters.setDsName(phyicalDbName);
				ZDataSource zDataSource = new ZScalableDataSource(phyicalDbParameters);
				this.getDataSourcesMap().put(phyicalDbName, zDataSource);
				//Put the dataSource in Map first
				getZdalConfig().getMasterLogicPhysicsDsNames().put(logicalDbName, phyicalDbName);
			} catch (Exception e) {
				throw new DataSourceBindingChangeException(logicalDbName, phyicalDbName, 
						phyicalDbParameters, e.getMessage());
			}
		}
	}

	public void resetConnectionPoolSize(String physicalDbId, int poolMinSize, int poolMaxSize){
		if( null == getDataSourcesMap().get(physicalDbId)){
			CONFIG_LOGGER.error("The physical DB " + physicalDbId + " is not existed in current ZdalDataSource.");
			return ;
		}
		ZDataSource dataSource = getDataSourcesMap().get(physicalDbId);
		if( dataSource instanceof ZScalableDataSource ){
			try {
				((ZScalableDataSource)dataSource).resetConnectionPoolSize(poolMinSize, poolMaxSize);
			} catch (ScaleConnectionPoolException e) {
				CONFIG_LOGGER.error("ZdalDataSource failed to reset connection pool size for physical DB " + physicalDbId, e);
			}
		}
	}
}