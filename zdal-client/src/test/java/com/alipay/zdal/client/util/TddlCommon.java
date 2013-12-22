/**
 * 
 */
package com.alipay.zdal.client.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import junit.framework.Assert;

/**
 * ��Ҫͨ��jdbc��ѯ�Ĺ��û��������
 * 
 * @author xiaoju.luo
 * 
 */
public class TddlCommon {
	private static Log logger = LogFactory.getLog("TddlCommon.class");
	
	
//	public static Connection getConnection(String url) throws SQLException,
//			java.lang.ClassNotFoundException {
//		// ��һ��������MySQL��JDBC������
//		Class.forName("com.mysql.jdbc.Driver");
//		// ȡ�����ӵ�url,�ܷ���MySQL���ݿ���û���,���룻���ݿ���
//
//		String username = "mysql";
//		String password = "mysql";
//
//		// �ڶ�����������MySQL���ݿ���������ʵ��
//		Connection con = DriverManager.getConnection(url, username, password);
//		return con;
//	}
	
	public static Connection getConnection(String url,String user,String psd) throws SQLException,
	java.lang.ClassNotFoundException {
// ��һ��������MySQL��JDBC������
Class.forName("com.mysql.jdbc.Driver");
//// ȡ�����ӵ�url,�ܷ���MySQL���ݿ���û���,���룻���ݿ���
//
//String username = "mysql";
//String password = "mysql";

// �ڶ�����������MySQL���ݿ���������ʵ��
Connection con = DriverManager.getConnection(url, user, psd);
return con;
}
	public static void dataClear(String url,String clearSql,String user, String psd){
		try {
			Connection jdbcCon;
			jdbcCon = TddlCommon.getConnection(url,user,psd);
			PreparedStatement clearState = jdbcCon.prepareStatement(clearSql);
			int clearCount = clearState.executeUpdate();
			logger.warn("����������ɹ���������Ϊ��" + clearCount);
			jdbcCon.close();
		} catch (Exception e) {
			Assert.fail("��������ʧ��");
		}
	}
	
	// ���Բ�������ݿ��Դ� Ԥ�ڵĿ����ȡ�����Զ�У��tddl�ֿ�ֱ�������ȷ��
	public static ResultSet dataCheckJDBC(String url,String querySqlJDBC,String user,String psd) {
		ResultSet result=null;
		try {
			Connection jdbcCon;
			jdbcCon = TddlCommon.getConnection(url,user,psd);
			// ���������Ĵ���
			PreparedStatement stateNormal = jdbcCon
					.prepareStatement(querySqlJDBC);
			 result = stateNormal.executeQuery(); 
			
		} catch (Exception e) {
			Assert.fail("��jdbc��ѯʧ��");
		}
		return result;
		//logger.warn(result);
		
	}
	
	// jdbc����
//	public static int dataInsertJDBC(String url,String insertSqlJDBC) {
//		
//		int rNumber=0;
//		try {
//			Connection jdbcCon;
//			jdbcCon = TddlCommon.getConnection(url);
//			// ���������Ĵ���
//			PreparedStatement stateNormal = jdbcCon
//					.prepareStatement(insertSqlJDBC);
//			rNumber = stateNormal.executeUpdate();
//			
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			Assert.fail("��jdbcд��ʧ��");
//		}
//		return rNumber;
//		
//	}
	
	// jdbc����
	public static int dataInsertJDBC(String url,String insertSqlJDBC,String user,String psd) {
		
		int rNumber=0;
		try {
			Connection jdbcCon;
			jdbcCon = TddlCommon.getConnection(url,user,psd);
			// ���������Ĵ���
			PreparedStatement stateNormal = jdbcCon
					.prepareStatement(insertSqlJDBC);
			rNumber = stateNormal.executeUpdate();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("��jdbcд��ʧ��");
		}
		return rNumber;
		
	}
}
