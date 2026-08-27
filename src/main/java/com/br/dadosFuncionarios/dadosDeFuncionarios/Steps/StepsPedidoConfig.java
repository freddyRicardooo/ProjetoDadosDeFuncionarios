package com.br.dadosFuncionarios.dadosDeFuncionarios.Steps;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StepsPedidoConfig {
    @Bean
    public Step stepPedido(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemReader pedidosReader) {
        return new StepBuilder("stepPedidos", jobRepository )
                .<Object, Object>chunk(200).transactionManager(transactionManager)
                .reader(pedidosReader)
                .processor(pedidosProcessor)
                .writer(pedidosWritter)
                .build();


    }
}
