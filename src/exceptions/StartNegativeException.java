package exceptions;

/**
 * �Զ����쳣: ʱ�为���쳣
 * @author ��С��
 *
 */
public class StartNegativeException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StartNegativeException() {
		super("insert error: start time must be nonnegative");
	}
}
