package com.alipay.zdal.valve.test.objects;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class TemporaryDataArea {
    private static ConcurrentHashMap<String, String> concurrentThreadReturnValues = new ConcurrentHashMap<String, String>(); //���沢���̷߳���ֵ��Ϣ

    public static void addConcurrentThreadReturnValues(String key, String value) {
        concurrentThreadReturnValues.put(key, value);
    }

    public static String getConcurrentThreadReturnValues(String key) {
        return concurrentThreadReturnValues.get(key);
    }

    public static void clearConcurrentThreadReturnValues() {
        concurrentThreadReturnValues.clear();
    }

    synchronized public static int getConcurrentThreadReturnValuesSum() {
        int sum = 0;
        Iterator<String> iterator = concurrentThreadReturnValues.values().iterator();
        while (iterator.hasNext()) {
            sum += Integer.parseInt(iterator.next());
        }
        System.out.println("��ǰ�ɹ����ۼƵ���������" + sum);
        return sum;
    }

    public static void main(String[] args) {

    }
}