package com.pdv.sistemapdv.service;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import com.pdv.sistemapdv.dto.*;
import com.pdv.sistemapdv.exception.EstoqueInsuficienteException;
import com.pdv.sistemapdv.exception.ProdutoNaoEncontradoException;
import com.pdv.sistemapdv.exception.FormaPagamentoInvalidaException;
import com.itextpdf.kernel.pdf.PdfDocument;

import com.pdv.sistemapdv.dto.VendaRequestDTO;
import com.pdv.sistemapdv.model.FormaPagamento;
import com.pdv.sistemapdv.model.ItemVenda;
import com.pdv.sistemapdv.model.Produto;
import com.pdv.sistemapdv.model.Venda;
import com.pdv.sistemapdv.repository.FormaPagamentoRepository;
import com.pdv.sistemapdv.repository.ProdutoRepository;
import com.pdv.sistemapdv.repository.VendaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VendaService {
    private final ProdutoRepository produtoRepository;
    private final VendaRepository vendaRepository;
    private final FormaPagamentoRepository formaPagamentoRepository;

    @Transactional
    public VendaResponseDTO registrarVenda(VendaRequestDTO dto) {
        log.info("Registrando venda com {} itens", dto.getItens().size());

        FormaPagamento formaPagamento = formaPagamentoRepository.findById(dto.getFormaPagamentoId())
                .orElseThrow(() -> new FormaPagamentoInvalidaException(dto.getFormaPagamentoId()));

        Venda venda = new Venda();
        venda.setDataHora(LocalDateTime.now());
        venda.setFormaPagamento(formaPagamento);

        List<ItemVenda> itens = processarItensVenda(dto.getItens(), venda);
        venda.setItens(itens);
        venda.setTotal(calcularTotal(itens));

        Venda vendaSalva = vendaRepository.save(venda);
        return mapToResponseDTO(vendaSalva);
    }

    private List<ItemVenda> processarItensVenda(List<ItemVendaDTO> itensDTO, Venda venda) {
        return itensDTO.stream()
                .map(itemDTO -> {
                    Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                            .orElseThrow(() -> new ProdutoNaoEncontradoException(itemDTO.getProdutoId()));

                    validarEstoque(produto, itemDTO.getQuantidade());
                    atualizarEstoque(produto, itemDTO.getQuantidade());

                    return criarItemVenda(itemDTO, produto, venda);
                })
                .collect(Collectors.toList());
    }

    private void validarEstoque(Produto produto, int quantidade) {
        if (produto.getQuantidadeEstoque() < quantidade) {
            throw new EstoqueInsuficienteException(produto.getNome());
        }
    }

    private void atualizarEstoque(Produto produto, int quantidadeVendida) {
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidadeVendida);
        produtoRepository.save(produto);
    }

    private ItemVenda criarItemVenda(ItemVendaDTO itemDTO, Produto produto, Venda venda) {
        BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(itemDTO.getQuantidade()));

        return ItemVenda.builder()
                .produto(produto)
                .quantidade(itemDTO.getQuantidade())
                .precoUnitario(produto.getPreco())
                .subtotal(subtotal)
                .venda(venda)
                .build();
    }

    private BigDecimal calcularTotal(List<ItemVenda> itens) {
        return itens.stream()
                .map(ItemVenda::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private VendaResponseDTO mapToResponseDTO(Venda venda) {
        return new VendaResponseDTO(
                venda.getId(),
                venda.getDataHora(),
                venda.getTotal(),
                venda.getFormaPagamento().getDescricao(),
                mapItensToDTO(venda.getItens())
        );
    }

    private List<ItemVendaResponseDTO> mapItensToDTO(List<ItemVenda> itens) {
        return itens.stream()
                .map(item -> new ItemVendaResponseDTO(
                        item.getProduto().getId(),
                        item.getProduto().getNome(),
                        item.getQuantidade(),
                        item.getPrecoUnitario(),
                        item.getSubtotal()
                ))
                .collect(Collectors.toList());
    }
    public List<RelatorioVendaDTO> gerarRelatorio(LocalDate dataInicio, LocalDate dataFim) {
        List<Venda> vendas = vendaRepository.findAll().stream()
                .filter(v -> {
                    LocalDate dataVenda = v.getDataHora().toLocalDate();
                    return (dataInicio == null || !dataVenda.isBefore(dataInicio)) &&
                            (dataFim == null || !dataVenda.isAfter(dataFim));
                })
                .collect(Collectors.toList());

        return vendas.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getDataHora().toLocalDate(),
                        Collectors.collectingAndThen(Collectors.toList(), lista -> {
                            BigDecimal total = lista.stream()
                                    .map(Venda::getTotal)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                            return new RelatorioVendaDTO(lista.get(0).getDataHora().toLocalDate(), total, lista.size());
                        })
                ))
                .values()
                .stream()
                .sorted(Comparator.comparing(RelatorioVendaDTO::getData))
                .toList();
    }

    public List<ProdutoMaisVendidoDTO> relatorioProdutosMaisVendidos(LocalDate dataInicio, LocalDate dataFim) {
        List<Venda> vendas = vendaRepository.findAll().stream()
                .filter(v -> {
                    LocalDate dataVenda = v.getDataHora().toLocalDate();
                    return (dataInicio == null || !dataVenda.isBefore(dataInicio)) &&
                            (dataFim == null || !dataVenda.isAfter(dataFim));
                })
                .collect(Collectors.toList());

        List<ItemVenda> itensVendidos = vendas.stream()
                .flatMap(venda -> venda.getItens().stream())
                .collect(Collectors.toList());

        return itensVendidos.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getProduto().getNome(),
                        Collectors.collectingAndThen(Collectors.toList(), lista -> {
                            int totalQuantidade = lista.stream().mapToInt(ItemVenda::getQuantidade).sum();
                            BigDecimal totalVendido = lista.stream().map(ItemVenda::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
                            return new ProdutoMaisVendidoDTO(
                                    lista.get(0).getProduto().getNome(),
                                    totalQuantidade,
                                    totalVendido
                            );
                        })
                ))
                .values()
                .stream()
                .sorted(Comparator.comparing(ProdutoMaisVendidoDTO::getTotalVendido).reversed())
                .collect(Collectors.toList());
    }

    public byte[] gerarRelatorioProdutosPDF(LocalDate dataInicio, LocalDate dataFim) {
        List<ProdutoMaisVendidoDTO> produtos = relatorioProdutosMaisVendidos(dataInicio, dataFim);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf);

        document.add(new Paragraph("Relatório de Produtos Mais Vendidos"));
        document.add(new Paragraph(" ")); // espaço

        Table table = new Table(UnitValue.createPercentArray(new float[]{40, 30, 30}));
        table.addHeaderCell("Produto");
        table.addHeaderCell("Quantidade");
        table.addHeaderCell("Total");

        for (ProdutoMaisVendidoDTO dto : produtos) {
            table.addCell(dto.getNomeProduto());
            table.addCell(String.valueOf(dto.getQuantidadeVendida()));
            table.addCell("R$ " + dto.getTotalVendido());
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }

    public FechamentoCaixaDTO gerarFechamentoCaixa(LocalDate dataInicio, LocalDate dataFim) {
        // Filtra as vendas no período definido
        List<Venda> vendas = vendaRepository.findAll().stream()
                .filter(v -> {
                    LocalDate dataVenda = v.getDataHora().toLocalDate();
                    return (dataInicio == null || !dataVenda.isBefore(dataInicio)) &&
                            (dataFim == null || !dataVenda.isAfter(dataFim));
                })
                .collect(Collectors.toList());

        // Calcula o total de vendas
        BigDecimal totalVendas = vendas.stream()
                .map(Venda::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Suponhamos que a entrada e saída sejam baseadas em alguma entidade/operacao no sistema
        BigDecimal totalEntrada = BigDecimal.valueOf(1000);  // Exemplo de entrada fixa, pode ser ajustado
        BigDecimal totalSaida = BigDecimal.valueOf(500);     // Exemplo de saída fixa, pode ser ajustado

        // Calcula o saldo final
        BigDecimal saldoFinal = totalVendas.add(totalEntrada).subtract(totalSaida);

        return new FechamentoCaixaDTO(totalVendas, totalEntrada, totalSaida, saldoFinal);
    }
    // src/main/java/com/pdv/sistemapdv/service/VendaService.java
    public byte[] gerarRelatorioFechamentoCaixaPDF(LocalDate dataInicio, LocalDate dataFim) {
        FechamentoCaixaDTO fechamentoCaixa = gerarFechamentoCaixa(dataInicio, dataFim);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Relatório de Fechamento de Caixa"));
        document.add(new Paragraph("Período: " + dataInicio + " a " + dataFim));
        document.add(new Paragraph(" ")); // espaço

        Table table = new Table(UnitValue.createPercentArray(new float[]{50, 50}));
        table.addHeaderCell("Descrição");
        table.addHeaderCell("Valor");

        table.addCell("Total Vendas");
        table.addCell("R$ " + fechamentoCaixa.getTotalVendas());
        table.addCell("Total Entrada");
        table.addCell("R$ " + fechamentoCaixa.getTotalEntrada());
        table.addCell("Total Saída");
        table.addCell("R$ " + fechamentoCaixa.getTotalSaida());
        table.addCell("Saldo Final");
        table.addCell("R$ " + fechamentoCaixa.getSaldoFinal());

        document.add(table);
        document.close();

        return out.toByteArray();
    }


}