package coop.bancocredicoop.guv.loader.controllers.api;

import coop.bancocredicoop.guv.loader.services.CorreccionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/cache")
public class LoaderController {

    @Autowired
    private CorreccionService correccionService;

    private static Logger log = LoggerFactory.getLogger(LoaderController.class);

    @GetMapping("/{type}")
    public Flux findAll(@PathVariable String type) {
        return this.correccionService.findAll(type);
    }

}
