package com.alipay.zdal.valve.test.cases;

import static org.junit.Assert.assertTrue;

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
@Feature("��������:δ���û������ò��Ϸ��ַ���������")
public class VV950130 extends ValveBasicTest{
	Map<String, String> changeMap = new HashMap<String, String>();

	@Before
	public void onSetUp() {
		Step("onSetUp");
		initLocalTxDsDoMap();

		try {
			dataSource = new ZDataSource(LocalTxDsDoMap.get("ds-mysql"));
			connection = dataSource.getConnection();
			statement = connection.createStatement();
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
			changeMap.put(Parameter.TX_VALVE, "-1,-1");
			dataSource.flush(changeMap);
			
			dataSource.destroy();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Subject("��������������{a,1}������")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950131() {
		Step("����������������");
		changeMap.clear();
		changeMap.put(Parameter.TX_VALVE, "a,1");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ������");
		try {
				for(int i=0;i<2;i++){
					connection.setAutoCommit(false);
					assertTrue(statement.execute("select * from test1"));
					connection.commit();
				}
		} catch (OutstripValveException e) {
			Logger.error(e.getMessage());
			assertTrue("OutstripValveException", false);
		} catch (SQLException e) {
			Logger.error(e.getMessage());
			assertTrue("SQLException", false);
		} catch (Exception e) {
			Logger.error(e.getMessage());
			assertTrue("Exception", false);
		}
	}
	
	@Subject("��������������{-1,1}������")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950132() {
		Step("����������������");
		changeMap.clear();
		changeMap.put(Parameter.TX_VALVE, "-1,1");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ������");
		try {
				for(int i=0;i<2;i++){
					connection.setAutoCommit(false);
					assertTrue(statement.execute("select * from test1"));
					connection.commit();
				}
		} catch (OutstripValveException e) {
			Logger.error(e.getMessage());
			assertTrue("OutstripValveException", false);
		} catch (SQLException e) {
			Logger.error(e.getMessage());
			assertTrue("SQLException", false);
		} catch (Exception e) {
			Logger.error(e.getMessage());
			assertTrue("Exception", false);
		}
	}
	
	@Subject("��������������{1,0}������")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950133() {
		Step("����������������");
		changeMap.clear();
		changeMap.put(Parameter.TX_VALVE, "1,0");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ������");
		try {
				for(int i=0;i<2;i++){
					connection.setAutoCommit(false);
					assertTrue(statement.execute("select * from test1"));
					connection.commit();
				}
		} catch (OutstripValveException e) {
			Logger.error(e.getMessage());
			assertTrue("OutstripValveException", false);
		} catch (SQLException e) {
			Logger.error(e.getMessage());
			assertTrue("SQLException", false);
		} catch (Exception e) {
			Logger.error(e.getMessage());
			assertTrue("Exception", false);
		}
	}
	
	@Subject("��������������{1,61}������")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950134() {
		Step("����������������");
		changeMap.clear();
		changeMap.put(Parameter.TX_VALVE, "1,61");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ������");
		try {
				for(int i=0;i<2;i++){
					connection.setAutoCommit(false);
					assertTrue(statement.execute("select * from test1"));
					connection.commit();
				}
		} catch (OutstripValveException e) {
			Logger.error(e.getMessage());
			assertTrue("OutstripValveException", false);
		} catch (SQLException e) {
			Logger.error(e.getMessage());
			assertTrue("SQLException", false);
		} catch (Exception e) {
			Logger.error(e.getMessage());
			assertTrue("Exception", false);
		}
	}
	
	@Subject("��������������{1,A}������")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950135() {
		Step("����������������");
		changeMap.clear();
		changeMap.put(Parameter.TX_VALVE, "1,A");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ������");
		try {
				for(int i=0;i<2;i++){
					connection.setAutoCommit(false);
					assertTrue(statement.execute("select * from test1"));
					connection.commit();
				}
		} catch (OutstripValveException e) {
			Logger.error(e.getMessage());
			assertTrue("OutstripValveException", false);
		} catch (SQLException e) {
			Logger.error(e.getMessage());
			assertTrue("SQLException", false);
		} catch (Exception e) {
			Logger.error(e.getMessage());
			assertTrue("Exception", false);
		}
	}
	@Subject("���������������ã�����")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950136() {
		Step("����������������");
		changeMap.clear();
		changeMap.put(Parameter.TX_VALVE, "");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ������");
		try {
				for(int i=0;i<2;i++){
					connection.setAutoCommit(false);
					assertTrue(statement.execute("select * from test1"));
					connection.commit();
				}
		} catch (OutstripValveException e) {
			Logger.error(e.getMessage());
			assertTrue("OutstripValveException", false);
		} catch (SQLException e) {
			Logger.error(e.getMessage());
			assertTrue("SQLException", false);
		} catch (Exception e) {
			Logger.error(e.getMessage());
			assertTrue("Exception", false);
		}
	}
}
