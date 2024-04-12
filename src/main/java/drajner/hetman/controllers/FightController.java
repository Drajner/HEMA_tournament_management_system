package drajner.hetman.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tournaments/{tournamentNumber}/group/{groupNumber}/fight/{fightNumber}")
@Log4j2
public class FightController {



}
