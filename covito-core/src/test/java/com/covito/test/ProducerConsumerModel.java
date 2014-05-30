package com.covito.test;

import java.util.Random;

public class ProducerConsumerModel {
	
	

	public static class SynchronizedStack {
		private int index = 0;
		private int size = 100;
		// 内存共享区
		private int[] data;

		public SynchronizedStack(int size) {
			System.out.println("栈被创建");
			this.size = size;
			data = new int[size];
		}

		public synchronized void push(int c) {
			while (index == size) {
				try {
					System.err.println("栈满了");
					this.wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					e.printStackTrace();
				}
			}
			data[index] = c;
			index++;
			this.notify();// 通知其它线程把数据出栈
		}

		public synchronized int pop() {
			while (index == 0) {
				try {
					System.err.println("栈空了");
					this.wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					e.printStackTrace();
				}
			}
			index--; 
			int ch = data[index];
			this.notify(); // 通知其它线程把数据入栈
			return ch;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SynchronizedStack stack = new SynchronizedStack(8);
		Runnable source = new Producer(stack);
		Runnable sink = new Consumer(stack);

		Thread t1 = new Thread(source);
		Thread t2 = new Thread(sink);
		t1.start();
		t2.start();
	}

	public static class Consumer implements Runnable {
		private SynchronizedStack stack;

		public Consumer(SynchronizedStack s) {
			stack = s;
		}

		public void run() {
			int ch;
			for (int i = 0; i < 100; i++) {
				ch = stack.pop();
				System.out.println("Consumed:" + ch);
				try {
					Thread.sleep(new Random().nextInt(10)*1000);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public static class Producer implements Runnable {
		private SynchronizedStack stack;

		public Producer(SynchronizedStack s) {
			stack = s;
		}

		public void run() {
			int ch;
			for (int i = 0; i < 100; i++) {
				ch = (new Random().nextInt(100));
				stack.push(ch);
				System.out.println("Produced:" + ch);
				try {
					Thread.sleep(new Random().nextInt(10)*1000);
				} catch (InterruptedException e) {

				}
			}
		}
	}
}
