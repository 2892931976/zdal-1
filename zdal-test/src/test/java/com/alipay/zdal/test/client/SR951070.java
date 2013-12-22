package com.alipay.zdal.test.client;

import org.junit.Test;
import static com.alipay.ats.internal.domain.ATS.Step;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.client.jdbc.ZdalDataSource;
import com.alipay.zdal.datasource.scalable.ZScalableDataSource;

@RunWith(ATSJUnitRunner.class)
@Feature("Ԥ��,zoneDs��Ϊ''")
public class SR951070 {
	public TestAssertion Assert = new TestAssertion();
	
	@Subject("���� prefill,shard+rw")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC951071() throws InterruptedException{
		Step("Ԥ������");
		ZdalDataSource zd = (ZdalDataSource) ZdalClientSuite.context
		.getBean("zdalClientPrefillshardrw");
		ZScalableDataSource zz = (ZScalableDataSource) zd.getDataSourcesMap().get("ds0");
		ZScalableDataSource zz2 = (ZScalableDataSource) zd.getDataSourcesMap().get("ds1");
		ZScalableDataSource zz3 = (ZScalableDataSource) zd.getDataSourcesMap().get("ds2");
		ZScalableDataSource zz4 = (ZScalableDataSource) zd.getDataSourcesMap().get("ds3");
		boolean bl=zz.getLocalTxDataSource().getPrefill()||zz2.getLocalTxDataSource().getPrefill()||zz3.getLocalTxDataSource().getPrefill()||zz4.getLocalTxDataSource().getPrefill();
		Assert.isTrue(bl, "shard+rw is prefill");
		//��־��εõ�����Ϊ����
	    //������sleep����־
		Step("��־���");
	}
	
	@Subject("Ԥ��,shard+failover����Դ��zoneDs��Ϊ''")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC951072() throws InterruptedException{
		Step("Ԥ��,shard+failover����Դ��zoneDs��Ϊ''");
		ZdalDataSource zd = (ZdalDataSource) ZdalClientSuite.context
		.getBean("zdalClientPrefillshardfailover");
		ZScalableDataSource zz = (ZScalableDataSource) zd.getDataSourcesMap().get("ds0");
		ZScalableDataSource zz2 = (ZScalableDataSource) zd.getDataSourcesMap().get("ds1");
		boolean bl=zz.getLocalTxDataSource().getPrefill()||zz2.getLocalTxDataSource().getPrefill();		
		Step("������־");
		Assert.isTrue(bl, "shard+failover is prefill");
	}
	
	@Subject("Ԥ��,shard+oracle ����Դ��zoneDs��Ϊ''")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC951073() throws InterruptedException{
		Step("Ԥ��,shard+oracle ����Դ��zoneDs��Ϊ''");
		ZdalDataSource zd = (ZdalDataSource) ZdalClientSuite.context
		.getBean("zdalClientPrefillshardOracle");
		ZScalableDataSource zz = (ZScalableDataSource) zd.getDataSourcesMap().get("master0");
		boolean bl=zz.getLocalTxDataSource().getPrefill();
		Step("��־���");
		Assert.isTrue(bl, "shard+oracle is prefill");
	}
	
	@Subject("Ԥ��,rw����Դ��zoneDs��Ϊ''")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC951074() throws InterruptedException{
		Step("Ԥ��,rw����Դ��zoneDs��Ϊ''");
		ZdalDataSource zd = (ZdalDataSource) ZdalClientSuite.context
		.getBean("zdalClientPrefillrw");
		ZScalableDataSource zz = (ZScalableDataSource) zd.getDataSourcesMap().get("ds0");
		ZScalableDataSource zz2 = (ZScalableDataSource) zd.getDataSourcesMap().get("ds1");
		boolean bl=zz.getLocalTxDataSource().getPrefill()||zz2.getLocalTxDataSource().getPrefill();
		Step("��־���");
		Assert.isTrue(bl, "rw is prefill");
	}
		
	

}
