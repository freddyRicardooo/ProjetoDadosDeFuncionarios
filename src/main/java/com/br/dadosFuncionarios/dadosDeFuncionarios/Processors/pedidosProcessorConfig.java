package com.br.dadosFuncionarios.dadosDeFuncionarios.Processors;

import com.br.dadosFuncionarios.dadosDeFuncionarios.Dominios.Item;
import com.br.dadosFuncionarios.dadosDeFuncionarios.Dominios.Pedido;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.validator.ValidatingItemProcessor;
import org.springframework.batch.infrastructure.item.validator.ValidationException;
import org.springframework.batch.infrastructure.item.validator.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class pedidosProcessorConfig {

    private Set<String> codigo = new HashSet<>();
    private Set<String> nomePedido = new HashSet<>();
    private List<Pedido> pedidos = new ArrayList<>();

    @Bean
    //verifica se todos os pedidos não tiveram duplicações
    public ItemProcessor<Pedido, Pedido> pedidosProcessor() {
        ValidatingItemProcessor<Pedido> processor = new ValidatingItemProcessor<>();
        processor.setFilter(true);
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
    public ItemProcessor<Item, Item> itemProcessor() {
        ValidatingItemProcessor<Item> processor = new ValidatingItemProcessor<>();
        processor.setFilter(true);
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
    public ItemProcessor<Pedido, Pedido> validacaoEntrega() {
        ValidatingItemProcessor<Pedido> processor = new ValidatingItemProcessor<>();
        processor.setFilter(true);
        processor.setValidator(validateEntrega());
        return processor;

    }

    private Validator<Pedido> validateEntrega() {

        return new  Validator<Pedido>() {
            @Override
            public void validate(Pedido pedido) throws ValidationException {
                if (!"ENTREGUE".equals(pedido.getEstado())) {
                    throw new ValidationException("Os pedidos só seram validados se o status estiver como 'ENTREGUE'");
                }
            }
        };
    }

    public ItemProcessor<Pedido, Pedido> pedidoVerificacaoValor() {
        ValidatingItemProcessor processor = new ValidatingItemProcessor<>();
        processor.setFilter(true);
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