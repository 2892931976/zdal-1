package com.alipay.zdal.test.tair;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;


@RunWith(ATSJUnitRunner.class)
@Feature("tair��zone��,tairIndexs����֤")
public class SR958030 {
	
	@Subject("tair��zone˫д,��дidc���Ǳ�дldc��shard����󳬳�tairIndexs[index��0��ʼ]")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958031(){
		
	}
	
	@Subject("tair��zone˫д,��дidc���Ǳ�дldc��shard������indexΪ0")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958032(){
		
	}
	
	@Subject("tair��zone˫д,��дidc���Ǳ�дldc��tairIndexs�е�ֵ����tairMappingMap�е�keyֵ")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958033(){
		
	}
	
	@Subject("tair��zone˫д,��дidc���Ǳ�дldc��tairIndexs�е�ֵ����tairMappingMap�е�keyֵ����shard֮���indexΪ�������tairIndexs")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958034(){
		
	}
	
	@Subject("tair��zone˫д,��дidc���Ǳ�дldc��tairIndexs�е�ֵΪ��")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958035(){
		
	}

}
