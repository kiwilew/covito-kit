package com.covito.test;

public class MyThreadPrinter2 implements Runnable{
	private String name;     
    private Object prev;     
    private Object self;     
    
    private MyThreadPrinter2(String name, Object prev, Object self) {     
        this.name = name;     
        this.prev = prev;     
        this.self = self;     
    }     
    
    public void run() {     
        int count = 10;     
        while (count > 0) {     
            synchronized (prev) {     
                synchronized (self) {     
                    System.out.print(name);     
                    count--;    
                    try{  
                    Thread.sleep(1);  
                    }  
                    catch (InterruptedException e){  
                     e.printStackTrace();  
                    }  
                      
                    self.notify();     
                }     
                try {     
                    prev.wait();     
                } catch (InterruptedException e) {     
                    e.printStackTrace();     
                }     
            }     
    
        }     
    }     
    
    public static void main(String[] args) throws Exception {  
    	
        Object a = new Object();     
        Object b = new Object();     
        Object c = new Object();     
        MyThreadPrinter2 pa = new MyThreadPrinter2("A", c, a);     
        MyThreadPrinter2 pb = new MyThreadPrinter2("B", a, b);     
        MyThreadPrinter2 pc = new MyThreadPrinter2("C", b, c);     
             
             
        new Thread(pa).start();  
        Thread.sleep(10);  
        new Thread(pb).start();  
        Thread.sleep(10);  
        new Thread(pc).start();  
        Thread.sleep(10);  
    }   
}
