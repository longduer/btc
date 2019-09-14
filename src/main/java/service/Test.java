package service;

public class Test {
	public static void main(String[] args) {
 
		// 事件源（被监听的对象）
		EventSource m1 = new EventSource();
		EventSource2 m2 = new EventSource2();
		// 监听器
		IEventListener mEventListener = new IEventListener() {
 
			@Override
			public void doEvent(IEvent arg) {
				if (true == arg.ClickButton()) {
					System.out.println("你点击了按钮");
				}else if(true == arg.MoveMouse()){
					System.out.println("你移动了鼠标");
				}
			}
		};
 
		// 注册监听器到事件源
//		m1.setEventListener(mEventListener);
//		m1.mouseEventHappened();
		
		// 注册监听器到事件源
		m2.setEventListener(mEventListener);
		m2.buttonEventHappened();
	}
}