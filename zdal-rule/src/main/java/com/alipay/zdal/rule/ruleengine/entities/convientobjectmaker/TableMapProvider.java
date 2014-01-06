/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.entities.convientobjectmaker;

import java.util.Map;

import com.alipay.zdal.rule.ruleengine.entities.abstractentities.SharedElement;

/**
 * 
 * ���ڴ���database�г��ж�����map
 * 
 * @author shenxun
 *
 */
public interface TableMapProvider {
    public Map<String, SharedElement> getTablesMap();

    public void setParentID(String parentID);

    public void setLogicTable(String logicTable);
}
