package com.alipay.zdal.test.dynamicAdjustment;
import java.util.HashMap;
import java.util.Map;
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
import com.alipay.zdal.datasource.scalable.impl.ScaleConnectionPoolException;
import com.ibatis.sqlmap.client.SqlMapClient;
import static com.alipay.ats.internal.domain.ATS.Step;
@RunWith(ATSJUnitRunner.class)
@Feature("��̬�������ӳ���,����maxConnֵ")
public class SR957010 {
	ExecutorService exec = Executors.newCachedThreadPool();
	TestAssertion Assert = new TestAssertion();
	DynamicAdjustmentCommon dynamicCommon=new DynamicAdjustmentCommon();
	ZScalableDataSource zz = null;
	ZdalDataSource zs = null;
	SqlMapClient sqlMap = null;
	TransactionTemplate tt = null;
	

	@Before
	public void beforeTestCase() {
		Step("��ʼ������Դ");
		String[] springXmlPath = { "./dynamicAdjustment/spring-dynamicAdjustment-ds.xml" };
		ApplicationContext context = new ClassPathXmlApplicationContext(
				springXmlPath);

		zs = (ZdalDataSource) context.getBean("zdalRwDSMysql10");
		sqlMap = (SqlMapClient) context.getBean("zdalRwdynamicAdjustment");
		tt = (TransactionTemplate) context
				.getBean("zdalRwdynamicAdjustmentTmp");
		zz = (ZScalableDataSource) zs.getDataSourcesMap().get("ds0");
		
	}

	@After
	public void afterTestCase() {
		Step("����Ϊ�˷�ֹ���̹߳ص��������̱߳��ص�");
		try {
			Thread.sleep(6000);
			resetMaxConnection(5);
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Step("�������");
		dynamicCommon.deleteDateZds1();
			
	}

	@Subject("������maxConnֵ��������5���̣߳���5���߳�ռ�����ӣ�����3���߳�")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC957011() {
		
		startNewThread1(5);
		
		dynamicCommon.testsleep(1000);
		
		startNewThread2(3);
		Step("���������ǰ5���̲߳������ݣ���3���̱߳��쳣");
		dynamicCommon.testsleep(5000);
		Assert.areEqual(5, dynamicCommon.selectCountZds1(), "ǰ5���̲߳������ݣ���3���̱߳��쳣");
	}

	
	@Subject("��̬����maxConnֵ������5���߳��õ����ӣ���5���߳�ռ�����ӡ�����3���̡߳��ٵ���maxConnֵΪ10")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC957012() {
		
		startNewThread1(5);
		
		dynamicCommon.testsleep(1000);
		
		startNewThread2(3);
		
		dynamicCommon.testsleep(1000);		
		resetMaxConnection(10);
		Step("���������5���߳����������������3���̱߳��쳣");
		dynamicCommon.testsleep(5000);
		Assert.areEqual(5, dynamicCommon.selectCountZds1(), "5���߳����������������3���̱߳��쳣");
	}
	
	
	@Subject("��̬����maxConnֵ������5���߳��õ����ӣ���5���߳�ռ�����ӡ��ֵ���maxConnֵΪ10������3���߳�")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC957013() {
		
		startNewThread1(5);
		
		dynamicCommon.testsleep(1000);

		resetMaxConnection(10);

		startNewThread2(3);
		Step("���������8���̶߳��������");
		dynamicCommon.testsleep(5000);	
		Assert.areEqual(8, dynamicCommon.selectCountZds1(), "8���̶߳��������");
	}
	
	
	@Subject("��̬����maxConnֵ������8���߳��õ����ӣ�ռ�����ӣ��ֵ���maxConnֵΪ10��")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC957014() {
		
		startNewThread1(8);	
        
		resetMaxConnection(10);

		Step("�������������5���̶߳��������");
		dynamicCommon.testsleep(5000);
		Assert.isTrue(dynamicCommon.selectCountZds1()>=5, "����5���̶߳��������");
	}
	
	@Subject("��̬����maxConnֵ������5���߳��õ����ӣ���5���߳�ռ�����ӡ��ֵ���maxConnֵΪ10������6���ǵȴ��߳�")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC957015() {
		
		startNewThread1(5);

		resetMaxConnection(10);

		startNewThread2(6);
		Step("���������ԭ5���߳��������������6���߳��е�5�������õ����ӣ������������1���̵߳ȸ�5���߳��ͷ����ӡ�");
		Step("����������ɵ�������Ϊ10��");
		dynamicCommon.testsleep(5000);
		Assert.isTrue(dynamicCommon.selectCountZds1()>=10, "����10���̶߳��������");
	}
	
	
	@Subject("��̬����maxConnֵ������5���߳��õ����ӣ���5���߳�ռ�����ӡ��ֵ���maxConnֵΪ10������6���ȴ��߳�")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC957016() {
		
		startNewThread1(5);

		resetMaxConnection(10);

		for (int i = 0; i < 6; i++) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("num", 100+i);
			exec.submit(new TestThread01(tt, sqlMap, params,3000));
		}
		Step("���������ԭ5���߳��������������6���߳��е�5���õ����������������1�����쳣��");
		dynamicCommon.testsleep(5000);
		Assert.areEqual(10, dynamicCommon.selectCountZds1(), "10���̶߳��������");
	}
	
	
	@Subject("��̬����maxConnֵ������5���߳��õ����ӣ���5���߳�ռ�����ӡ�����3���ǵȴ��̡߳�����maxConnֵΪ6")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC957017() {
		
		startNewThread1(5);
		
		dynamicCommon.testsleep(1000);
		
		startNewThread2(3);
		
		dynamicCommon.testsleep(1000);
		resetMaxConnection(6);
		
		Step("���������5���߳����������������3���̱߳��쳣����Ϊ�߳�������֮ǰ���������õ�֮ǰ���߳����ӣ�");
		dynamicCommon.testsleep(5000);
		Assert.areEqual(5, dynamicCommon.selectCountZds1(), "5���̶߳��������,��������3���̱߳��쳣");
	}
		
	
	@Subject("��̬����maxConnֵ������5���߳��õ����ӣ���5���߳�ռ�����ӡ�����3���ȴ��̡߳�����maxConnֵΪ6")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC957018() {
		
		startNewThread1(5);
		
		dynamicCommon.testsleep(1000);
		
		for (int i = 0; i < 3; i++) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("num", 100+i);
			exec.submit(new TestThread01(tt, sqlMap, params,3000));
		}
		
		dynamicCommon.testsleep(1000);
		resetMaxConnection(6);
		
		Step("���������5���߳��������,��������3�����쳣");
		dynamicCommon.testsleep(5000);
		Assert.areEqual(5, dynamicCommon.selectCountZds1(), "5���߳��������,��������3�����쳣");
	}
		
	@Subject("��̬����maxConnֵ������3���߳��õ����ӣ���3���߳�ռ�����ӡ�����5���ǵȴ��̡߳�����maxConnֵΪ10")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC957019(){
		
		startNewThread1(3);
		
		dynamicCommon.testsleep(1000);
		
		startNewThread2(5);
		
		dynamicCommon.testsleep(1000);
		resetMaxConnection(10);
		
		//���������3���߳��������������5���߳����е�2�������õ����ӣ�����3���߳�Ҫ�ȸ�2���߳��ͷ����ӡ�
		//���ԣ�����Ҫ5���߳��������
		dynamicCommon.testsleep(5000);
		Assert.isTrue(dynamicCommon.selectCountZds1()>=5, "����5���̶߳��������");
	}
	
	
	
	@Subject("��̬����maxConnֵ������3���߳��õ����ӣ���3���߳�ռ�����ӡ�����5���ȴ��̡߳�����maxConnֵΪ10")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC95701a(){
		
		startNewThread1(3);
		
		dynamicCommon.testsleep(1000);
		
		for (int i = 0; i < 5; i++) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("num", 100+i);
			exec.submit(new TestThread01(tt, sqlMap, params,3000));
		}
		
		dynamicCommon.testsleep(1000);
		resetMaxConnection(10);
		
		//���������3���߳��������������5���߳����е�2�������õ����ӣ����е�����3�����쳣
		dynamicCommon.testsleep(5000);
		Assert.areEqual(5, dynamicCommon.selectCountZds1(), "3���߳��������������5���߳����е�2�������õ����ӣ����е�����3�����쳣");
		
	}
	

			
	@Subject("��̬����maxConnֵ������С��ԭmaxConnΪ5������4���߳�����")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC95701b(){
		
		startNewThread1(4);
		
		Step("���������4���߳��õ����ӷ���db");
		dynamicCommon.testsleep(5000);
		Assert.areEqual(4, dynamicCommon.selectCountZds1(), "4���߳��õ����ӷ���db");
	}
	
	@Subject("��̬����maxConnֵ����С����maxConn��5����Ϊ3,Ȼ��4���߳�����")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC95701c(){
		resetMaxConnection(3);
		
		startNewThread1(4);
		
		Step("�������������3���߳��õ����ӷ���db������1���쳣");
		dynamicCommon.testsleep(5000);
		Assert.areEqual(3, dynamicCommon.selectCountZds1(), "����3���߳��õ����ӷ���db������1���쳣");
	}
		
	
	@Subject("��̬����maxConnֵ����С������4���߳������õ����ӡ��ֽ�maxConn��5����Ϊ3")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC95701e(){
		startNewThread1(4);
		
		dynamicCommon.testsleep(1000);
		resetMaxConnection(3);
				
		Step("���������ԭ4���̣߳����õ����ӣ��������");
		dynamicCommon.testsleep(5000);
		Assert.areEqual(4, dynamicCommon.selectCountZds1(), "ԭ4���̣߳����õ����ӣ��������");
	}
	
	
	@Subject("��̬����maxConnֵ����С������4���߳������õ����ӡ��ֽ�maxConn��5����Ϊ3��������5���ȴ��߳�")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC95701f(){
		startNewThread1(4);
		
		dynamicCommon.testsleep(3000);
		resetMaxConnection(3);
		dynamicCommon.testsleep(1000);
		
		for (int i = 0; i < 5; i++) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("num", 100+i);
			exec.submit(new TestThread01(tt, sqlMap, params,3000));
		}
				
		Step("���������ԭ4���̣߳����õ����ӣ��������.��������5���̣߳�����3���õ����������������2�����쳣��");
		dynamicCommon.testsleep(5000);
		Assert.areEqual(7, dynamicCommon.selectCountZds1(), "ԭ4���̣߳����õ����ӣ��������.��������5���̣߳�����3���õ����������������2�����쳣��");
	
	}

	
	/**
	 * �����߳�1
	 * @param z �̸߳���
	 */
	private void startNewThread1(int z){
		// ����5���̣߳���ռ������db����
		for (int i = 0; i < z; i++) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("num", i);
			exec.submit(new TestThread01(tt, sqlMap, params,3000));
		}
	}
	
	/**
	 * �����߳�2
	 * @param z
	 */
	private void startNewThread2(int z){
		// ������3���߳�,�����ȴ���ռ�õ�db����
		for (int j = 0; j < z; j++) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("num", 10 + j);
			exec.submit(new TestThread01(tt, sqlMap, params,0));
		}
	}
	
	/**
	 * ������maxConn������
	 * @param z
	 */
	private void resetMaxConnection(int z){
		try {
			// �������������('0'��ʾ��������
			zz.resetConnectionPoolSize(0,z);
		} catch (ScaleConnectionPoolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
