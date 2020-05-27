package view

import view.LoginView.Event
import view.LoginView.Model

class TestLoginView : TestMviView<Model, Event>(), LoginView
