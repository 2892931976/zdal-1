package com.alipay.zdal.client;

import java.util.List;

/**
 * ����ִ��SqlExecutoʱ����sql�������Զ����Bean
 * @author shenxun
 *
 *
 */
public class SqlExecuteObject {
	private String sql;

	private List<Object> params;

	public String getSql() {
		return sql;
	}

	public List<Object> getParams() {
		return params;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public void setParams(List<Object> params) {
		this.params = params;
	}

}
