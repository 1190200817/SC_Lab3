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
 * 排班管理系统
 * @author 刘小川
 *
 */
public class DutyRosterApp {

	public static void printMenu() {
		System.out.println("菜单：");
		System.out.println("1:添加员工");
		System.out.println("2:删除员工");
		System.out.println("3:添加排班记录");
		System.out.println("4:删除排班记录");
		System.out.println("5:显示未安排时间段");
		System.out.println("6:自动编排");
		System.out.println("7:显示排班表");
		System.out.println("8.从文件中读入排班信息");
		System.out.println("9:退出");
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
				System.out.println("日期格式错误！请重新输入（格式yyyy-mm-dd）");
				s = scanner.nextLine();
			}
		}
	}
	
	public static Employee readEmployee(Scanner scanner) {		
		while(true) {
			String employeeInfo = scanner.nextLine();
			String[] parse = employeeInfo.split("-");
			
			if(parse.length != 3) {
				System.out.println("员工信息格式错误！请重新输入！");
				continue;
			}
			
			long phoneNumber = 0;
			try {
				phoneNumber = Long.valueOf(parse[2]);
			} catch (Exception e) {
				System.out.println("员工信息格式错误！请重新输入！");
				continue;
			}
			
			Position pos = positionTransfer(parse[1]);
			if(pos == null) {
				System.out.println("员工信息格式错误！请重新输入！");
				continue;
			}
			
			String name = parse[0];
			if(name.equals("")) {
				System.out.println("名字不能为空！请重新输入！");
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
			myLine = myLine.replaceAll("　", "");
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
		System.out.println("请输入排班文件名（注意文件需要放在./src/txt/目录下）:");
		String fileName = scanner.nextLine();
		String originInfo = "";
		try {
			originInfo = readFile("./src/txt/" + fileName);
		} catch (IOException e4) {
			System.out.println("Error:读文件出现异常！请确保文件放在./src/txt/目录下！");
			return null;
		}
		Parser parser = new Parser();
		List<String> eInfo = parser.parseEmployee(originInfo);
		String pInfo = parser.parsePeriod(originInfo);
		List<String> rInfo = parser.parseRoster(originInfo);
		if(eInfo.size() == 0 || pInfo == "" || rInfo.size() == 0) {
			System.out.println("Error:文件格式存在问题！");
			return null;
		}
		boolean eflag = true;
		for(String eString:eInfo) {
			String[] singles = eString.split("\\s|\\{|\\}|,");
			if(singles.length != 3) {
				System.out.println("Error:员工信息格式错误！");
				eflag = false;
				break;
			}
			String name = singles[0];
			if(name.equals("")) {
				System.out.println("Error:员工名字格式错误！");
				eflag = false;
				break;
			}
			Position pos = positionTransfer(singles[1]);
			if(pos == null) {
				System.out.println("Error:员工职位格式错误！");
				eflag = false;
				break;
			}
			long phoneNumber;
			try {
				phoneNumber = Long.valueOf(singles[2].replaceAll("-", ""));
			} catch (Exception e4) {
				System.out.println("Error:员工电话号码格式错误！");
				eflag = false;
				break;
			}
			for(Employee exist:newEmployeeSet) {
				if(exist.getName().equals(name)) {
					System.out.println("Error:员工名字重复！");
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
			System.out.println("Error:Period日期错误！");
			return null;
		}
		if(stime >= etime) {
			System.out.println("Error:开始日期需要小于结束日期！");
			return null;
		}
		newDis = new DutyIntervalSet<Employee>(stime, etime);
		eflag = true;
		for(String rString:rInfo) {
			String[] singles = rString.split("\\s|\\{|,|\\}");
			Employee eAs = null;
			if(singles.length != 3) {
				System.out.println("Error:排班信息格式错误！");
				eflag = false;
				break;
			}
			String name = singles[0];
			if(name.equals("")) {
				System.out.println("Error:排班员工名格式错误！");
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
				System.out.println("Error:排班员工未定义！");
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
				System.out.println("Error:排班日期格式错误！");
				eflag = false;
				break;
			}
			if(st >= et) {
				System.out.println("Error:排班开始日期需要小于结束日期！");
				eflag = false;
				break;
			}
			try {
				newDis.insert(st, et, eAs);
			} catch (IntervalConflictException | StartNegativeException e4) {
				System.out.println("Error:编排Overlap!");
				eflag = false;
				break;
			}
		}
		if(!eflag) {
			return null;
		}
		employeeSet.clear();
		employeeSet.addAll(newEmployeeSet);
		System.out.println("文件读入信息成功！");
		return newDis;
	}
	
	public static void main(String[] args) {
		System.out.println("欢迎使用排班管理系统！");
		System.out.println("(请注意：员工职务只能从manger,secretary,lecturer,professor,assciateprofessor,associatedean中选择，但无需考虑大小写！)");
		System.out.println("是否从文件初始化信息？（输入y进行文件初始化，其他任意键进入手动初始化）");
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
			System.out.println("请手动初始化信息！");
			while(true) {
				System.out.println("请设定排班开始日期（格式yyyy-mm-dd）：");
				startTime = readTime(scanner);
				System.out.println("请设定排班结束日期（格式yyyy-mm-dd）：");
				endTime = readTime(scanner);
				if(startTime >= endTime) {
					System.out.println("开始时间需要小于结束时间！请重新设置！");
				}
				else {
					dis = new DutyIntervalSet<Employee>(startTime, endTime);
					break;
				}
			}
			while(true) {
				System.out.println("请设置员工信息（格式：名字-职务-手机号码）：");
				Employee e = readEmployee(scanner);
				employeeSet.add(e);
				System.out.println("添加成功！" + e.toString());
				System.out.println("是否继续添加员工信息？（输入n停止，其他任意键继续）");
				String c = scanner.nextLine();
				if(c.equals("n"))
					break;
			}
		}
		
		printMenu();
		boolean flag = true;
		while(flag) {
			System.out.println("请输入要执行的操作编号：");
			String operation = scanner.nextLine();
			switch(operation) {
				case "1":
					System.out.println("请输入员工信息（格式：名字-职务-手机号码）：");
					Employee e = readEmployee(scanner);
					employeeSet.add(e);
					System.out.println("添加成功！" + e.toString());
					break;
				case "2":
					System.out.println("请输入员工信息（格式：名字-职务-手机号码）：");
					Employee e1 = readEmployee(scanner);
					if(dis.labels().contains(e1)) {
						System.out.println("无法删除！该员工排班信息未删除！");
					}
					else if(!(employeeSet.contains(e1))) {
						System.out.println("员工不存在！");
					}
					else {
						employeeSet.remove(e1);
						System.out.println("删除成功！" + e1.toString());
					}
					break;
				case "3":
					System.out.println("请输入员工信息（格式：名字-职务-手机号码）：");
					Employee e2 = readEmployee(scanner);
					if(!(employeeSet.contains(e2))) {
						System.out.println("员工不存在！请先添加！");
						break;
					}
					while(true) {
						System.out.println("请设定排班开始日期（格式yyyy-mm-dd）：");
						long start = readTime(scanner);
						System.out.println("请设定排班结束日期（格式yyyy-mm-dd）：");
						long end = readTime(scanner);
						if(start >= end) {
							System.out.println("开始时间需要小于结束时间！请重新输入！");
						}
						else if(start < startTime || end > endTime) {
							System.out.println("时间超出范围！请重新输入！");
						}
						else {
							try {
								dis.insert(start, end, e2);
								System.out.println("编排成功！");
							} catch (IntervalConflictException | StartNegativeException e3) {
								System.out.println("编排错误Overlap！");
							}
							break;
						}
					}
					break;
				case "4":
					System.out.println("请输入员工信息（格式：名字-职务-手机号码）：");
					Employee e3 = readEmployee(scanner);
					if(!(employeeSet.contains(e3))) {
						System.out.println("员工不存在！");
						break;
					}
					if(dis.remove(e3))
						System.out.println("排班记录删除成功！");
					else {
						System.out.println("该员工无排班记录！");
					}
					break;
				case "5":
					Set<time> blankSet = dis.blankIntervals();
					if(blankSet.size() == 0) {
						System.out.println("当前排班已满！");
					}
					else {
						System.out.println("未安排时间段:");
						Set<time> sortedBlank = new TreeSet<>();
						sortedBlank.addAll(blankSet);
						int count = 1;
						for(time t:sortedBlank) {
							LocalDate sld = LocalDateTime.ofInstant(Instant.ofEpochMilli(t.getStart()), ZoneId.systemDefault()).toLocalDate();
							LocalDate eld = LocalDateTime.ofInstant(Instant.ofEpochMilli(t.getEnd()), ZoneId.systemDefault()).toLocalDate();
							System.out.println(count + ": " + sld.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 至 " + eld.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
							count += 1;
						}
						System.out.println("未安排时间段比例：" + APIs.totalIntervalSpan(sortedBlank)/(double)(endTime - startTime));
					}
					break;
				case "6":
					if(dis.blankIntervals().size() == 0) {
						System.out.println("当前排班已满，无需自动编排！");
					}
					else if(dis.labels().size() == employeeSet.size()) {
						System.out.println("排班未满，但所有员工均被安排，如果需要自动安排使得排班表为满，请添加员工或删除排班信息！");
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
							System.out.println("自动编排完成，排班未满，但所有员工均被安排，如果需要使得排班表为满，请添加员工或删除排班信息！");
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
							System.out.println("自动编排完成，排班已满！");
						}
					}
					break;
				case "7":
					System.out.println("排班表信息：");
					System.out.println("序号\t时间段\t\t\t\t员工");
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
							System.out.println(count + "\t" + sld + " 至 " + eld + "\t\t未安排");
						else
							System.out.println(count + "\t" + sld + " 至 " + eld + "\t\t" + e7.toString());
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
					System.out.println("正在退出排班管理系统，欢迎下次使用！");
					break;
				default: 
					System.out.println("编号错误，请输入1-8之间的编号！");
					break;	
			}
		}	
	}
	
}
