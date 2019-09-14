package service;

import java.util.concurrent.Callable;

public class TimeTask implements Callable<String> {
 
	@Override
	public String call() throws Exception {
		//执行任务主体,简单示例
		Thread.sleep(1000);
		return "hehe";
	}
}