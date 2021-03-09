package org.zerock.domain;

import lombok.Data;

/*# 첨부파일들의 정보들을 저장하는 클래스*/

@Data
public class AttachFileDTO {
	private String fileName;
	private String uploadPath;
	private String uuid;
	private boolean image;
}
