/*
rule = NoDefaultCase
 */
package fix

object NoDefaultCase {

  val x: Option[Int] = Some(1)
  1 match {
    case _ => 0 /* assert: NoDefaultCase
    ^^^^^^
    Don't be lazy! */
  }
  1 match {
    case _ => /* assert: NoDefaultCase
    ^^^^^^
    Don't be lazy! */
      val y = 1
      y - 1
  }

  1 match {
    case x @ _ => x /* assert: NoDefaultCase
    ^^^^^^^^^^
    Don't be lazy! */
  }

  1 match {
    case 1 | _ => 0 /* assert: NoDefaultCase
    ^^^^^^^^^^
    Don't be lazy! */
  }

  (1, 2) match {
    case (1, _) => 0 /* assert: NoDefaultCase
    ^^^^^^^^^^^
    Don't be lazy! */
  }

  (1, 2) match {
    case _ Tuple2 1 => 0 /* assert: NoDefaultCase
    ^^^^^^^^^^^^^^^
    Don't be lazy! */
  }

  Some(1) match {
    case Some(_) => 0 /* assert: NoDefaultCase
    ^^^^^^^^^^^^
    Don't be lazy! */
  }

  List(1, 2) match {
    case List(xs @ _*) => 0 /* assert: NoDefaultCase
    ^^^^^^^^^^^^^^^^^^
    Don't be lazy! */
  }

  1 match {
    case _: Int => 0 /* assert: NoDefaultCase
    ^^^^^^^^^^^
    Don't be lazy! */
  }

}
