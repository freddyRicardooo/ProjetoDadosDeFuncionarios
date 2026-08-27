package com.br.dadosFuncionarios.dadosDeFuncionarios.Jobs;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobPedidosConfig {
    @Bean
    public Job JobPedidos(JobRepository jobRepository, Step StepPedidos){
        return new JobBuilder("Jobpedidos", jobRepository)
                .start(StepPedidos)
                .listener()
                .build();
    }
}
