package com.zony.app;

import java.util.HashMap;
import java.util.Map;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.zony.common.utils.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ZonySpireTest {
	/**
	 * 方法1：测试接口是否连通
	 */
	@Test
	public void method1() {
		ComThread.InitSTA();
		try {
			Dispatch test = new Dispatch("ZonySpire.ReadWord");
			Variant result = Dispatch.call(test, "GetInfo", "f:\\深圳中海油档案系统设计说明书v1.1.docx", "f:\\PureColor.docx");
			System.out.println(result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ComThread.Release();
		}
	}

	/**
	 * 方法2：读取文档表格数据
	 */
	@Test
	public void method2() {
		ComThread.InitSTA();
		try {
			Dispatch test = new Dispatch("ZonySpire.ReadWord");
			Variant result = Dispatch.call(test, "ReadTable", "f:\\1019-CG-04-02-06 中国海洋石油有限公司制度文档化操作细则(1).doc");
			System.out.println(result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ComThread.Release();
		}
	}

	/**
	 * 方法3：读取文档文本段数据
	 */
	@Test
	public void method3() {
		ComThread.InitSTA();
		try {
			Dispatch test = new Dispatch("ZonySpire.ReadWord");
			Variant result = Dispatch.call(test, "ReadText", "f:\\1019-CG-02-01 中国海洋石油有限公司董事及高级管理人员道德守则更新管理办法.docx");
			System.out.println(result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ComThread.Release();
		}
	}

	/**
	 * 方法4：替换文档数据
	 */
	@Test
	public void method4() {
		ComThread.InitSTA();
		try {
			Dispatch test = new Dispatch("ZonySpire.ReadWord");
			Map<String, String> replaceMap = new HashMap<>();
			replaceMap.put("#{VERSION}", "A");
			String mapJson = JsonUtil.getInstance().obj2json(replaceMap);
			Variant result = Dispatch.call(test, "ReplaceText", "D:\\zonyprojects\\CNOOC_SYSTEM\\cnooc_system\\target\\test-classes\\public\\systemDoc\\Level_1.doc",
					"C:\\zony\\temp\\44ace757-ad33-44b0-9fbc-bd0f854da136.docx", mapJson);
			System.out.println(result.getBoolean());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ComThread.Release();
		}
	}
}
