package context

import jakarta.inject.{Inject, Singleton}
import org.apache.pekko.actor.ActorSystem
import play.api.libs.concurrent.CustomExecutionContext

@Singleton
class ServiceExecutionContext @Inject()(system: ActorSystem) extends CustomExecutionContext(system, "service.dispatcher")
