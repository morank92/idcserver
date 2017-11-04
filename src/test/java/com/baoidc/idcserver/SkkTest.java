package com.baoidc.idcserver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;

import com.baoidc.idcserver.dao.IUserAccountDAO;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:application-context.xml")
public class SkkTest {

	@Autowired
	private IUserAccountDAO userAccountDAO;
	
	public static void main(String[] args) {
		/*
		int des = 20;
		int[] srcArray = {10,20,30,40,50,60,70,80,90};
		 int low = 0;
		    int high = srcArray.length - 1;
		 
		    while ((low <= high) && (low <= srcArray.length - 1)
		            && (high <= srcArray.length - 1)) {
		        int middle = (high + low) >> 1;
		        if (des>=srcArray[middle] && des<=srcArray[middle+1]) {
		            System.out.println(middle+1);
		            return;
		        } else if (des < srcArray[middle]) {
		            high = middle - 1;
		        } else {
		            low = middle + 1;
		        }
		    }
		    System.out.println(-1);
		    
		    */
	}
	@Test
	public void test2(){
		GregorianCalendar gc = new GregorianCalendar();
		gc.add(Calendar.YEAR, 2017);
		gc.add(Calendar.MONTH, 5);
		gc.add(Calendar.DAY_OF_MONTH, 22);
		Date time = gc.getTime();
		System.out.println(time);
	}
	
	@SuppressWarnings("resource")
	@Test
	public void test1()throws Exception{
		Jedis jedis = new Jedis("192.168.7.112");
		jedis.auth("myredis");
		Set<String> keys = jedis.keys("s*");
		System.out.println(keys);
	}
	
	@Test
	public void test3(){
		GregorianCalendar gc = new GregorianCalendar();
		gc.setGregorianChange(new Date());
		System.out.println(gc.get(Calendar.YEAR));
		System.out.println(gc.get(Calendar.MONTH));
		int nowYear = gc.get(Calendar.YEAR);
		int nowMonth = gc.get(Calendar.MONTH)+1;
		String startTime = "";
		if(nowMonth>=6){
			int startMonth = nowMonth-5;
			startTime = nowYear+"-"+startMonth;
		}else{
			int startMonth = nowMonth+7;
			int startYear = nowYear-1;
			startTime = startYear+"-"+startMonth;
		}
		System.out.println(startTime);
		System.out.println("-----------");
		String[] timeArr = new String[6];
		for (int i = 0; i < 6; i++) {
			int flag = nowMonth-i;
			if(flag>0){
				System.out.println(nowYear+"-"+flag);
				timeArr[i] = nowYear+"-"+flag;
			}else{
				System.out.println((nowYear-1)+"-"+(12-flag));
				timeArr[i] = (nowYear-1)+"-"+(12-flag);
			}
		}
		for (String string : timeArr) {
			System.out.println(string);
		}
	}
}
