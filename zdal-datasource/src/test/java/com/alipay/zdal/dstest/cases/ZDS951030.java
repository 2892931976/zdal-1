package com.alipay.zdal.dstest.cases;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.annotation.Tester;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;import static com.alipay.ats.internal.domain.ATS.*;

import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.ZDataSource;
import com.alipay.zdal.dstest.utils.ZDSTest;

/**
 *ZDataSource.checkParam��LocalTxDataSourceDO������Ϊ�� 
 * @author yin.meng
 *
 */
@RunWith(ATSJUnitRunner.class)
@Feature("ZDataSource.checkParam��LocalTxDataSourceDO��һ����Ϊ��")
public class ZDS951030  extends ZDSTest{
	
    @Subject("δ����dsName")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void testTC951031() {
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

		try {
			zDataSource = new ZDataSource(localTxDSDo);
			fail();
		} catch (Exception e) {
			Step("��֤");
			Logger.error(e.getMessage());
			Assert.isTrue(e.getClass().equals(IllegalArgumentException.class),"��֤�쳣��");
			Assert.isTrue(e.getMessage().equalsIgnoreCase("DsName is null"),"��֤�쳣��Ϣ");
		}
	}
	
	
    @Subject("δ����connection url")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void testTC951032() {
    	Step("��������Դ");
    	LocalTxDataSourceDO localTxDSDo = new LocalTxDataSourceDO();
		ZDataSource zDataSource = null;
		
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
			fail();
		} catch (Exception e) {
	    	Step("��֤");
			Logger.error(e.getMessage());
			Assert.isTrue(e.getClass().equals(IllegalArgumentException.class),"��֤�쳣��");
			Assert.isTrue(e.getMessage().equalsIgnoreCase("connection URL is null"),"��֤�쳣��Ϣ");
		}
	}
	
	
    @Subject("δ����userName")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void testTC951033() {		
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
		localTxDSDo.setDsName("ds");

		try {
			zDataSource = new ZDataSource(localTxDSDo);
			fail();
		} catch (Exception e) {
	    	Step("��֤");
			Logger.error(e.getMessage());
			Assert.isTrue(e.getClass().equals(IllegalArgumentException.class),"��֤�쳣��");
			Assert.isTrue(e.getMessage().equalsIgnoreCase("username is null"),"��֤�쳣��Ϣ");
		}
	}
	
	
    @Subject("δ����driverClass")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void testTC951034() {
    	Step("��������Դ");
    	LocalTxDataSourceDO localTxDSDo = new LocalTxDataSourceDO();
		ZDataSource zDataSource = null;
		
		localTxDSDo.setConnectionURL("jdbc:oracle:thin:@10.253.94.6:1521:perfdb6");
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
			fail();
		} catch (Exception e) {
	    	Step("��֤");
			Logger.error(e.getMessage());
			Assert.isTrue(e.getClass().equals(IllegalArgumentException.class),"��֤�쳣��");
			Assert.isTrue(e.getMessage().equalsIgnoreCase("driverClass is null"),"��֤�쳣��Ϣ");
		}
	}	

	
    @Subject("δ����pwd�� encPwd")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void testTC951035() {
    	Step("��������Դ");
    	LocalTxDataSourceDO localTxDSDo = new LocalTxDataSourceDO();
		ZDataSource zDataSource = null;

		localTxDSDo.setConnectionURL("jdbc:oracle:thin:@10.253.94.6:1521:perfdb6");
		localTxDSDo.setDriverClass("oracle.jdbc.OracleDriver");
		localTxDSDo.setExceptionSorterClassName("com.alipay.zdatasource.resource.adapter.jdbc.vendor.OracleExceptionSorter");
		localTxDSDo.setMaxPoolSize(12);
		localTxDSDo.setMinPoolSize(6);
		localTxDSDo.setPreparedStatementCacheSize(100);
		localTxDSDo.setUserName("ACM");
		localTxDSDo.setDsName("ds");

		try {
			zDataSource = new ZDataSource(localTxDSDo);
			fail();
		} catch (Exception e) {
	    	Step("��֤");
			Logger.error(e.getMessage());
			Assert.isTrue(e.getClass().equals(IllegalArgumentException.class),"��֤�쳣��");
			Assert.isTrue(e.getMessage().equalsIgnoreCase("both pwd and encPwd are null"),"��֤�쳣��Ϣ");
		}
	}
	
	
    @Subject("δ����minSize")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void testTC951036() {
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
		localTxDSDo.setPreparedStatementCacheSize(100);
		localTxDSDo.setUserName("ACM");
		localTxDSDo.setDsName("ds");

		try {
			zDataSource = new ZDataSource(localTxDSDo);
			fail();
		} catch (Exception e) {
	    	Step("��֤");
			Logger.error(e.getMessage());
			Assert.isTrue(e.getClass().equals(IllegalArgumentException.class),"��֤�쳣��");
			Assert.isTrue(e.getMessage().equalsIgnoreCase("minSize is unset"),"��֤�쳣��Ϣ");
		}
	}
	
	
    @Subject("δ����maxSize")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void testTC951037() {
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
		localTxDSDo.setMinPoolSize(6);
		localTxDSDo.setPreparedStatementCacheSize(100);
		localTxDSDo.setUserName("ACM");
		localTxDSDo.setDsName("ds");

		try {
			zDataSource = new ZDataSource(localTxDSDo);
			fail();
		} catch (Exception e) {
	    	Step("��֤");
			Logger.error(e.getMessage());
			Assert.isTrue(e.getClass().equals(IllegalArgumentException.class),"��֤�쳣��");
			Assert.isTrue(e.getMessage().equalsIgnoreCase("maxSize is unset"),"��֤�쳣��Ϣ");
		}
	}
	
	
    @Subject("δ����preparedStatementCacheSize")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void testTC951038() {
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
		localTxDSDo.setUserName("ACM");
		localTxDSDo.setDsName("ds");

		try {
			zDataSource = new ZDataSource(localTxDSDo);
			fail();
		} catch (Exception e) {
	    	Step("��֤");
			Logger.error(e.getMessage());
			Assert.isTrue(e.getClass().equals(IllegalArgumentException.class),"��֤�쳣��");
			Assert.isTrue(e.getMessage().equalsIgnoreCase("preparedStatementCacheSize is unset"),"��֤�쳣��Ϣ");
		}
	}
	
    @Subject("δ����ExceptionSorterClassName")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void testTC951039() {
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
		localTxDSDo.setMaxPoolSize(12);
		localTxDSDo.setMinPoolSize(6);
		localTxDSDo.setPreparedStatementCacheSize(100);
		localTxDSDo.setUserName("ACM");
		localTxDSDo.setDsName("ds");

		try {
			zDataSource = new ZDataSource(localTxDSDo);
			fail();
		} catch (Exception e) {
	    	Step("��֤");
			Logger.error(e.getMessage());
			Assert.isTrue(e.getClass().equals(IllegalArgumentException.class),"��֤�쳣��");
			Assert.isTrue(e.getMessage().equalsIgnoreCase("ExceptionSorterClassName is null"),"��֤�쳣��Ϣ");
		}
	}
}
