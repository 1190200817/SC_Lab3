package Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ����Ű��ļ��Ľ�����
 * @author ��С��
 *
 */
public class Parser {
	
	/**
	 * ���ֵ����ַ����Ƿ�Ϸ�
	 * @param s ֵ���ת������ַ���
	 * @return �Ϸ��򷵻�true�����򷵻�false
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
	 * ��ֵ����ַ����г�ȡԱ����Ϣ
	 * @param s ֵ����ַ���
	 * @return Ա����Ϣ���б�
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
	 * ��ֵ����ַ����г�ȡperiod��Ϣ
	 * @param s ֵ����ַ���
	 * @return period��Ϣ
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
	 * ��ֵ����ַ����г�ȡ�Ű���Ϣ
	 * @param s ֵ����ַ���
	 * @return �Ű���Ϣ���б�
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
