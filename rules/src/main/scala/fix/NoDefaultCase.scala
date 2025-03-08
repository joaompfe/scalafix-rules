package fix

import scalafix.v1._

import scala.meta._

class NoDefaultCase extends SemanticRule("NoDefaultCase") {

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree
      .collect {
        case NoDefaultCase(t) => Patch.lint(NoDefaultDiagnostic(t))
        case _                => Patch.empty
      }
  }.asPatch

}

final case class NoDefaultDiagnostic(t: Case) extends Diagnostic {
  override def position: Position = {
    Position.Range(t.pos.input, t.pos.start, t.pat.pos.end)
  }

  override def message: String =
    "Don't be lazy!"
}

object NoDefaultCase {

  def containsWildcard(p: Pat): Boolean = p match {
    case _: Pat.Wildcard    => true
    case _: Pat.SeqWildcard => true
    case _: Pat.Var => false // assuming it is fully handled in the case body...
    case Pat.Bind(_, pat)        => containsWildcard(pat)
    case Pat.Alternative(p1, p2) => containsWildcard(p1) || containsWildcard(p2)
    case Pat.Tuple(pats)         => pats.exists(containsWildcard)
    case e: Pat.Extract          => e.argClause.values.exists(containsWildcard)
    case e: Pat.ExtractInfix =>
      containsWildcard(e.lhs) || e.argClause.values.exists(containsWildcard)
    case _: Pat.Interpolate =>
      false // assuming any Pat.Var is fully handled in the case body...
    case _: Pat.Xml   => false
    case _: Pat.Typed => true
    case _            => false
  }

  def unapply(t: Tree): Option[Case] =
    t match {
      case c: Case if containsWildcard(c.pat) => Some(c)
      case _                                  => None
    }
}
