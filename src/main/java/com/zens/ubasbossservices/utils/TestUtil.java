package com.zens.ubasbossservices.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.junit.Test;

import com.zens.activiti.hessian.ActivitiHessian;
import com.zens.ubasbossservices.common.ApiUtils;
import com.zens.ubasbossservices.common.HessianFactory;
import com.zens.ubasbossservices.common.Message;
import com.zens.ubasbossservices.serviceimpl.T_TermUserServiceImpl;

public class TestUtil {
	public static void main(String[] args) throws IOException {
		FileInputStream fin = new FileInputStream(
				"D:/ubas-boss-service/src/main/webapp/boss.import.test/大理订购关系数据20180102.xls");
		try {

			POIFSFileSystem poifs = new POIFSFileSystem(fin);
			try {

				// 从流中获取Exc</span>el的WorkBook流
				InputStream workBookInputStream = poifs.createDocumentInputStream("Workbook");
				try {
					HSSFRequest hssfRequest = new HSSFRequest();
					// 为所有的record注册一个监听器
					hssfRequest.addListenerForAllRecords(new HSSListenerImpl());
					// 创建事件工厂
					HSSFEventFactory factory = new HSSFEventFactory();
					// 根据WorkBook输入流处理所有事件
					factory.processEvents(hssfRequest, workBookInputStream);
				} finally {
					workBookInputStream.close();
				}
			} finally {
				// poifs.
			}
		} finally {
			// 一旦所有的监听器处理完成，关闭文件输入流
			fin.close();
		}
	}

	@FunctionalInterface
	public interface UAGetTOken {
		void create(String IP);
	}

	@Test
	public void Test1() {
		T_TermUserServiceImpl t=new T_TermUserServiceImpl();
		try {
			System.out.println("@22");
			
			System.out.println(t.UAGetTOken("0:0:0:0:0:0:0:1"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Test
	public void Test11() {
		String key ="321";
		String Category ="2121";
		String tenantId = "21";
		ActivitiHessian activitiHessian;
		try {
			activitiHessian = HessianFactory.create(ActivitiHessian.class, "http://192.168.0.144:8080/Artiviti/avt");
			String data1 = activitiHessian.selectModelid(key, Category, tenantId);
			System.out.println("hessian调用结果: "+data1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
	}
}