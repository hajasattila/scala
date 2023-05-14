package Items

/**
 * Egy olyan case class, amibe a dedikált armor találhatóak, ami az Item-ből extends-el
 * maximum egy Armor-ja lehet
 * @param name Item neve
 * @param maxStackSize inventory slot maxmimuma egy adott Itemnek
 * @param defense védelemre szolgál
 */
case class Armor(name: String, maxStackSize: Int = 1, defense: Int) extends Item