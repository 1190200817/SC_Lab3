package exceptions;

/**
 * �Զ����쳣: ʱ��γ�ͻ�쳣
 * @author ��С��
 *
 */
public class IntervalConflictException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Make a new interval conflict exception with the given detail message
	 * @param message the detail message
	 */
	public IntervalConflictException(String message) {
		super(message);
	}
}
