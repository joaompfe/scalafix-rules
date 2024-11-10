package fix

object OptionBool {

  {
    val e = Option(true)
    e.forall(identity)
    e.forall { x =>
      x
    }
    e.exists(identity)
    e.exists { x =>
      x
    }
  }

  {
    val e = Some(true)
    e.forall(identity)
    e.forall { x =>
      x
    }
    e.exists(identity)
    e.exists { x =>
      x
    }
  }

  {
    val e = None
    e.forall(identity)
    e.forall { x =>
      x
    }
    e.exists(identity)
    e.exists { x =>
      x
    }
  }

  {
    None.forall(identity)
    None.forall { x =>
      x
    }
    None.exists(identity)
    None.exists { x =>
      x
    }
  }

  {
    None.forall(identity)
    None.exists { x =>
      x
    }
  }

}
