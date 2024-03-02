package api

import context.ServiceExecutionContext
import jakarta.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

@Singleton
class HomePageWebService @Inject()(protected val cc: ControllerComponents, implicit val ec: ServiceExecutionContext) extends AbstractController(cc) {
    def index: Action[AnyContent] = Action {
        Ok("hello-scala-backend is running")
    }
}
