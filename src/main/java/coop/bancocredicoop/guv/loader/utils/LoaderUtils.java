package coop.bancocredicoop.guv.loader.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LoaderUtils {

    @Autowired
    Environment env;

    public Pageable getPage(Integer actual) {
        //FIXME: ora-01795 - maximum number of expressions in a list is 1000
        return PageRequest.of(
                0,
                Integer.parseInt(Objects.requireNonNull(env.getProperty("loader.page.size"))) - actual);
    }
}
