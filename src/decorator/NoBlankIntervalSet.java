package decorator;

import java.util.Set;

import Assist.time;

/**
 * ���������ʱ��հ׵�IntervalSet
 * ���ӷ����ж���ʼʱ�������ʱ��֮���Ƿ��пհ�
 *
 * @author ��С��
 *
 * @param <L>
 */
public interface NoBlankIntervalSet<L> {
	
	/**
	 * ���شӿ�ʼʱ�䵽����ʱ������пհ�ʱ��μ���
	 * @return �հ�ʱ��μ���
	 */
	public Set<time> blankIntervals();
	
	/**
	 * ��ÿ�ʼʱ��
	 * @return ��ʼʱ��
	 */
	public long getStartTime();

	/**
	 * ��ý���ʱ��
	 * @return ����ʱ��
	 */
	public long getEndTime();
}
