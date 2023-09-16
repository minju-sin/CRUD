package com.ancho.crud.domain.constant;

import lombok.Getter;

public enum SearchType {
//    검색 타입에 필요한 것은 제목, 본문, 아이디, 닉네임, 해시태그 임
    TITLE("제목"),
    CONTENT("본문"),
    ID("유저 ID"),
    NICKNAME("닉네임"),
    HASHTAG("해시태그");

    @Getter private final String description;

    SearchType(String description){
        this.description = description;
    }
}
