package com.br.dadosFuncionarios.dadosDeFuncionarios.Reader;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class PedidosReaderConfig {
    @StepScope
    @Bean
    public FlatFileItemReader pedidosReader(
            @Value("#{jobParameters['arquivoPedidos']}") Resource arquivoPedidos, LineMapper lineMapper
    ){
        return new FlatFileItemReaderBuilder()
                .name("pedidosItemReader")
                .resource(arquivoPedidos)
                .lineMapper(lineMapper)
                .build();
    }
}
