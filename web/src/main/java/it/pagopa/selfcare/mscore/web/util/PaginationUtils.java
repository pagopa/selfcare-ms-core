package it.pagopa.selfcare.mscore.web.util;

import java.util.Collections;
import java.util.List;

public class PaginationUtils {

    private PaginationUtils(){
    }

    public static <T> List<T> paginate(List<T> list, Integer pageSize, Integer pageNumber){
        if(pageSize < 0 || pageNumber < 0){
            throw new IllegalArgumentException("invalid page size: " + pageSize);
        }
        int start =  (pageNumber - 1) * pageSize;
        int end = Math.min(start + pageSize, list.size());
        if(list.size() <= start){
            return Collections.emptyList();
        }
        return list.subList(start, end);
    }

}
