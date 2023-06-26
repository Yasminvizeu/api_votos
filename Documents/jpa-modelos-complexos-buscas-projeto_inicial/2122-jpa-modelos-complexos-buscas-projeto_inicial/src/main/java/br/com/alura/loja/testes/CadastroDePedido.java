package br.com.alura.loja.testes;

import br.com.alura.loja.dao.CategoriaDao;
import br.com.alura.loja.dao.ClienteDao;
import br.com.alura.loja.dao.PedidoDao;
import br.com.alura.loja.dao.ProdutoDao;
import br.com.alura.loja.modelo.*;
import br.com.alura.loja.util.JPAUtil;
import br.com.alura.loja.vo.RelatorioDeVendasVo;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

public class CadastroDePedido {

    public static void main(String[] args) {
        popularBancoDeDados();
        EntityManager em = JPAUtil.getEntityManager();
        ProdutoDao produtoDao = new ProdutoDao(em);
        ClienteDao clienteDao = new ClienteDao(em);
        Produto produto = produtoDao.buscarPorId(1L);
        Produto produto2 = produtoDao.buscarPorId(2L);
        Produto produto3 = produtoDao.buscarPorId(3L);
        Cliente cliente = clienteDao.buscarPorId(1L);

        em.getTransaction().begin();

        Pedido pedido = new Pedido(cliente);
        pedido.adicionarProdutosPedido(new ProdutosPedido(10, pedido, produto));
        pedido.adicionarProdutosPedido(new ProdutosPedido(40, pedido, produto2));

        Pedido pedido2 = new Pedido(cliente);
        pedido.adicionarProdutosPedido(new ProdutosPedido(10, pedido, produto3));

        PedidoDao pedidoDao = new PedidoDao(em);
        pedidoDao.cadastrar(pedido);

        em.getTransaction().commit();


        BigDecimal totalVendido = pedidoDao.valorTotalVendido();
        System.out.println("valor total: "+ totalVendido);

        List<RelatorioDeVendasVo> relatorio = pedidoDao.relatorioDeVendas();
        relatorio.forEach(System.out::println);
    }

    private static void popularBancoDeDados() {
        Categoria celulares = new Categoria("CELULARES");
        Categoria videogames = new Categoria("VIDEOGAME");
        Categoria informatica = new Categoria("INFORMATICA");


        Produto celular = new Produto("Xiaomi Redmi", "Muito legal", new BigDecimal("800"), celulares );
        Produto videogame = new Produto("playstation 5 ", "Muito legal", new BigDecimal("8000"), videogames );
        Produto macbook = new Produto("macbook", "Muito legal", new BigDecimal("80000"), informatica );


        Cliente cliente = new Cliente("Rodrigo", "1231231231");


        EntityManager em = JPAUtil.getEntityManager();
        ProdutoDao produtoDao = new ProdutoDao(em);
        CategoriaDao categoriaDao = new CategoriaDao(em);
        ClienteDao clienteDao = new ClienteDao(em);

        em.getTransaction().begin();

        categoriaDao.cadastrar(celulares);
        categoriaDao.cadastrar(videogames);
        categoriaDao.cadastrar(informatica);

        produtoDao.cadastrar(celular);
        produtoDao.cadastrar(videogame);
        produtoDao.cadastrar(macbook);

        clienteDao.cadastrar(cliente);

        em.getTransaction().commit();
        em.close();
    }
}
