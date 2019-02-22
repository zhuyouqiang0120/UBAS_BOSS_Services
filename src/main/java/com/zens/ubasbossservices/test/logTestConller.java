package com.zens.ubasbossservices.test;

import com.chasonx.apidoc.ApiDataExample;
import com.chasonx.apidoc.ApiField;
import com.chasonx.apidoc.Apidoc;
import com.chasonx.apidoc.ApiParams;
import com.jfinal.core.Controller;
@Apidoc(title = "测试",version = "1.0.0",remark = "test",route = "/manager/test")
@ApiDataExample("com/zens/ubasbossservices/ex")
public class logTestConller extends Controller {

	@Apidoc(title = "添加用户", remark = "增加系统用户信息", method = "POST", route = "/addUser")
	@ApiParams({ @ApiField(name = "userName", length = 30, desc = "用户名"),
			@ApiField(name = "userPwd", length = 30, desc = "用户密码"),
			@ApiField(name = "email", length = 60, desc = "邮箱") })
	@ApiDataExample
	public void logTest() {

		renderText("HELLO TEXT");
	}

}
