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
public class SR957040 {
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



	@Subject("��̬����minConnֵ,���͡�����С��������ԭ����4����2")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC957041() {
		Step("1��minΪ4�����Ӵ�����ɣ�managed�ﵽ4");
		dynamicMinConn.testsleep(30000);
		PoolCondition pc = zz.getPoolCondition();
		Assert.areEqual(4, dynamicMinConn.checkPoint(pc), "minΪ4�����Ӵ�����ɣ�managed�ﵽ4");
		Step("����minֵΪ2");
		dynamicMinConn.resetMinConnection(2,zz);
		Step("2��minֵΪ2�������������ؽ���managed��Ϊ2");
		dynamicMinConn.testsleep(60000);
		pc = zz.getPoolCondition();
		Assert.areEqual(2, dynamicMinConn.checkPoint(pc), "minֵΪ2�������������ؽ���managed��Ϊ2");
	}

	
}
