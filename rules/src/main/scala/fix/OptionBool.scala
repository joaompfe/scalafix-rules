package fix

import scalafix.v1._
import scala.meta._

class OptionBool extends SemanticRule("OptionBool") {

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree
      .collect {
        case t @ OptionBoolDotFold(receiver, bool) =>
          val replacement = if (bool) "forall" else "exists"
          Patch.replaceTree(t, s"${receiver.value}.$replacement")
        case _ =>
          Patch.empty
      }
  }.asPatch

}

object OptionBoolDotFold {

  private val symbolMatcher: SymbolMatcher =
    SymbolMatcher.exact("scala/Option#fold().")

  def unapply(
      term: Term
  )(implicit doc: SemanticDocument): Option[(Term.Name, Boolean)] =
    term match {
      case Term.Apply.After_4_6_0(
            Term.Select(receiver: Term.Name, Term.Name("fold")),
            Term.ArgClause(args, _)
          ) if symbolMatcher.matches(term) =>
        val boolArg = args match {
          case Term.Assign(Term.Name("ifEmpty"), Lit.Boolean(bool)) :: Nil =>
            Some(bool)
          case Lit.Boolean(bool) :: Nil => Some(bool)
          case _                        => None
        }
        boolArg.map(bool => (receiver, bool))
      case _ => None
    }

}
