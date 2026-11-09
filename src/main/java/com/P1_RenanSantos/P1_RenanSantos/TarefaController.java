/* TarefaController é responsável por receber as diferentes requisições HTTP, como
* GET, POST, PUT e DELETE. Em outras palavras, ele vai interpretar a ação do usuário. */

// Pacote pertencente.
package com.P1_RenanSantos.P1_RenanSantos;

// Importações.
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Date;

@Controller
public class TarefaController{

    // Declaração das sessões.
    private static final String SESSION_STATUS = "sessionStatus";
    private static final String SESSION_PRAZO = "sessionPrazo";
    private static final String SESSION_PALAVRACHAVE = "sessionPalavra";

    @Autowired
    TarefaRepository tarefaRepository;

    // Tratamento da requisição GET.

    // Listagem

    @GetMapping(value={"/index", "/"})
    public String listarTarefas(Model model){

        // Adiciona todas as tarefas do BD ao objeto model.
        model.addAttribute("tarefa", tarefaRepository.findAll());

        // Retorna uma "renderização" de index.
        return "index";

    }

    // OBS: request é um objeto também.

    @GetMapping("/status")
    public String listarTarefaStatus(Model model, HttpServletRequest request){

        // Tenta recuperar o filtro de status da sessão.
        Boolean status = (Boolean)request.getSession().getAttribute(SESSION_STATUS);

        // Se o filtro for não-nulo (há filtro),
        if(status != null){
            // Se o filtro tem valor true,
            if(status.equals(true)){
                // Serão adicionas ao objeto model todas as tarefas do BD com status concluída.
                model.addAttribute("tarefa", tarefaRepository.findByConcluidaTrue());
            // Senão,
            }else{
                // Serão adicionas ao objeto model todas as tarefas do BD com status não-concluída.
                model.addAttribute("tarefa", tarefaRepository.findByConcluidaFalse());
            }
        // Senão,
        }else{
            // Serão adicionadas todas as tarefas do BD ao objeto model.
            model.addAttribute("tarefa", tarefaRepository.findAll());
        }

        // Renderiza a view status.
        return "status";

    }

    @GetMapping("/prazo/listar")
    public String listarTarefaPrazo(Model model, HttpServletRequest request){

        // Tenta recuperar o filtro de prazo da sessão.
        Date prazo = (Date)request.getSession().getAttribute(SESSION_PRAZO);

        // Se o filtro for não-nulo (há filtro),
        if(prazo != null){
            // Serão adicionas todas as tarefas (com prazo específico) do BD ao objeto model.
            model.addAttribute("tarefa", tarefaRepository.findByPrazo(prazo));
        // Senão,
        }else{
            // Serão adicionadas todas as tarefas do BD ao objeto model.
            model.addAttribute("tarefa", tarefaRepository.findAll());
        }

        // Renderiza a view prazo.
        return "prazo";

    }

    @GetMapping("/palavra-chave/listar")
    public String listarTarefaPalavraChave(Model model, HttpServletRequest request){

        // Tenta recuperar o filtro de palavra-chave da sessão.
        String palavra = (String)request.getSession().getAttribute(SESSION_PALAVRACHAVE);

        // Se o filtro for não-nulo (há filtro) e a palavra não é vazia,
        if(palavra != null && palavra.isEmpty() == false){
            // Serão adicionas todas as tarefas (que contém a palavra-chave no título ou na descrição) do BD ao objeto model.
            model.addAttribute("tarefa", tarefaRepository.findByTituloContainingIgnoreCaseOrDescricaoContainingIgnoreCase(palavra, palavra));
        // Senão,
        }else{
            // Serão adicionadas todas as tarefas do BD ao objeto model.
            model.addAttribute("tarefa", tarefaRepository.findAll());
        }

        // Renderiza a view palavra-chave.
        return "palavra-chave";

    }

    ////////////////////////////////////////////////////////////////////////

    // Filtragem.

    @GetMapping("/status/{status}")
    public String filtrarStatusTarefa(@PathVariable("status") String status, HttpServletRequest request){

        Boolean concluida;

        if("concluidas".equalsIgnoreCase(status)){
            concluida = true;
        }else if("nao-concluidas".equalsIgnoreCase(status)){
            concluida = false;
        }else{
            request.getSession().removeAttribute(SESSION_STATUS);
            return "redirect:/status";
        }

        // Salva o filtro de status temporariamente na sessão.
        request.getSession().setAttribute(SESSION_STATUS, concluida);

        // Redireciona para o método que lista as tarefas filtradas por status.
        return "redirect:/status";

    }

    @GetMapping("/prazo")
    public String filtrarPrazoTarefa(@RequestParam("prazo") @DateTimeFormat(pattern = "yyyy-MM-dd") Date prazo, HttpServletRequest request){

        // Salva o filtro de prazo temporariamente na sessão.
        request.getSession().setAttribute(SESSION_PRAZO, prazo);

        // Redireciona para o método que lista as tarefas filtradas por prazo.
        return "redirect:/prazo/listar";

    }

    @GetMapping("/palavra-chave")
    public String filtrarPalavraChaveTarefa(@RequestParam("palavra") String palavra, HttpServletRequest request){

        // Salva o filtro de palavra-chave temporariamente na sessão.
        request.getSession().setAttribute(SESSION_PALAVRACHAVE, palavra);

        // Redireciona para o método que lista as tarefas filtradas por palavra-chave.
        return "redirect:/palavra-chave/listar";

    }

    ////////////////////////////////////////////////////////////////////////

    @GetMapping("/nova-tarefa")
    public String mostrarFormularioNovaTarefa(Tarefa tarefa){

        // Renderiza a view nova-tarefa.
        return "nova-tarefa";

    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioAtualizarTarefa(@PathVariable("id") int id, Model model){

        Tarefa tarefa = tarefaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("O id da tarefa é inválido:" + id));

        // Adiciona todas as tarefas do BD ao objeto model.
        model.addAttribute("tarefa", tarefa);

        // Rendereiza a view atualizar-tarefa.
        return "atualizar-tarefa";

    }

    // Tratamento da requisição POST.

    @PostMapping("/adicionar-tarefa")
    public String adicionarTarefa(@Valid Tarefa tarefa, BindingResult result){

        if(result.hasErrors()){
            return "/nova-tarefa";
        }

        tarefaRepository.save(tarefa);

        // Redireciona para o método que lista todas as tarefas.
        return "redirect:/index";

    }

    // Tratamento da requisição PUT.

    @PostMapping("/atualizar/{id}")
    public String atualizarTarefa(@PathVariable("id") int id, @Valid Tarefa tarefa,BindingResult result, Model model){

        if(result.hasErrors()){
            tarefa.setId(id);
            return "atualizar-tarefa";
        }

        tarefaRepository.save(tarefa);

        // Redireciona para o método que lista todas as tarefas.
        return "redirect:/index";

    }

    // Tratamento da requisição DELETE.

    @GetMapping("/remover/{id}")
    public String removerTarefa(@PathVariable("id") int id){

        Tarefa tarefa = tarefaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("O id da tarefa é inválido:" + id));

        tarefaRepository.delete(tarefa);

        // Redireciona para o método que lista todas as tarefas.
        return "redirect:/index";

    }

}
