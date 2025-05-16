package br.ind.ajrorato.gateway.database.entity.anexo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(value = "TAREFA_HELPDESK")
public class TarefaAnexoEntity extends AnexoEntity {}