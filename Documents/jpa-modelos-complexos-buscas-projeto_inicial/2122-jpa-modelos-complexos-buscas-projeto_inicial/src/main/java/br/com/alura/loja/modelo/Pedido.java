package br.com.alura.loja.modelo;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="valor_total")
    private BigDecimal valorTotal = BigDecimal.ZERO;
    private LocalDate data = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;

    //entidade criada pela união do id das duas tabelas que ela referencia
    //sempre que ha o relacionamento many to many é necessario criar uma nova tabela usando o jing
    //RELACIOMENTO BIDIRECIONAL
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    //o mappedby mapeia os relacionmaento bidireciona, indicando que o relacionamento tem um mapelamento pelo atributo chamdo pedido
    private List<ProdutosPedido> produtosPedido = new ArrayList<>();
//relacionamento demuitos para muitos os dois lados devem ser configurado criando um metodo que faz o vinculo com os dois lados


    public Pedido() {
    }
    public Pedido(Cliente cliente) {
        this.cliente = cliente;
    }

    public void adicionarProdutosPedido(ProdutosPedido produtosPedido){
        produtosPedido.setPedido(this);
        this.produtosPedido.add(produtosPedido);
        this.valorTotal = this.valorTotal.add(produtosPedido.getValor());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorrTotal() {
        return valorTotal;
    }

    public void setValorrTotal(BigDecimal valorrTotal) {
        this.valorTotal = valorrTotal;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setItens(List<ProdutosPedido> itens) {
        this.produtosPedido = itens;
    }

    public void adicionarItem(ProdutosPedido produtosPedido) {
        produtosPedido.setPedido(this);
        this.produtosPedido.add(produtosPedido);
        this.valorTotal = this.valorTotal.add(produtosPedido.getValor());
    }
}
