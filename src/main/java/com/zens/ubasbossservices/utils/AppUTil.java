package com.zens.ubasbossservices.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.apache.catalina.User;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.chasonx.directory.FileUtil;
import com.jfinal.kit.PathKit;
import com.jfinal.template.expr.ast.Field;
import com.jfinal.upload.UploadFile;
import com.swetake.util.Qrcode;
import com.zens.ubasbossservices.common.Constant;

public class AppUTil {
	/**
	 * 发送验证码公用方法
	 * 
	 * @author ZKill
	 * @create 2017年11月7日 11:18:33
	 * @update
	 * @param result
	 *            发送短信的结果
	 * @param FVerificationcode
	 *            随机生成的验证码（五位数）
	 * @param FTelephone
	 *            手机号
	 * @return boolean
	 * 
	 * 
	 */
	public static boolean Short(String FVerificationcode, String FTelephone) throws Exception {
		boolean result = false;
		String content = new String("您的验证码是：" + FVerificationcode + "。请不要把验证码泄露给其他人。");
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		Constant.SHORTMESSAGE += "&account=" + Constant.SHORTAPIID + "&password=" + Constant.SHORTAPIKEY + "&mobile="
				+ FTelephone + "&content=" + content;
		System.out.println(Constant.SHORTMESSAGE);
		HttpPost httpPost = new HttpPost(Constant.SHORTMESSAGE);
		httpPost.setHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();
		if (httpEntity != null) {
			String resultXML = EntityUtils.toString(httpEntity, "UTF-8");
			System.out.println(resultXML);
			Map<String, String> resultmap = doXMLParse(resultXML);
			String code = resultmap.get("code");
			String smsid = resultmap.get("smsid");
			String msg = resultmap.get("msg");
			if (code.equals("2") && !smsid.equals("0") && msg.equals("提交成功")) {
				// 短信发送成功
				result = true;
			}
		}
		return result;

	}

	/**
	 * XML字符串解析
	 * 
	 * @author ZKill
	 * @create 2017年10月30日 15:05:27
	 * @return
	 */
	public static Map<String, String> doXMLParse(String strxml) {
		if (null == strxml || "".equals(strxml)) {
			return null;
		}
		Element root = null;
		System.out.println("~~开始解析XML字符串" + strxml);
		Map<String, String> map = new HashMap<String, String>();
		ByteArrayInputStream strm = new ByteArrayInputStream(strxml.getBytes());
		InputSource source = new InputSource(strm);
		// 创建一个新的SAXBuilder
		SAXBuilder sb = new SAXBuilder();
		try {
			Document doc = sb.build(source);
			// 取的根元素
			root = doc.getRootElement();
			System.out.println(root.getName());// 输出根元素的名称（测试）
			// 得到根元素所有子元素的集合
			List list = root.getChildren();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				String k = e.getName();
				String v = "";
				List children = e.getChildren();
				if (children.isEmpty()) {
					v = e.getTextNormalize();
					map.put(k, v);
					// System.out.println("解析XML的值~~~~~~"+v);
				} else {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * XML字符串的组装
	 * 
	 * @author ZKill
	 * @create 2017年10月31日 11:03:02
	 * @return
	 */
	public static String XMLString(Map<String, String> map) {
		String XMLString = "<xml>";
		for (Entry<String, String> maps : map.entrySet()) {
			XMLString += "<" + maps.getKey() + ">" + maps.getValue() + "<" + maps.getKey() + ">";
		}
		XMLString += "<xml>";

		return XMLString;
	}

	private static final String hexDigitS[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };

	public static String hexDigitS() {
		String sign = "";
		for (int j = 5; j >= 0; j--) {
			Random rand = new Random();
			int Math = rand.nextInt(6);
			sign += hexDigitS[Math];
		}
		return sign;
	}

	private static final String hexDigit[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

	public static String AccountRandom() {
		String sign = "";
		for (int j = 5; j >= 0; j--) {
			Random rand = new Random();
			int Math = rand.nextInt(6);
			sign += hexDigit[Math];
		}
		return sign;
	}

	public static String AccountRandomc() {
		String sign = "";
		for (int j = 9; j >= 0; j--) {
			Random rand = new Random();
			int Math = rand.nextInt(9);
			sign += hexDigit[Math];
		}
		return sign;
	}

	/**
	 * MD5加密算法
	 * 
	 * @author ZKill
	 * @create 2017年10月20日 10:01:06
	 * @update
	 * @param
	 * @return String
	 */
	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

	/**
	 *
	 * 
	 * @author ZKill
	 * @create 2017年10月20日 10:01:06
	 * @update
	 * @param
	 * @return String
	 */
	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	/**
	 * 获取十六进制字符串形式的MD5摘要
	 * 
	 * @author ZKill
	 * @create 2017年10月24日 13:56:01
	 * @update
	 * @param
	 * @return String
	 */

	public static String md5Hex(String src) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] bs = md5.digest(src.getBytes());
			return new String(new Hex().encode(bs));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 *
	 * 
	 * @author ZKill
	 * @create 2017年10月20日 10:01:06
	 * @update
	 * @param
	 * @return String
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	/**
	 * 编码字符串内容到目标File对象中
	 * 
	 * @param encodeddata
	 *            编码的内容
	 * @param destFile
	 *            生成file文件
	 * @throws IOException
	 */
	public static File qrCodeEncode(String encodeddata, File destFile) throws IOException {
		Qrcode qrcode = new Qrcode();
		qrcode.setQrcodeErrorCorrect('M'); // 纠错级别（L 7%、M 15%、Q 25%、H 30%）和版本有关
		qrcode.setQrcodeEncodeMode('B');
		qrcode.setQrcodeVersion(7); // 设置Qrcode包的版本

		byte[] d = encodeddata.getBytes("GBK"); // 字符集
		BufferedImage bi = new BufferedImage(139, 139, BufferedImage.TYPE_INT_RGB);
		// createGraphics // 创建图层
		Graphics2D g = bi.createGraphics();

		g.setBackground(Color.WHITE); // 设置背景颜色（白色）
		g.clearRect(0, 0, 139, 139); // 矩形 X、Y、width、height
		g.setColor(Color.BLACK); // 设置图像颜色（黑色）

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

		g.dispose(); // 释放此图形的上下文以及它使用的所有系统资源。调用 dispose 之后，就不能再使用 Graphics 对象
		bi.flush(); // 刷新此 Image 对象正在使用的所有可重构的资源

		ImageIO.write(bi, "png", destFile);
		return destFile;
	}

	/**
	 *
	 * 移动文件
	 * 
	 * @author ZKill
	 * @create 2017年10月20日 10:01:06
	 * @update
	 * @param
	 * @return String
	 * @throws IOException
	 */

	public static String fileMove(String userguid, String path) throws IOException {
		String renamePath = PathKit.getWebRootPath() + "/uploadFile" + "/" + userguid;
		File fromFile = new File(path);
	
		File toFile = new File(renamePath);
		if (!toFile.exists()) {
			// 不存在创建文件夹
			toFile.mkdirs();
		}
		long current=System.currentTimeMillis();
		 toFile = new File(renamePath+"/"+current+"_hp.png");
		// 移动文件到当前新创建的文件夹下
		copyFile(toFile, fromFile, renamePath,current);
		return current+"_hp.png";
	}

	/**
	 *
	 * 复制文件
	 * 
	 * @author ZKill
	 * @create 2017年10月20日 10:01:06
	 * @update
	 * @param
	 * @return String
	 * @throws IOException
	 */
	public static void copyFile(File toFile, File fromFile, String renamePath,long current) throws IOException {
		if (toFile.exists()) {// 判断目标目录中文件是否存在
			System.out.println("文件" + toFile.getAbsolutePath() + "已经存在，跳过该文件！");
			return;
		} else {
			// 创建新文件
			if (toFile.createNewFile()) {
				try {
					InputStream is = new FileInputStream(fromFile);// 创建文件输入流
					FileOutputStream fos = new FileOutputStream(toFile);// 文件输出流
					byte[] buffer = new byte[1024];// 字节数组
					while (is.read(buffer) != -1) {// 将文件内容写到文件中
						fos.write(buffer);
					}
					is.close();// 输入流关闭
					fos.close();// 输出流关闭
					// 修改文件名
					// 刪除临时目录文件
					//File old = new File(renamePath + "/" +current+"_hp.png");
					//toFile.renameTo(old);
					System.out.println("移动文件成功");
				} catch (FileNotFoundException e) {// 捕获文件不存在异常
					e.printStackTrace();
				} catch (IOException e) {// 捕获异常
					e.printStackTrace();
				}
			} else {
				System.out.println("创建新文件失败");
			}
		}
		System.out.println("复制文件" + fromFile.getAbsolutePath() + "到" + toFile.getAbsolutePath());
		//if(fromFile.delete()){
			//System.out.println("刪除原文件成功");
		//}
		
	}
}
