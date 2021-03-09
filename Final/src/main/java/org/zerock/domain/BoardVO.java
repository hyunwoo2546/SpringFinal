package org.zerock.domain;

import java.util.List;

import lombok.Data;

@Data
public class BoardVO {

	private Long bno;
	private String title;
	private String content;
	private String writer;
	private String regdate;
	private String updateDate;
	private int replyCnt;
	
	private List<BoardAttachVO> attachList;
	
}
