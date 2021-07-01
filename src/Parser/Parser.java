package Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 针对排班文件的解析器
 * @author 刘小川
 *
 */
public class Parser {
	
	/**
	 * 检查值班表字符串是否合法
	 * @param s 值班表转换后的字符串
	 * @return 合法则返回true，否则返回false
	 */
	public boolean checkValid(String s) {
		String employeeString = "Employee\\{\n(([a-zA-z]+)\\{((Manger)|(Secretary)|(Professor)|(Lecturer)|(AssciateProfessor)|(AssociateDean)),1\\d{2}-\\d{4}-\\d{4}\\}\n)+\\}";
		String periodString = "Period\\{\\d{4}-\\d{2}-\\d{2},\\d{4}-\\d{2}-\\d{2}\\}";
		String rosterString = "Roster\\{\n([a-zA-Z]+\\{\\d{4}-\\d{2}-\\d{2},\\d{4}-\\d{2}-\\d{2}\\}\n)+\\}";
		String patternString = "(" + employeeString + "\n" + periodString + "\n" + rosterString + "\n)" + "|" +
							   "(" + employeeString + "\n" + rosterString + "\n" + periodString + "\n)" + "|" + 
							   "(" + periodString + "\n" + employeeString + "\n" + rosterString + "\n)" + "|" + 
							   "(" + periodString + "\n" + rosterString + "\n" + employeeString + "\n)" + "|" +
							   "(" + rosterString + "\n" + periodString + "\n" + employeeString + "\n)" + "|" +
							   "(" + employeeString + "\n" + periodString + "\n" + rosterString + "\n)" + "|" + 
							   "(" + employeeString + "\n" + rosterString + "\n" + periodString + "\n)\n";
		Pattern pattern = Pattern.compile(patternString);
		return pattern.matcher(s).matches();
	}
	
	/**
	 * 从值班表字符串中抽取员工信息
	 * @param s 值班表字符串
	 * @return 员工信息的列表
	 */
	public List<String> parseEmployee(String s) {
		List<String> res = new ArrayList<String>();
		String employeeString = "Employee\\{\n(([a-zA-z]+)\\{((Manger)|(Secretary)|(Professor)|(Lecturer)|(AssciateProfessor)|(AssociateDean)),1\\d{2}-\\d{4}-\\d{4}\\}\n)+\\}";
		Pattern pattern = Pattern.compile(employeeString);
		Matcher matcher = pattern.matcher(s);
		if(matcher.find()) {
			String eInfoString = matcher.group();
			String ePattern = "([a-zA-z]+)\\{((Manger)|(Secretary)|(Professor)|(Lecturer)|(AssciateProfessor)|(AssociateDean)),1\\d{2}-\\d{4}-\\d{4}\\}\n";
			Pattern pattern2 = Pattern.compile(ePattern);
			Matcher matcher2 = pattern2.matcher(eInfoString);
			while(matcher2.find()) {
				res.add(matcher2.group());
			}
		}
		return res;
	}
	
	/**
	 * 从值班表字符串中抽取period信息
	 * @param s 值班表字符串
	 * @return period信息
	 */
	public String parsePeriod(String s) {
		String periodString = "Period\\{\\d{4}-\\d{2}-\\d{2},\\d{4}-\\d{2}-\\d{2}\\}";
		Pattern pattern = Pattern.compile(periodString);
		Matcher matcher = pattern.matcher(s);
		if(matcher.find()) {
			return matcher.group();
		}
		return "";
	}
	
	/**
	 * 从值班表字符串中抽取排班信息
	 * @param s 值班表字符串
	 * @return 排班信息的列表
	 */
	public List<String> parseRoster(String s) {
		List<String> res = new ArrayList<String>();
		String rosterString = "Roster\\{\n([a-zA-Z]+\\{\\d{4}-\\d{2}-\\d{2},\\d{4}-\\d{2}-\\d{2}\\}\n)+\\}";
		Pattern pattern = Pattern.compile(rosterString);
		Matcher matcher = pattern.matcher(s);
		if(matcher.find()) {
			String eInfoString = matcher.group();
			String ePattern = "[a-zA-Z]+\\{\\d{4}-\\d{2}-\\d{2},\\d{4}-\\d{2}-\\d{2}\\}\n";
			Pattern pattern2 = Pattern.compile(ePattern);
			Matcher matcher2 = pattern2.matcher(eInfoString);
			while(matcher2.find()) {
				res.add(matcher2.group());
			}
		}
		return res;
	}
}
