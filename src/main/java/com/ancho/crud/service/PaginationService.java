package com.ancho.crud.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaginationService {
//      페이지네이션 서비스

    //        페이지 네이션의 길이를 상태값으로 정해서 알아낸다.
    private static final int BAR_LENGTH = 5;

    //      현재 어느 페이지 인지 확인하기 위해서 currentPageNumber 변수로 현재 페이지 번호를 읽어온다.
//     마지막 페이지 번호를 알아내야 하기 때문에 totlaPages로 전체 페이지 수를 알아낸다.
    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages) {
        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH / 2), 0);
        int endNumber = Math.min(startNumber + BAR_LENGTH, totalPages);

        return IntStream.range(startNumber, endNumber).boxed().toList();
    }

    public int currentBarLength(){
        return BAR_LENGTH;
    }
}
