/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.datasource.oci;

import java.sql.Driver;

/**
 * ֧��cloundengine��oracle-oci����£���ȡdriver.
 * @author ����
 * @version $Id: ZDataSourceOciDriver.java, v 0.1 2013-1-27 ����01:20:16 Exp $
 */
public class ZDataSourceOciDriver {
    /**
     * ֧��cloundengine��oracle-oci����£���ȡdriver.
     * @param driverClass
     * @param url
     * @return
     * @throws ZDataSourceOciDriverException
     */
    public static Driver getDriver(final String driverClass, final String url)
                                                                              throws ZDataSourceOciDriverException {
        if (driverClass == null || driverClass.length() == 0) {
            throw new ZDataSourceOciDriverException("ERROR ## the driverClass is null");
        }
        if (url == null || url.length() == 0) {
            throw new ZDataSourceOciDriverException("ERROR ## the url is null");
        }

        try {
            Class<?> clazz = Class.forName(driverClass, true, Thread.currentThread()
                .getContextClassLoader());
            return (Driver) clazz.newInstance();
        } catch (Exception e) {
            throw new ZDataSourceOciDriverException("ERROR ## Failed to register driver for: "
                                                    + driverClass, e);
        }
    }
}
