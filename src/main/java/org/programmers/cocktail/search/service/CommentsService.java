package org.programmers.cocktail.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentsService {

    @Autowired
    CommentsMapper commentsMapper;
}