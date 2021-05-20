package decompose

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.lifecycle.LifecycleRegistry
import com.arkivanov.decompose.lifecycle.destroy
import com.arkivanov.decompose.lifecycle.resume
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.ccfraser.muirwik.components.mContainer
import com.ccfraser.muirwik.components.mCssBaseline
import com.ccfraser.muirwik.components.styles.Breakpoint
import decompose.myroot.MyRoot
import decompose.myroot.MyRootR
import local.LoginLocalDataSource
import local.ResearchLocalDataSource
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import remote.LoginRemoteDataSource
import remote.ResearchRemoteDataSource
import repository.LoginRepository
import repository.LoginRepositoryImpl
import repository.ResearchRepository
import repository.ResearchRepositoryImpl


class App : RComponent<RProps, RState>() {

  private val lifecycle = LifecycleRegistry()
  private val ctx = DefaultComponentContext(lifecycle = lifecycle)

  val loginRepositoryImpl = LoginRepositoryImpl(
    local = LoginLocalDataSource,
    remote = LoginRemoteDataSource
  )
  val researchRepositoryImpl = ResearchRepositoryImpl(
    local = ResearchLocalDataSource,
    remote = ResearchRemoteDataSource,
    token = loginRepositoryImpl.local::getToken
  )

  private val myRoot = MyRoot(ctx, dependencies = object : MyRoot.Dependencies {
    override val storeFactory: StoreFactory = DefaultStoreFactory
    override val repository: LoginRepository = loginRepositoryImpl
    override val researchRepository: ResearchRepository = researchRepositoryImpl
  })

  override fun componentDidMount() {
    lifecycle.resume()
  }

  override fun componentWillUnmount() {
    lifecycle.destroy()
  }

  override fun RBuilder.render() {
    mCssBaseline()

    mContainer(maxWidth = Breakpoint.xs) {
      renderableChild(MyRootR::class, myRoot)
    }
  }
}
