/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.log4j.Logger;
import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import org.codehaus.xfire.transport.http.CommonsHttpMessageSender;

import com.alipay.zdal.client.config.exceptions.ZdalConfigException;
import com.alipay.zdal.client.config.utils.ZdalConfigParserUtils;
import com.alipay.zdal.common.Constants;
import com.alipay.zdataconsole.zdal.service.ZdalConfigService;

/**
 * Ĭ�ϵ�����Դ������Ϣ������.
 * @author ����
 * @version $Id: DefaultZdalConfigAdapter.java, v 0.1 2012-11-17 ����4:06:38 Exp $
 */
public final class ZdalConfigAdapter {
    private static final Logger                         log          = Logger
                                                                         .getLogger(Constants.CONFIG_LOG_NAME_LOGNAME);

    private static ExecutorService                      executor     = Executors
                                                                         .newFixedThreadPool(1,
                                                                             new ThreadFactory() {

                                                                                 @Override
                                                                                 public Thread newThread(
                                                                                                         Runnable r) {
                                                                                     return new Thread(
                                                                                         r,
                                                                                         "sync-config-thread");
                                                                                 }
                                                                             });

    /** ���app����������Դ��������Ϣ��key=appName  value=(key = appDsName,value = zdalconfig )*/
    private static Map<String, Map<String, ZdalConfig>> appDsConfigs = new HashMap<String, Map<String, ZdalConfig>>();

    /** ���ش�Ŵ�zdataconsole�л�ȡ������. */
    public static String                               LOCAL_CONFIG_PATH;
    
    static {
        LOCAL_CONFIG_PATH = System.getProperty("user.home");
        if (!LOCAL_CONFIG_PATH.endsWith(File.separator)) {//����������ļ��ķָ�����β���Ͳ����ļ��ָ���.
            LOCAL_CONFIG_PATH += File.separator;
        }
        LOCAL_CONFIG_PATH += "conf" + File.separator + "zdal";
    }

    /**
     * ���zdataconsoleUrl��ֵ��Ϊ��,�����ȴ�zdataconsole�л�ȡ����,��ȡʧ���ٴӱ��������ļ�����;
     * ���zdataconsoleUrl��ֵΪ��,��ֱ�Ӵӱ��������ļ��м���;
     * @param appName
     * @param appDsName
     * @param dbmode
     * @param idcName
     * @param zdataconsoleUrl
     * @param configPath
     * @return
     * @throws ZdalConfigException
     */
    public static synchronized ZdalConfig getConfig(String appName, String appDsName,
                                                    String dbmode, String idcName,
                                                    String zdataconsoleUrl, String configPath)
                                                                                              throws ZdalConfigException {
        Map<String, ZdalConfig> configs = appDsConfigs.get(appName);
        if (configs == null || configs.isEmpty()) {
            Map<String, ZdalConfig> maps = adapterConfig(appName, dbmode, idcName, zdataconsoleUrl,
                configPath, false);
            appDsConfigs.put(appName, maps);
            return maps.get(appDsName);
        } else {
            return configs.get(appDsName);
        }
    }

    public static synchronized ZdalConfig getConfig(String appName, String appDsName,
                                                    String dbmode, String idcName,
                                                    String zdataconsoleUrl, 
                                                    String configPath, boolean loadOnlyFromLocal)
                                                                                              throws ZdalConfigException {
        Map<String, ZdalConfig> configs = appDsConfigs.get(appName);
        if (configs == null || configs.isEmpty()) {
            Map<String, ZdalConfig> maps = adapterConfig(appName, dbmode, idcName, zdataconsoleUrl,
                configPath, loadOnlyFromLocal);
            appDsConfigs.put(appName, maps);
            return maps.get(appDsName);
        } else {
            return configs.get(appDsName);
        }
    }
    /**
     * ��zdataconsoleΪ��ʱ,ֱ�Ӵӱ��ؼ�������,������ȴ�zdataconsole�м���,�ڼ���ʧ���Ժ�,�ٴӱ��ؼ���.
     * @param appName
     * @param dbmode
     * @param idcName
     * @param zdataconsoleUrl
     * @param configPath
     * @return
     */
    public static Map<String, ZdalConfig> adapterConfig(final String appName, final String dbmode,
                                                         final String idcName,
                                                         String zdataconsoleUrl,
                                                         final String configPath,
                                                         boolean loadOnlyFromLocal) {
        String configInfo = null;
        if ( loadOnlyFromLocal == true && zdataconsoleUrl != null && zdataconsoleUrl.length() > 0) {
            Service service = new ObjectServiceFactory().create(ZdalConfigService.class);
            XFireProxyFactory factory = new XFireProxyFactory();
            ZdalConfigService configService = null;
            try {
                configService = (ZdalConfigService) factory.create(service,
                    zdataconsoleUrl + Constants.WERBSERVICE_URL_SUFFIX);
                Client client = null;
                for (int i = 0; i < 3; i++) {//����3�Σ�����xfire�ڲ��Ѿ�������3�Σ���ÿ�μ��1��.
                    try {
                        client = Client.getInstance(configService);
                        client.setProperty(CommonsHttpMessageSender.HTTP_TIMEOUT, "5000");//����socket����ͨ�ŵĳ�ʱʱ��
                        /*client.setProperty(CommonsHttpMessageSender.HTTP_CONNECTION_TIMEOUT,
                            "10000");//�ȴ����ӽ�����ʱʱ��
                        client.setProperty(
                            CommonsHttpMessageSender.HTTP_CONNECTION_MANAGER_TIMEOUT, "10000");*///Ĭ�ϵȴ�HttpConnectionManager�������ӳ�ʱ��ֻ���ڴﵽ���������ʱ�����ã�
                        configInfo = configService.getAppDsConfig(appName, dbmode, idcName);//Ĭ�ϻ�����3��.
                        break;
                    } catch (Exception e) {
                        log
                            .warn("WARN ## get the config info from zdataconsole has an error,the appName = "
                                  + appName
                                  + ",the dbmode = "
                                  + dbmode
                                  + ",the idcName = "
                                  + idcName + e.getMessage());
                        try {
                            Thread.sleep(2000L);
                        } catch (InterruptedException e1) {
                            log.warn("WARN ## the thread sleep is interrupted ", e);
                        }
                    } finally {
                        if (client != null) {
                            client.close();
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("WARN ## init the xfire-ws for zdalconfigService has an error", e);
            }
            if (configInfo != null && configInfo.length() > 0) {
            	
                InputStream configStream = null;
                Map<String, ZdalConfig> zdalConfigs = new HashMap<String, ZdalConfig>();
                try {
                    configStream = new ByteArrayInputStream(configInfo
                        .getBytes(Constants.CONFIG_ENCODE));
                    zdalConfigs = ZdalConfigParserUtils.parseConfig(configStream, appName, dbmode,
                        idcName);
                    log.warn("WARN ## get config of [" + appName + "] from zdataconsole success");
                } catch (Exception e) {
                    log.warn("WARN ## parse the config info from zdataconsole has an error", e);
                    configInfo = null;
                } finally {
                    if (configStream != null) {
                        try {
                            configStream.close();
                        } catch (IOException e) {
                            log.error("ERROR ## close configStream has an error:", e);
                        }
                    }
                }
                if (configInfo != null && configInfo.trim().length() > 0) {
                    final String newConfigInfo = configInfo;
                    executor.execute(new Runnable() {

                        public void run() {
                            String configFileName = MessageFormat.format(
                                Constants.LOCAL_CONFIG_FILENAME_SUFFIX, appName, dbmode, idcName);
                            File configDir = new File(LOCAL_CONFIG_PATH);
                            if (!configDir.exists()) {
                                configDir.mkdirs();
                            }
                            File configFile = new File(LOCAL_CONFIG_PATH, configFileName);
                            try {
                                if (configFile.exists()) {
                                    configFile.delete();//��ɾ��ԭ�����ļ�.
                                    configFile = null;
                                    configFile = new File(LOCAL_CONFIG_PATH, configFileName);
                                    configFile.createNewFile();
                                } else {
                                    configFile.createNewFile();
                                }
                                Writer writer = null;
                                try {
                                    writer = new FileWriter(configFile);
                                    writer.write(newConfigInfo);
                                    log
                                        .warn("WARN ## sync the newest config to local file success,the appName = "
                                              + appName
                                              + ",the dbmode = "
                                              + dbmode
                                              + ",the idcName = " + idcName);
                                } catch (Exception e) {
                                    log.error(
                                        "ERROR ## flush config from zdataconsole to local file has an error,the fileName = "
                                                + configFileName, e);
                                } finally {
                                    if (writer != null) {
                                        writer.close();
                                    }
                                }
                            } catch (Exception e) {
                                log.error(
                                    "ERROR ## sync the newest config to local file has an error,the appName = "
                                            + appName + ",the dbmode = " + dbmode
                                            + ",the idcName = " + idcName, e);
                            }

                        }
                    });
                }
                return zdalConfigs;
            }
        }
        //�ӱ��������ļ��м���.
        if (configInfo == null || configInfo.length() == 0) {
            String configFileName = MessageFormat.format(Constants.LOCAL_CONFIG_FILENAME_SUFFIX,
                appName, dbmode, idcName);
            
            File configFile = new File(LOCAL_CONFIG_PATH, configFileName);//�߳�/home/admin/conf/zdalĿ¼�²���.
            if (!configFile.exists()) {//��������ڣ��ʹ�-run/work/.../config/dbĿ¼�²���.
                log.warn("WARN ## the configFile = " + configFile.getAbsolutePath()
                         + " not exists,the zdataconsoleUrl = " + zdataconsoleUrl);
                configFile = new File(configPath, configFileName);
                if (!configFile.exists()) {//����������ھ�ֱ���׳��쳣��˵������ʧ��.
                    throw new ZdalConfigException("WARN ## the configFile = "
                                                  + configFile.getAbsolutePath()
                                                  + " not exists ,the zdataconsoleUrl = "
                                                  + zdataconsoleUrl);
                }
            }
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(configFile);
                Map<String, ZdalConfig> zdalConfigs = ZdalConfigParserUtils.parseConfig(
                    inputStream, appName, dbmode, idcName);
                log.warn("WARN ## get config of[" + appName + "] from local config file success");
                return zdalConfigs;
                
            } catch (Exception e) {
                throw new ZdalConfigException(
                    "WARN ## parse the config info from local file has an error,the appName = "
                            + appName + ",the dbmode = " + dbmode + ",the idcName = " + idcName, e);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        //
                    }
                }
            }
        }
        throw new ZdalConfigException("ERROR ## can not get config ofthe appName = " + appName
                                      + ",the dbmode = " + dbmode + ",the idcName = " + idcName);
    }

    public static void close() {
        if (executor != null) {
            executor.shutdown();
        }
    }
		    
}