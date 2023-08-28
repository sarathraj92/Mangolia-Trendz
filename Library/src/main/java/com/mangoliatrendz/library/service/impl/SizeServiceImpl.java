package com.mangoliatrendz.library.service.impl;

import com.mangoliatrendz.library.model.Size;
import com.mangoliatrendz.library.repository.SizeRepository;
import com.mangoliatrendz.library.service.SizeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeServiceImpl implements SizeService {


    private SizeRepository sizeRepository;

    public SizeServiceImpl(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    @Override
    public List<Size> allSizeVariations() {
        return sizeRepository.findAll();
    }

    @Override
    public Size findById(long id) {
        return sizeRepository.findById(id);
    }
}
