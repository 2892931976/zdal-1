package com.alipay.zdal.test.sequence;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.annotation.Tester;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import static com.alipay.ats.internal.domain.ATS.*;
import org.junit.Before;
import com.alipay.zdal.sequence.impl.MultipleSequenceFactory;
import com.alipay.zdal.test.common.ConstantsTest;
import com.mysql.jdbc.PreparedStatement;

@RunWith(ATSJUnitRunner.class)
@Feature("multipleSequenceFactory:������ȡnextValue,datasourceList����list")
public class SR955020 {
    public TestAssertion Assert = new TestAssertion();
    public final static Log Logger = LogFactory.getLog(SR955020.class);
    private MultipleSequenceFactory multipleSequenceFactory;
    private long threadNum;
    private String sequenceName="sequence_BCD1";
    private String url = ConstantsTest.mysql12UrlSequence0;
    private String user = ConstantsTest.mysq112User;
    private String psw = ConstantsTest.mysq112Psd;
	
	@Before
    public void beginTestCase() throws Exception {
		multipleSequenceFactory = (MultipleSequenceFactory) ZdalSequenceSuite.context
		.getBean("multipleSequenceFactory1");
		
	}
	
	@Subject("��ȡmultipleSequenceFactory.getNextValue����֤�Ƿ����ظ�ֵ")
	@Priority(PriorityLevel.NORMAL)
	@Test	
	public void TC955021(){		
		threadNum = 8;
		int maxDB = getValueFromDB(url,user,psw,sequenceName,"max");
		int minDB = getValueFromDB(url,user,psw,sequenceName,"min");
		int turn = (int) ((maxDB-minDB)*2/(threadNum*3));
		
		Step("���̻߳�ȡ��nextValue");
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<Map<Integer, Long>>> results = new ArrayList<Future<Map<Integer, Long>>>();	
		int count = 0;
		for (int i = 0; i < threadNum; i++) {
			results.add(exec.submit(new MulSequenceFactoryCall(multipleSequenceFactory,
					sequenceName, turn)));
		}
		
		Step("ͳ�ƻ�ȡ��nextValue�ĸ���");
		Set<Long> result = new HashSet<Long>();
		for (Future<Map<Integer, Long>> fs : results) {
			try {
				count += fs.get().size();
				for (int i = 1; i <= fs.get().size(); i++) {
					if (!result.add(fs.get().get(i))) {
						Logger.info(""+i);
						Logger.info(""+fs.get().get(i));
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} finally {
				exec.shutdown();
			}
		}		
		Logger.warn("��ͬ��sequenceres����" + result.size());
		Logger.warn("��ȡnextValue����Ϊ" + count);

		Step("��֤���");
		Assert.areEqual(count,result.size(), "��ȡsquence�Ƿ����ظ������");
	}
	
	@Subject("��ȡmultipleSequenceFactory.getNextValue����֤�ɻ�ȡ�������ֵ�Ƿ�ΪMaxValue-1")
	@Priority(PriorityLevel.NORMAL)
	@Test	
	public void TC955022(){
		threadNum = 8;
		int maxDB = getValueFromDB(url,user,psw,sequenceName,"max");
		int minDB = getValueFromDB(url,user,psw,sequenceName,"min");
		int turn = (int) ((maxDB-minDB)*3/(threadNum*2));
		
		Step("���̻߳�ȡ��nextValue");
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<Map<Integer, Long>>> results = new ArrayList<Future<Map<Integer, Long>>>();
		int count = 0;
		for (int i = 0; i < threadNum; i++) {
			results.add(exec.submit(new MulSequenceFactoryCall(multipleSequenceFactory,
					sequenceName, turn)));
		}

		
		Step("ɸѡnextValue�����ֵ");
		Set<Long> result = new TreeSet<Long>();
        for (Future<Map<Integer, Long>> fs : results) {
        	try {
        		count += fs.get().size();
        		for (int i = 1; i <= fs.get().size(); i++) {
        		result.add(fs.get().get(i)); 	        		
        		}
        	} catch (InterruptedException e) {
              e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
              exec.shutdown();
            }
           
        }
		Iterator<Long> it=result.iterator();	
		Long maxValue=Long.MIN_VALUE;
		while(it.hasNext()){
		   maxValue=it.next();
		}		
		Logger.warn("��ǰ����sequenceֵΪ��"+maxValue);

		Step("��֤���");
		Assert.areEqual(maxValue.intValue(), maxDB-1, "��ȡ�����ֵ�Ƿ����max-1");
	}
	
	@Subject("��ȡmultipleSequenceFactory.getNextValue����֤�ɻ�ȡ������Сֵ�Ƿ�ΪMinValue")
	@Priority(PriorityLevel.NORMAL)
	@Test	
	public void TC955023(){
		threadNum = 8;
		Step("��ȡ���ݿ���min_valu��max_value");
		int maxDB = getValueFromDB(url,user,psw,sequenceName,"max");
		int minDB = getValueFromDB(url,user,psw,sequenceName,"min");
		int turn = (int) ((maxDB-minDB)*3/(threadNum*2));
		
		Step("���̻߳�ȡ��nextValue");
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<Map<Integer, Long>>> results = new ArrayList<Future<Map<Integer, Long>>>();
		int count = 0;
		for (int i = 0; i < threadNum; i++) {
			results.add(exec.submit(new MulSequenceFactoryCall(multipleSequenceFactory,
					sequenceName, turn)));
		}

		Step("ɸѡnextValue����Сֵ");
		Set<Long> result = new TreeSet<Long>();
		for (Future<Map<Integer, Long>> fs : results) {
			try {
				count += fs.get().size();
				for (int i = 1; i <= fs.get().size(); i++) {
					result.add(fs.get().get(i));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				exec.shutdown();
			}
		}				
		Iterator<Long> it = result.iterator();
		Long minValue = it.next();		
		Logger.warn("sequence��Сֵ Ϊ��" + minValue);
		
		Step("��֤���");
		Assert.areEqual(minValue.intValue(), minDB, "��ȡ����Сֵ�Ƿ����min");
	}
	
	@Subject("��ȡmultipleSequenceFactory.getNextValue����֤����min��max֮��")
	@Priority(PriorityLevel.NORMAL)
	@Test	
	public void TC955024(){
		threadNum = 8;
		Step("��ȡ���ݿ���min_valu��max_value");
		int maxDB = getValueFromDB(url,user,psw,sequenceName,"max");
		int minDB = getValueFromDB(url,user,psw,sequenceName,"min");
		int turn = (int) ((maxDB-minDB)*4/(threadNum*2));
		
		Step("���̻߳�ȡ��nextValue");
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<Map<Integer, Long>>> results = new ArrayList<Future<Map<Integer, Long>>>();
		int count = 0;
		for (int i = 0; i < threadNum; i++) {
			results.add(exec.submit(new MulSequenceFactoryCall(multipleSequenceFactory,
					sequenceName, turn)));
		}

		Step("��ȡ��nextValue����HashSet");
		Set<Long> result = new HashSet<Long>();
		for (Future<Map<Integer, Long>> fs : results) {
			try {
				count += fs.get().size();
				for (int i = 1; i <= fs.get().size(); i++) {
					result.add(fs.get().get(i));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				exec.shutdown();
			}
		}
			
		Logger.warn("db�е���СֵΪ��" + minDB);
		Logger.warn("db�е����ֵΪ��" + maxDB);
/**
		Step("��֤nextValue�Ƿ��ڷ�Χ֮��");
		Iterator<Long> it = result.iterator();
		while (it.hasNext()) {
			Long seq = it.next();
			Assert.isTrue(seq >= minDB && seq <= maxDB, "nextValue�Ƿ񳬳���Χ");
		}
		
		*/
	}
	
	/**
	 * 
	 * @param url
	 * @param user
	 * @param psw
	 * @param sequenceName
	 * @param name
	 * @return
	 */
	public int getValueFromDB(String url,String user,String psw,String sequenceName,String name){
		int res = -1; 
		
		String minSql = "select min_value from multiple_sequence where name=?";
		String maxSql = "select max_value from multiple_sequence where name=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection conLog = DriverManager.getConnection(url,
					user, psw);
			
		    if(name.equalsIgnoreCase("max")){
		    	ps = (PreparedStatement) conLog.prepareStatement(maxSql);
		    }else if(name.equalsIgnoreCase("min")){
		    	ps = (PreparedStatement) conLog.prepareStatement(minSql);
		    }
			ps.setObject(1, sequenceName);
			rs = ps.executeQuery();
			rs.next();
			res = rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return res;
	}
}
