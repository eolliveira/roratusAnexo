package br.ind.ajrorato.gateway.database.entity;

import br.ind.ajrorato.gateway.database.converters.StringToBooleanConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Table(name = "SEGURANCA")
@NoArgsConstructor
public class SegurancaEntity implements UserDetails {
    @Id
    @Column(name = "ID_SEGURANCA")
    private Long idSeguranca;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "SENHA")
    private String senha;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "BO_ADMINISTRADOR")
    @Convert(converter = StringToBooleanConverter.class)
    private UserRole boAdministrador;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        if (Objects.nonNull(boAdministrador))
            authorityList.add(new SimpleGrantedAuthority("ROLE_" + boAdministrador.name()));

        return authorityList;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
