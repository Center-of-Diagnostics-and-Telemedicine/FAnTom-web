//package components.preset
//
//import com.arkivanov.decompose.ComponentContext
//import com.arkivanov.decompose.value.Value
//import com.arkivanov.mvikotlin.core.store.StoreFactory
//import components.mip.MipComponent
//import repository.MipRepository
//
//interface Preset {
//
//  val model: Value<Model>
//
//  fun onItemClick(mip: model.Mip)
//  fun onMipValueChanged(value: Int)
//
//  data class Model(
//    val items: List<model.Mip>,
//    val current: model.Mip,
//    val currentValue: Int? = null
//  )
//
//  interface Dependencies {
//    val storeFactory: StoreFactory
//    val mipRepository: MipRepository
//    val mipOutput: Consumer<Output>
//    val researchId: Int
//  }
//
//  sealed class Output {
//  }
//}
//
//@Suppress("FunctionName") // Factory function
//fun Mip(componentContext: ComponentContext, dependencies: Dependencies): Mip =
//  MipComponent(componentContext, dependencies)