package APP;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


import APIs.APIs;
import Assist.Employee;
import Assist.Position;
import Assist.time;
import Parser.Parser;
import Specific.DutyIntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

/**
 * �Ű����ϵͳ
 * @author ��С��
 *
 */
public class DutyRosterApp {

	public static void printMenu() {
		System.out.println("�˵���");
		System.out.println("1:���Ա��");
		System.out.println("2:ɾ��Ա��");
		System.out.println("3:����Ű��¼");
		System.out.println("4:ɾ���Ű��¼");
		System.out.println("5:��ʾδ����ʱ���");
		System.out.println("6:�Զ�����");
		System.out.println("7:��ʾ�Ű��");
		System.out.println("8.���ļ��ж����Ű���Ϣ");
		System.out.println("9:�˳�");
	}
	
	public static Position positionTransfer(String s) {
		s = s.toLowerCase();
		if(s.equals("secretary")) {
			return Position.SECRETARY;
		}
		if(s.equals("lecturer")) {
			return Position.LECTURER;
		}
		if(s.equals("assciateprofessor")) {
			return Position.VICEPROFESSOR;
		}
		if(s.equals("associatedean")) {
			return Position.VICEDEAN;
		}
		if(s.equals("professor")) {
			return Position.PROFESSOR;
		}
		if(s.equals("manger")) {
			return Position.MANAGER;
		}
		return null;
	}
	
	public static long readTime(Scanner scanner) {
		LocalDate ld = null;
		String s = scanner.nextLine();
		while(true) {
			try {
				ld = LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				return ld.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
			} catch (Exception e) {
				System.out.println("���ڸ�ʽ�������������루��ʽyyyy-mm-dd��");
				s = scanner.nextLine();
			}
		}
	}
	
	public static Employee readEmployee(Scanner scanner) {		
		while(true) {
			String employeeInfo = scanner.nextLine();
			String[] parse = employeeInfo.split("-");
			
			if(parse.length != 3) {
				System.out.println("Ա����Ϣ��ʽ�������������룡");
				continue;
			}
			
			long phoneNumber = 0;
			try {
				phoneNumber = Long.valueOf(parse[2]);
			} catch (Exception e) {
				System.out.println("Ա����Ϣ��ʽ�������������룡");
				continue;
			}
			
			Position pos = positionTransfer(parse[1]);
			if(pos == null) {
				System.out.println("Ա����Ϣ��ʽ�������������룡");
				continue;
			}
			
			String name = parse[0];
			if(name.equals("")) {
				System.out.println("���ֲ���Ϊ�գ����������룡");
				continue;
			}
			
			Employee e = new Employee(name, pos, phoneNumber);
			return e;
		}		
	}
	
	public static String readFile(String fileName) throws IOException {
		String format = new String("gbk");
		String res = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),format));
		String myLine;
		while((myLine = br.readLine())!=null) {
			myLine = myLine.trim();
			myLine = myLine.replaceAll(" ", "");
			myLine = myLine.replaceAll("��", "");
			myLine = myLine.replaceAll("\r\n", "\n");
			if(myLine.length() != 0) {
				res += myLine + "\n";
			}
		}
		br.close();
		return res;
	}
	
	public static DutyIntervalSet<Employee> parseFile(Set<Employee> employeeSet, Scanner scanner) {
		Set<Employee> newEmployeeSet = new HashSet<Employee>();
		DutyIntervalSet<Employee> newDis;
		System.out.println("�������Ű��ļ�����ע���ļ���Ҫ����./src/txt/Ŀ¼�£�:");
		String fileName = scanner.nextLine();
		String originInfo = "";
		try {
			originInfo = readFile("./src/txt/" + fileName);
		} catch (IOException e4) {
			System.out.println("Error:���ļ������쳣����ȷ���ļ�����./src/txt/Ŀ¼�£�");
			return null;
		}
		Parser parser = new Parser();
		List<String> eInfo = parser.parseEmployee(originInfo);
		String pInfo = parser.parsePeriod(originInfo);
		List<String> rInfo = parser.parseRoster(originInfo);
		if(eInfo.size() == 0 || pInfo == "" || rInfo.size() == 0) {
			System.out.println("Error:�ļ���ʽ�������⣡");
			return null;
		}
		boolean eflag = true;
		for(String eString:eInfo) {
			String[] singles = eString.split("\\s|\\{|\\}|,");
			if(singles.length != 3) {
				System.out.println("Error:Ա����Ϣ��ʽ����");
				eflag = false;
				break;
			}
			String name = singles[0];
			if(name.equals("")) {
				System.out.println("Error:Ա�����ָ�ʽ����");
				eflag = false;
				break;
			}
			Position pos = positionTransfer(singles[1]);
			if(pos == null) {
				System.out.println("Error:Ա��ְλ��ʽ����");
				eflag = false;
				break;
			}
			long phoneNumber;
			try {
				phoneNumber = Long.valueOf(singles[2].replaceAll("-", ""));
			} catch (Exception e4) {
				System.out.println("Error:Ա���绰�����ʽ����");
				eflag = false;
				break;
			}
			for(Employee exist:newEmployeeSet) {
				if(exist.getName().equals(name)) {
					System.out.println("Error:Ա�������ظ���");
					eflag = false;
					break;
				}
			}
			if(!eflag)
				break;
			newEmployeeSet.add(new Employee(name, pos, phoneNumber));
		}
		if(!eflag) {
			return null;
		}
		String sstring = pInfo.substring(7, 17);
		String estring = pInfo.substring(18,28);
		long stime;
		long etime;
		try {
			LocalDate ldt = LocalDate.parse(sstring, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			stime = ldt.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
			ldt = LocalDate.parse(estring, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			ldt = ldt.plusDays(1);
			etime = ldt.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		} catch (Exception ex) {
			System.out.println("Error:Period���ڴ���");
			return null;
		}
		if(stime >= etime) {
			System.out.println("Error:��ʼ������ҪС�ڽ������ڣ�");
			return null;
		}
		newDis = new DutyIntervalSet<Employee>(stime, etime);
		eflag = true;
		for(String rString:rInfo) {
			String[] singles = rString.split("\\s|\\{|,|\\}");
			Employee eAs = null;
			if(singles.length != 3) {
				System.out.println("Error:�Ű���Ϣ��ʽ����");
				eflag = false;
				break;
			}
			String name = singles[0];
			if(name.equals("")) {
				System.out.println("Error:�Ű�Ա������ʽ����");
				eflag = false;
				break;
			}
			boolean tflag = false;
			for(Employee em:newEmployeeSet) {
				if(em.getName().equals(name)) {
					tflag = true;
					eAs = em;
					break;
				}
					
			}
			if(!tflag) {
				System.out.println("Error:�Ű�Ա��δ���壡");
				eflag = false;
				break;
			}
			long st;
			long et;
			try {
				LocalDate ldt = LocalDate.parse(singles[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				st = ldt.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
				ldt = LocalDate.parse(singles[2], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				ldt = ldt.plusDays(1);
				et = ldt.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
			} catch (Exception ex) {
				System.out.println("Error:�Ű����ڸ�ʽ����");
				eflag = false;
				break;
			}
			if(st >= et) {
				System.out.println("Error:�Ű࿪ʼ������ҪС�ڽ������ڣ�");
				eflag = false;
				break;
			}
			try {
				newDis.insert(st, et, eAs);
			} catch (IntervalConflictException | StartNegativeException e4) {
				System.out.println("Error:����Overlap!");
				eflag = false;
				break;
			}
		}
		if(!eflag) {
			return null;
		}
		employeeSet.clear();
		employeeSet.addAll(newEmployeeSet);
		System.out.println("�ļ�������Ϣ�ɹ���");
		return newDis;
	}
	
	public static void main(String[] args) {
		System.out.println("��ӭʹ���Ű����ϵͳ��");
		System.out.println("(��ע�⣺Ա��ְ��ֻ�ܴ�manger,secretary,lecturer,professor,assciateprofessor,associatedean��ѡ�񣬵����迼�Ǵ�Сд��)");
		System.out.println("�Ƿ���ļ���ʼ����Ϣ��������y�����ļ���ʼ������������������ֶ���ʼ����");
		Scanner scanner = new Scanner(System.in);
		String choice = scanner.nextLine();
		Set<Employee> employeeSet = new HashSet<Employee>();
		DutyIntervalSet<Employee> dis;
		long startTime;
		long endTime;
		if(choice.equals("y")) {
			dis = new DutyIntervalSet<Employee>(0, 1);
			while((dis = parseFile(employeeSet,scanner)) == null)
				continue;
			startTime = dis.getStartTime();
			endTime = dis.getEndTime();
		}
		else {
			System.out.println("���ֶ���ʼ����Ϣ��");
			while(true) {
				System.out.println("���趨�Ű࿪ʼ���ڣ���ʽyyyy-mm-dd����");
				startTime = readTime(scanner);
				System.out.println("���趨�Ű�������ڣ���ʽyyyy-mm-dd����");
				endTime = readTime(scanner);
				if(startTime >= endTime) {
					System.out.println("��ʼʱ����ҪС�ڽ���ʱ�䣡���������ã�");
				}
				else {
					dis = new DutyIntervalSet<Employee>(startTime, endTime);
					break;
				}
			}
			while(true) {
				System.out.println("������Ա����Ϣ����ʽ������-ְ��-�ֻ����룩��");
				Employee e = readEmployee(scanner);
				employeeSet.add(e);
				System.out.println("��ӳɹ���" + e.toString());
				System.out.println("�Ƿ�������Ա����Ϣ��������nֹͣ�����������������");
				String c = scanner.nextLine();
				if(c.equals("n"))
					break;
			}
		}
		
		printMenu();
		boolean flag = true;
		while(flag) {
			System.out.println("������Ҫִ�еĲ�����ţ�");
			String operation = scanner.nextLine();
			switch(operation) {
				case "1":
					System.out.println("������Ա����Ϣ����ʽ������-ְ��-�ֻ����룩��");
					Employee e = readEmployee(scanner);
					employeeSet.add(e);
					System.out.println("��ӳɹ���" + e.toString());
					break;
				case "2":
					System.out.println("������Ա����Ϣ����ʽ������-ְ��-�ֻ����룩��");
					Employee e1 = readEmployee(scanner);
					if(dis.labels().contains(e1)) {
						System.out.println("�޷�ɾ������Ա���Ű���Ϣδɾ����");
					}
					else if(!(employeeSet.contains(e1))) {
						System.out.println("Ա�������ڣ�");
					}
					else {
						employeeSet.remove(e1);
						System.out.println("ɾ���ɹ���" + e1.toString());
					}
					break;
				case "3":
					System.out.println("������Ա����Ϣ����ʽ������-ְ��-�ֻ����룩��");
					Employee e2 = readEmployee(scanner);
					if(!(employeeSet.contains(e2))) {
						System.out.println("Ա�������ڣ�������ӣ�");
						break;
					}
					while(true) {
						System.out.println("���趨�Ű࿪ʼ���ڣ���ʽyyyy-mm-dd����");
						long start = readTime(scanner);
						System.out.println("���趨�Ű�������ڣ���ʽyyyy-mm-dd����");
						long end = readTime(scanner);
						if(start >= end) {
							System.out.println("��ʼʱ����ҪС�ڽ���ʱ�䣡���������룡");
						}
						else if(start < startTime || end > endTime) {
							System.out.println("ʱ�䳬����Χ�����������룡");
						}
						else {
							try {
								dis.insert(start, end, e2);
								System.out.println("���ųɹ���");
							} catch (IntervalConflictException | StartNegativeException e3) {
								System.out.println("���Ŵ���Overlap��");
							}
							break;
						}
					}
					break;
				case "4":
					System.out.println("������Ա����Ϣ����ʽ������-ְ��-�ֻ����룩��");
					Employee e3 = readEmployee(scanner);
					if(!(employeeSet.contains(e3))) {
						System.out.println("Ա�������ڣ�");
						break;
					}
					if(dis.remove(e3))
						System.out.println("�Ű��¼ɾ���ɹ���");
					else {
						System.out.println("��Ա�����Ű��¼��");
					}
					break;
				case "5":
					Set<time> blankSet = dis.blankIntervals();
					if(blankSet.size() == 0) {
						System.out.println("��ǰ�Ű�������");
					}
					else {
						System.out.println("δ����ʱ���:");
						Set<time> sortedBlank = new TreeSet<>();
						sortedBlank.addAll(blankSet);
						int count = 1;
						for(time t:sortedBlank) {
							LocalDate sld = LocalDateTime.ofInstant(Instant.ofEpochMilli(t.getStart()), ZoneId.systemDefault()).toLocalDate();
							LocalDate eld = LocalDateTime.ofInstant(Instant.ofEpochMilli(t.getEnd()), ZoneId.systemDefault()).toLocalDate();
							System.out.println(count + ": " + sld.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " �� " + eld.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
							count += 1;
						}
						System.out.println("δ����ʱ��α�����" + APIs.totalIntervalSpan(sortedBlank)/(double)(endTime - startTime));
					}
					break;
				case "6":
					if(dis.blankIntervals().size() == 0) {
						System.out.println("��ǰ�Ű������������Զ����ţ�");
					}
					else if(dis.labels().size() == employeeSet.size()) {
						System.out.println("�Ű�δ����������Ա���������ţ������Ҫ�Զ�����ʹ���Ű��Ϊ���������Ա����ɾ���Ű���Ϣ��");
					}
					else {
						Set<time> blankSet1 = dis.blankIntervals();
						List<time> blankList = new ArrayList<>(blankSet1);
						List<Employee> freeEmployee = new ArrayList<Employee>();
						for(Employee e4:employeeSet) {
							if(!(dis.labels().contains(e4)))
								freeEmployee.add(e4);
						}
						if(blankSet1.size() > freeEmployee.size()) {
							int count = 0;
							for(Employee free:freeEmployee) {
								time t = blankList.get(count);
								long start = t.getStart();
								long end = t.getEnd();
								try {
									dis.insert(start, end, free);
								} catch (IntervalConflictException | StartNegativeException e5) {
								}
								count += 1;
							}
							System.out.println("�Զ�������ɣ��Ű�δ����������Ա���������ţ������Ҫʹ���Ű��Ϊ���������Ա����ɾ���Ű���Ϣ��");
						}
						else {
							int count = 0;
							for(time t:blankList) {
								long start = t.getStart();
								long end = t.getEnd();
								try {
									dis.insert(start, end, freeEmployee.get(count));
								} catch (IntervalConflictException | StartNegativeException e5) {
								}
								count += 1;
							}
							System.out.println("�Զ�������ɣ��Ű�������");
						}
					}
					break;
				case "7":
					System.out.println("�Ű����Ϣ��");
					System.out.println("���\tʱ���\t\t\t\tԱ��");
					Map<time, Employee> reverseMap = new TreeMap<time, Employee>();
					for(Employee e6:dis.labels()) {
						time t = new time(dis.start(e6), dis.end(e6));
						reverseMap.put(t, e6);
					}
					for(time t:dis.blankIntervals()) {
						reverseMap.put(t, null);
					}
					int count = 1;
					for(time t:reverseMap.keySet()) {
						Employee e7 = reverseMap.get(t);
						LocalDate sld = LocalDateTime.ofInstant(Instant.ofEpochMilli(t.getStart()), ZoneId.systemDefault()).toLocalDate();
						LocalDate eld = LocalDateTime.ofInstant(Instant.ofEpochMilli(t.getEnd()), ZoneId.systemDefault()).toLocalDate();
						if(e7 == null)
							System.out.println(count + "\t" + sld + " �� " + eld + "\t\tδ����");
						else
							System.out.println(count + "\t" + sld + " �� " + eld + "\t\t" + e7.toString());
						count += 1;
					}
					break;
				case "8":
					DutyIntervalSet<Employee> tdis = parseFile(employeeSet, scanner);
					if(tdis != null) {
						dis = tdis;
						startTime = dis.getStartTime();
						endTime = dis.getEndTime();
					}
					break;
				case "9":
					flag = false;
					System.out.println("�����˳��Ű����ϵͳ����ӭ�´�ʹ�ã�");
					break;
				default: 
					System.out.println("��Ŵ���������1-8֮��ı�ţ�");
					break;	
			}
		}	
	}
	
}
