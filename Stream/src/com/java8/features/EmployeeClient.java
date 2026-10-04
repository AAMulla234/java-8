package com.java8.features;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeeClient {

    static List<Employee> employeeList = new ArrayList<>();

	public static void main(String[] args) {
		
		
		        EmployeeFactory employeeFactory = new EmployeeFactory();
		        employeeList = employeeFactory.getAllEmployee();

		  // List all distinct project in non-ascending order
		  // FlatMap is used while doing list of list or list inside list
		   List<String> uniqueProjects = employeeList.stream().flatMap(emp -> emp.getProjects().stream()).map(Project :: getName)				   
				   									.distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
		
		   uniqueProjects.forEach(p -> System.out.println(p));
		   
		   //Print full name of any employee whose firstName starts with ‘A’.
		   List<String> employeeNames =  employeeList.stream().map(emp -> emp.getFirstName() + " " + emp.getLastName()).filter(s -> s.startsWith("A")).collect(Collectors.toList());
		   employeeNames.forEach(e -> System.out.println(e));
		   
		   //List of all employee who joined in year 2023 (year to be extracted from employee id i.e., 1st 4 characters)
		   List<Employee> employees = employeeList.stream().filter(emp -> Integer.parseInt(emp.getId().substring(0, 4)) == 2023).collect(Collectors.toList());
		   employees.forEach(e -> System.out.println(e.getId()));
		   
		   //Sort employees based on firstName, for same firstName sort by salary.
		   Comparator<Employee> comparator = Comparator.comparing(Employee :: getFirstName).thenComparing(Employee :: getSalary);
		   
		   employees = employeeList.stream().sorted(comparator).collect(Collectors.toList());
		   employees.forEach(e -> System.out.println(e.getFirstName() + "::" + e.getSalary()));
		   
		   //get employee with 3rd highest salary.
		   Comparator<Employee> comparator2 = Comparator.comparing(Employee :: getSalary);
		   Employee employee = employeeList.stream().sorted(comparator2.reversed()).skip(3).findFirst().get();
		   System.out.println("EMployeee::::" + employee.getFirstName() + " ::" + employee.getSalary());
		   
		   //Print names of all employee with 3rd highest salary.
		   employees = employeeList.stream().filter(emp -> emp.getSalary() == employee.getSalary()).collect(Collectors.toList());
		   employees.forEach(e -> System.out.println(e.getFirstName() + "::" + e.getSalary()));
		   
		   //Create a map based on this data, the key should be year of joining and value should be the count of people joined in that particular year.
		   Map<Object, Long> employeeGrouped = employeeList.stream().collect(Collectors.groupingBy(e -> Integer.parseInt(e.getId().substring(0, 4)), Collectors.counting()));
		   for(Object o : employeeGrouped.keySet()) {
			   System.out.println(o.toString() + " :: " + employeeGrouped.get(o));
		   }
		   
		 //Create a map based on this data, they key should be the year of joining, and value should be list of all the employees who joined the particular year.
		   Map<Object, List<Employee>> employeesGrouped = employeeList.stream().collect(Collectors.groupingBy(e -> Integer.parseInt(e.getId().substring(0, 4)), Collectors.toList()));
		   for(Object o : employeesGrouped.keySet()) {
			   System.out.print(o.toString() + " :: ");
			   for(Employee e : employeesGrouped.get(o)) {
				   System.out.print(e.getFirstName() + " " + e.getLastName() + ", ");
			   }
			   System.out.println("\n\n");
		   }
	}
	

}
