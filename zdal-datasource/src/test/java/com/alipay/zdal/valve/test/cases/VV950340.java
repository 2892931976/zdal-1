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
import com.alipay.zdal.valve.Valve;
import com.alipay.zdal.valve.test.utils.ValveBasicTest;
import com.alipay.zdal.valve.util.OutstripValveException;

@RunWith(ATSJUnitRunner.class)
@Feature("�����������")
public class VV950340 extends ValveBasicTest{
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
			changeMap.put(Parameter.SQL_VALVE, "-1,-1");
			changeMap.put(Parameter.TX_VALVE, "-1,-1");
			changeMap.put(Parameter.TABLE_VALVE, tbName+",-1,-1");
			dataSource.flush(changeMap);

			dataSource.destroy();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Subject("�����������")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test
	public void TC950341() {
		Step("������������");
		changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "5,3");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Step("ִ��sql");	
		try {
			clearRequestTime();
			for(long i=0;i<6;i++){
				incremRequestTime();
				statement.execute("select * from " + tbName);
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			Logger.info(e.getMessage());
			assertTrue("����SQL����",e.getMessage().contains("SQLִ�г���ָ����ֵ"));
			assertTrue("���ǵ�6������ʼ����",getRequestTime() == 6);
			assertTrue("�׳�OutstripValveException",true);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}		
		
		Step("�����������Ϊ������");
		changeMap.clear();
		changeMap.put(Parameter.TABLE_VALVE, tbName+",2,3");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Step("��֤����");
		Valve valve2 = dataSource.getValve();
		Assert.isTrue(valve2.getSqlValve().getPeriod()==3l && valve2.getSqlValve().getThreshold()==5l, "��֤sql-valve����");
		Assert.isTrue(valve2.getTXValve().getPeriod()==-1l && valve2.getTXValve().getThreshold()==-1l, "��֤tx-valve����");
		Assert.isTrue(valve2.getTableValve(tbName).getPeriod()==3l && valve2.getTableValve(tbName).getThreshold()==2l, "��֤table-valve����");
		
		Step("ִ��sql");	
		try {
			clearRequestTime();
			for(long i=0;i<3;i++){
				incremRequestTime();
				statement.execute("select * from " + tbName);
			}
			fail("δ�׳������쳣");
		} catch (OutstripValveException e) {
			Logger.info(e.getMessage());
			assertTrue("���Ǳ�����",e.getMessage().contains("��" + tbName.trim() + "����ִ�г���ָ����ֵ"));
			assertTrue("��"+getRequestTime() +"������ʼ����",getRequestTime() == 3);
			assertTrue("�׳�OutstripValveException",true);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
		
		Step("�����������������������");
		changeMap.clear();
		changeMap.put(Parameter.TX_VALVE, "2,3");
		changeMap.put(Parameter.TABLE_VALVE, tbName+",3,5");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Step("��֤����");
		Valve valve3 = dataSource.getValve();
		Assert.isTrue(valve3.getSqlValve().getPeriod()==3l && valve3.getSqlValve().getThreshold()==5l, "��֤sql-valve����");
		Assert.isTrue(valve3.getTXValve().getPeriod()==3l && valve3.getTXValve().getThreshold()==2l, "��֤tx-valve����");
		Assert.isTrue(valve3.getTableValve(tbName).getPeriod()==5l && valve3.getTableValve(tbName).getThreshold()==3l, "��֤table-valve����");
		
		Step("�����������Ϊ��");
		changeMap.clear();
		changeMap.put(Parameter.SQL_VALVE, "");
		changeMap.put(Parameter.TABLE_VALVE, "");
		changeMap.put(Parameter.TX_VALVE, "");
		dataSource.flush(changeMap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Step("ִ��sql");	
		try {
			clearRequestTime();
			for(long i=0;i<4;i++){
				incremRequestTime();
				statement.execute("select * from " + tbName);
			}
		} catch (OutstripValveException e) {
			assertTrue("�׳�OutstripValveException",false);
		} catch (Exception e) {
			assertTrue("Exception",false);
		}
	}
}
