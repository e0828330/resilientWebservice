package utils;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

public class RandomData {

	/**
	 * Returns a random number as String
	 * @return
	 */
	public String getNumber() {
		Random randomGenerator = new Random();
		String result = "" + randomGenerator.nextInt(100);
		return result;
	}
	
	/**
	 * Returns a random word
	 * @return
	 */
	public String getWord() {
		return RandomStringUtils.randomAlphabetic(new Random().nextInt(10));
	}
	
	/**
	 * Returns a random string from the given string 
	 * array
	 * @param list
	 * @return
	 */
	public String getFromList(String[] list) {
		Random randomGenerator = new Random();
		int index = randomGenerator.nextInt(list.length);
		return list[index];
	}
}
