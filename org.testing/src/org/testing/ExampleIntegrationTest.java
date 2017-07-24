package org.testing;

import static org.ops4j.pax.exam.CoreOptions.composite;

import org.junit.Rule;
import org.junit.Test;
import org.ops4j.net.FreePort;
import org.ops4j.pax.exam.acceptance.ClientConfiguration;
import org.ops4j.pax.exam.acceptance.SessionSpec;
import org.ops4j.pax.exam.acceptance.junit4.OSGiTestSubjectRule;
import org.ops4j.pax.exam.acceptance.junit4.TestSubjectRule;
import org.ops4j.pax.exam.acceptance.rest.api.RestClient;
import org.ops4j.pax.exam.bnd.BndtoolsOptions;

public class ExampleIntegrationTest {
	public @Rule TestSubjectRule subject = new OSGiTestSubjectRule(
			composite(BndtoolsOptions.workspace().fromBndrun("org.hello.provider/launch.bndrun")),
			getSessionSpec());

	// kinda like docker port exposure?
	private SessionSpec getSessionSpec() {
		SessionSpec sessionSpec = new SessionSpec();
		sessionSpec.setHost("localhost");
		sessionSpec.setPort(new FreePort(8280, 8380).getPort());
		return sessionSpec;
	}

	@Test
	public void workflowTest() {
		ClientConfiguration correct = new ClientConfiguration("admin", "admin");
		RestClient rest = subject.createClient(RestClient.class, correct);
		rest.get("/foo").then().statusCode(404);
		rest.get("/system/console").then().statusCode(200);
	}
}