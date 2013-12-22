package com.alipay.zdal.valve.test.utils;

import java.sql.SQLException;
import java.sql.Statement;


import com.alipay.zdal.valve.util.OutstripValveException;

/**
 * 
 * 
 * @author liangjie.li
 * @version $Id: ValveClient.java, v 0.1 2012-8-17 ����5:06:42 liangjie.li Exp $
 */
public class ValveClient implements Runnable {
    private Statement statement;
    private String    state;
    private int       unRequestNum = 0;   //ʣ��δ�������
    private int       qeuuestNum   = 0;   //���������
    private boolean   flag         = true;
    private int       exceptionNum = 0;   //���������쳣ʱ��������Ĵ���
    private int       stopNum      = 0;   //�������߳�����
    private int       UNLIMIT      = -10;

    public ValveClient() {
    }

    /**
     * ����һ��valve�Ŀͻ���
     * @param statement ��ǰ�߳�ִ��sql��statement
     * @param state ��ǰ�߳�ִ�е�sql���
     * @param type �������� 1-��sql���� 2-tair����
     * @param unRequestNum ��ǰ�߳�ִ��sql�Ĵ��� -10-��ʾ������ִ�д���
     */
    public ValveClient(Object object, String state, int unRequestNum) {
        this.statement = (Statement) object;
        this.state = state;
        this.unRequestNum = unRequestNum;
    }

    public void execute(Statement statement, String state) throws SQLException {
        statement.execute(state);
    }

    public int getQeuuestNum() {
        return qeuuestNum;
    }

    public int getExceptionNum() {
        return exceptionNum;
    }

    /**
     * �õ��Ѿ��������߳�������
     * @return
     */
    public int getStopNum() {
        return stopNum;
    }

    public void run() {

        while (true) {
            if (unRequestNum <= 0 && unRequestNum != UNLIMIT) {
                break;
            }
            try {
                qeuuestNum++;
                execute(statement, state);
            } catch (OutstripValveException e) {
                if (flag) {
                    exceptionNum = qeuuestNum;
                    flag = false;
                }
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (unRequestNum != UNLIMIT) {
                unRequestNum--;
            }
        }
        stopNum++;
    }

}
