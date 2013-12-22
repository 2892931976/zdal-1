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
@Feature("��������:��ʱ�����ڣ�sqlִ�д�����������������Ӧ����")
public class VV950140 extends ValveBasicTest{
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
	
	@Subject("��������(BASIC1628)�������������������")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950141() {
		Step("����������������");
		changeMap.clear();
		changeMap.put(Parameter.TX_VALVE, "1,5");
		changeMap.put(Parameter.TX_VALVE, "4,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Step("ִ������");
		try {
			clearRequestTime();
			for (int i = 0; i < 5; i++) {
				incremRequestTime();
				connection.setAutoCommit(false);
				assertTrue(statement.execute("select * from test1"));
				connection.commit();
			}
			fail("û��ס");
		} catch (OutstripValveException e) {
			Logger.info("***********" + getRequestTime());
			assertTrue("OutstripValveException", true);
			assertTrue("���ǵ�5�β�����", getRequestTime() == 5);
			Logger.info(e.getMessage());
		} catch (SQLException e) {
			assertTrue("SQLException", false);
			Logger.error(e.getMessage());
		} catch (Exception e) {
			assertTrue("Exception", false);
			Logger.error(e.getMessage());
		}
	}
	
	@Subject("��������(BASIC1579)�������������������")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950142() {
		Step("����������������");
		changeMap.clear();
		changeMap.put(Parameter.TX_VALVE, "3,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		currentTime = new Date().getTime();

		Step("ִ������");	
		//�������С���������ޣ������� 
		try {
			for(long i=0;i<2;i++){
				connection.setAutoCommit(false);
				statement.execute("delete from test1 where clum=1");
				connection.commit();
			}
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}finally{
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//�ȴ�һ�����ڣ�������������������ޣ������� 
		try {
			sleep(6000);
			for(long i=0;i<3;i++){
				connection.setAutoCommit(false);
				statement.execute("delete from test1 where clum=1");
				connection.commit();
			}
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}finally{
			try {
				sleep(6000);
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//�ȴ�һ�����ڣ�������´��������������ޣ�����
		try {
			sleep(6000);
			clearRequestTime();
			for(long i=0;i<4;i++){
				incremRequestTime();
				connection.setAutoCommit(false);
				statement.execute("delete from test1 where clum=1");
				connection.commit();
			}
			fail("ִ�е�" + getRequestTime() + "�����������δ�׳������쳣");
		} catch (OutstripValveException e) {
			try {
				sleep(6000);
				connection.setAutoCommit(true);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			assertTrue("���ǵ�4������ʼ����" + getRequestTime(),getRequestTime() == 4);
			assertTrue("�׳�OutstripValveException",true);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}finally{
			try {
				sleep(6000);
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//�ȴ�һ�����ڣ�������´��������������ޣ�setAutoCommitΪtrue��������
		try {
			sleep(6000);
			clearRequestTime();
			for(long i=0;i<4;i++){
				incremRequestTime();
				connection.setAutoCommit(true);
				statement.execute("delete from test1 where clum=1");
			}
		} catch (OutstripValveException e) {
			assertTrue("�����������4��",getRequestTime() == 4);
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
	}
}
