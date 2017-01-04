package farm.chaos.ppfax.persistance;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import farm.chaos.ppfax.model.Article;
import farm.chaos.ppfax.model.Category;
import farm.chaos.ppfax.model.Paragraph;
import farm.chaos.ppfax.model.PpUser;


public class FeederObjectifyService {

    static {
    	ObjectifyService.register(Category.class);
    	ObjectifyService.register(Article.class);
    	ObjectifyService.register(Paragraph.class);
    	ObjectifyService.register(PpUser.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }

}
