package com.alipay.zdal.client.rule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ���zdal-document�ķֿ�ֱ����.
 * �̲߳���ȫ�ģ���Ҫ���õĵط����в�������.
 * @author ����
 * @version $Id: DocumentShardingRule.java, v 0.1 2013-1-16 ����03:59:35 Exp $
 */
public class DocumentShardingRule {

    /** key=tableIndex,value=dbIndex */
    private volatile static ConcurrentHashMap<Integer, Integer> cacheRuleMap = new ConcurrentHashMap<Integer, Integer>();

    /**
     * ����document���ȵ��Ķ�Ӧ��ϵ.
     * tableIndex:dbIndex,tableIndex:dbIndex,tableIndex:dbIndex
     * @param cacheString
     */
    public static void setHotspot(Map<Integer, Integer> hotspots) {
        cacheRuleMap.clear();//�����ԭ�����ȵ���Ӧ��ϵ.
        if (hotspots == null || hotspots.isEmpty()) {//���cacheStringΪ�գ���ֱ�ӷ���.
            return;
        }
        cacheRuleMap.putAll(hotspots);
    }

    /**
     * ���� �ֱ�Ĺ���
     * @param key      �ֿ�key
     * @param dbCount  ��ĸ���
     * @param tbCount  ��ĸ���
     * @return   �ֱ�ֵ
     */
    public static int getShardingDBNumber(String key, int dbCount, int tbCount) {
        //����ֱ�
        int tbNumber = getShardingTableNumber(key, dbCount, tbCount);
        //����cache,����������У���ֱ�ӷ���
        if (cacheRuleMap.get(tbNumber) != null) {
            return cacheRuleMap.get(tbNumber);
        }

        //�������շֿ������ֿ�
        int keyHashValue = getKeyHashValue(key);
        int dbNumber = (keyHashValue % tbCount) / (tbCount / dbCount);
        return dbNumber;
    }

    /**
     * ���� �ֱ�Ĺ���
     * @param key      �ֿ�key
     * @param dbCount  ��ĸ���
     * @param tbCount  ��ĸ���
     * @return   �ֱ�ֵ
     */
    public static int getShardingTableNumber(String key, int dbCount, int tbCount) {
        int keyHashValue = getKeyHashValue(key);
        int tbNumber = (keyHashValue % tbCount) % (tbCount / dbCount);
        return tbNumber;
    }

    /**
     * ����key��hashcode��ȷ����>=0������.
     * @param key
     * @return
     */
    private static int getKeyHashValue(String key) {
        int keyHashValue = key.hashCode();
        if (keyHashValue < 0) {
            keyHashValue = keyHashValue & 0x7FFFFFFF;
        }
        return keyHashValue;
    }
}
