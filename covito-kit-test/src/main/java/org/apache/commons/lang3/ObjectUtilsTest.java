package org.apache.commons.lang3;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import static org.apache.commons.lang3.ObjectUtils.*;
import org.junit.Test;

public class ObjectUtilsTest {

	@Test
	public void test(){
		Mo m=new Mo();
		m.setName("A");
		Mo n=ObjectUtils.clone(m);
		m.setName("B");
		System.out.println(m);//B
		System.out.println(n);//null
		
		//当clone返回null，返回参数
		n=cloneIfPossible(m);
		System.out.println(n);//B
		
		n=null;
		n=defaultIfNull(n, new Mo());
		System.out.println(n);//<null>
		
		System.out.println(firstNonNull(new Object[]{null,"","a"}));;//""
		
		System.out.println(hashCodeMulti(new Object[]{null,1,"3"}));
		
		System.out.println(identityToString(""));//java.lang.String@5388b5
		
		System.out.println(max("a","b","c"));//c
		System.out.println(min("a","b","c"));//a
		
		//取中间数
		System.out.println(median("a","b","c"));//b
		System.out.println(median("a","b","c","d"));//b
		
		//最频繁出现的项。
		System.out.println(mode("a","b","c","c"));//c
		
		
	}
	
	public static class Mo {
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
		}
	}
}
