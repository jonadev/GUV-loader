package coop.bancocredicoop.guv.loader.services.jobs;

import com.mongodb.client.result.DeleteResult;
import coop.bancocredicoop.guv.loader.models.Proceso;
import coop.bancocredicoop.guv.loader.models.mongo.CorreccionCUIT;
import coop.bancocredicoop.guv.loader.models.mongo.CorreccionFecha;
import coop.bancocredicoop.guv.loader.models.mongo.CorreccionImporte;
import coop.bancocredicoop.guv.loader.models.mongo.LoaderFlag;
import coop.bancocredicoop.guv.loader.repositories.mongo.implementations.LoaderRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

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

    @Value("${loader.page.size:200}")
    private Integer pageSize;

    @Value("${loader.mongo.percentage:50}")
    private Integer percentage;

    private final static Logger LOGGER = LoggerFactory.getLogger(LoaderService.class);

    void loadImporte(){
        initExecution(Proceso.IMPORTE)
            .flatMap(importeService::getSize)
            .map(this::evalSize)
            .flatMap(load -> load ? importeService.doLoad() : Mono.empty())
            .doFinally(finish -> finishExecution(Proceso.IMPORTE))
            .subscribe();
    }

    void loadCUIT(){
        initExecution(Proceso.CUIT)
                .flatMap(cuitService::getSize)
                .map(this::evalSize)
                .flatMap(load -> load ? cuitService.doLoad() : Mono.empty())
                .doFinally(finish -> finishExecution(Proceso.CUIT))
                .subscribe();
    }

    void loadCMC7(){
        initExecution(Proceso.CMC7)
                .flatMap(cmc7Service::getSize)
                .map(this::evalSize)
                .flatMap(load -> load ? cmc7Service.doLoad() : Mono.empty())
                .doFinally(finish -> finishExecution(Proceso.CMC7))
                .subscribe();
    }

    void loadFecha(){
        initExecution(Proceso.FECHA)
                .flatMap(fechaService::getSize)
                .map(this::evalSize)
                .flatMap(load -> load ? fechaService.doLoad() : Mono.empty())
                .doFinally(finish -> finishExecution(Proceso.FECHA))
                .subscribe();
    }

    /**
     *  Verifica si se necesita cargar mas cheques.
     *
     *  100% ------ pageSize
     *   X   ------ total
     *
     *   X <= percentage
     *
     * @return Boolean
     */
    private Boolean evalSize(Long total){
        return total >= 0L && (100 * total / pageSize) <= percentage;
    }

    private Mono<Boolean> initExecution(Proceso proceso) {
        LOGGER.debug("Initializing process: " + proceso.name());
        LoaderFlag flag = loaderRepository.retrieveByProcessName(proceso.name());
        if (flag == null)
            loaderRepository.store(new LoaderFlag(proceso.name()));
        return Mono.just(flag == null);
    }

    //TODO: logging at omission
    private Boolean finishExecution(Proceso proceso) {
        return loaderRepository.deleteByProcessName(proceso.name()).wasAcknowledged();
    }

    public void refreshPriority() {
         Stream.of(
            CorreccionImporte.class,
            CorreccionCUIT.class,
            CorreccionCUIT.class,
            CorreccionFecha.class)
                 .map(loaderRepository::deleteByCreatedAtIsNullAndCollection)
                 .map(DeleteResult::wasAcknowledged)
                 .forEach(result ->
                         LOGGER.info(result ? "A Collection has been updated" : "Couldn't update the collection"));
    }
}
