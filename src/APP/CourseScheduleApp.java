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
		System.out.println("�˵���");
		System.out.println("1:��ӿγ�");
		System.out.println("2:ɾ���γ�");
		System.out.println("3:���ſγ�");
		System.out.println("4:ȡ���γ̰���");
		System.out.println("5:�鿴δ���ſγ�");
		System.out.println("6:�鿴ÿ�ܿ���ʱ���");
		System.out.println("7:�鿴ÿ���ظ�ʱ���");
		System.out.println("8:�鿴�α���");
		System.out.println("9:�˳�");
	}
	
	public static int readPeriod(Scanner scanner) {
		while(true) {
			String periodInfo = scanner.nextLine();
			try {
				int res = Integer.valueOf(periodInfo);
				if(res <= 0) {
					System.out.println("������Ϊ�Ǹ��������������룡");
					continue;
				}
				return res;
			} catch (Exception e) {
				System.out.println("��������ʽ�������������룡");
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
				System.out.println("���ڸ�ʽ�������������루��ʽyyyy-mm-dd��");
				s = scanner.nextLine();
			}
		}
	}
	
	public static Course readCourse(Scanner scanner) {		
		while(true) {
			String courseInfo = scanner.nextLine();
			String[] parse = courseInfo.split("-");
			
			if(parse.length != 4) {
				System.out.println("�γ���Ϣ��ʽ�������������룡");
				continue;
			}
			
			int cid = 0;
			try {
				cid = Integer.valueOf(parse[0]);
			} catch (Exception e) {
				System.out.println("�γ�ID��Ϣ��ʽ�������������룡");
				continue;
			}
			
			String courseName = parse[1];
			String teacherName = parse[2];
			String location = parse[3];
			if(courseName.equals("")) {
				System.out.println("�γ̲���Ϊ�գ����������룡");
				continue;
			}
			if(teacherName.equals("")) {
				System.out.println("��ʦ������Ϊ�գ����������룡");
				continue;
			}
			if(location.equals("")) {
				System.out.println("�ص㲻��Ϊ�գ����������룡");
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
					System.out.println("��ѧʱ��Ϊ�Ǹ��������������룡");
					continue;
				}
				if(res % 2 == 1) {
					System.out.println("��ѧʱ��Ϊż�������������룡");
					continue;
				}
				return res;
			} catch (Exception e) {
				System.out.println("��ѧʱ����ʽ�������������룡");
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
				System.out.println("��ʽ�������������루ֻ�ܴ�8-10��10-12��13-15,15-17,19-21��ѡ��");
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
		System.out.println("��ӭʹ�ÿα����ϵͳ��");
		System.out.println("���ʼ��һЩ��Ϣ��");
		
		Scanner scanner = new Scanner(System.in);
		Map<Course, Integer> courseMap = new HashMap<Course, Integer>(); 
		CourseIntervalSet<Course> cis;
		long startTime;
		long endTime;
		
		System.out.println("���趨ѧ�ڿ�ʼ���ڣ���ʽyyyy-mm-dd����");
		startTime = readTime(scanner);
		System.out.println("���趨��������");
		int week = readPeriod(scanner);
		LocalDate sld = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault()).toLocalDate();
		LocalDate eld = sld.plusWeeks(week);
		endTime = eld.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		long period = (endTime - startTime) / week;
		cis = new CourseIntervalSet<Course>(period);
		
		while(true) {
			System.out.println("�����ÿγ���Ϣ����ʽ���γ�ID-�γ�����-��ʦ����-�ص㣩��");
			Course c = readCourse(scanner);
			System.out.println("�����ÿγ̵���ѧʱ����ż������");
			int hours = readWeekHours(scanner);
			courseMap.put(c, hours);
			System.out.println("��ӳɹ���" + c.toString() + ",hours:" + hours);
			System.out.println("�Ƿ������ӿγ���Ϣ��������nֹͣ�����������������");
			String cont = scanner.nextLine();
			if(cont.equals("n"))
				break;
		}
		
		printMenu();
		boolean flag = true;
		while(flag) {
			System.out.println("������Ҫִ�еĲ�����ţ�");
			String operation = scanner.nextLine();
			switch(operation) {
				case "1":
					System.out.println("�����ÿγ���Ϣ����ʽ���γ�ID-�γ�����-��ʦ����-�ص㣩��");
					Course c = readCourse(scanner);
					if(courseMap.containsKey(c)) {
						System.out.println("�ظ����ÿγ̣�");
						break;
					}
					System.out.println("�����ÿγ̵���ѧʱ����ż������");
					int hours = readWeekHours(scanner);
					courseMap.put(c, hours);
					System.out.println("��ӳɹ���" + c.toString() + ",hours:" + hours);
					break;
				case "2":
					System.out.println("������γ���Ϣ����ʽ���γ�ID-�γ�����-��ʦ����-�ص㣩��");
					Course c1 = readCourse(scanner);
					if(!(courseMap.containsKey(c1))) {
						System.out.println("�γ̲����ڣ�");
					}
					else {
						cis.remove(c1);
						courseMap.remove(c1);
						System.out.println("ɾ���ɹ���" + c1.toString());
					}
					break;
				case "3":
					System.out.println("������γ���Ϣ����ʽ���γ�ID-�γ�����-��ʦ����-�ص㣩��");
					Course c2 = readCourse(scanner);
					if(!(courseMap.containsKey(c2))) {
						System.out.println("�γ̲����ڣ�������ӣ�");
						break;
					}
					if(courseMap.get(c2) == 0) {
						System.out.println("�ÿγ��Ѿ�������ɣ�����������ţ�");
						break;
					}
					while(true) {
						System.out.println("���趨�γ̰������ڣ���ʽyyyy-mm-dd����");
						long startDate = readTime(scanner);
						if(startDate >= endTime || startDate < startTime) {
							System.out.println("����ѧ�ڷ�Χ�ڣ����������룡");
							continue;
						}
						LocalDate ldd = LocalDateTime.ofInstant(Instant.ofEpochMilli(startDate), ZoneId.systemDefault()).toLocalDate();
						LocalDateTime ldtd = LocalDateTime.of(ldd, LocalTime.parse("00:00:00"));
						System.out.println("���趨�γ�ʱ��Σ�ֻ�ܴ�8-10��10-12��13-15,15-17,19-21��ѡ�񣩣�");
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
							System.out.println("���ųɹ���");
							int left = courseMap.get(c2) - 2;
							if(f)
								courseMap.put(c2, left);
						} catch (IntervalConflictException | StartNegativeException e3) {
							System.out.println("���Ŵ���Overlap��");
						}
						break;
					}
					break;
				case "4":
					System.out.println("������γ���Ϣ����ʽ���γ�ID-�γ�����-��ʦ����-�ص㣩��");
					Course c3 = readCourse(scanner);
					if(!(courseMap.containsKey(c3))) {
						System.out.println("�γ̲����ڣ�������ӣ�");
						break;
					}
					int add = cis.intervals(c3).labels().size() * 2;
					if(cis.remove(c3)) {
						System.out.println("�γ̰���ȡ���ɹ���");
						courseMap.put(c3, courseMap.get(c3) + add);
					}
					else {
						System.out.println("�ÿγ���δ�����ţ�����ȡ����");
					}
					break;
				case "5":
					Set<Course> leftCourse = new TreeSet<Course>();
					for(Course course:courseMap.keySet()) {
						if(courseMap.get(course) > 0)
							leftCourse.add(course);
					}
					if(leftCourse.size() == 0) {
						System.out.println("��ǰ��δ�����ŵĿγ̣�");
						break;
					}
					System.out.println("�γ�\t\t\t\tδ����ѧʱ��");
					for(Course course:leftCourse) {
						System.out.println(course.toString() + "\t" + courseMap.get(course));
					}
					break;
				case "6":
					if(APIs.totalSpan(cis) == 0) {
						System.out.println("��ǰÿ�ܿ���ʱ�������" + 1.0);
						break;
					}
					double total = APIs.calcFreeTimeRatio(cis) * APIs.totalSpan(cis);
					double left = period + total - APIs.totalSpan(cis);
					System.out.println("��ǰÿ�ܿ���ʱ�������" + left/(double)(period));
					break;
				case "7":
					double total2 = APIs.calcConflictRatio(cis) * APIs.totalSpan(cis);
					System.out.println("��ǰÿ���ظ�ʱ�������" + total2/(double)(period));
					break;
				case "8":
					System.out.println("�������ѯ���ڣ���ʽyyyy-mm-dd����");
					long startDate;
					while(true) {
						startDate = readTime(scanner);
						if(startDate >= endTime || startDate < startTime) {
							System.out.println("����ѧ�ڷ�Χ�ڣ����������룡");
							continue;
						}
						else {
							break;
						}
					}
					
					LocalDate ldd = LocalDateTime.ofInstant(Instant.ofEpochMilli(startDate), ZoneId.systemDefault()).toLocalDate();
					System.out.println("�γ���Ϣ(" + ldd.getYear() + "-" + ldd.getMonthValue() + "-" + ldd.getDayOfMonth() + "):");
					System.out.println("ʱ��\t�γ�");
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
					System.out.println("�����˳��α����ϵͳ����ӭ�´�ʹ�ã�");
					break;
				default: 
					System.out.println("��Ŵ���������1-9֮��ı�ţ�");
					break;	
			}
		}	
	}
}
