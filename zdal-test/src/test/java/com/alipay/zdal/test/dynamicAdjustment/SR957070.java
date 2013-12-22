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
import static com.alipay.ats.internal.domain.ATS.Step;
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
import static com.alipay.ats.internal.domain.ATS.Step;
import static com.alipay.ats.internal.domain.ATS.Step;
import static com.alipay.ats.internal.domain.ATS.Step;
@RunWith(ATSJUnitRunner.class)
@Feature("��̬�������ӳ���,ͬʱ����minConn��maxConnֵ")
public class SR957070 {
	TestAssertion Assert = new TestAssertion();
	DynamicAdjustmentCommon dynamicMinConn=new DynamicAdjustmentCommon();
	ExecutorService exec = Executors.newCachedThreadPool();
	ZScalableDataSource zz = null;
	ZdalDataSource zs = null;
	SqlMapClient sqlMap = null;
	TransactionTemplate tt = null;
	
	@Before
	public void beforeTestCase() {
		// ��ʼ������Դ
		String[] springXmlPath = { "./dynamicAdjustment/spring-dynamicAdjustment-ds.xml" };
		ApplicationContext context = new ClassPathXmlApplicationContext(
				springXmlPath);			
		try {
			zs = (ZdalDataSource) context.getBean("zdalRwDSMysql11");
			sqlMap = (SqlMapClient) context.getBean("zdalRwdynamicAdjustmentMin");
			tt = (TransactionTemplate) context
			.getBean("zdalRwdynamicAdjustmentTmpMin");
			zz = (ZScalableDataSource) zs.getDataSourcesMap().get("ds0");
			zz.getLocalTxDataSource().getDatasource().getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@After
	public void afterTestCase(){
		dynamicMinConn.deleteDateZds2();
	}
	
	
	@Subject("��̬����minConn��maxConnֵ����minConn���ͣ�maxConn���ߡ�Ȼ��9�ȴ��̷߳���")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC957071(){
		Step("1��������minConnΪ4,maxConnΪ8");
		PoolCondition pc = zz.getPoolCondition();
		System.out.println("========1:"+pc.toString());
		Step("����minConn��maxConn");
		dynamicMinConn.resetMinMaxConnection(2,10,zz);
		dynamicMinConn.testsleep(1000);
		Step("2������9���ȴ��̷߳���");
		dynamicMinConn.startNewThread2(9, tt, sqlMap);
		dynamicMinConn.testsleep(30000);
		pc=zz.getPoolCondition();
		System.out.println("========2:"+pc.toString());
		Step("3�����ӹ�ʱ�����ؽ���managedΪ2");
		dynamicMinConn.testsleep(60000);
		pc=zz.getPoolCondition();
		System.out.println("========3:"+pc.toString());
		Assert.areEqual(2, dynamicMinConn.checkPoint(pc), "�����ؽ����ӣ�managed��Ϊ2");
		Step("���������minConnΪ2,manageΪ2��9���̶߳��������");	
		Assert.areEqual(9,dynamicMinConn.selectCountZds2(),"��֤��¼����");
	}
	

}
