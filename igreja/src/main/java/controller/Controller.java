package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import model.DAO;
import model.JavaBeans;

@WebServlet(urlPatterns = { "/Controller", "/main", "/insert", "/select", "/update", "/delete", "/relatorio",
        "/relatorioCooperadores" })
public class Controller extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DAO dao = new DAO();

    public Controller() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        System.out.println(action);
        switch (action) {
            case "/main":
                contatos(request, response);
                break;
            case "/insert":
                novoContato(request, response);
                break;
            case "/Controller":
                if (request.getParameter("listagem") != null) {
                    listaCadastro(request, response);
                } else if (request.getParameter("listagemCoperadores") != null) {
                    listaCoperadores(request, response);
                }
                break;
            case "/select":
                editarCadastro(request, response);
                break;
            case "/update":
                salvarEdicao(request, response);
                break;
            case "/relatorio":
                gerarRelatorio(request, response, dao.listarContatos());
                break;
            case "/relatorioCooperadores":
                gerarRelatorio(request, response, dao.listarCooperadores());
                break;
            case "/delete":
                removerCadastro(request, response);
                break;
            default:
                response.sendRedirect("index.html");
        }
    }

    protected void contatos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("central.jsp");
    }

    protected void novoContato(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JavaBeans membro = criarJavaBeans(request);
        dao.inserirContato(membro);
        response.sendRedirect("main");
    }

    protected void listaCadastro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ArrayList<JavaBeans> lista = dao.listarContatos();
        encaminharLista(request, response, lista, "listaPresbitero.jsp");
    }

    protected void listaCoperadores(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ArrayList<JavaBeans> lista = dao.listarCooperadores();
        encaminharLista(request, response, lista, "listaCooperador.jsp");
    }

    protected void editarCadastro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JavaBeans membro = selecionarContato(request);
        encaminharEdicao(request, response, membro, "editar.jsp");
    }

    protected void salvarEdicao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JavaBeans membro = criarJavaBeans(request);
        dao.alterarCadastro(membro);
        response.sendRedirect("main");
    }

    protected void removerCadastro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JavaBeans membro = criarJavaBeans(request);
        dao.deletarCadastro(membro);
        response.sendRedirect("main");
    }

    protected void gerarRelatorio(HttpServletRequest request, HttpServletResponse response,
            ArrayList<JavaBeans> listaMembros) throws ServletException, IOException {
        Document documento = new Document();
        try {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "inline; filename=" + "listagem.pdf");
            PdfWriter.getInstance(documento, response.getOutputStream());
            documento.open();
            documento.add(new Paragraph("Lista de Membros"));
            documento.add(new Paragraph(" "));
            PdfPTable tabela = new PdfPTable(3);
            PdfPCell col1 = new PdfPCell(new Paragraph("Nome"));
            PdfPCell col2 = new PdfPCell(new Paragraph("Cargo"));
            PdfPCell col3 = new PdfPCell(new Paragraph("Dizimista"));
            tabela.addCell(col1);
            tabela.addCell(col2);
            tabela.addCell(col3);
            for (JavaBeans membro : listaMembros) {
                tabela.addCell(membro.getNome());
                tabela.addCell(membro.getCargo());
                tabela.addCell(membro.getDizimista());
            }
            documento.add(tabela);
            documento.close();
        } catch (Exception e) {
            e.printStackTrace();
            documento.close();
        }
    }

    private JavaBeans criarJavaBeans(HttpServletRequest request) {
        JavaBeans membro = new JavaBeans();
        membro.setId_membro(request.getParameter("id_membro"));
        membro.setNome(request.getParameter("nome"));
        membro.setCargo(request.getParameter("cargo"));
        membro.setDizimista(request.getParameter("dizimista"));
        return membro;
    }

    private JavaBeans selecionarContato(HttpServletRequest request) {
        String id_membro = request.getParameter("id_membro");
        JavaBeans membro = new JavaBeans();
        membro.setId_membro(id_membro);
        dao.selecionarContato(membro);
        return membro;
    }

    private void encaminharLista(HttpServletRequest request, HttpServletResponse response, ArrayList<JavaBeans> lista,
            String pagina) throws ServletException, IOException {
        request.setAttribute("presbiteros", lista);
        RequestDispatcher rd = request.getRequestDispatcher(pagina);
        rd.forward(request, response);
    }

    private void encaminharEdicao(HttpServletRequest request, HttpServletResponse response, JavaBeans membro,
            String pagina) throws ServletException, IOException {
        request.setAttribute("id_membro", membro.getId_membro());
        request.setAttribute("nome", membro.getNome());
        request.setAttribute("cargo", membro.getCargo());
        request.setAttribute("dizimista", membro.getDizimista());
        RequestDispatcher rd = request.getRequestDispatcher(pagina);
        rd.forward(request, response);
    }
}
