package org.covito.kit.test.normal;

/**
 * 猴子吃桃问题
 * 猴子第一天摘下若干个桃子，当即吃了一半，还不过瘾，又多吃了一个。
 * 第二天早上又将剩下的桃子吃掉一半，又多吃一个。
 * 以后每天早上都吃了前一天剩下的一半零一个。
 * 到第10天早上想再吃时，就只剩一个桃子了。
 * 求第一天共摘多少个桃子。
 */
public class MonkeyPeach {

	public static void main(String[] args) {
		System.out.println(normal());
		System.out.println(recursion(1));
	}
	
	/**
	 * 普通算法
	 * @return
	 */
	static int normal(){
		int count = 1;
		for (int i = 0; i < 9; i++) {
			count = (count + 1) * 2;
		}
		return count;
	}

	/**
	 * 递归
	 * @param day
	 * @return
	 */
	static int recursion(int day) {
		if (day >= 10) {
			return 1;
		} else {
			return (recursion(day + 1) + 1) * 2;
		}
	}
}
