package com.alipay.zdal.rule.ruleengine.rule;

/**
 * ��Ϊ�����Ժ���õ����������ӳ���������Զ��õ�һ��mappingKey
 * 
 * @author shenxun
 *
 */
public class ResultAndMappingKey {
	public ResultAndMappingKey(String result) {
		this.result = result;
	}
	final String result;
	/**
	 * һ�����ֻ��֧����һ��mapping keyӳ���������֧�ֶ����
	 * �������ֻ����mappingrule��ʱ�������
	 */
	Object mappingKey;

        String mappingTargetColumn;
}
