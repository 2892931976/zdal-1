package com.alipay.zdal.valve.test.cases;

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
import static org.junit.Assert.*;
@RunWith(ATSJUnitRunner.class)
@Feature("sql����:��Ӧ�ö�����")
public class VV950060 extends ValveBasicTest{	
	Map<String, String> changeMap=new HashMap<String, String>();
	@Before
	public void onSetUp(){
		Step("onSetUp");
		initLocalTxDsDoMap();
		
		try {
			dataSources[0] = new ZDataSource(LocalTxDsDoMap.get("ds-mysql"));				
			dataSources[1] = new ZDataSource(LocalTxDsDoMap.get("ds-mysql"));	
			
			for(int i=0;i<2;i++){
				connections[i] = dataSources[i].getConnection();
				statements[i] = connections[i].createStatement();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void onTearDown() throws Exception{
		try {
			for(int i=0;i<2;i++){
				statements[i].close();
				connections[i].close();
			}
			changeMap.clear();
			changeMap.put(Parameter.SQL_VALVE, "-1,-1");
			dataSources[0].flush(changeMap);
			
			dataSources[0].destroy();
			dataSources[1].destroy();
			} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
    @Subject("sql����(BASIC1563)������{3,5},������Ӧ�öˣ�ֻһ��Ӧ�ö�����")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
	@Test
	public void TC950061(){
	    Step("һӦ�ö�����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "3,5");
		dataSources[0].flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		currentTime = new Date().getTime();

		Step("����Ӧ�ö�ִ��sql");
		try {
			for(long i=0;i<4;i++){
				assertTrue(statements[0].execute("select * from test1"));
			}
			fail("Ӧ��0û�������ɹ�");
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",true);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
		try {
			for(long i=0;i<4;i++){
				assertTrue(statements[1].execute("select * from test1"));
			}
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
	}
    
    @Subject("sql����(BASIC1564)������{3,5},������Ӧ�öˣ�����Ӧ�ö�����")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
	@Test
	public void TC950062(){
	    Step("һӦ�ö�����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "3,5");
		dataSources[0].flush(changeMap);
		dataSources[1].flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		currentTime = new Date().getTime();

		Step("����Ӧ�ö�ִ��sql");
		try {
			for(long i=0;i<4;i++){
				assertTrue(statements[0].execute("select * from test1"));
			}
			fail("Ӧ��0û�������ɹ�");
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",true);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
		try {
			for(long i=0;i<4;i++){
				assertTrue(statements[1].execute("select * from test1"));
			}
			fail("Ӧ��1û�������ɹ�");
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",true);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
	}
}