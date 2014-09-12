package org.apache.commons.lang3.builder;

import java.io.Serializable;

import org.junit.Test;


public class ToStringBuilderTest {

	@Test
	public void test(){
		Mo m=new Mo();
		m.setName("A");
		//org.apache.commons.lang3.builder.ToStringBuilderTest$Mo@16721bd[name=A]
		System.out.println(ToStringBuilder.reflectionToString(m, ToStringStyle.DEFAULT_STYLE));;
		//org.apache.commons.lang3.builder.ToStringBuilderTest$Mo@16721bd[
		//  name=A
		//]
		System.out.println(ToStringBuilder.reflectionToString(m, ToStringStyle.MULTI_LINE_STYLE));
		//org.apache.commons.lang3.builder.ToStringBuilderTest$Mo@16721bd[A]
		System.out.println(ToStringBuilder.reflectionToString(m, ToStringStyle.NO_FIELD_NAMES_STYLE));
		//ToStringBuilderTest.Mo[name=A]
		System.out.println(ToStringBuilder.reflectionToString(m, ToStringStyle.SHORT_PREFIX_STYLE));
		//A
		System.out.println(ToStringBuilder.reflectionToString(m, ToStringStyle.SIMPLE_STYLE));
		
		//ToStringBuilderTest.Mo[name=A,nameEx=<null>]
		System.out.println(ToStringBuilder.reflectionToString(m,  ToStringStyle.SHORT_PREFIX_STYLE, true));
		
		//org.apache.commons.lang3.builder.ToStringBuilderTest$Mo@b3c24f[A]
		System.out.println(new ToStringBuilder(m).append(m.getName()).toString());
		
		Mo n=new Mo();
		n.setName("A");
		
		System.out.println(HashCodeBuilder.reflectionHashCode(m, false));//694
		
		System.out.println("A".hashCode());//65
		
		//比较
		System.out.println(EqualsBuilder.reflectionEquals(m, n, false));
		
		new EqualsBuilder().append("a", "b");
		
	}

	public static class Mo implements Serializable,Cloneable{
		private String name;
		
		private transient String nameEx;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public String getNameEx() {
			return nameEx;
		}

		public void setNameEx(String nameEx) {
			this.nameEx = nameEx;
		}

		@Override
		public String toString() {
			new ToStringBuilder(this).append(name).toString();
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
		}
		
		@Override
		public boolean equals(Object obj) {
			Mo m=(Mo)obj;
			new EqualsBuilder().appendSuper(super.equals(obj))
			.append(name, m.getName()).isEquals();
			return EqualsBuilder.reflectionEquals(this, obj,false);
		}
		
		@Override
		public int hashCode() {
			new HashCodeBuilder().appendSuper(super.hashCode()).append(name).hashCode();
			//不包括transient属性
			return HashCodeBuilder.reflectionHashCode(this,false);
		}
		
	}
}
