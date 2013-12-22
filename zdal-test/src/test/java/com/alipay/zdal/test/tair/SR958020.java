package com.alipay.zdal.test.tair;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.tair.shard.impl.ZdalTairShardingViolationException;


@RunWith(ATSJUnitRunner.class)
@Feature("tair��zone��,ͨ�����ػ�ȥ��ʼ��,��Ϊ˫д")
public class SR958020 {
	TestAssertion Assert = new TestAssertion();
	
	/**
	@Subject("tair��zone˫д,��дidc��ldc,�������ɹ�")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958021(){
		String va = (String)
        TestBaseOne.getInstanceWithLocalpath(
        "doubleWriteLdcInitFromLocal01", "test", "./config/tair",
        "12345678909876553", "555555", "testdouble01");
		
		Assert.areEqual("testdouble01", va, "��֤�洢��tair��ֵ");
		
	}
	

	@Subject("tair��zone˫д,��дidc��ldc,������ʧ��")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958022(){
		
        try {
			TestBaseOne.getInstanceWithLocalpathException(
			"doubleWriteLdcInitFromLocal02", "test", "./config/tair",
			"12345678909876553", "555555", "testdouble02");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.areEqual(ZdalTairShardingViolationException.class, e.getClass(), "�쳣");
		}	
		
	}
	
	
	@Subject("tair��zone˫д,��дidc��ldc,����һ���ɹ�����һ��ʧ��(һ�����Ӳ���,��һ������������)")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958023(){
		try {
			TestBaseOne.getInstanceWithLocalpathException(
			"doubleWriteLdcInitFromLocal03", "test", "./config/tair",
			"12345678909876553", "555555", "testdouble03");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.areEqual(ZdalTairShardingViolationException.class, e.getClass(), "�쳣");
		}	
	}
	
	@Subject("tair��zone˫д,��дidc��ldc,����һ���ɹ�����һ��ʧ��(һ��дʧ��namespace����,��һ��д�ɹ�)")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958024(){
		try {
			TestBaseOne.getInstanceWithLocalpath(
			"doubleWriteLdcInitFromLocal04", "test", "./config/tair",
			"12345678909876553", "555555", "testdouble03");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.areEqual(ZdalTairShardingViolationException.class, e.getClass(), "�쳣");
		}	
	}
	*/
	
	@Subject("tair��zone˫д,��дidc��ldc,����һ���ɹ�����һ��ʧ��(һ��дʧ��namespaceΪ99999,��һ��д�ɹ�)")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958025(){
		try {
			TestBaseOne.getInstanceWithLocalpath(
			"doubleWriteLdcInitFromLocal05", "test", "./config/tair",
			"12345678909876553", "555555", "testdouble03");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.areEqual(ZdalTairShardingViolationException.class, e.getClass(), "�쳣");
		}	
	}
	
	
	
	
	@Subject("tair��zone˫д,��дidc�Ǳ�дldc��дidc�ɹ���дldc�ɹ�")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC95802j(){
		
	}
	
	@Subject("tair��zone˫д,��дidc�Ǳ�дldc��дidc�ɹ�����дldcʧ��")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC95802i(){
		
	}
	
	@Subject("tair��zone˫д,��дidc�Ǳ�дldc��дidcʧ�ܣ���дldc�ɹ�")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958026(){
		
	}
	
	@Subject("tair��zone˫д,��дidc�Ǳ�дldc��дidcʧ�ܣ�дldcʧ��")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958027(){
		
	}
	
	@Subject("tair��zone˫д,�Ǳ�дidc����дldc��дidc�ɹ���дldc�ɹ�")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958028(){
		
	}
	
	@Subject("tair��zone˫д,�Ǳ�дidc����дldc��дidc�ɹ���дldcʧ��")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958029(){
		
	}
	
	@Subject("tair��zone˫д,�Ǳ�дidc����дldc��дidcʧ�ܣ�дldcʧ��")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC95802a(){
		
	}
	
	@Subject("tair��zone˫д,�Ǳ�дidc����дldc��дidcʧ�ܣ�дldc�ɹ�")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC95802b(){
		
	}

}
