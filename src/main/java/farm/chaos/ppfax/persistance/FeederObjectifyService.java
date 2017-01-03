package farm.chaos.ppfax.persistance;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import farm.chaos.ppfax.model.PpUser;


public class FeederObjectifyService {

    static {
    	ObjectifyService.register(PpUser.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }

}
