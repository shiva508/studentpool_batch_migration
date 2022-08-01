package com.pool.config;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.pool.datasourceone.entity.StudentOne;
import com.pool.datasourcetwo.entity.StudentTwo;

@Component
public class StudentItemProcessor implements ItemProcessor<StudentTwo, StudentOne> {

	@Override
	public StudentOne process(StudentTwo item) throws Exception {
		System.out.println(item);
		StudentOne studentOne=new StudentOne();
		studentOne.setStudentId(item.getStudentId());
		studentOne.setFirstName(item.getFirstName());
		studentOne.setLastName(item.getLastName());
		studentOne.setEmail(item.getEmail());
		return studentOne;
	}

}
