package com.ancho.crud.dto.security;

import com.ancho.crud.dto.UserAccountDto;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public record BoardPrincipal(
        String username,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        String email,
        String nickname,
        String memo
) implements UserDetails {
    public static BoardPrincipal of(String username, String password, String email, String nickname, String memo) {
        Set<RoleType> roleTypes = Set.of(RoleType.USER);    //  현재는 권한 정보로 유저만 준다.

        return new BoardPrincipal(
                username,
                password,
                roleTypes.stream()
                        .map(RoleType::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableSet())
                ,
                email,
                nickname,
                memo
        );
    }

    public static BoardPrincipal from(UserAccountDto dto){
        return BoardPrincipal.of(
                dto.userId(),
                dto.userPassword(),
                dto.email(),
                dto.nickname(),
                dto.memo()
        );
    }

    public UserAccountDto toDto(){
        return UserAccountDto.of(
               username,
               password,
               email,
               nickname,
               memo
        );
    }


    //  이것은 유저 정보를 구현할 것임

    @Override public String getPassword() { return password; }

    @Override public String getUsername() { return username; }

    @Override   //  권한 기능 -> 로그인한 사용자가 가지는 권한 (관리자 권한이나 사용자 권한)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;    //  현재는 사용자 권한만 다룰거라서 현재는 간단히 작성함
    }


    @Override public boolean isAccountNonExpired() { return true; }

    @Override public boolean isAccountNonLocked() { return true; }

    @Override public boolean isCredentialsNonExpired() { return true; }

    @Override public boolean isEnabled() { return true; }

    public enum RoleType {
        USER("ROLE_USER");

        @Getter private final String name;

        RoleType(String name){
            this.name = name;
        }
    }
}
