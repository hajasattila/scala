import Items.{Armor, Consumable, Equipment, Item, Placable, Weapon}

/**
 * Egy olyan case class, ami tartalmaza a világban lévő összes item típust
 * és az alkalmazható összes receptet
 */
case class GameRules(){

  private val Items: Vector[Item] = Vector(
    Armor(name = "Armor", 1, 5),
    Weapon(name = "Weapon", damage = 20)
  )

  /**
   * Visszaadja az összes Itemet amire igaz a predikátum
   * @param p predikátum
   */
  def getItems(p: Item => Boolean): Vector[Item] = Items.filter(p)

  /**
   * Visszaadja az Placable Itemek egy vektorát amire igaz a predikátum
   *
   * @param p predikátum
   */
  def getPlacables(p: Item => Boolean): Vector[Placable] = Items.collect { case placable: Placable if p(placable) => placable }

  /**
   * Visszaadja az Weapons Itemek egy vektorát amire igaz a predikátum
   *
   * @param p predikátum
   */
  def getWeapons(p: Item => Boolean): Vector[Weapon] = Items.collect { case w: Weapon if p(w) => w }

  /**
   * Visszaadja az ArmorsItemek egy vektorát amire igaz a predikátum
   *
   * @param p predikátum
   */
  def getArmors(p: Item => Boolean): Vector[Armor] = Items.collect { case a: Armor if p(a) => a }

  /**
   * Visszaadja az Equipments Itemek egy vektorát amire igaz a predikátum
   *
   * @param p predikátum
   */
  def getEquipments(p: Item => Boolean): Vector[Equipment] = Items.collect { case eq: Equipment if p(eq) => eq }

  /**
   * Visszaadja az Consumables Itemek egy vektorát amire igaz a predikátum
   *
   * @param p predikátum
   */
  def getConsumables(p: Item => Boolean): Vector[Consumable] = Items.collect { case c: Consumable if p(c) => c }
}