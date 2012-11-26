package job;

import models.User;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

/**
 * Created with IntelliJ IDEA.
 * User: ivanma
 * Date: 26.11.12
 * Time: 18:16
 * To change this template use File | Settings | File Templates.
 */
@OnApplicationStart
public class BaseJob extends Job {
	@Override
	public void doJob() throws Exception {
		if(User.count() == 0){
			Fixtures.loadModels("data.yml");
		}

	}
}
