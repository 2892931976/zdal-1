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
@Feature("table����:��ʱ�����ڣ�ִ�д���С�ڵ�������������������")
public class VV950220  extends ValveBasicTest{
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

	@Subject("������������{tbName,1,5}����1�β�����")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950221() {
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
		try {
			for (int i = 0; i < 1; i++) {
				assertTrue(statement.execute("SELECT * FROM " + "`" + tbName
						+ "`"));
			}
		} catch (OutstripValveException e) {
			Assert.isTrue(false, "OutstripValveException");
		} catch (SQLException e) {
			Assert.isTrue(false, "SQLException");
		} catch (Exception e) {
			Assert.isTrue(false, "Exception");
		}
	}
	
	@Subject("������������{tbName,2,1}��ʱ������Сֵ����2�β�����")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950222() {
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
		try {
			for (int i = 0; i < 2; i++) {
				assertTrue(statement.execute("SELECT * FROM " + "`" + tbName
						+ "`"));
			}
		} catch (OutstripValveException e) {
			Assert.isTrue(false, "OutstripValveException");
		} catch (SQLException e) {
			Assert.isTrue(false, "SQLException");
		} catch (Exception e) {
			Assert.isTrue(false, "Exception");
		}
	}
	
	@Subject("������������{tbName,1,60}��ʱ�������ֵ����1�β�����")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950223() {
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
		try {
			for (int i = 0; i < 1; i++) {
				Thread.sleep(58000);
				assertTrue(statement.execute("SELECT * FROM " + "`" + tbName
						+ "`"));
			}
		} catch (OutstripValveException e) {
			Assert.isTrue(false, "OutstripValveException");
		} catch (SQLException e) {
			Assert.isTrue(false, "SQLException");
		} catch (Exception e) {
			Assert.isTrue(false, "Exception");
		}
	}
	
	@Subject("������������{tbName,2,60}�����������+ʱ�������ֵ����299�β�����")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950224(){
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
		try {
			for (int i = 0; i < 3; i++) {
				assertTrue(statement.execute("SELECT * FROM " + "`" + tbName
						+ "`"));
			}
		} catch (OutstripValveException e) {
			Assert.isTrue(false, "OutstripValveException");
		} catch (SQLException e) {
			Assert.isTrue(false, "SQLException");
		} catch (Exception e) {
			Assert.isTrue(false, "Exception");
		}
	}
}
