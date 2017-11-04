package com.baoidc.idcserver.core;

//时长计算工具类
public class DurationUtil{
	
	//时长字符串转换成时长数字
	public static int durationStrToNum(String durationStr){
		int duration = 0;
		if(durationStr.contains("月")){
			duration = Integer.parseInt(durationStr.substring(0,durationStr.indexOf("个")));
		}else{
			if("半年".equalsIgnoreCase(durationStr)){
				duration = 6;
			}else{
				duration = Integer.parseInt(durationStr.substring(0,durationStr.indexOf("年"))) * 12;
			}
		}
		return duration;
	}
	
	public static String numToDurationStr(int num){
		StringBuilder durationStr = new StringBuilder();
		if(num < 6){
			durationStr.append(num).append("个月");
		}else if(num == 6){
			durationStr.append("半年");
		}else if(num > 6 && num < 12){
			durationStr.append(num).append("个月");
		}else if(num == 12){
			durationStr.append("1年");
		}else if(num >12 && num < 24){
			durationStr.append(num).append("个月");
		}else if(num == 24){
			durationStr.append("2年");
		}else if(num > 24 && num < 36){
			durationStr.append(num).append("个月");
		}else if(num == 36){
			durationStr.append("3年");
		}else if(num > 36){
			durationStr.append(num).append("个月");
		}
		return durationStr.toString();
	}
	
	/*public static void main(String[] args) {
		int str = DurationUtil.durationStrToNum("37个月");
		System.out.println(str);
	}*/

}
