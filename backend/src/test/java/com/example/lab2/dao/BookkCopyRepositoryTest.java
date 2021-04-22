package com.example.lab2.dao;

import com.example.lab2.entity.BookCopy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BookkCopyRepositoryTest {

    @Autowired
    BookkCopyRepository bookkCopyRepository;

    @Test
    public void testGetBookCopyByUniqueBookMark() {
        BookCopy bc1 = new BookCopy(
                BookCopy.AVAILABLE,
                "isbntest",
                "isbntest-1",
                (long) 1,
                null,
                null,
                (long) 1
        );

        BookCopy bc2 = new BookCopy(
                BookCopy.AVAILABLE,
                "isbntest",
                "isbntest-2",
                (long) 2,
                null,
                null,
                (long) 2
        );

        bookkCopyRepository.save(bc1);
        bookkCopyRepository.save(bc2);

        Optional<BookCopy> bcfromdb = bookkCopyRepository.getBookCopyByUniqueBookMark("isbntest-2");
        assertTrue(bcfromdb.isPresent());
        assertEquals(bcfromdb.get().getStatus(), BookCopy.AVAILABLE);
        assertEquals(bcfromdb.get().getIsbn(),"isbntest");
        assertEquals(bcfromdb.get().getUniqueBookMark(),"isbntest-2");
        assertEquals( bcfromdb.get().getAdminID().longValue(),2);
        assertEquals( bcfromdb.get().getLibraryID().longValue(),2);


    }
}