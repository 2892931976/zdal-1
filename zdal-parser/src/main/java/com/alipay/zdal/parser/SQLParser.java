/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser;

import com.alipay.zdal.parser.result.SqlParserResult;

/**
 * SQL����������
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLParser.java, v 0.1 2012-5-22 ����09:59:15 xiaoqing.zhouxq Exp $
 */
public interface SQLParser {
    /**
     * ����sql��䣬����mysql��oracle��sql���.
     * @param sql 
     * @param isMySQL true=mysql,false=oracle.
     * @return
     */
    SqlParserResult parse(String sql, boolean isMySQL);
}
