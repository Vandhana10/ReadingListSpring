package com.Book;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface ReadingListRepository extends JpaRepository <Book,Long> {
		List<Book> findByReader(String reader);

		UserDetails findOne(String username);

	}


