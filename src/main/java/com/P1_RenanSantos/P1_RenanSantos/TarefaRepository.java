/* De forma sucinta, TarefaRepository fica encarregado de se comunicar com o banco de dados.
* É uma interface que contém métodos que interagem com esse banco, sendo estabelecido, então, o CRUD. */

// Pacote pertencente.
package com.P1_RenanSantos.P1_RenanSantos;

// Importações.
import org.springframework.data.repository.CrudRepository;
import java.util.Date;
import java.util.List;

// A interface TarefaRepository herda métodos da interface CrudRepository.
public interface TarefaRepository extends CrudRepository<Tarefa, Integer>{

    /* Para o seguinte trabalho, foi necessário a criação de quatro métodos para o uso de filtros. */

    // Método que encontra e retorna todas as tarefas concuídas.
    List<Tarefa> findByConcluidaTrue();

    // Método que encontra e retorna todas as tarefas não concluídas.
    List<Tarefa> findByConcluidaFalse();

    // Método que encontra e retorna todas as tarefas com prazo espececífico.
    List<Tarefa> findByPrazo(Date prazo);

    // Método que encontra e retorna uma palavra específica na tarefa, seja em seu título ou sua descrição.
    List<Tarefa> findByTituloContainingIgnoreCaseOrDescricaoContainingIgnoreCase(String titulo, String descricao);

}
