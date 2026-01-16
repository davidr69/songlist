package com.lavacro.songlist.service;

import com.lavacro.songlist.model.LanguageEntity;
import com.lavacro.songlist.repository.LanguageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguagesService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LanguagesService.class);

	private final LanguageRepository languageRepository;

	LanguagesService(LanguageRepository languageRepository) {
		this.languageRepository = languageRepository;
	}

	public List<LanguageEntity> getAllLanguages() {
		LOGGER.info("getAllLanguages");
		Sort sort = Sort.by(Sort.Direction.ASC, "language");
		return languageRepository.findAll(sort);
	}
}
