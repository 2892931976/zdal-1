package com.alipay.zdal.test.dynamicAdjustment;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static com.alipay.ats.internal.domain.ATS.Step;
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



@RunWith(ATSJUnitRunner.class)
@Feature("��̬�������ӳ���,ͬʱ����minConn��maxConnֵ")
public class SR957080 {
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
	
	
	@Subject("��̬����minConn��maxConnֵ������6���ȴ��̷߳��ʣ���minConn���ͣ�maxConn���ߡ�Ȼ��4�ȴ��̷߳���")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC957081(){
		dynamicMinConn.startNewThread2(6, tt, sqlMap);
		dynamicMinConn.testsleep(1000);
		dynamicMinConn.resetMinMaxConnection(2,10,zz);
		dynamicMinConn.testsleep(1000);
		for (int i = 0; i < 3; i++) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("num", 100+i);
			exec.submit(new TestThread01(tt, sqlMap, params,3000));
		}
		dynamicMinConn.testsleep(90000);
		PoolCondition pc=zz.getPoolCondition();
		System.out.println("========:"+pc.toString());
		Assert.areEqual(2, dynamicMinConn.checkPoint(pc), "�����ؽ����ӣ�managed��Ϊ2");
		Step("���������minConnΪ2,manageΪ2��9���߳�ȫ���������");
		Assert.areEqual(9,dynamicMinConn.selectCountZds2(),"��֤��¼����");
	}
	

}
