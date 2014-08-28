package org.covito.kit.test.normal;

/**
 * 使用数组实现约瑟夫环问题 由m个人围成一个首尾相连的圈报数。 从第一个人开始，从1开始报数，报到n的人出圈，
 * 剩下的人继续从1开始报数，直到最后一个。 对于给定的m和n，求出所有人的出圈顺序和最后一个数。
 */
public class JosephCircle {

	public static void main(String[] args) {
		int m = 50;
		int n = 3;
		int[] group = new int[m];
		for (int i = 0; i < 50; i++) {
			group[i] = i + 1;
		}
		for (int i = 0, j = 0, leave = m; leave > 1; i++) {// i为点名数，死人也点，j为活人报数，leave为活着人数
			int index = i % 50;// 位置编号
			if (group[index] > 0) {// 当为活人时（当人死之后将置为-1）
				j++; // 没死就要报数（从1开始）
				if (j % n == 0) {// 报的数为3的倍数
					System.out.print(index + " ");
					group[index] = -1;// 将这个位置的人杀死
					leave--;// 将剩下的人数减去1
				}
			}
		}
		System.out.println();
		for (int i : group) {
			if (i > 0) {
				System.out.println("最后的幸运者编号为：" + i);
			}
		}
	}

}
