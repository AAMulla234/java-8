package com.java8.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoreStreams {

	public static void main(String[] args) {
		final List<Integer> inputArrayList = Arrays.asList(1, 3, 2, 4, 3, 1, 2);
        final List<Integer> outputArrayList = inputArrayList.stream()
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        System.out.println(outputArrayList);  // Output: [4, 3, 2, 1]
        
        List<String> strs = findDuplicateChars("Test me dup chars test again and again");
        strs.forEach(s ->  System.out.println(s));
        
        Map.Entry<Integer , List<String>> salaries =  findNthSalary(3);
        
        System.out.println("-------------------");
        List<String> strs2 = findDistinctChars("Test me dup chars test again and again");
        strs2.forEach(s -> System.out.println(s));
        
        System.out.println("Find FIrst unique chars::" + findFirstDistinctChars("Test me dup chars test again and again"));
        
        System.out.println("2nd highest element:::" + nthHighestElementFromArray());
        
        System.out.println("Find First repeat chars:::" + findFirstRepeatChars("Test me dup chars test again and again"));
        
        System.out.println("Find Longest string::::::::" + findLongestString());
        
        findStartWith2().forEach(s -> System.out.println("Start with::" + s));

		List<Integer> list = new ArrayList<>(List.of(1, 2, 3));

		// list.stream().filter(i -> i % 2 == 1).forEach(list::remove); concurrent modification

		int sum = Stream.of(1,2,3,4)
        .reduce(10, Integer::sum);

		System.out.println(sum);
       
	}
	
	public static List<String> findDuplicateChars(String str) {
		return Arrays.stream(str.split("")).collect(Collectors.groupingBy(ch-> ch , Collectors.counting()))
		.entrySet().stream().filter(cha->cha.getValue()>1)
		.map(in->in.getKey()).collect(Collectors.toList());
	}
	
	public static Map<Object, Object> findDuplicateCharsWithCount(String str) {
		return Arrays.stream(str.split("")).collect(Collectors.groupingBy(ch-> ch , Collectors.counting()))
		.entrySet().stream().filter(cha->cha.getValue()>1)
		.collect(Collectors.toMap(Map.Entry :: getKey, Map.Entry :: getValue));
	}
	
	public static List<String> findDistinctChars(String str) {
		return Arrays.stream(str.split("")).collect(Collectors.groupingBy(ch-> ch , Collectors.counting()))
		.entrySet().stream().filter(cha->cha.getValue() == 1)
		.map(in->in.getKey()).collect(Collectors.toList());
	}
	
	public static String findFirstDistinctChars(String str) {
		return Arrays.stream(str.split("")).collect(Collectors.groupingBy(ch-> ch , Collectors.counting()))
		.entrySet().stream().filter(cha->cha.getValue() == 1).findFirst().get().getKey();
		
	}
	
	public static String findFirstRepeatChars(String str) {
		return Arrays.stream(str.split("")).collect(Collectors.groupingBy(ch -> ch, Collectors.counting()))
		.entrySet().stream().filter(cha-> cha.getValue() > 1).findFirst().get().getKey();
	}
	
	private static Map.Entry<Integer , List<String>> findNthSalary(int nth ) {
		 Map<String, Integer> map = new HashMap<>();
			map.put("vivek", 100);	
			map.put("Rinku", 400);	
			map.put("vishal", 600);	
			map.put("shankar", 500);	
			map.put("shyam", 500);	
			map.put("tinku", 300);	
			return	map.entrySet().stream().collect(Collectors.groupingBy(a->a.getValue() ,
						Collectors.mapping(Map.Entry::getKey, Collectors.toList())))
				.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByKey())).
				collect(Collectors.toList()).get(nth-1);
	}
	
	public static Integer nthHighestElementFromArray() {
		int array[] = {1,3,2,4,5,6,6,9,9,10,11};
		return Arrays.stream(array).boxed().distinct().sorted(Comparator.reverseOrder()).skip(1).findFirst().get();
	}
	
	public static String findLongestString() {
		String [] arrayInput = {"vivek"  , "kadiyan" , "ram" , "chaudhary vivek kadiyan"};
		return Arrays.stream(arrayInput).reduce((word1, word2) -> word1.length() > word2.length() ? word1 : word2).get();
	}
	
	public static List<String> findStartWith2() {
		int inputArray[] = {1,25,34,45,26,26,37};
		return Arrays.stream(inputArray).boxed().map(s -> s.toString()).filter(s -> s.startsWith("2")).collect(Collectors.toList());
		
	}
}
