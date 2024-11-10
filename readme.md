# Scalafix Rules WIP!!!

## Linters

### UnsafeIterableOps

```scala
List(1).head /* assert: UnsafeIterableOps
^^^^^^^^^^^^
  .head is unsafe, consider .headOption */

List(1).tail /* assert: UnsafeIterableOps
^^^^^^^^^^^^
  .tail is unsafe, consider .drop(1) */

List(1).minBy(identity) /* assert: UnsafeIterableOps
^^^^^^^^^^^^^
  .minBy is unsafe, consider .minByOption */
```

## Rewrites

### OptionBool
```diff
val x: Option[Boolean] = ???

- x.fold(true)(f)
+ a x.forall(f)

- x.fold(false)(f)
+ x.exists(f)
```