package com.br.dadosFuncionarios.dadosDeFuncionarios.Reader;

import com.br.dadosFuncionarios.dadosDeFuncionarios.Dominios.Item;
import com.br.dadosFuncionarios.dadosDeFuncionarios.Dominios.Pedido;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.infrastructure.item.file.mapping.FieldSetMapper;
import org.springframework.batch.infrastructure.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.infrastructure.item.file.transform.LineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class PedidoItemLineMapperConfig {
    @Bean
    //Pattern serve para coordenar e chamar as instancias de tokinizers e mappers
    public PatternMatchingCompositeLineMapper<Object> lineMapper() {
        PatternMatchingCompositeLineMapper<Object> lineMapper = new PatternMatchingCompositeLineMapper<>(tokenizers(), fieldSetMappers());
        lineMapper.setTokenizers(tokenizers());
        lineMapper.setFieldSetMappers(fieldSetMappers());
        return lineMapper;
    }

    //associa cada padrão de prefixo ao seu FielSetMapper correspondente
    private Map<String, FieldSetMapper<Object>> fieldSetMappers() {
        Map<String, FieldSetMapper<Object>> fieldSetMappers = new HashMap<>();
        fieldSetMappers.put("PEDIDO*", fieldSetMapper(Pedido.class));
        fieldSetMappers.put("ITEM*", fieldSetMapper(Item.class));
        return fieldSetMappers;
    }

    /* configura o target type de classe para Pedido.class ou Item.class
    * o BeanWrapperFieldSetMapper significa que ele nao sabe qual classe ele vai usar pra fazer o reflection
    * (saber a instancia da classe em tempo de execução)
    * */
    private FieldSetMapper <Object> fieldSetMapper(Class <?> classe) {
        BeanWrapperFieldSetMapper <Object> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(classe);
        return fieldSetMapper;
    }

    //associa cada padrão de prefixo ao seu LineTokenizer
    private Map<String, LineTokenizer> tokenizers() {
        Map<String, LineTokenizer> tokenizers = new HashMap<>();
        tokenizers.put("PEDIDO*", pedidoLineTokenizer());
        tokenizers.put("ITEM*", itemLineTokenizer());
        return tokenizers;
    }

    /*aqui eu delimito oq ele vai quebrar como token
    *
    * pro java uma linha de pedidos é uma string e o delimited tem o papel de pegar oq esta escrito
    * apos cada ; e coloca em um token
    * */
    private LineTokenizer pedidoLineTokenizer() {
        DelimitedLineTokenizer pedidoLineTokenizer = new DelimitedLineTokenizer();
        pedidoLineTokenizer.setNames(
                "codigoPedido", "codigoCliente", "nomeCliente", "dataPedido", "horaPedido", "estado", "valorTotal", "status");
        pedidoLineTokenizer.setIncludedFields(1, 2, 3, 4, 5, 6, 7, 8);
        return pedidoLineTokenizer;

    }


    private LineTokenizer itemLineTokenizer() {
        DelimitedLineTokenizer pedidoLineTokenizer = new DelimitedLineTokenizer();
        pedidoLineTokenizer.setNames(
                "codigoPedido", "nomeProduto", "quantidade", "valorUnitario");
        pedidoLineTokenizer.setIncludedFields(1, 2, 3, 4);
        return pedidoLineTokenizer;

    }


}
