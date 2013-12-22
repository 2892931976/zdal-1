package com.alipay.zdal.valve.test.cases;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
@Feature("��������:��ʱ�����ڣ�����ִ�д�����������������Ӧ����")
public class VV950110  extends ValveBasicTest{
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
			changeMap.put(Parameter.TX_VALVE, "-1,-1");
			dataSource.flush(changeMap);
			
			dataSource.destroy();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Subject("��������(BASIC1618)������{1,5}����2��ִ�б���")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950111() {
		Step("����������������");
		changeMap.clear();
		changeMap.put(Parameter.TX_VALVE, "1,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ������");
		clearRequestTime();
		try {
			for (int i = 0; i < 2; i++) {
				incremRequestTime();
				connection.setAutoCommit(false);
				assertTrue(statement.execute("select * from test1"));
				connection.commit();
			}
			fail("û��ס");
		} catch (OutstripValveException e) {
			Logger.info(e.getMessage());
			assertTrue("OutstripValveException", true);
			assertTrue("��" + getRequestTime() + "�����쳣", getRequestTime() == 2);
		} catch (SQLException e) {
			Logger.error(e.getMessage());
			assertTrue("SQLException", false);
		} catch (Exception e) {
			Logger.error(e.getMessage());
			assertTrue("Exception", false);
		}
	}

	@Subject("��������������{0,5}��ȫ������")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950112() {
		Step("����������������");
		changeMap.clear();
		changeMap.put(Parameter.TX_VALVE, "0,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ������");
		try {
			clearRequestTime();
			for (long i = 0; i < 1; i++) {
				incremRequestTime();
				connection.setAutoCommit(false);
				statement.execute("select * from test1");
				connection.commit();
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			Logger.info(e.getMessage());
			assertTrue("��" + getRequestTime() + "1������ʼ����",
					getRequestTime() == 1);
			assertTrue("�׳�OutstripValveException", true);
		} catch (Exception e) {
			assertTrue("Exception", false);
		}
	}
		
	@Subject("��������(BASIC1684)������{2,60}����3��ִ�б���")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950113() {
		Step("����������������");
		changeMap.clear();
		changeMap.put(Parameter.TX_VALVE, "2,60");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ������");
		try {
			clearRequestTime();
			for (long i = 0; i < 3; i++) {
				incremRequestTime();
				connection.setAutoCommit(false);
				statement.execute("select * from test1");
				connection.commit();
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			Logger.info(e.getMessage());
			assertTrue("��"+getRequestTime()+"������ʼ����", getRequestTime() == 3);
			assertTrue("�׳�OutstripValveException", true);
		} catch (Exception e) {
			assertTrue("Exception", false);
		}
	}

	@Subject("��������������{2,1}����3��ִ�б���")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950114() {
		Step("����������������");
		changeMap.clear();
		changeMap.put(Parameter.TX_VALVE, "2,1");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ������");
		try {
			clearRequestTime();
			for (long i = 0; i < 3; i++) {
				incremRequestTime();
				connection.setAutoCommit(false);
				statement.execute("select * from test1");
				connection.commit();
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			Logger.info(e.getMessage());
			assertTrue("��"+getRequestTime()+"������ʼ����", getRequestTime() == 3);
			assertTrue("�׳�OutstripValveException", true);
		} catch (Exception e) {
			assertTrue("Exception", false);
		}
	}
	
	@Subject("��������������{60,60}����61��ִ�б���")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950115() {
		Step("����������������");
		changeMap.clear();
		changeMap.put(Parameter.TX_VALVE, "60,60");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ������");
		try {
			clearRequestTime();
			for (long i = 0; i < 61; i++) {
				incremRequestTime();
				connection.setAutoCommit(false);
				statement.execute("select * from test1");
				connection.commit();
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			Logger.info(e.getMessage());
			assertTrue("��"+getRequestTime()+"������ʼ����", getRequestTime() == 61);
			assertTrue("�׳�OutstripValveException", true);
		} catch (Exception e) {
			assertTrue("Exception", false);
		}
	}
}
