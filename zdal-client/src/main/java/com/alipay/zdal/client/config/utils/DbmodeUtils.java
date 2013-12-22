/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.config.utils;

import com.alipay.zdal.common.lang.StringUtil;

/**
 * ��ȡdbmode��ֵ.
 * @author ����
 * @version $Id: DbmodeUtils.java, v 0.1 2013-2-5 ����11:32:30 Exp $
 */
public class DbmodeUtils {
    /** ���������������ȡdbmode�������� */
    public static final String DBMODE_NAME = "dbmode";

    /**
     * ���ж�dbmode�Ƿ�Ϊ��,���Ϊ��,�ʹӻ��������л�ȡ,������ǿ�,��ֱ���׳��쳣,ת����Сд��ĸ.
     * @param dbmode
     * @return
     */
    public static String getDbmode(String dbmode) {
        if (StringUtil.isNotBlank(dbmode)) {
            return dbmode.toLowerCase();
        }
        String tmpDbmode = System.getProperty(DBMODE_NAME);
        if (StringUtil.isBlank(tmpDbmode)) {
            throw new IllegalArgumentException("ERROR ## the dbmode is null");
        }
        return tmpDbmode.toLowerCase();
    }

}
