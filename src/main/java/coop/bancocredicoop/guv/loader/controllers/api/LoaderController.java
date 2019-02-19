package coop.bancocredicoop.guv.loader.controllers.api;

import coop.bancocredicoop.guv.loader.models.mongo.*;
import coop.bancocredicoop.guv.loader.services.CorreccionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@CrossOrigin
@RestController
@RequestMapping("/api/cache")
public class LoaderController {

    @Autowired
    private CorreccionService correccionCMC7Service;

    private static Logger log = LoggerFactory.getLogger(LoaderController.class);

    @GetMapping("/{type}")
    public Flux<? extends Correccion> findAllCMC7(@PathVariable("type") String type) {
        return this.correccionCMC7Service.findAll(type);
    }

}
