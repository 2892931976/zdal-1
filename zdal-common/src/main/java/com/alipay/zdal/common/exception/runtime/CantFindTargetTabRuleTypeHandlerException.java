package com.alipay.zdal.common.exception.runtime;

public class CantFindTargetTabRuleTypeHandlerException extends ZdalRunTimeException{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4073830327289870566L;

	public CantFindTargetTabRuleTypeHandlerException(String msg) {
		super("�޷��ҵ�"+msg+"��Ӧ�Ĵ�����");
	}
}
