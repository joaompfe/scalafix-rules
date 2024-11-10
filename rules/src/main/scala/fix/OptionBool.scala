package fix

import scalafix.v1._
import scala.meta._

class OptionBool extends SemanticRule("OptionBool") {

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree
      .collect {
        case e if e.symbol == Symbol("scala/Option#fold().") =>

          e match {
            case q"$e1.fold(false)" =>
              Patch.replaceTree(e, s"${e1.pos.text}.exists")
            case q"$e1.fold(true)" =>
              Patch.replaceTree(e, s"${e1.pos.text}.forall")
            case _ =>
              Patch.empty
          }
      }
  }.asPatch

}
