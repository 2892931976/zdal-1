/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.jdbc.test;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: ZdalInit.java, v 0.1 2012-5-15 ����09:18:16 xiaoqing.zhouxq Exp $
 */
public interface ZdalInit {

    /**
     * ��ʼ���ֿ�ֱ���Ϣ.
     * @param dbInfo[] ���зֿ��������Ϣ.
     * @param tableCount ÿ���ֿ��½����ű�.
     * @param tableSuffixWidth ������׺�ĳ���.
     */
    public void initDatabase(DbInfo[] dbInfos, int tableCount, int tableSuffixWidth);
}
