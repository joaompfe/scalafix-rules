/*
rule = OptionBool
*/
package fix

object OptionBool {

  {
    val e = Option(true)
    e.fold(true)(identity)
    e.fold(true) { x =>
      x
    }
    e.fold(false)(identity)
    e.fold(false) { x =>
      x
    }
  }

  {
    val e = Some(true)
    e.fold(true)(identity)
    e.fold(true) { x =>
      x
    }
    e.fold(false)(identity)
    e.fold(false) { x =>
      x
    }
  }

  {
    val e = None
    e.fold(true)(identity)
    e.fold(true) { x =>
      x
    }
    e.fold(false)(identity)
    e.fold(false) { x =>
      x
    }
  }

  {
    None.fold(true)(identity)
    None.fold(true) { x =>
      x
    }
    None.fold(false)(identity)
    None.fold(false) { x =>
      x
    }
  }

}
