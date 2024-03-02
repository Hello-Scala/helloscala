package context

import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}

class AppModule(environment: Environment, configuration: Configuration) extends AbstractModule {
    override def configure(): Unit = {
        super.configure()
    }
}
