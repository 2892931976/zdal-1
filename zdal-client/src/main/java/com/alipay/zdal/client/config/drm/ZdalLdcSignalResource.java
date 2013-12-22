/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.config.drm;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.alipay.drm.client.api.annotation.AfterUpdate;
import com.alipay.drm.client.api.annotation.DAttribute;
import com.alipay.drm.client.api.annotation.DResource;
import com.alipay.zdal.client.config.ZdalConfigListener;
import com.alipay.zdal.common.Constants;
import com.alipay.zdal.common.lang.StringUtil;

/**
 * zdalΪ��֧��ldc��zone���ʵ��߼�����Դ����.
 * @author ����
 * @version $Id: ZdalLdcSignalResource.java, v 0.1 2013-5-15 ����09:30:06 Exp $
 */
@DResource(id = "com.alipay.zdal.ldc")
public class ZdalLdcSignalResource {
    /** ר�Ŵ�ӡ���ͽ����log��Ϣ. */
    private static final Logger log            = Logger
                                                   .getLogger(Constants.CONFIG_LOG_NAME_LOGNAME);

    /** ���Ƶ�drmֵ. */
    private static final String DEFAULT_ZONEDS = "null";

    private String              resourceId;

    /** ��zone֧�ֵ��߼�����Դ. */
    @DAttribute
    private String              zoneDs;

    /** ·�ɵ��Ǳ�zone������Դ���׳��쳣���Ǽ�¼��־,���throwException=true �����׳��쳣�����throwException=false���Ǽ�¼��־. */
    @DAttribute
    private String              throwException;

    private ZdalConfigListener  configListener;

    private Lock                lock           = new ReentrantLock();

    public ZdalLdcSignalResource(ZdalConfigListener configListener, String ldcDsDrm) {
        this.resourceId = MessageFormat.format(Constants.ZDALDATASOURCE_LDC_DRM_DATAID, ldcDsDrm);
        this.configListener = configListener;
    }

    public ZdalLdcSignalResource() {
    }

    @AfterUpdate
    public void updateResource(String key, Object value) {
        lock.lock();
        try {

            if (key.equals("zoneDs")) {
                if (value == null || StringUtil.isBlank(value.toString())
                    || value.toString().equalsIgnoreCase(DEFAULT_ZONEDS)) {
                    configListener.resetZoneDs(new HashSet<String>());
                    return;
                }
                Set<String> zoneDses = convertZoneDs(value.toString());
                configListener.resetZoneDs(zoneDses);
                this.zoneDs = value.toString();
            }
            if (key.equals("throwException")) {
                if (value == null || StringUtil.isBlank(value.toString())) {
                    log.warn("WARN ## the throwExceptoin is null,will ignore this drm pull");
                    return;
                }
                boolean isThrowException = false;
                try {
                    isThrowException = Boolean.parseBoolean(value.toString());
                    configListener.resetZoneDsThreadException(isThrowException);
                } catch (Exception e) {
                    log.warn("WARN ## " + this.resourceId + ".throwException value = "
                             + value.toString() + " is invalidate,must true or flase.");
                }
                this.throwException = value.toString();
            }
        } catch (Exception e) {
            log.error("ERROR ## ", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * ��ȡ����֧�ֵ�zoneDs�б�.
     * @param zoneDs
     * @return
     */
    private Set<String> convertZoneDs(String zoneDs) {
        String[] splits = zoneDs.split(",");
        Set<String> zoneDses = new HashSet<String>();
        for (String string : splits) {
            zoneDses.add(string);
        }
        return zoneDses;
    }

    public String getZoneDs() {
        return zoneDs;
    }

    public void setZoneDs(String zoneDs) {
        this.zoneDs = zoneDs;
    }

    public String getThrowException() {
        return throwException;
    }

    public void setThrowException(String throwException) {
        this.throwException = throwException;
    }

    public String getResourceId() {
        return resourceId;
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "resourceId: " + this.resourceId;
    }
}
