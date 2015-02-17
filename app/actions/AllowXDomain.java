package actions;

import play.Logger;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.libs.F;
/**
 * Allowing cross domain for API.
 * 
 * @author christopher@back.no
 */
public class AllowXDomain extends Action.Simple {
	
	public static final String LANG_SESSION_KEY = "lang";
	
	public F.Promise<Result> call(Http.Context ctx) throws Throwable {
		
		ctx.response().setHeader("Access-Control-Allow-Origin", "*");		
		ctx.response().setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		ctx.response().setHeader("Access-Control-Allow-Headers", "Content-Type, X-Requested-With, x-requested-with, Accept, Authorization");
		
		Logger.debug("Adding headers");
		return delegate.call(ctx);
	}
}