/**
 * @project  : UCMS
 * @Author   : Chasonx.Xiang
 * @Date     : 2016年1月8日
 * @Email    : zuocheng911@163.com
 * @Copyright: 2016 Inc. All right reserved. 版权所有，翻版必究
 */
package com.zens.ubasbossservices.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author  Chason.x <zuocheng911@163.com>
 * @createTime 2016年1月8日 上午10:09:28
 * @remark  获取参数
 */
@Target({ElementType.TYPE,ElementType.METHOD})//类型的注解可以注解的程序元素的范围
@Retention(RetentionPolicy.RUNTIME)//保留时间长短
@Documented//指明拥有这个注解的元素可以被javadoc此类的工具文档化
public @interface ApiDesc {
	String value() default "";
}
