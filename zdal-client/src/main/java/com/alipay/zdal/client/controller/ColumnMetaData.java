package com.alipay.zdal.client.controller;

import com.alipay.zdal.common.sqljep.function.Comparative;

public class ColumnMetaData {
	/**
	 * ָ���������ֶ�
	 */
	public final String key;
	/**
	 * �������ֶεĶ�ӦComparative
	 */
	public final  Comparative value;
	public ColumnMetaData(String key,Comparative value) {
		this.key=key;
		this.value=value;
	}
}
