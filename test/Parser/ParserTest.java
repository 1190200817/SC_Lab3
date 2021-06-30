package Parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ParserTest {

	// 对test1.txt - test8.txt进行测试
	// test1.txt:true
	// test2.txt:false
	// test3.txt:false
	// test4.txt:true
	// test5.txt:true
	// test6.txt:false
	// test7.txt:true
	// test8.txt:true
	@Test
	public void fileTest() {
		String format = new String("gbk");
		String baseString = "./src/txt/test";
		List<String> sList = new ArrayList<String>();
		
		for(int i = 1; i <= 8; i++) {
			String res = "";
			String fileName = baseString + i + ".txt";
			try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),format))) {
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
				sList.add(res);
			}
			catch (Exception e) {
				System.out.println("读文件错误！");
				return;
			}
			// System.out.println(res);
			System.out.println("test" + i + ".txt:" + new Parser().checkValid(res));
		}
		assertTrue(new Parser().checkValid(sList.get(0)));
		assertFalse(new Parser().checkValid(sList.get(1)));
		assertFalse(new Parser().checkValid(sList.get(2)));
		assertTrue(new Parser().checkValid(sList.get(3)));
		assertTrue(new Parser().checkValid(sList.get(4)));
		assertFalse(new Parser().checkValid(sList.get(5)));
		assertTrue(new Parser().checkValid(sList.get(6)));
		assertTrue(new Parser().checkValid(sList.get(7)));
	}
	
	@Test
	public void parseEmployeeTest() {
	
		// 解析成功
		String s = "Employee{\n" + 
				"ZhangSan{Manger,139-0451-0000}\n" + 
				"LiSi{Secretary,151-0101-0000}\n" + 
				"ZhaoLiuj{AssciateProfessor,138-1920-0044}\n" + 
				"ZhaoLiuk{Professor,188-1920-0000}\n" + 
				"}";
		List<String> res = new ArrayList<String>();
		res.add("ZhangSan{Manger,139-0451-0000}\n");
		res.add("LiSi{Secretary,151-0101-0000}\n");
		res.add("ZhaoLiuj{AssciateProfessor,138-1920-0044}\n");
		res.add("ZhaoLiuk{Professor,188-1920-0000}\n");
		assertEquals(res, new Parser().parseEmployee(s));
		
		// 解析失败
		String s2 = "Employee{\nZhangSan{Manger,139-0451-0000} abc";
		assertEquals(new ArrayList<String>(), new Parser().parseEmployee(s2));
	}
	
	@Test
	public void parsePeriodTest() {
		
		// 解析成功
		String s = "　ZhaoLiuj{AssciateProfessor,138-1920-0044}\r\n" + 
				"　ZhaoLiuk{Professor,188-1920-0000}\r\n" + 
				"}\r\n" + 
				"Period{2021-01-10,2021-03-06}\n" + 
				"Roster{ \r\n" + 
				"　ZhangSan{2021-01-10,2021-01-11}";
		assertEquals("Period{2021-01-10,2021-03-06}", new Parser().parsePeriod(s));
		
		// 解析失败
		String s1 = "　ZhaoLiuj{AssciateProfessor,138-1920-0044}\r\n" +
					"ZhaoLiuk{Professor,188-1920-0000}\r\n}Period{2021-";
		assertEquals("", new Parser().parsePeriod(s1));	
	}
	
	@Test
	public void parseRosterTest() {
		// 解析成功
		String s = "Roster{\n" + 
				"ZhangSan{2021-01-10,2021-01-11}\n" + 
				"LiSi{2021-01-12,2021-01-20}\n" + 
				"WangWu{2021-01-21,2021-01-21}\n" + 
				"}";
		List<String> res = new ArrayList<String>();
		res.add("ZhangSan{2021-01-10,2021-01-11}\n");
		res.add("LiSi{2021-01-12,2021-01-20}\n");
		res.add("WangWu{2021-01-21,2021-01-21}\n");
		assertEquals(res, new Parser().parseRoster(s));
		// 解析失败
		String s1 = "}\r\n" + 
				"Period{2021-01-10,2021-03-06}\r\n" + 
				"Roster{ \r\n" + 
				"　ZhangSan{2021-01-10,2021-01-11}\r\n" + 
				"　LiSi{2021-01-12,2021-01-20}\r\n" + 
				"　WangWu{2021-01-21,2021-01-21}";
		assertEquals(new ArrayList<String>(), new Parser().parseRoster(s1));
		
	}

}
