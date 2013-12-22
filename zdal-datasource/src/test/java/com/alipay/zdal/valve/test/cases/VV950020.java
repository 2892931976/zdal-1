package com.alipay.zdal.valve.test.cases;

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
@Feature("sql����:��ʱ�����ڣ�sqlִ�д���С�ڵ�����������������ִ��")
public class VV950020 extends ValveBasicTest{
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
	
	
    @Subject("sql����(BASIC1415)������{5,5}��ִ��2�β�����")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950021(){
		Step("����sql��������");
		changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "5,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
		
		Step("ִ��sql");
		try {
			for(int i=0;i<2;i++){
				Assert.isTrue(statement.execute("select * from test1"),"");
			}
		} catch (OutstripValveException e) {
			Logger.error(e.getMessage());
			Assert.isTrue(false,"�׳�OutstripValveException");
		} catch (Exception e) {
			Logger.error(e.getMessage());
			Assert.isTrue(false,"Exception");
		}
	}
	
    @Subject("sql����(BASIC1429)������{2,5}��ִ��2�β�����")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950022(){
		Step("����sql��������");
		changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "2,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
		
		Step("ִ��sql");
		try {
			for(int i=0;i<2;i++){
				Assert.isTrue(statement.execute("select * from test1"),"");
			}
		} catch (OutstripValveException e) {
			Logger.error(e.getMessage());
            Assert.isTrue(false,"�׳�OutstripValveException");
		} catch (Exception e) {
			Logger.error(e.getMessage());
			Assert.isTrue(false,"Exception");
		}
	}
      
}
