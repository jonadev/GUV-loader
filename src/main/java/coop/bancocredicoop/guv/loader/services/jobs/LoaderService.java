package coop.bancocredicoop.guv.loader.services.jobs;

import coop.bancocredicoop.guv.loader.models.Proceso;
import coop.bancocredicoop.guv.loader.models.mongo.LoaderFlag;
import coop.bancocredicoop.guv.loader.repositories.mongo.implementations.LoaderRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class LoaderService {

    @Autowired
    private Environment env;

    @Autowired
    private ImporteService importeService;

    @Autowired
    private CUITService cuitService;

    @Autowired
    private CMC7Service cmc7Service;

    @Autowired
    private FechaService fechaService;

    @Autowired
    private LoaderRepositoryImpl loaderRepository;

    private static Logger log = LoggerFactory.getLogger(LoaderService.class);

    void loadImporte(){
        initExecution(Proceso.IMPORTE)
            .flatMap(importeService::getSize)
            .map(this::evalSize)
            .flatMap(load -> load ? importeService.doLoad() : Mono.empty())
            .map(finish -> finishExecution(Proceso.IMPORTE))
            .subscribe();
    }

    void loadCUIT(){
        initExecution(Proceso.CUIT)
                .flatMap(cuitService::getSize)
                .map(this::evalSize)
                .flatMap(load -> load ? cuitService.doLoad() : Mono.empty())
                .map(finish -> finishExecution(Proceso.CUIT))
                .subscribe();
    }

    void loadCMC7(){
        initExecution(Proceso.CMC7)
                .flatMap(cmc7Service::getSize)
                .map(this::evalSize)
                .flatMap(load -> load ? cmc7Service.doLoad() : Mono.empty())
                .map(finish -> finishExecution(Proceso.CMC7))
                .subscribe();
    }

    void loadFecha(){
        initExecution(Proceso.FECHA)
                .flatMap(fechaService::getSize)
                .map(this::evalSize)
                .flatMap(load -> load ? fechaService.doLoad() : Mono.empty())
                .map(finish -> finishExecution(Proceso.FECHA))
                .subscribe();
    }

    /**
     *  Verifica si se necesita cargar mas cheques.
     *
     *  100% ------ pageSize
     *   X   ------ total
     *
     *   X <= percentageSize
     *
     * @return Boolean
     */
    private Boolean evalSize(Long total){
        int pageSize,percentageSize;
        try{
            pageSize = Integer.parseInt(Objects.requireNonNull(env.getProperty("loader.page.size")));
            percentageSize = Integer.parseInt(Objects.requireNonNull(env.getProperty("loader.mongo.percentage")));
        }catch (Exception e) {
            pageSize = 200;
            percentageSize = 50;
        }
        return total >= 0L && (100 * total / pageSize) <= percentageSize;
    }

    private Mono<Boolean> initExecution(Proceso proceso) {
        log.debug("Initializing process: " + proceso.name());
        LoaderFlag flag = loaderRepository.retrieveByProcessName(proceso.name());
        if(flag == null)
            loaderRepository.store(new LoaderFlag(proceso.name()));
        return Mono.just(flag == null);
    }

    //TODO: logging at omission
    private Boolean finishExecution(Proceso proceso){
        return loaderRepository.deleteByProcessName(proceso.name()).wasAcknowledged();
    }
}
