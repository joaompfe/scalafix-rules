package fix

import scalafix.v1._
import scala.meta._

private[fix] object util {

  private def getParentSymbols(
      symbol: Symbol
  )(implicit doc: SemanticDocument): Set[Symbol] =
    symbol.info.get.signature match {
      case ClassSignature(_, parents, _, _) =>
        Set(symbol) ++ parents.collect { case TypeRef(_, symbol, _) =>
          getParentSymbols(symbol)
        }.flatten
      case _ => Set.empty
    }

  private def getClassMethods(
      symbol: Symbol
  )(implicit doc: SemanticDocument): Set[SymbolInformation] =
    symbol.info.get.signature match {
      case ClassSignature(_, parents, _, declarations) =>
        val methods = declarations.filter(_.isMethod)
        methods.toSet ++ parents.collect { case TypeRef(_, symbol, _) =>
          getClassMethods(symbol)
        }.flatten
      case _ => Set.empty
    }

  private def getParameterList(
      s: Symbol
  )(implicit doc: SemanticDocument): List[List[SymbolInformation]] =
    s.info.get.signature match {
      case MethodSignature(_, params, _) => params
    }

  private def getOverridenSymbolsAux(
      symbol: Symbol
  )(implicit doc: SemanticDocument): Set[Symbol] = {
    val name = symbol.displayName
    getParentSymbols(symbol.owner)
      .flatMap(getClassMethods)
      .map(_.symbol)
      .filter { s =>
        s.displayName == name && getParameterList(s).map(
          _.map(_.symbol)
        ) == getParameterList(symbol).map(_.map(_.symbol))
      }
  }

  def getOverridenSymbols(
      term: Term.Select
  )(implicit doc: SemanticDocument): Set[Symbol] = term match {
    case Term.Select(_, method) =>
      getOverridenSymbolsAux(method.symbol)
  }

  def getUpperOverridenSymbol(
      term: Term.Select
  )(implicit doc: SemanticDocument): Symbol = term match {
    case Term.Select(_, method) =>
      getOverridenSymbolsAux(method.symbol)
        .map(getOverridenSymbolsAux)
        .reduce(_ `intersect` _)
        .head
  }

}
