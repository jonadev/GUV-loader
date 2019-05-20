package coop.bancocredicoop.guv.loader.repositories.mongo;

import com.mongodb.client.result.DeleteResult;
import coop.bancocredicoop.guv.loader.models.mongo.LoaderFlag;

public interface LoaderRepository {
    void store(LoaderFlag flag);
    LoaderFlag retrieveByProcessName(String processName);
    DeleteResult deleteByProcessName(String processName);
    DeleteResult deleteByCreatedAtIsNullAndCollection(Class clazz);
}
