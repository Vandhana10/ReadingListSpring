package com.Book;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")

public class ReadingListController {
	
	private ReadingListRepository readingListRepository; 
	
	@Autowired
	 public ReadingListController (ReadingListRepository readingListRepository){
		this.readingListRepository= readingListRepository;
	}

	@RequestMapping(value="/{reader}", method=RequestMethod.GET)
	
	public String readerBooks(@PathVariable ("reader") String reader,Model model){
		
		List<Book> readingList = readingListRepository.findByReader(reader);
		if(readingList !=null){
			model.addAttribute("books",readingList);
		}
		System.out.println("Home Page");
		return "readingList";
		}
		
	@RequestMapping(value="/{reader}", method=RequestMethod.POST)
	
	public String addtoREadingList(@PathVariable("reader") String reader,Book book){
		book.setReader(reader);
		readingListRepository.save(book);
		
		return "redirect:/{reader}" ;
	}
	
	 
	}
	

