package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Dto.BoardDto;
import com.assemblock.assemblock_be.Entity.Block;
import com.assemblock.assemblock_be.Entity.Board;
import com.assemblock.assemblock_be.Entity.BoardBlock;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Repository.BlockRepository;
import com.assemblock.assemblock_be.Repository.BoardBlockRepository;
import com.assemblock.assemblock_be.Repository.BoardRepository;
import com.assemblock.assemblock_be.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardBlockRepository boardBlockRepository;
    private final UserRepository userRepository;
    private final BlockRepository blockRepository;

    public List<BoardDto.BoardSummaryResponse> getMyBoards(Long userId) {
        User user = findUserById(userId);
        List<Board> boards = boardRepository.findAllByUser(user);
        return boards.stream()
                .map(board -> {
                    long blockCount = boardBlockRepository.countByBoard(board);
                    List<String> previewImages = boardBlockRepository.findTop4ByBoardOrderByCreatedAtDesc(board)
                            .stream()
                            .map(boardBlock -> boardBlock.getBlock().getResultUrl())
                            .collect(Collectors.toList());

                    return BoardDto.BoardSummaryResponse.builder()
                            .boardId(board.getId())
                            .boardName(board.getBoardName())
                            .blockCount((int) blockCount)
                            .previewImageUrls(previewImages)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public BoardDto.BoardDetailResponse createBoard(Long userId, BoardDto.BoardCreateRequest requestDto) {
        User user = findUserById(userId);
        if (boardRepository.existsByUserAndBoardName(user, requestDto.getBoardName())) {
            throw new IllegalArgumentException("이미 사용 중인 보드 이름입니다.");
        }
        Board board = Board.builder()
                .user(user)
                .boardName(requestDto.getBoardName())
                .boardMemo(requestDto.getBoardMemo())
                .build();
        Board savedBoard = boardRepository.save(board);
        return boardToDetailResponse(savedBoard);
    }

    public BoardDto.BoardDetailResponse getBoardDetails(Long userId, Long boardId) {
        Board board = findBoardByIdAndUser(userId, boardId);
        return boardToDetailResponse(board);
    }

    @Transactional
    public BoardDto.BoardDetailResponse updateBoard(Long userId, Long boardId, BoardDto.BoardUpdateRequest requestDto) {
        User user = findUserById(userId);
        Board board = findBoardByIdAndUser(userId, boardId);
        if (!board.getBoardName().equals(requestDto.getBoardName()) &&
                boardRepository.existsByUserAndBoardName(user, requestDto.getBoardName())) {
            throw new IllegalArgumentException("이미 사용 중인 보드 이름입니다.");
        }
        board.update(requestDto.getBoardName(), requestDto.getBoardMemo());
        return boardToDetailResponse(board);
    }

    @Transactional
    public void deleteBoard(Long userId, Long boardId) {
        Board board = findBoardByIdAndUser(userId, boardId);
        boardRepository.delete(board);
    }

    @Transactional
    public void addBlockToBoard(Long userId, Long boardId, Long blockId) {
        Board board = findBoardByIdAndUser(userId, boardId);
        Block block = findBlockById(blockId);
        if (boardBlockRepository.existsByBoardAndBlock(board, block)) {
            throw new IllegalArgumentException("이미 보드에 추가된 블록입니다.");
        }
        BoardBlock boardBlock = BoardBlock.builder()
                .board(board)
                .block(block)
                .build();
        boardBlockRepository.save(boardBlock);
    }

    @Transactional
    public void removeBlockFromBoard(Long userId, Long boardId, Long blockId) {
        Board board = findBoardByIdAndUser(userId, boardId);
        Block block = findBlockById(blockId);
        BoardBlock boardBlock = boardBlockRepository.findByBoardAndBlock(board, block)
                .orElseThrow(() -> new IllegalArgumentException("보드에 해당 블록이 존재하지 않습니다."));
        boardBlockRepository.delete(boardBlock);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));
    }

    private Block findBlockById(Long blockId) {
        return blockRepository.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("블록을 찾을 수 없습니다. ID: " + blockId));
    }

    private Board findBoardByIdAndUser(Long userId, Long boardId) {
        User user = findUserById(userId);
        return boardRepository.findByIdAndUser(boardId, user)
                .orElseThrow(() -> new IllegalArgumentException("보드를 찾을 수 없거나 접근 권한이 없습니다."));
    }

    private BlockResponseDto blockToResponseDto(Block block) {
        return BlockResponseDto.builder()
                .blockId(block.getId())
                .title(block.getTitle())
                .description(block.getOnelineSummary())
                .username(block.getUser().getNickname())
                .coverImageUrl(block.getResultUrl())
                .build();
    }
    private BoardDto.BoardDetailResponse boardToDetailResponse(Board board) {
        List<BlockResponseDto> blocksInBoard = boardBlockRepository.findAllByBoard(board)
                .stream()
                .map(BoardBlock::getBlock)
                .map(this::blockToResponseDto)
                .collect(Collectors.toList());

        return BoardDto.BoardDetailResponse.builder()
                .boardId(board.getId())
                .boardName(board.getBoardName())
                .boardMemo(board.getBoardMemo())
                .blocks(blocksInBoard)
                .build();
    }
}