/*
rule = UnsafeIterableOps
 */
package fix

object UnsafeIterableOps {

  List(1).head /* assert: UnsafeIterableOps
  ^^^^^^^^^^^^
    .head is unsafe, consider .headOption */

  List(1).tail /* assert: UnsafeIterableOps
  ^^^^^^^^^^^^
    .tail is unsafe, consider .drop(1) */

  List(1).minBy(identity) /* assert: UnsafeIterableOps
  ^^^^^^^^^^^^^
    .minBy is unsafe, consider .minByOption */

  List(1).min /* assert: UnsafeIterableOps
  ^^^^^^^^^^^
    .min is unsafe, consider .minOption */

  List(1).init /* assert: UnsafeIterableOps
  ^^^^^^^^^^^^
    .init is unsafe, consider .dropRight(1) */

  List(1).last /* assert: UnsafeIterableOps
  ^^^^^^^^^^^^
    .last is unsafe, consider .lastOption */

  val x = List(1)
  x.last /* assert: UnsafeIterableOps
  ^^^^^^
    .last is unsafe, consider .lastOption */

  List(1).filter(_ => true)
    .minBy(identity) /* assert: UnsafeIterableOps
    ^^^^^^^^^^^^^^^^
    .minBy is unsafe, consider .minByOption */
}
