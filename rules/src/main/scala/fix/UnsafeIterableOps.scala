package fix

import fix.util._

import scalafix.v1._
import scala.meta._

class UnsafeIterableOps extends SemanticRule("UnsafeIterableOps") {

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree
      .collect {
        // list.head
        case t @ DotHead(_, _) => Patch.lint(HeadDiagnostic(t))
        // list.tail
        case t @ DotTail(_, _) => Patch.lint(TailDiagnostic(t))
        // list.init
        case t @ DotInit(_, _) => Patch.lint(InitDiagnostic(t))
        // list.last
        case t @ DotLast(_, _) => Patch.lint(LastDiagnostic(t))
        // list.min
        case t @ DotMin(_, _) => Patch.lint(MinDiagnostic(t))
        // list.minBy
        case t @ DotMinBy(_, _) => Patch.lint(MinByDiagnostic(t))
        case t @ Term.Select(receiver, method: Term.Name) =>
          println(s"term: $t")
          println(s"term.structure: ${t.structure}")
          println(s"term.symbol: ${t.symbol}")
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
  )(implicit doc: SemanticDocument): Option[(Term.Select, Term)] =
    term match {
      case select @ Term.Select(
            receiver,
            Term.Name(`termName`)
          ) if getOverridenSymbols(select).contains(symbol) =>
        Some((select, receiver))
      case _ => None
    }

}
