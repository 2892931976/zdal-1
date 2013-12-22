package com.alipay.zdal.client;

public class ThreadLocalString {
    public static final String ROUTE_CONDITION = "ROUTE_CONDITION",
            IS_EXIST_QUITE = "IS_EXIST_QUITE";
    public static final String TABLE_MERGE_SORT_TABLENAME = "TABLE_MERGE_SORT_TABLENAME",
            TABLE_MERGE_SORT_POOL = "TABLE_MERGE_SORT_POOL",
            TABLE_MERGE_SORT_VIRTUAL_TABLE_NAME = "TABLE_MERGE_SORT_VIRTUAL_TABLE_NAME";
    /**
    * added by fanzeng,��ѡ��ĳ��������ִ��ĳ��sql
    */
    public static final String DATABASE_INDEX             = "DATABASE_INDEX";

    /** ��ָ�����ʱ��ֻѡ��д�⣬������failover. */
    public static final String WRITE_DATABASE_READ_RETRY  = "WRITE_DATABASE_READ_RETRY";
    /**
     * added by fanzeng,��֧��cif��������󣬼�ѡ����⻹��д����ִ��ĳ��sql
     */
    public static final String SELECT_DATABASE            = "SELECT_DATABASE";
    /**
     * added by fanzeng, ֧��cif�Լ���Ϣ��������ģʽ��������󣬼�����sql�����ĸ���ִ�еģ��Լ��ÿ�ı�ʶid
     */
    public static final String GET_ID_AND_DATABASE        = "GET_ID_AND_DATABASE";
    /**
     * added by fanzeng, ֧�� tradeϵͳ�ڰ�����Դkey�������ѡ��dbʱ������autocommit�������������Ա�֤����
     */
    public static final String GET_DB_ORDER_IN_GROUP      = "GET_DB_ORDER_IN_GROUP";
    public static final String GET_AUTOCOMMIT_PROPERTY    = "GET_AUTOCOMMIT_PROPERTY";

    /**
     * added by fanzeng, ���ú�һ��sql��ִ��db������ͬһ������������˹��ϣ���ȥ��һ�����õġ� 
     */
    public static final String SET_GROUP_SQL_DATABASE     = "SET_GROUP_SQL_DATABASE";
    public static final String DB_NAME_USED_BY_GROUP_SQL  = "DB_NAME_USED_BY_GROUP_SQL";

}
