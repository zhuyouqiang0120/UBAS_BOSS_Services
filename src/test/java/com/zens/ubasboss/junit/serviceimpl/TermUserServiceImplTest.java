/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasboss.junit.serviceimpl								 
 **    Type    Name : TermUserServiceImplTest 							     	
 **    Create  Time : 2017年3月17日 上午10:18:04								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2017 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasboss.junit.serviceimpl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


import org.junit.Test;

import com.jfinal.ext.test.ControllerTestCase;
import com.jfinal.kit.JsonKit;
import com.sun.xml.internal.fastinfoset.tools.FI_DOM_Or_XML_DOM_SAX_SAXEvent;
import com.swetake.util.Qrcode;
import com.zens.ubasbossservices.config.Dconfig;
import com.zens.ubasbossservices.service.T_TermUserService;
import com.zens.ubasbossservices.serviceimpl.T_TermUserServiceImpl;

/**
 * @author  Floristy
 * @email   yangsen@zensvision.com
 * @create  2017年3月17日上午10:18:04
 * @version 1.0 
 */
public class TermUserServiceImplTest  {
   public static void main(String[] args) throws IOException {
	    String FilePath = "d:/qrcode.png";  
        File  destFile = new File(FilePath);  

        String encodeddata="赵宇佳是S B";
	   Qrcode qrcode = new Qrcode();  
       qrcode.setQrcodeErrorCorrect('M');  // 纠错级别（L 7%、M 15%、Q 25%、H 30%）和版本有关  
       qrcode.setQrcodeEncodeMode('B');      
       qrcode.setQrcodeVersion(7);     // 设置Qrcode包的版本  
         
       byte[] d = encodeddata.getBytes("GBK"); // 字符集  
       BufferedImage bi = new BufferedImage(139, 139, BufferedImage.TYPE_INT_RGB);  
       // createGraphics   // 创建图层  
       Graphics2D g = bi.createGraphics();  
         
       g.setBackground(Color.WHITE);   // 设置背景颜色（白色）  
       g.clearRect(0, 0, 139, 139);    // 矩形 X、Y、width、height  
       g.setColor(Color.BLACK);    // 设置图像颜色（黑色）  
 
       if (d.length > 0 && d.length < 123) {  
           boolean[][] b = qrcode.calQrcode(d);  
           for (int i = 0; i < b.length; i++) {  
               for (int j = 0; j < b.length; j++) {  
                   if (b[j][i]) {  
                       g.fillRect(j * 3 + 2, i * 3 + 2, 3, 3);  
                   }  
               }  
           }  
       }  
         
//     Image img = ImageIO.read(new File("D:/tt.png"));  logo  
//     g.drawImage(img, 25, 55,60,50, null);  
                 
       g.dispose(); // 释放此图形的上下文以及它使用的所有系统资源。调用 dispose 之后，就不能再使用 Graphics 对象  
       bi.flush(); // 刷新此 Image 对象正在使用的所有可重构的资源  
 
       ImageIO.write(bi, "png", destFile);  
       System.out.println("Input Encoded data is：" + encodeddata);  
}


}
