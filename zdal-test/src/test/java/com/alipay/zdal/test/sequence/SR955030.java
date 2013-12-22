package com.alipay.zdal.test.sequence;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.test.common.ConstantsTest;
import static com.alipay.ats.internal.domain.ATS.*;



@RunWith(ATSJUnitRunner.class)
@Feature("multipleSequenceFactory:��ʼ��Sequence�쳣")
public class SR955030 {
    public TestAssertion Assert = new TestAssertion();
	String sequenceName="sequence_BCD1";
	String url = ConstantsTest.mysql12UrlSequence0;
	String user = ConstantsTest.mysq112User;
	String psw = ConstantsTest.mysq112Psd;
	
	@Subject("multipleSequenceFactory��sequenceDao is null����ʼ���쳣")
	@Priority(PriorityLevel.NORMAL)
	@Test	
	public void TC955031(){		
		@SuppressWarnings("unused")
		ApplicationContext context;
		Step("��������");
		try{
			context = new ClassPathXmlApplicationContext(
			new String[] { "/sequence/spring-sequence-exception.xml" });
		}catch (Exception e) {
			Step("У��Ԥ�ڵ��쳣");	
			String errormessage = "nested exception is java.lang.IllegalArgumentException: The sequenceDao is null!";
			Assert.isTrue(e.getMessage().contains(errormessage), "��֤�쳣��Ϣ");
		}
		
	}
}
