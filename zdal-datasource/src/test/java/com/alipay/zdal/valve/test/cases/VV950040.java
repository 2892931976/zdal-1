package com.alipay.zdal.valve.test.cases;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Date;
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
@Feature("sql����:��ʱ�����ڣ�sqlִ�д�����������������Ӧ����")
public class VV950040 extends ValveBasicTest{	
	Map<String, String> changeMap = new HashMap<String, String>();

	@Before
	public void onSetUp() {
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
	public void onTearDown() throws Exception {
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
	
    @Subject("sql����(BASIC1427)�����ò���{1,60}����ʱ��������")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950041(){
    	Step("����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "1,60");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Step("ִ��sql");
    	try {
			for(int i=0;i<1;i++){
				assertTrue(statement.execute("select * from test1"));
			}
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
		
		try {
			Thread.sleep(58000);
			for(int i=0;i<1;i++){
				assertTrue(statement.execute("select * from test1"));
			}
			fail("û��ס");
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",true);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
		
		try {
			for(int i=0;i<1;i++){
				Thread.sleep(3000);
				assertTrue(statement.execute("select * from test1"));
			}
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
	}
    
    @Subject("sql����(BASIC1540)�����ò���{3,5}����ʱ��������")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950042(){
    	Step("����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "3,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		currentTime = new Date().getTime();

		Step("ִ��sql");
		try {
			for(long i=0;i<3;i++){
				assertTrue(statement.execute("select * from test1"));
			}
		} catch (OutstripValveException e) {
			Long countTime = new Date().getTime() - currentTime;
			Logger.info("�ӹ�����Ч����ǰʱ��ms" + countTime);
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
		try {
			Thread.sleep(6000);
			clearRequestTime();
			for(long i=0;i<4;i++){
				incremRequestTime();
				assertTrue(statement.execute("select * from test1"));
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			Long countTime = new Date().getTime() - currentTime;
			Logger.info("�ӹ�����Ч�������ķ�ʱ��ms" + countTime);
			assertTrue("�ķ�ʱ�䲻����5000",countTime > 5000);
			assertTrue("�����쳣����4�ξ��׳�",getRequestTime() == 4);
			assertTrue("�׳�OutstripValveException",true);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue("Exception",false);
		}
    }
    
    @Subject("sql����(BASIC1542)�����ò���{3,5}����ʱ��������")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950043(){
    	Step("����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "3,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		currentTime = new Date().getTime();

		Step("ִ��sql");
		try {
			for(long i=0;i<3;i++){
				assertTrue(statement.execute("select * from test1"));
			}
		} catch (OutstripValveException e) {
			Long countTime = new Date().getTime() - currentTime;
			Logger.info("�ӹ�����Ч����ǰʱ��ms" + countTime);
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
		try {
			Thread.sleep(6000);
			for(long i=0;i<3;i++){
				assertTrue(statement.execute("select * from test1"));
			}
		} catch (OutstripValveException e) {
			Long countTime = new Date().getTime() - currentTime;
			Logger.info("�ӹ�����Ч����ǰʱ��ms" + countTime);
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
	}
    
    @Subject("sql����(BASIC1543)�����ò���{3,5}����ʱ��������")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950044(){
    	Step("����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "3,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		currentTime = new Date().getTime();

		Step("ִ��sql");
		try {
			clearRequestTime();
			for(long i=0;i<4;i++){
				incremRequestTime();
				assertTrue(statement.execute("select * from test1"));
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			Long countTime = new Date().getTime() - currentTime;
			Logger.info("�ӹ�����Ч�������ķ�ʱ��ms" + countTime);
			assertTrue("�����쳣����4�ξ��׳�",getRequestTime() == 4);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
		try {
			Thread.sleep(6000);
			clearRequestTime();
			for(long i=0;i<4;i++){
				incremRequestTime();
				assertTrue(statement.execute("select * from test1"));
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			Long countTime = new Date().getTime() - currentTime;
			Logger.info("�ӹ�����Ч�������ķ�ʱ��ms" + countTime);
			assertTrue("�ķ�ʱ�䲻����5000",countTime > 5000);
			assertTrue("�����쳣����4�ξ��׳�",getRequestTime() == 4);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
	}
    
    @Subject("sql����(BASIC1544)�����ò���{3,5}����ʱ��������")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950045(){
    	Step("����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "3,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		currentTime = new Date().getTime();

		Step("ִ��sql");
		try {
			clearRequestTime();
			for(long i=0;i<4;i++){
				incremRequestTime();
				assertTrue(statement.execute("select * from test1"));
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			Long countTime = new Date().getTime() - currentTime;
			Logger.info("�ӹ�����Ч�������ķ�ʱ��ms" + countTime);
			assertTrue("�����쳣����4�ξ��׳�",getRequestTime() == 4);
			assertTrue("�׳�OutstripValveException",true);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
		try {
			Thread.sleep(6000);
			clearRequestTime();
			for(long i=0;i<3;i++){
				incremRequestTime();
				assertTrue(statement.execute("select * from test1"));
			}
		} catch (OutstripValveException e) {
			Long countTime = new Date().getTime() - currentTime;
			Logger.info("�ӹ�����Ч�������ķ�ʱ��ms" + countTime);
			assertTrue("�ķ�ʱ�䲻����5000",countTime > 5000);
			assertTrue(getRequestTime() == 3);
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
	}
    
    @Subject("sql����(BASIC1545)�����ò���{3,5}����ʱ��������")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950046(){
    	Step("����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "3,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		currentTime = new Date().getTime();

		Step("ִ��sql");
		try {
			clearRequestTime();
			for(long i=0;i<4;i++){
				incremRequestTime();
				assertTrue(statement.execute("select * from test1"));
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			Long countTime = new Date().getTime() - currentTime;
			Logger.info("�ӹ�����Ч�������ķ�ʱ��ms" + countTime);
			assertTrue("�����쳣����4�ξ��׳�",getRequestTime() == 4);
			assertTrue("�׳�OutstripValveException",true);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
		try {
			Thread.sleep(6000);
			clearRequestTime();
			for(long i=0;i<2;i++){
				incremRequestTime();
				assertTrue(statement.execute("select * from test1"));
			}
		} catch (OutstripValveException e) {
			Long countTime = new Date().getTime() - currentTime;
			Logger.info("�ӹ�����Ч�������ķ�ʱ��ms" + countTime);
			assertTrue("�ķ�ʱ�䲻����5000",countTime > 5000);
			assertTrue(getRequestTime() == 2);
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
	}
    
    @Subject("sql����(BASIC1626)�������������")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950047(){
    	int loop = 1000;
    	Long timeNormal = 0L;
    	Long timeValve = 0L;
    	Long timeValveAfter = 0L;
    	
    	try {
			for(int i=0;i<loop;i++){
				currentTime = new Date().getTime();
				statement.execute("select * from test1");
				timeNormal += new Date().getTime() - currentTime;
			}
			timeNormal = timeNormal / loop;
		
	    	changeMap.clear();
			changeMap.put(Parameter.SQL_VALVE, "10000,60");
			dataSource.flush(changeMap);
			for(int i=0;i<loop;i++){
				currentTime = new Date().getTime();
				statement.execute("select * from test1");
				timeValve += new Date().getTime() - currentTime;
			}
			timeValve = timeValve / loop;
			
	    	changeMap.clear();
			changeMap.put(Parameter.SQL_VALVE, "-1,1");
			dataSource.flush(changeMap);
			for(int i=0;i<loop;i++){
				currentTime = new Date().getTime();
				statement.execute("select * from test1");
				timeValveAfter += new Date().getTime() - currentTime;
			}
			timeValveAfter = timeValveAfter / loop;
			Logger.info("timeNormal  " + timeNormal);
			Logger.info("timeValve  " + timeValve);
			Logger.info("timeValveAfter  " + timeValveAfter);
			assertTrue("Exception",Math.abs(timeNormal - timeValveAfter) < 5);
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}	
    }
    
    @Subject("sql����(BASIC1627)�������������������")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950048(){
    	Step("����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "1,5");
		changeMap.put(Parameter.SQL_VALVE, "4,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		currentTime = new Date().getTime();

		Step("ִ��sql");
		try {
			clearRequestTime();
			for(int i=0;i<5;i++){
				incremRequestTime();
				assertTrue(statement.execute("select * from test1"));
			}
			fail("û��ס");
		} catch (OutstripValveException e) {
			assertTrue("���ǵ�5�β�����",super.getRequestTime() == 5);
			assertTrue("OutstripValveException",true);
		} catch (SQLException e) {
			assertTrue("SQLException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
	}
}
