package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.*;
import com.assemblock.assemblock_be.Entity.*;
import com.assemblock.assemblock_be.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final ProposalRepository proposalRepository;
    private final ProposalTargetRepository proposalTargetRepository;

    public List<BoardListResponseDto> getMyBoards(Long userId) {
        User user = findUserById(userId);
        List<Board> boards = boardRepository.findAllByUser(user);

        return boards.stream()
                .map(board -> {
                    int blockCount = (int) boardBlockRepository.countByBoard(board);
                    List<String> previewTypes = boardBlockRepository.findTop4ByBoardOrderByCreatedAtDesc(board)
                            .stream()
                            .map(boardBlock -> boardBlock.getBlock().getBlockType().name())
                            .collect(Collectors.toList());

                    return new BoardListResponseDto(board, blockCount, previewTypes);
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
            throw new IllegalStateException("이미 사용 중인 보드 이름입니다.");
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
    public void removeBlocksFromBoard(Long userId, Long boardId, List<Long> blockIds) {
        Board board = findBoardByIdAndUser(userId, boardId);
        List<Block> blocks = blockRepository.findAllById(blockIds);

        if (blocks.isEmpty()) {
            throw new IllegalArgumentException("삭제할 블록이 선택되지 않았거나 존재하지 않습니다.");
        }

        boardBlockRepository.deleteByBoardAndBlockIn(board, blocks);
    }

    @Transactional
    public void createTeamProposal(Long userId, BoardDto.TeamProposalRequest requestDto) {
        User proposer = findUserById(userId);

        String[] dates = requestDto.getRecruitingPeriod().split("~");
        if (dates.length != 2) {
            throw new IllegalArgumentException("날짜 형식이 올바르지 않습니다.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDate startDate = LocalDate.parse(dates[0].trim(), formatter);
        LocalDate endDate = LocalDate.parse(dates[1].trim(), formatter);

        Proposal proposal = Proposal.builder()
                .user(proposer)
                .projectTitle(requestDto.getProjectTitle())
                .projectMemo(requestDto.getMemo())
                .discordId(requestDto.getContact())
                .recruitStartDate(startDate)
                .recruitEndDate(endDate)
                .build();
        proposalRepository.save(proposal);

        List<Block> targetBlocks = blockRepository.findAllById(requestDto.getTargetBlockIds());
        for (Block block : targetBlocks) {
            ProposalTarget target = ProposalTarget.builder()
                    .proposal(proposal)
                    .user(proposer)
                    .block(block)
                    .responseStatus(ProposalStatus.PENDING)
                    .build();
            proposalTargetRepository.save(target);
        }
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

    private BoardDto.BoardDetailResponse boardToDetailResponse(Board board) {
        List<BlockResponseDto> blocksInBoard = boardBlockRepository.findAllByBoard(board)
                .stream()
                .map(BoardBlock::getBlock)
                .map(BlockResponseDto::fromEntity)
                .collect(Collectors.toList());

        return BoardDto.BoardDetailResponse.builder()
                .boardId(board.getId())
                .boardName(board.getBoardName())
                .boardMemo(board.getBoardMemo())
                .blocks(blocksInBoard)
                .build();
    }
}