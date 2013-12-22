package com.alipay.zdal.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;

/**
 *���ڽ�String��װΪClob�����Ա�ϵͳʶ�𡣷����޷�ֱ�ӽ�clobָ��Ϊϵͳ����
 * @author shenxun
 *
 */
public class StringClobWrapper implements java.sql.Clob{
	String str=null;
	public StringClobWrapper(String str) {
		this.str=str;
	}
	public void free() throws SQLException {
		str=null;
	}

	public InputStream getAsciiStream() throws SQLException {
		throw new SQLException("��֧�ֵĲ���");
	}

	public Reader getCharacterStream() throws SQLException {
		if(str!=null){
			return new StringReader(str);
		}
		else{
			return null;
		}
	}

	public Reader getCharacterStream(long pos, long length) throws SQLException {
		throw new SQLException("��֧�ֵĲ���");
	}

	public String getSubString(long pos, int length) throws SQLException {
		throw new SQLException("��֧�ֵĲ���");
	}

	/* (non-Javadoc)
	 * @see java.sql.Clob#length()
	 */
	public long length() throws SQLException {
		if(str!=null){
			return str.length();
		}
		else{
			return 0;
		}
	}

	public long position(String searchstr, long start) throws SQLException {
		throw new SQLException("��֧�ֵĲ���");
	}

	public long position(Clob searchstr, long start) throws SQLException {
		throw new SQLException("��֧�ֵĲ���");
	}

	public OutputStream setAsciiStream(long pos) throws SQLException {
		throw new SQLException("��֧�ֵĲ���");
	}

	public Writer setCharacterStream(long pos) throws SQLException {
		throw new SQLException("��֧�ֵĲ���");
	}

	public int setString(long pos, String str) throws SQLException {
		throw new SQLException("��֧�ֵĲ���");
	}

	public int setString(long pos, String str, int offset, int len)
			throws SQLException {
		throw new SQLException("��֧�ֵĲ���");
	}

	public void truncate(long len) throws SQLException {
		throw new SQLException("��֧�ֵĲ���");
	}

}
