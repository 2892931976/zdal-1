/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2010 All Rights Reserved.
 */
package com.alipay.zdal.datasource.validation;

/**
 *
 * @author fengqi.lin@alipay.com
 * @version $Id: UnreleaseConnectionChecker.java, v 0.1 2010-8-12 ����01:18:42 fengqi.lin Exp $
 */
public interface UnreleaseConnectionChecker {
    
    /**
     * ��û���ͷŵ����ݿ����ӽ��м��
     */
    void connectionCheck();

}
