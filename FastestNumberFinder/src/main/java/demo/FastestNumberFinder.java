package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public class FastestNumberFinder implements NumberFinder {
	Logger logger;
	FastestComparator fastestComparator = new FastestComparator();

	public FastestNumberFinder() {
		logger = LoggerFactory.getLogger(FastestNumberFinder.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomNumberEntity> readFromFile(String filePath) {

		try {
			JSONParser parser = new JSONParser();
			File FileNumbers = new File(filePath);

			FileReader fileReader = new FileReader(FileNumbers);
			JSONArray jsonNumbersArray = (JSONArray) parser.parse(fileReader);

		    return (List<CustomNumberEntity>) 
		    		 jsonNumbersArray.stream()
						.map(json -> (String) ((JSONObject) json).get("number"))
						.filter(jsonValues -> (jsonValues != null))
						.map(jsonValues -> {
							 return customNumberEntity(jsonValues.toString());})
						.collect(Collectors.toList());
		} catch (IOException | ParseException | NullPointerException e) {
			logger.warn(e.getMessage());
			return new ArrayList<>();
		}
	}

	private static CustomNumberEntity customNumberEntity(String number) {
		try {
			Constructor<CustomNumberEntity> construct = CustomNumberEntity.class.getDeclaredConstructor(String.class);
			construct.setAccessible(true);
			CustomNumberEntity CustomNumberEntity = construct.newInstance(number);
			return CustomNumberEntity;
		} catch (SecurityException | 
				 IllegalArgumentException | 
				 NoSuchMethodException | 
				 InstantiationException | 
				 IllegalAccessException | 
				 InvocationTargetException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
 	}



	@Override
	public boolean contains(int value, List<CustomNumberEntity> l) {
		Optional<Integer> exist = 
				l.stream()
				.parallel()
				.map(number -> {
					try {
						return fastestComparator.compare(value, number);
					} catch (NumberFormatException e) {
						return Integer.MAX_VALUE;
					}
					})
				.filter(n -> n.equals(0))
				.findAny();
			return exist.isPresent();
	}

}