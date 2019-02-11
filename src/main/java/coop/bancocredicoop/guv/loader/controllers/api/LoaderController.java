package coop.bancocredicoop.guv.loader.controllers.api;

import coop.bancocredicoop.guv.loader.models.mongo.CorreccionImporte;
import coop.bancocredicoop.guv.loader.services.CorreccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/cache")
public class LoaderController {

    @Autowired
    private CorreccionService correccionService;

    @GetMapping
    public Flux<CorreccionImporte> findAll() {
        return this.correccionService.findAll();
    }

}
