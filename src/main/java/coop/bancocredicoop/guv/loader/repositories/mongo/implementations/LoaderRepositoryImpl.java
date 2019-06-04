package coop.bancocredicoop.guv.loader.repositories.mongo.implementations;

import com.mongodb.client.result.DeleteResult;
import coop.bancocredicoop.guv.loader.models.mongo.LoaderFlag;
import coop.bancocredicoop.guv.loader.repositories.mongo.LoaderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class LoaderRepositoryImpl implements LoaderRepository {

    private static Logger log = LoggerFactory.getLogger(LoaderRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    private final static String PROCESS_FIELD = "process";

    @Override
    public void store(LoaderFlag flag) {
        log.debug("Storing loader flag: " + flag);
        mongoTemplate.save(flag);
    }

    @Override
    public LoaderFlag retrieveByProcessName(String processName) {
        Query q = new Query(
                    Criteria
                        .where(PROCESS_FIELD)
                        .is(processName));

        return mongoTemplate.findOne(q, LoaderFlag.class);
    }

    @Override
    public DeleteResult deleteByProcessName(String processName) {
        Query q = new Query(
                    Criteria
                        .where(PROCESS_FIELD)
                        .is(processName));

        log.debug("Removing loader flag for process: " + processName);
        return mongoTemplate.remove(q, LoaderFlag.class);
    }

    @Override
    public DeleteResult deleteByCreatedAtIsNullAndCollection(Class clazz){
        Query q = new Query(
                Criteria.where("createdAt").is(null));
        return mongoTemplate.remove(q,clazz);
    }

}
