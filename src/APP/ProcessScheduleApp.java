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
 * ����ϵͳ�Ľ��̵��ȹ���ϵͳ
 * @author ��С��
 *
 */
public class ProcessScheduleApp {

	public static void printMenu() {
		System.out.println("�˵���");
		System.out.println("1:��ӽ���");
		System.out.println("2:ɾ������");
		System.out.println("3:ģ�����");
		System.out.println("4:��̽�������ģ�����");
		System.out.println("5:�鿴���̵��Ƚ��");
		System.out.println("6:�鿴��ǰ����ִ�н���");
		System.out.println("7:�˳�");
	}
	
	public static int readTime(Scanner scanner) {
		while(true) {
			String time = scanner.nextLine();
			try {
				int res = Integer.valueOf(time);
				if(res < 0) {
					System.out.println("ʱ��Ϊ�Ǹ��������������룡");
					continue;
				}
				return res;
			} catch (Exception e) {
				System.out.println("��ʽ������ҪΪ�����������������룡");
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
				System.out.println("��ʽ������ҪΪ���������������룡");
				continue;
			}
		}
	}
	
	
	public static Process readProcess(Scanner scanner) {		
		while(true) {
			String processInfo = scanner.nextLine();
			String[] parse = processInfo.split("-");
			
			if(parse.length != 4) {
				System.out.println("������Ϣ��ʽ�������������룡");
				continue;
			}
			
			int pid = 0;
			try {
				pid = Integer.valueOf(parse[0]);
			} catch (Exception e) {
				System.out.println("����ID��Ϣ��ʽ�������������룡");
				continue;
			}
			String name = parse[1];
			if(name.equals("")) {
				System.out.println("�������Ʋ���Ϊ�գ����������룡");
				continue;
			}
			long minimumTime = 0;
			try {
				minimumTime = Long.valueOf(parse[2]);
				if(minimumTime <= 0) {
					System.out.println("���ִ��ʱ��Ϊ���������������룡");
					continue;
				}
			} catch (Exception e) {
				System.out.println("���ִ��ʱ����Ϣ��ʽ�������������룡");
				continue;
			}
			long maximumTime = 0;
			try {
				maximumTime = Long.valueOf(parse[3]);
				if(minimumTime > maximumTime) {
					System.out.println("���ִ��ʱ�䲻�����ִ��ʱ�䣡���������룡");
					continue;
				}
			} catch (Exception e) {
				System.out.println("�ִ��ʱ����Ϣ��ʽ�������������룡");
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
		System.out.println("��ӭʹ�ý��̵��ȹ���ϵͳ��");
		
		Scanner scanner = new Scanner(System.in);
		Map<Process, Long> processMap = new HashMap<>(); 
		ProcessIntervalSet<Process> pis = new ProcessIntervalSet<Process>();
		Random rand = new Random();
		printMenu();
		
		boolean flag = true;
		while(flag) {
			System.out.println("������Ҫִ�еĲ�����ţ�");
			String operation = scanner.nextLine();
			switch(operation) {
				case "1":
					System.out.println("�����ý�����Ϣ����ʽ������ID-��������-���ִ��ʱ��-�ִ��ʱ�䣩��");
					Process c = readProcess(scanner);
					if(processMap.containsKey(c)) {
						System.out.println("�ظ����ý��̣�");
						break;
					}
					processMap.put(c, (long) 0);
					System.out.println("��ӳɹ���" + c.toString());
					break;
				case "2":
					System.out.println("���������ID��");
					int pid = readPid(scanner);
					Process c1 = new Process(pid, "0", 0, 0);
					if(!(processMap.containsKey(c1))) {
						System.out.println("���̲����ڣ�");
					}
					else {
						pis.remove(c1);
						processMap.remove(c1);
						System.out.println("ɾ���ɹ�������ID��" + c1.getPid());
					}
					break;
				case "3":
					pis = new ProcessIntervalSet<Process>();
					if(processMap.size() == 0) {
						System.out.println("��ǰ�޽��̣�����ӽ��̺��ٽ��е��ȣ�");
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
					System.out.println("ģ�������ɣ�");
					break;
				case "4":
					pis = new ProcessIntervalSet<Process>();
					if(processMap.size() == 0) {
						System.out.println("��ǰ�޽��̣�����ӽ��̺��ٽ��е��ȣ�");
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
					System.out.println("��̽�������ģ�������ɣ�");
					break;
				case "5":
					System.out.println("ʱ���\t\t���Ƚ���");
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
					System.out.println("�����뵱ǰʱ�䣺");
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
							System.out.println("��ǰִ�н��̣�" + reverse.get(t1).toString() + "\t" + "ִ��ʱ��Σ�" + t1.getStart() + "-" + t1.getEnd());
							break;
						}
					}
					System.out.println("��ǰʱ���޽���ִ�У�");
					break;
				case "7":
					flag = false;
					System.out.println("�����˳����̵��ȹ���ϵͳ����ӭ�´�ʹ�ã�");
					break;
				default: 
					System.out.println("��Ŵ���������1-7֮��ı�ţ�");
					break;	
			}
		}	
	}
}
