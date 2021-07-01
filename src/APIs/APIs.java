package APIs;

import java.util.HashSet;
import java.util.Set;

import Assist.time;
import IntervalSet.IntervalSet;
import MultiIntervalSet.MultiIntervalSet;

/**
 * API
 * ����ָ�����и�����5�������⣬��������������õĺ������ֱ��ǣ�
 * ����ʱ�伯�ϵ�ʱ����: public static double totalIntervalSpan(Set<time> set);
 * ����MultiIntervalSet��ʱ����: public static <L> double totalSpan(MultiIntervalSet<L> multiIntervalSet);
 * @author ��С��
 *
 */
public abstract class APIs {
	
	/**
	 * ����MultiIntervalSet���ƶ�
	 * @param s1 ��һ��MultiIntervalSet
	 * @param s2 �ڶ���MultiIntervalSet
	 * @return s1��s2�����ƶ�
	 */
	public static <L> double Similarity(MultiIntervalSet<L> s1, MultiIntervalSet<L> s2) {
		long minTime = Math.min(getMinTime(s1), getMinTime(s2));
		long maxTime = Math.max(getMaxTime(s1), getMaxTime(s2));
		if(minTime == maxTime)
			return 1.0;
		
		double ans = 0.0;
		for(L label:s1.labels()) {
			if(s2.labels().contains(label)) {
				ans += totalIntervalSpan(intervalSetSimilarity(s1.intervals(label), s2.intervals(label)));
			}
		}
		
		ans /= (double)(maxTime - minTime);
		return ans;
	}
	
	/**
	 * ����IntervalSet�ĳ�ͻ����
	 * @param set һ��IntervalSet
	 * @return ���IntervalSet�ĳ�ͻ����
	 */
	public static <L> double calcConflictRatio(IntervalSet<L> set) {
		return calcConflictRatio(MultiIntervalSet.init(set));
	}
	
	

	/**
	 * ����MultiIntervalSet�ĳ�ͻ����
	 * @param set һ��MultiIntervalSet
	 * @return ���MultiIntervalSet�ĳ�ͻ����
	 */
	public static <L> double calcConflictRatio(MultiIntervalSet<L> set) {
		Set<time> conflictTime = new HashSet<>();
		if(totalSpan(set) == 0)
			return 0.0;
		for(L label1:set.labels()) {
			for(L label2:set.labels()) {
				if(label1.equals(label2)) 
					continue;
				Set<time> originConflictTime = intervalSetSimilarity(set.intervals(label1), set.intervals(label2));
				for(time conflict:conflictTime) {
					Set<time> octCopy = new HashSet<>(originConflictTime);
					for(time t:octCopy) {
						if(conflict.getStart() >= t.getEnd() || t.getStart() >= conflict.getEnd())
							continue;
						else {
							originConflictTime.remove(t);
							if(t.getStart() < conflict.getStart())
								originConflictTime.add(new time(t.getStart(), conflict.getStart()));
							if(t.getEnd() > conflict.getEnd())
								originConflictTime.add(new time(conflict.getEnd(), t.getEnd()));
						}
					}
				}
				conflictTime.addAll(originConflictTime);
			}
		}
		return totalIntervalSpan(conflictTime)/totalSpan(set);
	}
	
	/**
	 * 	����IntervalSet���б���
	 * @param set һ��IntervalSet
	 * @return ���IntervalSet�Ŀ��б���
	 */
	public static <L> double calcFreeTimeRatio(IntervalSet<L> set) {
		return calcFreeTimeRatio(MultiIntervalSet.init(set));
	}
	
	/**
	 * 	����MultiIntervalSet���б���
	 * @param set һ��MultiIntervalSet
	 * @return ���MultiIntervalSet�Ŀ��б���
	 */
	public static <L> double calcFreeTimeRatio(MultiIntervalSet<L> set) {
		Set<time> freeTime = new HashSet<>();
		long minTime = getMinTime(set);
		long maxTime = getMaxTime(set);
		if(minTime == maxTime)
			return 1.0;
		freeTime.add(new time(minTime, maxTime));
		for(L label:set.labels()) {
			IntervalSet<Integer> intervalSet = set.intervals(label);
			for(Integer ilabel:intervalSet.labels()) {
				long start = intervalSet.start(ilabel);
				long end = intervalSet.end(ilabel);
				Set<time> ftCopy = new HashSet<>(freeTime);
				for(time t:ftCopy) {
					if(t.getStart() >= end || start >= t.getEnd())
						continue;
					else {
						freeTime.remove(t);
						if(t.getStart() < start)
							freeTime.add(new time(t.getStart(), start));
						if(t.getEnd() > end)
							freeTime.add(new time(end, t.getEnd()));
					}
				}
			}
		}
		return totalIntervalSpan(freeTime)/totalSpan(set);
	}
	
	/**
	 * ����ʱ��μ��ϵ�ʱ���ܺ�
	 * @param set ʱ��μ���
	 * @return ʱ���ܺ�
	 */
	public static double totalIntervalSpan(Set<time> set) {
		double res = 0.0;
		for(time t:set) {
			res += t.getEnd() - t.getStart();
		}
		return res;
	}

	/**
	 * ����MultiIntervalSet��ʱ����
	 * @param <L> 
	 * @param multiIntervalSet һ��MultiIntervalSet
	 * @return ʱ����
	 */
	public static <L> double totalSpan(MultiIntervalSet<L> multiIntervalSet) {
		return (double)(getMaxTime(multiIntervalSet) - getMinTime(multiIntervalSet));
	}
	
	private static Set<time> intervalSetSimilarity(IntervalSet<Integer> s1, IntervalSet<Integer> s2) {
		int size1 = s1.labels().size();
		int size2 = s2.labels().size();
		Set<time> res = new HashSet<>();
		for(int label1 = 0; label1 < size1; label1++) {
			int label2 = 0;
			long start = s1.start(label1);
			long end = s1.end(label1);
			while(label2 < size2 && s2.end(label2) <= start)
				label2 += 1;
			while(label2 < size2 && s2.end(label2) <= end) {
				res.add(new time(Math.max(start, s2.start(label2)), s2.end(label2)));
				label2 += 1;
			}
			if(label2 >= size2) 
				break;
			if(s2.start(label2) < end) {
				res.add(new time(Math.max(start, s2.start(label2)), end));
			}
		}
		return res;
	}
	
	private static <L> long getMaxTime(MultiIntervalSet<L> multiIntervalSet) {
		long maxTime = 0;
		
		for(L label:multiIntervalSet.labels()) {
			IntervalSet<Integer> intervals = multiIntervalSet.intervals(label);
			for(Integer ilabel:intervals.labels()) {
				long endTime = intervals.end(ilabel);
				if(endTime > maxTime) 
					maxTime = endTime;
			}
		}
		return maxTime;
	}
	
	private static <L> long getMinTime(MultiIntervalSet<L> multiIntervalSet) {
		long minTime = Long.MAX_VALUE;
		
		for(L label:multiIntervalSet.labels()) {
			IntervalSet<Integer> intervals = multiIntervalSet.intervals(label);
			for(Integer ilabel:intervals.labels()) {
				if(intervals.start(ilabel) < minTime) 
					minTime = intervals.start(ilabel);
			}
		}
		if(minTime == Long.MAX_VALUE)
			minTime = 0;
		return minTime;
	}
}
