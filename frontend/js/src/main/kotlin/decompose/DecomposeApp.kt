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


class App() : RComponent<RProps, RState>() {

  private val lifecycle = LifecycleRegistry()
  private val ctx = DefaultComponentContext(lifecycle = lifecycle)

  val loginRepositoryImpl = LoginRepositoryImpl(
    local = LoginLocalDataSource,
    remote = LoginRemoteDataSource
  )
  val researchRepositoryImpl = MyResearchRepositoryImpl(
    local = ResearchLocalDataSource,
    remote = ResearchRemoteDataSource,
    token = loginRepositoryImpl.local::getToken
  )

  val mipRepositoryImpl = MyMipRepositoryImpl()
  val brightnessRepositoryImpl = MyBrightnessRepositoryImpl()
  val marksRepositoryImpl = MarksRepositoryImpl(
    remote = MarksRemoteDataSource,
    token = loginRepositoryImpl.local::getToken
  )

  val gridRepositoryImpl = GridRepositoryImpl()

  private val myRoot = MyRoot(ctx, dependencies = object : MyRoot.Dependencies {
    override val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory)
    override val loginRepository: LoginRepository = loginRepositoryImpl
    override val researchRepository: MyResearchRepository = researchRepositoryImpl
    override val mipRepository: MyMipRepository = mipRepositoryImpl
    override val brightnessRepository: MyBrightnessRepository = brightnessRepositoryImpl
    override val marksRepository: MarksRepository = marksRepositoryImpl
    override val gridRepository: GridRepository = gridRepositoryImpl
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
