package com.alipay.zdal.test.tair;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;

@RunWith(ATSJUnitRunner.class)
@Feature("tair��zone��,��zone����")
public class SR958050 {

	@Subject("tair��zone����Ӧ��Ϊ��gzone����zone����,��zone����ģʽΪlog��")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958051() {

	}

	@Subject("tair��zone����Ӧ��Ϊ��gzone,��zone����,��zone����ģʽΪexception��")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958052() {

	}
	
	@Subject("tair��zone����Ӧ��Ϊgzone,��zone����,��zone����ģʽΪexception��")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958053() {

	}
	
	@Subject("tair��zone����Ӧ��Ϊgzone,��zone����,��zone����ģʽΪlog��")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958054() {

	}

}
