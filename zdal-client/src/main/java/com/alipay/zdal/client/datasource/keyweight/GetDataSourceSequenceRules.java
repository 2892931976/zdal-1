/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.alipay.zdal.client.datasource.keyweight;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alipay.zdal.client.ThreadLocalString;
import com.alipay.zdal.client.util.TableSuffixGenerator;
import com.alipay.zdal.client.util.ThreadLocalMap;
import com.alipay.zdal.common.Constants;
import com.alipay.zdal.common.RuntimeConfigHolder;

/**
 * ��������Դ��������������failover��֮��ѡ��һ���⣬ҵ��ϵͳ���ڸÿ��ϻ�ȡsequence 
 * @author zhaofeng.wang
 * @version $Id: GetDataSourceSequenceRules.java, v 0.1 2011-9-24 ����12:08:59 zhaofeng.wang Exp $
 */
public class GetDataSourceSequenceRules {
    private static Log                                                       logger                      = LogFactory
                                                                                                             .getLog(GetDataSourceSequenceRules.class);

    /**
     * ����ʱ����,��Ҫ�������Դ�ķ����ʶ�Լ���Ӧ�����ڸ�������Դ��Ȩ����Ϣ��
     */
    private static final RuntimeConfigHolder<ZdalDataSourceKeyWeightRumtime> keyWeightRuntimeConfigHoder = new RuntimeConfigHolder<ZdalDataSourceKeyWeightRumtime>();

    public GetDataSourceSequenceRules() {

    }

    /**
     * ע�⣺�������ֻ�ṩ��trade,tradecore,tradequery��failover����ʹ��.
     * �������ֲ�����ѡ������Դ��
     * 1. ��һ����ֻ����������⣬
     * 2. �ڶ����Ǹ���Ȩ���������failover��֮�����ѡ��һ����,���еڶ��ֲ����ְ������ֿ���:
     *    (1)ҵ��ϵͳ���������ֻʹ�����⣬ֻ����������ϵ�����£���ȥʹ��failover�⣬���ڷ���������Ϻ�ͨ��
     *     configserver����Ȩ�صĻ����˹����л���failover��,������û�л�֮ǰ��ϵͳ���ǻ᷵�����⣬ҵ���һֱ����
     *     ֱ���л��ɹ�;
     *    (2)���������,ҵ��ϵͳ��֧���������failover��֮�����Ȩ�����ѡ��һ��ʹ��
     * Ŀǰ��ȡ�Ĳ�����2(1).
     * @param groupNum ��������Դ��ĳһ������
     * @return
     */
    @SuppressWarnings("unchecked")
    public static int getDataSourceKeyOrderNum(int groupNum) {
        int orderNum = 0;

        Map<String, ZdalDataSourceKeyWeightRandom> keyWeightMapHolder = keyWeightRuntimeConfigHoder
            .get().getKeyWeightMapHolder();
        if (groupNum >= keyWeightMapHolder.size() || groupNum < 0) {
            throw new IllegalArgumentException("The groupNum is " + groupNum
                                               + ", but the biggest number is "
                                               + (keyWeightMapHolder.size() - 1));
        }
        //String groupNumKey = StringUtil.alignRight(String.valueOf(groupNum), 2, '0');
        //TODO V3
        int convertGrouNum = TableSuffixGenerator.trade50ConvertGroupNum(groupNum);
        String groupNumKey = TableSuffixGenerator.getTableSuffix(convertGrouNum, keyWeightMapHolder
            .size());

        ZdalDataSourceKeyWeightRandom keyWeightRandom = keyWeightMapHolder
            .get(Constants.DBINDEX_DS_GROUP_KEY_PREFIX + groupNumKey);
        if (keyWeightRandom == null) {
            throw new IllegalArgumentException("The group_" + groupNumKey
                                               + " is not in the keyWeightMapHolder!");
        }
        //��ȡ�����threadlocal����autoCommit��ֵ
        boolean autoCommit = (Boolean) ThreadLocalMap
            .get(ThreadLocalString.GET_AUTOCOMMIT_PROPERTY);

        int orderInGroup = -1;
        //���autoCommit����Ϊtrue�����ʾδ�������У�
        if (autoCommit) {
            orderInGroup = keyWeightRandom.select();
        } else {
            //�������з�Ϊ�����������һ�μ����������棬�ڶ���ֱ�ӷ��أ�
            Map<String, Integer> map = (Map<String, Integer>) ThreadLocalMap
                .get(ThreadLocalString.GET_DB_ORDER_IN_GROUP);
            if (map == null) {
                orderInGroup = keyWeightRandom.select();
                Map<String, Integer> groupAndOrder = new HashMap<String, Integer>();
                groupAndOrder.put("groupNum", groupNum);
                groupAndOrder.put("orderNum", orderInGroup);
                ThreadLocalMap.put(ThreadLocalString.GET_DB_ORDER_IN_GROUP, groupAndOrder);
            } else {
                //�������ͬһ���鲢�һ����˽����ֱ�ӷ���
                if (map.get("groupNum") != groupNum) {
                    throw new IllegalArgumentException(
                        "The groupNum is different from the last one in the transaction,the groupNum="
                                + groupNum + ",the last one is " + map.get("groupNum") + ".");
                }
                orderInGroup = map.get("orderNum");
                if (logger.isDebugEnabled()) {
                    logger.debug("Use the orderInGroup=" + orderInGroup + " in the threadLocal!");
                }
            }
        }

        if (logger.isDebugEnabled()) {
            logger
                .debug("Select the " + orderInGroup + "th datasource in the group_" + groupNumKey);
        }

        if (orderInGroup < 0 || orderInGroup >= keyWeightRandom.getDataSourceNumberInGroup()) {
            throw new IllegalArgumentException("The order number in group_" + groupNumKey + " is "
                                               + orderInGroup + ", but the biggest number is "
                                               + (keyWeightRandom.getDataSourceNumberInGroup() - 1));
        }

        //ͳ��0~groupNum-1��Щ���ڵ�����Դ���ܵĸ���
        for (int i = 0; i < groupNum; i++) {
            int tempGroupNum = TableSuffixGenerator.trade50ConvertGroupNum(i);
            String groupNumKey2 = TableSuffixGenerator.getTableSuffix(tempGroupNum,
                keyWeightMapHolder.size());
            if (keyWeightMapHolder.get(Constants.DBINDEX_DS_GROUP_KEY_PREFIX + groupNumKey2) != null) {

                int number = keyWeightMapHolder.get(
                    Constants.DBINDEX_DS_GROUP_KEY_PREFIX + groupNumKey2)
                    .getDataSourceNumberInGroup();
                if (number <= 0) {
                    throw new IllegalArgumentException("The datasource number in the group_"
                                                       + groupNumKey2 + " is illegal, it is"
                                                       + number);
                }
                orderNum += number;
            } else {
                throw new IllegalArgumentException("The key of group_" + groupNumKey2
                                                   + "does not exist in the keyWeightMapHolder!");
            }
        }
        return orderNum + orderInGroup;
    }

    /**
     * ע�⣺�������ֻ�ṩ��trade,tradecore,tradequery��failover����ʹ��.
     * �޸�tradeϵͳfailover�������⣬������ѯ�ж���������ȫ�ҵ����������ѯȫ����ʧ�ܡ�
     * �������������Բ�һ����ֻ�����õ�ǰ��Ȩ���������ѡ��һ��dbIndex���ظ�ҵ��
     * @param groupNum
     * @return
     */
    public static int getDbIndexOrderNum(int groupNum) {
        int orderNum = 0;
        Map<String, ZdalDataSourceKeyWeightRandom> keyWeightMapHolder = keyWeightRuntimeConfigHoder
            .get().getKeyWeightMapHolder();
        if (groupNum >= keyWeightMapHolder.size() || groupNum < 0) {
            throw new IllegalArgumentException("The groupNum is " + groupNum
                                               + ", but the biggest number is "
                                               + (keyWeightMapHolder.size() - 1));
        }
        int convertGrouNum = TableSuffixGenerator.trade50ConvertGroupNum(groupNum);
        String groupNumKey = TableSuffixGenerator.getTableSuffix(convertGrouNum, keyWeightMapHolder
            .size());
        ZdalDataSourceKeyWeightRandom keyWeightRandom = keyWeightMapHolder
            .get(Constants.DBINDEX_DS_GROUP_KEY_PREFIX + groupNumKey);
        if (keyWeightRandom == null) {
            throw new IllegalArgumentException("The group_" + groupNumKey
                                               + "is not in the keyWeightMapHolder!");
        }

        int orderInGroup = -1;
        orderInGroup = keyWeightRandom.select();

        if (logger.isDebugEnabled()) {
            logger
                .debug("Select the " + orderInGroup + "th datasource in the group_" + groupNumKey);
        }

        if (orderInGroup < 0 || orderInGroup >= keyWeightRandom.getDataSourceNumberInGroup()) {
            throw new IllegalArgumentException("The order number in group_" + groupNumKey + " is "
                                               + orderInGroup + ", but the biggest number is "
                                               + (keyWeightRandom.getDataSourceNumberInGroup() - 1));
        }
        //ͳ��0~groupNum-1��Щ���ڵ�����Դ���ܵĸ���
        for (int i = 0; i < groupNum; i++) {
            int tempGroupNum = TableSuffixGenerator.trade50ConvertGroupNum(i);
            String groupNumKey2 = TableSuffixGenerator.getTableSuffix(tempGroupNum,
                keyWeightMapHolder.size());
            if (keyWeightMapHolder.get(Constants.DBINDEX_DS_GROUP_KEY_PREFIX + groupNumKey2) != null) {

                int number = keyWeightMapHolder.get(
                    Constants.DBINDEX_DS_GROUP_KEY_PREFIX + groupNumKey2)
                    .getDataSourceNumberInGroup();
                if (number <= 0) {
                    throw new IllegalArgumentException("The datasource number in the group_"
                                                       + groupNumKey2 + " is illegal, it is"
                                                       + number);
                }
                orderNum += number;
            } else {
                throw new IllegalArgumentException("The key of group_" + groupNumKey2
                                                   + "does not exist in the keyWeightMapHolder!");
            }
        }
        return orderNum + orderInGroup;
    }

    /**
     * ��������ʱ����
     * 
     * @return
     */
    public static RuntimeConfigHolder<ZdalDataSourceKeyWeightRumtime> getKeyWeightRuntimeConfigHoder() {
        return keyWeightRuntimeConfigHoder;
    }

}
