package com.mangoliatrendz.library.service.impl;

import com.mangoliatrendz.library.dto.BannerDto;
import com.mangoliatrendz.library.dto.CouponDto;
import com.mangoliatrendz.library.model.Banner;
import com.mangoliatrendz.library.model.Coupon;
import com.mangoliatrendz.library.repository.BannerRepository;
import com.mangoliatrendz.library.service.BannerService;
import com.mangoliatrendz.library.utils.ImageUpload;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {

    private BannerRepository bannerRepository;

    private ImageUpload imageUpload;

    public BannerServiceImpl(BannerRepository bannerRepository,ImageUpload imageUpload) {
        this.imageUpload=imageUpload;
        this.bannerRepository = bannerRepository;
    }

    @Override
    public Banner save(BannerDto bannerDto, MultipartFile bannerImage) {
        Banner banner=new Banner();
        banner.setName(bannerDto.getName());
        banner.setDescription_1(bannerDto.getDescription_1());
        banner.setDescription_2(bannerDto.getDescription_2());
        banner.setDescription_3(bannerDto.getDescription_3());
        banner.setProduct(bannerDto.getProduct());
        banner.setEnabled(true);
        try {
            if (bannerImage == null) {
                banner.setBannerFile(null);
            } else {
                String fileName = imageUpload.storeFile(bannerImage);
                banner.setBannerFile(fileName);
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        bannerRepository.save(banner);


        return banner;
    }

    @Override
    public List<BannerDto> getAllBanners() {
        List<Banner> bannerList=bannerRepository.findAll();
        List<BannerDto>bannerDtoList = transferData(bannerList);
        return bannerDtoList;
    }

    @Override
    public BannerDto findById(long id) {
        Banner banner=bannerRepository.findById(id);
        BannerDto bannerDto=new BannerDto();
        bannerDto.setId(banner.getId());
        bannerDto.setName(banner.getName());
        bannerDto.setDescription_1(banner.getDescription_1());
        bannerDto.setDescription_2(banner.getDescription_2());
        bannerDto.setDescription_3(banner.getDescription_3());
        bannerDto.setProduct(banner.getProduct());
        bannerDto.setEnabled(banner.isEnabled());
        bannerDto.setBannerFile(banner.getBannerFile());



        return bannerDto;
    }

    @Override
    public Banner update(BannerDto bannerDto, MultipartFile bannerImage) {
        long banner_id=bannerDto.getId();
        Banner banner=bannerRepository.findById(banner_id);
        banner.setName(bannerDto.getName());
        banner.setDescription_1(bannerDto.getDescription_1());
        banner.setDescription_2(bannerDto.getDescription_2());
        banner.setDescription_3(bannerDto.getDescription_3());
        banner.setProduct(bannerDto.getProduct());
        try{
            if (!bannerImage.isEmpty()) {
                String fileName = imageUpload.storeFile(bannerImage);
                banner.setBannerFile(fileName);
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        bannerRepository.save(banner);
        return banner;
    }

    public List<BannerDto> transferData(List<Banner> bannerList){
        List<BannerDto> BannerDtoList=new ArrayList<>();
        for(Banner banner : bannerList){
            BannerDto bannerDto=new BannerDto();
            bannerDto.setId(banner.getId());
            bannerDto.setName(banner.getName());
            bannerDto.setDescription_1(banner.getDescription_1());
            bannerDto.setDescription_2(banner.getDescription_2());
            bannerDto.setDescription_3(banner.getDescription_3());
            bannerDto.setBannerFile(banner.getBannerFile());
            bannerDto.setProduct(banner.getProduct());
            bannerDto.setEnabled(banner.isEnabled());

            BannerDtoList.add(bannerDto);

        }


        return BannerDtoList;
    }
}
