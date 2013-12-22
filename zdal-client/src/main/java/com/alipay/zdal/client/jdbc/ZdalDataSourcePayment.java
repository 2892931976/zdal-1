/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alipay.zdal.client.datasource.keyweight.GetDataSourceSequenceRules;
import com.alipay.zdal.client.datasource.keyweight.ZdalDataSourceKeyWeightRandom;
import com.alipay.zdal.client.datasource.keyweight.ZdalDataSourceKeyWeightRumtime;
import com.alipay.zdal.client.exceptions.ZdalClientException;
import com.alipay.zdal.common.lang.StringUtil;
import com.alipay.zdal.rule.config.beans.AppRule;

/**
 * Ϊ��֧��paycore-payment�е�ȫ������µ�failover����.
 * ��Ҫ�ĸĶ����ǣ�
 * ���ڲ�ά��ÿ��group��Ҫ����ȫ���������Դ�б���failover�л��Ժ󣬰�����б��dbKey����.
 * @author ����
 * @version $Id: ZdalDataSourcePayment.java, v 0.1 2013-9-3 ����03:21:13 Exp $
 */
public class ZdalDataSourcePayment extends ZdalDataSource {

    /** ���ڴ����Ҫ����ȫ���������Դ����,key=groupName value=dbKeys */
    private Map<String, List<String>> checkDbKeys = new HashMap<String, List<String>>();

    protected void initForAppRule(AppRule appRule) {
        super.initForAppRule(appRule);
        //�Ѹ���group��Ȩ��>0��dbKey���浽checkDbKeys�У�����ȫ���н��м��.
        Map<String, ZdalDataSourceKeyWeightRandom> keyWeightMapHolder = super
            .getKeyWeightMapConfig();
        for (Entry<String, ZdalDataSourceKeyWeightRandom> entrySet : keyWeightMapHolder.entrySet()) {
            String groupKey = entrySet.getKey();
            ZdalDataSourceKeyWeightRandom keyWeightRandom = entrySet.getValue();
            Map<String, Integer> cacheKeyWeights = keyWeightRandom.getWeightConfig();
            List<String> canUseDbKeys = new ArrayList<String>();
            for (Entry<String, Integer> weightsEntry : cacheKeyWeights.entrySet()) {
                String dbKey = weightsEntry.getKey();
                Integer weight = weightsEntry.getValue();
                if (weight > 0) {
                    canUseDbKeys.add(dbKey);
                }
            }
            checkDbKeys.put(groupKey, canUseDbKeys);
        }
        CONFIG_LOGGER.warn("WARN ## the PayMentZdalDataSource need to check dataSources = ["
                           + checkDbKeys + "]");
    }

    /**
     * ����p��ʽ����
     * group_00=ds0:10,ds1:0
     * group_01=ds2:10,ds3:0
     * group_02=ds4:0,ds5:10
     * һ��ֻ��һ�����ʱ���õ�����Ȩ�أ�Ĭ��Ϊ10
     * @param p ���͹���������
     *
     * @see com.alipay.zdal.client.jdbc.AbstractZdalDataSource#resetKeyWeightConfig(java.util.Map)
     */
    protected void resetKeyWeightConfig(Map<String, String> p) {
        //        Map<String, ZdalDataSourceKeyWeightRandom> keyWeightMapHolder = GetDataSourceSequenceRules
        //            .getKeyWeightRuntimeConfigHoder().get().getKeyWeightMapHolder();
        Map<String, ZdalDataSourceKeyWeightRandom> keyWeightMapHolder = super
            .getKeyWeightMapConfig();
        for (Entry<String, String> entrySet : p.entrySet()) {
            String groupKey = entrySet.getKey();
            String value = entrySet.getValue();
            if (StringUtil.isBlank(groupKey) || StringUtil.isBlank(value)) {
                throw new ZdalClientException("ERROR ## ����ԴgroupKey=" + groupKey
                                              + "����Ȩ��������Ϣ����Ϊ��,value=" + value);
            }
            String[] keyWeightStr = value.split(",");
            String[] weightKeys = new String[keyWeightStr.length];
            int[] weights = new int[keyWeightStr.length];
            List<String> canUseDbKeys = new ArrayList<String>();
            for (int i = 0; i < keyWeightStr.length; i++) {
                if (StringUtil.isBlank(keyWeightStr[i])) {
                    throw new ZdalClientException("ERROR ## ����ԴkeyWeightStr[" + i
                                                  + "]����Ȩ��������Ϣ����Ϊ��.");
                }
                String[] keyAndWeight = keyWeightStr[i].split(":");
                if (keyAndWeight.length != 2) {
                    throw new ZdalClientException("ERROR ## ����Դkey��������Ȩ�ش���,keyWeightStr[" + i
                                                  + "]=" + keyWeightStr[i] + ".");
                }
                String key = keyAndWeight[0];
                String weightStr = keyAndWeight[1];
                if (StringUtil.isBlank(key) || StringUtil.isBlank(weightStr)) {
                    CONFIG_LOGGER.error("ERROR ## ����Դ����Ȩ��������Ϣ����Ϊ��,key=" + key + ",weightStr="
                                        + weightStr);
                    return;
                }
                weightKeys[i] = key.trim();
                weights[i] = Integer.parseInt(weightStr.trim());
                if (weights[i] > 0) {//����Ҫȫ���������Դ������.
                    canUseDbKeys.add(weightKeys[i]);
                }
            }
            //          ���� groupKey�Լ���Ӧ��keyAndWeightMapȥ��ѯ
            ZdalDataSourceKeyWeightRandom weightRandom = keyWeightMapHolder.get(groupKey);
            if (weightRandom == null) {
                throw new ZdalClientException("ERROR ## �����͵İ�����Դkey����Ȩ�������е�key����,�Ƿ���groupKey="
                                              + groupKey);
            }
            for (String newKey : weightKeys) {
                if (weightRandom.getWeightConfig() == null
                    || !weightRandom.getWeightConfig().containsKey(newKey)) {
                    throw new ZdalClientException("�����͵�����Դ����" + groupKey
                                                  + "Ȩ�������а��������ڸ��������Դ��ʶ,key=" + newKey);
                }
            }
            if (weightKeys.length != weightRandom.getDataSourceNumberInGroup()) {
                throw new ZdalClientException("�����͵İ�����Դkey����Ȩ�������У�����groupKey=" + groupKey
                                              + "����������Դ�������� ,size=" + weightKeys.length
                                              + ",the size should be "
                                              + weightRandom.getDataSourceNumberInGroup());
            }
            //���ݸ����groupKey�Լ���Ӧ��keyAndWeightMap����TDataSourceKeyWeightRandom
            ZdalDataSourceKeyWeightRandom TDataSourceKeyWeightRandom = new ZdalDataSourceKeyWeightRandom(
                weightKeys, weights);
            keyWeightMapHolder.put(groupKey, TDataSourceKeyWeightRandom);

            CONFIG_LOGGER.warn("WARN ## the groupName = " + groupKey + " check dbKey changed to "
                               + canUseDbKeys);
            checkDbKeys.put(groupKey, canUseDbKeys);
        }
        GetDataSourceSequenceRules.getKeyWeightRuntimeConfigHoder().set(
            new ZdalDataSourceKeyWeightRumtime(keyWeightMapHolder));
        //���ñ��ص�keyWeightMapCofig���ԣ�ȫ����Ի������ڸ�����
        super.setKeyWeightMapConfig(keyWeightMapHolder);
    }

    public Map<String, List<String>> getCheckDbKeys() {
        return checkDbKeys;
    }

    public void setCheckDbKeys(Map<String, List<String>> checkDbKeys) {
        this.checkDbKeys = checkDbKeys;
    }

}
