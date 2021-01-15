package client.newmvi.newlogin.controller

import client.domain.repository.LoginRepository
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory

class LoginDependencies(
  override val storeFactory: StoreFactory,
  override val lifecycle: Lifecycle,
  override val repository: LoginRepository
) : LoginController.Dependencies