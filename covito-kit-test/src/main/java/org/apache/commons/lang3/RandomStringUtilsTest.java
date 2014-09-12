package org.apache.commons.lang3;

import static org.apache.commons.lang3.RandomStringUtils.*;

import org.junit.Test;

public class RandomStringUtilsTest {

	@Test
	public void test(){
		System.out.println(random(40));//룆㩡띂ꢬ堒㋶櫳󒱏⹀ܰ击柰釉𣑀簀輻ဢ䶴㹦澽踍荏浦랫緵㗦䍣ࢿꯓ毦떂⌨뿮ꮷ
		System.out.println(randomNumeric(40));//8115329776364020465325743885756411032426
		System.out.println(randomAscii(40));//y\mhZ)A)=sj:e.czn&3pPhRW2ZtKESr@7W&[%%:?
		System.out.println(randomAlphabetic(40));//ZJmJEDNwntXjaPjbotKQSBTVfpICNirqToUXkHAf
		System.out.println(randomAlphanumeric(40));//0DJ5mksXf56pRC3wFaQRAcImChpyUSHYRwzxakXw
		System.out.println(random(40, false, false));//ઌ嬊󝱌摘뛮͠薢锌刼ꃝ𮈫왞柱콫⃮䪮ﻺ䵀竴픳𑃩몢크麚ﰜ뿯ﾂ迭陿釡艞᠄ᅲ
		System.out.println(random(40, true, true));//WkkUKwda087eKoqreBZprrLgYBuilTjj1IVjlNNh
		System.out.println(random(40, "ABCDEFG"));//EBADBFEAAEFCDDDFCGDGDBEDEBAFGCGFGDFFBEFF
	}
}
