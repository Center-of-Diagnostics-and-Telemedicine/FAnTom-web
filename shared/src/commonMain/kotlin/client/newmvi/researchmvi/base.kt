package client.newmvi.researchmvi

interface BaseView<E : BaseEvent> {
  fun dispatch(event: E)
}

interface BaseEvent