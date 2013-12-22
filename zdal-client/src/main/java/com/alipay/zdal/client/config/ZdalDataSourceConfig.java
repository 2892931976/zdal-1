package com.alipay.zdal.client.config;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import com.alipay.zdal.client.config.utils.DbmodeUtils;
import com.alipay.zdal.client.config.utils.ZoneUtils;
import com.alipay.zdal.common.Constants;
import com.alipay.zdal.common.DBType;
import com.alipay.zdal.common.lang.StringUtil;

/**
 * mysql/oracle/tair��������Դ�����ü���.
 * @author ����
 * @version $Id: ZdalDataSourceConfig.java, v 0.1 2013-1-18 ����03:48:28 Exp $
 */
public abstract class ZdalDataSourceConfig {

    /** ר�Ŵ�ӡ���ͽ����log��Ϣ. */
    protected static final Logger CONFIG_LOGGER = Logger
                                                    .getLogger(Constants.CONFIG_LOG_NAME_LOGNAME);

    /**app����  */
    protected String              appName;

    /** ����Դ������. */
    protected String              appDsName     = null;

    /** ���ݿ⻷������,�ȴ�bean�Ĳ��������ȡ,�ٴ�System.getProperty�л�ȡ,�����������,���׳��쳣(��Ĭ��ת����Сд��ĸ). */
    protected String              dbmode;

    /** �߼���������,��System.getProperty�л�ȡ,�����������,������Ĭ�ϵ�gzone(��Ĭ��ת����Сд��ĸ). */
    protected String              zone;

    /** zdataconsole��webservice��ַ. */
    protected String              zdataconsoleUrl;

    /** ���������ļ���ŵ�·��. */
    protected String              configPath;

    /** ����Դ��������Ϣ. */
    protected ZdalConfig          zdalConfig    = null;

    /** ���ڱ�ʾZdalDataSource�Ƿ��ʼ�����.*/
    protected AtomicBoolean       inited        = new AtomicBoolean(false);

    protected DBType              defaultDbType;

    protected DataSourceConfigType      dbConfigType  = null;
    
    /**
    * ����������.
    */
    protected void checkParameters() {
        if (StringUtil.isBlank(appName)) {
            throw new IllegalArgumentException("ERROR ## the appName is null");
        }
        CONFIG_LOGGER.warn("WARN ## the appName = " + this.appName);

        if (StringUtil.isBlank(appDsName)) {
            throw new IllegalArgumentException("ERROR ## the appDsName is null");
        }
        CONFIG_LOGGER.warn("WARN ## the appDsName = " + this.appDsName);

        this.dbmode = DbmodeUtils.getDbmode(this.dbmode);
        CONFIG_LOGGER.warn("WARN ## the dbmode = " + this.dbmode);

        this.zone = ZoneUtils.getZone(this.zone);
        if (StringUtil.isBlank(this.zone)) {
            throw new IllegalArgumentException("ERROR ## the zone is null");
        }
        CONFIG_LOGGER.warn("WARN ## the zone = " + this.zone);

        if (StringUtil.isBlank(configPath)) {
            throw new IllegalArgumentException("ERROR ## the configPath is null");
        }
        CONFIG_LOGGER.warn("WARN ## the configPath = " + this.configPath);
    }

    /**
     * Ӧ��ʹ��ʱ�������ȵ���initZdalDataSource��������ʼ��.
     */
    protected void initZdalDataSource() {
        long startInit = System.currentTimeMillis();
        initDrmListener();
        this.inited.set(true);
        CONFIG_LOGGER.warn("WARN ## init ZdalDataSource [" + appDsName + "] success,cost "
                           + (System.currentTimeMillis() - startInit) + " ms");
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("appName:").append(appName);
        sb.append(" appDsName:").append(appDsName);
        sb.append(" dbmode:").append(dbmode);
        sb.append(" zone:").append(zone);
        sb.append(" zdataconsoleUrl:").append(zdataconsoleUrl);
        sb.append(" configPath:").append(configPath);
        sb.append("}");
        return sb.toString();
    }

    /**
     * ��ʼ��drm��Դ.
     */
    protected abstract void initDrmListener();

    /**
     * ��ʼ��mysql/oracle/tair������Դ.
     */
    protected abstract void initDataSources(ZdalConfig zdalConfig);

    public ZdalConfig getZdalConfig() {
        return zdalConfig;
    }

    //�����get/set��Ӧ�Ĳ�����Ҫ�ڳ�ʼ����ʱ������.

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppDsName() {
        return appDsName;
    }

    public void setAppDsName(String appDsName) {
        this.appDsName = appDsName;
    }

    public String getDbmode() {
        return dbmode;
    }

    public void setDbmode(String dbmode) {
        this.dbmode = dbmode.toLowerCase();
    }

    public String getZdataconsoleUrl() {
        return zdataconsoleUrl;
    }

    public void setZdataconsoleUrl(String zdataconsoleUrl) {
        this.zdataconsoleUrl = zdataconsoleUrl;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone.toLowerCase();
    }

}
