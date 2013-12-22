package com.alipay.zdal.test.sequence;

import static org.junit.Assert.fail;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.alibaba.common.lang.StringUtil;
import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import static com.alipay.ats.internal.domain.ATS.*;
import org.junit.Before;

import com.alipay.zdal.sequence.exceptions.SequenceException;
import com.alipay.zdal.sequence.impl.MultipleSequenceFactory;
import com.alipay.zdal.test.common.ConstantsTest;
import com.mysql.jdbc.PreparedStatement;

@RunWith(ATSJUnitRunner.class)
@Feature("multipleSequenceFactory:��̬���sequence")

public class SR955010 {
    public TestAssertion Assert = new TestAssertion();
    public final static Log Logger = LogFactory.getLog(SR955010.class);
    private MultipleSequenceFactory multipleSequenceFactory;
    private String url = ConstantsTest.mysql22UrlSequence3;
    private String user = ConstantsTest.mysq122User;
    private String psw = ConstantsTest.mysq122Psd;
	private String sequenceName = "SR955060";
	
	@Before
    public void beginTestCase() throws Exception {	
		multipleSequenceFactory = (MultipleSequenceFactory) ZdalSequenceSuite.context
		.getBean("multipleSequenceFactory2");
	}

	@Subject("multipleSequenceFactory����̬���sequence����ȡnextvalue")
	@Priority(PriorityLevel.NORMAL)
	@Test	
	public void TC955011(){	
		int isClear = deleteSequenceFromDB(url,user,psw,sequenceName);
		Assert.isTrue(isClear>=0, "��֤�����sequence");
		
		Step("��̬���sequence");
		int isAdd = putSequenceIntoDB(url,user,psw,sequenceName);
		Assert.areEqual(1, isAdd, "��֤�����sequence");
		
		Step("��ȡnextvalue");
		long value = 0l;
		try {
			value = multipleSequenceFactory.getNextValue(sequenceName);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}		
		Assert.isTrue(value>0, "��֤��ȡ��nextvalue");
		
		deleteSequenceFromDB(url,user,psw,sequenceName);
	}
	
	@Subject("multipleSequenceFactory��δ��̬���sequence����ȡnextvalue")
	@Priority(PriorityLevel.NORMAL)
	@Test	
	public void TC955012(){	
		Step("δ��̬���sequence����ȡnextvalue");
		long value = 0l;
		try {
			value = multipleSequenceFactory.getNextValue(sequenceName);
		}catch (SequenceException e1) {
			Step("��֤�쳣��Ϣ");
			Assert.isTrue(StringUtil.isNotBlank(e1.getMessage()) && e1.getMessage().contains("can not find the record"), "��֤�쳣��Ϣ");
		} catch (Exception e) {
			Assert.isTrue(false, "��Ԥ���쳣"+e);
		}		
	}
	
	
    /*
     * ��db����sequence��¼����
     */
	public int putSequenceIntoDB(String url,String user,String psw,String sequenceName){
		int res = -1; 
		String sql = "insert into multiple_sequence value (?,50,'2013-06-18 15:39:02',50,50,1000,'2013-06-18 15:39:02')";
		PreparedStatement ps = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection conLog = DriverManager.getConnection(url,
					user, psw);
		    ps = (PreparedStatement) conLog.prepareStatement(sql);
			ps.setString(1, sequenceName);
			res = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
				ps = null;
			}
		}		
		return res;
	}
	

    /*
     * dbɾ��sequence��¼����
     */
	public int deleteSequenceFromDB(String url,String user,String psw,String sequenceName){
		int res = -1; 
		String sql = "delete from multiple_sequence where name = ?";
		PreparedStatement ps = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection conLog = DriverManager.getConnection(url,
					user, psw);
		    ps = (PreparedStatement) conLog.prepareStatement(sql);
			ps.setString(1, sequenceName);
			res = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
				ps = null;
			}
		}		
		return res;
	}
}
