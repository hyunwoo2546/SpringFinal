package org.zerock.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
//import oracle.jdbc.proxy.annotation.Post;

@Controller
@Log4j
@RequestMapping("/board/*")
@AllArgsConstructor
public class BoardController {

	private BoardService boardService;
	
	/* # ��ü��ȸ */
	@GetMapping("/list")
	public void list(Criteria cri, Model model) {
		log.info("list" + cri);
		model.addAttribute("list", boardService.getList(cri));
		
		int total = boardService.getTotal(cri);
		
		log.info("total" + total);
		
		model.addAttribute("pageMaker", new PageDTO(cri, total));
	}
	
	/* # ��� */
	@PostMapping("/register")
	@PreAuthorize("isAuthenticated()")
	public String register(BoardVO boardVO,RedirectAttributes rttr) {
		
		log.info("=========================");
		
		log.info("register : "+boardVO);
		
		if(boardVO.getAttachList() != null) {
			boardVO.getAttachList().forEach(attach -> log.info(attach));
		}
		
		log.info("=========================");
		
		boardService.register(boardVO);
		
		rttr.addFlashAttribute("result",boardVO.getBno());
		
		return "redirect:/board/list";
	}
	
	/* # ��� 2 */
	// �Է��������� �����ֱ� ���� �޼ҵ�
	@GetMapping("/register")
	@PreAuthorize("isAuthenticated()")
	public void register() {
		
	}
	 
	/* # ��ȸ */
	@GetMapping({"/get","/modify"})
	public void get(@RequestParam("bno") Long bno,@ModelAttribute("cri")Criteria cri, Model model) {
		log.info("/get or modify");
		model.addAttribute("board",boardService.get(bno));
	}
	
	/* # ���� */
	/*
	 * @PreAuthorize("principal.username == #board.writer")
	 * 
	 * @PostMapping("/modify") public String modify(BoardVO
	 * boardVO,@ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
	 * log.info("Modify : " + boardVO);
	 * 
	 * if(boardService.modify(boardVO)) {
	 * rttr.addFlashAttribute("result","success"); }
	 * 
	 * return "redirect:/board/list" + cri.getListLink(); }
	 */
	
	@PreAuthorize("principal.username == #board.writer")
	@PostMapping("/modify")
	public String modify(BoardVO board, Criteria cri, RedirectAttributes rttr) {
		log.info("modify:" + board);

		if (boardService.modify(board)) {
			rttr.addFlashAttribute("result", "success");
		}

		return "redirect:/board/list" + cri.getListLink();
	}
	
	/* # ���� */
	@PreAuthorize("principal.username == #writer")
	@PostMapping("/remove")
	public String remove(@RequestParam("bno")Long bno,RedirectAttributes rttr, @ModelAttribute("cri") Criteria cri) {
		log.info("Remove : " + bno);
		
		/* + ÷������ ��� Ȯ�� */
		List<BoardAttachVO> attachList = boardService.getAttachList(bno);
		
		if(boardService.remove(bno)) {
			
			/* + deleteFiles��� private Method�� ��� */
			/* + ���� ���������� �����ϴ� �۾��� Method */
			deleteFiles(attachList);
			
			rttr.addFlashAttribute("result","success");
		}
		
		return "redirect:/board/list" + cri.getListLink();
	}
	
	/* # ÷������ ������ -> JSON */
	@GetMapping(value = "/getAttachList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno) {
		
		log.info("getAttachList : " + bno);
		
		return new ResponseEntity<>(boardService.getAttachList(bno), HttpStatus.OK);
	}
	
	/* # ���� ���� ó�� */
	private void deleteFiles(List<BoardAttachVO> attachList) {
		
		if(attachList == null || attachList.size() == 0) {
			return;
		}
		
		log.info("delete attach files.......");
		log.info(attachList);
		
		attachList.forEach(attach -> {
			try {
				/* + �̹��� �̿��� ���� ���� */
				Path file = Paths.get("C:\\upload\\"+attach.getUploadPath()+"\\"+ attach.getUuid()+"_"+ attach.getFileName());
				
				Files.deleteIfExists(file);
				
				/* + �̹��� ���� ����  */
				if(Files.probeContentType(file).startsWith("image")) {
					Path thumbNail = Paths.get("C:\\upload\\" + attach.getUploadPath() + "\\s_" + attach.getUuid() + "_" + attach.getFileName());
					
					Files.delete(thumbNail);
				}
			} catch (Exception e) {
				log.error("delete file error " + e.getMessage());
			}
		});
	}
	
}
