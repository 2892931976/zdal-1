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
@Feature("sql����:ִ��select��delete")
public class VV950070 extends ValveBasicTest{	
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
			changeMap.put(Parameter.SQL_VALVE, "-1,-1");
			dataSource.flush(changeMap);
			
			dataSource.destroy();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
    @Subject("sql����(BASIC1573)�����ò���{3,5}����ʱ����������ִ��select")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950071(){
    	Step("����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "3,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		currentTime = new Date().getTime();

		Step("ִ��sql");
    	//��ѯ����С����������
		try {
			for(long i=0;i<2;i++){
				statement.execute("select * from test1");
			}
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue("Exception",false);
		}
		//�ȴ�һ�����ڣ���ѯ���������������ޣ������� 1574
		try {
			sleep(6000);
			for(long i=0;i<3;i++){
				statement.execute("select * from test1");
			}
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
		//�ȴ�һ�����ڣ���ѯ���������������ޣ����� 1575
		try {
			sleep(6000);
			clearRequestTime();
			for(long i=0;i<4;i++){
				incremRequestTime();
				statement.execute("select * from test1");
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			assertTrue("���ǵ�4������ʼ����",getRequestTime() == 4);
			assertTrue("�׳�OutstripValveException",true);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
	}
    
    @Subject("sql����(BASIC1576)�����ò���{3,5}����ʱ����������ִ��delete")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void TC950072(){
    	Step("����sql��������");
    	changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "3,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		currentTime = new Date().getTime();

		Step("ִ��sql");
    	//���´���С����������
		try {
			for(long i=0;i<2;i++){
				statement.execute("delete from test1 where clum = 1");
			}
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue("Exception",false);
		}
		//�ȴ�һ�����ڣ����´��������������ޣ������� 1574
		try {
			sleep(6000);
			for(long i=0;i<3;i++){
				statement.execute("delete from test1 where clum = 1");
			}
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
		//�ȴ�һ�����ڣ����´��������������ޣ����� 1575
		try {
			sleep(6000);
			clearRequestTime();
			for(long i=0;i<4;i++){
				incremRequestTime();
				statement.execute("delete from test1 where clum = 1");
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			assertTrue("���ǵ�4������ʼ����",getRequestTime() == 4);
			assertTrue("�׳�OutstripValveException",true);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
	}
}