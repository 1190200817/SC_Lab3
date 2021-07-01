package exceptions;

/**
 * 自定义异常: 时间负数异常
 * @author 刘小川
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
