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
public class VV950210 extends ValveBasicTest{
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
	
	@Subject("������(BASIC1619)������{tbName,1,5}����2�α���")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950211() {
		Step("���ñ���������");
		changeMap.clear();
		changeMap.put(Parameter.TABLE_VALVE, tbName + ",1,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ��sql");
		clearRequestTime();
		try {
			for (int i = 0; i < 2; i++) {
				incremRequestTime();
				assertTrue(statement.execute("SELECT * FROM " + "`" + tbName
						+ "`"));				
			}
			fail("û��ס");
		} catch (OutstripValveException e) {
			Assert.areEqual(OutstripValveException.class,e.getClass(),"��֤�쳣��");
			Assert.areEqual(2L,getRequestTime(),"�Ƿ��2������");
		} catch (SQLException e) {
			Assert.isTrue(false, "SQLException");
		} catch (Exception e) {
			Assert.isTrue(false, "Exception");
		}
	}
		
	@Subject("������������{tbName,0,5}������������Сֵ����1�α���")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950212() {
		Step("���ñ���������");
		changeMap.clear();
		changeMap.put(Parameter.TABLE_VALVE, tbName + ",0,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ��sql");
		clearRequestTime();
		try {
			for (int i = 0; i < 1; i++) {
				incremRequestTime();
				assertTrue(statement.execute("SELECT * FROM " + "`" + tbName
						+ "`"));
				
			}
			fail("û��ס");
		} catch (OutstripValveException e) {
			Assert.areEqual(OutstripValveException.class, e.getClass(),"��֤�쳣��");
			Assert.areEqual(1L,getRequestTime(),"�Ƿ��1������");
		} catch (SQLException e) {
			Assert.isTrue(false, "SQLException");
		} catch (Exception e) {
			Assert.isTrue(false, "Exception");
		}
	}
	
	@Subject("������������{tbName,2,1}��ʱ������Сֵ����3�α���")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950213() {
		Step("���ñ���������");
		changeMap.clear();
		changeMap.put(Parameter.TABLE_VALVE, tbName + ",2,1");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ��sql");
		clearRequestTime();
		try {
			for (int i = 0; i < 3; i++) {
				incremRequestTime();
				assertTrue(statement.execute("SELECT * FROM " + "`" + tbName
						+ "`"));
				
			}
			fail("û��ס");
		} catch (OutstripValveException e) {
			Assert.areEqual(OutstripValveException.class, e.getClass(),"��֤�쳣��");
			Assert.areEqual(3L, getRequestTime(),"�Ƿ��3������");
		} catch (SQLException e) {
			Assert.isTrue(false, "SQLException");
		} catch (Exception e) {
			Assert.isTrue(false, "Exception");
		}
	}
	
	@Subject("������������{tbName,1,60}��ʱ�������ֵ����2�α���")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950214() {
		Step("���ñ���������");
		changeMap.clear();
		changeMap.put(Parameter.TABLE_VALVE, tbName + ",1,60");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ��sql");
		clearRequestTime();
		try {
			for (int i = 0; i < 2; i++) {
				incremRequestTime();
				assertTrue(statement.execute("SELECT * FROM " + "`" + tbName
						+ "`"));
				Thread.sleep(58000);
				
			}
			fail("û��ס");
		} catch (OutstripValveException e) {
			Assert.areEqual(OutstripValveException.class,e.getClass(),"��֤�쳣��");
			Assert.areEqual(2L,getRequestTime(),"�Ƿ��2������");
		} catch (SQLException e) {
			Assert.isTrue(false, "SQLException");
		} catch (Exception e) {
			Assert.isTrue(false, "Exception");
		}
	}
	
	@Subject("������(BASIC1620)���ظ����ò���{tbName,1,5}����2�α���")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950215() {
		Step("���ñ���������");
		changeMap.clear();
		changeMap.put(Parameter.TABLE_VALVE, tbName + ",1,5;" + tbName + ",1,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ��sql");
		clearRequestTime();
		try {
			for (int i = 0; i < 2; i++) {
				incremRequestTime();
				assertTrue(statement.execute("SELECT * FROM " + "`" + tbName
						+ "`"));
				
			}
			fail("û��ס");
		} catch (OutstripValveException e) {
			Assert.areEqual(OutstripValveException.class,e.getClass(),"��֤�쳣��");
			Assert.areEqual(2L,getRequestTime(),"�Ƿ��2������");
			Logger.info(e.getMessage());
		} catch (SQLException e) {
			Assert.isTrue(false, "SQLException");
		} catch (Exception e) {
			Assert.isTrue(false, "Exception");
		}
	}
	
	@Subject("������������{tbName,2,60}�����������+ʱ�������ֵ����301����")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950216() {
		Step("���ñ���������");
		changeMap.clear();
		changeMap.put(Parameter.TABLE_VALVE, tbName + ",2,60");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ��sql");
		clearRequestTime();
		try {
			for (int i = 0; i < 3; i++) {
				incremRequestTime();
				assertTrue(statement.execute("SELECT * FROM " + "`" + tbName
						+ "`"));
				
			}
			fail("û��ס");
		} catch (OutstripValveException e) {
			Assert.areEqual(OutstripValveException.class, e.getClass(),"��֤�쳣��");
			Assert.areEqual(3L,getRequestTime(),"�Ƿ��301������");
		} catch (SQLException e) {
			Assert.isTrue(false, "SQLException");
		} catch (Exception e) {
			Assert.isTrue(false, "Exception");
		}
	}
}
