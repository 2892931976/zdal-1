package com.alipay.zdal.client;



/**
 * ͨ�ýӿڣ�����ͨ����д{@link RouteHandler} ����ע�ᵽ{@link RouteHandlerRegister}
 * �ϵķ�ʽ�������µ��Զ���conditionHandler.�����Ϳ���֧�ֶ��ֲ�ͬ�������Լ����� {@link RouteHandlerRegister}
 * �е�keyΪRouteConditionʵ�ֵ�classȫ���ơ�
 * 
 * 
 * @author shenxun
 * 
 */
public interface RouteCondition {
	public String getVirtualTableName() ;

}
