package com.lavacro.songlist.controllers;

import com.lavacro.songlist.model.ActiveService;
import com.lavacro.songlist.model.ServiceEntity;

import com.lavacro.songlist.model.SongStats;

import com.lavacro.songlist.repository.ActiveServicesRepository;
import com.lavacro.songlist.service.ReportsService;
import com.lavacro.songlist.service.ServicesService;
import com.lavacro.songlist.service.SingersService;
import com.lavacro.songlist.service.SongsService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@Slf4j
public class Pages {
	private static final String SERVICE = "service";
	private static final String SONGS = "songs";

	private final SingersService singersService;
	private final SongsService songsService;
	private final ReportsService reportsService;
	private final ServicesService servicesService;
	private final ActiveServicesRepository activeServicesRepository;

	public Pages(SingersService singersService, SongsService songsService, ReportsService reportsService,
				 ActiveServicesRepository activeServicesRepository, ServicesService servicesService) {
		this.singersService = singersService;
		this.songsService = songsService;
		this.reportsService = reportsService;
		this.servicesService = servicesService;
		this.activeServicesRepository = activeServicesRepository;
	}

	@GetMapping(path = "/calendar")
	public String calendar(Model model) {
		return "calendar";
	}

	@GetMapping(path = "/makelist")
	public String makeList(Model model, @RequestParam(value = "id") Integer id) {
		model.addAttribute(SERVICE, activeServicesRepository.getOneService(id));
		return "make_list";
	}

	@PostMapping(path = "/newlist")
	public String newList(
			Model model,
			@RequestParam(value = "month") Integer month,
			@RequestParam(value = "day") Integer day,
			@RequestParam(value = "year") Integer year,
			@RequestParam(value = "service") Integer serviceId
	) {

		log.info("month={}, day={}, year={}, service={}", month, day, year, serviceId);
		ActiveService service = new ActiveService();
		LocalDate ld = LocalDate.of(year, month, day);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM d, y");
		String formatted = ld.format(dtf);
		service.setFormattedDate(formatted);
		service.setId(0);
		service.setService(servicesService.getServiceById(serviceId).getDescription());
		model.addAttribute(SERVICE, service);
		model.addAttribute("month", month);
		model.addAttribute("day", day);
		model.addAttribute("year", year);
		model.addAttribute("serviceType", serviceId);
		model.addAttribute("leaders", singersService.getLeaders());
		return "make_list";
	}

	@GetMapping(path = "/print")
	public String print(Model model, @RequestParam("serviceId") Integer id) {
		model.addAttribute(SONGS, songsService.selectedSongs(id));
		model.addAttribute(SERVICE, activeServicesRepository.getOneService(id));
		return "print";
	}

	@GetMapping(path = "/setList")
	public String setList(
			Model model,
			@RequestParam(value = SERVICE, required = false) final Integer service) {
		if(service == null) {
			model.addAttribute(SERVICE, null);
		} else {
			ServiceEntity svc = servicesService.getServiceById(service);
			model.addAttribute(SERVICE, svc);
		}
		model.addAttribute("services", servicesService.getAllServices());
		return "sets";
	}

	@GetMapping(path = "/songStats")
	public String songStats(
			Model model,
			@RequestParam(value = SERVICE) Integer service) {
		SongStats stats = reportsService.chartTopppers(service);
		model.addAttribute("stats", stats.getSongStat());
		return "songStats";
	}

	@GetMapping(path = "/")
	public String home() {
		return "home";
	}
}
