package APP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
import Assist.Process;
import Assist.time;
import Specific.ProcessIntervalSet;
import exceptions.IntervalConflictException;
import exceptions.StartNegativeException;

/**
 * 操作系统的进程调度管理系统
 * @author 刘小川
 *
 */
public class ProcessScheduleApp {

	public static void printMenu() {
		System.out.println("菜单：");
		System.out.println("1:添加进程");
		System.out.println("2:删除进程");
		System.out.println("3:模拟调度");
		System.out.println("4:最短进程优先模拟调度");
		System.out.println("5:查看进程调度结果");
		System.out.println("6:查看当前正在执行进程");
		System.out.println("7:退出");
	}
	
	public static int readTime(Scanner scanner) {
		while(true) {
			String time = scanner.nextLine();
			try {
				int res = Integer.valueOf(time);
				if(res < 0) {
					System.out.println("时间为非负数！请重新输入！");
					continue;
				}
				return res;
			} catch (Exception e) {
				System.out.println("格式错误，需要为正整数！请重新输入！");
				continue;
			}
		}
	}
	
	public static int readPid(Scanner scanner) {
		while(true) {
			String pid = scanner.nextLine();
			try {
				int res = Integer.valueOf(pid);
				return res;
			} catch (Exception e) {
				System.out.println("格式错误，需要为整数！请重新输入！");
				continue;
			}
		}
	}
	
	
	public static Process readProcess(Scanner scanner) {		
		while(true) {
			String processInfo = scanner.nextLine();
			String[] parse = processInfo.split("-");
			
			if(parse.length != 4) {
				System.out.println("进程信息格式错误！请重新输入！");
				continue;
			}
			
			int pid = 0;
			try {
				pid = Integer.valueOf(parse[0]);
			} catch (Exception e) {
				System.out.println("进程ID信息格式错误！请重新输入！");
				continue;
			}
			String name = parse[1];
			if(name.equals("")) {
				System.out.println("进程名称不能为空！请重新输入！");
				continue;
			}
			long minimumTime = 0;
			try {
				minimumTime = Long.valueOf(parse[2]);
				if(minimumTime <= 0) {
					System.out.println("最短执行时间为正数！请重新输入！");
					continue;
				}
			} catch (Exception e) {
				System.out.println("最短执行时间信息格式错误！请重新输入！");
				continue;
			}
			long maximumTime = 0;
			try {
				maximumTime = Long.valueOf(parse[3]);
				if(minimumTime > maximumTime) {
					System.out.println("最短执行时间不大于最长执行时间！请重新输入！");
					continue;
				}
			} catch (Exception e) {
				System.out.println("最长执行时间信息格式错误！请重新输入！");
				continue;
			}
			
			Process c = new Process(pid, name, minimumTime, maximumTime);
			return c;
		}
	}
	
	public static boolean choice() {
		Random random = new Random();
		return random.nextBoolean();
	}
	
	public static void main(String[] args) {
		System.out.println("欢迎使用进程调度管理系统！");
		
		Scanner scanner = new Scanner(System.in);
		Map<Process, Long> processMap = new HashMap<>(); 
		ProcessIntervalSet<Process> pis = new ProcessIntervalSet<Process>();
		Random rand = new Random();
		printMenu();
		
		boolean flag = true;
		while(flag) {
			System.out.println("请输入要执行的操作编号：");
			String operation = scanner.nextLine();
			switch(operation) {
				case "1":
					System.out.println("请设置进程信息（格式：进程ID-进程名称-最短执行时间-最长执行时间）：");
					Process c = readProcess(scanner);
					if(processMap.containsKey(c)) {
						System.out.println("重复设置进程！");
						break;
					}
					processMap.put(c, (long) 0);
					System.out.println("添加成功！" + c.toString());
					break;
				case "2":
					System.out.println("请输入进程ID：");
					int pid = readPid(scanner);
					Process c1 = new Process(pid, "0", 0, 0);
					if(!(processMap.containsKey(c1))) {
						System.out.println("进程不存在！");
					}
					else {
						pis.remove(c1);
						processMap.remove(c1);
						System.out.println("删除成功！进程ID：" + c1.getPid());
					}
					break;
				case "3":
					pis = new ProcessIntervalSet<Process>();
					if(processMap.size() == 0) {
						System.out.println("当前无进程，请添加进程后再进行调度！");
						break;
					}
					long start = 0;
					Map<Process, Long> copyMap = new HashMap<Process, Long>();
					List<Process> plist = new ArrayList<Process>();
					copyMap.putAll(processMap);
					plist.addAll(processMap.keySet());
					while(copyMap.size() > 0) {
						if(choice()) {
							start += rand.nextInt(5);
							continue;
						}
						int choice = rand.nextInt(processMap.size());
						Process pchoice = plist.get(choice);
						if(!(copyMap.containsKey(pchoice)))
							continue;
						else{
							long allocateTime = rand.nextLong() % (pchoice.getMaximumTime() - copyMap.get(pchoice));
							if(allocateTime < 0)
								allocateTime += pchoice.getMaximumTime() - copyMap.get(pchoice);
							allocateTime += 1;
							try {
								pis.insert(start, start + allocateTime, pchoice);
							} catch (IntervalConflictException | StartNegativeException e) {
								System.out.println("Error!");
								break;
							}
							start += allocateTime;
							copyMap.put(pchoice, copyMap.get(pchoice) + allocateTime);
							if(copyMap.get(pchoice) >= pchoice.getMinimumTime())
								copyMap.remove(pchoice);
						}
					}
					System.out.println("模拟调度完成！");
					break;
				case "4":
					pis = new ProcessIntervalSet<Process>();
					if(processMap.size() == 0) {
						System.out.println("当前无进程，请添加进程后再进行调度！");
						break;
					}
					long start1 = 0;
					Map<Process, Long> copyMap1 = new HashMap<Process, Long>();
					copyMap1.putAll(processMap);
					while(copyMap1.size() > 0) {
						if(choice()) {
							start1 += rand.nextInt(5);
							continue;
						}
						Process pchoice = null;
						long gap = Long.MAX_VALUE;
						for(Process p:copyMap1.keySet()) {
							if(pchoice == null) {
								pchoice = p;
								gap = pchoice.getMinimumTime() - copyMap1.get(pchoice);
							}
							else {
								long newGap = p.getMinimumTime() - copyMap1.get(p);
								if(newGap < gap) {
									pchoice = p;
									gap = newGap;
								}
							}
						}
						long allocateTime = rand.nextLong() % (pchoice.getMaximumTime() - copyMap1.get(pchoice));
						if(allocateTime < 0)
							allocateTime += pchoice.getMaximumTime() - copyMap1.get(pchoice);
						allocateTime += 1;
						try {
							pis.insert(start1, start1 + allocateTime, pchoice);
						} catch (IntervalConflictException | StartNegativeException e) {
							System.out.println("Error!");
							break;
						}
						start1 += allocateTime;
						copyMap1.put(pchoice, copyMap1.get(pchoice) + allocateTime);
						if(copyMap1.get(pchoice) >= pchoice.getMinimumTime())
							copyMap1.remove(pchoice);
					}
					System.out.println("最短进程优先模拟调度完成！");
					break;
				case "5":
					System.out.println("时间段\t\t调度进程");
					Map<time, Process> reverseMap = new TreeMap<>();
					for(Process p:pis.labels()) {
						for(Integer i:pis.intervals(p).labels()) {
							long s = pis.intervals(p).start(i);
							long e = pis.intervals(p).end(i);
							time t = new time(s, e);
							reverseMap.put(t,p);
						}
					}
					for(time t:reverseMap.keySet()) {
						System.out.println(t.getStart() + "-" + t.getEnd() + "\t\t" + reverseMap.get(t).toString());
					}
					System.out.println();
					break;
				case "6":
					System.out.println("请输入当前时间：");
					long tim = readTime(scanner);
					Map<time, Process> reverse = new TreeMap<>();
					for(Process p:pis.labels()) {
						for(Integer i:pis.intervals(p).labels()) {
							long s = pis.intervals(p).start(i);
							long e = pis.intervals(p).end(i);
							time t = new time(s, e);
							reverse.put(t,p);
						}
					}
					for(time t1:reverse.keySet()) {
						if(tim >= t1.getStart() && tim <= t1.getEnd()) {
							System.out.println("当前执行进程：" + reverse.get(t1).toString() + "\t" + "执行时间段：" + t1.getStart() + "-" + t1.getEnd());
							break;
						}
					}
					System.out.println("当前时间无进程执行！");
					break;
				case "7":
					flag = false;
					System.out.println("正在退出进程调度管理系统，欢迎下次使用！");
					break;
				default: 
					System.out.println("编号错误，请输入1-7之间的编号！");
					break;	
			}
		}	
	}
}
