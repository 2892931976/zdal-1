package com.alipay.zdal.common.monitor;

public interface MonitorLogUtilMBean {
    /**
     * <p>
     * StatLog�����ݲ���ʱ��
     * </p>
     * 
     * @return
     */
    long getWaitTime();

    /**
     * <p>
     * ����StatLog�����ݲ���ʱ��
     * </p>
     * 
     * @param waitTime
     */
    void setWaitTime(long waitTime);

    /**
     * <p>
     * ��ȡ���jvm��Ϣ����
     * </p>
     * 
     * @return
     */
    boolean getJVMInfoPower();

    /**
     * <p>
     * ���ü��jvm��Ϣ����
     * </p>
     * 
     * @return
     */
    void setJVMInfoPower(boolean power);
}
