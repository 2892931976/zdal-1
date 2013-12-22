package com.alipay.zdal.test.tair;

import com.alipay.zdal.tair.impl.ZdalTairCacheManager;

public class TestBaseOne {
	

	/**
	 * 
	 * @param appTairName
	 * @param dbmode
	 * @param configPath
	 * @param uid
	 * @param originalKey
	 * @param putvalue
	 * @return
	 */
	public  static Object getInstanceWithLocalpath(String appTairName,String dbmode,String configPath,String uid,String originalKey,Object putvalue) {
		ZdalTairCacheManager zdalTairCacheManager = new ZdalTairCacheManager();
		zdalTairCacheManager.setAppTairName(appTairName);
		zdalTairCacheManager.setCurrentMode(dbmode);
		zdalTairCacheManager.setLocalConfigurationPath(configPath);
		try {
			//1.��ʼ��
		zdalTairCacheManager.init();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		try {
			// 2.�����µ�key
			// (udΪuid,okΪԭʼ��,
			// wsΪдģʽ[0:LEGACY_MASTER,1:LEGACY_MASTER_LDC_REDUNDANT,2:LEGACY_MASTER_LDC_MASTER],3:LDC_MASTER,4:LEGACY_DEDUNDANT_LDC_MASTER
			// tiΪѡ���tairlogicId,
			// mfΪmaster��failoverģʽ[1:master,0:failover])
			String newKey = zdalTairCacheManager.createZdalTairKey(
					uid ,originalKey);
			System.out.println("===newkey:" + newKey);
			// 3.дֵ
			boolean bl = zdalTairCacheManager
					.putObject(newKey, putvalue);
			// 4.��ֵ������
			return zdalTairCacheManager.getObject(newKey);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return null;		
	}
	
	/**
	 * 
	 * @param appTairName
	 * @param dbmode
	 * @param configPath
	 * @param uid
	 * @param originalKey
	 * @param putvalue
	 * @return
	 * @throws Exception
	 */
	public static void getInstanceWithLocalpathException(String appTairName,String dbmode,String configPath,String uid,String originalKey,Object putvalue) throws Exception{
		ZdalTairCacheManager zdalTairCacheManager = new ZdalTairCacheManager();
		zdalTairCacheManager.setAppTairName(appTairName);
		zdalTairCacheManager.setCurrentMode(dbmode);
		zdalTairCacheManager.setLocalConfigurationPath(configPath);
		try {
			//1.��ʼ��
		zdalTairCacheManager.init();
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
		
		try {
			String newKey = zdalTairCacheManager.createZdalTairKey(
					uid ,originalKey);
			System.out.println("===newkey:" + newKey);
						
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
			
		}
	}

}
