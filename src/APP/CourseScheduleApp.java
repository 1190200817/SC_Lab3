package APP;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import APIs.APIs;
import Assist.Course;
import Specific.CourseIntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;


public class CourseScheduleApp {

	public static void printMenu() {
		System.out.println("菜单：");
		System.out.println("1:添加课程");
		System.out.println("2:删除课程");
		System.out.println("3:安排课程");
		System.out.println("4:取消课程安排");
		System.out.println("5:查看未安排课程");
		System.out.println("6:查看每周空闲时间比");
		System.out.println("7:查看每周重复时间比");
		System.out.println("8:查看课表结果");
		System.out.println("9:退出");
	}
	
	public static int readPeriod(Scanner scanner) {
		while(true) {
			String periodInfo = scanner.nextLine();
			try {
				int res = Integer.valueOf(periodInfo);
				if(res <= 0) {
					System.out.println("总周数为非负数！请重新输入！");
					continue;
				}
				return res;
			} catch (Exception e) {
				System.out.println("总周数格式错误！请重新输入！");
				continue;
			}
		}
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
	
	public static Course readCourse(Scanner scanner) {		
		while(true) {
			String courseInfo = scanner.nextLine();
			String[] parse = courseInfo.split("-");
			
			if(parse.length != 4) {
				System.out.println("课程信息格式错误！请重新输入！");
				continue;
			}
			
			int cid = 0;
			try {
				cid = Integer.valueOf(parse[0]);
			} catch (Exception e) {
				System.out.println("课程ID信息格式错误！请重新输入！");
				continue;
			}
			
			String courseName = parse[1];
			String teacherName = parse[2];
			String location = parse[3];
			if(courseName.equals("")) {
				System.out.println("课程不能为空！请重新输入！");
				continue;
			}
			if(teacherName.equals("")) {
				System.out.println("教师名不能为空！请重新输入！");
				continue;
			}
			if(location.equals("")) {
				System.out.println("地点不能为空！请重新输入！");
				continue;
			}
			Course c = new Course(cid, courseName, teacherName, location);
			return c;
		}
	}
	
	public static int readWeekHours(Scanner scanner) {
		while(true) {
			String hoursInfo = scanner.nextLine();
			try {
				int res = Integer.valueOf(hoursInfo);
				if(res <= 0) {
					System.out.println("周学时数为非负数！请重新输入！");
					continue;
				}
				if(res % 2 == 1) {
					System.out.println("周学时数为偶数！请重新输入！");
					continue;
				}
				return res;
			} catch (Exception e) {
				System.out.println("周学时数格式错误！请重新输入！");
				continue;
			}
		}
	}
	
	public static int readInterval(Scanner scanner) {
		while(true) {
			String interval = scanner.nextLine();
			switch (interval) {
			case "8-10":
				return 8;
			case "10-12":
				return 10;
			case "13-15":
				return 13;
			case "15-17":
				return 15;
			case "19-21":
				return 19;
			default:
				System.out.println("格式错误，请重新输入（只能从8-10，10-12，13-15,15-17,19-21中选择）");
				break;
			}
		}
	}
	
	public static void printCouseInfo(long start,int hour, CourseIntervalSet<Course> cis) {
		for(Course course:cis.labels()) {
			for(Integer i:cis.intervals(course).labels()) {
				if(cis.intervals(course).start(i) == start) {
					System.out.println(hour + "-" +  (hour + 2) + "\t" + course.toString());
					break;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		System.out.println("欢迎使用课表管理系统！");
		System.out.println("请初始化一些信息！");
		
		Scanner scanner = new Scanner(System.in);
		Map<Course, Integer> courseMap = new HashMap<Course, Integer>(); 
		CourseIntervalSet<Course> cis;
		long startTime;
		long endTime;
		
		System.out.println("请设定学期开始日期（格式yyyy-mm-dd）：");
		startTime = readTime(scanner);
		System.out.println("请设定总周数：");
		int week = readPeriod(scanner);
		LocalDate sld = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault()).toLocalDate();
		LocalDate eld = sld.plusWeeks(week);
		endTime = eld.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		long period = (endTime - startTime) / week;
		cis = new CourseIntervalSet<Course>(period);
		
		while(true) {
			System.out.println("请设置课程信息（格式：课程ID-课程名称-教师名称-地点）：");
			Course c = readCourse(scanner);
			System.out.println("请设置课程的周学时数（偶数）：");
			int hours = readWeekHours(scanner);
			courseMap.put(c, hours);
			System.out.println("添加成功！" + c.toString() + ",hours:" + hours);
			System.out.println("是否继续添加课程信息？（输入n停止，其他任意键继续）");
			String cont = scanner.nextLine();
			if(cont.equals("n"))
				break;
		}
		
		printMenu();
		boolean flag = true;
		while(flag) {
			System.out.println("请输入要执行的操作编号：");
			String operation = scanner.nextLine();
			switch(operation) {
				case "1":
					System.out.println("请设置课程信息（格式：课程ID-课程名称-教师名称-地点）：");
					Course c = readCourse(scanner);
					if(courseMap.containsKey(c)) {
						System.out.println("重复设置课程！");
						break;
					}
					System.out.println("请设置课程的周学时数（偶数）：");
					int hours = readWeekHours(scanner);
					courseMap.put(c, hours);
					System.out.println("添加成功！" + c.toString() + ",hours:" + hours);
					break;
				case "2":
					System.out.println("请输入课程信息（格式：课程ID-课程名称-教师名称-地点）：");
					Course c1 = readCourse(scanner);
					if(!(courseMap.containsKey(c1))) {
						System.out.println("课程不存在！");
					}
					else {
						cis.remove(c1);
						courseMap.remove(c1);
						System.out.println("删除成功！" + c1.toString());
					}
					break;
				case "3":
					System.out.println("请输入课程信息（格式：课程ID-课程名称-教师名称-地点）：");
					Course c2 = readCourse(scanner);
					if(!(courseMap.containsKey(c2))) {
						System.out.println("课程不存在！请先添加！");
						break;
					}
					if(courseMap.get(c2) == 0) {
						System.out.println("该课程已经安排完成，无需继续安排！");
						break;
					}
					while(true) {
						System.out.println("请设定课程安排日期（格式yyyy-mm-dd）：");
						long startDate = readTime(scanner);
						if(startDate >= endTime || startDate < startTime) {
							System.out.println("不在学期范围内！请重新输入！");
							continue;
						}
						LocalDate ldd = LocalDateTime.ofInstant(Instant.ofEpochMilli(startDate), ZoneId.systemDefault()).toLocalDate();
						LocalDateTime ldtd = LocalDateTime.of(ldd, LocalTime.parse("00:00:00"));
						System.out.println("请设定课程时间段（只能从8-10，10-12，13-15,15-17,19-21中选择）：");
						int startHour = readInterval(scanner);
						LocalDateTime ldts = ldtd.plusHours(startHour);
						LocalDateTime ldte = ldts.plusHours(2);
						long start = ldts.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - startTime;
						long end = ldte.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - startTime;

						try {
							boolean f = true;
							for(Integer i:cis.intervals(c2).labels()) {
								if(cis.intervals(c2).start(i) == start) {
									f= false;
									break;
								}
							}
							cis.insert(start, end, c2);
							System.out.println("编排成功！");
							int left = courseMap.get(c2) - 2;
							if(f)
								courseMap.put(c2, left);
						} catch (IntervalConflictException | StartNegativeException e3) {
							System.out.println("编排错误Overlap！");
						}
						break;
					}
					break;
				case "4":
					System.out.println("请输入课程信息（格式：课程ID-课程名称-教师名称-地点）：");
					Course c3 = readCourse(scanner);
					if(!(courseMap.containsKey(c3))) {
						System.out.println("课程不存在！请先添加！");
						break;
					}
					int add = cis.intervals(c3).labels().size() * 2;
					if(cis.remove(c3)) {
						System.out.println("课程安排取消成功！");
						courseMap.put(c3, courseMap.get(c3) + add);
					}
					else {
						System.out.println("该课程尚未被安排，无需取消！");
					}
					break;
				case "5":
					Set<Course> leftCourse = new TreeSet<Course>();
					for(Course course:courseMap.keySet()) {
						if(courseMap.get(course) > 0)
							leftCourse.add(course);
					}
					if(leftCourse.size() == 0) {
						System.out.println("当前无未被安排的课程！");
						break;
					}
					System.out.println("课程\t\t\t\t未安排学时数");
					for(Course course:leftCourse) {
						System.out.println(course.toString() + "\t" + courseMap.get(course));
					}
					break;
				case "6":
					if(APIs.totalSpan(cis) == 0) {
						System.out.println("当前每周空闲时间比例：" + 1.0);
						break;
					}
					double total = APIs.calcFreeTimeRatio(cis) * APIs.totalSpan(cis);
					double left = period + total - APIs.totalSpan(cis);
					System.out.println("当前每周空闲时间比例：" + left/(double)(period));
					break;
				case "7":
					double total2 = APIs.calcConflictRatio(cis) * APIs.totalSpan(cis);
					System.out.println("当前每周重复时间比例：" + total2/(double)(period));
					break;
				case "8":
					System.out.println("请输入查询日期（格式yyyy-mm-dd）：");
					long startDate;
					while(true) {
						startDate = readTime(scanner);
						if(startDate >= endTime || startDate < startTime) {
							System.out.println("不在学期范围内！请重新输入！");
							continue;
						}
						else {
							break;
						}
					}
					
					LocalDate ldd = LocalDateTime.ofInstant(Instant.ofEpochMilli(startDate), ZoneId.systemDefault()).toLocalDate();
					System.out.println("课程信息(" + ldd.getYear() + "-" + ldd.getMonthValue() + "-" + ldd.getDayOfMonth() + "):");
					System.out.println("时间\t课程");
					LocalDateTime ldtd = LocalDateTime.of(ldd, LocalTime.parse("00:00:00"));
					LocalDateTime ldtTemp = ldtd.plusHours(8);
					long start = (ldtTemp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - startTime) % period;
					if(start < 0)
						start += period;
					printCouseInfo(start, 8, cis);
					
					ldtTemp = ldtd.plusHours(10);
					start = (ldtTemp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - startTime) % period;
					if(start < 0)
						start += period;
					printCouseInfo(start, 10, cis);
					
					ldtTemp = ldtd.plusHours(13);
					start = (ldtTemp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - startTime) % period;
					if(start < 0)
						start += period;
					printCouseInfo(start, 13, cis);
					
					ldtTemp = ldtd.plusHours(15);
					start = (ldtTemp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - startTime) % period;
					if(start < 0)
						start += period;
					printCouseInfo(start, 15, cis);
					
					ldtTemp = ldtd.plusHours(19);
					start = (ldtTemp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - startTime) % period;
					if(start < 0)
						start += period;
					printCouseInfo(start, 19, cis);		
					System.out.println();
					break;
				case "9":
					flag = false;
					System.out.println("正在退出课表管理系统，欢迎下次使用！");
					break;
				default: 
					System.out.println("编号错误，请输入1-9之间的编号！");
					break;	
			}
		}	
	}
}
