package com.example.springbbsmst.service;

import com.example.springbbsmst.domain.entity.BoardEntity;
import com.example.springbbsmst.domain.repository.BoardRepository;
import com.example.springbbsmst.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    private BoardRepository boardRepository;

    private static final int BLOCK_PAGE_NUM_COUNT=5; // 블럭에 존재하는 페이지 번호 수
    private static final int PAGE_POST_COUNT=4; // 한 페이지에 존재하는 게시글 수

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // insert, update
    @Transactional
    public Long savePost(BoardDto boardDto) {

        return boardRepository.save(boardDto.toEntity()).getId(); //save는 DB insert, update 역할
    }

    // select & paging
    @Transactional
    public List<BoardDto> getBoardList(Integer pageNum) {

        // PageRequest.of(
        // pageNum - 1, <==== limit 을 의미
        // PAGE_POST_COUNT, <==== offset, 몇 개를 가져올 것인가?
        // Sort.by(Sort.Direction.ASC, "createdDate") <==== 생성일을 기준으로 정렬
        // )
        Page<BoardEntity> page = boardRepository.findAll(PageRequest.of(pageNum - 1, PAGE_POST_COUNT,
                Sort.by(Sort.Direction.ASC, "createdDate")));

//        List<BoardEntity> boardEntityList = boardRepository.findAll();
        // 위에껄 가리고, 페이징처리 부분 넣어줘야 갯수만큼 잘라서 보임. 아니면 페이징과 상관없이 전체 리스트 조회됨.
        List<BoardEntity> boardEntityList = page.getContent();
        List<BoardDto> boardDtoList = new ArrayList<>();


        for(BoardEntity boardEntity : boardEntityList) {
//            BoardDto boardDto = BoardDto.builder()
//                    .id(boardEntity.getId())
//                    .title(boardEntity.getTitle())
//                    .content(boardEntity.getContent())
//                    .writer(boardEntity.getWriter())
//                    .createdDate(boardEntity.getCreatedDate())
//                    .build();

//            boardDtoList.add(boardDto);
            // 위에꺼가 별도 메소드로 구현되어서 그걸 가져다 쓰면 됨.
            boardDtoList.add(this.convertEntityToDto(boardEntity));
        }

        return boardDtoList;
    }

    // 게시글 총 개수 가져오기 = select count(*) from board
    @Transactional
    public Long getBoardCount() {
        return boardRepository.count();
    }

    // 프론트에 노출시킬 페이지 번호 리스트 계산하기
    public Integer[] getPageList(Integer currPageNum) {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];  // 하나의 페이지는 4개의 게시글을 가져오며, 페이지 번호는 총 5개로 정함.

        // total article
        Double postsTotCount = Double.valueOf(this.getBoardCount());
        // 총 게시글 기준으로 계산한 마지막 페이지 번호 계산 (올림)
        Integer totalLastPageNum = (int)(Math.ceil(postsTotCount/PAGE_POST_COUNT));
        // 현재 페이지를 기준으로 블럭의 마지막 페이지 번호 계산
        Integer blockLastPageNum = (totalLastPageNum > currPageNum + BLOCK_PAGE_NUM_COUNT)?
                currPageNum + BLOCK_PAGE_NUM_COUNT : totalLastPageNum;
        // 페이지 시작 번호 조정
        currPageNum = (currPageNum <= 3)? 1 : currPageNum-2;
        // 페이지 번호 할당
        for (int val = currPageNum, idx = 0; val <= blockLastPageNum; val++, idx++) {
            pageList[idx] = val;
        }

        return pageList;
    }


    // 게시글 내용 조회

    @Transactional
    public BoardDto getPost(Long id) {
        Optional<BoardEntity> boardWrapper = boardRepository.findById(id); // findById 동작 구조를 알아야 함.... 왜 옵셔널로 리턴하는지
        BoardEntity boardEntity = boardWrapper.get(); // boardWrapper.get();는 엔티티를 쏙 빼서 주는 역할

//        BoardDto boardDto = BoardDto.builder()
//                .id(boardEntity.getId())
//                .title(boardEntity.getTitle())
//                .content(boardEntity.getContent())
//                .writer(boardEntity.getWriter())
//                .createdDate(boardEntity.getCreatedDate())
//                .build();

//        return boardDto;
        // 위에꺼가 별도 메소드로 구현되어서 그걸 가져다 쓰면 됨.
        return this.convertEntityToDto(boardEntity);
    }

    // delete
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }

    // search list
    public List<BoardDto> searchBoardList(String keyword) {
        List<BoardEntity> boards = boardRepository.findByTitleContaining(keyword);
        List<BoardDto> boardDtoList = new ArrayList<>();

        if(boards.isEmpty()) return boardDtoList;

        for (BoardEntity board : boards) {
            boardDtoList.add(this.convertEntityToDto(board)); // 이건 모르겠다
        }

        return boardDtoList;
    }

    private BoardDto convertEntityToDto(BoardEntity board) {
        return BoardDto.builder()
                .id(board.getId())
                .writer(board.getWriter())
                .title(board.getTitle())
                .content(board.getContent())
                .createdDate(board.getCreatedDate())
                .build();
    }

}
