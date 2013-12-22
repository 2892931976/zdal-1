package com.alipay.zdal.test.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.client.config.drm.ZdalSignalResource;
import com.alipay.zdal.client.jdbc.ZdalDataSource;
import com.alipay.zdal.common.DBType;
import com.alipay.zdal.common.jdbc.sorter.MySQLExceptionSorter;
import com.alipay.zdal.common.jdbc.sorter.OracleExceptionSorter;
import static com.alipay.ats.internal.domain.ATS.Step;



@RunWith(ATSJUnitRunner.class)
@Feature("�޸�mysql��oracle��  errorCode")
public class SR951110 {
	public TestAssertion Assert = new TestAssertion();
	private ZdalSignalResource zdalSignalResource; 
	private String resourceKey= "errorCode";
	
	@Subject("�޸�mysq��errorCode���޸ĳ���ֵ")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC951111(){
		Step("�޸�mysq��errorCode���޸ĳ���ֵ");
		ZdalDataSource zd = (ZdalDataSource) ZdalClientSuite.context
		.getBean("zdalClientPrefillshardfailoverzoneDsIsNull");
		Step("�����޸�");
		updateErrorCode(zd,"123456789","mysql");
		Assert.areEqual(MySQLExceptionSorter.ERRORCODE, Integer.parseInt("123456789"),"errorCode");		
		zdalSignalResource.updateResource(resourceKey, "-1");
			
	}
	
	@Subject("�޸�mysql��errorCode���޸ĳɷ���ֵ")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC951112(){
		Step("�޸�mysql��errorCode���޸ĳɷ���ֵ");
		ZdalDataSource zd = (ZdalDataSource) ZdalClientSuite.context
		.getBean("zdalClientPrefillshardfailoverzoneDsIsNull");
		Step("�޸�ֵ");
		int  value_before = MySQLExceptionSorter.ERRORCODE;
		updateErrorCode(zd,"a","mysql");
		Assert.areEqual(MySQLExceptionSorter.ERRORCODE, value_before,"errorCode");
	}
	
	@Subject("�޸�oracle��errorCode���޸ĳ���ֵ")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC951113(){
		Step("�޸�oracle��errorCode���޸ĳ���ֵ");
		ZdalDataSource zd = (ZdalDataSource) ZdalClientSuite.context
		.getBean("zdalClientPrefillshardOraclezoneDsIsNull");
		Step("�޸�ֵ");
		updateErrorCode(zd,"123456789","oracle");
		Assert.areEqual(OracleExceptionSorter.ERRORCODE, Integer.parseInt("123456789"),"errorCode");		
		zdalSignalResource.updateResource(resourceKey, "-1");
			
	}
	
	@Subject("�޸�oracle��errorCode���޸ĳɷ���ֵ")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC951114(){
		ZdalDataSource zd = (ZdalDataSource) ZdalClientSuite.context
		.getBean("zdalClientPrefillshardOraclezoneDsIsNull");
		
		int  value_before = OracleExceptionSorter.ERRORCODE;
		updateErrorCode(zd,"a","oracle");
		Assert.areEqual(OracleExceptionSorter.ERRORCODE, value_before,"errorCode");				
		
	}
	
	/**
	 * �޸�errorCode�Ĺ�������
	 * @param zd
	 * @param value
	 * @param type
	 */
	public void updateErrorCode(ZdalDataSource zd,String value,String type){
		if("mysql".equalsIgnoreCase(type)){
			zdalSignalResource=new ZdalSignalResource(zd.getZdalConfigListener(), "zdal-test",DBType.MYSQL);
		}else{
			zdalSignalResource=new ZdalSignalResource(zd.getZdalConfigListener(), "zdal-test",DBType.ORACLE);
		}

		zdalSignalResource.updateResource(resourceKey, value);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	


}
