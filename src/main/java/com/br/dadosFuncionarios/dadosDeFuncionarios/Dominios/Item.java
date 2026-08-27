package com.br.dadosFuncionarios.dadosDeFuncionarios.Dominios;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    private String codigoPedido;
    private String nomeProduto;
    private Integer quantidade;
    private BigDecimal valorUnitario;

    @Override
    public String toString() {
        return "Item{" +
                "codigoPedido='" + codigoPedido + '\'' +
                ", nomeProduto='" + nomeProduto + '\'' +
                ", quantidade=" + quantidade +
                ", valorUnitario=" + valorUnitario +
                '}';
    }
}
