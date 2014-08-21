package org.covito.kit.utility.bean.castor;

import java.util.ArrayList;
import java.util.List;

import org.covito.kit.utility.bean.CastorNotFoundException;


public class CastorService {
	
	private List<ICastor> castors=new ArrayList<ICastor>();
	
	private static CastorService instance;
	
	/** 
	 * <默认构造函数>
	 */
	public CastorService() {
		init();
	}

	/** 
	 * <获取所有转换器><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @return
	 */
	public List<ICastor> getAll(){
		return castors;
	}

	/** 
	 * <注册转换器><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param item
	 */
	public void register(ICastor item) {
		castors.add(item);
	}
	
	/** 
	 * <单例><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @return
	 */
	public static CastorService getInstance() {
		if(instance==null){
			instance=new CastorService();
		}
		return instance;
	}

	private void init() {
		register(IntCastor.getInstance());
		register(LongCastor.getInstance());
		register(FloatCastor.getInstance());
		register(DoubleCastor.getInstance());
		register(BooleanCastor.getInstance());
		register(BigDecimalCastor.getInstance());
		register(TimeCastor.getInstance());
		register(DateCastor.getInstance());
		register(SqlDateCastor.getInstance());
		register(TimestampCastor.getInstance());
		
		register(StringArrayCastor.getInstance());
		register(IntArrayCastor.getInstance());
		register(LongArrayCastor.getInstance());
		register(FloatArrayCastor.getInstance());
		register(DoubleArrayCastor.getInstance());
		register(BooleanArrayCastor.getInstance());
		register(GenricArrayCastor.getInstance());
	}
	
	public static Object toType(Object obj, Class<?> type) {
		if ((obj == null) || (type.isInstance(obj))) {
			return obj;
		}
		if (type == String.class) {
			return obj.toString();
		}
		for (ICastor castor : getInstance().getAll()) {
			if (castor.canCast(type)) {
				return castor.cast(obj, type);
			}
		}
		throw new CastorNotFoundException(type.getClass().getName());
	}
	
}
