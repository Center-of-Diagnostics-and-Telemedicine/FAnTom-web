package model

sealed class Grid {
  object Single : Grid()
  object TwoVertical : Grid()
  object TwoHorizontal : Grid()
  object Four : Grid()
}
