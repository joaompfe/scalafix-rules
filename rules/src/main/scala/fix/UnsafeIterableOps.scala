package fix

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
        // list.minBy
        case t @ DotMinBy(_, _) => Patch.lint(MinByDiagnostic(t))
        case t @ Term.Select(receiver, term: Term.Name) =>
          println(s"term: ${term.symbol}")
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

object DotMinBy extends SelectMatching {
  override val symbol = Symbol("scala/collection/IterableOnceOps#minBy().")
}

final case class MinByDiagnostic(t: Tree) extends Diagnostic {
  override def position: Position = t.pos
  override def message: String =
    s".minBy is unsafe, consider .minByOption"
}

trait SelectMatching {

  protected val symbol: Symbol
  private lazy val termName: String = symbol.displayName
  private lazy val symbolMatcher: SymbolMatcher =
    SymbolMatcher.exact(symbol.value)

  def unapply(
      term: Term
  )(implicit doc: SemanticDocument): Option[(Term.Select, Term)] =
    term match {
      case select @ Term.Select(
            receiver,
            term @ Term.Name(`termName`)
          ) if symbolMatcher.matches(term) =>
        Some((select, receiver))
      case _ => None
    }

}
