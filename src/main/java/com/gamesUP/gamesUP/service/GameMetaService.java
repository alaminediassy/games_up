package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.request.AuthorDTO;
import com.gamesUP.gamesUP.dto.request.CategoryDTO;
import com.gamesUP.gamesUP.dto.request.PublisherDTO;
import com.gamesUP.gamesUP.dto.response.AuthorResponseDTO;
import com.gamesUP.gamesUP.dto.response.CategoryResponseDTO;
import com.gamesUP.gamesUP.dto.response.PublisherResponseDTO;

import java.util.List;

public interface GameMetaService {
    void createAuthor(AuthorDTO dto);
    void createCategory(CategoryDTO dto);
    void createPublisher(PublisherDTO dto);

    List<AuthorResponseDTO> getAllAuthors();
    List<CategoryResponseDTO> getAllCategories();
    List<PublisherResponseDTO> getAllPublishers();

}
