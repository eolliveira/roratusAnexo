package br.ind.ajrorato.gateway.database.entity.anexo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(value = "LICENCA_SOFTWARE")
public class LicencaAnexoEntity extends AnexoEntity {}