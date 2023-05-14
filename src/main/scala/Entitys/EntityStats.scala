package Entitys

import Effects.Effect

/**
 * Egy élő mozgó entitás statjai
 * @param attack támadás
 * @param defense védekezés
 * @param speed sebesség
 * @param maxHP maximum életereje
 * @param regeneration regenárálás
 */
case class EntityStats(attack: Int, defense: Int, speed: Double, maxHP: Int, regeneration: Double){

  /**
   * Visszaadja az effect alapján módosított statokat
   * @param effect ez az effect alapján kerül módosításra az entitás
   * @return Módositás utáni entitás
   */
  def applyEffect(effect: Effect): EntityStats = {
    effect.apply(this)
    this
  }

  /**
   * Visszaadja a kapott effectek mindegyike alapján a módosult statokat
   * @param effect a kapott effect ez lehet egy vagy több is
   * @return Módositás utáni entitás statjai
   */
  def applyEffect(effect: Effect*): EntityStats = {
    effect.foreach(_.apply(this))
    this
  }
}