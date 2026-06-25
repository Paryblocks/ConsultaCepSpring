package com.trabalho.viacep.service;

import com.trabalho.viacep.model.Cep;
import com.trabalho.viacep.model.Usuario;
import com.trabalho.viacep.repository.CepRepository;
import com.trabalho.viacep.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.Optional;
import java.util.Scanner;

@Service
public class CepService {

    @Autowired
    private CepRepository cepRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Cep buscar(String cep, Long usuarioId) {

        try {
            URL url = new URL("https://viacep.com.br/ws/" + cep + "/json/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner sc = new Scanner(conn.getInputStream(), "UTF-8");
            String json = "";

            while (sc.hasNext())
                json += sc.nextLine();
            sc.close();

            if (json.contains("\"erro\"")) {
                return null;
            }

            String cidade = extrair(json, "\"localidade\": \"");
            String estado = extrair(json, "\"estado\": \"");
            String logradouro = extrair(json, "\"logradouro\": \"");
            String complemento = extrair(json, "\"complemento\": \"");
            String bairro = extrair(json, "\"bairro\": \"");
            String UF = extrair(json, "\"uf\": \"");
            String regiao = extrair(json, "\"regiao\": \"");
            String DDD = extrair(json, "\"ddd\": \"");

            Cep novaPesquisa = new Cep(cep, cidade, estado, logradouro, complemento, bairro, UF, regiao, DDD,
                    Timestamp.from(Instant.now()));

            if (usuarioId != null) {
                Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);

                if (usuarioOpt.isPresent()) {
                    Usuario usuario = usuarioOpt.get();
                    novaPesquisa.setUsuario(usuario);

                    Optional<Cep> cepExistente = cepRepository.findByCepAndUsuarioId(cep, usuarioId);

                    if (cepExistente.isPresent()) {
                        Cep antigo = cepExistente.get();
                        antigo.setDataConsulta(Timestamp.from(Instant.now()));
                        return cepRepository.save(antigo);
                    } else {
                        return cepRepository.save(novaPesquisa);
                    }
                }
            }

            System.out.println("DEBUG: Ignorou o salvamento e retornou direto.");
            return novaPesquisa;

        } catch (Exception e) {
            return null;
        }
    }

    public List<Cep> buscarPorEndereco(String uf, String cidade, String logradouro, Long usuarioId) {

        List<Cep> lista = new ArrayList<>();
        try {
            cidade = cidade.replace(" ", "%20");
            logradouro = logradouro.replace(" ", "%20");
            URL url = new URL("https://viacep.com.br/ws/" + uf + "/" + cidade + "/" + logradouro + "/json/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner sc = new Scanner(conn.getInputStream(), "UTF-8");
            String json = "";

            while (sc.hasNext())
                json += sc.nextLine();
            sc.close();

            json = json.substring(1, json.length() - 1);

            String[] objetos = json.split("\\}\\s*,\\s*\\{");

            Usuario usuarioLogado = null;
            if (usuarioId != null) {
                usuarioLogado = usuarioRepository.findById(usuarioId).orElse(null);
            }

            for (String obj : objetos) {

                String cepDaLista = extrair(obj, "\"cep\": \"").replace("-", "");
                String estado = extrair(obj, "\"estado\": \"");
                String logradouroRetornado = extrair(obj, "\"logradouro\": \"");
                String complemento = extrair(obj, "\"complemento\": \"");
                String bairro = extrair(obj, "\"bairro\": \"");
                String regiao = extrair(obj, "\"regiao\": \"");
                String DDD = extrair(obj, "\"ddd\": \"");

                String erroStr = extrair(obj, "\"erro\": \"");
                boolean erro = erroStr != null && erroStr.equals("true");

                if (erro) {
                    return null;
                } else {
                    if (!logradouroRetornado.equals("N/A")) {
                        Cep c = new Cep(cepDaLista, cidade, estado, logradouroRetornado, complemento, bairro, uf,
                                regiao, DDD, Timestamp.from(Instant.now()));
                        
                        if (usuarioLogado != null) {
                            c.setUsuario(usuarioLogado);

                            Optional<Cep> cepExistente = cepRepository.findByCepAndUsuarioId(cepDaLista, usuarioId);

                            if (cepExistente.isPresent()) {
                                Cep antigo = cepExistente.get();
                                antigo.setDataConsulta(Timestamp.from(Instant.now()));
                                cepRepository.save(antigo);
                            } else {
                                cepRepository.save(c);
                            }
                        }
                        lista.add(c);
                    } else {
                        return null;
                    }
                }
            }

            return lista;
        } catch (Exception e) {
            return null;
        }
    }

    public int obterTotalAcessos(Long usuarioId) {
        return cepRepository.contarTotalAcessosPorUsuarioId(usuarioId);
    }

    public Cep obterCepMaisConsultadoGlobal() {
        Optional<String> cepOpt = cepRepository.findCepMaisConsultadoGlobal();
        return cepOpt.map(cep -> buscar(cep, null)).orElse(null);
    }

    public List<String> obterTop15MaisPesquisadosDoUsuario(Long usuarioId) {
        return cepRepository.findTop15CepsMaisPesquisados(usuarioId);
    }

    public List<Cep> mostrarHistorico(Long usuarioId) {
        return cepRepository.findByUsuarioIdOrderByDataConsultaDesc(usuarioId);
    }

    @Transactional
    public void limparHistoricoUsuario(Long usuarioId) {
        cepRepository.deleteByUsuarioId(usuarioId);
    }

    private String extrair(String json, String chave) {
        try {
            int i = json.indexOf(chave) + chave.length();
            return json.substring(i, json.indexOf("\"", i));
        } catch (Exception e) {
            return "N/A";
        }
    }

    public ByteArrayInputStream gerarRelatorioCsv(Long usuarioId) {

        List<Cep> consultas = cepRepository.findByUsuarioIdOrderByDataConsultaDesc(usuarioId);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        writer.println("CEP,Cidade,Estado,Logradouro,Data Consulta");

        for (Cep c : consultas) {
            writer.printf("%s,%s,%s,%s,%s%n",
                    c.getCep(),
                    c.getCidade(),
                    c.getEstado(),
                    c.getLogradouro(),
                    c.getDataConsulta());
        }

        writer.flush();

        return new ByteArrayInputStream(out.toByteArray());
    }
}