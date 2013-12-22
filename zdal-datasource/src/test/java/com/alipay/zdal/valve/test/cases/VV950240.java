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
@Feature("table����:��ʱ�����ڣ�ִ�д�����������������Ӧ����")
public class VV950240 extends ValveBasicTest{
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
			changeMap.put(Parameter.TABLE_VALVE, tbName+",-1,-1");
			dataSource.flush(changeMap);
			
			dataSource.destroy();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Subject("������(BASIC1581)����ʱ����������")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950241() {
		Step("���ñ���������");
		changeMap.clear();
		changeMap.put(Parameter.TABLE_VALVE, tbName+",3,3");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ��sql:���������С������ֵ������");
		try {
			for(long i=0;i<2;i++){
				statement.execute("select * from " + tbName);
			}
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",false);
			Logger.error(e.getMessage());
		} catch (Exception e) {
			assertTrue("Exception",false);
			Logger.error(e.getMessage());
		}
		
		Step("ִ��sql:�����������������ֵ������");
		try {
			sleep(4000);
			for(long i=0;i<3;i++){
				statement.execute("select * from " + tbName);
			}
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",false);
			Logger.error(e.getMessage());
		} catch (Exception e) {
			assertTrue("Exception",false);
			Logger.error(e.getMessage());
		}
		
		Step("ִ��sql:�����������������ֵ������");
		try {
			sleep(4000);
			clearRequestTime();
			for(long i=0;i<4;i++){
				incremRequestTime();
				statement.execute("select * from " + tbName);
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			assertTrue("���ǵ�4������ʼ����,getRequestTime()=" + getRequestTime(), getRequestTime() == 4);
			assertTrue("�׳�OutstripValveException",true);
			Logger.info(e.getMessage());
		} catch (Exception e) {
			assertTrue("Exception",false);
			Logger.error(e.getMessage());
		}
	}
	
	@Subject("������(BASIC1631)������������ã�����")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950242() {
		Step("���ñ���������");
		changeMap.clear();
		changeMap.put(Parameter.TABLE_VALVE, tbName+",1,5");
		changeMap.put(Parameter.TABLE_VALVE, tbName+",4,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ��sql");
		try {
			clearRequestTime();
			for(int i=0;i<5;i++){
				incremRequestTime();
				assertTrue(statement.execute("SELECT * from `" + tbName + "`"));
			}
			fail("û��ס");
		} catch (OutstripValveException e) {
			assertTrue("���ǵ�5�β�����",super.getRequestTime() == 5);
			assertTrue("OutstripValveException",true);
			Logger.info(e.getMessage());
		} catch (SQLException e) {
			assertTrue("SQLException",false);
			Logger.error(e.getMessage());
		} catch (Exception e) {
			assertTrue("Exception",false);
			Logger.error(e.getMessage());
		}
	}
}
