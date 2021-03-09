package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardAttachMapper;
import org.zerock.mapper.BoardMapper;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService{

	@Setter(onMethod_ = @Autowired)
	private BoardMapper mapper;

	@Setter(onMethod_ = @Autowired)
	private BoardAttachMapper attachMapper;
	
	@Override
	public void register(BoardVO boardVO) {

		log.info("register...." + boardVO);
		
		mapper.insertSelectKey(boardVO);
		
		if(boardVO.getAttachList() == null || boardVO.getAttachList().size() <=0) {
			return;
		}
		boardVO.getAttachList().forEach(attach -> {
			attach.setBno(boardVO.getBno());
			attachMapper.insert(attach);
		});
		
	}

	@Override
	public BoardVO get(Long bno) {
		
		log.info("get....." + bno);
		
		return mapper.read(bno);
	}
	
	@Transactional
	@Override
	public boolean modify(BoardVO boardVO) {

		log.info("modify......" + boardVO);
		
		/*- 1. 일단 첨부파일에 모든 파일들을 삭제*/
		attachMapper.deleteAll(boardVO.getBno());

		boolean modifyResult = mapper.update(boardVO) == 1;
		
		/*- 2. 수정된 게시판 항목 & 첨부파일 재 업로드*/
		if(modifyResult && boardVO.getAttachList() != null && boardVO.getAttachList().size() > 0) {

			boardVO.getAttachList().forEach(attach -> {

				attach.setBno(boardVO.getBno());
				attachMapper.insert(attach);
			});
		}

		return modifyResult;
	}

	@Override
	public boolean remove(Long bno) {

		log.info("remove...." + bno);
		
		attachMapper.deleteAll(bno);
		
		return mapper.delete(bno) == 1;
	}

	@Override
	public List<BoardVO> getList(Criteria cri) {

		log.info("getList......." + cri);
		
		return mapper.getListWithPaging(cri);
	}
	
	@Override
	public int getTotal(Criteria cri) {
		
		log.info("get Total : ");
		
		return mapper.getTotalCount(cri);
	}
	
	@Override
	public List<BoardAttachVO> getAttachList(Long bno) {
		
		log.info("get Attach list by bno" + bno);
		
		return attachMapper.findByBno(bno);
	}
}
