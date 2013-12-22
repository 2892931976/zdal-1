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
@Feature("����sql��tx��table�����������sql����")
public class VV950320 extends ValveBasicTest{
	Map<String, String> changeMap = new HashMap<String, String>();    
	
	@Before
	public void onSetUp() {
		Step("onSetUp");
		initLocalTxDsDoMap();
		tbName="test1";

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
	public void onTearDown() throws Exception {
		Step("onTearDown");
		try {
			statement.close();
			connection.close();

			changeMap.clear();
			changeMap.put(Parameter.SQL_VALVE, "-1,-1");
			changeMap.put(Parameter.TX_VALVE, "-1,-1");
			changeMap.put(Parameter.TABLE_VALVE, tbName+",-1,-1");
			dataSource.flush(changeMap);
			
			dataSource.destroy();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Subject("(BASIC1585)������sql��tx��table������sql��ֵ��С��ִ�з����������sql����")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950321() {
		Step("���ñ���������");
		changeMap.clear();
		changeMap.put(Parameter.TX_VALVE, "6,3");
		changeMap.put(Parameter.SQL_VALVE, "4,3");
		changeMap.put(Parameter.TABLE_VALVE, tbName+",5,3");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Step("ִ��sql");	
		try {
			clearRequestTime();
			for(long i=0;i<7;i++){
				incremRequestTime();
				statement.execute("select * from " + tbName);
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			Logger.info(e.getMessage());
			assertTrue("����SQL����",e.getMessage().contains("SQLִ�г���ָ����ֵ"));
			assertTrue("���ǵ�5������ʼ����",getRequestTime() == 5);
			assertTrue("�׳�OutstripValveException",true);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
	}


	@Subject("����sql��tx��table������sql��ֵ��С��ִ�����������sql����")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950322() {
		Step("���ñ���������");
		changeMap.clear();
		changeMap.put(Parameter.TX_VALVE, "6,3");
		changeMap.put(Parameter.SQL_VALVE, "4,3");
		changeMap.put(Parameter.TABLE_VALVE, tbName+",5,3");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Step("ִ��sql");	
		try {
			clearRequestTime();
			for(long i=0;i<7;i++){
				incremRequestTime();
				connection.setAutoCommit(false);
				statement.execute("select * from " + tbName);
				connection.commit();
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			Logger.info(e.getMessage());
			assertTrue("����SQL����",e.getMessage().contains("SQLִ�г���ָ����ֵ"));
			assertTrue("���ǵ�5������ʼ����",getRequestTime() == 5);
			assertTrue("�׳�OutstripValveException",true);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}finally{
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Subject("����sql��tx��table������sql��ֵ����table��ֵ�������sql����")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950323() {
		Step("���ñ���������");
		changeMap.clear();
		changeMap.put(Parameter.TX_VALVE, "6,3");
		changeMap.put(Parameter.SQL_VALVE, "4,3");
		changeMap.put(Parameter.TABLE_VALVE, tbName+",4,3");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Step("ִ��sql");	
		try {
			clearRequestTime();
			for(long i=0;i<7;i++){
				incremRequestTime();
				statement.execute("select * from " + tbName);
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			Logger.info(e.getMessage());
			assertTrue("����SQL����",e.getMessage().contains("SQLִ�г���ָ����ֵ"));
			assertTrue("���ǵ�5������ʼ����",getRequestTime() == 5);
			assertTrue("�׳�OutstripValveException",true);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
	}
	
	@Subject("BASIC1588������sql��tx������sql��ֵ��С��ִ�з����������sql����")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950324(){
		Step("���ñ���������");
		changeMap.clear();
		changeMap.put(Parameter.TX_VALVE, "6,3");
		changeMap.put(Parameter.SQL_VALVE, "5,3");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Step("ִ��sql");	
		try {
			clearRequestTime();
			for(long i=0;i<7;i++){
				incremRequestTime();
				statement.execute("select * from " + tbName);
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			Logger.info(e.getMessage());
			assertTrue("����SQL����",e.getMessage().contains("SQLִ�г���ָ����ֵ"));
			assertTrue("���ǵ�4������ʼ����",getRequestTime() == 6);
			assertTrue("�׳�OutstripValveException",true);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
	}
	
	@Subject("BASIC1588������sql��tx������sql��ֵ��С��ִ�����������sql����")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950325(){
		Step("���ñ���������");
		changeMap.clear();
		changeMap.put(Parameter.TX_VALVE, "6,3");
		changeMap.put(Parameter.SQL_VALVE, "5,3");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Step("ִ��sql");	
		try {
			clearRequestTime();
			for(long i=0;i<7;i++){
				incremRequestTime();
				connection.setAutoCommit(false);
				statement.execute("select * from " + tbName);
				connection.commit();
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			Logger.info(e.getMessage());
			assertTrue("����SQL����",e.getMessage().contains("SQLִ�г���ָ����ֵ"));
			assertTrue("���ǵ�4������ʼ����",getRequestTime() == 6);
			assertTrue("�׳�OutstripValveException",true);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}finally{
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Subject("����sql��table������sql��ֵ��С�������sql����")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950326() {
		Step("���ñ���������");
		changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "3,3");
		changeMap.put(Parameter.TABLE_VALVE, tbName+",4,3");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Step("ִ��sql");	
		try {
			clearRequestTime();
			for(long i=0;i<5;i++){
				incremRequestTime();
				statement.execute("select * from " + tbName);
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			Logger.info(e.getMessage());
			assertTrue("����SQL����",e.getMessage().contains("SQLִ�г���ָ����ֵ"));
			assertTrue("���ǵ�4������ʼ����",getRequestTime() == 4);
			assertTrue("�׳�OutstripValveException",true);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}	
	}
	
	@Subject("����sql��table������sql��ֵ����table��ֵ�������sql����")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950327() {
		Step("���ñ���������");
		changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "3,3");
		changeMap.put(Parameter.TABLE_VALVE, tbName+",3,3");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Step("ִ��sql");	
		try {
			clearRequestTime();
			for(long i=0;i<5;i++){
				incremRequestTime();
				statement.execute("select * from " + tbName);
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			Logger.info(e.getMessage());
			assertTrue("����SQL����",e.getMessage().contains("SQLִ�г���ָ����ֵ"));
			assertTrue("���ǵ�4������ʼ����",getRequestTime() == 4);
			assertTrue("�׳�OutstripValveException",true);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}	
	}
}
