package com.alipay.zdal.valve.test.cases;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.annotation.Tester;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;import static com.alipay.ats.internal.domain.ATS.*;

import com.alipay.zdal.datasource.ZDataSource;
import com.alipay.zdal.datasource.util.Parameter;
import com.alipay.zdal.valve.test.utils.ValveBasicTest;
import com.alipay.zdal.valve.util.OutstripValveException;

@RunWith(ATSJUnitRunner.class)
@Feature("sql����:δ���û������ò��Ϸ��ַ���������")
public class VV950030 extends ValveBasicTest{
Map<String, String> changeMap=new HashMap<String, String>();	
	@Before
	public void onSetUp(){
		Step("onSetUp");
		initLocalTxDsDoMap();
		
		try {
			dataSource = new ZDataSource(LocalTxDsDoMap.get("ds-mysql"));	
			connection = dataSource.getConnection();
			statement = connection.createStatement();	
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void onTearDown() throws Exception{
		Step("onTearDown");
		try {
			statement.close();
			connection.close();
			
			changeMap.clear();
			changeMap.put(Parameter.SQL_VALVE, "-1,-1");
			dataSource.flush(changeMap);
			
			dataSource.destroy();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
    @Subject("sql����(BASIC1421)������{A,1}��������")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950031(){
    	Step("����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "A,1");
		dataSource.flush(changeMap);		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
		
		Step("ִ��sql");
		try {
			for(int i=0;i<2;i++){
				assertTrue(statement.execute("select * from test1"));
			}
		} catch (OutstripValveException e) {
			Assert.isTrue(false,"�׳�OutstripValveException");
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
		
		Step("��֤sql��������");
		long threshold=dataSource.getValve().getSqlValve().getThreshold();		
		long period=dataSource.getValve().getSqlValve().getPeriod();
		Assert.areEqual(-1L,threshold,"threshol�Ƿ�Ϊ-1");
		Assert.areEqual(-1L,period,"period�Ƿ�Ϊ-1");
	}
    
    @Subject("sql����������{1,A}��������")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950032(){
    	Step("����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "1,A");
		dataSource.flush(changeMap);		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
		
		Step("ִ��sql");
		try {
			for(int i=0;i<2;i++){
				assertTrue(statement.execute("select * from test1"));
			}
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
		
		Step("��֤sql��������");
		long threshold=dataSource.getValve().getSqlValve().getThreshold();		
		long period=dataSource.getValve().getSqlValve().getPeriod();
		Assert.areEqual(-1L,threshold,"threshol�Ƿ�Ϊ-1");
		Assert.areEqual(-1L,period,"period�Ƿ�Ϊ-1");
	}
    
    @Subject("sql����������{-1,1}��������")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950033(){
    	Step("����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "1,-1");
		dataSource.flush(changeMap);		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
		
		Step("ִ��sql");
		try {
			for(int i=0;i<2;i++){
				assertTrue(statement.execute("select * from test1"));
			}
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
		
		Step("��֤sql��������");
		long threshold=dataSource.getValve().getSqlValve().getThreshold();		
		long period=dataSource.getValve().getSqlValve().getPeriod();
		Assert.areEqual(-1L,threshold,"threshol�Ƿ�Ϊ-1");
		Assert.areEqual(-1L,period,"period�Ƿ�Ϊ-1");
	}
    
    @Subject("sql����(BASIC1422)������{1,-1}��������")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950034(){
    	Step("����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "1,-1");
		dataSource.flush(changeMap);		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
		
		Step("ִ��sql");
		try {
			for(int i=0;i<2;i++){
				assertTrue(statement.execute("select * from test1"));
			}
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
		
		Step("��֤sql��������");
		long threshold=dataSource.getValve().getSqlValve().getThreshold();		
		long period=dataSource.getValve().getSqlValve().getPeriod();
		Assert.areEqual(-1L,threshold,"threshol�Ƿ�Ϊ-1");
		Assert.areEqual(-1L,period,"period�Ƿ�Ϊ-1");
	}
    
    @Subject("sql����(BASIC1423)������{1,0}��������")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950035(){
    	Step("����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "1,0");
		dataSource.flush(changeMap);		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
		
		Step("ִ��sql");
		try {
			for(int i=0;i<2;i++){
				assertTrue(statement.execute("select * from test1"));
			}
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
		
		Step("��֤sql��������");
		long threshold=dataSource.getValve().getSqlValve().getThreshold();		
		long period=dataSource.getValve().getSqlValve().getPeriod();
		Assert.areEqual(-1L,threshold,"threshol�Ƿ�Ϊ-1");
		Assert.areEqual(-1L,period,"period�Ƿ�Ϊ-1");
	}
    
    
    @Subject("sql����(BASIC1425)�������úϷ������ò��Ϸ���������")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950036(){
    	Step("����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "-1,1");
		changeMap.put(Parameter.SQL_VALVE, "1,A");
		dataSource.flush(changeMap);		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
		
		Step("ִ��sql");
		try {
			for(int i=0;i<2;i++){
				assertTrue(statement.execute("select * from test1"));
			}
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
		
		Step("��֤sql��������");
		long threshold=dataSource.getValve().getSqlValve().getThreshold();		
		long period=dataSource.getValve().getSqlValve().getPeriod();
		Assert.areEqual(-1L,threshold,"threshol�Ƿ�Ϊ-1");
		Assert.areEqual(-1L,period,"period�Ƿ�Ϊ-1");
	}
    
    @Subject("sql����(BASIC1428)������{1,61}��������")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950037(){
    	Step("����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "1,61");
		dataSource.flush(changeMap);		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
		
		Step("ִ��sql");
		try {
			for(int i=0;i<2;i++){
				assertTrue(statement.execute("select * from test1"));
			}
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
		
		Step("��֤sql��������");
		long threshold=dataSource.getValve().getSqlValve().getThreshold();		
		long period=dataSource.getValve().getSqlValve().getPeriod();
		Assert.areEqual(-1L,threshold,"threshol�Ƿ�Ϊ-1");
		Assert.areEqual(-1L,period,"period�Ƿ�Ϊ-1");
	}
    
    @Subject("sql�����������ã�������")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950038(){
    	Step("����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "");
		dataSource.flush(changeMap);		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
		
		Step("ִ��sql");
		try {
			for(int i=0;i<2;i++){
				assertTrue(statement.execute("select * from test1"));
			}
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
		
		Step("��֤sql��������");
		long threshold=dataSource.getValve().getSqlValve().getThreshold();		
		long period=dataSource.getValve().getSqlValve().getPeriod();
		Assert.areEqual(-1L,threshold,"threshol�Ƿ�Ϊ-1");
		Assert.areEqual(-1L,period,"period�Ƿ�Ϊ-1");
	}
}
