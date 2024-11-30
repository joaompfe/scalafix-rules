/*
rule = NamedArguments
 */
package fix

object NamedArguments {

  def f0(): Unit = ()
  def f1(x1: Int): Unit = ()
  def f2(x1: Int, x2: Int): Unit = ()
  def f3(x1: Int, x2: Int, x3: Int): Unit = ()
  def f4(x1: Int, x2: Int, x3: Int, x4: Int): Unit = ()
  def f5(x1: Int, x2: Int, x3: Int, x4: Int, x5: Int): Unit = ()
  def f6(x1: Int, x2: Int, x3: Int, x4: Int, x5: Int, x6: Int): Unit = ()

  f0()
  f1(1)
  f2(1, 2)
  f3(1, 2, 3)
  f4(1, 2, 3, 4)
  f5(1, 2, 3, 4, 5)
  f6(1, 2, 3, 4, 5, 6)

  def f00()(): Unit = ()
  def f15(x01: Int)(x11: Int, x12: Int, x13: Int, x14: Int, x15: Int): Unit = ()

  f00()()
  f15(1)(1, 2, 3, 4, 5)

  f5(1, 2, x3 = 3, x4 = 4, x5 = 5)

  val x = Seq(1, 2, 3, 4, 5)

  x.map(f5(_, 2, 3, 4, 5))

}
