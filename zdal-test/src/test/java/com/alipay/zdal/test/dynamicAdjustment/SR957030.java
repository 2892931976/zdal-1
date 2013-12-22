package com.alipay.zdal.test.dynamicAdjustment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.support.TransactionTemplate;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.client.jdbc.ZdalDataSource;
import com.alipay.zdal.datasource.scalable.ZScalableDataSource;
import com.alipay.zdal.datasource.util.PoolCondition;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.alipay.zdal.test.dynamicAdjustment.DynamicAdjustmentCommon;
import static com.alipay.ats.internal.domain.ATS.Step;
@RunWith(ATSJUnitRunner.class)
@Feature("��̬�������ӳ���,����minConnֵ")
public class SR957030 {
	TestAssertion Assert = new TestAssertion();
	DynamicAdjustmentCommon dynamicMinConn=new DynamicAdjustmentCommon();
	ExecutorService exec = Executors.newCachedThreadPool();
	ZScalableDataSource zz = null;
	ZdalDataSource zs =null;
	SqlMapClient sqlMap =null; 
	TransactionTemplate tt =null; 

	@Before
	public void beforeTestCase() {
		String[] springXmlPath = { 
		"./dynamicAdjustment/spring-dynamicAdjustment-ds.xml" };
		ApplicationContext context = new ClassPathXmlApplicationContext(springXmlPath);
		try {
			zs=(ZdalDataSource)context.getBean("zdalRwDSMysql11");
			sqlMap=(SqlMapClient) context.getBean("zdalRwdynamicAdjustmentMin");
			tt = (TransactionTemplate) context.getBean("zdalRwdynamicAdjustmentTmpMin");
			zz = (ZScalableDataSource) zs.getDataSourcesMap().get("ds0");
			zz.getLocalTxDataSource().getDatasource().getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@After
	public void afterTestcase() {
		dynamicMinConn.deleteDateZds2();
	}


	@Subject("��̬����minConnֵ,���ߡ�����С��������ԭ����4������5")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC957031() {
		Step("1��minΪ4����ʼ������������managed��δ�ﵽ4");
		PoolCondition pc = zz.getPoolCondition();
		Assert.isTrue(dynamicMinConn.checkPoint(pc) < 4, "minΪ4����ʼ������������managed��δ�ﵽ4,"+dynamicMinConn.checkPoint(pc));
		Step("2��min��Ϊ5,����������������managed ��δ�ﵽ5");
		dynamicMinConn.resetMinConnection(5,zz);
		pc = zz.getPoolCondition();
		Assert.isTrue(dynamicMinConn.checkPoint(pc) < 5, "min��Ϊ5,����������������managed��δ�ﵽ5,"+dynamicMinConn.checkPoint(pc));
		Step("3��minΪ5,������������ɣ�managedΪ5");
		dynamicMinConn.testsleep(30000);
		pc = zz.getPoolCondition();
		Assert.areEqual(5, dynamicMinConn.checkPoint(pc), "minΪ5,������������ɣ�managedΪ5");
		Step("4��minֵΪ6,�����������ؽ�,managed��Ϊ6");
		dynamicMinConn.resetMinConnection(6,zz);
		dynamicMinConn.testsleep(60000);
		pc = zz.getPoolCondition();
		Assert.areEqual(6, dynamicMinConn.checkPoint(pc), "minֵΪ6,�����������ؽ�,managed��Ϊ6");
	}

	
}
