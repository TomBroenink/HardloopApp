package filters;

import play.filters.cors.CORSFilter;
import play.http.DefaultHttpFilters;

import javax.inject.Inject;

/**
 * Created by hbh13 on 14-11-2016.
 */
public class Filters extends DefaultHttpFilters {
    @Inject
    public Filters (CORSFilter corsFilter){
        super(corsFilter);
    }
}
