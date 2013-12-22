package com.alipay.zdal.test.rw;

import static com.alipay.ats.internal.domain.ATS.Step;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.client.ThreadLocalString;
import com.alipay.zdal.client.util.ThreadLocalMap;
import com.alipay.zdal.test.common.ZdalTestCommon;
import com.ibatis.sqlmap.client.SqlMapClient;

@RunWith(ATSJUnitRunner.class)
@Feature("ճ��ģ�ͣ�ThreadLocalMapָ����sql·�ɵ���ͬdb.ճ��ģ��,ֻ��ճ��ȥ����û��ճ��д����")
public class SR952180 {
	public TestAssertion Assert = new TestAssertion();
	private SqlMapClient sqlMap;
	private int countA = 0;
	private int countB = 0;

	@Before
	public void beforeTestCase() {
		Step("����׼��");
		ZdalTestCommon.dataPrepareForZds();

		sqlMap = (SqlMapClient) ZdalRwSuite.context.getBean("zdalRwMysql1");
	}

	@After
	public void afterTestCase() {
		Step("ɾ������");
		ZdalTestCommon.dataDeleteForZds();
	}

	@Subject("��������ָ��·�ɵ���ͬdb")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952181() {

		Step("����·�� ��ͬһ����");
		ThreadLocalMap.put(ThreadLocalString.DB_NAME_USED_BY_GROUP_SQL, null);
		ThreadLocalMap.put(ThreadLocalString.SET_GROUP_SQL_DATABASE,
				"SET_GROUP_SQL_DATABASE");
		Step("������");
		testReadDb();
		Step("���ԣ�Ӧ���Ǵ�ͬһ�����ж�ȡ��");
		Assert.areEqual(true, 10 == countA || countB == 10, "the count value");

		Step("�رգ�ָ��·�ɵ���ͬ��");
		ThreadLocalMap.put(ThreadLocalString.SET_GROUP_SQL_DATABASE, null);
		ThreadLocalMap.put(ThreadLocalString.DB_NAME_USED_BY_GROUP_SQL, null);

	}

	@Subject("���������ȴ򿪣���ر�ճ��ģ��")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952182() {
		Step("��ʹ��ճ��ģ��");
		TC952181();

		Step("�Ѿ��ر���ճ��ģ�� �����Ӷ�10��");
		testReadDb();
		Assert.areEqual(true, 10 > countA && countB < 10, "the count value");
	}

	/**
	 * ������ȡdb
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void testReadDb() {
		countA = 0;
		countB = 0;
		Step("������10��");
		for (int i = 0; i < 10; i++) {
			try {
				List<Object> a = (List<Object>) sqlMap
						.queryForList("queryRwSql");
				for (int j = 0; j < a.size(); j++) {
					HashMap<String, String> hs = (HashMap<String, String>) a
							.get(j);
					if ("DB_A".equalsIgnoreCase((String) hs.get("colu2"))) {
						countA++;
					} else if ("DB_B"
							.equalsIgnoreCase((String) hs.get("colu2"))) {
						countB++;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
