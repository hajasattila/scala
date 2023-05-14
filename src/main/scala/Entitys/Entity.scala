package Entitys

import Effects.{Duration, Effect}

/**
 * Entity trait (interface)
 *
 */
trait Entity{
  /**
   * Entitás alapstatjainak visszaadasa
   */
  def baseStats(): EntityStats

  /**
   * Az entitás életerejének növelésére szolgáló metódus,
   * ha a hp negatív ne történjen semmi
   * @param hp ennyivel legyen több az életereje
   * @return A felhealhelt entitás vagy az original ha a hp negatív
   */
  def heal(hp: Int): Entity

  /**
   * Az entitás életerejének csökkenésére szolgálaló metódus,
   * @param hp ennyivel legyen kevesebb az életereje
   * @return ha pozítiv marad, akkor adjuk vissza Optionbe, de ha 0 vagy az alá csökken , akkor None
   */
  def takeDamage(hp: Int): Option[Entity]

  /**
   * Az entitás effect listájának a bővítésére való metódus,
   * ha az effekt nincs még az entitáson, akkor pluszba kerüljön hozzá
   * ha már rajta van, akkor meglévő és az új duration közül a nagyobb marad meg
   * @param effect amit hozzáadunk az entitáshoz
   * @param duration mennyi idő alatt
   * @return Entitás a frissített effect listával
   */
  def addEffect(effect: Effect, duration: Duration) : Entity

  /**
   * Effect a levétel az entitásról
   * @param p predikátum
   * @return Entitás a frissített effect listával
   */
  def removeEffects(p: Effect => Boolean): Entity

  /**
   * Entitás mozgatása egy másik pozira
   * @param position az a pozi, amire mozgatni szeretnénk az entitást
   * @return Entitás amely mozgatva lett az új oldal
   */
  def moveTo(position: Position): Entity

  /**
   * Adott esetben akár el is pusztulhat, ekkor az opcióban Nonet
   * adjunk vissza, egyébként a megváltoztatott entitást (opcióba csomagolva), a rajta lévő effektek durationje egy
   * tickkel csökken, a hp-je az effektekkel megváltoztatott regenerálódásnak megfelelően
   * változik. Ha épp lejár egy effekt, ami a maxHP-t növelte eddig,
   * akkor a HP már a kövi tickben az új maxHP fölé nem mehet, és ezt a felső korlátot
   * alkalmazzuk.
   * @return Hogy egy tickkel később mivé válik ez az entitás.
   */
  def tick() : Option[Entity]


}