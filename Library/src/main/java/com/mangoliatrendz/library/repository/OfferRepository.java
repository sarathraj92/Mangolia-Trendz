package com.mangoliatrendz.library.repository;

import com.mangoliatrendz.library.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer,Long> {


}
