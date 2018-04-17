package com.keyten.base.test;

import org.junit.Test;

import com.keyten.base.util.LogUtil;

public class BaseUtilTest {
	@Test
	public void LogTest() {
		LogUtil.addLogInfo("我错了",this.getClass().getName());
		LogUtil.addLogError("异常了", this.getClass().getName());
	}
	@Test
	public void testPath() {
		System.out.println(this.getClass().getName());
	}
}
