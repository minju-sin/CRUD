package com.ancho.crud.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;


import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DisplayName("비즈니스 로직 - 페이지네이션")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = PaginationService.class)
class PaginationServiceTest {

    private final PaginationService sut;

    public PaginationServiceTest(@Autowired PaginationService paginationService) {
        this.sut = paginationService;
    }

    @DisplayName("현재 페이지 번호와 총 페이지 수를 주면, 페이징 바 리스트를 만들어준다.")
    //  @Test 대신 메소드 소스를 사용함으로써 테스트를 ㅜ현할 수 있다.
    //  구현하는 방법에는 void함수로 테스트를 구현한 뒤 static 함수이름으로 테스트를 실행한다.
    @MethodSource
    @ParameterizedTest(name = "[{index}] 현재 페이지 : {0}, 총 페이지 : {1} => {2}")
//    int currentPageNumber => 현재 페이지 번호
//    int totalPages => 총 페이지 수
//    List<Integer> expected => 검증하고 싶은 값
    void givenCurrentPageNumberAndTotalPages_whenCalculating_thenReturnsPaginationBarNumbers(int currentPageNumber, int totalPages, List<Integer> expected){
        //  Given

        //  When
        //  실제 값 => actual 을 이용해 페이징 바 리스트를 생성한다.
        List<Integer> actual = sut.getPaginationBarNumbers(currentPageNumber, totalPages);

        //  Then
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> givenCurrentPageNumberAndTotalPages_whenCalculating_thenReturnsPaginationBarNumbers() {
        return Stream.of(
                //  내가 검증하고 싶은 값을 나열함
                //  arguments(현재 페이지 번호, 총 페이지 수, 나와야하는 페이징 번호)
                arguments(0, 13, List.of(0, 1, 2, 3, 4)),
                arguments(1, 13, List.of(0, 1, 2, 3, 4)),
                arguments(2, 13, List.of(0, 1, 2, 3, 4)),
                arguments(3, 13, List.of(1, 2, 3, 4, 5)),
                arguments(4, 13, List.of(2, 3, 4, 5, 6)),
                arguments(5, 13, List.of(3, 4, 5, 6, 7)),
                arguments(6, 13, List.of(4, 5, 6, 7, 8)),
                arguments(10, 13, List.of(8, 9, 10, 11, 12)),
                arguments(11, 13, List.of(9, 10, 11, 12)),
                arguments(12, 13, List.of(10, 11, 12))
        );
    }

    @DisplayName("현재 설정되어 있는 페이지네이션 바의 길이를 알려준다.")
    @Test
    void givenNothing_whenCalling_thenReturnsCurrentBarLength(){
        //  Given

        //  When
        //  바의 길이 값 => barLength 을 이용해 바의 길이를 알려준다.
        int barLength = sut.currentBarLength();

        //  Then
        assertThat(barLength).isEqualTo(5);
    }

}
