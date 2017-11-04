package com.baoidc.idcserver.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.4
 *修改日期：2016-03-08
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
	//public static String partner = "";
	public static String partner = "2088912244200800";
	
	// 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
	public static String seller_id = partner;

	//商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
	public static String private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDj1yN9CrlUvXE36wUzapONALok6E1/J5EZ7EBPEgOBNX279M1jvyQVc5Bt1wnCeZK5dSh/J/EG/r5NCwbYCXCNTNa1CQkF+TV4RsvbK3gtTJagG42CRbtm3S0m+lGrC6A2uITsn305/dpg8J37KFhNfD5mGVXUqoj4MRjRDTiaJN5ixKQABB/mcou+UHEe9TzmyzYap8TF+CLiMbXEtK5+tpOvst9v72imFKviM8pThwbtQishLpMcAFUkOmlbcWWBdOxMcVGo76Yj40hwWh0utFxfuJtaVxphVKVR3fE910SA8sG0vcn61VVvEZqnX+To7NSL7yPf+QmQu0uD3dULAgMBAAECggEBAMTKqA5y2s0Eq+vehSl53mQlwqHqPDWd2UiItKsXJgvORgG4WkbTa7lTjeYxnG5rT338YbQirZ6bHYeyBIFI9fw16CWJ3k9YhwSSMbXmf/CwfFFiOwHQdSNN2pgWEcApVIKvMbCnuSQDzUF50xqCKHMU41iQfYAopBGrZoH6Z29K01fsWleJRMZSfrh4I/rVak30SiPJAHvhe3HInOc/qogtaIT94M6EaueW+4KqjSokr27GUj1rm2BmWvKWA9jWUxEIKg6/nwZR38YoR++JwBZSJFsJxfDrM6X3BteyAzjwV/EtAioKKA3u/FYRhxeXXzY52TuoGTYoFZ/Ar4FriQECgYEA/ShqgTs5oGcsqYjcv3go4EOT47lOwzUeM6DjJiPTZH1EjBeNde0vGbuNb55TYJPywmk3K/ER1QUwFOUBxaf+2kbKr9JnVIWazoK4qakS6BaPYC68QJ3VuEbQTVMtp3pjDzQOw3D58bO1zObmLLxy7pITXspg9OlU1PpehHMjassCgYEA5mX1lOPDRQqT9tYGBYVvIVB88Mjcvrgt1G5XcKsI88olC3IXlVKk/Gq21Yr86iEvJ99vPNuWcGkrUZmadXx/mKCvz+M7yp9AqZvLJBoq8YvNM1egFQqqu/pubuJrUOeb0juek3T1jjw4Imsb1vFhs8B/wRytcrDquJcqf/r6tsECgYB9EwnDvXoUKMVlIWxG4Nv5KQoZGYWzZjv8sAFp54tae5VxTXWjjMKXiRUwz11MOuxWZttOR3VS9JQzzo0YlY6RUyZyxZYtqTC51NXdZN4lcnf1wkFZ/2EHAV2Kxoyo0Qu+CC8/GkMyymII0Q9pjNWPzvXpAD304o76BpTKBYS7DwKBgCsZv8tOoGT2CaAPgajewV9WE0loCgOTqN8olSr6k/4nPq7ZdRf2Ee82d2WTTqTt+EhZQBH+cjItOnFVbtOzqpr+1GTreW/6Qrp37pSeYJI3OPf12HiXOkFYI/iSWskTys6VYKDtLFKjaUznBJo9Ja3ewQd7OedOuVo2zTiL1wMBAoGAe1Qvl5sD9DcHLYcGcPArGEaPyCf714d5u7m5mFGNg4Y3KQyzzgKhmxw/pBDRk58KHHe0IfXFP9OtzxQVKsKK3LjbPxqXHs2MYa2hjG2ooSinEkCciT0Wy7x53jlTe8qPgVIHnE3Jc+AxSc3cggpIWqhzUSC/kHIFpQrwDwnKZ1Q=";
	
	// 支付宝的公钥,查看地址：https://b.alipay.com/order/pidAndKey.htm
	public static String alipay_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	//public static String notify_url = "http://商户网址/create_direct_pay_by_user-JAVA-UTF-8/notify_url.jsp";
	public static String notify_url = "http://127.0.0.1:8080/idcserver/servlet/aliPayResult";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	//public static String return_url = "http://商户网址/create_direct_pay_by_user-JAVA-UTF-8/return_url.jsp";
	public static String return_url = "http://127.0.0.1:8080/idcserver/servlet/aliPayResult";

	// 签名方式
	public static String sign_type = "RSA";
	
	// 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。
	public static String log_path = "F:\\";
		
	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
		
	// 支付类型 ，无需修改
	public static String payment_type = "1";
		
	// 调用的接口名，无需修改
	public static String service = "create_direct_pay_by_user";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	
//↓↓↓↓↓↓↓↓↓↓ 请在这里配置防钓鱼信息，如果没开通防钓鱼功能，为空即可 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	
	// 防钓鱼时间戳  若要使用请调用类文件submit中的query_timestamp函数
	public static String anti_phishing_key = "";
	
	// 客户端的IP地址 非局域网的外网IP地址，如：221.0.0.1
	public static String exter_invoke_ip = "";
		
//↑↑↑↑↑↑↑↑↑↑请在这里配置防钓鱼信息，如果没开通防钓鱼功能，为空即可 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	
}

