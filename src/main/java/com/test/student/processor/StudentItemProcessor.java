package com.test.student.processor;

import org.springframework.batch.item.ItemProcessor;

import com.test.student.Student;

public class StudentItemProcessor implements ItemProcessor<Student, Student> {
	
	
	@Override
	public Student process(Student student) throws Exception {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>"+student.getName());
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>"+student.getStudentId());

		return student;
	}

}
