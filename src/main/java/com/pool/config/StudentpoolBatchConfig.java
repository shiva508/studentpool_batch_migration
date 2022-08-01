package com.pool.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;

import com.pool.datasourceone.entity.StudentOne;
import com.pool.datasourcetwo.entity.StudentTwo;

@Configuration
@EnableBatchProcessing
public class StudentpoolBatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	@Qualifier("datasource")
	private DataSource datasource;

	@Autowired
	@Qualifier("datasourcetwo")
	private DataSource datasourcetwo;

	@Autowired
	@Qualifier("datasourceOneEntityManagerFactory")
	private EntityManagerFactory datasourceOneEntityManagerFactory;

	@Autowired
	@Qualifier("datasourceTwoEntityManagerFactory")
	private EntityManagerFactory datasourceTwoEntityManagerFactory;
	
	@Autowired
	private StudentItemProcessor studentItemProcessor;
	
	@Autowired
	private JpaTransactionManager jpaTransactionManager;

	@Bean
	public Job jpaBatchJob() {
		return jobBuilderFactory.get("Jpa Batch Job")
				.incrementer(new RunIdIncrementer())
				.start(jpaBatchStep())
				.build();
	}

	
	public Step jpaBatchStep() {
		return stepBuilderFactory
								.get("Jpa Batch Step")
								.<StudentTwo,StudentOne>chunk(2)
								.reader(jpaCursorItemReader())
								.processor(studentItemProcessor)
								.writer(jpaItemWriter())
								.transactionManager(jpaTransactionManager)
								.build();
	}

	public JpaCursorItemReader<StudentTwo> jpaCursorItemReader() {
		JpaCursorItemReader<StudentTwo> jpaCursorItemReader = new JpaCursorItemReader<>();
		jpaCursorItemReader.setEntityManagerFactory(datasourceTwoEntityManagerFactory);
		jpaCursorItemReader.setQueryString("FROM StudentTwo");
		return jpaCursorItemReader;
	}

	public JpaItemWriter<StudentOne> jpaItemWriter() {
		JpaItemWriter<StudentOne> jpaItemWriter=new JpaItemWriter<>();
		jpaItemWriter.setEntityManagerFactory(datasourceOneEntityManagerFactory);
		return jpaItemWriter;
	}
}
