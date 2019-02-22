package com.zens.ubasbossservices.interceptor;
import javax.servlet.http.HttpServletRequest;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.upload.MultipartRequest;
import com.zens.ubasbossservices.common.Constant;
import com.zens.ubasbossservices.entity.T_App_User;
import com.zens.ubasbossservices.utils.AppUTil;


/**
 * 登录 操作验证拦截器(接口加密+登录验证+SESSION用户会话）
 * 
 * @author ZKill
 * @email zhangkai@zensvision.com
 * @create 2017年11月3日 10:41:37
 * @version 1.0
 */
public class CheckInterceptor implements Interceptor{

	public void intercept(Invocation inv) {
		String timestamp;
		String encryptiontoken;
		MultipartRequest multi = null;
		Controller controller=inv.getController();
		//获取到APP商户端传过来的密文和时间戳和token令牌
		String token=controller.getCookie(Constant.LOGINGCOOKIE);
		String account=controller.getCookie("ACCOUNT");
		T_App_User t_App_User=controller.getSessionAttr("userKey");
		System.out.println("当前的会话用户对象是"+t_App_User);
		if(token==null||t_App_User==null){
			//说明登录超时或者还没有登录,APP商户端让用户重新登录
			controller.renderJavascript("202");
			return;
		}
		//System.out.println(x);
		if(CacheKit.get(Constant.EHCACHENAME,Constant.token+account)!=null&&
				!CacheKit.get(Constant.EHCACHENAME,Constant.token+account).equals(token)){
			//说明已经有相同的帐号登录，无法进行操作请重新登录
			controller.renderJavascript("405");
			return;
		}
		//获取此次请求客户端的IP地址
		HttpServletRequest request=controller.getRequest();
		String IPAddr=request.getRemoteAddr();
		if(request.getContentType()!=null){
		multi = new MultipartRequest(request);
		//获取时间时间戳
		 timestamp=multi.getParameter("timestamp");
		//获取密文
		 encryptiontoken=multi.getParameter("encryptiontoken");
		}else{
			//获取时间时间戳
		timestamp=request.getParameter("timestamp");
		//获取密文
		encryptiontoken=request.getParameter("encryptiontoken");
		}
		 //从缓存中去取值,（防止二次恶意请求）
		if(CacheKit.get(Constant.EHCACHENAME, IPAddr+timestamp+encryptiontoken)!=null){
			//恶意请求,请求不合法
		controller.renderJavascript(CacheKit.get(Constant.EHCACHENAME, IPAddr+timestamp+encryptiontoken).toString());
		return;
	}
		//合法请求,时间戳和token加密生成摘要和密文进行比较
		String MD5TOKEN=AppUTil.MD5Encode(timestamp+token,"UTF-8");
		//摘要一致
		if(MD5TOKEN.equals(encryptiontoken)){
			//通过验证
			// 把本次请求标识 放入缓存之中（防止二次恶意请求）
			CacheKit.put(Constant.EHCACHENAME,IPAddr+timestamp+encryptiontoken,"503");
			controller.setAttr("multi", multi);
			controller.setAttr("user", t_App_User);
		
			inv.invoke();
		}else{
			//此次的请求不合法
			controller.renderJavascript("403");
		}
		
	}

}
