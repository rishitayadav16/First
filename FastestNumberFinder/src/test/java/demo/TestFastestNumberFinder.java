package demo;

import java.util.List;

import demo.CustomNumberEntity;
import demo.FastestNumberFinder;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestFastestNumberFinder {

	FastestNumberFinder fastNumberFinder = new FastestNumberFinder();

	
	@Before
	public void init() {
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism","12");
	}
	
	
	@Test
	public void testReadFromFile() {

		List<CustomNumberEntity> filesList = fastNumberFinder.readFromFile(System.getProperty("user.dir") + "/TestNumberFile.txt");
		assertEquals("["+"CustomNumberEntity [number=67]"+", "
				        +"CustomNumberEntity [number=45]"+", "
				        +"CustomNumberEntity [number=45]"+", "
				        +"CustomNumberEntity [number=s]"+", "
				        +"CustomNumberEntity [number=-3]"+", "
				        +"CustomNumberEntity [number=12]"+", "
				        +"CustomNumberEntity [number=100]"+", "
				        +"CustomNumberEntity [number=3]"+"]",
				        filesList.toString());
	}
	
	@Test
	public void testContainsYes() {
		List<CustomNumberEntity> filesList = fastNumberFinder.readFromFile(System.getProperty("user.dir") + "/TestNumberFile.txt");
		assertEquals(true, fastNumberFinder.contains(100, filesList));
	}

	@Test
	public void testContainsNo() {
		List<CustomNumberEntity> filesList = fastNumberFinder.readFromFile(System.getProperty("user.dir") + "/TestNumberFile.txt");
		assertEquals(false, fastNumberFinder.contains(10, filesList));
	}

	@Test
	public void testReadFromFileJsonInCorrect() {
		List<CustomNumberEntity> filesList = fastNumberFinder.readFromFile(System.getProperty("user.dir") + "/TestJsonFileInCorrect.txt");
		assertEquals(0, filesList.size());
	}
	
	@Test
	public void testReadFromFileNotExist() {
		List<CustomNumberEntity> filesList = fastNumberFinder.readFromFile(System.getProperty("user.dir") + "/TestFileNotExist.txt");
		assertEquals(0, filesList.size());
	}

}