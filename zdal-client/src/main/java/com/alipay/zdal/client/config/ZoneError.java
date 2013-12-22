/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.config;

/**
 * 
 * @author ����
 * @version $Id: ZoneError.java, v 0.1 2013-1-11 ����11:13:05 Exp $
 */
public enum ZoneError {
    /**zdal���ֱ���׳��쳣.  */
    EXCEPTOIN,
    /** zdal�����¼��־�����Һ���. */
    LOG;
    //        /** zdal������ԣ�����ִ��. */
    //        IGNORE;

    public static ZoneError convert(String zoneError) {
        ZoneError[] errors = values();
        for (ZoneError tmp : errors) {
            if (tmp.toString().equalsIgnoreCase(zoneError)) {
                return tmp;
            }
        }
        throw new IllegalArgumentException("ERROR ## the zoneError = " + zoneError
                                           + " is invalidate");
    }

    public boolean isException() {
        return this.equals(EXCEPTOIN);
    }

    public boolean isLog() {
        return this.equals(LOG);
    }
}
