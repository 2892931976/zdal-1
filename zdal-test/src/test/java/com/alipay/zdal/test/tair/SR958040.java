package com.alipay.zdal.test.tair;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;

@RunWith(ATSJUnitRunner.class)
@Feature("tair��zone��,shard�������֤")
public class SR958040 {

	@Subject("tair��zone˫д,��дidc����дldc��groovy�ű�Ϊ%")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958041() {

	}

	@Subject("tair��zone˫д,��дidc����дldc��groovy�ű����ط�����")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958042() {

	}

	@Subject("tair��zone˫д,��дidc����дldc��shard����Ϊ��")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958043() {

	}

}
