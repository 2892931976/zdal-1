package com.alipay.zdal.common.exception.runtime;

public class CantFindTargetTabRuleTypeException extends ZdalRunTimeException{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7179888759169646552L;
	public CantFindTargetTabRuleTypeException(String msg) {
		super("�޷����������tableRule:"+msg+"�ҵ���Ӧ�Ĵ�������");
	}
}
