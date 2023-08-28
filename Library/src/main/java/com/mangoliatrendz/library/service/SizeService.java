package com.mangoliatrendz.library.service;

import com.mangoliatrendz.library.model.Size;

import java.util.List;

public interface SizeService {

    List<Size> allSizeVariations();

    Size findById(long id);
}
