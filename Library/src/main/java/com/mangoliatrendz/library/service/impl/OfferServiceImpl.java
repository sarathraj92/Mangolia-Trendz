package com.mangoliatrendz.library.service.impl;

import com.mangoliatrendz.library.dto.OfferDto;
import com.mangoliatrendz.library.model.Offer;
import com.mangoliatrendz.library.repository.OfferRepository;
import com.mangoliatrendz.library.service.OfferService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    private OfferRepository offerRepository;

    public OfferServiceImpl(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @Override
    public List<OfferDto> getAllOffers() {
        List<Offer> offerList=offerRepository.findAll();
        List<OfferDto> offerDtoList=transfer(offerList);


        return offerDtoList;
    }


    public List<OfferDto> transfer(List<Offer> offerList){
        List<OfferDto> offerDtoList=new ArrayList<>();
        for(Offer offer : offerList) {
            OfferDto offerDto=new OfferDto();
            offerDto.setId(offer.getId());
            offerDto.setName(offer.getName());
            offerDto.setDescription(offer.getDescription());
            offerDto.setOffPercentage(offer.getOffPercentage());
            offerDto.setOfferType(offer.getOfferType());
            offerDto.setApplicableForId(offer.getApplicableForId());
            offerDto.setExpiryDate(offer.getExpiryDate());
            offerDto.setApplicableForName(offer.getApplicableForName());
            offerDto.setEnabled(offer.isEnabled());
            offerDtoList.add(offerDto);
        }
        return offerDtoList;
    }

}
