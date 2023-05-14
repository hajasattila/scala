package Entitys

import World.{Chest, ItemStack}
import Effects.{Duration, Effect}

/**
 * Player egy speciális entitás
 * @param name Player neve
 * @param id Player idje
 * @param baseStat A Player alap statjai
 * @param currentHP Player életereje
 * @param position Player pozija a térképen
 * @param activeEffect Playeren lévő aktív statok leképezése: Map(ahol a kulcs az effect és a hozzá tartozó duration az effect lejárati ideje)
 * @param capacity Az inventorynak a kapacitása
 * @param inventory Player inventory
 * @param equipmentSlots equipment inventory kapacitása
 * @param equipmentInventory Stacelkhető itemek inventory
 */
case class Player(name: String, id: String, baseStat: EntityStats, currentHP: Int, position: Position, capacity: Int, inventory: Chest, equipmentSlots: Int, equipmentInvertory:Chest ,activeEffect: Map[Effect, Duration], onCursor:Option[ItemStack] = None, respawnP: Position) extends Entity{

  val respawnPosition: Position = respawnP
  val reachingDistance: Double = 0.0

  override def baseStats(): EntityStats = this.baseStat

  /**
   * Felülírja az entitás életerejének növelésére szolgáló metódust,
   * ha a hp negatív ne történjen semmi
   *
   * @param hp ennyivel legyen több az életereje
   * @return A felhealhelt entitás vagy az original ha a hp negatív
   */
  override def heal(hp: Int): Entity = {
    if (hp < 0) this else {
      val maxHp = baseStat.maxHP
      val newHp = (this.currentHP + hp).min(maxHp)
      this.copy(currentHP = newHp)
    }
  }

  /**
   * Felülírja az entitás életerejének csökkenésére szolgálaló metódust,
   *
   * @param hp ennyivel legyen kevesebb az életereje
   * @return ha pozítiv marad, akkor adjuk vissza Optionbe, de ha 0 vagy az alá csökken , akkor None
   */
  override def takeDamage(hp: Int): Option[Entity] = {
    val newHP = currentHP - hp
    if (newHP > 0) {
      Some(copy(currentHP = newHP))
    } else {
      None
    }
  }

  /**
   * Felülírja az entitás effect listájának a bővítésére való metódust,
   * ha az effekt nincs még az entitáson, akkor pluszba kerüljön hozzá
   * ha már rajta van, akkor meglévő és az új duration közül a nagyobb marad meg
   *
   * @param effect   amit hozzáadunk az entitáshoz
   * @param duration mennyi idő alatt
   * @return Entitás a frissített effect listával
   */
  override def addEffect(effect: Effect, duration: Duration): Entity = {
    val updatedEffectMap = activeEffect.get(effect) match {
      case Some(existingDuration) =>
        val updatedDuration = if (duration.tick().exists(_.tick().isDefined)) duration else existingDuration
        activeEffect.updated(effect, updatedDuration)
      case None =>
        activeEffect.updated(effect, duration)
    }
    copy(activeEffect = updatedEffectMap)
  }


  /**
   * Felülírja az Effect a levétel az entitásról metódust
   *
   * @param p predikátum
   * @return Entitás a frissített effect listával
   */
  override def removeEffects(p: Effect => Boolean): Entity = {
    val updatedEffects = activeEffect.filterNot { case (effect, _) => p(effect) }
    copy(activeEffect = updatedEffects)
  }

  /**
   * Felülírja azEntitás mozgatása egy másik pozira metódust
   *
   * @param position az a pozi, amire mozgatni szeretnénk az entitást
   * @return Entitás amely mozgatva lett az új oldal
   */
  override def moveTo(position: Position): Entity = {
    this.copy(position = position)
  }

  /**
   * Felülírja az adott esetben akár el is pusztulhat, ekkor az opcióban Nonet
   * adjunk vissza, egyébként a megváltoztatott entitást (opcióba csomagolva), a rajta lévő effektek durationje egy
   * tickkel csökken, a hp-je az effektekkel megváltoztatott regenerálódásnak megfelelően
   * változik. Ha épp lejár egy effekt, ami a maxHP-t növelte eddig,
   * akkor a HP már a kövi tickben az új maxHP fölé nem mehet, és ezt a felső korlátot
   * alkalmazzuk.
   *
   * @return Hogy egy tickkel később mivé válik ez az entitás.
   */
  override def tick(): Option[Entity] = {
    val (active, _) = activeEffect.mapValues(_.tick()).partition(_._2.isDefined)
    val newActiveE = active.collect { case (k, Some(v)) => k -> v }
    val newHealth = (currentHP + baseStat.regeneration).min(baseStat.maxHP)
    val newE = this.copy(activeEffect = newActiveE.toMap, currentHP = newHealth.toInt)
    if (newHealth <= 0) None
    else Some(newE)
  }

  def peekCursor(amount: Int = 1): Option[ItemStack] = onCursor.flatMap(_.peek(amount))

  def isAlive(): Boolean = currentHP == 0

}
