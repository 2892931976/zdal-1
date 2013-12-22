/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.exceptions;

/**
 * �ڿ�zone����ʱ��������Ǳ�zone������Դ�����׳����쳣.
 * @author ����
 * @version $Id: ZdalLdcException.java, v 0.1 2013-6-25 ����05:16:08 Exp $
 */
public class ZdalLdcException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ZdalLdcException(String cause) {
        super(cause);
    }

    public ZdalLdcException(Throwable t) {
        super(t);
    }

    public ZdalLdcException(String cause, Throwable t) {
        super(cause, t);
    }
}
