package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

public interface BoardMapper {

	//@Select("SELECT * FROM TBL_BOARD WHERE BNO > 0")
	public List<BoardVO> getList();
	
	public void insert(BoardVO boardVO);
	
	public void insertSelectKey(BoardVO boardVO);
	
	public BoardVO read(Long bno);
	
	public int delete(Long bno); 
	
	public int update(BoardVO boardVO);
	
	public List<BoardVO> getListWithPaging(Criteria cri);
	
	public int getTotalCount(Criteria cri);
	
	public void updateReplyCnt(@Param("bno") Long bno,@Param("amount") int amount);
}
