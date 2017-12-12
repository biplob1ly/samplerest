package com.plethora.samplerest.Controller;

import com.plethora.samplerest.Entity.Bookmark;
import com.plethora.samplerest.Exception.UserNotFoundException;
import com.plethora.samplerest.Repository.AccountRepository;
import com.plethora.samplerest.Repository.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/{userName}/bookmarks")
public class BookmarkController {

    private final AccountRepository accountRepository;
    private final BookmarkRepository bookmarkRepository;

    @Autowired
    public BookmarkController(AccountRepository accountRepository, BookmarkRepository bookmarkRepository) {
        this.accountRepository = accountRepository;
        this.bookmarkRepository = bookmarkRepository;
    }

    private void validateUser(String usrName) {
        this.accountRepository.findByUserName(usrName).orElseThrow(() -> new UserNotFoundException(usrName));
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@PathVariable String userName, @RequestBody Bookmark input) {
        this.validateUser(userName);

        return this.accountRepository.findByUserName(userName).map(
                account -> {
                    Bookmark result = bookmarkRepository.save(new Bookmark(account, input.getUri(), input.getDescription()));

                    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                            .buildAndExpand(result.getId()).toUri();

                    return ResponseEntity.created(location).build();
                }
        ).orElse(ResponseEntity.noContent().build());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{bookmarkId}")
    Bookmark readBookmark(@PathVariable String userName, @PathVariable Long bookmarkId) {
        this.validateUser(userName);
        return this.bookmarkRepository.findOne(bookmarkId);
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Bookmark> readBookmarks(@PathVariable String userName) {
        this.validateUser(userName);
        return this.bookmarkRepository.findByAccountUserName(userName);
    }
}
