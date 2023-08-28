package com.mangoliatrendz.library.service;

import com.mangoliatrendz.library.model.Image;

import java.util.List;

public interface ImageService {


    List<Image> findProductImages(long id);

    List<Image> findAll();

}
