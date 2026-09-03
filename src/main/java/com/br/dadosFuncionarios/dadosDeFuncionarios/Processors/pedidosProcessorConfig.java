package com.br.dadosFuncionarios.dadosDeFuncionarios.Processors;

import com.br.dadosFuncionarios.dadosDeFuncionarios.Dominios.Item;
import com.br.dadosFuncionarios.dadosDeFuncionarios.Dominios.Pedido;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.infrastructure.item.support.builder.ClassifierCompositeItemProcessorBuilder;
import org.springframework.batch.infrastructure.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.batch.infrastructure.item.validator.ValidatingItemProcessor;
import org.springframework.batch.infrastructure.item.validator.ValidationException;
import org.springframework.batch.infrastructure.item.validator.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class pedidosProcessorConfig {

    private Set<String> codigo = new HashSet<>();
    private Set<String> nomePedido = new HashSet<>();


    @Bean
    public ItemProcessor<Object, Object> processorComposite() throws Exception {
        //agrupa os processor de pedidos
        ItemProcessor<Object, Object> processarPedidos = new CompositeItemProcessorBuilder<>()
                .delegates(pedidoVerificacaoValor(), validacaoEntrega(), pedidosProcessor())
                .build();
        //agrupa os processor de itens
        ItemProcessor<Object, Object> processarItens = new CompositeItemProcessorBuilder<>()
                .delegates(itemProcessorNoDuplicates())
                .build();

        //decide, em tempo de execução, qual dos dois grupos acima deve processar o objeto atual
        ClassifierCompositeItemProcessor<Object, Object> classifier = new ClassifierCompositeItemProcessor<>();
        classifier.setClassifier(objeto -> {
            if (objeto instanceof Item) {
                return processarItens;
            } else if (objeto instanceof Pedido) {
                return processarPedidos;
            }
            // se nao tiver nem pedido nem item nao quebra o job
            return objeto2 -> objeto2;
        });

        return classifier;
    }
    @Bean
    //verifica se todos os pedidos não tiveram duplicações
    public ItemProcessor<Pedido, Pedido> pedidosProcessor() throws Exception {
        ValidatingItemProcessor<Pedido> processor = new ValidatingItemProcessor<>();
        processor.setFilter(true);
        processor.afterPropertiesSet();
        processor.setValidator(validator());
        return processor;
    }

    private Validator<Pedido> validator() {
        return new Validator<Pedido>() {

            @Override
            public void validate(Pedido pedido) throws ValidationException {
                if (codigo.contains(pedido.getCodigoPedido()) || codigo.contains(pedido.getCodigoCliente())) {
                    throw new ValidationException("O codigo do pedido ou do cliente já foi processado");
                }
                codigo.add(pedido.getCodigoPedido());
            }
        };
    }


    //verifica se todos os itens foram processados sem duplicações
    public ItemProcessor<Item, Item> itemProcessorNoDuplicates() throws Exception {
        ValidatingItemProcessor<Item> processor = new ValidatingItemProcessor<>();
        processor.setFilter(true);
        processor.afterPropertiesSet();
        processor.setValidator(validatorItem());
        return processor;
    }

    private Validator<Item> validatorItem() {
        return new Validator<Item>() {

            @Override
            public void validate(Item item) throws ValidationException {
                if (nomePedido.contains(item.getCodigoPedido())){
                    throw new ValidationException("O item já foi processado");
                }
                nomePedido.add(item.getCodigoPedido());
            }
        };
    }

    //Verifica se todos os pedidos tem status como entregue
    public ItemProcessor<Pedido, Pedido> validacaoEntrega() throws Exception {
        ValidatingItemProcessor<Pedido> processor = new ValidatingItemProcessor<>();
        processor.setFilter(true);
        processor.afterPropertiesSet();
        processor.setValidator(validateEntrega());
        return processor;

    }

    private Validator<Pedido> validateEntrega() {

        return new  Validator<Pedido>() {
            @Override
            public void validate(Pedido pedido) throws ValidationException {
                if (!"ENTREGUE".equals(pedido.getStatus())) {
                    throw new ValidationException("Os pedidos só seram validados se o status estiver como 'ENTREGUE'");
                }
            }
        };
    }

    //verifica se o valor é maior que 0
    public ItemProcessor<Pedido, Pedido> pedidoVerificacaoValor() throws Exception {
        ValidatingItemProcessor processor = new ValidatingItemProcessor<>();
        processor.setFilter(true);
        processor.afterPropertiesSet();
        processor.setValidator(validateValor());
        return processor;
    }

    private Validator validateValor() {
        return new Validator<Pedido>() {
            @Override
            public void validate(Pedido pedido) throws ValidationException {

                if (pedido.getValorTotal().compareTo(BigDecimal.ZERO) <= 0){
                    throw new ValidationException("O valor total deve ser maior que zero");
                }
            }
        };
    }
}