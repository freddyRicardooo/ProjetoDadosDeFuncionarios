package com.br.dadosFuncionarios.dadosDeFuncionarios.Dominios;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    @NotNull
    private String codigoPedido;
    @NotNull
    private String codigoCliente;
    @NotNull
    private String nomeCliente;
    @NotNull
    private LocalDate dataPedido;
    @NotNull
    private LocalTime horaPedido;
    @NotNull
    private String estado;
    @NotNull
    private BigDecimal valorTotal;
    @NotNull
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
