/*
rule = UnsafeIterableOpsRewrite
 */
package fix

object UnsafeIterableOpsRewrite {

  val xs = List()

  if (xs.isEmpty) None
  else Some(xs.head)

}
