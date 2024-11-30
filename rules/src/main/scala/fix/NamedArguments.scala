package fix

import metaconfig.{ConfDecoder, Configured}
import metaconfig.generic.Surface
import scalafix.v1._
import scala.meta._

case class NamedArgumentsConfig(arity: Int = 5)
object NamedArgumentsConfig {
  private def default = NamedArgumentsConfig()
  implicit val surface: Surface[NamedArgumentsConfig] =
    metaconfig.generic.deriveSurface[NamedArgumentsConfig]
  implicit val decoder: ConfDecoder[NamedArgumentsConfig] =
    metaconfig.generic.deriveDecoder(default)
}

class NamedArguments(config: NamedArgumentsConfig)
    extends SemanticRule("NamedArguments") {
  override def withConfiguration(config: Configuration): Configured[Rule] =
    config.conf
      .getOrElse("NamedArguments")(this.config)
      .map { newConfig => new NamedArguments(newConfig) }

  def this() = this(NamedArgumentsConfig())

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree
      .collect { case Term.Apply.After_4_6_0(fun, args) =>
        args.zipWithIndex.collect {
          case (Term.Assign(_, _), _) => Patch.empty // already named
          case (Term.Placeholder(), _) =>
            Patch.empty // placeholders cannot be named
          case (t, i) =>
            fun.symbol.info match {
              case Some(info) =>
                info.signature match {
                  case method: MethodSignature
                      if method.parameterLists.nonEmpty && info.isScala =>
                    val params = method.parameterLists(curries(fun))
                    if (params.size >= config.arity) {
                      val name = params(i).displayName
                      Patch.addLeft(t, s"$name = ").atomic
                    } else {
                      Patch.empty
                    }
                  case _ =>
                    Patch.empty
                }
              case None =>
                Patch.empty
            }
        }
      }
  }.flatten.asPatch

  private def curries(t: Term): Int = t match {
    case Term.Apply.After_4_6_0(f, _) => 1 + curries(f)
    case _                            => 0
  }

}
