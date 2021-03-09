package org.zerock.mapper;

import java.util.List;

import org.zerock.domain.BoardAttachVO;

public interface BoardAttachMapper {

	public void insert(BoardAttachVO vo);
	
	public void delete(String uuid);
	
	/* # 게시물 번호로 첨부파일 찾기 */
	public List<BoardAttachVO> findByBno(Long bno);
	
	/* # 첨부파일 삭제 */
	public void deleteAll(Long bno);
	
	/* # 오래된 첨부파일 목록 */
	public List<BoardAttachVO> getOldFiles();
	
}
