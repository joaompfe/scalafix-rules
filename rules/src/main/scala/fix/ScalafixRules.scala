package fix

import scalafix.v1._
import scala.meta._

class ScalafixRules extends SemanticRule("ScalafixRules") {

  override def fix(implicit doc: SemanticDocument): Patch = {
//    println("Tree.syntax: " + doc.tree.syntax)
//    println("Tree.structure: " + doc.tree.structure)
//    println("Tree.structureLabeled: " + doc.tree.structureLabeled)
    Patch.empty
  }

}
