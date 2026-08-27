package com.br.dadosFuncionarios.dadosDeFuncionarios.Dominios;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    private String codigoPedido;
    private String codigoCliente;
    private String nomeCliente;
    private LocalDate dataPedido;
    private LocalTime horaPedido;
    private String estado;
    private BigDecimal valorTotal;
    private String status;

    @Override
    public String toString() {
        return "Pedido{" +
                "codigoPedido='" + codigoPedido + '\'' +
                ", codigoCliente='" + codigoCliente + '\'' +
                ", nomeCliente='" + nomeCliente + '\'' +
                ", dataPedido=" + dataPedido +
                ", horaPedido=" + horaPedido +
                ", estado='" + estado + '\'' +
                ", valorTotal=" + valorTotal +
                ", status='" + status + '\'' +
                '}';
    }
}
