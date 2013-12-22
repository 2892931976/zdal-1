package com.alipay.zdal.valve.test.utils;

import java.net.UnknownHostException;
import java.util.Properties;

import javax.management.Attribute;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * 
 * @author tao.he
 * @version $Id: DataSourceMonitorTestHelper.java, v 0.1 2012-2-19 ����02:45:44 tao.he Exp $
 */
public class DataSourceMonitorTestHelper {

    /** ��־���*/
    private static final Logger          logger                = Logger
                                                                   .getLogger(DataSourceMonitorTestHelper.class);
    /**jboss��jndi�����ṩ���ṩ��InitialContextʵ�ֹ���*/
    private static final String          JBOSS_CONTEXT_FACTORY = "org.jnp.interfaces.NamingContextFactory";

    /**��ʼ�������ṩ�ߵ�URL�������ó�ʼ������*/
    private static final String          PROVIDE_URL           = "jnp://placeHolder:1099";

    private static final String          URL_PKGS              = "org.jboss.naming:org.jnp.interfaces";

    /**��jboss mbeanServer����*/
    private static MBeanServerConnection server;

    private static String                host;
    static {
        setHost();
        startClient();
    }

    public static void changeAttrValue(String dsName, String attribteName, Object value) {
        try {
            ObjectName objectName = new ObjectName("jboss.jca:name=" + dsName
                                                   + ",service=ManagedConnectionPool");
            Attribute attribute = new Attribute(attribteName, value);
            server.setAttribute(objectName, attribute);
        } catch (Exception e) {
            logger.error("Ϊ" + dsName + "�����ԣ�" + attribteName + ",��ֵ:" + value + ",ʱ���쳣��", e);
        }
    }

    public static void changeAttrValue(ObjectName objectName, String attribteName, Object value) {
        try {
            Attribute attribute = new Attribute(attribteName, value);
            server.setAttribute(objectName, attribute);
        } catch (Exception e) {
            logger.error("Ϊ" + objectName.toString() + "�����ԣ�" + attribteName + ",��ֵ:" + value
                         + ",ʱ���쳣��", e);
        }
    }

    private static void setHost() {
        //ȡ��������
        String localHost = null;
        try {
            localHost = java.net.InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            localHost = "localhost";
        }
        host = localHost;
    }

    private static void startClient() {
        final Properties properties = new Properties();
        properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, JBOSS_CONTEXT_FACTORY);
        final String url = PROVIDE_URL.replace("placeHolder", host);
        properties.setProperty(Context.PROVIDER_URL, url);
        properties.setProperty(Context.URL_PKG_PREFIXES, URL_PKGS);
        InitialContext initialContext;
        try {
            initialContext = new InitialContext(properties);
            server = (MBeanServerConnection) initialContext.lookup("jmx/invoker/RMIAdaptor");
        } catch (NamingException e) {
            logger.error("", e);
        }
    }

    @Test
    public void testDs() {
        DataSourceMonitorTestHelper.changeAttrValue("DefaultDS", "MaxSize", 50);
    }
}
