package com.test.student.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.test.student.Student;
import com.test.student.processor.StudentItemProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	public StudentItemProcessor processor() {
		return new StudentItemProcessor();
	}

	@Bean
	public StaxEventItemReader<Student> reader() {
		StaxEventItemReader<Student> reader = new StaxEventItemReader<Student>();
		reader.setResource(new ClassPathResource("student.xml"));
		reader.setFragmentRootElementName("student");
		Map<String, String> alisasMap = new HashMap<String, String>();
		alisasMap.put("student", "com.test.student.Student");
		XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
		xStreamMarshaller.setAliases(alisasMap);
		reader.setUnmarshaller(xStreamMarshaller);
		System.out.println("reader================================:"+reader);
		return reader;
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<Student, Student> chunk(10).reader(reader()).processor(processor())
				.build();
	}

	@Bean
	public Job importUserJob() {
		return jobBuilderFactory.get("importStudentJob").incrementer(new RunIdIncrementer()).flow(step1()).end()
				.build();

	}
}
