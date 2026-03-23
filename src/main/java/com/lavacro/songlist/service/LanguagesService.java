package com.lavacro.songlist.service;

import com.lavacro.songlist.model.LanguageEntity;
import com.lavacro.songlist.repository.LanguageRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LanguagesService {
	private final LanguageRepository languageRepository;

	LanguagesService(LanguageRepository languageRepository) {
		this.languageRepository = languageRepository;
	}

	public List<LanguageEntity> getAllLanguages() {
		log.info("getAllLanguages");
		Sort sort = Sort.by(Sort.Direction.ASC, "language");
		return languageRepository.findAll(sort);
	}
}
