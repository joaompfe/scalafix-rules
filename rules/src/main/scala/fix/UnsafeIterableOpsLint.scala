package fix

import fix.util._

import scalafix.v1._
import scala.meta._

class UnsafeIterableOpsLint extends SemanticRule("UnsafeIterableOpsLint") {

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree
      .collect {
        // list.head
        case DotHead(op) => Patch.lint(HeadDiagnostic(op))
        // list.tail
        case DotTail(op) => Patch.lint(TailDiagnostic(op))
        // list.init
        case DotInit(op) => Patch.lint(InitDiagnostic(op))
        // list.last
        case DotLast(op) => Patch.lint(LastDiagnostic(op))
        // list.min
        case DotMin(op) => Patch.lint(MinDiagnostic(op))
        // list.minBy
        case DotMinBy(op) => Patch.lint(MinByDiagnostic(op))
        case t @ Term.Select(receiver, method: Term.Name) =>
//          println(s"term: $t")
//          println(s"term.structure: ${t.structure}")
//          println(s"term.symbol: ${t.symbol}")
          Patch.empty
      }
  }.asPatch

}

object DotHead extends SelectMatching {
  override val symbol = Symbol("scala/collection/IterableOps#head().")
}

final case class HeadDiagnostic(t: Tree) extends Diagnostic {
  override def position: Position = t.pos
  override def message: String =
    s".head is unsafe, consider .headOption"
}

object DotTail extends SelectMatching {
  override val symbol = Symbol("scala/collection/IterableOps#tail().")
}

final case class TailDiagnostic(t: Tree) extends Diagnostic {
  override def position: Position = t.pos
  override def message: String =
    s".tail is unsafe, consider .drop(1)"
}

object DotInit extends SelectMatching {
  override val symbol = Symbol("scala/collection/IterableOps#init().")
}

final case class InitDiagnostic(t: Tree) extends Diagnostic {
  override def position: Position = t.pos
  override def message: String =
    s".init is unsafe, consider .dropRight(1)"
}

object DotLast extends SelectMatching {
  override val symbol = Symbol("scala/collection/IterableOps#last().")
}

final case class LastDiagnostic(t: Tree) extends Diagnostic {
  override def position: Position = t.pos
  override def message: String =
    s".last is unsafe, consider .lastOption"
}

object DotMinBy extends SelectMatching {
  override val symbol = Symbol("scala/collection/IterableOnceOps#minBy().")
}

final case class MinByDiagnostic(t: Tree) extends Diagnostic {
  override def position: Position = t.pos
  override def message: String =
    s".minBy is unsafe, consider .minByOption"
}

object DotMin extends SelectMatching {
  override val symbol = Symbol("scala/collection/IterableOnceOps#min().")
}

final case class MinDiagnostic(t: Tree) extends Diagnostic {
  override def position: Position = t.pos
  override def message: String =
    s".min is unsafe, consider .minOption"
}

trait SelectMatching {

  protected val symbol: Symbol
  private lazy val termName: String = symbol.displayName

  def unapply(
      term: Term
  )(implicit doc: SemanticDocument): Option[Term.Name] =
    term match {
      case select @ Term.Select(
            _,
            method @ Term.Name(`termName`)
          ) if getOverridenSymbols(select).contains(symbol) =>
        Some(method)
      case _ => None
    }

}
