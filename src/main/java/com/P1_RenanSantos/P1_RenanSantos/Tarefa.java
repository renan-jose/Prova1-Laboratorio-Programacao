// Pacote pertencente.
package com.P1_RenanSantos.P1_RenanSantos;

// Importações.
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Entity
public class Tarefa{

    // OBS: O BD tem 4 campos (id, titulo, descricao, prazo de conclusão).

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "O título da tarefa é obrigatório!")
    private String titulo;

    private String descricao;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date prazo;

    private boolean concluida;

    // Método que retorna o id da tarefa no BD.
    public int getId(){

        return id;

    }

    // Método que modifica o id da tarefa no BD.
    public void setId(int id){

        this.id = id;

    }

    // Método que retorna o título da tarefa no BD.
    public String getTitulo(){

        return titulo;

    }

    // Método que modifica o título da tarefa no BD.
    public void setTitulo(String titulo){

        this.titulo = titulo;

    }

    // Método que retorna a descrição da tarefa no BD.
    public String getDescricao(){

        return descricao;

    }

    // Método que modifica a descrição da terafa no BD.
    public void setDescricao(String descricao){

        this.descricao = descricao;

    }

    // Método que retorna o prazo de conclusão da tarefa no BD.
    public Date getPrazo(){

        return prazo;

    }

    // Método que modifica o prazo de conclusão da tarefa no BD.
    public void setPrazo(Date prazo){

        this.prazo = prazo;

    }

    // Método que retorna o status da tarefa no BD.
    public boolean getConcluida(){

        return concluida;

    }

    // Método que modifica o status da tarefa no BD.
    public void setConcluida(boolean concluida){

        this.concluida = concluida;

    }

    @Override // Sobrescreve o método equals pois a classe Object o possui também.
    public boolean equals(Object o){

        if(o == null || this.getClass() != o.getClass()){
            return false;
        }

        if(((Tarefa)o).id == (this.id)){
            return true;
        }

        return false;

    }

    @Override
    public int hashCode(){

        return id * 12345;

    }

}
