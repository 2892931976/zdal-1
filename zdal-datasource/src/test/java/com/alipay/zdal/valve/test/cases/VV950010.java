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
public class VV950010 extends ValveBasicTest{
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
	
    @Subject("sql����(BASIC1426)������{1,59}���ӽ�ʱ����������2��ִ�б���")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950011(){
		Step("����sql��������");
		changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "1,59");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ��sql");
		try {
			clearRequestTime();
			for (int i = 0; i < 2; i++) {
				incremRequestTime();
				assertTrue(statement.execute("select * from test1"));
				sleep(50000);
			}
			fail("δ�׳������쳣");
		} catch (Exception e) {
			Logger.info("OutstripValveException:"+e.getMessage());			
			Assert.areEqual(OutstripValveException.class,e.getClass(),"��֤�쳣��");
			Assert.areEqual(2L,getRequestTime(),"�ڵ�" + getRequestTime() + "��sql�����׳������쳣");
		}
	}
    
    @Subject("sql����(BASIC1417)������{1,5}����2��ִ�б���")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950012(){
    	Step("����sql��������");
		changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "1,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
		
		Step("ִ��sql");
		try {
			clearRequestTime();
			for(int i=0;i<2;i++){
				incremRequestTime();
				assertTrue(statement.execute("select * from test1"));
			}
			fail("δ�׳������쳣");
		} catch (Exception e) {
			Logger.info("OutstripValveException:"+e.getMessage());
			Assert.areEqual(OutstripValveException.class,e.getClass(),"��֤�쳣��");
			Assert.areEqual(2L,getRequestTime(),"�ڵ�" + getRequestTime() + "��sql�����׳������쳣");						
		}
	}
    
    @Subject("sql����(BASIC1416)������{0,5}��sqlִ�д�����Сֵ0��ȫ������")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950013(){		
    	Step("����sql��������");
		changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "0,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 

		Step("ִ��sql");
    	try {
			for(int i=0;i<1;i++){
				assertTrue(statement.execute("select * from test1"));
			}
			fail("û��ס");
		} catch (OutstripValveException e) {
			Step("��֤�����׳����쳣");
			Logger.info("OutstripValveException:"+e.getMessage());	
			Assert.areEqual(OutstripValveException.class,e.getClass(),"��֤�쳣��");
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
	}   
    
    
    @Subject("sql����(BASIC1420)������{9223372036854775807l,60}��sqlִ�д������ֵ����92233720368547748071l��ִ�б���")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
//    @Test
	public void TC950014(){
    	Step("����sql��������");
		changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "9223372036854775807l,60");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
		
    	try {
			for(long i=0l;i<9223372036854774807l;i++){
				assertTrue(statement.execute("select * from test1"));
			}
			assertTrue(statement.execute("select * from test1"));
			assertTrue(statement.execute("select * from test1"));
			fail("û��ס");
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",true);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
	}
    
    @Subject("sql����(BASIC1418)������{300,60}��ʱ�������ֵ60s����301��ִ�б���")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950015(){
    	Step("����sql��������");
		changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "300,60");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
    	
		Step("ִ��sql");
    	try {
			clearRequestTime();
			for(int i=0;i<301;i++){
				Logger.info("sql���ô���" + i);
				incremRequestTime();
				assertTrue(statement.execute("select * from test1"));
			}
			fail("û��ס");
		} catch (OutstripValveException e) {
			Logger.info("OutstripValveException:"+e.getMessage());		
			Assert.areEqual(OutstripValveException.class,e.getClass(),"��֤�쳣��");
			Assert.areEqual(301L,getRequestTime(),"�ڵ�" + getRequestTime() + "��sql�����׳������쳣");
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
	}
    
    @Subject("sql����(BASIC1424)������{1,1}��ʱ������Сֵ1s����2��ִ�б���")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950016(){
    	Step("����sql��������");
		changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "1,1");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
    	
		Step("ִ��sql");
		clearRequestTime();
		try {
			for(int i=0;i<2;i++){
				incremRequestTime();
				assertTrue(statement.execute("select * from test1"));				
			}
			fail("û��ס");
		} catch (OutstripValveException e) {
			Logger.info(e.getMessage());
			Assert.areEqual(OutstripValveException.class,e.getClass(),"��֤�쳣��");
			Assert.areEqual(2L,getRequestTime(),"�ڵ�" + getRequestTime() + "��sql�����׳������쳣");
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
	}
  
    @Subject("sql����(BASIC1537)�����ò���{1,5}����������ʱ��")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950017(){
    	Step("����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "1,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		currentTime = new Date().getTime();

		Step("ִ��sql");
    	try {
			for(long i=0;i<2;i++){
				assertTrue(statement.execute("select * from test1"));
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			Assert.areEqual(OutstripValveException.class,e.getClass(),"��֤�쳣��");
			Long countTime = new Date().getTime() - currentTime;
			Assert.isTrue(countTime <= 100,"�ķ�ʱ�䳬��100ms");
			Logger.info("�ӹ�����Ч�������ķ�ʱ��ms" + countTime);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
    }
    
    @Subject("sql����(BASIC1539)�����ò���{5,5}����������ʱ��")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950018(){
    	Step("����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "5,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		currentTime = new Date().getTime();

		Step("ִ��sql");
		try {
			for(long i=0;i<5;i++){
				assertTrue(statement.execute("select * from test1"));
				//logger.info("�������" + i);
			}
			Thread.sleep(4700);
			assertTrue(statement.execute("select * from test1"));
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			Assert.areEqual(OutstripValveException.class,e.getClass(),"��֤�쳣��");
			Long countTime = new Date().getTime() - currentTime;
			Logger.info("�ӹ�����Ч�������ķ�ʱ��ms" + countTime);
			Assert.isTrue(countTime >= 4700,"�ķ�ʱ��![4800,5000)");
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
	}
}
