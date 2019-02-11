package coop.bancocredicoop.guv.loader.services.jobs;

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

    void loadImporte(){
        importeService.count()
                .map(this::evalSize)
                .flatMap(bool -> bool ? importeService.doLoad() : Mono.empty())
                .subscribe();
    }

    void loadCUIT(){
        cuitService.count()
                .map(this::evalSize)
                .flatMap(bool -> bool ? cuitService.doLoad() : Mono.empty())
                .subscribe();
    }

    void loadCMC7(){
        cmc7Service.count()
                .map(this::evalSize)
                .flatMap(bool -> bool ? cmc7Service.doLoad() : Mono.empty())
                .subscribe();
    }

    void loadFecha(){
        fechaService.count()
                .map(this::evalSize)
                .flatMap(bool -> bool ? fechaService.doLoad() : Mono.empty())
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
        return total == 0L || (100 * total / pageSize) <= percentageSize;
    }
}
