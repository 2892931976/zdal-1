package com.alipay.zdal.test.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.Assert;


public class ZdalTestCommon {
    /**
     * mysql��ȡconnection
     * @param url
     * @param psd
     * @param user
     * @return
     * @throws SQLException
     * @throws java.lang.ClassNotFoundException
     */
	public static Connection getConnectionFromMysql(String url, String psd,
			String user) throws SQLException, java.lang.ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, user, psd);
		return con;
	}
    /**
     * oracle��ȡconnection
     * @param url
     * @param psd
     * @param user
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
	public static Connection getConnectionFromOracle(String url, String psd,
			String user) throws ClassNotFoundException, SQLException {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con = DriverManager.getConnection(url, user, psd);
		return con;

	}

	/**
	 * ͨ��mysql jdbc���ӻ�ȡ����
	 * @param querySqlJDBC
	 * @param url
	 * @param psd
	 * @param user
	 * @return
	 */
	public static ResultSet dataCheckFromJDBC(String querySqlJDBC, String url,
			String psd, String user) {
		ResultSet result = null;
		try {
			Connection jdbcCon;
			jdbcCon = getConnectionFromMysql(url, psd, user);

			PreparedStatement stateNormal = jdbcCon
					.prepareStatement(querySqlJDBC);
			result = stateNormal.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
			// Assert.fail("��jdbc��ѯʧ��");
		}
		return result;

	}

	/**
	 * ͨ��mysql jdbc�����޸�ɾ������
	 * @param updateSqlJDBC
	 * @param url
	 * @param psd
	 * @param user
	 * @return
	 */
	public static int dataUpdateJDBC(String updateSqlJDBC, String url,
			String psd, String user) {

		int rNumber = 0;
		try {
			Connection jdbcCon;
			jdbcCon = getConnectionFromMysql(url, psd, user);
			// ���������Ĵ���
			PreparedStatement stateNormal = jdbcCon
					.prepareStatement(updateSqlJDBC);
			rNumber = stateNormal.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			// Assert.fail("��jdbc����ʧ��");
		}
		return rNumber;

	}

	/**
	 * ͨ��oracle jdbc���ӻ�ȡ����
	 * @param querySqlJDBC
	 * @param url
	 * @param psd
	 * @param user
	 * @return
	 */
	public static ResultSet dataCheckFromJDBCOracle(String querySqlJDBC,
			String url, String psd, String user) {
		ResultSet result = null;
		try {
			Connection jdbcCon;
			jdbcCon = getConnectionFromOracle(url, psd, user);
			Statement stmt = jdbcCon.createStatement();
			result = stmt.executeQuery(querySqlJDBC);

		} catch (Exception e) {
			e.printStackTrace();
			// Assert.fail("��jdbc��ѯʧ��");
		}
		return result;

	}

	
	/**
	 * ͨ��oracle jdbc�����޸�ɾ������
	 * @param updateSqlJDBC
	 * @param url
	 * @param psd
	 * @param user
	 * @return
	 */
	public static int dataUpdateJDBCOracle(String updateSqlJDBC, String url,
			String psd, String user) {

		int rNumber = 0;
		try {
			Connection jdbcCon;
			jdbcCon = getConnectionFromOracle(url, psd, user);
			// ���������Ĵ���
			PreparedStatement stateNormal = jdbcCon
					.prepareStatement(updateSqlJDBC);
			rNumber = stateNormal.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			// Assert.fail("��jdbc����ʧ��");
		}
		return rNumber;

	}


	/**
	 * jdbc��ѯoracle ��dual��
	 * @param url
	 * @param queryNextSql
	 * @param queryCurrSql
	 * @param psd
	 * @param user
	 * @return
	 */
	public static ResultSet dualCheckJDBC(String url, String queryNextSql,
			String queryCurrSql, String psd, String user) {
		ResultSet result = null;
		try {
			Connection jdbcCon;
			jdbcCon = getConnectionFromOracle(url, psd, user);
			// ִ��sequence.nextval��ѯ
			PreparedStatement stateNormal = jdbcCon
					.prepareStatement(queryNextSql);
			stateNormal.executeQuery();
			// ִ��sequence.currval��ѯ
			PreparedStatement stateNormal0 = jdbcCon
					.prepareStatement(queryCurrSql);
			result = stateNormal0.executeQuery();

		} catch (Exception e) {
			Assert.fail("��jdbc��ѯʧ��");
		}
		return result;
		// logger.warn(result);

	}

	/**
	 * Ϊmysql ��zds������׼������
	 */
	public static void dataPrepareForZds() {
		String url1 = ConstantsTest.mysql12UrlZds1;
		String url2 = ConstantsTest.mysql12UrlZds2;
		String psd = ConstantsTest.mysq112Psd;
		String user = ConstantsTest.mysq112User;
		String insertSql1 = "insert into test1(clum,colu2) values (100,'DB_A')";
		String insertSql2 = "insert into test1(clum,colu2) values (100,'DB_B')";
		dataUpdateJDBC(insertSql1, url1, psd, user);
		dataUpdateJDBC(insertSql2, url2, psd, user);
	}
	
	/**
	 * Ϊmysql��zds������ɾ������
	 */
	public static void dataDeleteForZds(){
		String url1 = ConstantsTest.mysql12UrlZds1;
		String url2 = ConstantsTest.mysql12UrlZds2;
		String psd = ConstantsTest.mysq112Psd;
		String user = ConstantsTest.mysq112User;
		String delSql1="delete from test1";
		dataUpdateJDBC(delSql1, url1, psd, user);
		dataUpdateJDBC(delSql1, url2, psd, user);
	}
	
	/**
	 * Ϊmysql��fail_0׼������
	 */
	public static void dataPrepareForFail0(){
		String insertSqlJDBC = "insert into master_0 (user_id,age,name,content) values (20,10,'a','s')";
		String url=ConstantsTest.mysq112UrlFail0;
		String user=ConstantsTest.mysq112User;
		String psd=ConstantsTest.mysq112Psd ;
		dataUpdateJDBC(insertSqlJDBC, url, psd, user);
	}
	
	/**
	 * Ϊmysql��Tddl0,tddl_1,tddl_2׼������
	 */
	public static void dataPrepareForTddl(){
		String insertSqlJDBC0 ="insert into users(name,address) values ('DB','DB_A')";
		String insertSqlJDBC1 ="insert into users(name,address) values ('DB','DB_B')";
		String insertSqlJDBC2 ="insert into users(name,address) values ('DB','DB_C')";
		String url0=ConstantsTest.mysq112UrlTddl0;
		String url1=ConstantsTest.mysq112UrlTddl1;
		String url2=ConstantsTest.mysq112UrlTddl2;
		String user=ConstantsTest.mysq112User;
		String psd=ConstantsTest.mysq112Psd;
		dataUpdateJDBC(insertSqlJDBC0, url0, psd, user);
		dataUpdateJDBC(insertSqlJDBC1, url1, psd, user);
		dataUpdateJDBC(insertSqlJDBC2, url2, psd, user);		
	}
	
	/**
	 * Ϊmysql��tddl_0,tddl_1,tddl_2ɾ������
	 */
	public static void dataDeleteForTddl(){
		String delSql="delete from users";
		String url0=ConstantsTest.mysq112UrlTddl0;
		String url1=ConstantsTest.mysq112UrlTddl1;
		String url2=ConstantsTest.mysq112UrlTddl2;
		String user=ConstantsTest.mysq112User;
		String psd=ConstantsTest.mysq112Psd;
		dataUpdateJDBC(delSql, url0, psd, user);
		dataUpdateJDBC(delSql, url1, psd, user);
		dataUpdateJDBC(delSql, url2, psd, user);	
		
	}

}
