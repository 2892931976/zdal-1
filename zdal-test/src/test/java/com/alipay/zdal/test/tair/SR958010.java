package com.alipay.zdal.test.tair;

import java.util.HashMap;

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
@Feature("tair��zone��,ͨ�����ػ�ȥ��ʼ��,��Ϊ��д")
public class SR958010 {
	TestAssertion Assert = new TestAssertion();

	
	  @Subject("tair��zone��д,��дidc,дldc��master,tairIndexsֻ��һ��")
	  @Priority(PriorityLevel.NORMAL)
	  @Test public void TC958011() { 
	        String va = (String)
	        TestBaseOne.getInstanceWithLocalpath(
	        "singleWriteLdcInitFromLocal01", "test", "./config/tair",
	        "12345678909876543", "555555", "jerrypanwei");
	  
	        Assert.areEqual("jerrypanwei", va, "��֤�洢��tair��ֵ"); 
	        }
	        
	  @Subject("tair��zone��д,��дidc,дldc��master,tairIndexs�ж��,����߼�ָ��ͬһ��Ⱥ����ͬ��namespace��")
	  @Priority(PriorityLevel.NORMAL)
	  @Test public void TC958012() { 
	        String va = (String)
	        TestBaseOne.getInstanceWithLocalpath(
	        "singleWriteLdcInitFromLocal02", "test", "./config/tair",
	        "12345678909876613", "test02", "test123456789-03");
	        Assert.areEqual("test123456789-03", va, "��֤�洢��tair��ֵ"); 
	        }
	 

	@Subject("tair��zone��д,ֻдidc,��дldc")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC958013() {
		String va = (String) TestBaseOne.getInstanceWithLocalpath(
				"singleWriteLdcInitFromLocal03", "test", "./config/tair",
				"12345678909876663", "test03", "test123456789-04");
		
		Assert.areEqual("test123456789-04", va, "��֤�洢��tair��ֵ");
	}
	

	
	  @Subject("tair��zone��д,��дidc,дldc��master,tairIndexs�ж��,�ڸ��߼�tair��ָ��ͬ�ļ�Ⱥ")
	  @Priority(PriorityLevel.NORMAL)
	  @Test public void TC958014(){
	  
		  String va = (String) TestBaseOne.getInstanceWithLocalpath(
					"singleWriteLdcInitFromLocal04", "test", "./config/tair",
					"12345678909876663", "test04", "test123456789-05");
			
			Assert.areEqual("test123456789-05", va, "��֤�洢��tair��ֵ");
			
	        }
	  
	  
	  @Subject("tair��zone��д,��дidc,дldc��failover,tairIndexs�ж��(ȫ��failoverΪr10w10),ÿ���߼�tairָ��ͬ�ļ�Ⱥ")
	  @Priority(PriorityLevel.NORMAL)
	  @Test public void TC958015(){
		  String va = (String) TestBaseOne.getInstanceWithLocalpath(
					"singleWriteLdcInitFromLocal05", "test", "./config/tair",
					"12345678909876683", "test05", "test123456789-06");
			
			Assert.areEqual("test123456789-06", va, "��֤�洢��tair��ֵ");
	  
	        }
	  
	  
	  @Subject("tair��zone��д,��дidc,дldc��failover,tairIndexs�ж��(��Ӧ��index��failoverΪr00w00������index��failoverΪr10w10),ÿ���߼�tairָ��ͬ�ļ�Ⱥ")
	  @Priority(PriorityLevel.NORMAL)
	  @Test public void TC958016(){
		  
		  String va = (String) TestBaseOne.getInstanceWithLocalpath(
					"singleWriteLdcInitFromLocal06", "test", "./config/tair",
					"12345678909876683", "test05", "test123456789-07");
			
			Assert.areEqual("test123456789-07", va, "��֤�洢��tair��ֵ");
	  
	        }
	 
	
	  @Subject("tair��zone��д,��дidc,дldc��failover,tairIndexs�ж��(��Ӧ��index��failoverΪr10w10������index��failoverΪr00w00),ÿ���߼�tairָ��ͬ�ļ�Ⱥ")
	  @Priority(PriorityLevel.NORMAL)
	  @Test public void TC958017(){
		  
		  String va = (String) TestBaseOne.getInstanceWithLocalpath(
					"singleWriteLdcInitFromLocal07", "test", "./config/tair",
					"12345678909876683", "test05", "test123456789-08");
			
			Assert.areEqual("test123456789-08", va, "��֤�洢��tair��ֵ");
	  
	        }
	  
	  @Subject("tair��zone��д,ֻдidc,��дldc,����idc��Ӧ������tair������")
		@Priority(PriorityLevel.NORMAL)
		@Test
		public void TC958018() {
		  try{
			TestBaseOne.getInstanceWithLocalpathException(
					"singleWriteLdcInitFromLocal08", "test", "./config/tair",
					"12345678909876663", "test08", "test123456789-09");
		  }catch(Exception e){
			 Assert.areEqual(ZdalTairShardingViolationException.class, e.getClass(), "��֤�쳣��Ϣ");
		  }
			
		}
	 
	  
	  @Subject("tair��zone��д,��дidc,дldc��master,tairIndexsֻ��һ��.д��ֵΪhashmap")
	  @Priority(PriorityLevel.NORMAL)
	  @Test public void TC958019() { 
		  HashMap<String, String> hm=new HashMap<String, String>();
		  hm.put("meimei", "gege");
		  hm.put("gongzhou","xuexi");
		  hm.put("yule","kaixin");
		  
		  HashMap<String, String> va = (HashMap<String, String>)
	        TestBaseOne.getInstanceWithLocalpath(
	        "singleWriteLdcInitFromLocal01", "test", "./config/tair",
	        "12345678909876543", "555555", hm);

	         Assert.isTrue(va.equals(hm), "��֤�Ƿ����");
        }
	  
	  @Subject("tair��zone��д,��дidc,дldc��failover,tairIndexs�ж��(��Ӧ��index��failoverΪr10w10������index��failoverΪr00w00),ÿ���߼�tairָ��ͬ�ļ�Ⱥ,д��HashMap����")
	  @Priority(PriorityLevel.NORMAL)
	  @Test public void TC95801a(){
		  
		  HashMap<String, String> hm=new HashMap<String, String>();
		  hm.put("meimei", "gege");
		  hm.put("gongzhou","xuexi");
		  hm.put("yule","kaixin");
		  
		  HashMap<String, String> va = (HashMap<String, String>) TestBaseOne.getInstanceWithLocalpath(
					"singleWriteLdcInitFromLocal07", "test", "./config/tair",
					"12345678909876683", "test05", hm);
			
			Assert.isTrue(va.equals(hm), "��֤�Ƿ����");
	  
	        }

}
