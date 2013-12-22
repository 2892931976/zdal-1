package com.alipay.zdal.test.ut.datasource;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.annotation.Tester;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;import static com.alipay.ats.internal.domain.ATS.*;
import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.ZDataSource;


@RunWith(ATSJUnitRunner.class)
@Feature("ZDataSource.checkParam��LocalTxDataSourceDOֻ���ñ������ԣ�LocalTxDataSourceDOΪ��")
public class ZDS951040 extends ZDSTest{
	
    @Subject("ֻ���û�������Դ���ԣ��ɴ�������Դ")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void testTC951041() {
    	Step("��������Դ");
    	LocalTxDataSourceDO localTxDSDo = new LocalTxDataSourceDO();
		ZDataSource zDataSource = null;
		
		localTxDSDo.setConnectionURL("jdbc:oracle:thin:@10.253.94.6:1521:perfdb6");
		localTxDSDo.setDriverClass("oracle.jdbc.OracleDriver");
		try {
			localTxDSDo.setEncPassword("-7cda29b2eef25d0e");
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}
		localTxDSDo.setExceptionSorterClassName("com.alipay.zdatasource.resource.adapter.jdbc.vendor.OracleExceptionSorter");
		localTxDSDo.setMaxPoolSize(12);
		localTxDSDo.setMinPoolSize(6);
		localTxDSDo.setPreparedStatementCacheSize(100);
		localTxDSDo.setUserName("ACM");
		localTxDSDo.setDsName("ds");

		try {
			zDataSource = new ZDataSource(localTxDSDo);
		} catch (Exception e) {
			Logger.error(e.getMessage());
			Assert.isTrue(false,"��������Դ�쳣");
		}
	}
	
    @Subject("LocalTxDataSourceDOΪnull�����ɴ�������Դ")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void testTC951042() {
    	Step("��������Դ");
    	LocalTxDataSourceDO localTxDSDo = null;
		ZDataSource zDataSource = null;		

		try {
			zDataSource = new ZDataSource(localTxDSDo);
			fail();
		} catch (Exception e) {
			Step("��֤");
			Logger.error(e.getMessage());
			Assert.isTrue(e.getClass().equals(IllegalArgumentException.class),"��֤�쳣��");
			Assert.isTrue(e.getMessage().equalsIgnoreCase("DO is null"),"��֤�쳣��Ϣ");
		}
	}
}
