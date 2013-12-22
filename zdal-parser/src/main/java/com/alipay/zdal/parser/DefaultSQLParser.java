/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.apache.log4j.Logger;

import com.alipay.zdal.parser.exceptions.SqlParserException;
import com.alipay.zdal.parser.result.SqlParserResult;
import com.alipay.zdal.parser.result.SqlParserResultFactory;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleOutputVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTOutputVisitor;
import com.alipay.zdal.parser.visitor.ZdalMySqlSchemaStatVisitor;
import com.alipay.zdal.parser.visitor.ZdalOracleSchemaStatVisitor;
import com.alipay.zdal.parser.visitor.ZdalSchemaStatVisitor;

/**
 * SQL ��������ʵ���࣬��Ҫ�ǽ�SQL�������ŵ�cache�У�
 * ���cache���и���SQL,��ֱ�Ӵ�cache��ȡ���������parse
 * 
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLParserImp.java, v 0.1 2012-5-22 ����10:01:18 xiaoqing.zhouxq Exp $
 */
public class DefaultSQLParser implements SQLParser {
    private static final Logger      LOG         = Logger.getLogger(DefaultSQLParser.class);

    private static final ParserCache GLOBALCACHE = ParserCache.instance();

    public SqlParserResult parse(String sql, boolean isMySQL) {
        this.parseSQL(sql, isMySQL);
        ZdalSchemaStatVisitor visitor = getStatement(sql);
        try {
            if (visitor == null) {
                // ���ûȡ�������Է���sql����ʼ��
                this.parseSQL(sql, isMySQL);
                visitor = getStatement(sql);

            }
        } catch (Exception e) {
            throw new SqlParserException("the sql = " + sql + " is not support yet "
                                         + e.getMessage());
        }
        return SqlParserResultFactory.createSqlParserResult(visitor, isMySQL);
    }

    /**
     * ���Դ�cache��ȡ��sql,���δȡ�����������sql����ʼ����
     * 
     * �����Ƕ�γ�ʼ��������Ϊkeyһ�£�ͬһ��sql��������ʼ���Ժ�Ľ����һ�µ�
     * 
     * ���п�����Ϊ������put��init֮ǰ������,�������������
     * @param sql
     */
    public void parseSQL(String sql) {
        this.nestedParseSql(sql, true);
    }

    public void parseSQL(String sql, boolean isMysql) {
        this.nestedParseSql(sql, isMysql);
    }

    private void nestedParseSql(final String sql, final boolean isMysql) {
        if (sql == null) {
            throw new SqlParserException("sql must not be null");
        }
        //Ϊ�˷�ֹ����ظ���ʼ��������ʹ����future task��ȷ����ʼ��ֻ����һ��
        FutureTask<ZdalSchemaStatVisitor> future = GLOBALCACHE.getFutureTask(sql);
        if (future == null) {
            Callable<ZdalSchemaStatVisitor> parserHandler = new Callable<ZdalSchemaStatVisitor>() {
                public ZdalSchemaStatVisitor call() throws Exception {
                    final List<SQLStatement> parserResults = getSqlStatements(sql, isMysql);
                    if (parserResults == null || parserResults.isEmpty()) {
                        LOG.error("ERROR ## the sql parser result is null,the sql = " + sql);
                        return null;
                    }
                    if (parserResults.size() > 1) {
                        LOG
                            .warn("WARN ## after this sql parser,has more than one SQLStatement,the sql = "
                                  + sql);
                    }
                    SQLStatement statement = parserResults.get(0);
                    ZdalSchemaStatVisitor visitor = null;
                    if (isMysql == true) {
                        visitor = new ZdalMySqlSchemaStatVisitor();
                        statement.accept(visitor);
                    } else {
                        visitor = new ZdalOracleSchemaStatVisitor();
                        statement.accept(visitor);
                    }
                    return visitor;
                }
            };
            future = new FutureTask<ZdalSchemaStatVisitor>(parserHandler);
            future = GLOBALCACHE.setFutureTaskIfAbsent(sql, future);
            future.run();
        }
    }

    /**
     * ���parse���������sql���.
     * @param parserResults
     * @return
     */
    public String outputParsedSql(List<SQLStatement> parserResults, boolean isMysql) {
        StringBuilder out = new StringBuilder();
        SQLASTOutputVisitor visitor = null;
        if (isMysql) {
            visitor = new MySqlOutputVisitor(out);
        } else {
            visitor = new OracleOutputVisitor(out);
        }
        for (SQLStatement stmt : parserResults) {
            stmt.accept(visitor);
        }

        return out.toString();
    }

    /**
     * ͨ��parserģ�����sql���,����java�����ʾ��sql.
     * @param sql
     * @param isMysql
     * @return
     */
    private List<SQLStatement> getSqlStatements(final String sql, final boolean isMysql) {
        if (isMysql == true) {
            MySqlStatementParser parser = new MySqlStatementParser(sql);
            return parser.parseStatementList();
        } else {
            OracleStatementParser parser = new OracleStatementParser(sql);
            return parser.parseStatementList();
        }
    }

    /**
     * ����SQL��ȡ��Ӧ��javaSQL����
     * @param sql
     * @return java SQL ���� ���cache��û���򷵻ؿ�
     */
    private ZdalSchemaStatVisitor getStatement(String sql) {
        try {
            FutureTask<ZdalSchemaStatVisitor> future = GLOBALCACHE.getFutureTask(sql);
            if (future == null) {
                return null;
            } else {
                return future.get();
            }
        } catch (Exception e) {
            throw new SqlParserException("ERROR ## get sqlparser result from cache has an error:",
                e);
        }
    }

}
