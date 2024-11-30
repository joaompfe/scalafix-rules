package fix

import fix.util._
import scalafix.v1._

import scala.meta._

class UnsafeIterableOpsRewrite
    extends SemanticRule("UnsafeIterableOpsRewrite") {

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree
      .collect {
        case t @ IfEmptyHead(_) =>
          println(s"term: $t")
          Patch.replaceTree(t, "xs.headOption" + "")
        case t =>
//          println(s"term: $t")
//          println(s"term.structure: ${t.structure}")
          Patch.empty
      }
  }.asPatch

}

object IfEmptyHead extends IfEmpty

trait IfEmpty {

  protected val symbol: Symbol =
    Symbol("scala/collection/IterableOnceOps#isEmpty().")

  def unapply(
      term: Term
  )(implicit doc: SemanticDocument): Option[Unit] =
    term match {
      case Term.If.After_4_4_0(
            select @ Term.Select(Term.Name("xs"), Term.Name("isEmpty")),
            Term.Name("None"),
            Term.Apply.After_4_6_0(
              Term.Name("Some"),
              Term.ArgClause(
                List(Term.Select(Term.Name("xs"), Term.Name("head"))),
                None
              )
            ),
            Nil
          ) if getOverridenSymbols(select).contains(symbol) =>
        Some(())
      case _ => None
    }

}
