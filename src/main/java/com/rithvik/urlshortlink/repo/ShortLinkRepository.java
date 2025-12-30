package com.rithvik.urlshortlink.repo;

import com.rithvik.urlshortlink.model.ShortLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortLinkRepository extends JpaRepository<ShortLink, Long> {
    Optional<ShortLink> findByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);
}
