package com.book.service;

import com.book.entity.Book;
import com.book.vo.BookVo;

import java.util.List;

/**
 * Created by lixuy on 2016/4/16.
 */
public interface BookService {
    public List<Book> list(String type);

    // 分页
    public List<Book> list(int pageSize, int pageNum, String type);

    public BookVo getById(int id);

    public int saveOrUpdate(BookVo bookVo);

    public List<Book> querySellBook(int pageSize, int pageNum);

    public int querySellBookTotal();
}
