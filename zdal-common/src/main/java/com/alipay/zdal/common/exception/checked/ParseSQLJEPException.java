package com.alipay.zdal.common.exception.checked;

public class ParseSQLJEPException extends ZdalCheckedExcption{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7724677712426352259L;
	public ParseSQLJEPException(Throwable th){
		super("����sqlJep��parseExpression��ʱ��������"+th.getMessage());
	}

}
