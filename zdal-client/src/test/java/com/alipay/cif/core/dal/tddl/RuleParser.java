/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2010 All Rights Reserved.
 */
package com.alipay.cif.core.dal.tddl;

import com.alibaba.common.lang.StringUtil;

/**
 * TDDL���������
 * 
 * @author jia.hej
 *
 * @version $Id: RuleParser.java, v 0.1 2010-11-3 ����06:55:18 jia.hej Exp $
 */
public class RuleParser {

    /** Ĭ�ϵ��ַ����ָ��� */
    private static String DEFAULT_SEPERATOR = "_";

    /** ������ַ����ָ��� */
    private static String ZERO_STR          = "0";

    //~~~~~~~~~~~~~~~address�ֿ�ֱ����~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * �����û�userId�����ֿ�����
     * <li>tableName=cs_deliver_address
     * 
     * @param userId    �û�id
     * @return
     */
    public static int parseDBIndex(String userId) {
        if (StringUtil.isBlank(userId)) {
            return -1;
        }

        try {
            int index = (Integer.valueOf(StringUtil.substring(userId, 13, 14).toCharArray()[0]) % 10 + 2) % 10;

            if (index >= 0 && index <= 4) {
                return 0;
            } else if (index >= 5 && index <= 9) {
                return 1;
            } else {
                return -1;
            }
        } catch (Exception e) {
            //�����ӡerror
            return -1;
        }
    }

    /**
     * �����û�userId�����ֱ�����
     * <li>tableName=cs_deliver_address
     * 
     * @param userId    �û�id
     * @return
     */
    public static int parseTableIndex(String userId) {
        if (StringUtil.isBlank(userId)) {
            return -1;
        }

        try {
            int dbIndex = (Integer.valueOf(StringUtil.substring(userId, 13, 14).toCharArray()[0]) % 10 + 2) % 10;
            int tableIndex = (Integer
                .valueOf(StringUtil.substring(userId, 14, 15).toCharArray()[0]) % 10 + 2) % 10;
            return dbIndex * 10 + tableIndex;
        } catch (Exception e) {
            return -1;
        }
    }

    //~~~~~~~~~~~~~~~userext�ֿ�ֱ����~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * �����û�userId�����ֿ�����
     * <li>tableName=CS_ACTION_LOG
     * <li>tableName=CS_USER_MEMO
     * 
     * @param userId    �û�id
     * @return
     */
    public static int parseUserextDBIndex(String userId) {
        if (StringUtil.isBlank(userId)) {
            return -1;
        }

        try {
            //userId�ĵ�����3λ�ֿ�
            int index = (Integer.valueOf(StringUtil.substring(userId, 13, 14).toCharArray()[0]) % 10 + 2) % 10;

            if (index >= 0 && index <= 9) {
                return index;
            } else {
                return -1;
            }
        } catch (Exception e) {
            //�����ӡerror
            return -1;
        }
    }

    /**
     * �����û�userId�����ֱ�����
     * <li>tableName=CS_USER_MEMO
     * 
     * @param userId    �û�id
     * @return
     */
    public static int parseUserMemoTableIndex(String userId) {
        if (StringUtil.isBlank(userId)) {
            return -1;
        }

        try {
            //userId�ĵ�����3λ
            int dbIndex = (Integer.valueOf(StringUtil.substring(userId, 13, 14).toCharArray()[0]) % 10 + 2) % 10;
            //userId�ĵ�����2λ
            int tableIndex = (Integer
                .valueOf(StringUtil.substring(userId, 14, 15).toCharArray()[0]) % 10 + 2) % 10;
            return dbIndex * 10 + tableIndex;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * �����û�id��gmtʱ������ֱ�����
     * �磺"2088111122221039" month=6 ���أ�03_05
     * �磺"2088111122221179" month=6 ���أ�17_05
     *  
     * @param userId    �û�id
     * @param calendar  ��ǰ����
     * @return
     */
    public static String parseActionLogTableIndex(String userId, int month) {
        if (StringUtil.isBlank(userId) || month < 0) {
            return null;
        }
        try {
            //userId�ĵ�����3λ
            int dbIndex = (Integer.valueOf(StringUtil.substring(userId, 13, 14).toCharArray()[0]) % 10 + 2) % 10;
            //userId�ĵ�����2λ
            int reSecondIndex = (Integer
                .valueOf(StringUtil.substring(userId, 14, 15).toCharArray()[0]) % 10 + 2) % 10;

            int tableIndex = dbIndex * 10 + reSecondIndex;
            return StringUtil.alignRight(String.valueOf(tableIndex), 2, ZERO_STR)
                   + DEFAULT_SEPERATOR + StringUtil.alignRight(String.valueOf(month), 2, ZERO_STR);
        } catch (Exception e) {
            return null;
        }
    }

    //~~~~~~~~~~~~~~~cifprofile�ֿ�ֱ����~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * �����û�userId�����ֿ�����
     * <li>tableName=cs_secure_info
     * <li>tableName=cs_user_profile
     * 
     * @param userId    �û�id
     * @return
     */
    public static int parseCifprofileDBIndex(String userId) {
        if (StringUtil.isBlank(userId)) {
            return -1;
        }

        try {
            //userId�ĵ�����3λ�ֿ�
            int index = (Integer.valueOf(StringUtil.substring(userId, 13, 14).toCharArray()[0]) % 10 + 2) % 10;

            if (index >= 0 && index <= 9) {
                return index;
            } else {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * �����û�userId�����ֱ�����
     * <li>tableName=cs_secure_info
     * <li>tableName=cs_user_profile
     * @param userId    �û�id
     * @return
     */
    public static int parseCifprofileTableIndex(String userId) {
        if (StringUtil.isBlank(userId)) {
            return -1;
        }

        try {
            //userId�ĵ�����3λ
            int dbIndex = (Integer.valueOf(StringUtil.substring(userId, 13, 14).toCharArray()[0]) % 10 + 2) % 10;
            //userId�ĵ�����2λ
            int tableIndex = (Integer
                .valueOf(StringUtil.substring(userId, 14, 15).toCharArray()[0]) % 10 + 2) % 10;
            return dbIndex * 10 + tableIndex;
        } catch (Exception e) {
            return -1;
        }
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.home"));
        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("java.home"));
    }
}
