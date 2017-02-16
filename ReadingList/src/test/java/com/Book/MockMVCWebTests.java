package com.Book;



import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ReadingListApplication.class)
@WebAppConfiguration
public class MockMVCWebTests {
	@Autowired
	private WebApplicationContext webContext;
	private MockMvc mockMvc;
	@Before
	public void setupMockMvc() {
	mockMvc = MockMvcBuilders
	.webAppContextSetup(webContext)
	.apply(springSecurity())
	.build();
	}
	
//	@Test
//	public void homePage() throws Exception {
//	mockMvc.perform(MockMvcRequestBuilders.get("/readingList"))
//	.andExpect(MockMvcResultMatchers.status().isOk())
//	.andExpect(MockMvcResultMatchers.view().name("readingList"))
//	.andExpect(MockMvcResultMatchers.model().attributeExists("books"))
//	.andExpect(MockMvcResultMatchers.model().attribute("books",
//	Matchers.is(Matchers.empty())));
//	}
//	
	
	@Test
	public void homePage() throws Exception {
	mockMvc.perform(get("/readingList"))
	.andExpect(status().isOk())
	.andExpect(view().name("readingList"))
	.andExpect(model().attributeExists("books"))
	.andExpect(model().attribute("books", is(empty())));
	}
	@Test
	// performs POST request
	public void postBook() throws Exception {
	mockMvc.perform(post("/readingList")
	.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	.param("title", "BOOK TITLE")
	.param("author", "BOOK AUTHOR")
	.param("isbn", "1234567890")
	.param("description", "DESCRIPTION"))
	.andExpect(status().is3xxRedirection())
	.andExpect(header().string("Location", "/readingList"));
	// Sets up expected book
	Book expectedBook = new Book();
	expectedBook.setId(1L);
	expectedBook.setReader("craig");
	expectedBook.setTitle("BOOK TITLE");
	expectedBook.setAuthor("BOOK AUTHOR");
	expectedBook.setIsbn("1234567890");
	expectedBook.setDescription("DESCRIPTION");
	//performs GET request
	mockMvc.perform(get("/readingList"))
	.andExpect(status().isOk())
	.andExpect(view().name("readingList"))
	.andExpect(model().attributeExists("books"))
	.andExpect(model().attribute("books", hasSize(1)))
	.andExpect(model().attribute("books",
	contains(samePropertyValuesAs(expectedBook))));
	}
	@Test
	public void homePage_unauthenticatedUser() throws Exception {
	mockMvc.perform(get("/"))
	.andExpect(status().is3xxRedirection())
	.andExpect(header().string("Location","http://localhost/loginpage.html"));
	}
	
	@Test
	// uses Vandy user 
	@WithUserDetails("Vandy")
	public void homePage_authenticatedUser() throws Exception {
	// sets up expected reader
	Reader expectedReader = new Reader();
	expectedReader.setUsername("Vandy");
	expectedReader.setPassword("password");
	expectedReader.setFullname("Vandhana Parthiban");
	// performs GET request
	mockMvc.perform(get("/"))
	.andExpect(status().isOk())
	.andExpect(view().name("readingList"))
	.andExpect(model().attribute("reader",
	samePropertyValuesAs(expectedReader)))
	.andExpect(model().attribute("books", hasSize(0)));
	}
	
	
}
