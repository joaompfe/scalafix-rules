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

}
