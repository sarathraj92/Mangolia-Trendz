package com.mangoliatrendz.library.service.impl;

import com.mangoliatrendz.library.model.Color;
import com.mangoliatrendz.library.repository.ColorRepository;
import com.mangoliatrendz.library.service.ColorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorServiceImpl implements ColorService {
    private ColorRepository colorRepository;

    public ColorServiceImpl(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    @Override
    public List<Color> allColorVariations() {
        return colorRepository.findAll();
    }
}
