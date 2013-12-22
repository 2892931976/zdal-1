package com.alipay.zdal.client.jdbc.dbselector;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.alipay.zdal.client.jdbc.DBSelector;
import com.alipay.zdal.client.jdbc.ZdalStatement.DB_OPERATION_TYPE;
import com.alipay.zdal.common.DBType;
import com.alipay.zdal.common.jdbc.sorter.ExceptionSorter;
import com.alipay.zdal.common.jdbc.sorter.MySQLExceptionSorter;
import com.alipay.zdal.common.jdbc.sorter.OracleExceptionSorter;

public abstract class AbstractDBSelector implements DBSelector {
    private static final Map<DBType, ExceptionSorter> exceptionSorters = new HashMap<DBType, ExceptionSorter>(
                                                                           2);
    static {
        exceptionSorters.put(DBType.ORACLE, new OracleExceptionSorter());
        exceptionSorters.put(DBType.MYSQL, new MySQLExceptionSorter());
    }
    private DBType                                    dbType           = DBType.MYSQL;
    protected ExceptionSorter                         exceptionSorter  = exceptionSorters
                                                                           .get(dbType);
    private String                                    id;

    /**
     * added by fanzeng.
     * ��ʾͬһ��dbSeletor������ѡ�񵽵�����Դ��ʶ��
     */
    private String                                    selectedDSName;

    //����Դ���ƣ����������Ϣ��add by ���� 20130903
    private String                                    appDsName;

    public AbstractDBSelector(String id) {
        this.id = id;
    }

    public String getSelectedDSName() {
        return selectedDSName;
    }

    public void setSelectedDSName(String setSelectedDSName) {
        this.selectedDSName = setSelectedDSName;
    }

    public <T> T tryExecute(DataSourceTryer<T> tryer, int times, DB_OPERATION_TYPE operationType,
                            Object... args) throws SQLException {
        return this.tryExecute(new LinkedHashMap<DataSource, SQLException>(0), tryer, times,
            operationType, args);
    }

    public DBType getDbType() {
        return dbType;
    }

    public void setDbType(DBType dbType) {
        this.dbType = dbType;
        this.exceptionSorter = exceptionSorters.get(this.dbType);
    }

    public final void setExceptionSorter(ExceptionSorter exceptionSorter) {
        //add by shenxun:��Ҫ���Ƿ�����ԡ�����������dbSelector�ṹ̫����
        this.exceptionSorter = exceptionSorter;
    }

    public String getId() {
        return id;
    }

    public void setAppDsName(String appDsName) {
        this.appDsName = appDsName;
    }

    public String getAppDsName() {
        return appDsName;
    }
}
