package com.alipay.zdal.common.exception.runtime;

public class CantfindConfigFileByPathException extends ZdalRunTimeException{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3338684575935778495L;
	public CantfindConfigFileByPathException(String path) {
		super("�޷�����path:"+path+"�ҵ�ָ����xml�ļ�");
	}
}
