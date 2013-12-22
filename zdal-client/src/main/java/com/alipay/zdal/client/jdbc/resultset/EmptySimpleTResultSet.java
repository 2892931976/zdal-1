package com.alipay.zdal.client.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.alipay.zdal.client.jdbc.ZdalStatement;



/**
 * ����rs.next��Զ����false�Ŀս������
 * ��Ҫ����һЩ��������
 * 
 * @author shenxun
 *
 */
public class EmptySimpleTResultSet extends AbstractTResultSet{

	public EmptySimpleTResultSet(ZdalStatement statementProxy,
			List<ResultSet> resultSets) {
		super(statementProxy, resultSets);
	}
	
	@Override
	public boolean next() throws SQLException {
		return false;
	}
}
