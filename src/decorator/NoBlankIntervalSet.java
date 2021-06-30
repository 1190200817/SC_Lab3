package decorator;

import java.util.Set;

import Assist.time;

/**
 * 不允许存在时间空白的IntervalSet
 * 增加方法判断起始时间与结束时间之间是否有空白
 *
 * @author 刘小川
 *
 * @param <L>
 */
public interface NoBlankIntervalSet<L> {
	
	/**
	 * 返回从开始时间到结束时间的所有空白时间段集合
	 * @return 空白时间段集合
	 */
	public Set<time> blankIntervals();
	
	/**
	 * 获得开始时间
	 * @return 开始时间
	 */
	public long getStartTime();

	/**
	 * 获得结束时间
	 * @return 结束时间
	 */
	public long getEndTime();
}
