package org.zerock.domain;

import lombok.Data;

/*# ÷�����ϵ��� �������� �����ϴ� Ŭ����*/

@Data
public class AttachFileDTO {
	private String fileName;
	private String uploadPath;
	private String uuid;
	private boolean image;
}
