package decompose

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.lifecycle.LifecycleRegistry
import com.arkivanov.decompose.lifecycle.destroy
import com.arkivanov.decompose.lifecycle.resume
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.ccfraser.muirwik.components.mCssBaseline
import components.root.MyRoot
import decompose.myroot.MyRootUi
import local.LoginLocalDataSource
import local.ResearchLocalDataSource
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import remote.LoginRemoteDataSource
import remote.MarksRemoteDataSource
import remote.ResearchRemoteDataSource
import repository.*


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

  val mipRepositoryImpl = MipRepositoryImpl()
  val brightnessRepositoryImpl = BrightnessRepositoryImpl()
  val marksRepositoryImpl = MarksRepositoryImpl(
    remote = MarksRemoteDataSource,
    token = loginRepositoryImpl.local::getToken
  )

  private val myRoot = MyRoot(ctx, dependencies = object : MyRoot.Dependencies {
    override val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory)
    override val loginRepository: LoginRepository = loginRepositoryImpl
    override val researchRepository: ResearchRepository = researchRepositoryImpl
    override val mipRepository: MipRepository = mipRepositoryImpl
    override val brightnessRepository: BrightnessRepository = brightnessRepositoryImpl
    override val marksRepository: MarksRepository = marksRepositoryImpl
  })

  override fun componentDidMount() {
    lifecycle.resume()
  }

  override fun componentWillUnmount() {
    lifecycle.destroy()
  }

  override fun RBuilder.render() {
    mCssBaseline()
    renderableChild(MyRootUi::class, myRoot)
  }
}
