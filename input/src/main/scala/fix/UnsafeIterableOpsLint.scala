/*
rule = UnsafeIterableOpsLint
 */
package fix

object UnsafeIterableOpsLint {

  List(1).head /* assert: UnsafeIterableOpsLint
          ^^^^
    .head is unsafe, consider .headOption */

  List(1).tail /* assert: UnsafeIterableOpsLint
          ^^^^
    .tail is unsafe, consider .drop(1) */

  List(1).minBy(identity) /* assert: UnsafeIterableOpsLint
          ^^^^^
    .minBy is unsafe, consider .minByOption */

  List(1).min /* assert: UnsafeIterableOpsLint
          ^^^
    .min is unsafe, consider .minOption */

  List(1).init /* assert: UnsafeIterableOpsLint
          ^^^^
    .init is unsafe, consider .dropRight(1) */

  List(1).last /* assert: UnsafeIterableOpsLint
          ^^^^
    .last is unsafe, consider .lastOption */

  val x = List(1)
  x.last /* assert: UnsafeIterableOpsLint
    ^^^^
    .last is unsafe, consider .lastOption */

  List(1)
    .filter(_ => true)
    .minBy(identity) /* assert: UnsafeIterableOpsLint
     ^^^^^
    .minBy is unsafe, consider .minByOption */
}
