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
@Feature("table����:δ���û������ò��Ϸ��ַ���������")
public class VV950230 extends ValveBasicTest{
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

	@Subject("������������{tbName,A,1}��������")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950231() {
		Step("���ñ���������");
		changeMap.clear();
		changeMap.put(Parameter.TABLE_VALVE, tbName + ",A,1");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ��sql");
		try {
			for (int i = 0; i < 2; i++) {
				assertTrue(statement.execute("SELECT * FROM " + "`" + tbName
						+ "`"));
			}
		} catch (OutstripValveException e) {
			assertTrue("OutstripValveException", false);
			Logger.error(e.getMessage());
		} catch (SQLException e) {
			assertTrue("SQLException", false);
			Logger.error(e.getMessage());
		} catch (Exception e) {
			assertTrue("Exception", false);
			Logger.error(e.getMessage());
		}
	}
	
	@Subject("������������{tbName,1,A}��������")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950232() {
		Step("���ñ���������");
		changeMap.clear();
		changeMap.put(Parameter.TABLE_VALVE, tbName + ",1,A");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ��sql");
		try {
			for (int i = 0; i < 2; i++) {
				assertTrue(statement.execute("SELECT * FROM " + "`" + tbName
						+ "`"));
			}
		} catch (OutstripValveException e) {
			assertTrue("OutstripValveException", false);
			Logger.error(e.getMessage());
		} catch (SQLException e) {
			assertTrue("SQLException", false);
			Logger.error(e.getMessage());
		} catch (Exception e) {
			assertTrue("Exception", false);
			Logger.error(e.getMessage());
		}
	}
	
	@Subject("������������{tbName,-1,1}��������")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950233() {
		Step("���ñ���������");
		changeMap.clear();
		changeMap.put(Parameter.TABLE_VALVE, tbName + ",-1,1");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ��sql");
		try {
			for (int i = 0; i < 2; i++) {
				assertTrue(statement.execute("SELECT * FROM " + "`" + tbName
						+ "`"));
			}
		} catch (OutstripValveException e) {
			assertTrue("OutstripValveException", false);
			Logger.error(e.getMessage());
		} catch (SQLException e) {
			assertTrue("SQLException", false);
			Logger.error(e.getMessage());
		} catch (Exception e) {
			assertTrue("Exception", false);
			Logger.error(e.getMessage());
		}
	}
	
	@Subject("������������{tbName,1,0}��������")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950234() {
		Step("���ñ���������");
		changeMap.clear();
		changeMap.put(Parameter.TABLE_VALVE, tbName + ",1,0");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ��sql");
		try {
			for (int i = 0; i < 2; i++) {
				assertTrue(statement.execute("SELECT * FROM " + "`" + tbName
						+ "`"));
			}
		} catch (OutstripValveException e) {
			assertTrue("OutstripValveException", false);
			Logger.error(e.getMessage());
		} catch (SQLException e) {
			assertTrue("SQLException", false);
			Logger.error(e.getMessage());
		} catch (Exception e) {
			assertTrue("Exception", false);
			Logger.error(e.getMessage());
		}
	}
	
	@Subject("�������������ã�������")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950235() {
		Step("���ñ���������");
		changeMap.clear();
		changeMap.put(Parameter.TABLE_VALVE, "");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ��sql");
		try {
			for (int i = 0; i < 2; i++) {
				assertTrue(statement.execute("SELECT * FROM " + "`" + tbName
						+ "`"));
			}
		} catch (OutstripValveException e) {
			assertTrue("OutstripValveException", false);
			Logger.error(e.getMessage());
		} catch (SQLException e) {
			assertTrue("SQLException", false);
			Logger.error(e.getMessage());
		} catch (Exception e) {
			assertTrue("Exception", false);
			Logger.error(e.getMessage());
		}
	}
}
