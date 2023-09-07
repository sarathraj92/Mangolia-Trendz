package com.mangoliatrendz.library.service;

import com.mangoliatrendz.library.dto.BannerDto;
import com.mangoliatrendz.library.dto.CouponDto;
import com.mangoliatrendz.library.model.Banner;
import com.mangoliatrendz.library.model.Coupon;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BannerService {

    Banner save(BannerDto bannerDto, MultipartFile bannerImage);

    List<BannerDto> getAllBanners();

    BannerDto findById(long id);

    Banner update(BannerDto bannerDto,MultipartFile bannerImage);

    void disable(long id);

    void enable(long id);

    void deleteBanner(long id);
}
