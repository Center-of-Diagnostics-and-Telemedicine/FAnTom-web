package model

import kotlinx.serialization.Serializable
import model.fantom.FantomResearchInitModel

@Serializable
data class ResearchInitResponse(
  val response: FantomResearchInitModel? = null,
  val error: ErrorModel? = null,
)