package ru.mousecray.endmagic.runes

object RunePartEntryWrapper {

  implicit class RunePartEntryWrapper(entry: ((Int, Int), RunePart)) {
    def x = entry._1._1

    def y = entry._1._2

    def colorId = entry._2.colorId

    def partType = entry._2.partType

  }

}
