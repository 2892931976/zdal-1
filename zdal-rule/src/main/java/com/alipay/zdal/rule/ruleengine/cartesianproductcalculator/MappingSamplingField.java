package com.alipay.zdal.rule.ruleengine.cartesianproductcalculator;

import java.util.List;

/**
 * �����ӳ�丽�����ֶΣ�
 * 
 * @author shenxun
 *
 */
public abstract class MappingSamplingField extends SamplingField{

	public MappingSamplingField(List<String> columns,int capacity) {
		super(columns,capacity);
	}

}
