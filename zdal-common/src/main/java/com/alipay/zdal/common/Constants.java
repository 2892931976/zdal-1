/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.common;

import java.io.File;

/**
 * 
 * @author ����
 * @version $Id: Constants.java, v 0.1 2013-1-10 ����03:31:24 Exp $
 */
public interface Constants {
    /** config��Ϣ���ʱ����¼��log����. */
    public static final String CONFIG_LOG_NAME_LOGNAME           = "zdal-client-config";

    /** ��ӡzdatasource���ӳ�״̬��log����. */
    public static final String ZDAL_DATASOURCE_POOL_LOGNAME      = "zdal-datasource-pool";

    /** ��ӡzdal�ڲ�ִ�е�״̬log. */
    public static final String ZDAL_MONITOR_LOGNAME              = "zdal-monitor";

    /** zdal-datasource��drm��ԴdataId��com.alipay.zdal.signal.{��ʾ} */
    public static final String ZDALDATASOURCE_DRM_DATAID         = "com.alipay.zdal.signal.{0}";

    /**zdal-document-datasource ���ȵ��̬Ǩ�Ƶ�drm��ԴdataId:com.alipay.zdal.hotspot.{��ʾ}  */
    public static final String ZDALDATASOURCE_HOTSPOT_DRM_DATAID = "com.alipay.zdal.document.{0}";

    /** zdal-datasource ldc��drm��ԴdataId */
    public static final String ZDALDATASOURCE_LDC_DRM_DATAID     = "com.alipay.zdal.ldc.{0}";

    /** zdataconsole��webservice-url��׺. */
    public static final String WERBSERVICE_URL_SUFFIX            = "/services/zdalConfigService";

    /** ��zdataconsole��ȡ���õı���. */
    public static final String CONFIG_ENCODE                     = "gbk";

    /**  ���������ļ��� ���ͣ� DS OR��RULE*/
    public static final int LOCAL_CONFIG_DS      				 = 0;
    
    public static final int LOCAL_CONFIG_RULE      				 = 1;
    
    /**  ���������ļ������Ƹ�ʽ��appName-dbmode-zone-ds.xml*/
    public static final String LOCAL_CONFIG_FILENAME_SUFFIX      = "{0}-{1}-{2}-ds.xml";
    
    /**  ���������ļ������Ƹ�ʽ��appName-dbmode-zone-rule.xml*/
    public static final String LOCAL_RULE_CONFIG_FILENAME_SUFFIX = "{0}-{1}-{2}-rule.xml";

    /** ��sofa3�У�����Դ�����ļ����working��ַ. */
    public static final String WORKING_PATH_SUFFIX               = "config" + File.separator + "db";

    public static final String DBINDEX_DSKEY_CONN_CHAR           = "_";

    public static final String DBINDEX_DS_GROUP_KEY_PREFIX       = "group_";

    /** dbmode ��antx������. */
    public static final String DBMODE                            = "dbmode";

    /**zdataconsole��webservice��url,��Ӧ��antx�������������:zdataconsole.service.url  */
    public static final String ZDATACONSOLE_SERVICE_URL          = "zdataconsole_service_url";

}
