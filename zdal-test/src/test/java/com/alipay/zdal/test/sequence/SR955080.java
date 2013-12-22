package com.alipay.zdal.test.sequence;

import static com.alipay.ats.internal.domain.ATS.Step;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
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
import com.alipay.zdal.sequence.impl.MultipleSequence;
import com.mysql.jdbc.PreparedStatement;

@RunWith(ATSJUnitRunner.class)
@Feature("sequence:���ݿ�����sequence��¼�����Զ�����")
public class SR955080 {
    public TestAssertion Assert = new TestAssertion();
    public final static Log Logger = LogFactory.getLog(SR955080.class);
    private MultipleSequence multipleSequence;
    private long threadNum;
    private String sequenceName="sequence_Hello";
    private String url0 = "jdbc:mysql://mysql-1-2.bjl.alipay.net:3306/tddl_sequence_0";
    private String user0 = "mysql";
    private String psw0 = "mysql";
    private String url1 = "jdbc:mysql://mysql-1-2.bjl.alipay.net:3306/tddl_sequence_1";
    private String url2 = "jdbc:mysql://mysql-2-2.bjl.alipay.net:3306/tddl_sequence_2";
    private String user2 = "mysql";
    private String psw2 = "mysqlbak";
    
	@Before
    public void beginTestCase() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "/sequence/spring-sequence-ds.xml" });
		multipleSequence = (MultipleSequence) context.getBean("multipleSequence4");
		
	}
	
	@Subject("��ȡsequence.nextValue����֤�Ƿ����ظ�ֵ")
	@Priority(PriorityLevel.NORMAL)
	@Tester("riqiu")
	@Test	
	public void TC955081(){
		threadNum = 8;
		long maxValue=multipleSequence.getMaxValue();
		long minValue=multipleSequence.getMinValue();
		int step=multipleSequence.getInnerStep();
		long maxValue0,maxValue1,maxValue2,minValue0,minValue1,minValue2;
		int step0,step1,step2;
		
		Step("��ȡ������Դ��minValue��maxValue��step");
		maxValue0 = getValueFromDB(url0, user0, psw0, sequenceName, "max");
		minValue0 = getValueFromDB(url0, user0, psw0, sequenceName, "min");
		step0 = getValueFromDB(url0, user0, psw0, sequenceName, "step");

		maxValue1 = getValueFromDB(url1, user0, psw0, sequenceName, "max");
		minValue1 = getValueFromDB(url1, user0, psw0, sequenceName, "min");
		step1 = getValueFromDB(url1, user0, psw0, sequenceName, "step");
		
		maxValue2 = getValueFromDB(url2, user2, psw2, sequenceName, "max");
		minValue2 = getValueFromDB(url2, user2, psw2, sequenceName, "min");
		step2 = getValueFromDB(url2, user2, psw2, sequenceName, "step");

		Step("��֤������Դ��minValue��maxValue��step");
		Assert.areEqual(maxValue, maxValue0, "��֤���ֵ");
		Assert.areEqual(minValue, minValue0, "��֤��Сֵ");
		Assert.areEqual(step, step0, "��֤����");
		
		Assert.areEqual(maxValue, maxValue1, "��֤���ֵ");
		Assert.areEqual(minValue, minValue1, "��֤��Сֵ");
		Assert.areEqual(step, step1, "��֤����");
		
		Assert.areEqual(maxValue, maxValue2, "��֤���ֵ");
		Assert.areEqual(minValue, minValue2, "��֤��Сֵ");
		Assert.areEqual(step, step2, "��֤����");
		
		Step("��ȡ��nextValue");
		long value = multipleSequence.nextValue();
		Assert.isTrue(value<maxValue && value>minValue, "��֤nextValue");
		
		Step("��������");
		deleteFromDB(url0,user0,psw0,sequenceName);
		deleteFromDB(url1,user0,psw0,sequenceName);
		deleteFromDB(url2,user2,psw2,sequenceName);
	}
	
	
	public int getValueFromDB(String url,String user,String psw,String sequenceName,String name){
		int res = -1; 
		
		String minSql = "select min_value from multiple_sequence where name=?";
		String maxSql = "select max_value from multiple_sequence where name=?";
		String stepSql = "select step from multiple_sequence where name=?";
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
		    }else if(name.equalsIgnoreCase("step")){
		    	ps = (PreparedStatement) conLog.prepareStatement(stepSql);
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
	
	public int deleteFromDB(String url,String user,String psw,String sequenceName){
		int res = 0; 
		
		String sql = "delete from multiple_sequence where name=?";
		PreparedStatement ps = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection conLog = DriverManager.getConnection(url,
					user, psw);			
		    ps = (PreparedStatement) conLog.prepareStatement(sql);
			ps.setObject(1, sequenceName);
			res = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
		return res;
	}
}
