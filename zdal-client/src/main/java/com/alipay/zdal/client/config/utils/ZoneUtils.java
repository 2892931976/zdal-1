/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.config.utils;

import com.alipay.zdal.common.lang.StringUtil;

/**
 * ά��ldc-zone������zdataconsole�����õ�zone���ƵĶ�Ӧ��ϵ.
 * @author ����
 * @version $Id: ZoneUtils.java, v 0.1 2013-2-5 ����11:13:10 Exp $
 */
public final class ZoneUtils {

    /** ���������������ȡldc�������� */
    public static final String ZONE_NAME          = "com.alipay.ldc.zone";

    /** Ĭ�ϵ�ldc. */
    public static final String DEFAULT_ZONE_VALUE = "gz00a";

    //    /** ���е�ldc��һ��ӳ���ϵ,ȫ������Сд��ĸ. */
    //    private static final Map<String, String> zoneMap            = new HashMap<String, String>();
    //
    //    static {
    //        //gzone
    //        zoneMap.put("gz00a", "gz00");
    //        zoneMap.put("gz00b", "gz00");
    //        //Ԥ���������ú�gzone��һ����.
    //        zoneMap.put("gz99p", "gz00");
    //        //rzone
    //        zoneMap.put("rz00a", "rz00");//���Ի�����zone.
    //        zoneMap.put("rz00b", "rz00");
    //        zoneMap.put("rz01a", "rz01");
    //        zoneMap.put("rz01b", "rz01");
    //        zoneMap.put("rz02a", "rz02");
    //        zoneMap.put("rz02b", "rz02");
    //        zoneMap.put("rz03a", "rz03");
    //        zoneMap.put("rz03b", "rz03");
    //        zoneMap.put("rz04a", "rz04");
    //        zoneMap.put("rz04b", "rz04");
    //        zoneMap.put("drz00a", "drz00a");
    //        zoneMap.put("drz01a", "drz01a");
    //        zoneMap.put("drz02a", "drz02a");
    //    }

    /**
     * �ȴӻ��������л�ȡzone�����û�У���ȡĬ��ֵ,ת����Сд.
     * @return
     */
    public static String getZone(String zone) {
        if (StringUtil.isNotBlank(zone)) {//zdal�Ĳ��Դ�����Ҫ�Լ����õ�ʱ��,ֱ�ӷ���.
            return zone.toLowerCase();
        }
        //        return DEFAULT_ZONE_VALUE.toLowerCase();//zone�ĸ�����zdal���������.
        String tmpZone = System.getProperty(ZONE_NAME);//�ȴӻ��������л�ȡ
        if (StringUtil.isBlank(tmpZone)) {
            return DEFAULT_ZONE_VALUE.toLowerCase();//ȡĬ�ϵ�zone=gz00a.
        }
        return tmpZone.toLowerCase();
    }
}
